package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;

public class Bot extends TelegramLongPollingBot {


    Bot() {

    }

    @Override
    public String getBotUsername() {
        return "DekstopTypeBot";
    }

    @Override
    public String getBotToken() {
        return "7582021290:AAG77MD0Mee6NQco8b2AnokrcihPXRudz9w";
    }


    @Override
    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();
        org.telegram.telegrambots.meta.api.objects.User user = msg.getFrom();

        String chatId = msg.getChatId().toString();
        String userName = user.getUserName();
        String message = msg.getText();
        
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