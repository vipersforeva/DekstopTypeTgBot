package org.example;


import org.example.time_picker.ScheduleForTextFile;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class App
{
    public static void main( String[] args )throws TelegramApiException {
        ScheduleForTextFile time = new ScheduleForTextFile();
        time.run();

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new Bot());
    }
}