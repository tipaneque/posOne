package com.posone.dao;

import com.posone.model.Product;
import javafx.scene.control.Alert;

import java.sql.*;

public class ProductDAO {
    private Connection connection;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;
    private Product product;

    public ProductDAO(){
        resultSet = null;
        connection = null;
        preparedStatement = null;
    }

    public void getConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posone", "root", "212397");
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void insertData(String id, String name, int price){
        getConnection();
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO product VALUES (id, name, price) (?,?,?)");
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setFloat(3, price);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public Product getProduct(String id){
        System.out.println("ID recebido " + id);

        getConnection();
        try{
            preparedStatement = connection.prepareStatement("SELECT * FROM product WHERE id = ?");
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()){
                Alert alert =new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Produto nao encontrado");
                alert.show();
            }else{
                while (resultSet.next()){
                    product = new Product(
                            resultSet.getString("id"),
                            resultSet.getString("prod_name"),
                            resultSet.getFloat("price")
                    );
                }
            }
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return product;
    }
}

