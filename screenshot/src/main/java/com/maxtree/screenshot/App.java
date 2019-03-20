package com.maxtree.screenshot;

import com.maxtree.screenshot.hotkey.RegisterHotkey;
import com.maxtree.screenshot.utils.ImageUtils;

import javafx.animation.Animation;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * MaxTree's Screenshot!
 */
public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		singleton = this;
		this.primaryStage = primaryStage;
		createContent();
	}

	public void stop() throws Exception {
		hotkey.unregister();
		System.exit(0);
    }
	
	public void createContent() {
		// INITIALISATION OF THE STAGE/SCENE
		// create stage which has set stage style transparent
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setAlwaysOnTop(true);
		
		// Create StackPane
		int numCells = 7;
		Image explosionImage = ImageUtils.createImage("floatpanel.oricle.bird.png");
		double cellWidth  = explosionImage.getWidth() / numCells;
        double cellHeight = explosionImage.getHeight();
		
        Rectangle2D[] cellClips = new Rectangle2D[numCells];
        for (int i = 0; i < numCells; i++) {
            cellClips[i] = new Rectangle2D(
                    i * cellWidth, 0,
                    cellWidth, cellHeight
            );
        }
        
		StackPane stack = new StackPane();
		stack.setStyle("-fx-background-color: rgba(255, 255, 255, 0);");
		
		ImageView blueBg = new ImageView();
		blueBg.setImage(explosionImage);
		cellClips[2] = new Rectangle2D(
    		cellClips[2].getMinX() + 1, -1,
    		cellClips[2].getWidth() - 2, cellClips[2].getHeight()
        );
		blueBg.setViewport(cellClips[2]);
		
	    ImageView highlight = new ImageView();
	    highlight.setImage(explosionImage);
	    // Adjust the rectangle
	    cellClips[6] = new Rectangle2D(
    		cellClips[6].getMinX() + 2, 0,
    		cellClips[6].getWidth() - 1, cellClips[6].getHeight()
        );
	    highlight.setViewport(cellClips[6]);
	    
	    Text maxlogo = new Text("Max");
	    maxlogo.setTranslateY(-2);
	    DropShadow dropShadow = new DropShadow();
	    maxlogo.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 20));
	    maxlogo.setEffect(dropShadow);
	    maxlogo.setFill(Color.WHITE);
	    
	    stack.getChildren().addAll(blueBg, highlight, maxlogo);
	    
	    // Highlight animation
        final Animation animation = new RotateAnimation(
        	highlight,
            Duration.millis(1000)
        );
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
        
        
		ContextMenu cm = new ContextMenu();
//		MenuItem menuItem1 = new MenuItem("Show main window");
//		MenuItem menuItem11 = new MenuItem("Hide main window");
//		MenuItem menuItem2 = new MenuItem("New Task");
//		MenuItem menuItem3 = new MenuItem("Start all tasks");
//		MenuItem menuItem4 = new MenuItem("Pause all tasks");
		MenuItem menuItem1 = new MenuItem("Capture screen!!!");
		MenuItem menuItem5 = new MenuItem("Exit");
		
		menuItem1.setOnAction(event -> {
			hotkey.onHotKey(RegisterHotkey.CTRL_ALT_A);
		});
		menuItem5.setOnAction(event -> {
			try {
				stop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			System.exit(0);
		});
		
//		menuItem11.setVisible(false);
//		cm.getItems().addAll(menuItem1, menuItem11, menuItem2, menuItem3, menuItem4, menuItem5);
		cm.getItems().addAll(menuItem1, menuItem5);
		
		stack.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if (e.getButton() == MouseButton.SECONDARY) {
					cm.hide();
					cm.show(stack, e.getScreenX(), e.getScreenY());
				}
			}
		});
		
		// when mouse button is pressed, save the initial position of screen
		stack.setOnMousePressed((MouseEvent me) -> {
			initX = me.getScreenX() - primaryStage.getX();
			initY = me.getScreenY() - primaryStage.getY();
		});
		// when screen is dragged, translate it accordingly
		stack.setOnMouseDragged((MouseEvent me) -> {
			primaryStage.setX(me.getScreenX() - initX);
			primaryStage.setY(me.getScreenY() - initY);
		});
		
		// create scene with set width, height and color
		Scene scene = new Scene(stack, Color.TRANSPARENT);
		// add all nodes to main root group
		scene.getStylesheets().add("styles/Styles.css");
		
		
		// set scene to stage
		primaryStage.setScene(scene);
		// center stage on screen
		primaryStage.centerOnScreen();
		// show the stage
		primaryStage.show(); 
		
		
		Thread jna = new Thread(new Runnable() {
			@Override
			public void run() {
				hotkey.initJIntellitype();
				hotkey.register();
			}
		});
		jna.start();
		
	}
	
	public static App getInstance() {
        return singleton;
    }
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	// variables for storing initial position of the stage at the beginning of drag
	private double initX;
	private double initY;
	private Stage primaryStage;
	private static App singleton;
	private RegisterHotkey hotkey = new RegisterHotkey();
}
