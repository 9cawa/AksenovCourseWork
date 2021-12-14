package ru.mtuci.databaseimporter;

public class Order {
    private int ID;
    private String userLogin;
    private String article;

    public Order(int ID, String userLogin, String article) {
        this.ID = ID;
        this.userLogin = userLogin;
        this.article = article;
    }

    public int getID() {
        return ID;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getArticle() {
        return article;
    }
}

