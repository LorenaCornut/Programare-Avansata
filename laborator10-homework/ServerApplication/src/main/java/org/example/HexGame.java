package org.example;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class HexGame {
    private static final int SIZE = 11;
    private final String gameId;
    private final Player player1;
    private volatile Player player2;
    private final AtomicReference<char[][]> board;
    private final AtomicReference<Character> currentPlayer;
    private final ScheduledExecutorService timer;
    private final AtomicInteger blackTime;
    private final AtomicInteger whiteTime;
    private final AtomicBoolean gameOver;
    private final boolean[][] visited;

    public HexGame(String gameId, Player player1) {
        this.gameId = gameId;
        this.player1 = player1;
        this.board = new AtomicReference<>(new char[SIZE][SIZE]);
        this.currentPlayer = new AtomicReference<>('B');
        this.timer = Executors.newSingleThreadScheduledExecutor();
        this.blackTime = new AtomicInteger(300);
        this.whiteTime = new AtomicInteger(300);
        this.gameOver = new AtomicBoolean(false);
        this.visited = new boolean[SIZE][SIZE];
        initializeBoard();
    }

    private void initializeBoard() {
        char[][] newBoard = new char[SIZE][SIZE];
        for (char[] row : newBoard) Arrays.fill(row, '.');
        board.set(newBoard);
    }

    public synchronized String joinGame(Player player) {
        if (player2 != null) return "Game full";
        player2 = player;
        startTimer();
        player1.notify("Game started! You are Black (B)");
        return "Game started! You are White (W)";
    }

    private void startTimer() {
        timer.scheduleAtFixedRate(() -> {
            if (currentPlayer.get() == 'B') {
                if (blackTime.decrementAndGet() <= 0) endGame("White wins (time)");
            } else {
                if (whiteTime.decrementAndGet() <= 0) endGame("Black wins (time)");
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public synchronized String makeMove(Player player, int x, int y) {
        if (gameOver.get()) return "Game over! " + getGameResult();

        char playerColor = player == player1 ? 'B' : 'W';
        if (currentPlayer.get() != playerColor) return "Not your turn";
        if (!isValidMove(x, y)) return "Invalid move";

        char[][] newBoard = Arrays.stream(board.get()).map(char[]::clone).toArray(char[][]::new);
        newBoard[x][y] = playerColor;
        board.set(newBoard);

        if (checkWin(playerColor)) {
            endGame(playerColor + " wins!");
            return getBoardState() + "\n" + playerColor + " wins!";
        }

        currentPlayer.set(playerColor == 'B' ? 'W' : 'B');
        return getBoardState();
    }

    private boolean isValidMove(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE && board.get()[x][y] == '.';
    }

    private boolean checkWin(char player) {
        for (boolean[] row : visited) Arrays.fill(row, false);

        if (player == 'B') {
            for (int col = 0; col < SIZE; col++) {
                if (board.get()[0][col] == 'B' && dfs(0, col, true)) {
                    return true;
                }
            }
        } else {
            for (int row = 0; row < SIZE; row++) {
                if (board.get()[row][0] == 'W' && dfs(row, 0, false)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean dfs(int x, int y, boolean isBlack) {
        if ((isBlack && x == SIZE-1) || (!isBlack && y == SIZE-1)) return true;
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE ||
                board.get()[x][y] != currentPlayer.get() || visited[x][y]) return false;

        visited[x][y] = true;

        int[][] directions = {
                {-1,0}, {1,0}, {0,-1}, {0,1}, {-1,1}, {1,-1}
        };

        for (int[] dir : directions) {
            if (dfs(x+dir[0], y+dir[1], isBlack)) return true;
        }

        return false;
    }

    private void endGame(String message) {
        if (gameOver.compareAndSet(false, true)) {
            timer.shutdown();
            player1.notify("Game over! " + message);
            if (player2 != null) player2.notify("Game over! " + message);
        }
    }

    public String getBoardState() {
        StringBuilder sb = new StringBuilder();
        char[][] currentBoard = board.get();
        for (int i = 0; i < SIZE; i++) {
            sb.append(String.format("%2d ", i));
            for (int j = 0; j < SIZE; j++) {
                sb.append(currentBoard[i][j]).append(" ");
            }
            sb.append("\n");
        }
        sb.append("   ");
        for (int j = 0; j < SIZE; j++) sb.append(String.format("%2d", j));
        sb.append("\nTime - Black: ").append(blackTime.get()).append("s, White: ").append(whiteTime.get()).append("s");
        return sb.toString();
    }

    public String getGameResult() {
        if (blackTime.get() <= 0) return "White wins (time)";
        if (whiteTime.get() <= 0) return "Black wins (time)";
        return currentPlayer.get() == 'B' ? "White wins!" : "Black wins!";
    }

    public boolean isWaitingForPlayer() {
        return player2 == null;
    }

    public boolean isPlayerInGame(Player player) {
        return player.equals(player1) || player.equals(player2);
    }

    public boolean isGameOver() {
        return gameOver.get();
    }

    public String getGameId() {
        return gameId;
    }
}