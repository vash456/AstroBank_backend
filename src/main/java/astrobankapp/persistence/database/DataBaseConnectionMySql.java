package astrobankapp.persistence.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnectionMySql {
    private static DataBaseConnectionMySql instance;
    private final Connection connection;



    private static final String url = "jdbc:mysql://localhost:3306/astro_bank";
    private static final String username = "root";
    private static final String password = "";

    private DataBaseConnectionMySql(){

        try{
            connection = DriverManager.getConnection(url, username, password);

        }catch (SQLException e){
            throw new RuntimeException("Error al conectar la base de datos" , e);
        }

    }


    public static synchronized DataBaseConnectionMySql getInstance(){
        if(instance == null){
            instance = new DataBaseConnectionMySql();
        }
        return instance;
    }

    public Connection getConnection(){
        System.out.println("Conectado a la base de datos");
        return connection;
    }
}




