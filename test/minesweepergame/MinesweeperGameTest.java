package minesweepergame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import minesweepergame.MinesweeperGame.Cell;
import minesweepergame.MinesweeperGame.Game;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MinesweeperGameTest {
    
    MinesweeperGame minesweeperToDesignTryToExposeNeighbors;
    MinesweeperGame minesweeperToDesignExpose;
    MinesweeperGame minesweeper;
    
    List<Integer> cellsToTryExposing;

    boolean exposeACellCalled;

    @Before
    public void setUp() {
        cellsToTryExposing = new ArrayList<>();
        
        minesweeper = new MinesweeperGame();
        
        minesweeperToDesignTryToExposeNeighbors = new MinesweeperGame(){
          @Override
          public void exposeACell(int row, int column) {
            exposeACellCalled = true;
          }
        };
        
        minesweeperToDesignExpose = new MinesweeperGame() {
          @Override
          public void tryToExposeNeighbor(int row, int column){
            cellsToTryExposing.add(row);
            cellsToTryExposing.add(column);
          }
        };
    }
    
    @Test
    public void exposeAnUnexposedCell(){
        minesweeperToDesignExpose.exposeACell(0, 0);
        assertEquals(Cell.EXPOSED, minesweeperToDesignExpose.cellStatus(0, 0));
    }
    
    @Test
    public void exposeAnExposedCell(){
        minesweeperToDesignExpose.exposeACell(0, 0);
        minesweeperToDesignExpose.exposeACell(0, 0);
        assertEquals(Cell.EXPOSED, minesweeperToDesignExpose.cellStatus(0, 0));
    }
    
    @Test
    public void alreadyExposedCellDoesNotTryToExposeNeighbors() {
        minesweeperToDesignExpose.exposeACell(1, 1);
        cellsToTryExposing.clear();

        minesweeperToDesignExpose.exposeACell(1, 1);
        List<Integer> empty = new ArrayList<Integer>();
        assertEquals(empty, cellsToTryExposing);
    }
    
    @Test
    public void exposedCellTriesToExposeNeighbors(){
        minesweeperToDesignExpose.exposeACell(1, 1);

        List<Integer> expected = Arrays.asList(2, 1, 1, 2, 0, 1, 1, 0, 0, 0, 0, 2, 2, 0, 2, 2);
        assertEquals(expected, cellsToTryExposing);
    }
    
    private void checkBounds(Runnable block) {
      try{
          block.run();
          fail("Selected cell is out of bounds");
      }catch(IndexOutOfBoundsException ex){
          assertTrue(true);
      }
    }
    
    @Test
    public void exposeCellRowLargerThanMax(){
        checkBounds(() -> minesweeperToDesignExpose.exposeACell(10, 1));
    }
    
    @Test
    public void exposeCellRowSmallerThanMin(){
        checkBounds(() -> minesweeperToDesignExpose.exposeACell(-1, 1));
    }
    
    @Test
    public void exposeCellColumnLargerThanMax(){
        checkBounds( () -> minesweeperToDesignExpose.exposeACell(1, 10));
    }
    
    @Test
    public void exposeCellColumnSmallerThanMin(){
        checkBounds( () -> minesweeperToDesignExpose.exposeACell(1, -1));
    }
    
    @Test
    public void tryToExposeCalledWithValidLocationWillCallExposeACell(){
        minesweeperToDesignTryToExposeNeighbors.tryToExposeNeighbor(0, 0);
        assertTrue(exposeACellCalled);
    }
    
    @Test
    public void tryToExposeCellRowLargerThanMaxWillNotCallExposeACell(){
        minesweeperToDesignTryToExposeNeighbors.tryToExposeNeighbor(10, 0);
        assertFalse(exposeACellCalled);
    }

    @Test
    public void tryToExposeCellRowSmallerThanMinWillNotCallExposeACell() {
        minesweeperToDesignTryToExposeNeighbors.tryToExposeNeighbor(-1, 0);
        assertFalse(exposeACellCalled);
    }

    @Test
    public void tryToExposeCellColumnLargerThanMaxWillNotCallExposeACell() {
        minesweeperToDesignTryToExposeNeighbors.tryToExposeNeighbor(0, 10);
        assertFalse(exposeACellCalled);
    }

    @Test
    public void tryToExposeCellColumnSmallerThanMinWillNotCallExposeACell() {
        minesweeperToDesignTryToExposeNeighbors.tryToExposeNeighbor(0, -1);
        assertFalse(exposeACellCalled);
    }
    
    @Test
    public void sealAnUnexposedCell(){
        minesweeper.toggleSeal(0, 0);
        assertEquals(Cell.SEALED, minesweeper.cellStatus(0, 0));
    }
    
    @Test
    public void sealAnExposedCell(){
        minesweeper.exposeACell(0, 0);
        minesweeper.toggleSeal(0, 0);
        assertEquals(Cell.EXPOSED, minesweeper.cellStatus(0, 0));
    }
    
    @Test
    public void sealedCellCannotBeExposed(){
        minesweeper.toggleSeal(0, 0);
        minesweeper.exposeACell(0, 0);
        assertEquals(Cell.SEALED, minesweeper.cellStatus(0, 0));
    }
    
    @Test
    public void unsealASealedCell(){
        minesweeper.toggleSeal(0, 0);
        minesweeper.toggleSeal(0, 0);
        assertEquals(Cell.UNEXPOSED, minesweeper.cellStatus(0, 0));
    }
    
    @Test
    public void whenExposeIsCalledOnASealedCellItDoesNotCallTryToExposeNeighbor(){
        minesweeperToDesignExpose.toggleSeal(1, 1);
        minesweeperToDesignExpose.exposeACell(1, 1);

        List<Integer> empty = new ArrayList<>();
        assertEquals(empty, cellsToTryExposing);
    }

    @Test
    public void verifyThatCellIsEmpty() {
        assertTrue(minesweeper.isEmpty(1, 1));
    }

    @Test
    public void verifyThatCellIsNotEmpty() {
        minesweeper.mined[0][0] = true;
        assertFalse(minesweeper.isEmpty(1, 1));
    }
    
    @Test
    public void whenExposeIsCalledOnAMinedCellItDoesNotCallTryToExposeNeighbor(){
        minesweeperToDesignExpose.mined[0][0] = true;
        minesweeperToDesignExpose.exposeACell(0, 0);
        List<Integer> empty = new ArrayList<>();
        assertEquals(empty, cellsToTryExposing);
    }

    @Test
    public void countAdjacentMinesForNonEdgeCell() {
        minesweeper.mined[1][1] = true; 
        minesweeper.mined[2][2] = true;
        assertEquals(2, minesweeper.getNumberOfAdjacentMines(2, 1));
    }

    @Test
    public void countNoAdjacentMinesForNonEdgeCell() {
        minesweeper.mined[2][1] = true;
        assertEquals(0, minesweeper.getNumberOfAdjacentMines(2, 1));
    }

    @Test
    public void countAdjacentMinesForEdgeCell() {
        minesweeper.mined[1][4] = true;
        minesweeper.mined[1][5] = true;
        minesweeper.mined[1][6] = true;
        minesweeper.mined[0][4] = true;
        minesweeper.mined[0][6] = true;

        assertEquals(5, minesweeper.getNumberOfAdjacentMines(0, 5));
    }

    @Test
    public void countAdjacentMinesForCornerCell() {
        minesweeper.mined[0][1] = true;
        minesweeper.mined[1][0] = true;
        minesweeper.mined[1][1] = true;

        assertEquals(3, minesweeper.getNumberOfAdjacentMines(0, 0));
    }

    @Test
    public void verifyThatCellIsAdjacent() {
        minesweeper.mined[0][0] = true;
        assertTrue(minesweeper.isAdjacent(1, 0));
    }

    @Test
    public void verifyThatCellIsNotAdjacent() {
        assertFalse(minesweeper.isAdjacent(1, 0));
    }

    @Test
    public void verifyThatMinedCellIsNotAdjacent() {
        minesweeper.mined[0][0] = true;
        minesweeper.mined[1][1] = true;
        assertFalse(minesweeper.isAdjacent(0, 0));
    }

    @Test
    public void thereAreTenMines() {
        minesweeper = MinesweeperGame.create();
        int numberOfMines = 0;
        for(int row = 0; row < 10; row++) {
            for(int column = 0; column < 10; column++) {
                if(minesweeper.mined[row][column])
                    numberOfMines++;
            }
        }
        assertEquals(10, numberOfMines);
    }

    @Test
    public void minesAreGeneratedRandomly() {
        MinesweeperGame minesweeperToDesignRandomMines = new MinesweeperGame();
        minesweeper.setRandomMineLocations();
        minesweeperToDesignRandomMines.setRandomMineLocations();

        boolean different = false;

        for(int row = 0; row < 10; row++) {
            for(int column = 0; column < 10; column++) {
                if(minesweeper.mined[row][column] != minesweeperToDesignRandomMines.mined[row][column])
                    different = true;
            }
        }

        assertTrue(different);
    }
    
    @Test
    public void testGameStateIsInProgressOnCreate(){
        assertEquals(Game.PROGRESS, minesweeper.checkGameState(0, 0));
    }
    
    @Test
    public void testGameStateIsLostWhenMinedCellIsExposed(){
        minesweeper.mined[0][0] = true;
        minesweeper.exposeACell(0, 0);
        assertEquals(Game.LOST, minesweeper.checkGameState(0, 0));
    }
    
    @Test
    public void testGameIsWonWhenAllNonMinedCellsAreExposedAndMinedCellsAreSealed(){
        minesweeper.mined[9][9] = false;
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                if(minesweeper.cellStatus(i, j) == Cell.UNEXPOSED && !minesweeper.mined[i][j]){
                    minesweeper.exposeACell(i, j);
                }
                if(minesweeper.mined[i][j]){
                    minesweeper.toggleSeal(i, j);
                }
            }
        }
        assertEquals(Game.WON, minesweeper.checkGameState(9, 9));
    }
}
