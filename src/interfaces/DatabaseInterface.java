
package interfaces;

/**
 * Az adatbázis eléréséhez szükséges konstansok interfésze.
 */

public interface DatabaseInterface {
    public static final String URL = "jdbc:mysql://localhost:3306/turkeybase?"
            + "useUnicode=true&characterEncoding=utf-8";
    public static final String USER = "root";
    public static final String PASSWORD = "";
    public static final String DRIVER = "com.mysql.jdbc.Driver";    
}

/*
 * Forrás: http://stackoverflow.com/questions/3275524/java-mysql-utf8-problem
 */