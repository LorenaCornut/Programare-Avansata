package org.example;

import java.io.PrintWriter;
import java.util.Objects;

public class Player {
    private final String name;
    private final transient PrintWriter out;

    public Player(String name, PrintWriter out) {
        this.name = Objects.requireNonNull(name);
        this.out = Objects.requireNonNull(out);
    }

    public void notify(String message) {
        out.println(message);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}