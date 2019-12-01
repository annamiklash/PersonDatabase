package util;

public final class RegexExpUtil {

    public static final String NAME_PATTERN = "((^[A-Z])([a-z])+)";

    private static final String YEAR_PATTERN = "([1-9][0-9][0-9][0-9]|[0-9][1-9][0-9][0-9]|[0-9][0-9][1-9][0-9]|[0-9][0-9][0-9][1-9])";
    private static final String MONTH_PATTERN = "(([0][1-9])|([1][0-2]))";
    private static final String DAY_PATTERN = "(([0][1-9])|([1-2][0-9])|([3][0-1]))";
    private static final String SEPARATOR = "-";
    private static final String WHITESPACE = "\\s";

    public static final String DATE_PATTERN =
            "^" + YEAR_PATTERN + SEPARATOR + MONTH_PATTERN + SEPARATOR + DAY_PATTERN + "$";

    public static final String LINE_PATTERN = "(" +
            NAME_PATTERN + WHITESPACE + NAME_PATTERN + WHITESPACE + DATE_PATTERN + ")";


}
