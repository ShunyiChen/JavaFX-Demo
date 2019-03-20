package com.maxtree.screenshot.utils;


public class ColorUtils {

	public static String toHexEncoding(javafx.scene.paint.Color c) {
		return toHexEncoding(new java.awt.Color((int)c.getRed(), (int)c.getGreen(), (int)c.getBlue()));
	}
	
	public static String toHexEncoding(java.awt.Color color) {
        String R, G, B;
        StringBuffer sb = new StringBuffer();
 
        R = Integer.toHexString(color.getRed());
        G = Integer.toHexString(color.getGreen());
        B = Integer.toHexString(color.getBlue());
 
        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;
 
        sb.append("#");
        sb.append(R);
        sb.append(G);
        sb.append(B);
 
        return sb.toString().toUpperCase();
    }
}
