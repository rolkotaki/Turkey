
package view;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.LookAndFeel;
import javax.swing.table.TableCellRenderer;

/**
 * A MultiLineHeaderRenderer osztály segítségével azt érjük el, hogy a különböző
 * JTable komponensek oszlopcímei több sorban, középre igazítva, és kedvünk
 * szerint formázva jelenjen meg.
 */

public class MultiLineHeaderRenderer extends JPanel implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        this.setAlignmentY(JPanel.CENTER_ALIGNMENT);
        JLabel label;
        removeAll();
        String[] header = ((String) value).split("\n");
        setLayout(new GridLayout(header.length, 1));
        for (String s : header) {
            label = new JLabel(s, JLabel.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 12));
            LookAndFeel.installColorsAndFont(label, "TableHeader.background",
                    "TableHeader.foreground", "TableHeader.font");
            add(label);
        }
        LookAndFeel.installBorder(this, "TableHeader.cellBorder");
        return this;
    }
}

/*
 * Forrás: http://www.java2s.com/Code/Java/Swing-JFC/Multilinetableheader.htm
 */