package ua.org.meters.lighthouse.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TelegramNotifier implements PowerNotifier {
    private static final Logger logger = LoggerFactory.getLogger(TelegramNotifier.class);

    private String token;
    private String chatId;
    private LighthouseTelegramBot bot;

    public TelegramNotifier(@Value("${lighthouse.telegram.token}") String token,
                            @Value("${lighthouse.telegram.chat-id}") String chatId) {
        this.token = token;
        this.chatId = chatId;
        this.bot = new LighthouseTelegramBot(this.token);
    }

    @Override
    public void notifyPower(boolean powerOn) {
        logger.debug("Sending notification to chat {}", chatId);

        String text = powerOn ?
                "\uD83C\uDF1D Світло з'явилось!" :
                "\uD83C\uDF1A Світло відключено";

        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();

        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new NotificationException("Cannot send notification with Telegram", e);
        }
    }
}
