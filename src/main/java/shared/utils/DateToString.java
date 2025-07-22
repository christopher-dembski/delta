package shared.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateToString {
    public static String call(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
