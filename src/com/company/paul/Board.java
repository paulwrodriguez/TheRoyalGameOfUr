package com.company.paul;


import java.util.*;

public class Board {

    Scanner scanner = new Scanner(System.in);

    Space[][] board;
    boolean playerOne = true;

    Space HOME;
    Space END ;
    final Space ERROR ;
    final Space HOMESPACEPLAYERONE ;
    final Space HOMESPACEPLAYERTWO ;
    final Space LASTSPACEPLAYERONE ;
    final Space LASTSPACEPLAYERTWO ;
    final Space ENDSPACEPLAYERONE ;
    final Space ENDSPACEPLAYERTWO ;

    private int playerOnePoints = 0;
    private int playerTwoPoints = 0;
    Space lastSpace = null;

    private static final int WINPOINTS = 2;

    public Board() {
        ERROR = new Space(new Cord(-3, -3), Player.NOPLAYER, false);
        LASTSPACEPLAYERONE = new Space( new Cord(6, 0), Player.NOPLAYER , true);
        LASTSPACEPLAYERTWO = new Space( new Cord(6, 2), Player.NOPLAYER , true);

        HOMESPACEPLAYERONE = new Space(new Cord(4,0), Player.NULLSPACE, true);
        HOMESPACEPLAYERTWO = new Space(new Cord (5, 0), Player.NULLSPACE, true);

        ENDSPACEPLAYERONE = new Space(new Cord(4,2), Player.NULLSPACE, false);
        ENDSPACEPLAYERTWO = new Space(new Cord(5,2), Player.NULLSPACE, false);

        board = new Space[8][3];
        for(int i = 0; i < board.length; ++i)
        {
            for(int j =0; j< board[i].length; ++j)
            {
                board[i][j] = new Space( new Cord(i, j), Player.NOPLAYER, true);
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
        if(playerOne)
        {
            HOME = HOMESPACEPLAYERONE;
            END  = ENDSPACEPLAYERONE;
        }
        else {
            HOME = HOMESPACEPLAYERTWO;
            END  = ENDSPACEPLAYERTWO;
        }
    }

    List<String> solution = Arrays.asList(
            "|0,0 |0,1 |0,2 |",
            "|1,0 |1,1 |1,2 |",
            "|2,0 |2,1 |2,2 |",
            "|3,0 |3,1 |3,2 |",
            "       |4,1 |     ",
            "       |5,1 |     ",
            "|6,0 |6,1 |6,2 |",
            "|7,0 |7,1 |7,2 |");

    public void printBoard() {
        System.out.println("-------------------------------------------");
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[i].length; ++j) {
                switch (board[i][j].getOwner()) {
                    case NULLSPACE:
                        if (j == 2) {
                            System.out.print("| ");
                        } else {
                            System.out.print("  ");
                        }
                        break;
                    case NOPLAYER:
                        System.out.print("| ");
                        break;
                    case PLAYERONE:
                        System.out.print("|X");
                        break;
                    case PLAYERTWO:
                        System.out.print("|O");
                }
            }
            if (board[i][2].getOwner() == Player.NULLSPACE) {
                System.out.print("");
            } else {
                System.out.print("| ");
            }

            System.out.println("       " + solution.get(i));

        }
    }

