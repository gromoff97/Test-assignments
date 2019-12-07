package home.grom.utils;

public final class ValidationUtils {

    private static final String NULL_OBJ_DEFAULT_MESSAGE = "Referencing to non-null instance is required.";
    private static final String BLANK_STRING_DEFAULT_MESSAGE = "Non-blank String's instance is required.";

    public static <T> T requireNonNull(T obj, String exceptionMessage) {
        if (obj == null)
            throw new IllegalArgumentException(exceptionMessage);
        return obj;
    }

    public static <T> T requireNonNull(T obj) {
        return requireNonNull(obj, NULL_OBJ_DEFAULT_MESSAGE);
    }

    public static String requireNonBlank(String str, String exceptionMessage) {
        if (str == null || str.trim().isEmpty())
            throw new IllegalArgumentException(exceptionMessage);
        return str;
    }
}
