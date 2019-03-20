package com.maxtree.screenshot.toolbar;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maxtree.screenshot.utils.ImageUtils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RadioButton extends StackPane {

	public RadioButton(String imgName, String groupName) {
		this.groupName = groupName;
		
		if (groups.containsKey(groupName)) {
			groups.get(groupName).add(this);
		} else {
			List<RadioButton> list = new ArrayList<RadioButton>();
			list.add(this);
			groups.put(groupName, list);
		}
		
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
	
	public void setSelected(boolean selected) {
		
		this.selected = selected;
		
		if (selected) {
			border.setFill(Color.rgb(168, 168, 168, 0.2d));
			border.setVisible(true);
			
			List<RadioButton> members = groups.get(groupName);
			for (int i = 0; i < members.size(); i++) {
				if (members.get(i) != this) {
					members.get(i).setSelected(false);
				}
			}
			
		} else {
			border.setFill(null);
			border.setVisible(false);
		}
	}
	
	public void setOnAction(EventHandler<ActionEvent> value) {
		button.setOnAction(value);
	}
	
	private String groupName;
	private boolean selected;
	private Rectangle border;
	private Button button;
	public static Map<String, List<RadioButton>> groups = new HashMap<String, List<RadioButton>>();
}
