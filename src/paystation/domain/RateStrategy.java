/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paystation.domain;

/**
 *
 * @author Dan
 */
public class RateStrategy {

    private int currentTime = 0;
    private int insertedSoFar = 0;


    public int calculateTime(int coinValue) {

        //Update amount entered into paystation
        insertedSoFar += coinValue;
        
        //Calculate time purchased based on amount entered into paystation
        currentTime = insertedSoFar * 2 / 5;

        //System.out.println("time(rs obj) " + currentTime);

        return currentTime;
    }


}
