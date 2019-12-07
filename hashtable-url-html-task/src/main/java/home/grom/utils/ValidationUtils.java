package home.grom.utils;

public final class ValidationUtils {
    public static <T> T requireNonNull(T obj, String exceptionMessage) {
        if (obj == null)
            throw new IllegalArgumentException(exceptionMessage);
        return obj;
    }
}
