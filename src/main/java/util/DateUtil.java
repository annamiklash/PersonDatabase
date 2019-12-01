package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {

    private static final String pattern = "yyyy-MM-dd";
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    public static Date parse(String dateStr) throws ParseException {
        return simpleDateFormat.parse(dateStr);
    }


}
