package org.example;

public class Ruta {
    private Location start;
    private Location end;
    private double probabilitate;
    private int friendlyct;
    private int neutralct;
    private int enemyct;
    public Ruta(Location start, Location end) {
        this.start = start;
        this.end = end;
    }
    public void setProbabilitate(double probabilitate) {
        this.probabilitate = probabilitate;
    }
    public void setct(int friendlyct,int neutralct,int enemyct) {
        this.friendlyct = friendlyct;
        this.neutralct = neutralct;
        this.enemyct = enemyct;
    }
    public Location getStart() {
        return start;
    }
    public Location getEnd() {
        return end;
    }
    public double getProbabilitate() {
        return probabilitate;
    }
    public int getFriendlyct() {
        return friendlyct;
    }
    public int getNeutralct() {
        return neutralct;
    }
    public int getEnemyct() {
        return enemyct;
    }
    @Override
    public String toString() {
        return "Ruta de la " + start.getName() + " la " + end.getName() +
                " (Probabilitate: " + probabilitate +
                ", Friendly: " + friendlyct +
                ", Neutral: " + neutralct +
                ", Enemy: " + enemyct + ")";
    }
}
