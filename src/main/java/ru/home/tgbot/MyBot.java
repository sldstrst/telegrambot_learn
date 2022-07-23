package ru.home.tgbot;

//import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyBot extends TelegramLongPollingBot {
    private static final String TOKEN = ".";
    private static final String USERNAME = "FirstBotFromMeBot";

    public MyBot(DefaultBotOptions options){
        super (options);
    }

    public MyBot(){
    }

    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    public synchronized void setButtons(SendMessage sendMessage) {
        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add(new KeyboardButton("Привет"));

        // Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardSecondRow.add(new KeyboardButton("Помощь"));

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public SendMessage start(SendMessage sendMessage){
        InlineKeyboardButton menuButton = new InlineKeyboardButton();
        menuButton.setText("О курсе");
        menuButton.setCallbackData("course");

        InlineKeyboardButton menuButton1 = new InlineKeyboardButton();
        menuButton1.setText("Отзывы");
        menuButton1.setCallbackData("reviews");

        InlineKeyboardButton menuButton2 = new InlineKeyboardButton();
        menuButton2.setText("Местоположение");
        menuButton2.setCallbackData("location");

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
        return sendMessage;
    }


    private void sendMessageFrom(String chatId, String textToSend){
        SendMessage message = new SendMessage();
        sendMessageFrom(chatId, textToSend, message);
    }

    private void sendMessageFrom(String chatId, String textToSend, SendMessage message){
        message.setChatId(chatId);
        message.setText(textToSend);

        try{
            execute(message);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }



    public void onUpdateReceived(Update update){

        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            message.setChatId(update.getMessage().getChatId().toString());

            String messageText = update.getMessage().getText();

            switch (messageText){
                case "/start":
                    sendMessageFrom(message.getChatId(), "Привет!)\n" +
                            "Просто кликай на интересующие кнопки = )");
                    break;
            }
            //повторение меню
            message = start(message);
            sendMessageFrom(message.getChatId(), "MENU", message);

        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            String chat_id = callbackQuery.getMessage().getChat().getId().toString();
            SendChatAction sendChatAction = new SendChatAction();
            sendChatAction.setChatId(chat_id);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chat_id);

            switch (data){
                case "course":
                    sendMessageFrom(chat_id, "Как устроен курс:\n" +
                        "Теория записана в видео - можно смотреть в любое время, не онлайн стрим\n");
                    break;
                case "reviews": sendMessageFrom(chat_id,"Здесь будут наши отзывы!");
                    break;
                case "location":
                    sendMessageFrom(chat_id,"Здесь будет локация!");
                    break;
                default: sendMessageFrom(chat_id,"Мы ну никак не можем распознать ваши слова..." + "\n" +
                "Подумайте ещё раз и напишите что-то другое...");
                break;
            }

            try {
                sendChatAction.setAction(ActionType.TYPING);
                execute(sendChatAction);
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            sendMessage = start(sendMessage);
            sendMessage.setText("Меню");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }


        }
    }
}
