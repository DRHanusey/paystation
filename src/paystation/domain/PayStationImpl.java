package paystation.domain;

import java.util.HashMap;
import java.util.Map;

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
    Map<Integer, Integer> map = new HashMap<>();
    
    
    //initiate map object to zero
    public void initMap() {
        map.put(5,0);
        map.put(10,0);
        map.put(25,0);
    };

    

    
    @Override
    public void addPayment(int coinValue)
            throws IllegalCoinException {
        
        switch (coinValue) {
            case 5:
                //retuns value for given key, adds 1 to it, puts it back into map
                map.put( 5 , map.get(5)+1 );
                break;
            case 10:
                map.put( 10 , map.get(10)+1 );
                break;
            case 25:
                map.put( 25 , map.get(25)+1 );
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
        reset();
        return r;
    }

    @Override
    public Map<Integer, Integer> cancel() {
        reset();
        return map;
    }

    private void reset() {
        timeBought = insertedSoFar = 0;
    }
}
