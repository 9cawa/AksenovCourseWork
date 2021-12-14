package ru.mtuci.databaseimporter;

public class Product {
    private String productCode;
    private String productName;
    private int cost;

    public Product(String productCode, String productName, int cost) {
        this.productCode = productCode;
        this.productName = productName;
        this.cost = cost;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getProductName() {
        return productName;
    }

    public int getCost() {
        return cost;
    }
}

