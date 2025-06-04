import java.util.*;

public class Main {
    public static final int MAX_HAND_SIZE = 10;
    public static final int INITIAL_HAND_SIZE = 5;
    public static List<Integer> deck = new ArrayList<>();
    public static List<Integer> playerHand = new ArrayList<>();
    public static List<Integer> opponentHand = new ArrayList<>();
    public static Random random = new Random();
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        printRules();
        System.out.println("Press Enter when you are done reading the rules.");
        scanner.nextLine();

        initializeDeck();
        shuffleDeck();
        dealInitialHands();

        while (true) {
            System.out.println("\nYour hand: " + playerHand);
            System.out.println("Opponent has " + opponentHand.size() + " cards.");

            if (playerHand.isEmpty()) {
                System.out.println("You have no cards left. You win");
                break;
            }

            if (opponentHand.isEmpty()) {
                System.out.println("Opponent has no cards left. Opponent wins");
                break;
            }

            if (opponentHand.size() == 1) {
                System.out.println("Opponent has only 1 card and cannot play. Opponent must take a penalty");
                applyPenalty(false);
                continue;
            }

            if (playerHand.size() >= MAX_HAND_SIZE) {
                System.out.println("You have 10 cards. You lose");
                break;
            }
            if (opponentHand.size() >= MAX_HAND_SIZE) {
                System.out.println("Opponent has 10 cards. You win");
                break;
            }

            if (playerHand.size() == 1) {
                System.out.println("You have only 1 card left and cannot play. You must take a penalty");
                applyPenalty(true);
                continue;
            }

            List<Integer> playerPlay = playerChooseCards();
            List<Integer> opponentPlay = opponentChooseCards();

            if (playerPlay.size() < 2) {
                System.out.println("You don't have enough cards to play. You lose");
                break;
            }
            if (opponentPlay.size() < 2) {
                System.out.println("Opponent doesn't have enough cards to play. You win");
                break;
            }

            int playerDiff = Math.abs(playerPlay.get(0) - playerPlay.get(1));
            int opponentDiff = Math.abs(opponentPlay.get(0) - opponentPlay.get(1));

            System.out.println("You played: " + playerPlay + " (difference = " + playerDiff + ")");
            System.out.println("Opponent played: " + opponentPlay + " (difference = " + opponentDiff + ")");

            for (int card : playerPlay) {
                playerHand.remove(Integer.valueOf(card));
            }
            for (int card : opponentPlay) {
                opponentHand.remove(Integer.valueOf(card));
            }

            if (playerDiff > opponentDiff) {
                System.out.println("You win this round. Opponent faces penalty");
                applyPenalty(false);
            } else if (playerDiff < opponentDiff) {
                System.out.println("Opponent wins this round. You face penalty");
                applyPenalty(true);
            } else {
                System.out.println("Tie. No penalty");
            }
        }
    }

    public static void printRules() {
        System.out.println("Welcome to the Number Card Game");
        System.out.println("Rules:");
        System.out.println("1. Each player starts with 5 cards.");
        System.out.println("2. Each turn, both you and the opponent pick 2 cards from your hands.");
        System.out.println("3. The difference between the two cards is calculated.");
        System.out.println("4. The player with the larger difference wins the round.");
        System.out.println("5. The loser must take a penalty: draw cards or roll a dice for more cards.");
        System.out.println("6. If you have only 1 card left, you must take a penalty.");
        System.out.println("7. If your hand reaches 10 cards, you lose.");
        System.out.println("8. If the opponent’s hand reaches 10 cards, you win.");
        System.out.println("9. The game continues until someone wins or loses by these conditions.");
    }

    public static void initializeDeck() {
        deck.clear();
        for (int i = 1; i <= 10; i++) {
            for (int j = 0; j < 4; j++) {
                deck.add(i);
            }
        }
    }

    public static void shuffleDeck() {
        Collections.shuffle(deck);
    }

    public static void dealInitialHands() {
        playerHand.clear();
        opponentHand.clear();
        for (int i = 0; i < INITIAL_HAND_SIZE; i++) {
            playerHand.add(drawCard());
            opponentHand.add(drawCard());
        }
    }

    public static int drawCard() {
        if (deck.isEmpty()) return -1;
        return deck.remove(deck.size() - 1);
    }

    public static List<Integer> playerChooseCards() {
        List<Integer> chosen = new ArrayList<>();
        while (true) {
            System.out.println("Pick 2 cards to play by entering their values separated by space");
            String line = scanner.nextLine();
            String[] parts = line.trim().split("\\s+");
            if (parts.length != 2) {
                System.out.println("Please enter exactly 2 cards");
                continue;
            }
            try {
                int c1 = Integer.parseInt(parts[0]);
                int c2 = Integer.parseInt(parts[1]);
                if (playerHand.contains(c1) && playerHand.contains(c2) && !(c1 == c2 && Collections.frequency(playerHand, c1) < 2)) {
                    chosen.add(c1);
                    chosen.add(c2);
                    break;
                } else {
                    System.out.println("You don't have those cards in your hand. Try again");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter numbers only");
            }
        }
        return chosen;
    }

    public static List<Integer> opponentChooseCards() {
        List<Integer> chosen = new ArrayList<>();
        if (opponentHand.size() < 2) {
            chosen.addAll(opponentHand);
        } else {
            Collections.shuffle(opponentHand);
            chosen.add(opponentHand.get(0));
            chosen.add(opponentHand.get(1));
        }
        return chosen;
    }

    public static void applyPenalty(boolean isPlayer) {
        if (isPlayer && playerHand.size() == 1) {
            System.out.println("You have only 1 card, you must choose a penalty");
        }

        if (isPlayer) {
            System.out.println("Choose penalty:\n1 - Draw 2 cards\n2 - Roll dice for penalty");
            String choice = scanner.nextLine();
            if (choice.equals("1")) {
                drawCardsToHand(playerHand, 2);
                System.out.println("You drew 2 cards");
            } else if (choice.equals("2")) {
                dicePenalty(true);
            } else {
                System.out.println("Invalid choice, defaulting to draw 2 cards");
                drawCardsToHand(playerHand, 2);
            }
        } else {
            System.out.println("Opponent is choosing penalty");
            if (random.nextBoolean()) {
                drawCardsToHand(opponentHand, 2);
                System.out.println("Opponent drew 2 cards");
            } else {
                dicePenalty(false);
            }
        }
    }

    public static void dicePenalty(boolean isPlayer) {
        int roll = random.nextInt(6) + 1;
        System.out.println("Dice rolled: " + roll);
        if (roll == 1 || roll == 2) {
            if (isPlayer) {
                drawCardsToHand(playerHand, 1);
                System.out.println("You drew 1 card");
            } else {
                drawCardsToHand(opponentHand, 1);
                System.out.println("Opponent drew 1 card");
            }
        } else if (roll == 3 || roll == 4) {
            if (isPlayer) {
                drawCardsToHand(playerHand, 2);
                System.out.println("You drew 2 cards");
            } else {
                drawCardsToHand(opponentHand, 2);
                System.out.println("Opponent drew 2 cards");
            }
        } else {
            if (isPlayer) {
                drawCardsToHand(playerHand, 3);
                System.out.println("You drew 3 cards");
            } else {
                drawCardsToHand(opponentHand, 3);
                System.out.println("Opponent drew 3 cards");
            }
        }
    }

    public static void drawCardsToHand(List<Integer> hand, int count) {
        for (int i = 0; i < count; i++) {
            int card = drawCard();
            if (card == -1) {
                System.out.println("Deck is empty. Can't draw more cards");
                break;
            }
            hand.add(card);
        }
    }
}
