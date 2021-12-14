package ru.mtuci.databaseimporter;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

public class DataBaseImporter {

    //Строки с запросом к БД для создания таблиц Products и Orders
    private static final String createProducts = "CREATE TABLE Products" +
            "(ArticleID VARCHAR(255) PRIMARY KEY," +
            "Product VARCHAR(255) NOT NULL, " +
            "Cost INT NOT NULL);";
    private static final String createOrders = "CREATE TABLE Orders" +
            "(ID INT NOT NULL, " +
            "UserName VARCHAR(255) NOT NULL, " +
            "Article VARCHAR(255), " +
            "foreign key (Article) references Products(ArticleID));";


    public static void main(String[] args) {
        List<Order> orders = new ArrayList<>(); //Список, куда записываются заказы (ID, UserName, Article)
        List<Product> products = new ArrayList<>(); //Список, куда записываются товары (ArticleID, Product, Cost)
        readFile(args[0],orders,products); //Считываем из файла заказы

        String connectionUrl = "jdbc:h2:tcp://localhost/~/OnlineShop/OnlineShop";

        try (Connection connection = DriverManager.getConnection(connectionUrl, "aleksandr", "123")) {
            createTableInDB(connection);

            importProductsToDB(connection, products);
            importOrdersToDB(connection,orders);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Метод, считывающий из файла информацию о заказах и записывающий ее в два списка **/
    static void readFile(String path,List<Order> orders, List<Product> product) {
        Scanner scanner;
        try {
            scanner = new Scanner(Paths.get(path));
        } catch (IOException e) {
            System.out.println("Не удалось открыть файл");
            return;
        }

        Set<String> Articles = new HashSet<>(); //В эту коллекцию будут добавляться артикулы
        // товаров для соблюдения уникальности
        String[] tmp; //В эту переменную в каждой итерации записываем текущую строку
        while (scanner.hasNextLine()) {
            tmp = scanner.nextLine().split(",");

            //В список orders добавляем объект класса
            orders.add(new Order(Integer.parseInt(tmp[0]),tmp[1],tmp[2]));

            //Если в коллекции Articles нет такого артикула то добавляем в список новый продукт
            if (Articles.add(tmp[2]))
                product.add(new Product(tmp[2], tmp[3], Integer.parseInt(tmp[4])));
        }
    }

    /** Метод, создающий таблицу в базе данных **/
    static void createTableInDB(Connection connection) throws SQLException{
        try (Statement statement = connection.createStatement()){
            statement.execute(DataBaseImporter.createProducts);
            statement.execute(DataBaseImporter.createOrders);
        }
    }

    /** Метод, который заполняет таблицу Products **/
    static void importProductsToDB(Connection connection, List<Product> products) throws SQLException{
        String sql = "INSERT INTO Products VALUES(?,?,?)";
        for (Product product : products) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, product.getProductCode());
                statement.setString(2, product.getProductName());
                statement.setInt(3, product.getCost());
                statement.execute();
            }
        }
    }

    /** Метод, который заполняет таблицу Orders **/
    static void importOrdersToDB(Connection connection, List<Order> orders) throws SQLException {
        String sql = "INSERT INTO Orders VALUES(?,?,?)";
        for (Order order : orders) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, order.getID());
                statement.setString(2, order.getUserLogin());
                statement.setString(3, order.getArticle());
                statement.execute();
            }
        }
    }

}

