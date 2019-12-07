package home.grom.utils;

public final class ValidationUtils {

    private static final String NULL_OBJ_DEFAULT_MESSAGE = "Referencing to non-null instance is required.";

    public static <T> T requireNonNull(T obj, String exceptionMessage) {
        if (obj == null)
            throw new IllegalArgumentException(exceptionMessage);
        return obj;
    }
}
