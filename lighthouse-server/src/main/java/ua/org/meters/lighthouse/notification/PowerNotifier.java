package ua.org.meters.lighthouse.notification;

/**
 * Defines a component able to notify users about power failure.
 */
public interface PowerNotifier {
    /**
     * Notifies about power supply state change.
     * @param powerOn {@code true} if the power is turned on, {@code false} if power is cut off
     */
    void notifyPower(boolean powerOn);
}
