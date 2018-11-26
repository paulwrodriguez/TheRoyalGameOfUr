package com.company.paul;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class Space {

    public static final Space NULLSPACE = new Space();
    private boolean isValidMove;
    private Cord piece;
    private ArrayList<Property> properties;
    private Player owner;

    public Space(Space cp) {
        this.isValidMove = cp.getIsValidMove();
        this.piece = new Cord(cp.getPiece());
        setProperties(cp.getProperties());
        this.owner = cp.getOwner();
    }

    public Space(Player owner) {
        this.owner = owner;
        isValidMove = true;
        piece = null;
        setProperty(Property.NONE);
    }

    public Space(Cord cord, Player _owner, boolean _isValidMove) {
        piece = cord;
        isValidMove = _isValidMove;
        setProperty(Property.NONE);
        owner = _owner;
    }

    public Space() {
        isValidMove = false;
        piece = null;
        setProperty(Property.NONE);
        owner = Player.NULLSPACE;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Space)) return false;
        Space space = (Space) o;
        return isValidMove == space.isValidMove &&
                Objects.equals(piece, space.piece) &&
                properties == space.properties &&
                owner == space.owner;
    }

    @Override
    public int hashCode() {

        return Objects.hash(isValidMove, piece, properties, owner);
    }

    public ArrayList<Property> getProperties() {
        return properties;
    }

    public void setProperties(ArrayList<Property> properties) {
        this.properties = new ArrayList<>(properties);
    }

    public void setProperty(Property property){
        properties = new ArrayList<>(Collections.singletonList(property));
    }

    public void addProperty(Property property)
    {
        properties.remove(Property.NONE);
        properties.add(property);
    }


    public void removeProperty(Property property){
        properties.remove(property);
    }

    public boolean isValidMove() {
        return isValidMove;
    }

    public void setValidMove(boolean validMove) {
        isValidMove = validMove;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
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
