package com.company.paul;

import java.util.Collections;
import java.util.Scanner;

public class Board {
    int[][] board;
    boolean playerOne = true;
    int homeX, homeY = -1;
    final int EMPTYSPACE = 0;
    Cord HOME = new Cord(-1,-1);
    Cord END = new Cord(-2,-2);

    public Board(){
        board = new int[8][3];
        board[4][0] = -1;
        board[4][2] = -1;
        board[5][0] = -1;
        board[5][2] = -1;
    }

    public void nextPlayer(){
        playerOne = !playerOne;
    }


    public void printBoard() {
        for(int i = 0; i < board.length; ++i)
        {
            for (int j = 0; j < board[i].length; ++j)
            {
                switch (board[i][j]) {
                    case -1:
                        if(j == 2){
                            System.out.print("| ");
                        } else{
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
            if(board[i][2] == -1){
                System.out.println("");
            }else {
                System.out.println("| ");
            }

        }
    }

    Scanner scanner = new Scanner(System.in);
    public void playerMove(int diceRoll) {
        /*
            * player needs to choose a piece to move
            * Options:
            *   home
            *   board
         */
        System.out.println("You rolled a " + diceRoll + ". Move home(h) or move piece(p)");
        String choice = scanner.next();
        if(choice.equalsIgnoreCase("h")){
            // home
            updateBoard(HOME, diceRoll);
        }
        else if(choice.equalsIgnoreCase("p")){
            // move previous piece
            System.out.println("Which piece do you want to move? {x y}");
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            Cord piece = new Cord(x,y);
            updateBoard(piece, diceRoll);
        }

    }

    private class Cord{
      int x;
      int y;
      public Cord(int _x, int _y)
      {
          x = _x;
          y = _y;
      }
      public Cord() {};
      public void set(int _x, int _y) {  x = _x; y = _y; }
    }
    public void nextSpace(Cord cur){

        // Player 1 moves
        if(cur.x == -1 && cur.y == -1) {
            cur.set(3,0);
        }
        else if(cur.x == 3 && cur.y == 0){
            cur.set(2,0);
        }
        else if(cur.x == 2 && cur.y == 0) {
            cur.set(1,0);
        }
        else if(cur.x == 1 && cur.y == 0) {
            cur.set(0,0);
        }
        else if(cur.x == 0 && cur.y == 0) {
            cur.set(0,1);
        }
        // Player 2 moves

        // Common moves
        else if(cur.x == 0 && cur.y == 1) {
            cur.set(1,1);
        }
        else if(cur.x == 1 && cur.y == 1) {
            cur.set(2,1);
        }
        else if(cur.x == 2 && cur.y == 1) {
            cur.set(3,1);
        }
        else if(cur.x == 3 && cur.y == 1) {
            cur.set(4,1);
        }
        else if(cur.x == 4 && cur.y == 1) {
            cur.set(5,1);
        }
        else if(cur.x == 5 && cur.y == 1) {
            cur.set(6,1);
        }
        else if(cur.x == 6 && cur.y == 1) {
            cur.set(7,1);
        }
        // Player 1 end moves
        else if(cur.x == 7 && cur.y == 1) {
            cur.set(7,0);
        }
        else if(cur.x == 7 && cur.y == 0) {
            cur.set(6,0);
        }
        else if(cur.x == 6 && cur.y == 0) {
            cur.set(END.x,END.y);
        }
        // Player 2 send moves


    }
    public int getCurrentPlayerSymbol(boolean playerOne){
        if(playerOne){
            return 1;
        }else {
            return 2;
        }
    }
    public void updateBoard(Cord cord, int diceRoll)
    {
        if(diceRoll == 0){
            return;
        }
        if (cord == HOME) {
            // evalue solution coordinate
            Cord moveTo = new Cord(HOME.x,HOME.y);
            for(int i = 0; i < diceRoll; ++i ) {
                nextSpace(moveTo);
            }
            board[moveTo.x][moveTo.y] = getCurrentPlayerSymbol(playerOne);
        }
        else {
            // move a piece that already exist
            Cord moveTo = new Cord(cord.x, cord.y);
            for(int i = 0; i < diceRoll; ++i ) {
                nextSpace(moveTo);
            }
            board[moveTo.x][moveTo.y] = getCurrentPlayerSymbol(playerOne);
            board[cord.x][cord.y] = EMPTYSPACE;
        }
    }
}
