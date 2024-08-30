import java.util.ArrayList;
import java.util.Random;

class Piece {
    private int position;
    private boolean isHome;
    private boolean isFinished;

    public Piece() {
        this.position = 0;
        this.isHome = true;
        this.isFinished = false;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isHome() {
        return isHome;
    }

    public void setHome(boolean isHome) {
        this.isHome = isHome;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public void move(int steps) {
        if (isHome && steps == 6) {
            isHome = false;
            position = 1;
        } else if (!isHome && !isFinished) {
            position += steps;
            if (position >= 57) { // Assuming 57 is the finish line
                isFinished = true;
                position = 57;
            }
        }
    }
}

class Player {
    private String name;
    private ArrayList<Piece> pieces;

    public Player(String name) {
        this.name = name;
        this.pieces = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            pieces.add(new Piece());
        }
    }

    public String getName() {
        return name;
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public boolean hasWon() {
        for (Piece piece : pieces) {
            if (!piece.isFinished()) {
                return false;
            }
        }
        return true;
    }
}

class Board {
    // Simplified board representation
    private Player[] players;

    public Board(Player[] players) {
        this.players = players;
    }

    public void movePiece(Player player, int pieceIndex, int steps) {
        Piece piece = player.getPieces().get(pieceIndex);
        piece.move(steps);
    }
}

class Game {
    private Player[] players;
    private Board board;
    private int currentPlayerIndex;
    private Random random;

    public Game(String[] playerNames) {
        players = new Player[playerNames.length];
        for (int i = 0; i < playerNames.length; i++) {
            players[i] = new Player(playerNames[i]);
        }
        board = new Board(players);
        currentPlayerIndex = 0;
        random = new Random();
    }

    public void playTurn() {
        Player currentPlayer = players[currentPlayerIndex];
        int diceRoll = random.nextInt(6) + 1;

        // Basic logic to move the first movable piece
        for (int i = 0; i < 4; i++) {
            Piece piece = currentPlayer.getPieces().get(i);
            if (!piece.isFinished() && (!piece.isHome() || diceRoll == 6)) {
                board.movePiece(currentPlayer, i, diceRoll);
                break;
            }
        }

        if (diceRoll != 6) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
        }

        // Check for victory
        if (currentPlayer.hasWon()) {
            System.out.println(currentPlayer.getName() + " has won the game!");
        }
    }

    public void startGame() {
        while (true) {
            playTurn();
        }
    }
}

public class LudoGame {
    public static void main(String[] args) {
        String[] playerNames = {"Player 1", "Player 2", "Player 3", "Player 4"};
        Game game = new Game(playerNames);
        game.startGame();
    }
}
