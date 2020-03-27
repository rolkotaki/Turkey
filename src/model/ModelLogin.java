
package model;

import database.Database;

/**
 * A felhasználónév és a jelszó begépelése után a model ellenőrzi, hogy az
 * adatok megfelelőek-e, és e szerint állítja be a model logikai változóját.
 */
public class ModelLogin {
    
    private static boolean correctDatas=false;

    public static boolean isCorrectDatas() {
        return correctDatas;
    }

    public static void setCorrectDatas(boolean correctDatas) {
        ModelLogin.correctDatas = correctDatas;
    }
    
    public static boolean checkUserName(String user) {
        if (user.equals(Database.selectUserName()))
            return true;       
        else
            return false;
    }
    
    public static boolean checkNewUserName(String user) {
        if (user.length()<26 && user.length()>0)
            return true;       
        else
            return false;
    }
    
    public static boolean checkPassword(String pwd) {
        if (pwd.equals(Database.selectPassword()))
            return true;
        else
            return false;
    }
    
    public static boolean checkNewPassword(String pwd1, String pwd2) {
        if (pwd1.equals(pwd2))
            return true;
        else
            return false;
    }
    
}
