package com.maxtree.screenshot.toolbar;

import com.maxtree.screenshot.utils.ImageUtils;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

public class ToggleIconButton extends StackPane {

	/**
	 * Constructor
	 * 
	 * @param imgName
	 * @param tip
	 */
	public ToggleIconButton(String imgName, String tip) {
		button = new Button("", ImageUtils.createImageView(imgName));
		button.setStyle("-fx-padding:5;");
		button.setAlignment(Pos.CENTER_RIGHT);
		
		border = new Rectangle(24, 20);
		border.setVisible(false);
		border.setStroke(Color.GRAY);
		border.setFill(null);
		border.setStrokeWidth(1);
		border.setArcWidth(4);
		border.setArcHeight(4);
		button.setBackground(Background.EMPTY);
		this.getChildren().addAll(border, button);
		
		if (tip != null && !"".equals(tip)) {
			button.setTooltip(new Tooltip(tip));
		}
		
		button.setOnMousePressed((MouseEvent me) -> {
			
			setSelected(!selected);
			
			// Perform action event.
			if (action != null) {
				action.call(selected);
			}
		});
		
		button.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				border.setWidth(button.getWidth() - 5);
				border.setVisible(true);
			}
		});
		
		button.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if (!selected) {
					border.setVisible(false);
				}
			}
		});
	}
	
	public void setOnAction(Callback<Boolean, String> action) {
		this.action = action;
	}
	
	public void fireAction() {
		setSelected(true);
		
		// Perform action event.
		if (action != null) {
			action.call(true);
		}
	}
	
	public void setSelected(boolean selected) {
		
		this.selected = selected;
		
		if (selected) {
			if (group != null) {
				for (ToggleIconButton b : group.getButtons()) {
					if (b != this) {
						b.setSelected(false);
					}
				}
			}
			border.setFill(Color.rgb(168, 168, 168, 0.2d));
			border.setVisible(true);
		} else {
			border.setFill(null);
			border.setVisible(false);
		}
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setGroup(ButtonGroup group) {
		this.group = group;
	}
	
	private boolean selected;
	private Rectangle border;
	private Button button;
	private Callback<Boolean, String> action;
	private ButtonGroup group;
	
}

