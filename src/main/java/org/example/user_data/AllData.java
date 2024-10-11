package org.example.user_data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class AllData {
    private static HashMap<String,int[]> allData = new HashMap<>();
    private static int lastDBSize = 0;

    public static void updateData() {

        try (BufferedReader br = new BufferedReader(new FileReader("C:/Users/user/Desktop/tmp/DesktopType/DesktopType/DesktopType/bin/Debug/net8.0-windows/Users.txt"))) {
            String line;
            String[] groupOfUsers;

            while ((line = br.readLine()) != null) {
                groupOfUsers = line.split("\\$");

                if (groupOfUsers.length >= 5) {
                    int[] tmp = new int[3];
                    tmp[0] = Integer.parseInt(groupOfUsers[2]);
                    tmp[1] = Integer.parseInt(groupOfUsers[3]);
                    tmp[2] = Integer.parseInt(groupOfUsers[4]);

                    allData.put(groupOfUsers[0], tmp);
                } else {
                    System.out.println("Ошибка: недостаточно данных в строке: " + line);
                }
            }
            if(allData.size() != lastDBSize) {
                System.out.println("Данные из БД собраны! " + allData.size());
                lastDBSize = allData.size();
            }

        } catch (Exception e) {
            System.out.println("Ошибка при открытие фала приложения: " + e);
        }
    }

    public static int[] getInfo(String userName){
        return allData.get(userName);
    }

    public static HashMap<String, Integer> getTops(int i){

        HashMap<String, Integer> topGuys = new HashMap<>();
        HashMap<String,int[]> allDataCopy = new HashMap<>(allData);

        String tmpUserName;
        for(int j = 0; j < 5; j++){
            tmpUserName = findMax(allDataCopy,i);
            allDataCopy.remove(tmpUserName);
            topGuys.put(tmpUserName,allData.get(tmpUserName)[i]);
        }

//            String[] alreadyExist = {"", "", "", "", ""};
//            for (int j = 0; j < 5; j++) {
//                alreadyExist[j] = findMax(alreadyExist, i);
//            }

//            for (int j = 0; j < 5; j++) {
//                topGuys.put(alreadyExist[j], allData.get(alreadyExist[j])[i-1]);
//            }
            return topGuys;
    }

    private static String findMax(HashMap<String,int[]> allDataCopy, int i){
        String userName = "";
        int max = Integer.MIN_VALUE;

        for(String nm : allDataCopy.keySet()){
            if(allDataCopy.get(nm)[i] > max){
                max = allDataCopy.get(nm)[i];
                userName = nm;
            }
        }

        return userName;
    }

//    private static String findMax(String[] alreadyExist, int i){
//        int max = Integer.MIN_VALUE;
//        String userNameOB = null;
//
//        for(String userName : allData.keySet()){
//            if(allData.get(userName)[i-1] > max) {
//                if(isContains(alreadyExist,userName))
//                    break;
//
//                max = allData.get(userName)[i-1];
//                userNameOB = userName;
//            }
//        }
//        return userNameOB;
//    }
//
//    private static boolean isContains(String[] alreadyExist, String userName){
//        for(int j = 0; j < 5; j++){
//            if (alreadyExist[j].equals(userName)) {
//                return true;
//            }
//        }
//        return false;
//    }
}
