package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;

public class Bot extends TelegramLongPollingBot {
    HashMap<String, UserInfo> users;

    Bot() {
        users = new HashMap<>();
    }

    @Override
    public String getBotUsername() {
        return "DesktopTypeBot";
    }

    @Override
    public String getBotToken() {
        return "7702213647:AAGmOs346UreDSTGgsQaqXmcPdhhrBYasP8";
    }


    @Override
    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();
        org.telegram.telegrambots.meta.api.objects.User user = msg.getFrom();

        String chatId = msg.getChatId().toString();
        String userName = user.getUserName();
        String message = msg.getText();

        if(!users.containsKey(userName) || message.equals("/start")) {
            users.put(userName, new UserInfo());
            String botMsg = "*Привет!*\n" +
                    "Я телеграм бот клавиатурного тренажера *DesktopType*⌨\uFE0F\n" +
                    "Чтобы узнать что я умею напиши /info";
            sendFormattedMessage(chatId,botMsg);
            return;
        }

        switch (message){
            case "/info":
                break;

            case "/test":
                System.out.println("tst");
                break;

            default:
                sendMessage(chatId,"Извини, я тебя не понял!\uD83D\uDE1E");
        }

        System.out.println(userName + " написал: " + message + "\nАйди чата: " + chatId);
    }

    public void sendMessage(String chatId, String msg){
        SendMessage sm = SendMessage.builder()
                .chatId(chatId)
                .text(msg).build();
        try{
            execute(sm);
        }catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendFormattedMessage(String chatId, String msg){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(msg);
        message.enableMarkdown(true);
        ;
        try{
            execute(message);
        }catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}