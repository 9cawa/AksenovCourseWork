package ru.mtuci.mainapp.exceptions;

import java.sql.SQLException;

public class ThereIsNoSuchProductInDB extends SQLException {
    @Override
    public String getMessage() {
        return "В базе данных нет такого товара!";
    }
}

