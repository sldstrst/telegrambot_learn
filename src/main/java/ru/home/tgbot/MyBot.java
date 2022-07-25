package ru.home.tgbot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyBot extends TelegramLongPollingBot {
    private static final BotConfig botConfig = new BotConfig();
    private static final String textStart = "Привет!)\nПросто кликай на интересующие кнопки = )";
    private static final String textMenu = "Это меню (не как в ресторане)";
    private static final String textCourse = "Здесь может быть представлена информация \nо курсе";
    private static final String textReviews = "Отзывы как они есть";
    private static final String textLocation = "Location";
    private static final String textDefault = "Don't speak english? Very bad. F'uk up!";
    private static final String textGeneralMenu = "Главное меню";

    public MyBot(){
        new BotConfig();
    }

    @Override
    public String getBotUsername() {
        return botConfig.appProps.getProperty("bot.username");
    }

    @Override
    public String getBotToken() {
        return botConfig.appProps.getProperty("bot.token");
    }

    public void onUpdateReceived(Update update){

        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());

            InlineKeyboardGeneralMenu inlineKeyboardGeneralMenu = new InlineKeyboardGeneralMenu();

            String messageText = update.getMessage().getText();

            switch (messageText){
                case "/start":
                    sendMessageFrom(message.getChatId(), textStart);
                    break;

            }
            new InlineKeyboardGeneralMenu().getKeyboard(message);
            inlineKeyboardGeneralMenu.getKeyboard(message);
            sendMessageFrom(message.getChatId(), textMenu, message);

        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            String chat_id = callbackQuery.getMessage().getChat().getId().toString();

            SendMessage sendMessage = new SendMessage();

            String textContent;
            InlineKeyboardGeneralMenu inlineKeyboardGeneralMenu = new InlineKeyboardGeneralMenu();
            ReplyMarkupSubMenu replyMarkupSubMenu = new ReplyMarkupSubMenu();
            SendLocation sendLocation = new SendLocation(chat_id,
                    Double.parseDouble(botConfig.appProps.getProperty("location.latitude")),
                    Double.parseDouble(botConfig.appProps.getProperty("location.longitude")));

            switch (data){
                case "course":
                    sendMessageFromCallback(chat_id, textCourse);
                    break;
                case "reviews":
                    replyMarkupSubMenu.getKeyboard(sendMessage);
                    sendMessageFromCallback(chat_id, textReviews, sendMessage);
                    break;
                case "/location":
                    sendMessageFromCallback(chat_id, textLocation);
                    sendLocationFromCallback(chat_id, sendLocation);
                    break;
                default:
                    sendMessageFromCallback(chat_id, textDefault);
                break;
            }

            inlineKeyboardGeneralMenu.getKeyboard(sendMessage);
            sendMessageFrom(chat_id, textGeneralMenu, sendMessage);

        }
    }

    //Send Message From Bot ____________________________-------------------------------________________
    private synchronized void sendMessageFrom(String chatId, String textToSend){
        SendMessage message = new SendMessage();
        sendMessageFrom(chatId, textToSend, message);
    }
    private synchronized void sendMessageFromCallback(String chatId, String textToSend){
        SendMessage message = new SendMessage();
        sendMessageFromCallback(chatId, textToSend, message);
    }

    private synchronized void sendMessageFrom(String chatId, String textToSend, SendMessage message){
        message.setChatId(chatId);
        message.setText(textToSend);

        try{
            execute(message);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
    private synchronized void sendLocationFromCallback(String chat_id, SendLocation sendLocation){
        sendLocation.setChatId(chat_id);
        try{
            execute(sendLocation);

        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    private synchronized void sendMessageFromCallback(String chat_id, String textToSend, SendMessage sendMessage){
        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setChatId(chat_id);
        sendChatAction.setAction(ActionType.TYPING);
        sendMessage.setChatId(chat_id);
        sendMessage.setText(textToSend);

        try{
            execute(sendChatAction);
            execute(sendMessage);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

}
