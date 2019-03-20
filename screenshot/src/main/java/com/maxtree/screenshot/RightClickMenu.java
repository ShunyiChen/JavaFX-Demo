package com.maxtree.screenshot;

import com.maxtree.screenshot.utils.ImageUtils;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class RightClickMenu {

	public RightClickMenu(Screenshot snapshot) {
		this.snapshot = snapshot;
		cm.getStyleClass().add("context-menu-aa");
		cm.getItems().addAll(rectItem, cirItem, arrowItem, bruItem, mosItem, textItem, undoItem, new SeparatorMenuItem(), reselItem, finishItem,
				saveItem, new SeparatorMenuItem(), hideItem, quitItem);

		rectItem.setOnAction(event -> {
			snapshot.getEditToolbar().getRect().fireAction();
		});

		cirItem.setOnAction(event -> {
			snapshot.getEditToolbar().getCircle().fireAction();
		});

		arrowItem.setOnAction(event -> {
			snapshot.getEditToolbar().getArrow().fireAction();
		});

		bruItem.setOnAction(event -> {
			snapshot.getEditToolbar().getBrush().fireAction();
		});

		mosItem.setOnAction(event -> {
			snapshot.getEditToolbar().getMosaic().fireAction();
		});

		textItem.setOnAction(event -> {
			snapshot.getEditToolbar().getText().fireAction();
		});

		undoItem.setOnAction(event -> {
			snapshot.undoTheLastChange();
		});

		reselItem.setOnAction(event -> {
			snapshot.backToScreen();
		});

		finishItem.setOnAction(event -> {
			snapshot.finish();
		});

		saveItem.setOnAction(event -> {
			snapshot.save();
		});

		hideItem.setOnAction(event -> {
			snapshot.getEditToolbar().setDisplayed(!snapshot.getEditToolbar().isDisplayed());
		});
		quitItem.setOnAction(event -> {
			snapshot.quit();
		});
	}

	public void show2(Node ownerNode, double anchorX, double anchorY) {
		if (snapshot.getEditToolbar().isDisplayed()) {
			hideItem.setText("Hide toolbar");
		} else {
			hideItem.setText("Show toolbar");
		}
		cm.show(ownerNode, anchorX, anchorY);
	}
	
	public MenuItem getRectItem() {
		return rectItem;
	}
	
	private MenuItem rectItem = new MenuItem("Rectangle", ImageUtils.createImageView("Rectangle.png"));
	private MenuItem cirItem = new MenuItem("Circle", ImageUtils.createImageView("Circle.png"));
	private MenuItem arrowItem = new MenuItem("Arrow", ImageUtils.createImageView("Arrow.png"));
	private MenuItem bruItem = new MenuItem("Brush", ImageUtils.createImageView("Brush.png"));
	private MenuItem mosItem = new MenuItem("Mosaic", ImageUtils.createImageView("Mosaic.png"));
	private MenuItem textItem = new MenuItem("Text", ImageUtils.createImageView("Text.png"));
	private MenuItem undoItem = new MenuItem("Undo", ImageUtils.createImageView("Undo.png"));
	private MenuItem reselItem = new MenuItem("Re-select the screen capture");
	private MenuItem finishItem = new MenuItem("Finish", ImageUtils.createImageView("Finish.png"));
	private MenuItem saveItem = new MenuItem("Save", ImageUtils.createImageView("Save.png"));
	private MenuItem hideItem = new MenuItem("Hide toolbar");
	private MenuItem quitItem = new MenuItem("Quit", ImageUtils.createImageView("Exit.png"));
	private ContextMenu cm = new ContextMenu();
	private Screenshot snapshot;
}
