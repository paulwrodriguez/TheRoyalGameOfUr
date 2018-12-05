package com.company.paul.service;


import com.company.paul.config.Config;
import com.company.paul.service.auxilary.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Board {

    Scanner scanner = new Scanner(System.in);

    Space[][] board;
    boolean playerOne = true;

    Space HOME;
    Space END;
    final Space ERROR;
    final Space HOMESPACEPLAYERONE;
    final Space HOMESPACEPLAYERTWO;
    final Space LASTSPACEPLAYERONE;
    final Space LASTSPACEPLAYERTWO;
    final Space ENDSPACEPLAYERONE;
    final Space ENDSPACEPLAYERTWO;

    private int playerOnePoints = 0;
    private int playerTwoPoints = 0;
    private Space lastSpace = null;
    private String output;

    private static final int WINPOINTS = 2;

    public Board() {
        output = "";
        ERROR = new Space(new Cord(-3, -3), Player.NOPLAYER, false);
        LASTSPACEPLAYERONE = new Space(new Cord(6, 0), Player.NOPLAYER, true);
        LASTSPACEPLAYERTWO = new Space(new Cord(6, 2), Player.NOPLAYER, true);

        HOMESPACEPLAYERONE = new Space(new Cord(4, 0), Player.NULLSPACE, true);
        HOMESPACEPLAYERTWO = new Space(new Cord(5, 0), Player.NULLSPACE, true);

        ENDSPACEPLAYERONE = new Space(new Cord(4, 2), Player.NULLSPACE, false);
        ENDSPACEPLAYERTWO = new Space(new Cord(5, 2), Player.NULLSPACE, false);

        board = new Space[8][3];
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[i].length; ++j) {
                board[i][j] = new Space(new Cord(i, j), Player.NOPLAYER, true);
            }
        }
        board[4][0] = HOMESPACEPLAYERONE;
        board[4][2] = ENDSPACEPLAYERONE;
        board[5][0] = HOMESPACEPLAYERTWO;
        board[5][2] = ENDSPACEPLAYERTWO;

        HOME = board[4][0];
        END = board[5][0];

        // Special Squares
        board[0][0].setProperty(Property.ROLLAGAIN);
        board[6][0].setProperty(Property.ROLLAGAIN);
        board[0][2].setProperty(Property.ROLLAGAIN);
        board[6][2].setProperty(Property.ROLLAGAIN);
        board[3][1].setProperty(Property.ROLLAGAIN);
        board[3][1].addProperty(Property.IMMUNITY);

    }

    public void nextPlayer() {
        playerOne = !playerOne;
        if (playerOne) {
            HOME = HOMESPACEPLAYERONE;
            END = ENDSPACEPLAYERONE;
        } else {
            HOME = HOMESPACEPLAYERTWO;
            END = ENDSPACEPLAYERTWO;
        }
    }

    private List<String> solution = Arrays.asList(
            "|0,0 |0,1 |0,2 |",
            "|1,0 |1,1 |1,2 |",
            "|2,0 |2,1 |2,2 |",
            "|3,0 |3,1 |3,2 |",
            "       |4,1 |     ",
            "       |5,1 |     ",
            "|6,0 |6,1 |6,2 |",
            "|7,0 |7,1 |7,2 |");

    private void addToStream(String m) {
        if (Config.CONSOLE_OUTPUT) {
            System.out.print(m);
        } else {
            output += m;
            System.out.print(m);
        }

    }

    private void addToStreamln(String m) {
        if (Config.CONSOLE_OUTPUT) {
            System.out.println(m);
        } else {
            output = output + m + "\n";
            System.out.println(m);
        }

    }

    public void printBoard() {

        addToStreamln("-------------------------------------------");
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[i].length; ++j) {
                switch (board[i][j].getOwner()) {
                    case NULLSPACE:
                        if (j == 2) {
                            addToStream("| ");
                        } else {
                            addToStream("  ");
                        }
                        break;
                    case NOPLAYER:
                        addToStream("| ");
                        break;
                    case PLAYERONE:
                        addToStream("|X");
                        break;
                    case PLAYERTWO:
                        addToStream("|O");
                }
            }
            if (board[i][2].getOwner() == Player.NULLSPACE) {
                addToStream("");
            } else {
                addToStream("| ");
            }

            addToStreamln("       " + solution.get(i));

        }
        addToStreamln("-------------------------------------------");
    }

    public Space isValidMoveFromHome(int diceRoll) {
        Space move = new Space(HOME);
        Space futureMoveLocation = fetchNextSpace(move, diceRoll);
        if (!futureMoveLocation.equals(HOME) && futureMoveLocation.getOwner().equals(getCurrentPlayer(playerOne))) {
            addToStream("Player " + getCurrentPlayer(playerOne) + " already has a piece on " + futureMoveLocation.getPiece().x + " " + futureMoveLocation.getPiece().y + ".");
            addToStreamln("Please choose another move.");
            move = Space.NULLSPACE; // TODO find a more obvious way to do this
        } else {
            move = HOME; // TODO might not be needed since frech doesnt change the passed value.
        }

        return move;

    }

    /*
     * player needs to choose a piece to move
     * Options:
     *   home
     *   board
     */
    public void playerMove(int diceRoll) throws Exception {

        Space move = Space.NULLSPACE;

        while (!move.getIsValidMove()) {

            String choice = promptPlayerForMove(diceRoll);
            move = validateMove(diceRoll, move, choice);

        }

        updateBoard(move, diceRoll);
        runLastMoveAction(getLastMove());

    }

    private String promptPlayerForMove(int diceRoll) {
        addToStreamln("Player (" + getCurrentPlayer().ordinal() + ") rolled a (" + diceRoll + "). Move home(h) or move piece(p)");
        String choice;

        if (Config.CONSOLE_OUTPUT) {
            choice = scanner.next();
        } else {

            List<String> options = Arrays.asList("Home(h)", "Piece (p)");

            UIManager.put("OptionPane.messageFont", new Font("Monospaced", Font.PLAIN, 30));
            JFrame frame = new JFrame();
            int n = JOptionPane.showOptionDialog(frame,
                    output,
                    "UR Game",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options.toArray(),
                    options.toArray()[0]);

            System.out.println(output);
            output = "";

            if (n == 0) {
                choice = "h";
            } else if (n == 1) {
                choice = "p";
            } else {
                choice = "";
            }
        }

        return choice;
    }

    private Space validateMove(int diceRoll, Space move, String choice) {

        if (choice.equalsIgnoreCase("h")) {
            // home
            move = isValidMoveFromHome(diceRoll);

        } else if (choice.equalsIgnoreCase("p")) {
            // move previous piece
            move = isValidMoveFromSpace(diceRoll);

        }
        return move;
    }

    public int rollDice() {
        Random random = new Random();
        int d1 = random.nextInt(2);
        int d2 = random.nextInt(2);
        int d3 = random.nextInt(2);
        int d4 = random.nextInt(2);

        int result = d1 + d2 + d3 + d4;
        return result;
    }

    private void runLastMoveAction(Space lastSpace) throws Exception {
        for (Property property : lastSpace.getProperties()) {
            if (Property.ROLLAGAIN.equals(property)) {
                int diceRoll = rollDice();
                printBoard();
                playerMove(diceRoll);
            }
        }

    }

    private Space getLastMove() {
        return lastSpace;
    }


    private int[] getIntFromConsole() {
        int[] result = new int[2];

        if(Config.CONSOLE_OUTPUT){
            result[0] = scanner.nextInt();
            result[1] = scanner.nextInt();
        }
        else {

            UIManager.put("OptionPane.messageFont", new Font("Monospaced", Font.PLAIN, 30));
            String n = JOptionPane.showInputDialog(output);
            System.out.println(output);

            String[] stringArray = n.trim().split("\\s+");

            result[0] = Integer.parseInt(stringArray[0]);
            result[1] = Integer.parseInt(stringArray[1]);

            output = "";
        }

        return result;
    }

    private Space isValidMoveFromSpace(int diceRoll) {
        Space moveTo = ERROR;

        addToStreamln("Which piece do you want to move? {x y}");
        addToStreamln("You have peices on " + getListOfActivePieces());

        int[] result = getIntFromConsole();
        int x = result[0];
        int y = result[1];
        Space startingSpace = getSpaceFromBoard(new Cord(x, y));

        Space futureMove = fetchNextSpace(startingSpace, diceRoll);
        if (!validMove(futureMove)) {
            addToStream("Player (" + getCurrentPlayer() + ") is not allowed to move from " + startingSpace.getPiece() + " to " + futureMove.getPiece());
            addToStreamln("Please choose another move.");

        } else {
            if (spaceContainsOtherPlayerPiece(futureMove)) { // TODO this should be removed.
                addToStreamln("You ate piece " + futureMove.getPiece());
            }
            moveTo = startingSpace;
        }

        return moveTo;
    }

    private boolean spaceContainsOtherPlayerPiece(Space space) {
        if (space.equals(HOME) || space.equals(ERROR) || space.equals(LASTSPACEPLAYERONE) || space.equals(LASTSPACEPLAYERTWO)) {
            return false;
        } else if (space.getOwner().equals(getCurrentPlayer(!playerOne))) {
            return true;
        } else {
            return false;
        }
    }

    private boolean validMove(Space futureMove) {

        if (futureMove.equals(END)) {
            return true;
        } else if (futureMove.equals(ERROR)) {
            return false;
        } else if (futureMove.getOwner().equals(getCurrentPlayer(playerOne))) {
            return false;
        } else if (futureMove.getOwner().equals(getCurrentPlayer(!playerOne)) && futureMove.getProperties().contains(Property.IMMUNITY)) {
            return false;
        } else {
            return true;
        }

    }

    private String getListOfActivePieces() {
        String result = "";

        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[i].length; ++j) {
                if (board[i][j].getOwner() == getCurrentPlayer(playerOne)) {
                    result += "(" + board[i][j].getPiece().x + " " + board[i][j].getPiece().y + ")";
                }
            }
        }

        return result;
    }

    public boolean isGameOver() {
        if (playerOnePoints >= WINPOINTS || playerTwoPoints >= WINPOINTS)
            return true;
        else
            return false;
    }

    public String getWinner() {
        if (playerOnePoints >= WINPOINTS) {
            return "1";
        } else {
            return "2";
        }
    }

    public Space nextSpace(Space current) throws Exception {
        Cord cur = new Cord(current.getPiece());
        // Starting moves
        if (playerOne) {
            // Player 1 moves
            if (cur.x == HOME.getPiece().x && cur.y == HOME.getPiece().y) {
                cur.set(3, 0);
            } else if (cur.x == 3 && cur.y == 0) {
                cur.set(2, 0);
            } else if (cur.x == 2 && cur.y == 0) {
                cur.set(1, 0);
            } else if (cur.x == 1 && cur.y == 0) {
                cur.set(0, 0);
            } else if (cur.x == 0 && cur.y == 0) {
                cur.set(0, 1);
            }
            // Common moves
            else if (cur.x == 0 && cur.y == 1) {
                cur.set(1, 1);
            } else if (cur.x == 1 && cur.y == 1) {
                cur.set(2, 1);
            } else if (cur.x == 2 && cur.y == 1) {
                cur.set(3, 1);
            } else if (cur.x == 3 && cur.y == 1) {
                cur.set(4, 1);
            } else if (cur.x == 4 && cur.y == 1) {
                cur.set(5, 1);
            } else if (cur.x == 5 && cur.y == 1) {
                cur.set(6, 1);
            } else if (cur.x == 6 && cur.y == 1) {
                cur.set(7, 1);
            }
            // Player 1 end moves
            else if (cur.x == 7 && cur.y == 1) {
                cur.set(7, 0);
            } else if (cur.x == 7 && cur.y == 0) {
                cur.set(6, 0);
            } else if (cur.x == 6 && cur.y == 0) {
                cur.set(END.getPiece().x, END.getPiece().y);
            } else {
                throw new Exception("No Mapping found for coordinate " + cur);
            }
        } else if (!playerOne) {
            // Player 2 moves
            if (cur.x == HOME.getPiece().x && cur.y == HOME.getPiece().y) {
                cur.set(3, 2);
            } else if (cur.x == 3 && cur.y == 2) {
                cur.set(2, 2);
            } else if (cur.x == 2 && cur.y == 2) {
                cur.set(1, 2);
            } else if (cur.x == 1 && cur.y == 2) {
                cur.set(0, 2);
            } else if (cur.x == 0 && cur.y == 2) {
                cur.set(0, 1);
            }
            // Common moves
            else if (cur.x == 0 && cur.y == 1) {
                cur.set(1, 1);
            } else if (cur.x == 1 && cur.y == 1) {
                cur.set(2, 1);
            } else if (cur.x == 2 && cur.y == 1) {
                cur.set(3, 1);
            } else if (cur.x == 3 && cur.y == 1) {
                cur.set(4, 1);
            } else if (cur.x == 4 && cur.y == 1) {
                cur.set(5, 1);
            } else if (cur.x == 5 && cur.y == 1) {
                cur.set(6, 1);
            } else if (cur.x == 6 && cur.y == 1) {
                cur.set(7, 1);
            }
            // player 2 ending moves
            else if (cur.x == 7 && cur.y == 1) {
                cur.set(7, 2);
            } else if (cur.x == 7 && cur.y == 2) {
                cur.set(6, 2);
            } else if (cur.x == 6 && cur.y == 2) {
                cur.set(END.getPiece().x, END.getPiece().y);
            } else {
                throw new Exception("You are not allowed to move past home. cur=" + cur.toString());
            }
        } else {
            throw new Exception("nextSpace else case encountered: playerOne = " + playerOne);
        }
        return getSpaceFromBoard(cur); // conversion to actual board data structure
    }

    public Player getCurrentPlayer() {
        return getCurrentPlayer(playerOne);
    }

    public Player getCurrentPlayer(boolean playerOne) {
        if (playerOne) {
            return Player.PLAYERONE;
        } else {
            return Player.PLAYERTWO;
        }
    }

    public Space fetchNextSpace(Space fromSpace, int diceRoll) {
        Space moveTo = fromSpace;
        try {
            for (int i = 0; i < diceRoll; ++i) {
                moveTo = nextSpace(moveTo);
            }
        } catch (Exception e) {
            moveTo = ERROR;
            addToStreamln(e.getMessage());
        }

        return moveTo;
    }

    private void setLastMove(Space space) {
        lastSpace = space;
    }

    public Space getSpaceFromBoard(Cord cord) {
        Space space = null;

        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[i].length; ++j) {
                if (board[i][j].getPiece().equals(cord)) {
                    space = board[i][j];

                }
            }
        }


        return space;
    }

    private void updateBoard(Space startingSpace, int diceRoll) {
        if (diceRoll == 0) {
            setLastMove(Space.NULLSPACE);
            return;
        }

        Space newSpace = new Space(startingSpace);
        newSpace = fetchNextSpace(newSpace, diceRoll);
        removePlayerFromSpace(startingSpace);
        setLastMove(newSpace);
        promptHome(newSpace.equals(END));
        setOwner(newSpace, getCurrentPlayer());

    }

    private void setOwner(Space space, Player player) {
        if (space.equals(HOME) || space.equals(END)) {
            return;
        }

        space.setOwner(player);
    }

    public void removePlayerFromSpace(Space space) {
        if (!(space.equals(HOME) || space.equals(END))) {
            space.setOwner(Player.NOPLAYER);
        }
    }

    public void printPlayerPoints() {
        addToStreamln("Player 1: " + playerOnePoints);
        addToStreamln("Player 2: " + playerTwoPoints);
    }

    private void promptHome(boolean act) {
        if (act) {
            if (playerOne) {
                ++playerOnePoints;
            } else {
                ++playerTwoPoints;
            }
            addToStreamln(getCurrentPlayer(playerOne) + " made a piece home !");
        }
    }

    private boolean isHome(Space moveTo) {
        return moveTo.equals(END);
    }
}
