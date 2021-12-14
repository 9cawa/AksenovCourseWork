package ru.mtuci.mainapp.entities;

import java.util.Objects;

public class Product {
    private String productCode;
    private String productName;
    private int cost;

    public Product(String productCode, String productName, int cost) {
        this.productCode = productCode;
        this.productName = productName;
        this.cost = cost;
    }

    public Product(String productCode) {
        this.productCode = productCode;
    }

    public Product() {
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

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "productCode: " + productCode + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Product))
            return false;
        Product product = (Product) o;
        return cost == product.cost && productCode.equals(product.productCode) && productName.equals(product.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCode, productName, cost);
    }

}

