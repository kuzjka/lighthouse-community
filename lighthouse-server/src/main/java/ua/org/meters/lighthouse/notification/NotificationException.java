package ua.org.meters.lighthouse.notification;

/**
 * Thrown if user notification failed.
 */
public class NotificationException extends RuntimeException {

    /**
     * Creates new exception.
     * @param message   Error message
     */
    public NotificationException(String message) {
        super(message);
    }

    /**
     * Creates new exception.
     * @param message   Error message
     * @param cause     Exception which caused notification failure
     */
    public NotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
