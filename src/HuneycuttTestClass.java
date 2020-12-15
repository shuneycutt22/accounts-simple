import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This class is designed to test the HuneycuttAccount class.
 *
 * Created for CSC 151 (MON02), Summer 2020, Guilford Technical Community College.
 *
 * I abide by GTCC's academic integrity policy and certify that this is my own, original work.
 *
 * @author Sam Huneycutt
 * @version 2020.7.17
 */
public class HuneycuttTestClass {
    /**
     * Tests HuneycuttAccount class by creating array of HuneycuttAccount objects,
     * initializing with data from a text file, performing account transactions.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        //maximum number of accounts to import
        final int MAX_ACCOUNTS = 10;

        //location of text file containing account data
        final String PATHNAME = "./src/BankData.txt";

        //location of text file for logs
        final String LOGFILE = "./src/HuneycuttErrorLog.txt";

        //set up log file
        PrintWriter log = createLog(LOGFILE);
        log.println("session started " + LocalDateTime.now().format(DateTimeFormatter
                .ofPattern("uuuu/MM/dd HH:mm:ss")));
        log.flush();

        //create array for HuneycuttAccount objects
        var accounts = new HuneycuttAccount[MAX_ACCOUNTS];

        //load data from text file and populate array with HuneycuttAccount objects
        try {
            getData(accounts, PATHNAME, log);
        }
        //if FileNotFoundException occurs, print message to console and halt execution
        //program cannot continue without a file
        catch (FileNotFoundException ex) {
            log.println("Account data file not found!");
            log.println(LocalDateTime.now().format(DateTimeFormatter
                    .ofPattern("uuuu/MM/dd HH:mm:ss")));
            log.flush();
            ex.printStackTrace();
            System.out.println("Exiting program...");
            System.exit(1);
        }

        //print String for each account in the array
        displayAccounts(accounts);
        System.out.println();

        //process accounts, repeating until user is finished
        System.out.print("Would you like to make a transaction? (y/n): ");
        boolean again = getYesOrNo();
        while (again) {
            accountProcessing(accounts, log);
            System.out.print("Would you like to make a transaction on any other accounts? (y/n): ");
            again = getYesOrNo();
        }

        //print String for each account in the array
        System.out.println();
        displayAccounts(accounts);

        //print exit message
        System.out.println();
        System.out.println("Have a good day!");

        //close logfile
        log.println("session ended " + LocalDateTime.now().format(DateTimeFormatter
                .ofPattern("uuuu/MM/dd HH:mm:ss")));
        log.flush();        //write logs to file
        log.close();
    }

    /**
     * Sets up a log file at the specified pathname and a PrintWriter object to write to the file
     * @param pathname location to create log file
     * @return PrintWriter object associated with log file
     */
    public static PrintWriter createLog(String pathname) {
        //create File object for log file
        var logfile = new File(pathname);

        //create the file
        try {
            logfile.createNewFile();
            return new PrintWriter(logfile);        //create a PrintWriter for the new file
        }
        //if the file cannot be created, warn the user
        catch (IOException ex) {
            System.out.println("WARNING: this session not writing to logfile. Logs will be printed on the console.");
            //create a PrintWriter for System.out so that logs can be output to the console
            return new PrintWriter(System.out, true);
        }
    }

    /**
     * Opens text file, populates HuneycuttAccount array with data from file until array is full or
     * no data is left in file.
     * Handles ArrayIndexOutOfBoundsException
     * @param accounts array for HuneycuttAccount objects to be initialized
     * @param pathname location of text file
     * @param log PrintWriter for logfile
     * @throws FileNotFoundException thrown if text file at pathname does not exist or cannot be read
     */
    public static void getData(HuneycuttAccount[] accounts, String pathname, PrintWriter log)
            throws FileNotFoundException {

        //create File object from text file
        var file = new File(pathname);

        //declare variable for Scanner object
        Scanner input;

        //create Scanner to read data from text file
        try {
            input = new Scanner(file);
            log.println("Importing data from file...");
        }
        //if FileNotFoundException thrown, throw exception to caller
        catch (FileNotFoundException ex) {
            throw new FileNotFoundException("File \"" + pathname + "\" not found");
        }

        int successes = 0;      //number of successful record imports

        //import data into array until array full or no data left
        for(int i = 0, line = 1; input.hasNext(); i++, line++) {
            try {
                //data in text file is in format: firstName lastName accountNumber balance
                //Each line contains one entry
                //InputMismatchExceptionPossible
                String firstName = input.next();
                String lastName = input.next();
                long accountNumber = input.nextLong();
                double balance = input.nextDouble();

                //create HuneycuttAccount object from data. Assign to array at index i
                //ArrayIndexOutOfBoundsException possible
                accounts[i] = new HuneycuttAccount(accountNumber, firstName, lastName, balance);
                successes++;
            }
            //if InputMismatchException, skip entire data entry
            catch (InputMismatchException ex) {
                log.println("Input mismatch at line " + line + ": Account skipped");    //log error message
                input.nextLine();       //skip line in text file
                i--;                    //decrement array index since no data stored in array (for-loop will increment)
            }
            //if ArrayIndexOutofBoundsException, array is full. Break out of for-loop.
            catch (ArrayIndexOutOfBoundsException ex) {
                //log error message
                log.println("Account data array full: Accounts at line " + line + " and later will not be imported");
                //exit for-loop
                break;
            }
        }
        //close file when done reading
        input.close();

        //log how many records were imported
        log.println(successes + " records successfully imported");
        log.flush();        //write logs to file
    }

    /**
     * Print the toString() for every object in the array to the console.
     * (Not limited to HuneycuttAccount object arrays)
     * @param objectArray array of Objects
     */
    public static void displayAccounts(Object[] objectArray) {
        for(Object oneObject : objectArray) {
            if(oneObject != null) {
                System.out.println(oneObject.toString());
            }
        }
    }

