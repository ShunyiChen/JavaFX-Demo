package com.maxtree.screenshot.toolbar;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Window;

public class MosaicToolBar extends Popup implements ToolBarIF {

	public MosaicToolBar() {
		
		setAutoFix(false);
		setAutoHide(false);
		rb3.setSelected(true);
		
		rb1.setOnAction(e -> {
			rb1.setSelected(true);
			line_weight = 1d;
		});
		
		rb2.setOnAction(e -> {
			rb2.setSelected(true);
			line_weight = 3d;
		});
		
		rb3.setOnAction(e -> {
			rb3.setSelected(true);
			line_weight = 5d;
		});
		
        Label blur = new Label("Blur degree");
        
		HBox t = new HBox();
		t.getStyleClass().add("EditToolbar");
		t.setAlignment(Pos.CENTER);
		t.setBackground(new Background(new BackgroundFill(Color.rgb(234, 238, 245), CornerRadii.EMPTY, Insets.EMPTY)));
        t.getChildren().addAll(rb1, rb2, rb3, blur, slider);
		getContent().add(t);
	}

	@Override
	public void show2(Window window, double anchorX, double anchorY) {
		// Hide others
		for (ToolBarIF b : group.getBars()) {
			if (this != b) {
				b.hide2();
			}
		}
		// Show tootbar
		super.show(window, anchorX, anchorY + 2);
		group.selection = this;
	}

	@Override
	public void hide2() {
		super.hide();
	}
	
	@Override
	public void setGroup(BarGroup group) {
		this.group = group;
	}
	
	@Override
	public Color getSelectedColor() {
		return null;
	}

	@Override
	public double getFontSize() {
		return 0;
	}
	
	@Override
	public double getLineWeight() {
		return line_weight;
	}

	@Override
	public double getBlur() {
		return slider.getValue();
	}
	
	public Slider getSlider() {
		return slider;
	}
	
	private RadioButton rb1 = new RadioButton("line_weight_1.png", this.hashCode()+"");
	private RadioButton rb2 = new RadioButton("line_weight_3.png", this.hashCode()+"");
	private RadioButton rb3 = new RadioButton("line_weight_5.png", this.hashCode()+"");
	private double line_weight = 5d;
	private Slider slider = new Slider(5.0d, 15.0d, 8.0d);
	private BarGroup group;
}
