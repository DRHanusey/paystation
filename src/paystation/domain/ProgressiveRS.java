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
public class ProgressiveRS extends RateStrategy {

    public final int TIER_1 = 60;
    public final int TIER_2 = 120;
    private int CurrentTime = 0;
    private int insertedSoFar = 0;

    @Override
    public int calculateTime(int coinValue) {

        //Update amount entered into paystation
        insertedSoFar += coinValue;

        ////Calculate time purchased based on amount entered into paystation
        if (CurrentTime < TIER_1) {
            CurrentTime = (insertedSoFar / 5 * 2);
        } else if (CurrentTime < TIER_2) {
            CurrentTime = (insertedSoFar - 150) * 3 / 10 + 60;
            //System.out.println("(< 120)current time = " + CurrentTime + ", insertedSoFar = " +  insertedSoFar);
        } else {
            CurrentTime = (insertedSoFar - 350) / 5 + 120;
            //System.out.println("(greater than 120)current time = " + CurrentTime + ", insertedSoFar = " +  insertedSoFar);
        }

        return CurrentTime;
    }


}
