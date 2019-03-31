public class Assign2 {

    public static void main(String[] args) {
        System.out.println("Enter some commands: ");
        for (int arg = 0; arg < args.length; arg++) {
            if (args[arg].equals("-cpu")) {
                int processsors = Runtime.getRuntime().availableProcessors();
                System.out.println("Processors          :   " + processsors);
                System.out.println("");
            }
            else if (args[arg].equals("-java")) {
                System.out.println("Java Vendor         :   " + System.getProperty("java.vm.vendor"));
                System.out.println("Java Runtime        :   " + System.getProperty("java.runtime.name"));
                System.out.println("Java Version        :   " + System.getProperty("java.version"));
                System.out.println("Java VM Version     :   " + System.getProperty("java.vm.version"));
                System.out.println("Java VM Name        :   " + System.getProperty("java.vm.name"));
                System.out.println("");
            }
            else if (args[arg].equals("-mem")) {
                System.out.println("Free Memory         :   " + Runtime.getRuntime().freeMemory());
                System.out.println("Max Memory          :   " + Runtime.getRuntime().maxMemory());
                System.out.println("Total Memory        :   " + Runtime.getRuntime().totalMemory());
                System.out.println("");
            }
            else if (args[arg].equals("-os")) {
                System.out.println("OS Name             :   " + System.getProperty("os.name"));
                System.out.println("OS Version          :   " + System.getProperty("os.version"));
                System.out.println("");
            }
            else if (args[arg].equals("-dirs")) {
                System.out.println("User Home Directory :   " + System.getProperty("user.home"));
                System.out.println("Working Directory   :   " + System.getProperty("user.dir"));
                System.out.println("");
            }
            else {
                System.out.printf("Unrecognized command %s\n\n", args[arg]);
            }
        }
    }
}
