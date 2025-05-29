import java.util.*;

public class Main {
    static final int ROWS = 5;
    static final int COLS = 10;
    static final String EMPTY = "🟦";
    static final String TOWER = "🛡️";
    static final String ENEMY = "👾";
    static final String BASE = "🏰";

    static String[][] grid = new String[ROWS][COLS];
    static List<int[]> enemies = new ArrayList<>();
    static int health = 5;
    static int gold = 10;
    static int towerCost = 3;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        for (int r = 0; r < ROWS; r++) {
            Arrays.fill(grid[r], EMPTY);
            grid[r][COLS - 1] = BASE;
        }

        for (int wave = 1; wave <= 15; wave++) {
            System.out.println("\nWave " + wave);
            spawnEnemy();
            printGrid();
            System.out.print("Place a tower? (y/n): ");
            String choice = scanner.nextLine().toLowerCase();
            if (choice.equals("y")) {
                placeTower();
            }
            moveEnemies();
            if (health <= 0) {
                printGrid();
                System.out.println("💀 Your base was destroyed. Game over!");
                return;
            }
            pause(1000);
        }

        printGrid();
        System.out.println("🎉 You survived all waves. You win!");
    }

    public static void printGrid() {
        System.out.println("\nGold: " + gold + "   Health: " + health);
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                System.out.print(grid[r][c]);
            }
            System.out.println();
        }
    }

    public static void spawnEnemy() {
        int row = new Random().nextInt(ROWS);
        if (grid[row][0].equals(EMPTY)) {
            grid[row][0] = ENEMY;
            enemies.add(new int[] { row, 0 });
        }
    }

    public static void placeTower() {
        System.out.print("Enter row (0–4): ");
        int r = scanner.nextInt();
        System.out.print("Enter column (0–8): ");
        int c = scanner.nextInt();
        scanner.nextLine();
        if (r >= 0 && r < ROWS && c >= 0 && c < COLS - 1) {
            if (grid[r][c].equals(EMPTY) && gold >= towerCost) {
                grid[r][c] = TOWER;
                gold -= towerCost;
            } else {
                System.out.println("Invalid spot or not enough gold.");
            }
        }
    }

    public static void moveEnemies() {
        List<int[]> newEnemies = new ArrayList<>();
        for (int[] enemy : enemies) {
            int r = enemy[0], c = enemy[1];
            if (c + 1 >= COLS - 1) {
                grid[r][c] = EMPTY;
                health--;
                continue;
            }
            if (grid[r][c + 1].equals(TOWER)) {
                grid[r][c + 1] = EMPTY;
                grid[r][c] = EMPTY;
            } else {
                grid[r][c] = EMPTY;
                c++;
                grid[r][c] = ENEMY;
                newEnemies.add(new int[] { r, c });
            }
        }
        enemies = newEnemies;
    }

    public static void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
        }
    }
}
