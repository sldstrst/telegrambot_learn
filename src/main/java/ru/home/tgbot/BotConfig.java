package ru.home.tgbot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BotConfig {

    String botName;
    String token;
    Properties appProps;

    BotConfig(){
        getConfigFile();
    }

    void getConfigFile() {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "text.properties";

        appProps = new Properties();
        try {
            appProps.load(new FileInputStream(appConfigPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getBotName() {
        return botName;
    }

    public String getToken() {
        return appProps.getProperty("bot.token");
    }

}
