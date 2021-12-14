package ru.mtuci.mainapp;

import ru.mtuci.mainapp.entities.Order;
import ru.mtuci.mainapp.entities.Product;
import ru.mtuci.mainapp.exceptions.ThereIsNoSuchProductInDB;
import ru.mtuci.mainapp.exceptions.ThisProductAlreadyInDBException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** DAO-класс для работы с БД **/
public class Dao {

    Connection connection;

    public Dao(Connection connection) {
        this.connection = connection;
    }

    /** Поиск в БД товара с указанным артикулом **/
    Product findProduct(String productCode) throws SQLException {
        Product returnProduct = new Product();
        try (PreparedStatement statement = connection
                .prepareStatement("SELECT * FROM Products p WHERE p.ArticleID = ?")){
            statement.setString(1, productCode);
            try (ResultSet result = statement.executeQuery()){
                while (result.next()) {
                    returnProduct.setProductCode(result.getString("ArticleID"));
                    returnProduct.setProductName(result.getString("Product"));
                    returnProduct.setCost(result.getInt("Cost"));
                }
            }
        }
        if (returnProduct.getProductCode() == null)
            return null;
        return returnProduct;
    }

    /** Создание нового товара **/
    Product createProduct(Product product) throws SQLException {
        if (findProduct(product.getProductCode()) != null)
            throw new ThisProductAlreadyInDBException();
        else {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO Products VALUES(?,?,?)")) {
                statement.setString(1, product.getProductCode());
                statement.setString(2, product.getProductName());
                statement.setInt(3, product.getCost());
                statement.execute();
            }
        }
        return product;
    }

    /** Изменение информации о товаре **/
    Product updateProduct(Product product) throws SQLException {
        if (findProduct(product.getProductCode()) == null)
            throw new ThereIsNoSuchProductInDB();
        else {
            try (PreparedStatement statement = connection.prepareStatement
                    ("UPDATE Products " +
                            "SET Product = ?, Cost = ? " +
                            "WHERE ArticleID = ?")) {
                statement.setString(1, product.getProductName());
                statement.setInt(2, product.getCost());
                statement.setString(3, product.getProductCode());
                statement.execute();
            }
        }
        return product;
    }

    /** Удаление товара и всех упоминаний о нем в заказах **/
    void deleteProduct(String productCode) throws SQLException {
        if (findProduct(productCode) == null)
            throw new ThereIsNoSuchProductInDB();
        else {
            try (PreparedStatement statement = connection.prepareStatement
                    ("DELETE FROM Orders o " +
                            "WHERE o.Article = ?; " +
                            "DELETE FROM Products p " +
                            "WHERE p.ArticleID = ?")) {
                statement.setString(1, productCode);
                statement.setString(2, productCode);
                statement.execute();
            }
        }
    }

    /** Метод нахождения ID последнего заказа **/
    int getLastOrderID() throws SQLException {
        try (Statement statement = connection.createStatement()){
            try (ResultSet result = statement.executeQuery("SELECT ID FROM Orders o ORDER BY o.ID DESC")) {
                result.next();
                return result.getInt("ID");
            }
        }
    }

    /** Создание заказа.
     * Для указанного пользователя в БД создается новый заказ с заданным списком товаров **/
    Order createOrder(String userLogin, List<Product> products) throws SQLException {
        int ID = getLastOrderID() + 1; //Ищем ID последнего заказа и прибавляем 1 = ID текущего заказа
        ArrayList<String> productsArticles = new ArrayList<>(); //В эту коллекцию будем записывать артикулы продуктов
        // для возвращаемого значения Order
        try (PreparedStatement statement = connection.prepareStatement
                ("INSERT INTO Orders VALUES(?,?,?)")) {
            for (Product product : products) {
                if (findProduct(product.getProductCode()) == null)
                    throw new ThereIsNoSuchProductInDB();
                productsArticles.add(product.getProductCode());
                statement.setInt(1, ID);
                statement.setString(2, userLogin);
                statement.setString(3, product.getProductCode());
                statement.execute();
            }
        }
        return new Order(ID, userLogin, productsArticles);
    }

}

