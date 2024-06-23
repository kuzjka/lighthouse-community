package ua.org.meters.lighthouse.notification;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Telegram bot to send messages.
 */
public class LighthouseTelegramBot extends TelegramLongPollingBot {
    private String token;

    /**
     * Creates new bot.
     * @param token Bot token
     */
    public LighthouseTelegramBot(String token) {
        this.token = token;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // do nothing
    }

    @Override
    public String getBotUsername() {
        return "Lighthouse";
    }
}
