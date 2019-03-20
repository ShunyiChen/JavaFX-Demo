package com.maxtree.screenshot;

import javafx.scene.paint.Color;

public class DrawingItem {

	/**
	 * Constructor
	 */
	public DrawingItem() {
		// Do nothing.
	}

	/**
	 * Constructor
	 * 
	 * @param x
	 * @param y
	 * @param type
	 * @param contents
	 * @param color
	 * @param lineWeight
	 * @param erasable
	 *            For type Shadow/BorderWithoutVertex/Border/SizeInfo/Image, it
	 *            should be set to FALSE.
	 */
	public DrawingItem(double[] x, double[] y, DrawingType type, Object[] contents, Color color, double lineWeight,
			boolean erasable) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.contents = contents;
		this.color = color;
		this.lineWeight = lineWeight;
		this.erasable = erasable;
	}

	public double[] getX() {
		return x;
	}

	public void setX(double[] x) {
		this.x = x;
	}

	public double[] getY() {
		return y;
	}

	public void setY(double[] y) {
		this.y = y;
	}

	public DrawingType getType() {
		return type;
	}

	public void setType(DrawingType type) {
		this.type = type;
	}

	public Object[] getContents() {
		return contents;
	}

	public void setContents(Object[] contents) {
		this.contents = contents;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public double getLineWeight() {
		return lineWeight;
	}

	public void setLineWeight(double lineWeight) {
		this.lineWeight = lineWeight;
	}

	public double getBlur() {
		return blur;
	}

	public void setBlur(double blur) {
		this.blur = blur;
	}

	public double getFontSize() {
		return fontSize;
	}

	public void setFontSize(double fontSize) {
		this.fontSize = fontSize;
	}

	public boolean isErasable() {
		return erasable;
	}

	public void setErasable(boolean erasable) {
		this.erasable = erasable;
	}

	public boolean isIgnored() {
		return ignored;
	}

	public void setIgnored(boolean ignored) {
		this.ignored = ignored;
	}

	private double[] x; // X coordinate points
	private double[] y; // Y coordinate points
	private DrawingType type; // Type of drawing
	private Object[] contents; // Contents of drawing/painting
	private Color color; // Font/graphics color
	private double lineWeight; // line weight
	private double blur; // Mosaic blur degree
	private double fontSize; // Font size
	private boolean erasable; // Erasable,such as Rectangle,Circle,Arrow
	private boolean ignored; // Ignore the drawing
}
