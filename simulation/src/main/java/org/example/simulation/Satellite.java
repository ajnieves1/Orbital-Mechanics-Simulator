package org.example.simulation;

import java.util.Arrays;

public class Satellite {
    private double mass;
    private double[] pos; // Array of coordinates
    private double[] velocity; // Array of x, y, and z velocities

    public Satellite(double mass, double[] pos, double[] velocity) { // Constructor
        this.mass = mass;
        this.pos = pos;
        this.velocity = velocity;
    }
    public double getMass() { // Simple getters and setters
        return mass;
    }

    public double[] getPos() {
        return pos;
    }

    public double[] getVelocity() {
        return velocity;
    }

    public void setPos(double[] position) { // Method that will change the position of our satellite
        this.pos = position;
    }

    public void setVelocity(double[] velocity) { // Method that will change velocity of our satellite
        this.velocity = velocity;
    }
    @Override
    public String toString() {
        return "Satellite{" +
                "mass=" + mass +
                ", pos=" + Arrays.toString(pos) +
                ", velocity=" + Arrays.toString(velocity) +
                '}';
    }
}
