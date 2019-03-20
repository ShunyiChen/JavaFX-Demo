package com.maxtree.screenshot;

import javafx.scene.image.Image;

/**
 * 
 * @author chens
 *
 */
public class MosaicImage {
	
	/**
	 * Constructor
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param image
	 */
	public MosaicImage(int x, int y, int w, int h, Image image) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.image = image;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	private int x;
	private int y;
	private int w;
	private int h;
	private Image image;
}