    public Space isValidMoveFromHome(int diceRoll){
        Space move = new Space(HOME);
        Space futureMoveLocation = fetchNextSpace(move, diceRoll);
        if (!futureMoveLocation.equals(HOME) && futureMoveLocation.getOwner().equals( getCurrentPlayer(playerOne)) ) {
            System.out.print("Player " + getCurrentPlayer(playerOne) + " already has a piece on " + futureMoveLocation.getPiece().x + " " + futureMoveLocation.getPiece().y + ".");
            System.out.println("Please choose another move.");
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

    private String promptPlayerForMove(int diceRoll){
        System.out.println("Player (" + getCurrentPlayer().ordinal() + ") rolled a (" + diceRoll + "). Move home(h) or move piece(p)");
        String choice = scanner.next();

        return choice;
    }

    private Space validateMove(int diceRoll, Space move, String choice)
    {

        if (choice.equalsIgnoreCase("h")) {
            // home
            move = isValidMoveFromHome(diceRoll);

        } else if (choice.equalsIgnoreCase("p")) {
            // move previous piece
            move = isValidMoveFromSpace(diceRoll);

        }
        return move;
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

    private void runLastMoveAction(Space lastSpace) throws Exception {
        for(Property property : lastSpace.getProperties())
        {
            if (Property.ROLLAGAIN.equals(property)){
                int diceRoll = rollDice();
                printBoard();
                playerMove(diceRoll);
            }
        }

    }

    private Space getLastMove(){
        return lastSpace;
    }


    private Space isValidMoveFromSpace(int diceRoll) {
        Space moveTo = ERROR;

        System.out.println("Which piece do you want to move? {x y}");
        System.out.println("You have peices on " + getListOfActivePieces());
        int x = scanner.nextInt();
        int y = scanner.nextInt();
        Space startingSpace = getSpaceFromBoard(new Cord(x,y));

        Space futureMove = fetchNextSpace(startingSpace, diceRoll);
        if (!validMove(futureMove)) {
            System.out.print("Player (" + getCurrentPlayer() + ") is not allowed to move from " + startingSpace.getPiece() + " to " + futureMove.getPiece());
            System.out.println("Please choose another move.");

        } else {
            if(spaceContainsOtherPlayerPiece(futureMove)){ // TODO this should be removed.
                System.out.println("You ate piece " + futureMove.getPiece());
            }
            moveTo = startingSpace;
        }

        return moveTo;
    }

    private boolean spaceContainsOtherPlayerPiece(Space space){
        if(space.equals(HOME) || space.equals(ERROR) || space.equals(LASTSPACEPLAYERONE) || space.equals(LASTSPACEPLAYERTWO)){
            return false;
        }
        else if( space.getOwner().equals( getCurrentPlayer(!playerOne)) ){
            return true;
        }
        else {
            return false;
        }
    }

    private boolean validMove(Space futureMove) {

        if( futureMove.equals(END)){
            return true;
        }
        else if (futureMove.equals(ERROR)) {
            return false;
        }
        else if (futureMove.getOwner().equals(getCurrentPlayer(playerOne))) {
            return false;
        }
        else if(futureMove.getOwner().equals(getCurrentPlayer(!playerOne)) && futureMove.getProperties().contains(Property.IMMUNITY) ){
            return false;
        }
        else {
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

    public String getWinner(){
        if(playerOnePoints >= WINPOINTS){
            return "1";
        }else {
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
        return getSpaceFromBoard(cur);
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
            System.out.println(e.getMessage());
        }

        return moveTo;
    }

    private void setLastMove(Space space)
    {
        lastSpace = space;
    }

    public Space getSpaceFromBoard(Cord cord){
        Space space = null;

        for(int i = 0; i < board.length; ++i){
            for( int j = 0; j < board[i].length; ++j)
            {
                if(board[i][j].getPiece().equals(cord))
                {
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

    private void setOwner(Space space, Player player)
    {
        if(space.equals(HOME) || space.equals(END)) { return; }

        space.setOwner(player);
    }
    public void removePlayerFromSpace(Space space)
    {
        if(!space.equals(HOME) || !space.equals(END))
        {
            space.setOwner(Player.NOPLAYER);
        }
    }

    public void printPlayerPoints() {
        System.out.println("Player 1: " + playerOnePoints);
        System.out.println("Player 2: " + playerTwoPoints);
    }

    private void promptHome(boolean act) {
        if(act){
            if (playerOne) {
                ++playerOnePoints;
            } else {
                ++playerTwoPoints;
            }
            System.out.println(getCurrentPlayer(playerOne) + " made a piece home !");
        }
    }

    private boolean isHome(Space moveTo) {
        return moveTo.equals(END);
    }
}
