package com.company.paul;

class Cord {
    int x;
    int y;

    public Cord(int _x, int _y) {
        x = _x;
        y = _y;
    }

    public Cord() {
    }

    public Cord(Cord cord) {
        this.x = cord.x;
        this.y = cord.y;
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
