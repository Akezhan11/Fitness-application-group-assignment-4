package components.notification;

public class NotificationComponent {
    private final Notifier notifier = new ConsoleNotifier();
    public Notifier notifier() { return notifier; }
}
