package org.example;

import java.io.FileWriter;
import java.io.IOException;

public class FileLogic {
    public static void saveToFile(String str) {
        try {
            FileWriter writer = new FileWriter("asks.txt", true);

            writer.write(str);
            writer.write("\n");

            writer.close();
        } catch (IOException e) {
            System.out.println("Ошибка при записи предложения пользователя в файл.");
        }
    }
}
