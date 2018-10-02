package paystation.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;



/**
 * Implementation of the pay station.
 *
 * Responsibilities:
 *
 * 1) Accept payment; 2) Calculate parking time based on payment; 3) Know
 * earning, parking time bought; 4) Issue receipts; 5) Handle buy and cancel
 * events.
 *
 * This source code is from the book "Flexible, Reliable Software: Using
 * Patterns and Agile Development" published 2010 by CRC Press. Author: Henrik B
 * Christensen Computer Science Department Aarhus University
 *
 * This source code is provided WITHOUT ANY WARRANTY either expressed or
 * implied. You may study, use, modify, and distribute it for non-commercial
 * purposes. For any commercial use, see http://www.baerbak.com/
 */
public class PayStationImpl implements PayStation {

    private int insertedSoFar;
    private int timeBought;
    Map<Integer, Integer> coinMap = new HashMap<>();
    private int totalcoins = 0;
    private int n;
    private int q;
    private int d;
    private int rate = 1;
    private int userSelection;
    Scanner keyboard = new Scanner(System.in);

    public static void main(String[] args) throws IllegalCoinException{
        //System.out.println("Hello World!");
        PayStationImpl test = new PayStationImpl(); //new instance of the class, for testing purposes
        test.payStationLoop(); //runs the main display loop
    }
    
    public void blankMethod(){
        
    }
    
    //initiate map object to zero
    public void initCoinsDepositedMap() {
        coinMap.put(5, 0);
        coinMap.put(10, 0);
        coinMap.put(25, 0);
    }
    
    public Map<Integer, Integer> returnCoinMap(){
        return coinMap;
    }

    @Override
    public void addPayment(int coinValue)
            throws IllegalCoinException {
        if (insertedSoFar == 0) {
            initCoinsDepositedMap();
        }

        switch (coinValue) {
            case 5:
                //retuns value for given key, adds 1 to it, puts it back into map
                coinMap.put(5, coinMap.get(5) + 1);                
                break;
            case 10:
                coinMap.put(10, coinMap.get(10) + 1);                
                break;
            case 25:
                coinMap.put(25, coinMap.get(25) + 1);                 
                break;
            default:
                throw new IllegalCoinException("Invalid coin: " + coinValue);
        }
        
        insertedSoFar += coinValue;
        
        switch (rate){
            case 1:
                timeBought = insertedSoFar / 5 * 2;
                break;
            case 2:
                //progressive rate
                break;
            case 3:
                //alternating rate
                break;
                
        }
        timeBought = insertedSoFar / 5 * 2;
    }

    @Override
    public int readDisplay() {
        return timeBought;
    }

    @Override
    public Receipt buy() {
        Receipt r = new ReceiptImpl(timeBought);
        totalcoins += insertedSoFar;
        reset();
        
        //Resets the coinMap to all 0 values
        initCoinsDepositedMap();
        
        return r;
    }

    @Override
    public Map<Integer, Integer> cancel() {
        
        if (coinMap.get(5) == 0) {
            coinMap.remove(5);
        }
        if (coinMap.get(10) == 0) {
            coinMap.remove(10);
        }
        if (coinMap.get(25) == 0) {
            coinMap.remove(25);
        }        

        //Creates new map and makes a copy of the current coinMap
        Map<Integer, Integer> coinMapCopy = new HashMap<>();
        coinMapCopy.putAll(coinMap);

        //Resets the coinMap to all 0 values
        initCoinsDepositedMap();
        reset();

        //returns the coinMap copy so as to return the correct amount of change
        //and not all zeros.
        return coinMapCopy;
    }

    private void reset() {
        timeBought = insertedSoFar = 0;
    }
    
        @Override
    public int empty(){
        int newtotal = 0;
        newtotal = totalcoins;
        totalcoins =0;
        
     return newtotal;
    }
    
    //gets the selection from the user (as an integer)
    public int getUserInput(){
        userSelection = keyboard.nextInt(); //gets integer (values from one to five) from user
        //checks to see that user has inputted a valid number
        if (userSelection < 1 || userSelection > 5){ 
            userSelection = -1; //user has selection an invalid option, set userSelection to -1 to indicate this
        }
        return userSelection;
    }
    
    //prints the display options that the user can choose from
    public void printDisplay(){
        System.out.println("1) Desposit Coins");
        System.out.println("2) Display ");
        System.out.println("3) Buy Ticket");
        System.out.println("4) Cancel ");
        System.out.println("5) Change Rate Strategy\n");
    }
    
    //main loop for paystation
    @Override
    public void payStationLoop()
        throws IllegalCoinException{
        boolean start = true;
        while (start){
            printDisplay();
            getUserInput();
            switch(userSelection){
                case 1: //allows the user to deposit coins
                        addPayment(depositCoins());
                        break;
                case 2: //call the display
                    getDate();
                        break;
                case 3: //call a function that lets user buy ticket
                    getDate();
                    System.out.println(buy().value());
                    System.out.println();
                        break;
                case 4: //call cancel function
                    printMap();
                        break;
                case 5: //call change rate strategy function
                    paymentRate();
                        break;
                default: //the user has entered an invalid input
                    System.out.println("Please enter a valid selection.\n");
            }
        }
        
    }
    
    public int depositCoins(){ //instructs user to enter coin value and returns user's inputted coin value
        int coinValue;
        System.out.print("Enter Coin Value: ");
        coinValue = keyboard.nextInt(); //gets coin value from user
        System.out.println(" "); //adds new line for visual purposes
        return coinValue; //user's entered coin value is returned
    }
    
    public void getDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();  
        System.out.println(dtf.format(now));
    }
    
    public void printMap(){
        System.out.println(coinMap);
        cancel();
    }
    
    public void paymentRate(){
        System.out.println("1: linear payment rate");
        System.out.println("2: progressive rate");
        System.out.println("3: alternating rate");
        rate = keyboard.nextInt();
    }
}
