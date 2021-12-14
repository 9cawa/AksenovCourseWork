package ru.mtuci.mainapp.entities;

import java.util.List;

public class Order {
    private int ID;
    private String userLogin;
    private List<String> article;

    public Order(int ID, String userLogin, List<String> article) {
        this.ID = ID;
        this.userLogin = userLogin;
        this.article = article;
    }

    public Order(){ }

    @Override
    public String toString() {
        return "ORDER:\nID :" + ID + "; userLogin: " + userLogin  + "; article(s): " + article;
    }
}
