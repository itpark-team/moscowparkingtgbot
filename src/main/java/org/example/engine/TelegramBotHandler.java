package org.example.engine;

import org.example.statemachine.ChatsRouter;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramBotHandler extends TelegramLongPollingBot {
    private String botUsername;
    private String botToken;

    private ChatsRouter chatsRouter;

    public TelegramBotHandler() {
        botUsername = "moscow_parking_smyslov_bot";
        botToken = "7044572252:AAGCc-dd0Hp3lZoPokrMwQdWcmVp-53aWZY";

        chatsRouter = new ChatsRouter();
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            long chatId = 0;
            String textFromUser = "";

            SendMessage messageToUser = null;

            if (update.hasMessage()) {
                chatId = update.getMessage().getChatId();
                textFromUser = update.getMessage().getText();
            } else if (update.hasCallbackQuery()) {
                chatId = update.getCallbackQuery().getMessage().getChatId();
                textFromUser = update.getCallbackQuery().getData();
            }

            messageToUser = chatsRouter.route(chatId, textFromUser);

            execute(messageToUser);
        } catch (Exception ee) {
            System.out.println("Ошибка отправки сообщения пользователю.  " + ee);
        }

    }
}
