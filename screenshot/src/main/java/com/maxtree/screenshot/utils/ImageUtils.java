/*
 * Copyright (C) 2016 Shunyi Chen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.maxtree.screenshot.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageUtils {

	/**
	 * Creates ImageView by fileName.
	 * 
	 * @param fileName
	 * @return
	 */
	public static ImageView createImageView(String fileName) {
		InputStream istream;
		try {
			istream = new FileInputStream(new File(SHARED_RESOURCES + "/" + fileName));
			return new ImageView(new Image(istream));
		} catch (FileNotFoundException ex) {
		}
		return null;
	}

	/**
	 * Creates image by fileName.
	 * 
	 * @param fileName
	 * @return
	 */
	public static Image createImage(String fileName) {
		InputStream istream;
		try {
			istream = new FileInputStream(new File(SHARED_RESOURCES + "/" + fileName));
			return new Image(istream);
		} catch (FileNotFoundException ex) {
		}
		return null;
	}

	/**
	 * Gets all pixels from an image.
	 * 
	 * @param image
	 * @return
	 */
	public static Color[][] loadPixelsFromImage(BufferedImage image) {
		Color[][] colors = new Color[image.getWidth()][image.getHeight()];
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				colors[x][y] = new Color(image.getRGB(x, y));
			}
		}
		return colors;
	}

	/**
	 * Zoom in image
	 * 
	 * @param originalImage
	 * @param times
	 * @return
	 */
	public static BufferedImage zoomInImage(BufferedImage originalImage, Integer times) {
		int width = originalImage.getWidth() * times;
		int height = originalImage.getHeight() * times;
		BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
		Graphics g = newImage.getGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
		return newImage;
	}
	
	private static String SHARED_RESOURCES = "shared-resources";
}
