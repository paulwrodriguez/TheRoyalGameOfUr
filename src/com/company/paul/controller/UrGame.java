package com.company.paul.controller;

import com.company.paul.service.Board;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class UrGame {

    private Board board;
    private static Scanner scanner = new Scanner(System.in);

    public UrGame(){
        board = new Board();
    }

    public void play() throws Exception {

//        while( !board.isGameOver() ) {
//            board.printBoard();
//            int diceRoll = board.rollDice();
//            board.printPlayerPoints();
//            board.playerMove(diceRoll);
//            board.nextPlayer();
//
//        }
        printWinner();
    }

    private void printWinner(){
        UIManager.put("OptionPane.messageFont", new Font("Monospaced", Font.PLAIN, 30));
        JOptionPane.showMessageDialog(null,"The winner is Player " + board.getWinner() + "!!!!!\"");
//        System.out.println("The winner is " + board.getWinner() + "!!!!!");
    }

}
