package com.company.paul;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.lang.management.PlatformLoggingMXBean;
import java.util.Random;
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

    public void play() {


        String cmd = prompt();
        while(cmd.equalsIgnoreCase("Y")){
            printBoard();
            int diceRoll = rollDice();
            board.playerMove(diceRoll);
            cmd = prompt();
        }

    }

    public int rollDice(){
        Random random = new Random();
        int d1 = random.nextInt(2);
        int d2 = random.nextInt(2);
        int d3 = random.nextInt(2);
        int d4 = random.nextInt(2);

        int result = d1 + d2 + d3 + d4;
        return result;
    }

    public void printBoard(){
        board.printBoard();
        System.out.println("");
    }
}
