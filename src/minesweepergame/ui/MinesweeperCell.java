package minesweepergame.ui;

import javax.swing.*;

public class MinesweeperCell extends JToggleButton{
    public final int row;
    public final int column;
    public MinesweeperCell(int theRow, int theColumn) {
        row = theRow;
        column = theColumn;
        setSize(100, 100);
    }

    public void updateButton() {
        getDisabledIcon();
        setEnabled(false);
    }
}
