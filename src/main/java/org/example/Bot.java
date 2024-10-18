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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

        Message msg = update.getMessage();
        User user = msg.getFrom();

        String chatId = msg.getChatId().toString();
        String userName = user.getUserName();
        String message = msg.getText();
        
        log(user.getFirstName(), user.getLastName(), chatId, message);

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


                    sendFormattedMessage(chatId, "\uD83C\uDFC6* Топы*");


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

                String botMsg = "\uD83C\uDFC6 Рекорды пользователя:\n";
                sendMessage(chatId,botMsg +"⌛\uFE0F15 сек " + userRecords[0] + " симв/сек\n" +
                        "⌛\uFE0F30 сек " + userRecords[1] + " симв/сек\n" +
                        "⌛\uFE0F60 сек " + userRecords[1] + " симв/сек");
            }
            catch(Exception e){
                sendMessage(chatId, "\uD83D\uDD0DПользователь не найден! ");
            }
            return;
        }

        switch (message){
            case "/info":
                sendFormattedMessage(chatId,"\uD83D\uDCDA *Информация:*\n");
                String botMsg = "*/top* - Топ игроков \uD83C\uDFC6 \n" +
                        "*/stat username* - Статистика по нику \uD83D\uDD0D \n" +
                        "*/newText* - Добавить новый текст \uD83D\uDCC4"+
                        "\n" +
                        "*/ask* - Что то не работает?/Есть предложение? \uD83D\uDCBC";
                sendFormattedMessage(chatId,botMsg);
                return;

            case "/ask":            //question Code 10
                sendMessage(chatId,"Расскажите о вашем предложении");
                users.get(userName).setQuest(10);
                return;

            case "/newText":        //question Code 5
                if(userName.equals("surfshtt")){
                    sendMessage(chatId,"Привет!\uD83D\uDC4B \nНапиши текст:");
                    users.get(userName).setQuest(5);
                }
                else{
                    sendMessage(chatId,"❌ Нет доступа!");
                }
                return;

            case "/top":            //question Code 3
                sendFormattedMessage(chatId,"\uD83D\uDDC2\uFE0F* Выберите:*\n");
                sendMessage(chatId,"1. 15-ти секундный режим\n" +
                        "2. 30-ти секундный режим\n" +
                        "3. 60-ти секундный режим");
                users.get(userName).setQuest(3);
                return;

            case "/howMany":
                sendMessage(chatId,String.valueOf(countOfMessages));
                return;

            default:
                sendMessage(chatId,"Извини, я тебя не понял!\uD83D\uDE1E");
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

    private void log(String first_name, String last_name, String user_id, String txt) {
        System.out.println("\n--------------------------------------------------");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        System.out.println("Message from " + first_name + " " + last_name + ". (id = " + user_id + ") \nText - " + txt);
    }
}