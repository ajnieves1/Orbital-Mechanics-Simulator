package org.example.simulation;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Controller {
    public BorderPane planet;
    public ChoiceBox celestialBodyField; // Whenever I decide to implement multiple planets this field will be used
    public ChoiceBox presetsField;
    @FXML
    private TextField xField, yField, zField, massField, xVelocityField, yVelocityField, zVelocityField; // User's input
    @FXML
    private Button myButton; // Button that allows user to save satellite settings
    @FXML
    private Sphere sphere; // Our planet that shows up
    @FXML
    Group root; // I decided to use a group for creating the satellite object
    @FXML
    private Label positionLabel; // Label thing to show the user the coordinate position of the satellite in accordance with real space scale
    @FXML
    private Label velocityLabel;// Same thing as positionlabel but for velocity

    private Sphere currentSatelliteSphere; // Variable that keeps track of our current satellite's sphere
    private Satellite currentSatellite; // Variable that keeps track of current satellite object
    private OrbitHandling orbitHandling = new OrbitHandling(); // Import orbithandling class

    // Variables for rotating planeet like gyro
    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);

    private static final double SCALE_FACTOR = 1e-10; // Variable that we can adjust depending on the scale of the numbers we want

    private Timeline timeline; // Timeline object that updates our satellites position every time step

    @FXML
    public void initialize() { // Method that initializes the planet and its texture, in addition to the timeline object
        sphere.getTransforms().addAll(rotateX, rotateY);

        // Load the earth.jpg image and set it as the texture for the sphere
        Image earthImage = new Image(getClass().getResourceAsStream("/org/example/simulation/earth.jpg"));
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(earthImage);
        sphere.setMaterial(material);

        // Initialize the timeline for real-time updates
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateSatellitePosition()));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    @FXML
    protected void onButtonClick(ActionEvent event) { // Method that saves users desired settings for the sattellite
        try {
            double x = Double.parseDouble(xField.getText());
            double y = Double.parseDouble(yField.getText());
            double z = Double.parseDouble(zField.getText());
            double mass = Double.parseDouble(massField.getText());
            double xVelocity = Double.parseDouble(xVelocityField.getText());
            double yVelocity = Double.parseDouble(yVelocityField.getText());
            double zVelocity = Double.parseDouble(zVelocityField.getText());

            createSatelliteSphere(x, y, z, mass, xVelocity, yVelocity, zVelocity); // Create satellite object from parsed values from user

        } catch (NumberFormatException e) { // Exception if user enters something but a number
            System.out.println("Enter only numbers");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void onDrag(MouseEvent event) { // Method that lets us rotate the planet
        double deltaX = event.getSceneX() - anchorX;
        double deltaY = event.getSceneY() - anchorY;
        rotateX.setAngle(anchorAngleX - deltaY);
        rotateY.setAngle(anchorAngleY + deltaX);
    }

    @FXML
    public void onMousePressed(MouseEvent event) {
        anchorX = event.getSceneX();
        anchorY = event.getSceneY();
        anchorAngleX = rotateX.getAngle();
        anchorAngleY = rotateY.getAngle();
    }

    private void createSatelliteSphere(double x, double y, double z, double mass, double xVelocity, double yVelocity, double zVelocity) { // Method that creates our satellite object
        if (currentSatelliteSphere != null) { // Checks if a satellite sphere is made, if it is, replace it
            root.getChildren().remove(currentSatelliteSphere);
        }

        Sphere satelliteSphere = new Sphere(10); // Create a sphere with radius 10
        satelliteSphere.setTranslateX(x * SCALE_FACTOR); // Move the satellite to the desired x position
        satelliteSphere.setTranslateY(y * SCALE_FACTOR); // Same thing as x for y and z
        satelliteSphere.setTranslateZ(z * SCALE_FACTOR); // We scaled down the numbers to avoid satellite going off the screen since I don't know how NASA actually handles these kind of things so I just wanted to keep it on the program window most cases

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.RED); // Set the color of the sphere
        satelliteSphere.setMaterial(material);

        root.getChildren().add(satelliteSphere); // Add the sphere to the scene
        currentSatelliteSphere = satelliteSphere; // Update the reference to the current satellite sphere

        // Create our satellite object
        double[] position = {x, y, z}; // Set array with our position coordinates
        double[] velocity = {xVelocity / 3600, yVelocity / 3600, zVelocity / 3600}; // Convert velocities to km/s
        currentSatellite = new Satellite(mass, position, velocity); // Allows us to not have duplicate satellites
//        System.out.println("Satellite created: " + currentSatellite); // Debugging statement

        // Start the timeline for real-time updates
        timeline.play();
    }

    private void updateSatellitePosition() { // Method that updates our satellites position
        if (currentSatelliteSphere != null && currentSatellite != null) { // If there is a satellite sphere and a satellite object, continue
            double[] planetPos = {0, 0, 0}; // Assume our planet is at origin
            double planetMass = 5.972e24; // Mass of earth in KG

            double[] force = orbitHandling.calcGravForce(currentSatellite, planetPos, planetMass); // Calculates gravitational force using Newton's law of gravitation
            orbitHandling.updateVelocity(currentSatellite, force, 1); // Update velocity of the satellite using the force we calculated in previous line
            orbitHandling.updatePos(currentSatellite, 1); // Update position

            double[] newPos = currentSatellite.getPos(); // Calculate our new position of satellite
            // Move the satellite sphere
            currentSatelliteSphere.setTranslateX(newPos[0] *  SCALE_FACTOR);
            currentSatelliteSphere.setTranslateY(newPos[1] * SCALE_FACTOR);
            currentSatelliteSphere.setTranslateZ(newPos[2] * SCALE_FACTOR);

            double[] velocity = currentSatellite.getVelocity(); // Get the velocity of our satellite and store it in the velocity array

            // Print statements that somewhat shows what the velocity would be like in real space
            positionLabel.setText(String.format("Position: X=%.2f, Y=%.2f, Z=%.2f", newPos[0], newPos[1], newPos[2]));
            velocityLabel.setText(String.format("Velocity: X=%.2f km/h, Y=%.2f km/h, Z=%.2f km/h", velocity[0] * 3600, velocity[1] * 3600, velocity[2] * 3600));

            // This print statement shows our updated position within the program window, not using the insane numbers we get in space
            System.out.println("Updated position: X=" + newPos[0] + ", Y=" + newPos[1] + ", Z=" + newPos[2]);
        }
    }
}