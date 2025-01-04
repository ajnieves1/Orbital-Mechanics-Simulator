package org.example.simulation;

public class OrbitHandling {
    private static final double gravity = 6.6740e-11; // Gravaitational constant

    public void updatePos(Satellite satellite, double time) {
        double[] pos = satellite.getPos(); // Create copy of array of position coordinates
        double[] velocity = satellite.getVelocity(); // Create copy of velocities

        for (int i = 0; i < pos.length; i++) { // Iterate through pos array, update position based on velocity and time
            pos[i] += velocity[i] * time; // Multiply the x y and z velocities by the provided time
        }
        satellite.setPos(pos); // Update new position
    }
    public void updateVelocity(Satellite satellite, double[] force, double time) {
        double[] velocity = satellite.getVelocity();
        double mass = satellite.getMass();

        for (int i = 0; i < velocity.length; i++) { // For loop that will update velocity based on force and mass
            velocity[i] += (force[i] / mass) * time;
        }

        satellite.setVelocity(velocity); // Update the satellite's velocity
    }
    public double[] calcGravForce(Satellite satellite, double[] otherPos, double otherMass) { // Method that will calculate the graviational force taking the satellite object, the other body's position, and the other body's mass. Uses newton's law of gravitation formula
        double[] pos = satellite.getPos(); // Create more copies of stuff
        double[] force = new double[3]; // The force has an x y and z

        double[] distanceVector = new double[3];
        for (int i = 0; i < pos.length; i++) { // This loop calculates the distance vector
            distanceVector[i] = otherPos[i] - pos[i];
        }

        // Calculate the distance magnitude
        double distanceMagnitude = Math.sqrt(distanceVector[0] * distanceVector[0] +
                distanceVector[1] * distanceVector[1] +
                distanceVector[2] * distanceVector[2]);

        double forceMagnitude = (gravity * satellite.getMass() * otherMass) / (distanceMagnitude * distanceMagnitude); // Force magnitude calculation

        // Calculate the force vector
        for (int i = 0; i < force.length; i++) { // This loops calculates the force vector
            force[i] = forceMagnitude * (distanceVector[i] / distanceMagnitude);
        }

        return force; // finally return the array of our new force

    }
}
