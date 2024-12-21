package game;

import java.util.Random;

import model.Difficulty;

public class Game {
    private final int rows;
    private final int columns;
    private final int bombs;
    private final int[][] board;
    private final Random random = new Random();

    public Game(Difficulty difficulty) {
        this.rows = difficulty.getRows();
        this.columns = difficulty.getColumns();
        this.bombs = difficulty.getBombs();
        this.board = new int[rows][columns];
    }

    @SuppressWarnings("squid:S106")
    void renderGame() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int node = board[i][j];

                if (node == 0) {
                    System.out.print((i + j) % 2 == 0 ? "🌳" : "🟩");
                } else if (node == 1) {
                    System.out.print("💣");
                } else if (node == 2) {
                    System.out.print("🚩");
                }
            }

            System.out.println();
        }
    }

    void placeBombs() {
        int placed = 0;

        while (placed < bombs) {
            int row = random.nextInt(rows);
            int col = random.nextInt(columns);

            if (board[row][col] != 1) {
                board[row][col] = 1;
                placed++;
            }
        }
    }
}