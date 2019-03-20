package com.maxtree.screenshot.toolbar;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Window;

public class GraphicToolBar extends Popup implements ToolBarIF {
	
	public GraphicToolBar() {
		
		setAutoFix(false);
		setAutoHide(false);
		rb1.setSelected(true);
		
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
		
		TilePane tilePane = new TilePane();
		tilePane.setPrefColumns(8); //preferred columns
        tilePane.setAlignment(Pos.CENTER);
        
        ColorButton black = new ColorButton(Color.BLACK, w, h);
        ColorButton gray = new ColorButton(Color.rgb(128, 128, 128), w, h);
        ColorButton red1 = new ColorButton(Color.rgb(128, 0, 0), w, h);
        ColorButton orange = new ColorButton(Color.rgb(247, 136, 58), w, h);
        ColorButton green = new ColorButton(Color.rgb(48, 132, 48), w, h);
        ColorButton blue1 = new ColorButton(Color.rgb(56, 90, 211), w, h);
        ColorButton purple = new ColorButton(Color.rgb(128, 0, 128), w, h);
        ColorButton green2 = new ColorButton(Color.rgb(0, 153, 153), w, h);
        
        ColorButton white = new ColorButton(Color.WHITE, w, h);
        ColorButton gray2 = new ColorButton(Color.rgb(192, 192, 192), w, h);
        ColorButton red2 = new ColorButton(Color.RED, w, h);
        ColorButton yellow = new ColorButton(Color.rgb(255, 255, 0), w, h);
        ColorButton green3 = new ColorButton(Color.rgb(153, 204, 0), w, h);
        ColorButton blue3 = new ColorButton(Color.rgb(56, 148, 228), w, h);
        ColorButton violet = new ColorButton(Color.rgb(243, 27, 243), w, h);
        ColorButton green4 = new ColorButton(Color.rgb(22, 220, 220), w, h);
        
        black.setOnMouseReleased(value -> {
        	big.setColor(Color.BLACK);
        });
        gray.setOnMouseReleased(value -> {
        	big.setColor(gray.getColor());
        });
        red1.setOnMouseReleased(value -> {
        	big.setColor(red1.getColor());
        });
        orange.setOnMouseReleased(value -> {
        	big.setColor(orange.getColor());
        });
        green.setOnMouseReleased(value -> {
        	big.setColor(green.getColor());
        });
        blue1.setOnMouseReleased(value -> {
        	big.setColor(blue1.getColor());
        });
        purple.setOnMouseReleased(value -> {
        	big.setColor(purple.getColor());
        });
        green2.setOnMouseReleased(value -> {
        	big.setColor(green2.getColor());
        });
        
        white.setOnMouseReleased(value -> {
        	big.setColor(Color.WHITE);
        });
        gray2.setOnMouseReleased(value -> {
        	big.setColor(gray2.getColor());
        });
        red2.setOnMouseReleased(value -> {
        	big.setColor(red2.getColor());
        });
        yellow.setOnMouseReleased(value -> {
        	big.setColor(yellow.getColor());
        });
        green3.setOnMouseReleased(value -> {
        	big.setColor(green3.getColor());
        });
        blue3.setOnMouseReleased(value -> {
        	big.setColor(blue3.getColor());
        });
        violet.setOnMouseReleased(value -> {
        	big.setColor(violet.getColor());
        });
        green4.setOnMouseReleased(value -> {
        	big.setColor(green4.getColor());
        });
        
        tilePane.getChildren().addAll(black, gray, red1, orange, green, blue1, purple, green2, white, gray2, red2, yellow, green3, blue3, violet, green4);
        
		HBox t = new HBox();
		t.getStyleClass().add("EditToolbar");
		t.setBackground(new Background(new BackgroundFill(Color.rgb(234, 238, 245), CornerRadii.EMPTY, Insets.EMPTY)));
        t.getChildren().addAll(rb1, rb2, rb3, big, tilePane);
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
		return big.getColor();
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
		return 0;
	}
	
	private double w = 12;
	private double h = 12;
	private ColorButton big = new ColorButton(Color.RED, 23, 23);
	private double line_weight = 1d;
	private RadioButton rb1 = new RadioButton("line_weight_1.png", this.hashCode()+"");
	private RadioButton rb2 = new RadioButton("line_weight_3.png", this.hashCode()+"");
	private RadioButton rb3 = new RadioButton("line_weight_5.png", this.hashCode()+"");
	private BarGroup group;
}
