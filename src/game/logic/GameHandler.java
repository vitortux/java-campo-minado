package game.logic;

import java.util.Random;

import game.ui.GameUI;
import model.Difficulty;
import model.Node;

public class GameHandler {
    private final int rows;
    private final int columns;
    private final int bombs;
    private final Node[][] board;
    private final Random random = new Random();
    private final GameUI ui;
    private boolean isFirstMove = true;

    public GameHandler(Difficulty difficulty) {
        this.rows = difficulty.getRows();
        this.columns = difficulty.getColumns();
        this.bombs = difficulty.getBombs();
        this.board = new Node[rows][columns];
        this.ui = new GameUI(this);
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Node[][] getBoard() {
        return board;
    }

    public void initBoard() {
        for (int row = 0; row < rows; row++)
            for (int col = 0; col < columns; col++)
                board[row][col] = new Node();
    }

    public void placeBombs() {
        int placed = 0;

        while (placed < bombs) {
            int row = random.nextInt(rows);
            int col = random.nextInt(columns);

            if (Math.abs(row - 6) <= 1 && Math.abs(col - 6) <= 1) {
                continue;
            }

            if (!board[row][col].isBomb() && !board[row][col].isRevealed()) {
                board[row][col].setBomb(true);
                placed++;
            }
        }

        setBombsAround();
    }

    public void setBombsAround() {
        for (int row = 0; row < rows; row++)
            for (int col = 0; col < columns; col++)
                board[row][col].setBombsAround(countBombsAround(row, col));
    }

    private int countBombsAround(int row, int col) {
        int bombsCount = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0)
                    continue;

                int neighborRow = row + i;
                int neighborCol = col + j;

                if (neighborRow >= 0 && neighborRow < rows && neighborCol >= 0 && neighborCol < columns
                        && board[neighborRow][neighborCol].isBomb()) {
                    bombsCount++;
                }
            }
        }

        return bombsCount;
    }

    public void reveal(int row, int col) {
        if (board[row][col].isRevealed()) {
            return;
        }

        board[row][col].reveal();

        if (isFirstMove) {
            placeBombs();
            isFirstMove = false;
        }

        if (board[row][col].getBombsAround() == 0) {
            revealAdjacentCells(row, col);
        }
    }

    private void revealAdjacentCells(int row, int col) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0)
                    continue;
                int neighborRow = row + i;
                int neighborCol = col + j;

                if (isValidCell(neighborRow, neighborCol)) {
                    reveal(neighborRow, neighborCol);
                }
            }
        }
    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < columns;
    }

    public void start() {
        initBoard();
        reveal(6, 6);
        ui.renderGame();
    }
}
