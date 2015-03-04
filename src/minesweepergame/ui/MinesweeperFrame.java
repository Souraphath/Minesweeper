package minesweepergame.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.util.ArrayList;
import minesweepergame.MinesweeperGame;
import minesweepergame.MinesweeperGame.Cell;
import minesweepergame.MinesweeperGame.Game;

public class MinesweeperFrame extends JFrame{
    private static final int BOARD_SIZE = 10;
    private MinesweeperGame minesweeperGame = MinesweeperGame.create();
    public static ArrayList<MinesweeperCell> cellArrayList = new ArrayList<>();
    
    @Override
    protected void frameInit(){
        super.frameInit();
        setLayout(new GridLayout(10, 10));
        for(int i = 0; i < BOARD_SIZE; i++){
            for(int j = 0; j < BOARD_SIZE; j++){
                MinesweeperCell cell = new MinesweeperCell(i, j);
                cell.setBackground(Color.BLACK);
                cellArrayList.add(cell);
                getContentPane().add(cell);
                cell.addMouseListener(new CellClickedHandler());
            }
        }
    }
    
    public static void main(String[] args){
        JFrame frame = new MinesweeperFrame();
        frame.setSize(500, 500);
        frame.setVisible(true);
    }
    
    private class CellClickedHandler extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            MinesweeperCell clickedCell = (MinesweeperCell) mouseEvent.getSource();
            if(SwingUtilities.isRightMouseButton(mouseEvent) || mouseEvent.isControlDown()){
                handleRightClick(clickedCell);
            }
            else{
                handleLeftClick(clickedCell);
            }
            checkWinOrLoss(clickedCell);
        }

        public void handleRightClick(MinesweeperCell cell) {
            Cell selected = minesweeperGame.cellStatus(cell.row, cell.column);
            if(selected == Cell.UNEXPOSED){
                cell.setText("S");
                cell.setEnabled(false);
            }
            if(selected == Cell.SEALED){
                cell.setText("");
                cell.setEnabled(true);
            }
            minesweeperGame.toggleSeal(cell.row, cell.column);
        }

        public void handleLeftClick(MinesweeperCell cell) {
            if(minesweeperGame.cellStatus(cell.row, cell.column) != Cell.SEALED) {
                minesweeperGame.exposeACell(cell.row, cell.column);
                if (minesweeperGame.isEmpty(cell.row, cell.column)) {
                    displayCells();
                }
                if (minesweeperGame.isAdjacent(cell.row, cell.column)) {
                    cell.setText(Integer.toString(minesweeperGame.getNumberOfAdjacentMines(cell.row, cell.column)));
                    cell.updateButton();
                }
            }
        }
        
        public void displayCells(){
            for(MinesweeperCell cell : cellArrayList) {
                if(minesweeperGame.cellStatus(cell.row, cell.column) == Cell.EXPOSED) {
                    if(minesweeperGame.isAdjacent(cell.row, cell.column)) {
                        cell.setText(Integer.toString(minesweeperGame.getNumberOfAdjacentMines(cell.row, cell.column)));
                    }
                    cell.setSelected(true);
                    cell.updateButton();
                }
            }
        }

        public void checkWinOrLoss(MinesweeperCell cell) {
            if (minesweeperGame.checkGameState(cell.row, cell.column) == Game.LOST) {
                cell.setText("M");
                JOptionPane.showMessageDialog(cell, "You Lose");
            }
            if (minesweeperGame.checkGameState(cell.row, cell.column) == Game.WON) {
                JOptionPane.showMessageDialog(cell, "You Win");
            }
        }
    }
}