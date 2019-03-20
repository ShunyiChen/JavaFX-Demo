package com.maxtree.screenshot;

import com.maxtree.screenshot.toolbar.BarGroup;
import com.maxtree.screenshot.toolbar.ButtonGroup;
import com.maxtree.screenshot.toolbar.GraphicToolBar;
import com.maxtree.screenshot.toolbar.MosaicToolBar;
import com.maxtree.screenshot.toolbar.NormalIconButton;
import com.maxtree.screenshot.toolbar.TextToolBar;
import com.maxtree.screenshot.toolbar.ToggleIconButton;
import com.maxtree.screenshot.toolbar.ToolBarIF;

import javafx.geometry.Rectangle2D;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Callback;

public class EditToolbar extends Popup {
	
	public EditToolbar(Screenshot screenshot) {
		this.screenshot = screenshot;
		
		HBox hbox = new HBox();
		hbox.getStyleClass().add("EditToolbar");
		hbox.setPrefWidth(barWidth);
		barMosaic.getSlider().setOnMouseReleased(e -> {
			screenshot.generateMosaicImage(barMosaic.getSlider().getValue());
			screenshot.updateMosaicItems();
		});
		
		barText.getComboBox().setOnAction(e -> {
			if (screenshot.getCurrentTextLabel() != null) {
				screenshot.getCurrentTextLabel().getTextStyle().setFontSize(barText.getComboBox().getValue());
				screenshot.getCurrentTextLabel().updatesStyle();
			}
		});
		
		barText.getColorButton().setAction(new Callback<Color, String>() {

			@Override
			public String call(Color param) {
				if (screenshot.getCurrentTextLabel() != null) {
					screenshot.getCurrentTextLabel().getTextStyle().setFontColor(param);
					screenshot.getCurrentTextLabel().updatesStyle();
				}
				return null;
			}
		});
		
//		fontSizeProperty.addListener(e -> {
//			if (screenshot.getTextLabel() != null) {
//				
//				System.out.println(barText.getComboBox().getValue()+"---");
//				screenshot.getTextLabel().update(new TextStyle(barText.getComboBox().getValue(), screenshot.getTextLabel().getTextStyle().getFontColor()));
//			}
//		});
//		fontSizeProperty.bind(barText.getComboBox().valueProperty());
		
		
		
		barGroup = new BarGroup();
		barGroup.addAll(barRect, barCircle, barArrow, barBrush, barMosaic, barText);
		
		btnGroup = new ButtonGroup();
		btnGroup.addAll(btnRect, btnCircle, btnArrow, btnBrush, btnMosaic, btnText);
		hbox.getChildren().addAll(btnRect, btnCircle, btnArrow, btnBrush, btnMosaic, btnText, btnUndo, btnSave, btnSend, btnCollect, btnShare, btnExit, btnFinish);
		getContent().add(hbox);
		
		btnRect.setOnAction(new Callback<Boolean, String> () {
			@Override
			public String call(Boolean selected) {
				if (selected) {
					display(barRect);
				
				} else {
					barRect.hide2();
				}
				screenshot.setEditable(selected);
				screenshot.setDrawingType(DrawingType.Rectangle);
				return null;
			}
		});
		
		btnCircle.setOnAction(new Callback<Boolean, String> () {
			@Override
			public String call(Boolean selected) {
				if (selected) {
					display(barCircle);
				} else {
					barCircle.hide2();
				}
				screenshot.setEditable(selected);
				screenshot.setDrawingType(DrawingType.Circle);
				return null;
			}
		});
		
		btnArrow.setOnAction(new Callback<Boolean, String> () {
			@Override
			public String call(Boolean selected) {
				if (selected) {
					display(barArrow);
				} else {
					barArrow.hide2();
				}
				screenshot.setEditable(selected);
				screenshot.setDrawingType(DrawingType.Arrow);
				return null;
			}
		});
		
		btnBrush.setOnAction(new Callback<Boolean, String> () {
			@Override
			public String call(Boolean selected) {
				if (selected) {
					display(barBrush);
				} else {
					barBrush.hide2();
				}
				screenshot.setEditable(selected);
				screenshot.setDrawingType(DrawingType.Brush);
				return null;
			}
		});
		
		btnMosaic.setOnAction(new Callback<Boolean, String> () {
			@Override
			public String call(Boolean selected) {
				if (selected) {
					display(barMosaic);
				} else {
					barMosaic.hide2();
				}
				screenshot.setEditable(selected);
				screenshot.setDrawingType(DrawingType.Mosaic);
				return null;
			}
		});
		
		btnText.setOnAction(new Callback<Boolean, String> () {
			@Override
			public String call(Boolean selected) {
				if (selected) {
					display(barText);
				} else {
					barText.hide2();
				}
				screenshot.setEditable(selected);
				screenshot.setDrawingType(DrawingType.Text);
				return null;
			}
		});
		
		btnUndo.setOnAction(e -> {
			screenshot.undoTheLastChange();
		});
		
		btnSave.setOnAction(e -> {
			screenshot.save();
		});
		
		btnSend.setOnAction(e -> {
			screenshot.send();
		});
		
		btnCollect.setOnAction(e -> {
			screenshot.collect();
		});
		
		btnShare.setOnAction(e -> {
			screenshot.share();
		});
		
		btnExit.setOnAction(e -> {
			screenshot.quit();
		});
		
		btnFinish.setOnAction(e -> {
			screenshot.finish();
		});
		
	}
	
