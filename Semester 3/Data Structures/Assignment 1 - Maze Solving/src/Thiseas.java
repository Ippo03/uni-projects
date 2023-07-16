import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Thiseas {
    private static class Position {
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }

    private char[][] maze;
    private int rows;
    private int cols;

    public Thiseas(String filePath) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            rows = scanner.nextInt();
            cols = scanner.nextInt();
            maze = new char[rows][cols];

            for (int i = 0; i < rows; i++) {
                String row = scanner.next();
                maze[i] = row.toCharArray();
            }
        }
    }

    public boolean solve() {
        Position startPosition = findStartPosition();
        return solveMaze(startPosition.x, startPosition.y);
    }

    private Position findStartPosition() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze[i][j] == 'E') {
                    return new Position(i, j);
                }
            }
        }
        return null;
    }

    private boolean solveMaze(int x, int y) {
        if (x < 0 || y < 0 || x >= rows || y >= cols) {
            return false; // Out of bounds
        }

        if (maze[x][y] == '1') {
            return false; // Wall encountered
        }

        if (maze[x][y] == '0') {
            if (x == 0 || y == 0 || x == rows - 1 || y == cols - 1) {
                return true; // Exit found
            }

            maze[x][y] = '1'; // Mark current position as visited

            // Recursively explore neighboring positions
            if (solveMaze(x - 1, y) || solveMaze(x, y + 1) || solveMaze(x + 1, y) || solveMaze(x, y - 1)) {
                return true; // Exit found in one of the neighboring positions
            }

            maze[x][y] = '0'; // Mark current position as unvisited (backtracking)
        }

        return false; // No path found
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide the path to the input file.");
            return;
        }

        try {
            Thiseas mazeSolver = new Thiseas(args[0]);
            if (mazeSolver.solve()) {
                System.out.println("Path Found!!!");
            } else {
                System.out.println("Path not Found");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Input file not found.");
        }
    }
}
