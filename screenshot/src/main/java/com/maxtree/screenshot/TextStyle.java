package com.maxtree.screenshot;

import javafx.scene.paint.Color;

public class TextStyle {

	public TextStyle(double fontSize, Color fontColor) {
		this.fontSize = fontSize;
		this.fontColor = fontColor;
	}

	public double getFontSize() {
		return fontSize;
	}

	public void setFontSize(double fontSize) {
		this.fontSize = fontSize;
	}

	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

	public String toString() {
		return "fontSize=" + fontSize + ",fontColor=" + fontColor;
	}

	private double fontSize;
	private Color fontColor;
}