	private void display(ToolBarIF bar) {
		if (displayed) {
			bar.show2(screenshot.getPrimaryStage(), getX(), (getY() + getHeight()));
		} else {
			bar.show2(screenshot.getPrimaryStage(), getX(), getY());
		}
	}
	
	public void display(Window window, Rectangle2D r2d) {
		double x = r2d.getMaxX() - barWidth;
		double y = r2d.getMaxY() + 5;
		if (x <= 0) {
			x = r2d.getMinX();
		}
		if (y + 70 >= screenshot.getScreenHeight()) {
			y = r2d.getMinY() - 35;
		}
		setX(x);
		setY(y);
		if (displayed) {
			show(window);
		}
	}
	
	public boolean isDisplayed() {
		return displayed;
	}
	
	public void setDisplayed(boolean displayed) {
		this.displayed = displayed;
		if (!displayed) {
			hideAll();
		} else {
			show();
		}
	}
	
	public void hideAll() {
		hide();
		barRect.hide();
		barCircle.hide();
		barArrow.hide();
		barBrush.hide();
		barMosaic.hide();
		barText.hide();
		btnGroup.clearSelection();
	}
	
	public ToolBarIF getBar() {
		return barGroup.selection;
	}

	public ToggleIconButton getRect() {
		return btnRect;
	}
	
	public ToggleIconButton getCircle() {
		return btnCircle;
	}
	
	public ToggleIconButton getArrow() {
		return btnArrow;
	}
	
	public ToggleIconButton getBrush() {
		return btnBrush;
	}
	
	public ToggleIconButton getMosaic() {
		return btnMosaic;
	}
	
	public ToggleIconButton getText() {
		return btnText;
	}
	
	private Screenshot screenshot;
	private boolean displayed = true;
	private final double barWidth = 403;
	private BarGroup barGroup;
	private ButtonGroup btnGroup;
	private ToggleIconButton btnRect = new ToggleIconButton("Rectangle.png", "Rectangle tool");
	private ToggleIconButton btnCircle = new ToggleIconButton("Circle.png", "Ellipse tool");
	private ToggleIconButton btnArrow = new ToggleIconButton("Arrow.png", "Arrow tool");
	private ToggleIconButton btnBrush = new ToggleIconButton("Brush.PNG", "Brush tool");
	private ToggleIconButton btnMosaic = new ToggleIconButton("Mosaic.png", "Mosaic tool");
	private ToggleIconButton btnText = new ToggleIconButton("Text.png", "Text tool");
	private NormalIconButton btnUndo = new NormalIconButton("Undo.png", "Undo editing");
	private NormalIconButton btnSave = new NormalIconButton("Save.png", "Save");
	private NormalIconButton btnSend = new NormalIconButton("dataline.png", "Send to cellphone");
	private NormalIconButton btnCollect = new NormalIconButton("myCollection.png", "Collect");
	private NormalIconButton btnShare = new NormalIconButton("qzonewb.png", "Share");
	private NormalIconButton btnExit = new NormalIconButton("Exit.png", "Exit the screenshot");
	private NormalIconButton btnFinish = new NormalIconButton("Finish", "Finish.png", "Complete the screenshot");
	
	private GraphicToolBar barRect = new GraphicToolBar();
	private GraphicToolBar barCircle = new GraphicToolBar();
	private GraphicToolBar barArrow = new GraphicToolBar();
	private GraphicToolBar barBrush = new GraphicToolBar();
	private MosaicToolBar barMosaic = new MosaicToolBar();
	private TextToolBar barText = new TextToolBar();
//	private SimpleDoubleProperty fontSizeProperty = new SimpleDoubleProperty();
	
}
