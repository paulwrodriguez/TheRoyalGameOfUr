package com.company.paul;

import java.util.Collections;
import java.util.Scanner;

public class Board {
    int[][] board;
    boolean playerOne = true;
    char playerOneSymbol = 'X';
    int homeX, homeY = -1;
    final int EMPTYSPACE = 0;
    Cord HOME = new Cord(-1, -1);
    Cord END = new Cord(-2, -2);
    private int playerOnePoints = 0;
    private int playerTwoPoints = 0;

    public Board() {
        board = new int[8][3];
        board[4][0] = -1;
        board[4][2] = -1;
        board[5][0] = -1;
        board[5][2] = -1;
    }

    public void nextPlayer() {
        playerOne = !playerOne;
    }


    public void printBoard() {
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[i].length; ++j) {
                switch (board[i][j]) {
                    case -1:
                        if (j == 2) {
                            System.out.print("| ");
                        } else {
                            System.out.print("  ");
                        }
                        break;
                    case EMPTYSPACE:
                        System.out.print("| ");
                        break;
                    case 1:
                        System.out.print("|X");
                        break;
                    case 2:
                        System.out.print("|O");
                }
            }
            if (board[i][2] == -1) {
                System.out.println("");
            } else {
                System.out.println("| ");
            }

        }
    }

    Scanner scanner = new Scanner(System.in);

    public void playerMove(int diceRoll) throws Exception {
        /*
         * player needs to choose a piece to move
         * Options:
         *   home
         *   board
         */
        System.out.println("You rolled a " + diceRoll + ". Move home(h) or move piece(p)");
        String choice = scanner.next();
        if (choice.equalsIgnoreCase("h")) {
            // home
            updateBoard(HOME, diceRoll);
        } else if (choice.equalsIgnoreCase("p")) {
            // move previous piece
            System.out.println("Which piece do you want to move? {x y}");
            System.out.println("You have peices on " + getListOfActivePieces());
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            Cord piece = new Cord(x, y);
            updateBoard(piece, diceRoll);
        }

    }

    private String getListOfActivePieces() {
        String result = "";

        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[i].length; ++j) {
                if (board[i][j] == getCurrentPlayerSymbol(playerOne)) {
                    result += "(" + i + " " + j + ")";
                }
            }
        }

        return result;
    }

    public boolean isGameOver() {
        if(playerOnePoints >= 7 || playerTwoPoints >= 7)
            return true;
        else
            return false;
    }

    private class Cord {
        int x;
        int y;

        public Cord(int _x, int _y) {
            x = _x;
            y = _y;
        }

        public Cord() {
        }

        ;

        public void set(int _x, int _y) {
            x = _x;
            y = _y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Cord)) return false;
            Cord cord = (Cord) o;
            return x == cord.x &&
                    y == cord.y;
        }

        @Override
        public String toString() {
            return "Cord{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    public void nextSpace(Cord cur) throws Exception {
        // Starting moves
        if (playerOne) {
            // Player 1 moves
            if (cur.x == -1 && cur.y == -1) {
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
                cur.set(END.x, END.y);
            } else {
                throw new Exception("You are not allowed to move past home");
            }
        } else if (!playerOne) {
            // Player 2 moves
            if (cur.x == -1 && cur.y == -1) {
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
                cur.set(END.x, END.y);
            }
            else {
                throw new Exception("You are not allowed to move past home. cur=" + cur.toString());
            }
        }
        else {
            throw new Exception("nextSpace else case encountered: playerOne = " + playerOne );
        }
    }

    public int getCurrentPlayerSymbol(boolean playerOne) {
        if (playerOne) {
            return 1;
        } else {
            return 2;
        }
    }

    public void updateBoard(Cord cord, int diceRoll) throws Exception {
        if (diceRoll == 0) {
            return;
        }
        if (cord.equals(HOME)) {
            // evaluate solution coordinate
            Cord moveTo = new Cord(HOME.x, HOME.y);
            for (int i = 0; i < diceRoll; ++i) {
                nextSpace(moveTo);
            }
            board[moveTo.x][moveTo.y] = getCurrentPlayerSymbol(playerOne);
        } else {
            // move a piece that already exist
            Cord moveTo = new Cord(cord.x, cord.y);
            for (int i = 0; i < diceRoll; ++i) {
                nextSpace(moveTo);
            }
            if (isHome(moveTo)) {
                promptHome();
            }
            board[moveTo.x][moveTo.y] = getCurrentPlayerSymbol(playerOne);
            board[cord.x][cord.y] = EMPTYSPACE;
        }
    }

    public void printPlayerPoints(){
        System.out.println("Player 1: " + playerOnePoints);
        System.out.println("Player 2: " + playerTwoPoints);
    }
    private void promptHome() {
        if(playerOne){
            ++playerOnePoints;
        }
        else {
            ++playerTwoPoints;
        }
        System.out.println( getCurrentPlayerSymbol(playerOne) + " made a piece home !");
    }

    private boolean isHome(Cord moveTo) {
        return moveTo.equals(END);
    }
}
