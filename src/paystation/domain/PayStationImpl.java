package paystation.domain;

import java.util.HashMap;
import java.util.Map;

//This is s test

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
}
