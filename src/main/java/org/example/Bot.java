package org.example;

import org.example.files.AskLogic;
import org.example.user_data.AllData;
import org.example.user_data.ChatData;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;

public class Bot extends TelegramLongPollingBot {
    HashMap<String, ChatData> users;

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

        System.out.println(userName + " написал: " + message + "\nАйди чата: " + chatId);

        if(!users.containsKey(userName) || message.equals("/start")) {
            users.put(userName, new ChatData());
            String botMsg = "*Привет!*\uD83D\uDC4B\n" +
                    "Я телеграм бот клавиатурного тренажера *DesktopType*⌨\uFE0F\n" +
                    "Хочешь узнать что я умею? " +
                    "*Напиши* /info";
            sendFormattedMessage(chatId,botMsg);
            return;
        }

        switch (users.get(userName).getQuest()){
            case 10:
                AskLogic.saveToFile(message + " \nChatId: " + chatId + " UserName: " + userName);
                sendFormattedMessage(chatId,"*Спасибо!*\nВаше обращение отправлено на рассмотрение❤\uFE0F");
                users.get(userName).setQuest(0);
                return;

            case 5:
                String newText = message;

                sendMessage(chatId,"Текст успешно добавлен! " + newText);
                users.get(userName).setQuest(0);
                return;
        }

        if(message.contains("/stat")){
            try {
                String userNameFS = message.split(" ")[1];
                int[] userRecords = AllData.getInfo(userNameFS);

                sendMessage(chatId,"\uD83C\uDFC6Рекорды пользователя:\n" +
                        "⌛\uFE0F30 сек " + userRecords[0] + " симв/сек\n" +
                        "⌛\uFE0F60 сек " + userRecords[1] + " симв/сек\n");
            }
            catch(Exception e){
                sendMessage(chatId, "\uD83D\uDD0DПользователь не найден! " + e);
            }
            return;
        }

        switch (message){
            case "/info":
                String botMsg = "_Пока что ничего(_";
                sendFormattedMessage(chatId,botMsg);
                return;

            case "/ask":            //question Code 10
                sendMessage(chatId,"Расскажите о вашем предложении");
                users.get(userName).setQuest(10);
                return;

            case "/newText":        //question Code 5
                if(userName.equals("surfshtt")){
                    sendMessage(chatId,"Привет адм! Напиши текст:");
                    users.get(userName).setQuest(5);
                }
                return;

            default:
                sendMessage(chatId,"Извинись, я тебя не понял!\uD83D\uDE1E");
        }
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