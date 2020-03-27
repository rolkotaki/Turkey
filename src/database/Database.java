
package database;

import interfaces.DatabaseInterface;
import interfaces.ModelInterface;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 * A Database osztály feladata, hogy kapcsolatot létesítsen vagy zárjon az
 * adatbázissal, valamint a lekérdezések is ebben az osztályban vannak megírva.
 * A lekérdezések mellett természetesen törlések és frissítések is vannak
 * írva.
 */
public class Database implements DatabaseInterface, ModelInterface {

    private static Connection connection;

    /**
     * Kapcsolatot létesít az adatbázissal.
     */
    public static void openConnection() {
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Hiba! Hiányzik a JDBC driver.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Hiba! Nem sikerült megnyitni a kapcsolatot az adatbázis-szerverrel.");
        }
    }

    /**
     * Bezárja az adatbázis kapcsolatot.
     */
    public static void closeConnection() {
        try {
            if (!connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Hiba! Nem sikerült lezárni a kapcsolatot az adatbázis-szerverrel.");
        }
    }
    
    /**
     * A szezon kezdetén felmerült kezdeti adatokat beírja az adatbázisba.
     */
    public static boolean insertInitialDatas(String date, int age, int bucknum, int hennum, int buckstock,
            int henstock, double unit, ArrayList<Double> bucklist, ArrayList<Double> henlist,
            double buckWeight, double henWeight, int onStock) {
        openConnection();
        try {
            PreparedStatement ps1 = connection.prepareStatement(
                    "INSERT INTO turkey_arrive VALUES ( ? , ? , ? , ? )");
            ps1.setString(1, date);
            ps1.setInt(2, age);
            ps1.setInt(3, bucknum);
            ps1.setInt(4, hennum);
            ps1.executeUpdate();
            PreparedStatement ps2 = connection.prepareStatement(
                    "INSERT INTO stock_start VALUES ( ? , ? , ? )");
            ps2.setInt(1, buckstock);
            ps2.setInt(2, henstock);
            ps2.setInt(3, onStock);
            ps2.executeUpdate();
            PreparedStatement ps3 = connection.prepareStatement(
                    "INSERT INTO unit_fodder VALUES (1, ? )");
            ps3.setDouble(1, unit);
            ps3.executeUpdate();
            PreparedStatement ps4;
            for (int i=0; i<bucklist.size(); i++) {
                ps4 = connection.prepareStatement(
                        "INSERT INTO consumption_prescribed VALUES ( ? , ? , ? )");
                ps4.setString(1, Integer.toString(i+7));
                ps4.setString(2, Double.toString(bucklist.get(i)));
                ps4.setString(3, Double.toString(henlist.get(i)));
                ps4.executeUpdate();
            }
            PreparedStatement ps5 = connection.prepareStatement(
                    "INSERT INTO turkey_number VALUES ( ? , ? , ? )");
            ps5.setInt(1, bucknum);
            ps5.setInt(2, hennum);
            int allturkey = bucknum+hennum;
            ps5.setInt(3, allturkey);
            ps5.executeUpdate();
            PreparedStatement ps6 = connection.prepareStatement(
                    "INSERT INTO stock_now VALUES ('fodder', ? )");
            ps6.setInt(1, (onStock-(buckstock+henstock)));
            ps6.executeUpdate();
            PreparedStatement ps7 = connection.prepareStatement(
                    "INSERT INTO weighing VALUES ( 0, ? , ? , ? )");
            double all=((bucknum*buckWeight)+(hennum*henWeight))/(bucknum+hennum);
            ps7.setDouble(1, buckWeight);
            ps7.setDouble(2, henWeight);
            ps7.setDouble(3, all);
            ps7.executeUpdate();
            PreparedStatement ps8 = connection.prepareStatement(
                    "INSERT INTO fodder_buck VALUES ( ? , ? )");
            ps8.setString(1, date);
            ps8.setInt(2, buckstock);
            ps8.executeUpdate();
            PreparedStatement ps9 = connection.prepareStatement(
                    "INSERT INTO fodder_hen VALUES ( ? , ? )");
            ps9.setString(1, date);
            ps9.setInt(2, henstock);
            ps9.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
        closeConnection();
        return true;
    }
    
    /**
     * A pulykák érkezésével kapcsolatos adatok lekérdezése.
     * Ezek: érkezés dátuma, pulykák darabszáma, életkor.
     */
    public static ResultSet selectTurkeyArrive () {
        ResultSet rs = null;
        try {
            Statement s = connection.createStatement();
            rs=s.executeQuery("SELECT arrive_date AS date, age AS age, buck_number AS buck, "+
                    "hen_number AS hen FROM turkey_arrive");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return rs;
    }
    
    /**
     * A pulykák elhalálozásával és selejtezésével kapcsolatos adatok
     * lekérdezése. Ezek a dátum és a darabszámra vonatkozó adatok.
     */
    public static ResultSet selectTurkeyLost () {
        ResultSet rs = null;
        try {
            Statement s = connection.createStatement();
            rs=s.executeQuery("SELECT date AS date, faulty_buck AS faultyBuck, "+
                    "faulty_hen AS faultyHen, death_buck AS deathBuck, "+
                    "death_hen AS deathHen FROM turkey_lost");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
    
    /**
     * Ez a lekérdezés a legnagyobb dátum lekérdezésére szolgál. Ennek
     * segítségével megtudhatjuk, hogy a felhasználó mikor vitt be utoljára
     * selejtezésre vagy elhalálozásra vonatkozó adatokat.
     */
    public static Date maxDateTurkeyLost () {
        openConnection();
        Date date=null;
        ResultSet rs=null;
        try {
            Statement s = connection.createStatement();
            rs=s.executeQuery("SELECT MAX(date) FROM turkey_lost");
            rs.next();
            date=rs.getDate(1);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return null;
        }
        closeConnection();
        return date;
    }
    
    /**
     * Az alábbi lekérdezés segítségével lekérdezhetjük, hogy egy bizonyos
     * napon hány darab pulykát selejteztünk le, vagy halálozott el.
     */
    public static int selectDayTurkeyLost(String turkey, Date day) {
        openConnection();
        String date = SDFDATE.format(day);
        int lost=0;
        ResultSet rs=null;
        try {
            Statement s1 = connection.createStatement();
            rs=s1.executeQuery("SELECT SUM(faulty_"+turkey+") FROM turkey_lost "+
                    "WHERE date = '"+date+"'");
            rs.next();
            lost+=rs.getInt(1);
            Statement s2 = connection.createStatement();
            rs=s2.executeQuery("SELECT SUM(death_"+turkey+") FROM turkey_lost "+
                    "WHERE date = '"+date+"'");
            rs.next();
            lost+=rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return lost;
        }
        closeConnection();
        return lost;
    }
    
    /**
     * Lekérdezzük, hogy egy adott dátum előtt összesen hány bak pulyka
     * halálozott el, vagy lett leselejtezve.
     */
    public static int allTurkeyLost (String turkey, Date d) {
        openConnection();
        String date = SDFDATE.format(d);
        int all=0;
        ResultSet rs=null;
        try {
            Statement s1 = connection.createStatement();
            rs=s1.executeQuery("SELECT SUM(faulty_"+turkey+") FROM turkey_lost "+
                    "WHERE date < '"+date+"'");
            rs.next();
            all+=rs.getInt(1);
            Statement s2 = connection.createStatement();
            rs=s2.executeQuery("SELECT SUM(death_"+turkey+") FROM turkey_lost "+
                    "WHERE date < '"+date+"'");
            rs.next();
            all+=rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return all;
        }
        closeConnection();
        return all;
    }
    
    /**
     * Lekérdezzük, hogy jelenleg hány darab élő pulyka (bak és tojó) van.
     */
    public static int selectTurkeyNumber(String turkey) {
        openConnection();
        int number=0;
        ResultSet rs=null;
        try {
            Statement s = connection.createStatement();
            rs=s.executeQuery("SELECT "+turkey+" FROM turkey_number");
            rs.next();
            number += rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return number;
        }
        closeConnection();
        return number;
    }
    
    /**
     * Kitörli a turkey_lost adatbázis tábla tartalmát.
     */    
    public static boolean deleteTurkeyLost() {
        openConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate("DELETE FROM turkey_lost");
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
        closeConnection();
        return true;
    }
    
    /**
     * A turkey_lost nevű adatbázis táblába beírja az adatokat, amiket a
     * felhasználó a megfelelő JTable-be bevitt. A Mentés gomb eseményeként
     * hajtódik végre.
     */
    public static boolean insertTurkeyLost(String date, String fb, String fh, String db, String dh) {
        openConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO turkey_lost VALUES ( ? , ? , ? , ? , ? )");
            ps.setString(1, date);
            ps.setString(2, fb);
            ps.setString(3, fh);
            ps.setString(4, db);
            ps.setString(5, dh);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
        closeConnection();
        return true;
    }
    
    /**
     * Frissíti a pulykák aktuális létszámát. Először kitörli a régi adatokat,
     * majd frissíti azokat.
     */
    public static void updateTurkeyNumber() {
        openConnection();
        int buck=0, hen=0, all=0;
        try {
            buck=Database.selectTurkeyNumberByDate(new Date(), TURKEY[BUCK], BUCK);
            hen=Database.selectTurkeyNumberByDate(new Date(), TURKEY[HEN], HEN);
            all=buck+hen;
            openConnection();
            Statement s = connection.createStatement();
            s.executeUpdate("DELETE FROM turkey_number");            
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO turkey_number VALUES ( ? , ? , ? )");
            ps.setInt(1, buck);
            ps.setInt(2, hen);
            ps.setInt(3, all);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
        }
        closeConnection();
    }
    
    /**
     * Ez a lekérdezés a pulykákhoz kivitt termény mennyiségét kérdezi le.
     */
    public static ResultSet selectFodderConsumption(String turkey) {
        ResultSet rs = null;
        try {
            Statement s = connection.createStatement();
            rs=s.executeQuery("SELECT date AS date, fodder AS fodder FROM fodder_"+turkey);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
    
    /**
     * A pulykákhoz kivitt termény mennyiséget kérdezi le, egy adott
     * idő intervallum alapján.
     */
    public static int selectFodderConsByDate(String turkey, Date from, Date to) {
        openConnection();
        int number=0;
        String dateFrom = SDFDATE.format(from);
        String dateTo = SDFDATE.format(to);
        ResultSet rs=null;
        try {
            Statement s = connection.createStatement();
            rs=s.executeQuery("SELECT fodder_"+turkey+".fodder FROM fodder_"+turkey+
                    " WHERE date BETWEEN '"+dateFrom+"' AND '"+dateTo+"'");
            rs.next();
            number += rs.getInt(1);
        } catch (SQLException e) {
            closeConnection();
            return number;
        }
        closeConnection();
        return number;
    }
    
    /**
     * A heti valós fogyasztás tábla lekérdezése.
     */
    public static ResultSet selectConsumptionRealAll(String turkey) {
        ResultSet rs = null;
        try {
            Statement s = connection.createStatement();
            rs=s.executeQuery("SELECT week AS week, cons_real AS cons FROM consumption_"+turkey);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
    
    /**
     * A pulykák heti valós fogyasztását kérdezi le az alábbi metódus.
     */
    public static int selectConsumptionReal(String turkey, int week) {
        openConnection();
        int cons=0;
        try {
            Statement s = connection.createStatement();
            ResultSet rs=s.executeQuery("SELECT consumption_"+turkey+".cons_real FROM "+
                    "consumption_"+turkey+" WHERE week="+week);
            rs.next();
            cons=rs.getInt(1);
        } catch (SQLException e) {
            closeConnection();
            return cons;
        }
        closeConnection();
        return cons;
    }
    
    /**
     * A pulykák heti előírt fogyasztását kérdezi le egy adott héten.
     */
    public static double selectConsPrescribed(String turkey, int week) {
        openConnection();
        double cons=0;
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT consumption_prescribed.prescribed_"+turkey+" FROM consumption_prescribed "+
                    "WHERE consumption_prescribed.week="+week);
            ResultSet rs=ps.executeQuery();
            rs.next();
            cons=rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
        }
        closeConnection();
        return cons;
    }
    
    /**
     * A zsák-kilogramm párosra vonatkozó váltószámot kérdezi le.
     */
    public static double selectUnitFodder() {
        openConnection();
        double unit=0;
        try {
            Statement s = connection.createStatement();
            ResultSet rs=s.executeQuery("SELECT unit_fodder.kg FROM unit_fodder");
            rs.next();
            unit=rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
        }
        closeConnection();
        return unit;
    }
    
    /**
     * Az utolsó takarmánykivitel dátumát kérdezi le.
     */
    public static Date maxDateFodderConsumption(String turkey) {
        openConnection();
        Date date=null;
        ResultSet rs=null;
        try {
            Statement s = connection.createStatement();
            rs=s.executeQuery("SELECT MAX(date) FROM fodder_"+turkey);
            rs.next();
            date=rs.getDate(1);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return null;
        }
        closeConnection();
        return date;
    }
    
    /**
     * Kitörli a takarmány kivitelre vonatkozó adatokat.
     */
    public static boolean deleteFodderConsumption(String turkey) {
        openConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate("DELETE FROM fodder_"+turkey);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
        closeConnection();
        return true;
    }
    
    /**
     * Kitörli a heti valós fogyasztásra vonatkozó adatokat.
     */
    public static boolean deleteConsumptionReal(String turkey) {
        openConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate("DELETE FROM consumption_"+turkey);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
        closeConnection();
        return true;
    } 
    
    /**
     * A takarmány kivitelre vonatkozó adatokat beírja az adatbázisba.
     */
    public static boolean insertFodderConsumption(String turkey, String date, String fodder) {
        openConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO fodder_"+turkey+" VALUES ( ? , ? )");
            ps.setString(1, date);
            ps.setString(2, fodder);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
        closeConnection();
        return true;
    }
    
    /**
     * A heti valós fogyasztásra vonatkozó adatokat beírja az adatbázisba.
     */
    public static boolean insertConsumptionReal(String turkey, String week, String cons) {
        openConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO consumption_"+turkey+" VALUES ( ? , ? )");
            ps.setString(1, week);
            ps.setString(2, cons);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
        closeConnection();
        return true;
    }
    
    /**
     * Summázva lekérdezi az eddigi összes takarmány kivitelt, és kilogrammban
     * megkapjuk az összeget.
     */
    public static int selectAllFodderCons(String turkey) {
        openConnection();
        int fodder=0;
        try {
            Statement s = connection.createStatement();
            ResultSet rs=s.executeQuery("SELECT SUM(fodder) FROM fodder_"+turkey);
            rs.next();
            fodder+=rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
        }
        closeConnection();
        return fodder;
    }
    
    /**
     * Summázva lekérdezi egy adott dátum előtti összes takarmány kivitelt, és
     * kilogrammban megkapjuk az összeget.
     */
    public static int selectAllFodderConsByDate(String turkey, Date date) {
        openConnection();
        String d = SDFDATE.format(date);
        int fodder=0;
        try {
            Statement s = connection.createStatement();
            ResultSet rs=s.executeQuery("SELECT SUM(fodder) FROM fodder_"+turkey+" "+
                    "WHERE date <= '"+d+"'");
            rs.next();
            fodder+=rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
        }
        closeConnection();
        return fodder;
    }
    
    /**
     * Summázva lekérdezi az eddigi összes takarmány fogadást, és kilogrammban
     * megkapjuk az összeget.
     */
    public static int selectAllReceivedFodder() {
        openConnection();
        int fodder=0;
        try {
            Statement s = connection.createStatement();
            ResultSet rs=s.executeQuery("SELECT SUM(fodder_receive) FROM stock");
            rs.next();
            fodder+=rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
        }
        closeConnection();
        return fodder;
    }
    
    /**
     * Summázva lekérdezi egy adott dátum előtti összes takarmány fogadást, és
     * kilogrammban megkapjuk az összeget.
     */
    public static int selectAllReceivedFodderByDate(Date date) {
        openConnection();
        String d = SDFDATE.format(date);
        int fodder=0;
        try {
            Statement s = connection.createStatement();
            ResultSet rs=s.executeQuery("SELECT SUM(fodder_receive) FROM stock "+
                    "WHERE date <= '"+d+"'");
            rs.next();
            fodder+=rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
        }
        closeConnection();
        return fodder;
    }
    
    /**
     * Frissíti a raktáron lévő takarmány mennyiségét.
     */
    public static void updateStockNow() {
        openConnection();
        int fodder = 0, stock = 0;
        try {
            fodder+=selectAllFodderCons(TURKEY[BUCK]);
            fodder+=selectAllFodderCons(TURKEY[HEN]);
            stock+=selectStockStart();
            stock+=selectAllReceivedFodder();
            openConnection();
            Statement s = connection.createStatement();
            s.executeUpdate("UPDATE stock_now SET amount = "+stock+" - "+fodder+
                    " WHERE name = 'fodder'");
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
        }
        closeConnection();
    }
    
    /**
     * A raktárkészlettel kapcsolatos adatok lekérdezése.
     */
    public static ResultSet selectStock() {
        ResultSet rs = null;
        try {
            Statement s = connection.createStatement();
            rs=s.executeQuery("SELECT date AS date, straw_buck AS strawBuck, "+
                    "straw_hen AS strawHen, fodder_order AS fodderOrder, "+
                    "fodder_receive AS fodderReceive FROM stock");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
    
    /**
     * A kezdő raktárkészlet lekérdezése.
     */
    public static int selectStockStart() {
        openConnection();
        int stock = 0;        
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT on_stock FROM stock_start");
            rs.next();
            stock+=rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
        }
        closeConnection();
        return stock;
    }
    
    /**
     * A jelenlegi raktárkészlet lekérdezése.
     */
    public static int selectStockNow() {
        openConnection();
        int stock = 0;        
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT amount FROM stock_now WHERE name = 'fodder'");
            rs.next();
            stock+=rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
        }
        closeConnection();
        return stock;
    }
    
    /**
     * A raktárkészlet lekérdezése egy adott időpontban.
     */
    public static int selectStockByDate(Date date) {
        openConnection();
        String d = SDFDATE.format(date);
        int fodder = 0, stock = 0;        
        try {
            fodder+=selectAllFodderConsByDate(TURKEY[BUCK], date);
            fodder+=selectAllFodderConsByDate(TURKEY[HEN], date);
            stock+=selectStockStart();
            stock+=selectAllReceivedFodderByDate(date);
            stock-=fodder;
        } catch (Exception e) {
            e.printStackTrace();
            closeConnection();
        }
        closeConnection();
        return stock;
    }
    
    /**
     * Lekérdezi a maximális dátumot a raktárkészlet táblából.
     */
    public static Date maxDateStock() {
        openConnection();
        Date date=null;
        ResultSet rs=null;
        try {
            Statement s = connection.createStatement();
            rs=s.executeQuery("SELECT MAX(date) FROM stock");
            rs.next();
            date=rs.getDate(1);
        } catch (SQLException e) {
            closeConnection();
            return null;
        }
        closeConnection();
        return date;
    }
    
    /**
     * Kitörli a raktárkészlet táblájának tartalmát.
     */
    public static boolean deleteStock() {
        openConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate("DELETE FROM stock");
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
        closeConnection();
        return true;
    }
    
    /**
     * A raktárkészlet táblájába beírja az adatokat.
     */
    public static boolean insertStock(String date, String strawbuck, String strawhen, String order, String receive) {
        openConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO stock VALUES ( ? , ? , ? , ? , ? )");
            ps.setString(1, date);
            ps.setString(2, strawbuck);
            ps.setString(3, strawhen);
            ps.setString(4, order);
            ps.setString(5, receive);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
        closeConnection();
        return true;
    }
    
    /**
     * A gyógyszerezéssel kapcsolatos információk lekérdezése egy adott
     * dátum alapján.
     */
    public static String selectMedicineByDate(Date date) {
        String medicine="";
        openConnection();
        String d = SDFDATE.format(date);
        try {
            Statement s = connection.createStatement();
            ResultSet rs=s.executeQuery(
                    "SELECT description FROM medicine "+
                    "WHERE date = '"+d+"'");
            rs.next();
            medicine=rs.getString(1);
        } catch (SQLException e) {
            closeConnection();
            return medicine;
        }
        closeConnection();
        return medicine;
    }
    
    /**
     * A technikai eseményekkel kapcsolatos adatok lekérdezése egy adott
     * dátum alapján.
     */
    public static String selectTechEventByDate(Date date) {
        String medicine="";
        openConnection();
        String d = SDFDATE.format(date);
        try {
            Statement s = connection.createStatement();
            ResultSet rs=s.executeQuery(
                    "SELECT description FROM technical_event "+
                    "WHERE date = '"+d+"'");
            rs.next();
            medicine=rs.getString(1);
        } catch (SQLException e) {
            closeConnection();
            return medicine;
        }
        closeConnection();
        return medicine;
    }
    
    /**
     * Az árváltozással kapcsolatos információk lekérdezése egy adott
     * dátum alapján.
     */
    public static String selectPriceChangeByDate(Date date) {
        String medicine="";
        openConnection();
        String d = SDFDATE.format(date);
        try {
            Statement s = connection.createStatement();
            ResultSet rs=s.executeQuery(
                    "SELECT description FROM price_change "+
                    "WHERE date = '"+d+"'");
            rs.next();
            medicine=rs.getString(1);
        } catch (SQLException e) {
            closeConnection();
            return medicine;
        }
        closeConnection();
        return medicine;
    }
    
    /**
     * A gyógyszerrel kapcsolatos adatokat beírja az adatbázis megfelelő
     * táblájába.
     */
    public static boolean insertMedicine(String date, String text) {
        openConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO medicine VALUES ( ? , ? )");
            ps.setString(1, date);
            ps.setString(2, text);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
        closeConnection();
        return true;
    }
    
    /**
     * A technikai eseményekkel kapcsolatos adatokat beírja az adatbázis
     * megfelelő táblájába.
     */
    public static boolean insertTechEvent(String date, String text) {
        openConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO technical_event VALUES ( ? , ? )");
            ps.setString(1, date);
            ps.setString(2, text);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
        closeConnection();
        return true;
    }
    
    /**
     * Az árváltozással kapcsolatos adatokat beírja az adatbázis megfelelő
     * táblájába.
     */
    public static boolean insertPriceChange(String date, String text) {
        openConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO price_change VALUES ( ? , ? )");
            ps.setString(1, date);
            ps.setString(2, text);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
        closeConnection();
        return true;
    }
    
    /**
     * A gyógyszerezéssel kapcsolatos adatbázis tábla tartalmának törlése.
     */
    public static boolean deleteMedicine() {
        openConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate("DELETE FROM medicine");
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
        closeConnection();
        return true;
    }
    
    /**
     * A technikai eseményekkel kapcsolatos adatbázis tábla tartalmának törlése.
     */
    public static boolean deleteTechEvent() {
        openConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate("DELETE FROM technical_event");
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
        closeConnection();
        return true;
    }
    
    /**
     * Az árváltozással kapcsolatos adatbázis tábla tartalmának törlése.
     */
    public static boolean deletePriceChange() {
        openConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate("DELETE FROM price_change");
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
        closeConnection();
        return true;
    }
    
    /**
     * Lekérdezi a heti méréseket tartalmazó adattábla tartalmát.
     */
    public static ResultSet selectWeighing() {
        ResultSet rs = null;
        try {
            Statement s = connection.createStatement();
            rs=s.executeQuery("SELECT week AS week, buck_weight AS buckWeight, "+
                    "hen_weight AS henWeight, all_average_weight AS allWeight "+
                    "FROM weighing");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
    
    /**
     * A heti méréseket megjelenítő táblázat tartalmát beírja az
     * adatbázisba.
     */
    public static boolean insertWeighing(String week, String buck, String hen, String all) {
        openConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO weighing VALUES ( ? , ? , ? , ? )");
            ps.setString(1, week);
            ps.setString(2, buck);
            ps.setString(3, hen);
            ps.setString(4, all);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
        closeConnection();
        return true;
    }
    
    /**
     * Kitörli az összes adatot a heti méréseket tartalmazó adatbázis
     * táblából.
     */
    public static boolean deleteWeighing() {
        openConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate("DELETE FROM weighing");
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
        closeConnection();
        return true;
    }
    
    /**
     * A kezdeti adatok lekérdezése, egy listában tér vissza.
     */
    public static ArrayList<Integer> selectArriveNumber() {
        ArrayList<Integer> list=new ArrayList<Integer>();
        openConnection();
        try {
            Statement s = connection.createStatement();
            ResultSet rs=s.executeQuery("SELECT buck_number AS buck, "
                    + "hen_number AS hen FROM turkey_arrive");
            rs.next();
            list.add(rs.getInt("buck"));
            list.add(rs.getInt("hen"));
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            list=null;
            return list;
        }
        closeConnection();
        return list;
    }
    
    /**
     * A kezdeti adatok lekérdezése, egy listában tér vissza.
     */
    public static int selectArriveAge() {
        int result=0;
        openConnection();
        try {
            Statement s = connection.createStatement();
            ResultSet rs=s.executeQuery("SELECT age AS age FROM turkey_arrive");
            rs.next();
            result=rs.getInt("age");
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
        }
        closeConnection();
        return result;
    }
    
    /**
     * A kezdeti adatok lekérdezése, egy listában tér vissza.
     */
    public static ArrayList<Integer> selectStockStartDatas() {
        ArrayList<Integer> list=new ArrayList<Integer>();
        openConnection();
        try {
            Statement s = connection.createStatement();
            ResultSet rs=s.executeQuery("SELECT buck_stock AS buckStock, hen_stock AS henStock, "
                    + "on_stock AS onStock FROM stock_start");
            rs.next();
            list.add(rs.getInt("buckStock"));
            list.add(rs.getInt("henStock"));
            list.add(rs.getInt("onStock"));
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            list=null;
            return list;
        }
        closeConnection();
        return list;
    }
    
    /**
     * Az érkezési tömegek lekérdezése egy listába.
     */
    public static ArrayList<Double> selectArriveWeights() {
        ArrayList<Double> list=new ArrayList<Double>();
        openConnection();
        try {
            Statement s = connection.createStatement();
            ResultSet rs=s.executeQuery("SELECT buck_weight AS buckWeight, "
                    + "hen_weight AS henWeight FROM weighing WHERE week=0");
            rs.next();
            list.add(rs.getDouble("buckWeight"));
            list.add(rs.getDouble("henWeight"));
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            list=null;
            return list;
        }
        closeConnection();
        return list;
    }
    
    /**
     * A pulykák érkezési dátumának lekérdezése.
     */
    public static Date selectArriveDate() {
        Date date=null;
        openConnection();
        try {
            Statement s = connection.createStatement();
            ResultSet rs=s.executeQuery("SELECT arrive_date FROM turkey_arrive");
            rs.next();
            date=rs.getDate(1);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return date;
        }
        closeConnection();
        return date;
    }
    
    /**
     * A bakok előírt fogyasztásának lekérdezése.
     */
    public static ArrayList<Double> selectBuckPrescribedCons() {
        ArrayList<Double> list=new ArrayList<Double>();
        openConnection();
        try {
            Statement s = connection.createStatement();
            ResultSet rs=s.executeQuery("SELECT prescribed_buck FROM consumption_prescribed");
            while(rs.next())
                list.add(rs.getDouble(1));
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            list=null;
            return list;
        }
        closeConnection();
        return list;
    }
    
    /**
     * A tojók előírt fogyasztásának lekérdezése.
     */
    public static ArrayList<Double> selectHenPrescribedCons() {
        ArrayList<Double> list=new ArrayList<Double>();
        openConnection();
        try {
            Statement s = connection.createStatement();
            ResultSet rs=s.executeQuery("SELECT prescribed_hen FROM consumption_prescribed");
            while(rs.next())
                list.add(rs.getDouble(1));
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            list=null;
            return list;
        }
        closeConnection();
        return list;
    }
    
    /**
     * A pulykák selejtezését és elhalálozását kérdezi le a metódus, egy adott
     * időintervallumban.
     */
    public static ResultSet selectTurkeyLostPeriod(String from, String to) {
        ResultSet rs=null;
        try {
            Statement s = connection.createStatement();
            rs=s.executeQuery("SELECT date AS Dátum, death_buck AS Halál_bak, death_hen AS Halál_tojó, "
                    + "faulty_buck AS Selejt_bak, faulty_hen AS Selejt_tojó "
                    + "FROM turkey_lost "
                    + "WHERE date BETWEEN '"+from+"' AND '"+to+"'");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
    
    /**
     * A pulykák elhalálozását és selejtezését vizsgálja az alábbi függvény.
     * Egyszerre egy, vagy több adatot is summázhatunk, átlagolhatunk,
     * valamint lekérdezhetjük a minimum illetve a maximum értéket is, mindezt
     * egy adott időtartam alatt.
     */
    public static ResultSet selectTurkeyLostPeriodFunction(String function, String from, String to, Vector v) {
        ResultSet rs=null;
        String datas = "";
        //összerakom a lekérdezni kívánt adatokat, a megfelelő
        //függvénnyel együtt
        for(int i=0; i<v.size(); i++)
                datas+=function+"("+v.get(i)+")"+", ";
        datas=datas.substring(0, datas.length()-2);
        try {
            Statement s = connection.createStatement();
            rs=s.executeQuery("SELECT "+datas
                    + " FROM turkey_lost "
                    + "WHERE date BETWEEN '"+from+"' AND '"+to+"'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
    
    /**
     * A pulykák selejtezését és elhalálozását kérdezi le napi szinten summázva.
     * A kapott értékeket az alábbi függvény summázza, átlagolja, vagy pedig
     * kiválasztja a minimum, maximum értéket.
     */
    public static int selectTurkeyLostBothFunction(String function, String from, String to) {
        openConnection();
        int result = 0;
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT "+function+"(death_buck + death_hen + faulty_buck + "
                    + "faulty_hen) AS lost FROM turkey_lost "
                    + "WHERE date BETWEEN '"+from+"' AND '"+to+"'");
            rs.next();
            result=rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
        }
        closeConnection();
        return result;
    }
    
    /**
     * A bakok vagy tojók számára kivitt takarmánymennyiséget kérdezi le az
     * alábbi metódus, egy adott időintervallumban.
     */
    public static ResultSet selectFodderPeriod(String turkey, String from, String to) {
        ResultSet rs=null;
        try {
            Statement s = connection.createStatement();
            rs=s.executeQuery("SELECT date AS Dátum, fodder AS Takarmány_kg "
                    + "FROM fodder_"+turkey+" "
                    + "WHERE date BETWEEN '"+from+"' AND '"+to+"'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
    
    /**
     * Az alábbi lekérdezés a takarmánykivitelt kérdezi le, napi szinten
     * összeadva a bakoknak és tojóknak kivitt takarmány mennyiségét.
     */
    public static ResultSet selectFodderBothPeriod(String from, String to) {
        ResultSet rs=null;
        try {
            Statement s = connection.createStatement();
            rs=s.executeQuery("SELECT b.date AS Dátum, (b.fodder + h.fodder) AS Takarmány_kg "
                    + "FROM fodder_buck b INNER JOIN fodder_hen h ON "
                    + "(b.date = h.date) "
                    + "WHERE b.date BETWEEN '"+from+"' AND '"+to+"' "
                    + "AND h.date BETWEEN '"+from+"' AND '"+to+"'");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
    
    /**
     * Egy adott időintervallumban lekérdezi a takarmánykivitelt summázva,
     * átlagolva, vagy a minimum illetve maximum értéket lekérdezve.
     */
    public static int selectFodderPeriodFunction(String function, String turkey, String from, String to) {
        openConnection();
        int sum=0;
        try {
            Statement s = connection.createStatement();
            ResultSet rs=s.executeQuery("SELECT "+function+"(fodder) AS Összeg FROM "
                    + "fodder_"+turkey+" "
                    + "WHERE date BETWEEN '"+from+"' AND '"+to+"'");
            rs.next();
            sum=rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
        }
        closeConnection();
        return sum;
    }
    
    /**
     * Egy adott időintervallumban lekérdezi a takarmánykivitelt summázva,
     * átlagolva, vagy a minimum illetve maximum értéket lekérdezve.
     * Ez a metódus az összes pulykára vonatkozik.
     */
    public static int selectFodderBothFunction(String function, String from, String to) {
        openConnection();
        int result=0;
        try {
            Statement s = connection.createStatement();
            s.executeUpdate("CREATE TEMPORARY TABLE rstbl (SELECT (b.fodder + h.fodder) AS fodder FROM "
                    + "fodder_buck b INNER JOIN fodder_hen h ON "
                    + "(b.date = h.date) "
                    + "WHERE b.date BETWEEN '"+from+"' AND '"+to+"' "
                    + "AND h.date BETWEEN '"+from+"' AND '"+to+"')");
            Statement s2 = connection.createStatement();
            ResultSet rs=s2.executeQuery("SELECT "+function+"(fodder) FROM rstbl");
            rs.next();
            result=rs.getInt(1);
            s.executeUpdate("DROP TABLE rstbl");
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
        }
        closeConnection();
        return result;
    }
    
    /**
     * A pulykák valós fogyasztását kérdezi le. A lekérdezés egyszerre
     * kérdezi le a bakok és tojók fogyasztását.
     */
    public static ResultSet selectConsRealBoth() {
        ResultSet rs=null;
        try {
            Statement s = connection.createStatement();
            rs=s.executeQuery("SELECT b.week AS week, (b.cons_real + h.cons_real) AS cons "
                    + "FROM consumption_buck b INNER JOIN consumption_hen h ON "
                    + "(b.week = h.week)");            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
    
    /**
     * Egy adott időintervallumban lekérdezi a valós fogyasztást summázva,
     * átlagolva, vagy a minimum illetve a maximum értéket lekérdezve.
     */
    public static int selectConsRealFunction(String function, String turkey) {
        openConnection();
        int sum=0;
        try {
            Statement s = connection.createStatement();
            ResultSet rs=s.executeQuery("SELECT "+function+"(cons_real) FROM "
                    + "consumption_"+turkey);
            rs.next();
            sum=rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
        }
        closeConnection();
        return sum;
    }
    
    /**
     * A pulykák valós fogyasztását kérdezi le, amely mind a bakokra, mind a
     * tojókra vonatkozik. Utána ebből az ideiglenes táblából lekérdezhetjük a
     * minimum vagy maximum értéket, illetve summázhatjuk és átlagolhatjuk is az
     * értékeket.
     */
    public static int selectConsRealBothFunction(String function) {
        openConnection();
        int result=0;
        try {
            Statement s = connection.createStatement();
            s.executeUpdate("CREATE TEMPORARY TABLE rstbl (SELECT (b.cons_real + h.cons_real) AS cons "
                    + "FROM consumption_buck b INNER JOIN consumption_hen h ON "
                    + "(b.week = h.week))");
            Statement s2 = connection.createStatement();
            ResultSet rs=s2.executeQuery("SELECT "+function+"(cons) FROM rstbl");
            rs.next();
            result=rs.getInt(1);
            s.executeUpdate("DROP TABLE rstbl");
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
        }
        closeConnection();
        return result;
    }
    
    /**
     * A raktár tábla tartalmát kérdezi le egy adott időtartam alapján.
     */
    public static ResultSet selectStockPeriod(String from, String to) {
        ResultSet rs=null;
        try {
            Statement s = connection.createStatement();
            rs=s.executeQuery("SELECT date AS date, fodder_order AS fOrder, "
                    + "fodder_receive AS fReceive, (straw_buck + straw_hen) AS straw "
                    + "FROM stock "
                    + "WHERE date BETWEEN '"+from+"' AND '"+to+"'");          
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
    
    /**
     * A raktár tábla tartalmát kérdezi le egy adott időtartam alatt. Ezen
     * adatokat utána summázhatjuk vagy átlagolhatjuk, illetve lekérdezhetjük a
     * minimum vagy maximum értéket is. Az SQL műveletet paraméterként kapja meg.
     */
    public static ResultSet selectStockFunction(String function, Vector v, String from, String to) {
        ResultSet rs=null;
        String datas = "";
        for(int i=0; i<v.size(); i++)
                datas+=function+"("+v.get(i)+")"+", ";
        datas=datas.substring(0, datas.length()-2);
        try {
            Statement s = connection.createStatement();
            rs=s.executeQuery("SELECT "+datas
                    + " FROM stock "
                    + "WHERE date BETWEEN '"+from+"' AND '"+to+"'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
    
    /**
     * A gyógyszerezéssel kapcsolatos adatbázis táblából kérdezünk le adott
     * időtartam alatt, és egy felhasználó által begépelt szövegre rákeresve.
     */
    public static ResultSet selectMedicineByDateText(String text, String from, String to) {
        ResultSet rs = null;
        try {
            Statement s = connection.createStatement();
            rs=s.executeQuery("SELECT date AS date, description AS description "
                    + "FROM medicine "
                    + "WHERE date BETWEEN '"+from+"' AND '"+to+"' "
                    + "AND description LIKE '%"+text+"%'");            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
    
    /**
     * A technikai eseményekkel kapcsolatos adatbázis táblából kérdezünk le adott
     * időtartam alatt, és egy felhasználó által begépelt szövegre rákeresve.
     */
    public static ResultSet selectTechEventByDateText(String text, String from, String to) {
        ResultSet rs = null;
        try {
            Statement s = connection.createStatement();
            rs=s.executeQuery("SELECT date AS date, description AS description "
                    + "FROM technical_event "
                    + "WHERE date BETWEEN '"+from+"' AND '"+to+"' "
                    + "AND description LIKE '%"+text+"%'");            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
    
    /**
     * Az árváltozással kapcsolatos adatbázis táblából kérdezünk le adott
     * időtartam alatt, és egy felhasználó által begépelt szövegre rákeresve.
     */
    public static ResultSet selectPriceChangeByDateText(String text, String from, String to) {
        ResultSet rs = null;
        try {
            Statement s = connection.createStatement();
            rs=s.executeQuery("SELECT date AS date, description AS description "
                    + "FROM price_change "
                    + "WHERE date BETWEEN '"+from+"' AND '"+to+"' "
                    + "AND description LIKE '%"+text+"%'");            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
    
    /**
     * A bakok vagy tojók átminősítésének beírása a megfelelő adatbázis
     * táblába.
     */
    public static boolean insertTurkeyNumberChange(String date, String number, int index) {
        openConnection();
        String col="";
        switch(index) {
            case 0: col="buck_to_hen";break;
            case 1: col="hen_to_buck";break;
        }
        Date d = null;
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT date FROM turkey_number_change "
                    + "WHERE date = '"+date+"'");
            try {
                rs.next();
                d = rs.getDate(1);
            } catch (Exception e) {
            }
            if (d==null) {
                PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO turkey_number_change (date, "+col+") "
                    + "VALUES ( ? , ? )");
                ps.setString(1, date);
                ps.setString(2, number);
                ps.executeUpdate();
            } else {
                PreparedStatement ps = connection.prepareStatement(
                    "UPDATE turkey_number_change SET "+col+" = ? "
                        + "WHERE date = '"+date+"'");
                ps.setString(1, number);
                ps.executeUpdate();
            }            
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
        closeConnection();
        return true;
    }
    
    /**
     * Az átminősítés következtében bekövetkezett létszámváltozás korrekciójához
     * szükséges ez a lekérdezés, hiszen tudnunk kell, hogy mennyivel nőtt a
     * pulykák száma.
     */
    public static int selectTurkeyNumberChange(String date, int index) {
        openConnection();
        String s="";
        switch(index) {
            case 0: s="hen_to_buck";break;
            case 1: s="buck_to_hen";break;
        }
        int result=0;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT SUM("+s+") FROM turkey_number_change "
                    + "WHERE date <= '"+date+"'");
            rs.next();
            result=rs.getInt(1);
            st.executeUpdate("DROP TABLE rstbl");
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
        }
        closeConnection();
        return result;
    }
    
    /**
     * A pulykák létszámának lekérdezése egy adott dátumban. Figyelembe kell
     * venni az átminősítést is.
     */
    public static int selectTurkeyNumberByDate(Date date, String turkey, int index) {
        openConnection();
        String d = SDFDATE.format(date);
        String col1="";
        String col2="";
        switch(index) {
            case 0: col1="hen_to_buck";col2="buck_to_hen";break;
            case 1: col1="buck_to_hen";col2="hen_to_buck";break;
        }
        int result=0;
        ResultSet rs=null;
        try {
            Statement st = connection.createStatement();
            rs = st.executeQuery("SELECT "+turkey+"_number FROM turkey_arrive");
            rs.next();
            result+=rs.getInt(1);
            rs = st.executeQuery("SELECT SUM(faulty_"+turkey+" + death_"+turkey+") FROM turkey_lost "+
                    "WHERE date < '"+d+"'");
            rs.next();
            result-=rs.getInt(1);
            rs = st.executeQuery("SELECT SUM("+col1+") FROM turkey_number_change "
                    + "WHERE date < '"+d+"'");
            rs.next();
            result+=rs.getInt(1);
            rs = st.executeQuery("SELECT SUM("+col2+") FROM turkey_number_change "
                    + "WHERE date < '"+d+"'");
            rs.next();
            result-=rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
        }
        closeConnection();
        return result;
    }
    
    /**
     * A pulykák leadási súlyának lekérdezése. Ez megegyezik a legutolsó heti
     * mérési adattal.
     */
    public static double selectTurkeySellWeight(String turkey) {
        openConnection();
        double result=0;
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT "+turkey+"_weight FROM weighing "
                    + "WHERE week = (SELECT MAX(week) FROM weighing)");
            rs.next();
            result=rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
        }
        closeConnection();
        return result;
    }
    
    /**
     * A felhasználónév lekérdezése.
     */
    public static String selectUserName() {
        openConnection();
        String user = null;
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT username FROM user");
            rs.next();
            user=rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
        }
        closeConnection();
        return user;        
    }
    
    /**
     * A jelszó lekérdezése.
     */
    public static String selectPassword() {
        openConnection();
        String pwd = null;
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT password FROM user");
            rs.next();
            pwd=rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
        }
        closeConnection();
        return pwd;        
    }
    
    public static boolean updateLoginDatas(String user, String pwd) {
        openConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate("UPDATE user SET username = '"+user+"'");
            s.executeUpdate("UPDATE user SET password = '"+pwd+"'");
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
        closeConnection();
        return true;
    }
    
    /**
     * Az alábbi metódus minden tábla tartalmát törli. Tehát üres lesz az
     * adatbázis.
     */
    public static boolean deleteAll() {
        openConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate("DELETE FROM turkey_arrive");
            s.executeUpdate("DELETE FROM turkey_number");
            s.executeUpdate("DELETE FROM turkey_lost");
            s.executeUpdate("DELETE FROM turkey_number_change");
            s.executeUpdate("DELETE FROM consumption_prescribed");
            s.executeUpdate("DELETE FROM stock_start");
            s.executeUpdate("DELETE FROM stock_now");
            s.executeUpdate("DELETE FROM stock");
            s.executeUpdate("DELETE FROM fodder_buck");
            s.executeUpdate("DELETE FROM fodder_hen");
            s.executeUpdate("DELETE FROM consumption_buck");
            s.executeUpdate("DELETE FROM consumption_hen");
            s.executeUpdate("DELETE FROM weighing");
            s.executeUpdate("DELETE FROM medicine");
            s.executeUpdate("DELETE FROM technical_event");
            s.executeUpdate("DELETE FROM price_change");
            s.executeUpdate("DELETE FROM unit_fodder");            
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
        closeConnection();
        return true;
    }
    
}

/*
 * Forrás: http://www.stardeveloper.com/articles/display.html?article=2003090401&page=1
 * http://dev.mysql.com/downloads/connector/j/
 * http://dev.mysql.com/doc/refman/5.0/en/integer-types.html
 * http://www.java2s.com/Code/Java/Database-SQL-JDBC/InsertRecordsUsingPreparedStatement.htm
 * http://stackoverflow.com/questions/3651985/compare-dates-in-mysql
 * http://dev.mysql.com/doc/refman/5.1/en/alter-table.html
 * http://dev.mysql.com/doc/refman/5.0/en/update.html
 * http://dev.mysql.com/doc/refman/5.0/en/join.html
 * http://stackoverflow.com/questions/5859391/create-temporary-table-from-select-statement-without-using-create-table
 */
