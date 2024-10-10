package org.example.user_data;

public class DesktopData {
    private String userName;
    private String password;
    private int[] records;

    public DesktopData(int[] records, String userName, String password) {
        this.records = records;
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int[] getRecords() {
        return records;
    }

    public void setRecords(int[] records) {
        this.records = records;
    }
}
