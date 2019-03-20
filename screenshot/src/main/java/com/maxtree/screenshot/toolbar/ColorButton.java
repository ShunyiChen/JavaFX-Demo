package com.maxtree.screenshot.toolbar;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

public class ColorButton extends StackPane {

	public ColorButton(Color color, double w, double h) {
		
		this.color = color;
		
		fill = new Rectangle(0, 0, w, h);
		fill.setFill(color);
		
		border = new Rectangle(w + 1, h + 1);
		border.setStroke(Color.GRAY);
		border.setFill(null);
		border.setStrokeWidth(1);
		
		border2 = new Rectangle(w + 3, h + 3);
		border2.setVisible(false);
		border2.setStroke(Color.GRAY);
		border2.setFill(null);
		border2.setStrokeWidth(1);
		
		this.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				border.setStroke(Color.WHITE);
				border2.setVisible(true);
			}
		});
		
		this.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				border.setStroke(Color.GRAY);
				border2.setVisible(false);
			}
		});
		
		this.getChildren().addAll(border2, fill, border);
	}
	
	public void setColor(Color color) {
		this.color = color;
		fill.setFill(color);
		if (action != null) {
			action.call(color);
		}
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setAction(Callback<Color, String> action) {
		this.action = action;
	}

	private Color color;
	private Rectangle border;
	private Rectangle border2;
	private Rectangle fill;
	private Callback<Color, String> action;
}
