package ru.home.tgbot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface BotResponses {
    void getKeyboard(SendMessage sendMessage);
}
