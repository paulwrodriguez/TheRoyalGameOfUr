package com.company.paul.controller;

import com.company.paul.service.Board;

import java.util.Scanner;

public class UrGame {

    private Board board;
    private static Scanner scanner = new Scanner(System.in);

    public UrGame(){
        board = new Board();
    }

    public void play() throws Exception {

        while( !board.isGameOver() ) {
            board.printBoard();
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

}
