import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Assign1 {
    private static void fibonacci(int number) {
        int result = 0;
        if (number == 0 || number == 1) {
            result = 1;
        }
        else {
            int[] a_Fib = new int[number + 1];
            a_Fib[0] = 1;
            a_Fib[1] = 1;
            for (int i = 2; i <= number; i++) {
                a_Fib[i] = a_Fib[i - 1] + a_Fib[i - 2];
                result = a_Fib[i];
            }
            
        }
        System.out.printf("Fibonacci of %d is %d. \n", number, result);
    }
    private static BigInteger factorial(int number) {
        BigInteger result = new BigInteger("1");
        for (int i = number; i > 0; i--) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }
    private static void e(int iterations) {
        BigDecimal result = new BigDecimal("0");
        for (int n = 0; n < iterations; n++) {
            BigDecimal quotient = new BigDecimal("1");
            BigDecimal power = new BigDecimal("1");
            BigDecimal fact = new BigDecimal(factorial(n));
            power = power.pow(n);
            quotient = power.divide(fact, 16, RoundingMode.HALF_DOWN);
            result = result.add(quotient);
        }
        System.out.printf("Value of e using %d iterations is " + result + ".\n", iterations);
    }
    private static void help() {
        System.out.println("\n--- Assign 1 Help --- \n-fib [n] : Compute the Fibonacci of [n]; valid range [0, 40]\n-fac [n] : Compute the factorial of [n]; valid range, [0, 2147483647]\n-e [n] : Compute the value of 'e' using [n] iterations; valid range [1, 2147483647]\n");
    }
    
    public static void main (String[] args) {
        for (int arg = 0; arg < args.length; arg++) {
            if (args[arg].equals("-fib")) {
                if ((arg + 1) != args.length) {
                    if (args[arg + 1].charAt(0) == '-') {
                        if (args[arg + 1].charAt(1) >= 48 && args[arg + 1].charAt(1) <= 57) {
                            System.out.print("Invalid Number try again. Valid Fibonacci range is [0, 40].\n");
                        }
                        else {
                            help();
                        }
                    }
                    else if (args[arg + 1].charAt(0) >= 48 && args[arg + 1].charAt(0) <= 57) {
                        boolean isInteger = true;
                        for (int i = 0; i < args[arg + 1].length(); i++) {
                            if (args[arg + 1].charAt(i) < 48 || args[arg + 1].charAt(i) > 57) {
                                isInteger = false;
                            }
                        }
                        if (isInteger) {
                            boolean valid = true;
                            boolean helpTriggered = false;
                            int number = Integer.parseInt(args[arg + 1]);
                            if ((arg + 2) != args.length) {
                                if (args[arg + 2].charAt(0) >= 48 && args[arg + 2].charAt(0) <= 57){
                                    help();
                                    valid = false;
                                    helpTriggered = true;
                                }
                                else if (args[arg + 2].charAt(0) == '-') {
                                    if (args[arg + 2].charAt(1) >= 48 && args[arg + 2].charAt(1) <= 57) {
                                        help();
                                        valid = false;
                                        helpTriggered = true;
                                    }
                                }
                            }
                            if (number >= 0 && number <= 40 && valid) {
                                fibonacci(number);
                            }
                            else if (!helpTriggered) {
                                System.out.print("Invalid number try again. Valid Fibonacci range is [0, 40].\n");
                            }
                        }
                        else {
                            help();
                        }
                    }
                }
                else {
                    help();
                }
            }
            else if (args[arg].equals("-fac")) {
                if ((arg + 1) != args.length) {
                    if (args[arg + 1].charAt(0) == '-') {
                        if (args[arg + 1].charAt(1) >= 48 && args[arg + 1].charAt(1) <= 57) {
                            System.out.print("Invalid number try again. Valid Factorial range is [0, 2147483647].\n");
                        }
                        else {
                            help();
                        }
                    }
                    else if (args[arg + 1].charAt(0) >= 48 && args[arg + 1].charAt(0) <= 57) {
                        boolean isInteger = true;
                        for (int i = 0; i < args[arg + 1].length(); i++) {
                            if (args[arg + 1].charAt(i) < 48 || args[arg + 1].charAt(i) > 57) {
                                isInteger = false;
                            }
                        }
                        if (isInteger) {
                            boolean valid = true;
                            boolean helpTriggered = false;
                            int number = Integer.parseInt(args[arg + 1]);
                            if ((arg + 2) != args.length) {
                                if (args[arg + 2].charAt(0) >= 48 && args[arg + 2].charAt(0) <= 57){
                                    help();
                                    valid = false;
                                    helpTriggered = true;
                                }
                                else if (args[arg + 2].charAt(0) == '-') {
                                    if (args[arg + 2].charAt(1) >= 48 && args[arg + 2].charAt(1) <= 57) {
                                        help();
                                        valid = false;
                                        helpTriggered = true;
                                    }
                                }
                            }
                            if (number >= 0 && number <= 2147483647 && valid) {
                                System.out.printf("Factorial of %d is %d. \n", number, factorial(number));
                            }
                            else if (!helpTriggered) {
                                System.out.print("Invalid number try again. Valid Factorial range is [0, 2147483647].\n");
                            }
                        }
                        else {
                            help();
                        }
                    }
                }
                else {
                    help();
                }
            }
            else if (args[arg].equals("-e")) {
                if ((arg + 1) != args.length) {
                    if (args[arg + 1].charAt(0) == '-') {
                        if (args[arg + 1].charAt(1) >= 48 && args[arg + 1].charAt(1) <= 57) {
                            System.out.print("Invalid number try again. Valid e iterations range is [1, 2147483647].\n");
                        }
                        else {
                            help();
                        }
                    }
                    else if (args[arg + 1].charAt(0) >= 48 && args[arg + 1].charAt(0) <= 57) {
                        boolean isInteger = true;
                        for (int i = 0; i < args[arg + 1].length(); i++) {
                            if (args[arg + 1].charAt(i) < 48 || args[arg + 1].charAt(i) > 57) {
                                isInteger = false;
                            }
                        }
                        if (isInteger) {
                            boolean valid = true;
                            boolean helpTriggered = false;
                            int number = Integer.parseInt(args[arg + 1]);
                            if ((arg + 2) != args.length) {
                                if (args[arg + 2].charAt(0) >= 48 && args[arg + 2].charAt(0) <= 57){
                                    help();
                                    valid = false;
                                    helpTriggered = true;
                                }
                                else if (args[arg + 2].charAt(0) == '-') {
                                    if (args[arg + 2].charAt(1) >= 48 && args[arg + 2].charAt(1) <= 57) {
                                        help();
                                        valid = false;
                                        helpTriggered = true;
                                    }
                                }
                            }
                            if (number >= 0 && number <= 2147483647 && valid) { 
                                e(number);
                            }
                            else if (!helpTriggered) {
                                System.out.printf("Invalid number try again. Valid e iterations range is [1, 2147483647].\n");
                            }
                        }
                        else {
                            help();
                        }
                    }
                }
                else {
                    help();
                }
            }
            else {
                if (args[arg].charAt(0) < 48 || args[arg].charAt(0) > 57) {
                    if (args[arg].charAt(0) == '-' && args[arg].length() > 1) {
                        if (args[arg].charAt(1) < 48 || args[arg].charAt(1) > 57) {
                            System.out.printf("Unknown command line argument: %s\n", args[arg]);
                        }
                    }
                    else {
                        System.out.printf("Unknown command line argument: %s\n", args[arg]);
                    }
                }
                else if (arg == 0) {
                    if (args[arg].charAt(0) == '-' && args[arg].length() > 1) {
                        if (args[arg].charAt(1) >= 48 && args[arg].charAt(1) <= 57) {
                            System.out.printf("Unknown command line argument: %s\n", args[arg]);
                        }
                    }
                    else if (args[arg].charAt(0) >= 48 && args[arg].charAt(0) <= 57) {
                        System.out.printf("Unknown command line argument: %s\n", args[arg]);
                    }
                }
            }
        }
    }
}