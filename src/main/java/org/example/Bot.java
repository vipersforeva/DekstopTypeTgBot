package org.example;

import org.example.files.AskLogic;
import org.example.files.NewText;
import org.example.user_data.AllData;
import org.example.user_data.ChatData;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;
import java.util.stream.Collectors;

public class Bot extends TelegramLongPollingBot {
    HashMap<String, ChatData> users;
    static int countOfMessages = 0;

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
        countOfMessages++;
        if(update.getMessage().getChatId().toString().equals("814292320"))
            return;

        Message msg = update.getMessage();
        User user = msg.getFrom();

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
                NewText.saveToFile(message);
                sendMessage(chatId,"Текст успешно добавлен! ");
                users.get(userName).setQuest(0);
                return;

            case 3:
                try {
                    StringBuilder finalMessage = new StringBuilder();
                    HashMap<String, Integer> tops;

                    switch (Integer.parseInt(message)) {
                        case 1:
                            tops = AllData.getTops(0);
                            break;

                        case 2:
                            tops = AllData.getTops(1);
                            break;

                        case 3:
                            tops = AllData.getTops(2);
                            break;

                        default:
                            sendMessage(chatId,"❌Такого номера нет!");
                            return;
                    }


                    sendFormattedMessage(chatId, "\uD83C\uDFC6*Топы*");


                    LinkedHashMap<String, Integer> sortedTops = getStringIntegerLinkedHashMap(tops);

                    int count = 1;
                    for (Map.Entry<String, Integer> entry : sortedTops.entrySet()) {
                        finalMessage
                                .append(count)
                                .append(".")
                                .append(" ")
                                .append(entry.getKey() )
                                .append(" ")
                                .append(entry.getValue())
                                .append(" симв/сек")
                                .append("\n");

                        count++;
                    }

                    //System.out.println(tops.values());

                    sendMessage(chatId, finalMessage.toString());
                    users.get(userName).setQuest(0);
                    return;
                }
                catch (NumberFormatException e) {
                    sendMessage(chatId,"❌Такого номера нет!");
                    return;
                }
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

            case "/top":            //question Code 3
                sendFormattedMessage(chatId,"*Выберите:*\n" +
                        "1. 30-ти секундный режим\n" +
                        "2. 60-ти секундный режим\n" +
                        "3. безвременный режим");
                users.get(userName).setQuest(3);
                return;

            case "/howMany":
                sendMessage(chatId,String.valueOf(countOfMessages));
                return;

            default:
                sendMessage(chatId,"Извинись, я тебя не понял!\uD83D\uDE1E");
        }
    }

    private static LinkedHashMap<String, Integer> getStringIntegerLinkedHashMap(HashMap<String, Integer> tops) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(tops.entrySet());

        list.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue()); // Compare in descending order
            }
        });

        LinkedHashMap<String, Integer> sortedTops = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedTops.put(entry.getKey(), entry.getValue());
        }
        return sortedTops;
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