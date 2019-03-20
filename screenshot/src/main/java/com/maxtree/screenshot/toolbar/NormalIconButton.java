package com.maxtree.screenshot.toolbar;

import com.maxtree.screenshot.utils.ImageUtils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class NormalIconButton extends StackPane {

	public NormalIconButton(String imgName, String tip) {
		this("", imgName, tip);
	}
	
	public NormalIconButton(String txt, String imgName, String tip) {

		button = new Button(txt, ImageUtils.createImageView(imgName));
		button.setStyle("-fx-padding:5;");
		button.setAlignment(Pos.CENTER_RIGHT);

		button.setTooltip(new Tooltip(tip));
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
				border.setVisible(false);
			}
		});
	}
	
	public void setOnAction(EventHandler<ActionEvent> value) {
		button.setOnAction(value);
	}

	private Rectangle border;
	private Button button;
}
