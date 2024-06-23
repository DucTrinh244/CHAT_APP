package Pattern;

import java.sql.Timestamp;
import java.util.Date;

public class TimestampUtil {

    public static Timestamp getCurrentTimestamp() {
        Date currentDate = new Date();
        return new Timestamp(currentDate.getTime());
    }

    public static void main(String[] args) {
        Timestamp currentTimestamp = getCurrentTimestamp();
        System.out.println("Current Timestamp: " + currentTimestamp);
    }
}
