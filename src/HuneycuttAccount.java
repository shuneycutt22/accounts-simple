/**
 * The HuneycuttAccount class maintains information about a bank account,
 * including the account holder's name, account number, and current balance.
 * The class allows users to modify these values and to make deposits and withdrawals.
 *
 * Created for CSC 151 (MON02), Summer 2020, Guilford Technical Community College.
 *
 * I abide by GTCC's academic integrity policy and certify that this is my own, original work.
 *
 * @author Sam Huneycutt
 * @version 2020.7.10
 */
public class HuneycuttAccount {
    /** Account number for account */
    private long accountNumber;

    /** Account holder's first name */
    private String firstName;

    /** Account holder's last name */
    private String lastName;

    /** Current account balance */
    private double balance;

    /**
     * Constructor for class HuneycuttAccount
     * @param accountNumber account holder's account number
     * @param firstName account holder's first name
     * @param lastName account holder's last name
     * @param balance current account balance
     */
    public HuneycuttAccount(long accountNumber, String firstName, String lastName, double balance) {
        this.accountNumber = accountNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
    }

    /**
     * Retrieve account number
     * @return current account number
     */
    public long getAccountNumber() {
        return accountNumber;
    }

    /**
     * Set a new account number
     * @param accountNumber new account number
     */
    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Retrieve account holder's first name
     * @return first name in String format
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set account holder's first name
     * @param firstName new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Retrieve account holder's last name
     * @return last name in String format
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set account holder's last name
     * @param lastName new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Get current account balance
     * @return current account balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Manually set account balance.  Warning!: deposit and withdrawal should be used
     * on existing accounts to ensure that a valid transaction has taken place
     * @param balance new account balance
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Make a deposit to the account. Checks if amount is allowed before depositing
     * @param amount amount to deposit
     * @return true if deposit successful, false if deposit failed
     */
    public boolean deposit(double amount) {
        if (amount > 10 && amount < 3000) {
            this.balance += amount;
            return true;
        }
        else return false;
    }

    /**
     * Withdraw money from the account. Checks if funds are available before withdrawing
     * @param amount amount to withdraw
     * @return true if withdraw successful, false if withdrawal failed
     */
    public boolean withdrawal(double amount) {
        if(balance - amount >= 10) {
            balance -= amount;
            return true;
        }
        else return false;
    }

    /**
     * Get string representation of object in JSON format
     * @return String representation of object
     */
    @Override
    public String toString() {
        return "HuneycuttAccount{" +
                "accountNumber=" + accountNumber +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", balance=" + balance +
                '}';
    }
}