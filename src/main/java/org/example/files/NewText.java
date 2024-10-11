package org.example.files;

import java.io.FileWriter;
import java.io.IOException;

public class NewText {
    public static void saveToFile(String str) {
        try {
            FileWriter writer = new FileWriter("C:/Users/user/Desktop/tmp/DesktopType/DesktopType/DesktopType/bin/Debug/net8.0-windows/Texts.txt", true);

            writer.write("$" + str + "$");
            writer.write("\n");

            writer.close();
        } catch (IOException e) {
            System.out.println("Ошибка при записи предложения пользователя в файл.");
        }
    }
}
