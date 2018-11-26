package com.company.paul;

import java.util.Objects;

public class Space {

    public static final Space NULLSPACE = new Space();
    private boolean isValidMove;
    private Cord piece;
    private Property property;
    private Player owner;

    public Space(Space cp) {
        this.isValidMove = cp.getIsValidMove();
        this.piece = new Cord(cp.getPiece());
        this.property = cp.getProperty();
        this.owner = cp.getOwner();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Space)) return false;
        Space space = (Space) o;
        return isValidMove == space.isValidMove &&
                Objects.equals(piece, space.piece) &&
                property == space.property &&
                owner == space.owner;
    }

    @Override
    public int hashCode() {

        return Objects.hash(isValidMove, piece, property, owner);
    }

    public Space(Player owner) {
        this.owner = owner;
        isValidMove = true;
        piece = null;
        property = Property.NONE;
    }

    public Space(Cord cord, Player _owner, boolean _isValidMove) {
        piece = cord;
        isValidMove = _isValidMove;
        property = Property.NONE;
        owner = _owner;
    }

    public Space() {
        isValidMove = false;
        piece = null;
        property = Property.NONE;
        owner = Player.NULLSPACE;
    }

    public boolean isValidMove() {
        return isValidMove;
    }

    public void setValidMove(boolean validMove) {
        isValidMove = validMove;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Property getProperty(){
        return  property;
    }

    public enum Property {
        ROLLAGAIN,
        NONE
    }

    public void setIsValidMove(boolean b) {
        isValidMove = b;
    }



    public void setPiece(Cord cord) {
        piece = cord;
    }

    public Cord getPiece() {
        return piece;
    }


    public boolean getIsValidMove() {
        return isValidMove;
    }
}
