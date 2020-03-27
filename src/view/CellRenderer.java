
package view;

import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Az alábbi osztály a DefaultTableCellRenderer osztály leszármazottja.
 * Ennek felüldefiniálásával elérhető, hogy az adatok középre igazítottan
 * jelenjenek meg.
 */
public class CellRenderer extends DefaultTableCellRenderer {
    
    public CellRenderer() {
        setHorizontalAlignment(JLabel.CENTER);
    }
}

/*
 * Forrás: http://stackoverflow.com/questions/4103114/align-text-in-a-jtable-cell-while-maintaining-jtable-default-renderer
 */
