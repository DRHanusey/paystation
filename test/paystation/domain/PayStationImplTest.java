/**
 * Testcases for the Pay Station system.
 *
 * This source code is from the book "Flexible, Reliable Software: Using
 * Patterns and Agile Development" published 2010 by CRC Press. Author: Henrik B
 * Christensen Computer Science Department Aarhus University
 *
 * This source code is provided WITHOUT ANY WARRANTY either expressed or
 * implied. You may study, use, modify, and distribute it for non-commercial
 * purposes. For any commercial use, see http://www.baerbak.com/
 */
package paystation.domain;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class PayStationImplTest {

    PayStation ps;

    @Before
    public void setup() {
        ps = new PayStationImpl();
        //Creates a Map object pairing the "key:coin type=val: number of coins"
        //initiates number of coins to 0
        ps.initCoinsDepositedMap();
    }

    /**
     * Entering 5 cents should make the display report 2 minutes parking time.
     */
    @Test
    public void shouldDisplay2MinFor5Cents()
            throws IllegalCoinException {
        ps.addPayment(5);
        assertEquals("Should display 2 min for 5 cents",
                2, ps.readDisplay());
    }

    /**
     * Entering 25 cents should make the display report 10 minutes parking time.
     */
    @Test
    public void shouldDisplay10MinFor25Cents() throws IllegalCoinException {
        ps.addPayment(25);
        assertEquals("Should display 10 min for 25 cents",
                10, ps.readDisplay());
    }

    /**
     * Verify that illegal coin values are rejected.
     */
    @Test(expected = IllegalCoinException.class)
    public void shouldRejectIllegalCoin() throws IllegalCoinException {
        ps.addPayment(17);
    }

    /**
     * Entering 10 and 25 cents should be valid and return 14 minutes parking
     */
    @Test
    public void shouldDisplay14MinFor10And25Cents()
            throws IllegalCoinException {
        ps.addPayment(10);
        ps.addPayment(25);
        assertEquals("Should display 14 min for 10+25 cents",
                14, ps.readDisplay());
    }

    /**
     * Buy should return a valid receipt of the proper amount of parking time
     */
    @Test
    public void shouldReturnCorrectReceiptWhenBuy()
            throws IllegalCoinException {
        ps.addPayment(5);
        ps.addPayment(10);
        ps.addPayment(25);
        Receipt receipt;
        receipt = ps.buy();
        assertNotNull("Receipt reference cannot be null",
                receipt);
        assertEquals("Receipt value must be 16 min.",
                16, receipt.value());
    }

    /**
     * Buy for 100 cents and verify the receipt
     */
    @Test
    public void shouldReturnReceiptWhenBuy100c()
            throws IllegalCoinException {
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(10);
        ps.addPayment(25);
        ps.addPayment(25);

        Receipt receipt;
        receipt = ps.buy();
        assertEquals(40, receipt.value());
    }

    /**
     * Verify that the pay station is cleared after a buy scenario
     */
    @Test
    public void shouldClearAfterBuy()
            throws IllegalCoinException {
        ps.addPayment(25);
        ps.buy(); // I do not care about the result
        // verify that the display reads 0
        assertEquals("Display should have been cleared",
                0, ps.readDisplay());
        // verify that a following buy scenario behaves properly
        ps.addPayment(10);
        ps.addPayment(25);
        assertEquals("Next add payment should display correct time",
                14, ps.readDisplay());
        Receipt r = ps.buy();
        assertEquals("Next buy should return valid receipt",
                14, r.value());
        assertEquals("Again, display should be cleared",
                0, ps.readDisplay());
    }

    /**
     * Verify that cancel clears the pay station
     */
    @Test
    public void shouldClearAfterCancel()
            throws IllegalCoinException {
        ps.addPayment(10);
        ps.cancel();
        assertEquals("Cancel should clear display",
                0, ps.readDisplay());
        ps.addPayment(25);
        assertEquals("Insert after cancel should work",
                10, ps.readDisplay());
    }

    // Call to cancel returns a map containing one coin deposited.
    @Test
    public void shouldRetOneCoin()
            throws IllegalCoinException {
        //create map of coins deposited object
        Map<Integer, Integer> coinsDepositedMap = new HashMap<>();

        int numberOfCoinsRet = 0;

        //deposit 1 nickle
        ps.addPayment(5);

        coinsDepositedMap = ps.cancel();

        //sum each map value representing the numbers of coins deposited of each
        //type of coin
        //numberOfCoinsRet = coinsDepositedMap.get(5) + coinsDepositedMap.get(10) + coinsDepositedMap.get(25);
        if (coinsDepositedMap.containsKey(5)) {
            numberOfCoinsRet += coinsDepositedMap.get(5);
        }
        if (coinsDepositedMap.containsKey(10)) {
            numberOfCoinsRet += coinsDepositedMap.get(10);
        }
        if (coinsDepositedMap.containsKey(25)) {
            numberOfCoinsRet += coinsDepositedMap.get(25);
        }

        System.out.println("shouldRetOneCoin");
        System.out.println("number of coins = " + numberOfCoinsRet);
        System.out.println(coinsDepositedMap);

        assertEquals("Cancel should return 1 coin",
                1, numberOfCoinsRet);
    }

// Call to cancel returns a map containing mixture of coins deposited.
    @Test
    public void shouldRetMixtureOfCoins()
            throws IllegalCoinException {
        //create map of coins deposited object
        Map<Integer, Integer> coinsDepositedMap = new HashMap<>();

        int numberOfCoinsDeposited = 6;
        int numberOfCoinsRet = 0;

        //deposit 3 nickle
        ps.addPayment(5);
        ps.addPayment(5);
        ps.addPayment(5);
        //deposit 2 dimes
        ps.addPayment(10);
        ps.addPayment(10);
        //deposit 1 quarter
        ps.addPayment(25);

        coinsDepositedMap = ps.cancel();

        //sum each map value representing the numbers of coins deposited of each
        //type of coin
        numberOfCoinsRet = coinsDepositedMap.get(5) + coinsDepositedMap.get(10) + coinsDepositedMap.get(25);
        System.out.println("shouldRetMixtureOfCoins");
        System.out.println("number of coins returned = " + numberOfCoinsRet);
        System.out.println(coinsDepositedMap);

        assertEquals("Cancel should return 6 coin",
                numberOfCoinsDeposited, numberOfCoinsRet);
    }

    // Call to cancel returns map of only coins deposited and does not
    //contain keys for coins not deposited
    @Test
    public void shouldNotReturnCoinThatWasntDeposited()
            throws IllegalCoinException {
        //create map of coins deposited object
        Map<Integer, Integer> coinsDepositedMap = new HashMap<>();

        //deposit 3 nickle
        ps.addPayment(5);
        ps.addPayment(5);
        ps.addPayment(5);
        //deposit 0 dimes    
        //deposit 1 quarter
        ps.addPayment(25);

        coinsDepositedMap = ps.cancel();

        System.out.println("shouldNotReturnCoinThatWasntDeposited");
        System.out.println(coinsDepositedMap);

        assertFalse("Cancel should not return coin types that were not deposited",
                coinsDepositedMap.containsKey(10));

    }

    @Test
    public void shouldClearMapAfterCancel()
            throws IllegalCoinException {
        //create map of coins deposited object
        Map<Integer, Integer> coinsDepositedMap = new HashMap<>();
        int numberOfCoinsRet = 0;

        //sum each map value representing the numbers of coins deposited of each
        //type of coin
        if (coinsDepositedMap.containsKey(5)) {
            numberOfCoinsRet += coinsDepositedMap.get(5);
        }
        if (coinsDepositedMap.containsKey(10)) {
            numberOfCoinsRet += coinsDepositedMap.get(10);
        }
        if (coinsDepositedMap.containsKey(25)) {
            numberOfCoinsRet += coinsDepositedMap.get(25);
        }

        //deposit 3 nickle
        ps.addPayment(5);
        ps.addPayment(5);
        ps.addPayment(5);
        //deposit 0 dimes    
        //deposit 1 quarter
        ps.addPayment(25);

        //will return map of 5=3, 25=1
        coinsDepositedMap = ps.cancel();
        //should return map of 5=0, 10=0, 25=0
        coinsDepositedMap = ps.returnCoinMap();

        System.out.println("shouldClearMapAfterCancel");
        System.out.println(coinsDepositedMap);

        assertEquals("Cancel should return 0 coin",
                0, numberOfCoinsRet);
        assertTrue("Cancel should return map containing 5 as key",
                coinsDepositedMap.containsKey(5));
        assertTrue("Cancel should return map containing 10 as key",
                coinsDepositedMap.containsKey(10));
        assertTrue("Cancel should return map containing 25 as key",
                coinsDepositedMap.containsKey(25));

    }
     @Test
     public void ShouldTotalCoinsThatWasEnterAndEmptyThePaystation() throws IllegalCoinException{
         
        ps.addPayment(10);
        ps.buy();
        ps.addPayment(25);
        ps.cancel();
        int shouldbenum = ps.empty();
         assertEquals("should be 10",10,shouldbenum);
     }
}
