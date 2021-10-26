package com.sia.client.ui;// A JavaFX application skeleton.
 
import javafx.application.*; 
import javafx.scene.*; 
import javafx.stage.*; 
import javafx.scene.layout.*;

import static com.sia.client.config.Utils.log;

public class JavaFXSkel extends Application { 
 
  public static void main(String[] args) { 
  
    log("Launching JavaFX application.");
  
    // Start the JavaFX application by calling launch(). 
    launch(args);   
  } 
 
  // Override the init() method. 
  public void init() { 
    log("Inside the init() method.");
  } 
 
  // Override the start() method. 
  public void start(Stage myStage) { 
 
    log("Inside the start() method.");
 
    // Give the stage a title. 
    myStage.setTitle("JavaFX Skeleton"); 
 
    // Create a root node. In this case, a flow layout 
    // is used, but several alternatives exist. 
    FlowPane rootNode = new FlowPane(); 
 
    // Create a scene. 
    Scene myScene = new Scene(rootNode, 300, 200); 
 
    // Set the scene on the stage. 
    myStage.setScene(myScene); 
 
    // Show the stage and its scene. 
    myStage.show(); 
  } 
 
  // Override the stop() method. 
  public void stop() { 
    log("Inside the stop() method.");
  } 
}
