package br.gov.dataprev.sdcgestaoaap.extension;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleJDBCUtil {
    // Configurações de conexão (modifique conforme seu ambiente)
    private static final String URL = "jdbc:unisys:mcpsql:Unisys.DMSII:resource=bdtabtst;host=dtprjcv1;port=2012;user=ddmigrajdbccv1";
    private static final String USER = "ddmigrajdbccv1";
    private static final String PASSWORD = "ddmigrajdbccv1";

    // Bloco estático para carregar o driver Oracle
    static {
        try {
            Class.forName("com.unisys.jdbc.mcpsql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver mcpsql não encontrado.", e);
        }
    }

    /**
     * Retorna uma conexão com o banco de dados.
     *
     * @return Conexão ativa.
     * @throws SQLException Caso ocorra algum erro ao conectar.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
