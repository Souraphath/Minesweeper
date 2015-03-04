package minesweepergame;

import java.util.Random;

public class MinesweeperGame {
    private final int BOARD_SIZE = 10;
    private final int NUMBER_OF_MINES = 10;
    private Cell[][] cell = new Cell[BOARD_SIZE][BOARD_SIZE];
    public enum Cell {EXPOSED, UNEXPOSED, SEALED}
    public enum Game {PROGRESS, LOST, WON}
    public boolean[][] mined = new boolean[BOARD_SIZE][BOARD_SIZE];
    
    MinesweeperGame(){
        for(int i = 0; i < BOARD_SIZE; i++){
            for(int j = 0; j < BOARD_SIZE; j++){
                cell[i][j] = Cell.UNEXPOSED;
                mined[i][j] = false;
            }
        }
    }
    
    public static MinesweeperGame create(){
        MinesweeperGame minesweeper = new MinesweeperGame();
        minesweeper.setRandomMineLocations();
        return minesweeper;
    }

    public void setRandomMineLocations(){
        Random rand = new Random();
        int row, column;
        int minesPlaced = 0;

        while(minesPlaced < NUMBER_OF_MINES) {
            row = rand.nextInt(BOARD_SIZE);
            column = rand.nextInt(BOARD_SIZE);
            if(!mined[row][column]) {
                mined[row][column] = true;
                minesPlaced++;
            }
        }
    }

    public Cell cellStatus(int row, int column) {
        return cell[row][column];
    }

    public void exposeACell(int row, int column) {
        if(cell[row][column] == Cell.UNEXPOSED && verifyBounds(row, column)){
            cell[row][column] = Cell.EXPOSED;
            if(!mined[row][column] && isEmpty(row, column)){
                tryToExposeNeighbor(row + 1, column);
                tryToExposeNeighbor(row, column + 1);
                tryToExposeNeighbor(row - 1, column);
                tryToExposeNeighbor(row, column - 1);
                tryToExposeNeighbor(row - 1, column - 1);
                tryToExposeNeighbor(row - 1, column + 1);
                tryToExposeNeighbor(row + 1, column - 1);
                tryToExposeNeighbor(row + 1, column + 1);
            }
        }
    }
    
    public void tryToExposeNeighbor(int row, int column){
        if(!(row < 0 || row > 9 || column < 0 || column > 9)){
            exposeACell(row, column);
        }
    }
    
    public boolean verifyBounds(int row, int column){
        return (!(row < 0 || row > BOARD_SIZE - 1 || column < 0 || column > BOARD_SIZE - 1));
    }
    
    public void toggleSeal(int row, int column){
        if(cell[row][column] == Cell.UNEXPOSED){
            cell[row][column] = Cell.SEALED;
        }
        else if(cell[row][column] == Cell.SEALED){
            cell[row][column] = Cell.UNEXPOSED;
        }
    }

    public int getNumberOfAdjacentMines(int row, int column) {
        int numberOfAdjacentMines = 0;

        for(int i = row - 1; i <= row + 1; i++) {
            for(int j = column - 1; j <= column + 1; j++) {
                if(!(i == row && j == column)) {
                    if(verifyBounds(i, j) && mined[i][j])
                        numberOfAdjacentMines++;
                }
            }
        }

        return numberOfAdjacentMines;
    }

    public boolean isAdjacent(int row, int column) {
        return (getNumberOfAdjacentMines(row, column) != 0 && !mined[row][column]);
    }

    public boolean isEmpty(int row, int column) {
        return (getNumberOfAdjacentMines(row, column) == 0);
    }
    
    public Game checkGameState(int row, int column){
        Game gameState;
        if(mined[row][column] && cellStatus(row, column) == Cell.EXPOSED){
            gameState = Game.LOST;
        }
        else{
            gameState = Game.WON;
            for(int i = 0; i < BOARD_SIZE; i++){
                for(int j = 0; j < BOARD_SIZE; j++){
                    if(!mined[i][j] && cell[i][j] == Cell.UNEXPOSED){
                        gameState = Game.PROGRESS;
                    }
                    if(!mined[i][j] && cell[i][j] == Cell.SEALED){
                        gameState = Game.PROGRESS;
                    }
                    if(mined[i][j] && cell[i][j] == Cell.UNEXPOSED){
                        gameState = Game.PROGRESS;
                    }
                }
            }
        }

        return gameState;
    }
}
