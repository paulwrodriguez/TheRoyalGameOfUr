package com.company.paul.controller;

import com.company.paul.service.Board;

import java.util.Scanner;

public class UrGame {

    Board board;
    static Scanner scanner = new Scanner(System.in);

    public UrGame(){
        board = new Board();
    }

    public String prompt()
    {
        System.out.println("Continue? Y/N" );
        return scanner.next();
    }

    public void play() throws Exception {

        while( !board.isGameOver() ) {
            printBoard();
            int diceRoll = board.rollDice();
            board.printPlayerPoints();
            board.playerMove(diceRoll);
            board.nextPlayer();

        }
        printWinner();
    }

    private void printWinner(){
        System.out.println("The winner is " + board.getWinner() + "!!!!!");
    }

    public void printBoard(){
        board.printBoard();
        System.out.println("");
    }
}
