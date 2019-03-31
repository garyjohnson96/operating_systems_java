import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class Assign3 {
    private static ArrayList<String> history = new ArrayList<>();
    private static String[] commands;
    private static String workingDirectory;
    private static double totalChildProcessTime;
    private static boolean running = true;
    private static boolean isInteger;

    private static double ptime() {
        return totalChildProcessTime;
    }

    private static void calculateTotalTime(long start, long end) {
        double totalTime = end - start;
        totalTime /= 1000;
        totalChildProcessTime += totalTime;
    }

    private static void printHistory() {
        System.out.println("--- Command History ---");
        for (int command = 0; command < history.size(); command++) {
            System.out.println((command + 1) + " : " + history.get(command));
        }
    }

    private static void saveToHistory(String command) {
        history.add(command);
    }

    private static void list() {
        File currentDirectory = new File(System.getProperty("user.dir"));
        File[] listOfFiles = currentDirectory.listFiles();
        String dirMarker;   //Marks a file as a directory
        String readMarker;  //Marks a file as readable
        String writeMarker; //Marks a file as writeable
        String exeMarker;   //Marks a file as executable

        System.out.println();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                Path myPath = Paths.get(String.valueOf(file));
                long bytes = 0;
                try {
                    bytes = Files.size(myPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (file.isDirectory()) {
                    dirMarker = "d";
                } else {
                    dirMarker = "-";
                }
                if (file.canRead()) {
                    readMarker = "r";
                } else {
                    readMarker = "-";
                }
                if (file.canWrite()) {
                    writeMarker = "w";
                } else {
                    writeMarker = "-";
                }
                if (file.canExecute()) {
                    exeMarker = "x";
                } else {
                    exeMarker = "-";
                }
                String pattern = "MMM dd, yyyy HH:mm";
                String lastModified = new SimpleDateFormat(pattern).format(new Date(file.lastModified()));
                System.out.printf(dirMarker + readMarker + writeMarker + exeMarker + "%10s %20s %22s", bytes, lastModified, file.getName() + "\n");
            }
        }
        System.out.println();
    }

    private static void changeDirectory(File path) {
        String currentDir = System.getProperty("user.dir");
        File fileDir = new File(currentDir);

        System.setProperty("user.dir", path.toString());
        workingDirectory = System.getProperty("user.dir");

        if (path.toString().equals("..")) {
            System.setProperty("user.dir", fileDir.getParent());
            workingDirectory = System.getProperty("user.dir");
        }
    }

    private static void changeToHomeDirectory() {
        File directory;     // Desired current working directory
        directory = new File(System.getProperty("user.home")).getAbsoluteFile();
        if (directory.exists() || directory.mkdirs()) {
            System.setProperty("user.dir", directory.getAbsolutePath());
        }
        workingDirectory = System.getProperty("user.dir");
    }

    private static void pipe(String command1, String command2) {  //user enters '|' as a command

    }

    private static boolean isBuiltInCommand(String command) {
        if (command.equals("history")) {
            return true;
        } else if (command.charAt(0) == '^') {   //command.equals("^ " + index)) {
            return true;
        } else if (command.equals("cd")) {
            return true;
        } else if (command.equals("ptime")) {
            return true;
        } else if (command.equals("|")) {
            return true;
        } else return command.equals("list");
    }

    private static boolean runExternalCommand(ArrayList<String> exCommands) {
        ProcessBuilder pb = new ProcessBuilder(exCommands);
        pb.redirectErrorStream(true);
        pb.inheritIO();
        try {
            long startTime = System.currentTimeMillis();
            Process p = pb.start();
            p.waitFor();
            long endTime = System.currentTimeMillis();
            calculateTotalTime(startTime, endTime);
            return true;
        } catch (IOException | InterruptedException ex) {
            return false;
        }
    }

    private static void runPreviousCommand(int number) {  // ^ <number>
        String command = history.get(number - 1);

        if (command.charAt(0) == '^') {
            int index = command.charAt(2) - '0';
            runPreviousCommand(index);
        }
        else {
            runCommandFromHistory(number - 1);
        }
    }

    private static void runCommandFromHistory(int cmnd) {
        if (history.get(cmnd).equals("history")) {
            printHistory();
        }
        else if (history.get(cmnd).equals("cd")) {
            changeToHomeDirectory();
        }
        else if (history.get(cmnd).contains("cd ")) {
            StringBuilder dirName = new StringBuilder();
            for (int letterInDirName = 3; letterInDirName < history.get(cmnd).length(); letterInDirName++) {
                dirName.append(history.get(cmnd).charAt(letterInDirName));
            }
            File path = new File(dirName.toString());
            File[] childFiles = new File(workingDirectory).listFiles();
            boolean changed = false;
                if (path.toString().equals("..")) {
                    changeDirectory(path);
                    changed = true;
                } else if (childFiles != null) {
                    for (File childFile : childFiles) {
                        if (path.getAbsolutePath().equals(childFile.toString())) {
                            path = childFile;
                            changeDirectory(path);
                            changed = true;
                        }
                    }
                }
                if (!changed) {  //If directory was unable to be changed
                    System.out.println("The system cannot find the path specified.");
                }
        }
        else if (history.get(cmnd).equals("|")) {
            System.out.println("Piping! Does nothing");
            if (!"|".equals(commands[0])) {
                pipe(history.get(cmnd - 1), history.get(cmnd + 1));
                System.out.println("Piping " + history.get(cmnd - 1) + " through " + history.get(cmnd + 1) + "!");
            }
        } else if (history.get(cmnd).equals("list")) {
            list();
        }
        else if (history.get(cmnd).equals("ptime")) {
            System.out.printf("Total time in child processes: %.4f seconds.\n", ptime());
        }
    }

    private static void runCommand(int command) {
        switch (commands[command]) {
            case "history":
                saveToHistory(commands[command]);
                printHistory();
                break;
            case "cd":
                int fileName = command + 1;
                if (fileName < commands.length) {
                    File path = new File(commands[fileName]);
                    boolean isChild = false;
                    File[] childFiles = new File(workingDirectory).listFiles();

                    if (childFiles != null) {
                        for (File childFile : childFiles) {
                            if (path.getAbsolutePath().equals(childFile.toString())) {
                                path = childFile;
                                isChild = true;
                            }
                        }
                    }
                    if (path.toString().contains(".")) {
                        if (path.toString().equals("..")) {  //if path exists as parent directory
                            changeDirectory(path);
                            saveToHistory(commands[command] + " " + commands[fileName]);
                        }
                        commands[fileName] = "";
                    } else if (isChild) {   // if path exists as subdirectory
                        changeDirectory(path);
                        saveToHistory(commands[command] + " " + commands[fileName]);
                        commands[fileName] = "";
                    } else {   //fileName wasn't separate command or actual path
                        System.out.println("The system cannot find the path specified.");
                        changeToHomeDirectory();
                        saveToHistory(commands[command]);
                    }
                } else {
                    changeToHomeDirectory();
                    saveToHistory(commands[command]);
                }
                break;
            case "|":
                System.out.println("Piping! Does nothing");
                if (!"|".equals(commands[0])) {
                    pipe(commands[command - 1], commands[command + 1]);
                    System.out.println("Piping " + commands[command - 1] + " through " + commands[command + 1] + "!");
                    saveToHistory(commands[command]);
                }
                break;
            case "list":
                list();
                saveToHistory(commands[command]);
                break;
            case "ptime":
                System.out.printf("Total time in child processes: %.4f seconds.\n", ptime());
                saveToHistory(commands[command]);
                break;
            case "^":
                if (command + 1 < commands.length) {
                    if (commands[command + 1].charAt(0) >= 48 && commands[command + 1].charAt(0) <= 57) {
                        isInteger = true;
                        for (int i = 0; i < commands[command + 1].length(); i++) {
                            if (commands[command + 1].charAt(i) < 48 || commands[command + 1].charAt(i) > 57) {
                                isInteger = false;
                            }
                        }
                        if (isInteger) {
                            int index = Integer.parseInt(commands[command + 1]);
                            if (index >= 1 && index <= history.size()) {  //bounds checking for history index
                                runPreviousCommand(index);
                                saveToHistory(commands[command] + " " + index);
                                commands[command + 1] = "";
                            } else {
                                System.out.println("Invalid index");
                            }
                        }
                    } else {
                        System.out.println("Invalid index");
                    }
                } else {
                    System.out.println("Syntax Error: ^ must also include a number and be in the form ^ <number>.");
                }
                break;
            case "exit":
                running = false;
                break;
            case "":
                break;
            default:
                ArrayList<String> externalCommands = new ArrayList<>();
                externalCommands.add(commands[command]);
                if (command + 1 < commands.length) {
                    for (int nextArg = command + 1; nextArg < commands.length; nextArg++) {
                        if (!isBuiltInCommand(commands[nextArg])) {
                            externalCommands.add(commands[nextArg]);
                            commands[nextArg] = "";
                        } else {
                            break;
                        }
                    }
                }
                StringBuilder externalCommand = new StringBuilder();
                for (String exCmd : externalCommands) {
                    externalCommand.append(exCmd);
                    externalCommand.append(" ");
                }
                saveToHistory(externalCommand.toString().trim());
                if (!runExternalCommand(externalCommands)) {
                    System.out.printf("Invalid command: %s. \n", commands[command]);
                }
                break;
        }
    }

    public static void main(String[] args) {
        workingDirectory = System.getProperty("user.dir");

        Scanner in = new Scanner(System.in);

        while (running) {
            String prompt = "[" + workingDirectory + "]: ";
            System.out.print(prompt);

            String input = in.nextLine();
            commands = input.split(" ");

            for (int command = 0; command < commands.length; command++) {
                isInteger = false;      //Used for indicating if the index selected for ^ <index> is an integer
                if (command + 1 < commands.length) {
                    if (commands[command + 1].startsWith("\"")) {
                        boolean found = false;
                        int directoryNameCount = 2;
                        while (!found) {
                            if (command + directoryNameCount < commands.length) {
                                if (commands[command + directoryNameCount].endsWith("\"")) {
                                    found = true;
                                } else {
                                    directoryNameCount++;
                                }
                            }
                        }
                        StringBuilder fullDirName = new StringBuilder();
                        for (int name = 0; name < directoryNameCount; name++) {
                            fullDirName.append(commands[command + 1 + name]).append(" ");
                            commands[command + 1 + name] = "";
                        }
                        String temp = fullDirName.toString().replace("\"", " ");
                        commands[command + 1] = temp.trim();
                    }
                }
                if (!commands[command].equals("")) {
                    runCommand(command);
                }
            }
        }
    }
}

