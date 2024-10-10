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
                    System.out.println(allData.values());

                    allData.put(groupOfUsers[0], tmp);
                    System.out.println(allData.values());
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
}
