package ru.mtuci.mainapp.exceptions;

public class ThisProductAlreadyInDBException extends NullPointerException {
    @Override
    public String getMessage() {
        return "Продукт с этим артикулом уже есть в базе данных!";
    }
}
