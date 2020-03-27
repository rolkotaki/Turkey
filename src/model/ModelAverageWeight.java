
package model;

import javax.swing.DefaultListModel;

/**
 * A pulykák tömegének átlagát kiszámoló modell osztály.
 */
public class ModelAverageWeight {
    
    private double average;
    
    public ModelAverageWeight() {
        this.average=0;
    }
    
    public double countAverage(DefaultListModel dlm) {
        double x=0;
        for (int i = 0; i < dlm.getSize(); i++)
            try {
                x += Double.parseDouble(dlm.getElementAt(i).toString());
            } catch (NumberFormatException e) {
                x+=0;
            }
        average = x / dlm.getSize();
        average = DoubleFormat.doubleFormat(average);
        return average;
    }

    public double getAverage() {
        return average;
    }
}