    /**
     * Presents user with options to manage accounts
     * @param accounts array of HuneycuttAccount objects to manage
     * @param log PrintWriter for log file
     */
    public static void accountProcessing(HuneycuttAccount[] accounts, PrintWriter log) {
        //get account number from user
        System.out.print("Please enter your account number: ");
        int accountNumber = getInt();

        //search for account number in array
        HuneycuttAccount account = searchForAccount(accountNumber, accounts);

        //if account exists, let user make transactions
        //keep making transactions until user is finished
        if (account != null) {
            boolean again = true;
            while(again) {
                doTransaction(account, log);
                System.out.print("Would you like to make another transaction on account " +
                        accountNumber + "? (y/n): ");
                again = getYesOrNo();
            }
        }
        //if account does not exist, print error message
        else {
            log.println("Account " + accountNumber + " not found");
            System.out.println("Sorry, account " + accountNumber + " not found");
        }
    }

    /**
     * Searches account array by account number
     * @param accountNumber account number to search for
     * @param accounts array of HuneycuttAccount objects to search
     * @return matching account in array. Returns null if no matching account found.
     */
    public static HuneycuttAccount searchForAccount(int accountNumber, HuneycuttAccount[] accounts) {
        //linear search account array for account matching accountNumber
        for (HuneycuttAccount account : accounts) {
            if (accountNumber == account.getAccountNumber())
                return account;
        }
        return null;
    }

    /**
     * Handles transactions involving a HuneycuttAccount object. Presents the user with options
     * to make deposits and withdrawals to their account.
     * @param account HuneycuttAccount object to use for transactions
     * @param log PrintWriter for log file
     */
    public static void doTransaction(HuneycuttAccount account, PrintWriter log) {
        //create Scanner to receive input from user
        var input = new Scanner(System.in);

        //prompt user to select deposit, withdrawal, or exit
        System.out.println("Would you like to make a deposit or a withdrawal?");
        System.out.print("Enter \"d\" for deposit, \"w\" for withdrawal, or \"0\" to exit: ");
        boolean success = false;
        String answer = "";
        //continue prompting until a valid option is selected
        while (!success) {
            answer = input.next();
            switch (answer) {
                case "d":
                case "w":
                    success = true;
                    break;
                case "0":
                    return;
                default:
                    success = false;
                    System.out.print("Please enter \"d\" for deposit, \"w\" for withdrawal, or \"0\" to exit: ");
                    break;
            }
        }

        //make a deposit or a withdrawal
        //application error if deposit or withdraw is not selected
        double amount;
        switch (answer) {//prompt for amount to deposit
        //if deposit successful, print success message
            case "d":
                System.out.print("Enter amount to deposit: ");
                amount = getDouble();
                if (account.deposit(amount)) {
                    log.println("Deposited " + amount + " to account " + account.getAccountNumber());
                    System.out.println("Successfully deposited " + amount);
                }
                //if deposit failed, print error message
                else {
                    log.println("Deposit to account " + account.getAccountNumber() + " failed: amount not allowed");
                    System.out.println("Deposit failed: amount not allowed");
                }
                break;
//prompt for amount to withdraw
//if withdrawal successful, print success message
            case "w":
                System.out.print("Enter amount to withdraw: ");
                amount = getDouble();
                if (account.withdrawal(amount)) {
                    log.println("Withdrew " + amount + " from account " + account.getAccountNumber());
                    System.out.println("Successfully withdrew " + amount);

                }
                //if withdrawal failed, print error message
                else {
                    log.println("Withdrawal from account " + account.getAccountNumber() + " failed: " +
                            "insufficient funds");
                    System.out.println("Withdrawal failed: insufficient funds");
                }
                break;
            default:
                log.println("Internal application error in method \"transaction\"");
                System.out.println("Unknown transaction error. Please try again.");
                break;
        }
        log.flush();        //write logs to file
    }

    /**
     * Handles input from user for yes/no questions. Loops until user enters "y" or "n".
     * @return true if user entered "y". false if user entered "n".
     */
    public static boolean getYesOrNo() {
        //create Scanner to receive input from user
        var input = new Scanner(System.in);

        //prompt user until acceptable value is entered
        //loop can only be exited by return statements
        while (true) {
            var answer = input.next();
            switch (answer) {
                case "y":
                case "Y":
                    return true;
                case "n":
                case "N":
                    return false;
                default:
                    System.out.print("Please enter \"y\" for yes or \"n\" for no: ");
                    break;
            }
        }
    }

    /**
     * Gets a double value from the user. Loops until user enters a number.
     * @return user-entered number
     */
    public static double getDouble() {
        //create Scanner to receive input from user
        var input = new Scanner (System.in);

        //initialize variable for input value
        double answer = 0;

        //prompt user for double
        //keep asking until acceptable value is entered
        boolean success = false;
        while (!success) {
            try {
                answer = input.nextDouble();
                success = true;
            } catch (InputMismatchException ex) {
                System.out.print("Incorrect format. Please enter a number: ");
                success = false;
                input.next();
            }
        }
    return answer;
    }

    /**
     * Gets an int value from the user. Loops until user enters a number.
     * @return user-entered number
     */
    public static int getInt() {
        //create Scanner to receive input from user
        var input = new Scanner(System.in);

        //initialize variable for input value
        int answer = 0;

        //prompt user for int
        //keep asking until acceptable value is entered
        boolean success = false;
        while (!success) {
            try {
                answer = input.nextInt();
                success = true;
            } catch (InputMismatchException ex) {
                System.out.print("Incorrect format. Please enter a number: ");
                success = false;
                input.next();
            }
        }
        return answer;
    }

}
