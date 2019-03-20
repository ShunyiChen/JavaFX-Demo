package com.maxtree.screenshot.toolbar;

import javafx.scene.paint.Color;
import javafx.stage.Window;

public interface ToolBarIF {

	void show2(Window window, double anchorX, double anchorY);
	
	void hide2();
	
	void setGroup(BarGroup group);
	
	Color getSelectedColor();
	
	double getFontSize();
	
	double getLineWeight();
	
	double getBlur();
}
