package com.maxtree.screenshot;

import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.scene.control.skin.TextAreaSkin;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class TextLabel extends StackPane {
	
	/**
	 * Constructor
	 * 
	 * @param gc
	 * @param pane
	 */
	public TextLabel(GraphicsContext gc, Group pane, Screenshot screenshot) {
		this.gc = gc;
		this.pane = pane;
		ta.setPrefSize(size, size);
		ta.setWrapText(true);
		ta.getStyleClass().add("custom-dashed-border");
		getChildren().add(ta);
		ta.setFocusTraversable(false);
		ta.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue,
					String newValue) {
				resizeByText(newValue);
			}
		});
		
		ta.focusedProperty().addListener(new ChangeListener<Boolean>() {
	        @Override
	        public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
	        	if (arg0.getValue()) {
	        		screenshot.setCurrentTextLabel(TextLabel.this);
	        	}
	        }
		});
		
		setOnMouseEntered(e -> {
			if (screenshot.getDrawingType() == DrawingType.Text) {
				getTextArea().setVisible(true);
				getItem().setIgnored(true);
				screenshot.drawAll();
				
			}
			
		});
		
		setOnMouseExited(e -> {
			if (getTextArea().isVisible() && !getTextArea().isFocused()) {
				getTextArea().setVisible(false);
				getItem().setIgnored(false);
				screenshot.drawAll();
				
			}
		});
		

		ta.setOnMouseMoved(e -> {
			ta.setCursor(Cursor.MOVE);
		});
		
		ta.setOnMousePressed(e -> {
	    	initX = getLayoutX();
	    	initY = getLayoutY();
	    	
	    	startX = e.getScreenX();
	    	startY = e.getScreenY();
	    });
		
		ta.setOnMouseDragged(e -> {
			double x2 = e.getScreenX() - startX;
			double y2 = e.getScreenY() - startY;
	    	
			setLayoutX(initX + x2);
			setLayoutY(initY + y2);
		});
		
	}
	
	private void resizeByText(String text) {
		Text t = new Text(text);
		t.setFont(ta.getFont());
		
		int width = (int)t.getBoundsInLocal().getWidth();
		int height = (int)t.getBoundsInLocal().getHeight();
		ta.setPrefWidth(width + size);
		ta.setPrefHeight(height + size);
		
	}
	
	public void startEditing(double x, double y) {
        if (!pane.getChildren().contains(this)) {
        	pane.getChildren().add(this);
        }
        this.setLayoutX(x);
        this.setLayoutY(y);
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	ta.requestFocus();
            }
        });
        
        // JavaFX disable TextArea scrolling
        ta.applyCss();
        ta.layout();
		ScrollBar scrollBarv = (ScrollBar)ta.lookup(".scroll-bar:vertical");
		scrollBarv.setDisable(true);
	}
	
	public void stopEditing() {
		
		if (!ta.getText().trim().equals("")) {
			ta.setVisible(false);
			
			TextAreaSkin skin = (TextAreaSkin) ta.getSkin();
		    Rectangle2D d = skin.getCharacterBounds(0);
			
			Text t = new Text(ta.getText());
			t.setFont(Font.font(ts.getFontSize()));
			double height = t.getBoundsInLocal().getHeight();
	        int rowCount = 1;
	        for (int i = 0; i < ta.getText().length(); i++) {
	        	char ch = ta.getText().charAt(i);
	        	if (ch == '\n') {
	        		rowCount++;
	        	}
	        }
			
	        tx = getLayoutX() + d.getMinX() + 2d;
	        ty = getLayoutY() + height / rowCount;
	        gc.setTextAlign(TextAlignment.LEFT);
			gc.fillText(ta.getText(), tx, ty);
			// Restore the text alignment
			gc.setTextAlign(TextAlignment.CENTER);
		    item = new DrawingItem(
					new double[]{tx},
					new double[]{ty},
					DrawingType.Text,
					new Object[]{this},
					ts.getFontColor(),
					0,
					true);
			item.setFontSize(ts.getFontSize());
			
		} else {
			pane.getChildren().remove(this);
			item.setContents(null);
		}
	}
 
	public boolean isEditing() {
		return pane.getChildren().contains(this) && ta.isFocused();
	}
	
	public void updatesStyle() {
		updatesStyle(ts);
	}
	
	public void updatesStyle(TextStyle ts) {
		this.ts = ts;
		ta.setStyle("-fx-text-fill: rgb("+(ts.getFontColor().getRed() * 255)+","+(ts.getFontColor().getGreen() * 255)+","+(ts.getFontColor().getBlue() * 255)+"); -fx-font-size: "+ts.getFontSize()+";");
		gc.setFill(ts.getFontColor());
		gc.setFont(Font.font(ts.getFontSize()));
		ta.requestFocus();
		
		ta.setFont(Font.font(ts.getFontSize()));
		resizeByText(ta.getText());
	}
	
	public TextStyle getTextStyle() {
		return ts;
	}
	
	public TextArea getTextArea() {
		return ta;
	}
	
	public double getTx() {
		return tx;
	}

	public double getTy() {
		return ty;
	}

	public DrawingItem getItem() {
		return item;
	}
	
	private TextStyle ts;
	private double tx;
	private double ty;
	private double initX;
	private double initY;
	private double startX;
	private double startY;
	private TextArea ta = new TextArea();
	private GraphicsContext gc;
	private Group pane;
	private final double size = 40;
	public static List<TextLabel> LIST = new ArrayList<TextLabel>();
	private DrawingItem item = new DrawingItem();
}
