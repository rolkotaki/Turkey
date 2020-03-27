
package model;

/**
 * Az osztály a pulykák érzési életkorát tárolja.
 */
public class ArriveAge {
    
    private int age;
    
    public ArriveAge() {
        this.age = database.Database.selectArriveAge();
    }

    public int getAge() {
        return age;
    }

    public void setAge() {
        this.age = database.Database.selectArriveAge();
    }
    
}
