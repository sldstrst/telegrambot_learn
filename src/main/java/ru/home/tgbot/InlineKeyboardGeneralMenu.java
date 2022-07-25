package ru.home.tgbot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.util.LinkedList;
import java.util.List;

//кнопки главного меню
public class InlineKeyboardGeneralMenu implements BotResponses {

    @Override
    public void getKeyboard(SendMessage sendMessage) {

        InlineKeyboardButton menuButton = new InlineKeyboardButton();
        menuButton.setText("О курсе");
        menuButton.setCallbackData("course");

        InlineKeyboardButton menuButton1 = new InlineKeyboardButton();
        menuButton1.setText("Отзывы");
        menuButton1.setCallbackData("reviews");

        InlineKeyboardButton menuButton2 = new InlineKeyboardButton();
        menuButton2.setText("Местоположение");
        menuButton2.setCallbackData("/location");

        List<InlineKeyboardButton> row = new LinkedList<>();
        row.add(menuButton);
        row.add(menuButton1);
        row.add(menuButton2);

        //row collection
        List<List<InlineKeyboardButton>> rowCollection = new LinkedList<>();
        rowCollection.add(row);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowCollection);

        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    }
}
