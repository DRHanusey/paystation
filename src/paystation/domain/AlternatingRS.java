
package paystation.domain;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class AlternatingRS extends RateStrategy {

    RateStrategy rs;

    AlternatingRS() {
        isWeekend();
    }

    //Determines which rate strategy will be implemented
    //based on the day of the weel
    public void isWeekend() {
        //Get day of the week
        Calendar c = new GregorianCalendar();
        int day = c.get(Calendar.DAY_OF_WEEK);
        
        //Creates ProgressiveRS if Saturday or Sunday
        //Creates RateStrategy if weekday
        if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
            rs = new ProgressiveRS();
        } else {
            rs = new RateStrategy();
        }
    }

    @Override
    public int calculateTime(int coinValue) {
        return rs.calculateTime(coinValue);
    }

}
