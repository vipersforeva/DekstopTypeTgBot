package org.example.user_data;

public class ChatData {

    private int numForConv;
    private int[] records;

    public ChatData() {
        numForConv = 0;
    }

    public void setQuest(int num){
        numForConv = num;
    }

    public int getQuest(){
        return numForConv;
    }
}
