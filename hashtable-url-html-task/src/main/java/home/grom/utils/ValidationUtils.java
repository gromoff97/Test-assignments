package home.grom.utils;

import org.apache.commons.validator.routines.UrlValidator;

/**
 * Utility-class which contains methods for
 * shortening the code responsible for validation.
 */
public final class ValidationUtils {

    private static final String NULL_OBJ_DEFAULT_MESSAGE = "Referencing to non-null instance is required.";
    private static final String BLANK_STRING_DEFAULT_MESSAGE = "Non-blank String's instance is required.";
    private static final String INVALID_URL_DEFAULT_MESSAGE = "Valid URL-address is required.";

    private ValidationUtils() {
        throw new AssertionError("Instance creation is not allowed here because this class is utility-class.");
    }

    /**
     * Does the same as {@link java.util.Objects#requireNonNull(Object, String)}
     * but throws another exception.
     *
     * @throws  IllegalArgumentException
     *          if object refers to {@code null}.
     *
     * @see java.util.Objects#requireNonNull(Object, String)
     */
    public static <T> T requireNonNull(T obj, String exceptionMessage) {
        if (obj == null)
            throw new IllegalArgumentException(exceptionMessage);
        return obj;
    }

    /**
     * Does the same as {@link #requireNonNull(Object, String)}
     * but uses default variable as exception's message.
     *
     * @see ValidationUtils#requireNonNull(Object, String)
     * @see #NULL_OBJ_DEFAULT_MESSAGE
     */
    public static <T> T requireNonNull(T obj) {
        return requireNonNull(obj, NULL_OBJ_DEFAULT_MESSAGE);
    }

    /**
     * Has the same purpose as {@link java.util.Objects#requireNonNull(Object, String)} but :
     * 1) accepts only {@link String} variables;
     * 2) checks whether {@link String} variable is not blank;
     * 3) throws another exception.
     *
     * @throws  IllegalArgumentException
     *          if {@link String} variable is blank.
     *
     * @see java.util.Objects#requireNonNull(Object, String)
     */
    public static String requireNonBlank(String str, String exceptionMessage) {
        if (str == null || str.trim().isEmpty())
            throw new IllegalArgumentException(exceptionMessage);
        return str;
    }

    /**
     * Does the same as {@link #requireNonBlank(String, String)}
     * but uses default variable as exception's message.
     *
     * @see ValidationUtils#requireNonBlank(String, String)
     * @see #BLANK_STRING_DEFAULT_MESSAGE
     */
    public static String requireNonBlank(String str) {
        return requireNonBlank(str, BLANK_STRING_DEFAULT_MESSAGE);
    }

    /**
     * Has the same purpose as {@link java.util.Objects#requireNonNull(Object, String)} but :
     * 1) accepts only {@link String} variables;
     * 2) checks whether {@link String} variable is valid according to {@link #isValidURL}'s return value;
     * 3) throws another exception.
     *
     * @throws  IllegalArgumentException
     *          if url represented as {@link String} is invalid.
     *
     * @see java.util.Objects#requireNonNull(Object, String)
     * @see #isValidURL
     */
    public static String requireValidURL(String url, String exceptionMessage) {
        if (!isValidURL(url))
            throw new IllegalArgumentException(exceptionMessage);
        return url;
    }

    /**
     * Does the same as {@link #requireValidURL(String, String)}
     * but uses default variable as exception's message.
     *
     * @see ValidationUtils#requireValidURL(String, String)
     * @see #INVALID_URL_DEFAULT_MESSAGE
     */
    public static String requireValidURL(String url) {
        return requireValidURL(url, INVALID_URL_DEFAULT_MESSAGE);
    }

    /** Method copied from JDK 11 */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
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
