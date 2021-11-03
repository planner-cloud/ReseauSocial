package BD;
import java.sql.*;

public class  JavaMySQL {

    Connection connection;
    public JavaMySQL(){}

    public Connection openConnection() {

         String url = "jdbc:mysql://localhost:3306/reseausocial";
         String username = "Aymen";
         String password  = "aymen2027";
         try {
             this.connection = DriverManager.getConnection(url,username,password);
             //System.out.println("Connécter à la BD !");
             return connection;
         }
         catch (Exception e){
             e.printStackTrace();
             System.out.println("Problem de connexion à la BD !");
             return null;
         }
    }

    public int updateQuery(String sql) throws SQLException{
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement.executeUpdate();
    }
    public ResultSet selectQuery(String sql) throws SQLException {
        PreparedStatement statement2 = connection.prepareStatement(sql);
        ResultSet res = statement2.executeQuery();
        return res;
    }
    public void close() throws SQLException {
        connection.close();
    }
}