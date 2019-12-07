package home.grom.utils;

import org.apache.commons.validator.routines.UrlValidator;

public final class ValidationUtils {

    private static final String NULL_OBJ_DEFAULT_MESSAGE = "Referencing to non-null instance is required.";
    private static final String BLANK_STRING_DEFAULT_MESSAGE = "Non-blank String's instance is required.";
    private static final String INVALID_URL_DEFAULT_MESSAGE = "Valid URL-address is required.";

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

    public static String requireNonBlank(String str) {
        return requireNonBlank(str, BLANK_STRING_DEFAULT_MESSAGE);
    }

    public static String requireValidURL(String url, String exceptionMessage) {
        if (!isValidURL(url))
            throw new IllegalArgumentException(exceptionMessage);
        return url;
    }

    /**
     * Validates entered URL with {@link UrlValidator#isValid(String)}
     *
     * @param   url
     *          contains URL of some web-page.
     *
     * @return  {@code true} if URL is valid,
     *          otherwise {@code false}.
     */
    private static boolean isValidURL(String url) {
        return UrlValidator.getInstance().isValid(url);
    }
}
