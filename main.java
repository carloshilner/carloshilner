package br.gov.dataprev.sdcgestaoaap.extension;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        // Utilizando try-with-resources para gerenciar o fechamento automático dos recursos
        try (Connection connection = OracleJDBCUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM tb0006")) {

            // Processa o resultado da consulta
            while (resultSet.next()) {
                int id = resultSet.getInt("nu_banc");
                String nome = resultSet.getString("cs_banc");
                System.out.println("ID: " + id + ", Nome: " + nome);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
