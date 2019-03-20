package com.maxtree.screenshot;

import java.awt.AWTException;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.maxtree.screenshot.utils.ColorUtils;
import com.maxtree.screenshot.utils.ImageUtils;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

/**
 * Snapshot 
 * @author Chen
 * 
 * 发送手机
 * 收藏
 * 共享空间
 * 文字国际化
 * 
 */
public class Screenshot {
	
	/**
	 * Constructor
	 * 
	 * @param windowRects
	 */
	public Screenshot() {
		initScreenParameters();
		toolbar = new EditToolbar(this);
		
		StackPane magnifierPane = new StackPane();
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(0, 0, 5, 0));
		vbox.setAlignment(Pos.BOTTOM_CENTER);
		
		sizeText.setFill(Color.WHITE);
		rgbText.setFill(Color.WHITE);
		
		sizeText.setFont(Font.font(12));
		rgbText.setFont(Font.font(12));
		
		vbox.getChildren().addAll(sizeText, rgbText);
		
		mainMenu = new RightClickMenu(this);
		
    	magnifierPane.getChildren().addAll(magnifierView, ImageUtils.createImageView("Magnifier.png"), vbox);
    	magnifier.getContent().add(magnifierPane);
		
		canvas = new Canvas(screenWidth, screenHeight);
		gc = canvas.getGraphicsContext2D();
		
		root = new Group(canvas);
		scene = new Scene(root);
		scene.getStylesheets().add("styles/Styles.css");
		primaryStage = new Stage(StageStyle.UNDECORATED);
		primaryStage.setAlwaysOnTop(true);
		primaryStage.setScene(scene);
		
		initActions();
	}
	
	/**
	 * Initialize the screen parameters
	 */
	private void initScreenParameters() {
		// Gets the union bounds of all the graphics configurations.
		Rectangle2D result = new Rectangle2D.Double();
		GraphicsEnvironment localGE = GraphicsEnvironment.getLocalGraphicsEnvironment();
		for (GraphicsDevice gd : localGE.getScreenDevices()) {
			for (GraphicsConfiguration graphicsConfiguration : gd.getConfigurations()) {
				Rectangle2D.union(result, graphicsConfiguration.getBounds(), result);
			}
		}
		screenWidth = (int) result.getWidth();
		screenHeight = (int) result.getHeight();
		
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	private void initActions() {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
		    public void handle(KeyEvent ke) {
		    	if (ke.getCode() == KeyCode.CONTROL) {
		    		isRGBShowing = true;
		    		updateMagnifierTexts();
		    		
		    	}
		    }
		});
		
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
		    public void handle(KeyEvent ke) {
		    	if (ke.getCode() == KeyCode.CONTROL) {
		    		isRGBShowing = false;
		    		updateMagnifierTexts();
		    	}
		    }
		});
		
		scene.setOnMousePressed(me -> {
			MouseButton button = me.getButton();
			if (button == MouseButton.PRIMARY) {
				if (STATUS == EditStatus.Started) {
					initX = me.getX();
					initY = me.getY();
				} else if (STATUS == EditStatus.Editing) {
					
					// For moving window
					initX = me.getX() - newR2D.getMinX();
					initY = me.getY() - newR2D.getMinY();
					
					// For dragging the border of the window
					upperLeft = new Point2D(newR2D.getMinX(), newR2D.getMinY());
					upperRight = new Point2D(newR2D.getMaxX(), newR2D.getMinY());
					lowerLeft = new Point2D(newR2D.getMinX(), newR2D.getMaxY());
					lowerRight = new Point2D(newR2D.getMaxX(), newR2D.getMaxY());
					
					// For drawing Rectangle,Circle,Arrow
					startX = me.getX();
					startY = me.getY();
					
					// Generate a mosaic image
					if (dtype == DrawingType.Mosaic) {
						if (mosaicImage == null) {
							generateMosaicImage(toolbar.getBar().getBlur());
						}
					}
				}
			}
		});
		
		// producer:
		scene.setOnMouseReleased(me -> {
			MouseButton button = me.getButton();
			if (button == MouseButton.PRIMARY) {
				
				if (STATUS == EditStatus.Started) {
					STATUS = EditStatus.Editing;
					hideMagnifier();
					
					// Display the edit toolbar.
					toolbar.display(primaryStage, newR2D);
					resizeWindow(newR2D);
					initCornerRects();
					
				} else if (STATUS == EditStatus.Editing) {
					if (me.getClickCount() == 2) {
						finish();
						return;
					}
					// For Dragging the border of the window.
					initCornerRects();
					if (!toolbar.isDisplayed()) {
						toolbar.hideAll();
					}
					
					// To avoid a repeat of items
					if (canvas.getCursor() != Cursor.CROSSHAIR) {
						return;
					}
					
					// Add brush item
					if (dtype == DrawingType.Rectangle) {
					}
					else if (dtype == DrawingType.Circle) {
					}
					else if (dtype == DrawingType.Arrow) {
					}
					else if (dtype == DrawingType.Brush) {
						myitems.add(new DrawingItem(
								new double[]{startX},
								new double[]{startY},
								DrawingType.Brush,
								new Object[]{path},
								toolbar.getBar().getSelectedColor(),
								toolbar.getBar().getLineWeight(),
								true));
						path = new ArrayList<Point2D>();
						oldPoint = null;
						
					} else if (dtype == DrawingType.Mosaic) {
						DrawingItem item = new DrawingItem(
								new double[]{startX},
								new double[]{startY},
								DrawingType.Mosaic,
								new Object[]{mosaicMap},
								toolbar.getBar().getSelectedColor(),
								toolbar.getBar().getLineWeight(),
								true);
						item.setBlur(toolbar.getBar().getBlur());
						myitems.add(item);
						mosaicMap = new HashMap<Point2D, MosaicImage>();
						oldPoint = null;
						
					} else if (dtype == DrawingType.Text) {
						if (!isEditingText()) {
							startEditing();
							
			    		} else {
			    			stopEditing();
			    			
			    		}
					}
					
					// Set the status to un-removable for the last item.
					if (myitems.size() > 0) {
						myitems.get(myitems.size() - 1).setErasable(false);
					}
				}
			} else if (button == MouseButton.SECONDARY) {
				if (STATUS == EditStatus.Editing) {
					if(newR2D.contains(me.getX(), me.getY())) {
						mainMenu.show2(canvas, me.getX(), me.getY());
						
					} else {
						if (myitems.size() > 0) {
							quit();
						} else {
							backToScreen();
						}
					}
					
				} else {
					undoTheLastChange();
				}
				
			}
			
		});
		
		scene.setOnMouseMoved(me -> {
			if (STATUS == EditStatus.Started) {
				selectWindow(me.getX(), me.getY());
				showMagnifier(me.getX(), me.getY());
			} else if (STATUS == EditStatus.Editing) {
				
				if (NW.contains(me.getX(), me.getY())) {
					canvas.setCursor(Cursor.NW_RESIZE);
				} else if (W.contains(me.getX(), me.getY())) {
					canvas.setCursor(Cursor.W_RESIZE);
				} else if (SW.contains(me.getX(), me.getY())) {
					canvas.setCursor(Cursor.SW_RESIZE);
				} else if (N.contains(me.getX(), me.getY())) {
					canvas.setCursor(Cursor.N_RESIZE);
				} else if (NE.contains(me.getX(), me.getY())) {
					canvas.setCursor(Cursor.NE_RESIZE);
				} else if (E.contains(me.getX(), me.getY())) {
					canvas.setCursor(Cursor.E_RESIZE);
				} else if (SE.contains(me.getX(), me.getY())) {
					canvas.setCursor(Cursor.SE_RESIZE);
				} else if (S.contains(me.getX(), me.getY())) {
					canvas.setCursor(Cursor.S_RESIZE);
				} else if (C.contains(me.getX(), me.getY())) {
					
					if (editable) {
						canvas.setCursor(Cursor.CROSSHAIR);
						
					} else {
						canvas.setCursor(Cursor.MOVE);
					}
				} else {
					canvas.setCursor(Cursor.DEFAULT);
				}
				 
			}
		});
		
		scene.setOnMouseDragged(me -> {

			MouseButton button = me.getButton();
			if (button == MouseButton.PRIMARY) {
				if (STATUS == EditStatus.Started) {
					double minX = initX < me.getX() ? initX : me.getX();
					double minY = initY < me.getY() ? initY : me.getY();
					double w = Math.abs(me.getX() - initX);
					double h = Math.abs(me.getY() - initY);
					double space = 0.7;
					javafx.geometry.Rectangle2D r = new javafx.geometry.Rectangle2D(minX + space, minY + space,
							w + space, h + space);

					resizeWindow(r);
					showMagnifier(me.getX(), me.getY());
				} else if (STATUS == EditStatus.Editing) {
					if (canvas.getCursor() == Cursor.MOVE) {
						moveWindow(me);
						
					} else if (canvas.getCursor() == Cursor.NW_RESIZE) {
						toolbar.display(primaryStage, newR2D);
						
						double x = me.getX() < upperRight.getX() ? me.getX() : upperRight.getX();
						double y = me.getY() < lowerRight.getY() ? me.getY() : lowerRight.getY();
						double w = Math.abs(upperRight.getX() - me.getX());
						double h = Math.abs(lowerRight.getY() -  me.getY());
						
//						// Checks if all drawing is included in the selected area.
//						if (!queue.isEmpty()) {
//							if (x > left.getRect()[0]) {
//								x = left.getRect()[0] - left.getLine_weight();
//								w = Math.abs(rightTop.getX() - x);
//							}
//							if (y > top.getRect()[1]) {
//								y = top.getRect()[1] - top.getLine_weight();
//								h = Math.abs(rightBottom.getY() - y);
//							}
//						}
						
						resizeWindow(new javafx.geometry.Rectangle2D(x, y, w, h));
						
					} else if (canvas.getCursor() == Cursor.W_RESIZE) {
						toolbar.display(primaryStage, newR2D);
						
						double x = me.getX() < upperRight.getX() ? me.getX() : upperRight.getX();
						double y = upperRight.getY();
						double w = Math.abs(upperRight.getX() - me.getX());
						double h = lowerRight.getY() - upperRight.getY();
						
//						// Checks if all drawing is included in the selected area.
//						if (!queue.isEmpty()) {
//							if (x > left.getRect()[0]) {
//								x = left.getRect()[0] - left.getLine_weight();
//								w = Math.abs(rightTop.getX() - x);
//							}
//						}
						
						resizeWindow(new javafx.geometry.Rectangle2D(x, y, w, h));
						
					} else if (canvas.getCursor() == Cursor.SW_RESIZE) {
						toolbar.display(primaryStage, newR2D);
						
						double x = me.getX() < upperRight.getX() ? me.getX() : upperRight.getX();
						double y = me.getY() < upperLeft.getY() ? me.getY() : upperLeft.getY();
						double w = Math.abs(upperRight.getX() - me.getX());
						double h = Math.abs(me.getY() - upperLeft.getY());
						
//						// Checks if all drawing is included in the selected area.
//						if (!queue.isEmpty()) {
//							if (x > left.getRect()[0]) {
//								x = left.getRect()[0] - left.getLine_weight();
//								w = Math.abs(rightTop.getX() - x);
//							}
//							if ((y + h) < (bottom.getRect()[1] + bottom.getRect()[3])) {
//								y = leftTop.getY();
//								h = bottom.getRect()[1] + bottom.getRect()[3] + bottom.getLine_weight() - y;
//							}
//						}
						
						resizeWindow(new javafx.geometry.Rectangle2D(x, y, w, h));
						
					} else if (canvas.getCursor() == Cursor.N_RESIZE) {
						toolbar.display(primaryStage, newR2D);
						double x = upperLeft.getX();
						double y = me.getY() < lowerRight.getY() ? me.getY() : lowerRight.getY();
						double w = lowerRight.getX() - lowerLeft.getX();
						double h = Math.abs(lowerRight.getY() - me.getY());
//						// Checks if all drawing is included in the selected area.
//						if (!queue.isEmpty()) {
//							if (y > top.getRect()[1]) {
//								y = top.getRect()[1] - top.getLine_weight();
//								h = Math.abs(rightBottom.getY() - y);
//							}
//						}
						
						resizeWindow(new javafx.geometry.Rectangle2D(x, y, w, h));
						
					} else if (canvas.getCursor() == Cursor.NE_RESIZE) {
						toolbar.display(primaryStage, newR2D);
						double x = me.getX() < lowerLeft.getX() ? me.getX() : lowerLeft.getX();
						double y = me.getY() < lowerLeft.getY() ? me.getY() : lowerLeft.getY();
						double w = Math.abs(lowerLeft.getX() - me.getX());
						double h = Math.abs(lowerLeft.getY() - me.getY());
//						// Checks if all drawing is included in the selected area.
//						if (!queue.isEmpty()) {
//							if (y > top.getRect()[1]) {
//								y = top.getRect()[1] - top.getLine_weight();
//								h = Math.abs(rightBottom.getY() - y);
//							}
//							if ((x + w) < (right.getRect()[0] + right.getRect()[2])) {
//								x = leftBottom.getX();
//								w = right.getRect()[0] + right.getRect()[2] + right.getLine_weight() - x;
//							}
//						}
						resizeWindow(new javafx.geometry.Rectangle2D(x, y, w, h));
						
					} else if (canvas.getCursor() == Cursor.E_RESIZE) {
						toolbar.display(primaryStage, newR2D);
						double x = me.getX() < upperLeft.getX() ? me.getX() : upperLeft.getX();
						double y = upperRight.getY();
						double w = Math.abs(upperLeft.getX() - me.getX());
						double h = lowerRight.getY() - upperRight.getY();
//						// Checks if all drawing is included in the selected area.
//						if (!queue.isEmpty()) {
//							if ((x + w) < (right.getRect()[0] + right.getRect()[2])) {
//								x = leftBottom.getX();
//								w = right.getRect()[0] + right.getRect()[2] + right.getLine_weight() - x;
//							}
//						}
						
						resizeWindow(new javafx.geometry.Rectangle2D(x, y, w, h));
						
					} else if (canvas.getCursor() == Cursor.SE_RESIZE) {
						toolbar.display(primaryStage, newR2D);
						double x = me.getX() < upperLeft.getX() ? me.getX() : upperLeft.getX();
						double y = me.getY() < upperLeft.getY() ? me.getY() : upperLeft.getY();
						double w = Math.abs(upperLeft.getX() - me.getX());
						double h = Math.abs(upperLeft.getY() - me.getY());
//						// Checks if all drawing is included in the selected area.
//						if (!queue.isEmpty()) {
//							if ((x + w) < (right.getRect()[0] + right.getRect()[2])) {
//								x = leftBottom.getX();
//								w = right.getRect()[0] + right.getRect()[2] + right.getLine_weight() - x;
//							}
//							if ((y + h) < (bottom.getRect()[1] + bottom.getRect()[3])) {
//								y = leftTop.getY();
//								h = bottom.getRect()[1] + bottom.getRect()[3] + bottom.getLine_weight() - y;
//							}
//						}
						
						resizeWindow(new javafx.geometry.Rectangle2D(x, y, w, h));
						
					} else if (canvas.getCursor() == Cursor.S_RESIZE) {
						toolbar.display(primaryStage, newR2D);
						double x = upperLeft.getX();
						double y = me.getY() < upperRight.getY() ? me.getY() : upperRight.getY();
						double w = lowerRight.getX() - lowerLeft.getX();
						double h = Math.abs(upperRight.getY() - me.getY());
//						// Checks if all drawing is included in the selected area.
//						if (!queue.isEmpty()) {
//							if ((y + h) < (bottom.getRect()[1] + bottom.getRect()[3])) {
//								y = leftTop.getY();
//								h = bottom.getRect()[1] + bottom.getRect()[3] + bottom.getLine_weight() - y;
//							}
//						}
						resizeWindow(new javafx.geometry.Rectangle2D(x, y, w, h));
						
					}
					else if (canvas.getCursor() == Cursor.CROSSHAIR) {
						if (dtype == DrawingType.Rectangle
								|| dtype == DrawingType.Circle) {
							updateItemForRectAndOval(me.getX(), me.getY(), dtype);
							drawAll();
							
						} 
						else if (dtype == DrawingType.Arrow) {
							updateItemForArrow(me.getX(), me.getY());
							drawAll();
							
						} else if (dtype == DrawingType.Brush) {
							Point2D newPoint = new Point2D(me.getX(), me.getY());
							Callback<double[], String> cb = new Callback<double[], String>() {
								@Override
								public String call(double[] param) {
									// Check if the drawing beyond the boundary of selected area.
									if (param[0] < newR2D.getMinX()) {
										param[0] = newR2D.getMinX();
									}
									if (param[1] < newR2D.getMinY()) {
										param[1] = newR2D.getMinY();
									}
									if (param[0] > newR2D.getMaxX() - toolbar.getBar().getLineWeight()) {
										param[0] = newR2D.getMaxX() - toolbar.getBar().getLineWeight();
									}
									if (param[1] > newR2D.getMaxY() - toolbar.getBar().getLineWeight()) {
										param[1] = newR2D.getMaxY() - toolbar.getBar().getLineWeight();
									}
									gc.setFill(toolbar.getBar().getSelectedColor());
									gc.fillOval(param[0], param[1], toolbar.getBar().getLineWeight(), toolbar.getBar().getLineWeight());
									path.add(new Point2D(param[0], param[1]));
									return null;
								}
							};
							if (oldPoint == null) {
								bresenhamLine(me.getX(), me.getY(), me.getX(), me.getY(), cb);
							} else {
								bresenhamLine(oldPoint.getX(), oldPoint.getY(), newPoint.getX(), newPoint.getY(), cb);
							}
							oldPoint = newPoint;
							
						} else if (dtype == DrawingType.Mosaic) {
							Point2D newPoint = new Point2D(me.getX(), me.getY());
							Callback<double[], String> cb = new Callback<double[], String>() {
								@Override
								public String call(double[] param) {
									// Check if the drawing beyond the boundary of selected area.
									// Line weight
									int box = (int)toolbar.getBar().getLineWeight() * 5;
									// x and y are relative coordinate
									int x = (int)(param[0] - newR2D.getMinX());
									int y = (int)(param[1] - newR2D.getMinY());
									int w = box;
									int h = box;
									// param[0] and param[1] are absolute coordinates
									if (param[0] < newR2D.getMinX() +1) {
										param[0] = newR2D.getMinX() +1;
										x = 0;
									}
									if (param[1] < newR2D.getMinY() +1) {
										param[1] = newR2D.getMinY() +1;
										y = 0;
									}
									if (newR2D.getMaxX() - param[0] < box) {
										w = (int) (newR2D.getMaxX() - param[0]);
									}
									if (newR2D.getMaxY() - param[1] < box) {
										h = (int) (newR2D.getMaxY() - param[1]);
									}
									if (w <= 0 || h <= 0) {
										return "";
									}
									WritableImage newImage = new WritableImage(preader, x, y, w, h);
									gc.drawImage(newImage, param[0] - 1, param[1] - 1);
									Point2D p = new Point2D(param[0] - 1, param[1] - 1);
									mosaicMap.put(p, new MosaicImage(x, y, w, h, newImage));
									return null;
								}
							};
							if (oldPoint == null) {
								bresenhamLine(me.getX(), me.getY(), me.getX(), me.getY(), cb);
							} else {
								bresenhamLine(oldPoint.getX(), oldPoint.getY(), newPoint.getX(), newPoint.getY(), cb);
							}
							oldPoint = newPoint;
							
						}
						
					} 
				}
			}
		});
	}
	
	private void initCornerRects() {
		double spacing = 7;
		double x = newR2D.getMinX();
		double y = newR2D.getMinY();
		double w = newR2D.getWidth();
		double h = newR2D.getHeight();
		// Validate dynamic width and height
		double dw = w - 2 * spacing;
		double dh = h - 2 * spacing;
		if (dw < 0) {
			dw = 0;
		}
		if (dh < 0) {
			dh = 0;
		}
		C = new javafx.geometry.Rectangle2D(x + spacing, y + spacing, dw, dh);
		NW = new javafx.geometry.Rectangle2D(x, y, spacing, spacing);
		W = new javafx.geometry.Rectangle2D(x, y + spacing, spacing, dh);
		SW = new javafx.geometry.Rectangle2D(x, y + h - spacing, spacing, spacing);
		N = new javafx.geometry.Rectangle2D(x + spacing, y, dw, spacing);
		NE = new javafx.geometry.Rectangle2D(x + w - spacing, y, spacing, spacing);
		E = new javafx.geometry.Rectangle2D(x + w - spacing, y + spacing, spacing, dh);
		SE = new javafx.geometry.Rectangle2D(x + w - spacing, y + h - spacing, spacing, spacing);
		S = new javafx.geometry.Rectangle2D(x + spacing, y + h - spacing, dw, spacing);
	}
	
	/**
	 * Select window
	 * 
	 * @param x
	 * @param y
	 */
	private void selectWindow(double x, double y) {
		for (javafx.geometry.Rectangle2D r : windowRects) {
			if (r.contains(x, y)) {
				newR2D = r;
				updateItemsForSelectWindow(r);
				break;
			}
		}
		if (oldR2D != newR2D) {
			drawAll();
			oldR2D = newR2D;
		}
	}
	
	/**
	 * Resize window.
	 * 
	 * @param r
	 */
	private void resizeWindow(javafx.geometry.Rectangle2D r) {
		newR2D = r;
		updateItemsForResizeWindow(r);
		drawAll();
		
	}

	/**
	 * 
	 * @param me
	 */
	private void moveWindow(MouseEvent me) {
		double x = (me.getX() - initX) < 0 ? 0 : me.getX() - initX;
		double y = (me.getY() - initY) < 0 ? 0 : me.getY() - initY;
		
		if (x + newR2D.getWidth() > screenWidth) {
			x = screenWidth - newR2D.getWidth();
		}
		if (y + newR2D.getHeight() > screenHeight) {
			y = screenHeight - newR2D.getHeight();
		}
		javafx.geometry.Rectangle2D r = new javafx.geometry.Rectangle2D(x, y, newR2D.getWidth(), newR2D.getHeight());
		resizeWindow(r);
		toolbar.display(primaryStage, newR2D);
		
	}
	
	private void updateItemsForSelectWindow(javafx.geometry.Rectangle2D r) {
		for (int i = items.size() - 1; i > 0; i--) {
			items.remove(i);
		}
		// Add shadow items
		Object[] contents = new Object[]{
			new javafx.geometry.Rectangle2D(
					0,
					0,
					r.getMinX(),
					screenHeight), // fill west area
			new javafx.geometry.Rectangle2D(
					r.getMaxX(),
					0,
					(screenWidth - r.getMaxX()) < 0 ? 0 : screenWidth - r.getMaxX(),
					screenHeight), // fill east area
			new javafx.geometry.Rectangle2D(
					r.getMinX(),
					0,
					r.getWidth(),
					r.getMinY() < 0 ? 0 : r.getMinY()), // fill north area
			new javafx.geometry.Rectangle2D(
					r.getMinX(),
					r.getMaxY(),
					r.getWidth(),
					(screenHeight - r.getMaxY()) < 0 ? 0 : screenHeight - r.getMaxY()
					) // fill south area
		};
		items.add(new DrawingItem(new double[]{r.getMinX()}, new double[]{r.getMinY()}, DrawingType.Shadow, contents, shadowColor, 1.0d, false));
		// Draw border
		items.add(new DrawingItem(new double[]{r.getMinX()}, new double[]{r.getMinY()}, DrawingType.BorderWithoutVertex, new Object[]{r}, themeColor, 5.0d, false));
	
	}
	
	private void updateItemsForResizeWindow(javafx.geometry.Rectangle2D r) {
		for (int i = items.size() - 1; i > 0; i--) {
			items.remove(i);
		}
		// Add shadow items
		Object[] contents = new Object[]{
			new javafx.geometry.Rectangle2D(
					0,
					0,
					(r.getMinX() + 0.11) < 0 ? 0 : r.getMinX() + 0.11,
					screenHeight), // fill west area
			new javafx.geometry.Rectangle2D(
					r.getMaxX(),
					0,
					(screenWidth - r.getMaxX()) < 0 ? 0 : screenWidth - r.getMaxX(),
					screenHeight), // fill east area
			new javafx.geometry.Rectangle2D(
					r.getMinX(),
					0,
					r.getWidth() + 0.17,
					r.getMinY() < 0 ? 0 : r.getMinY()), // fill north area
			new javafx.geometry.Rectangle2D(
					r.getMinX(),
					r.getMaxY(),
					r.getWidth() + 0.17,
					(screenHeight - r.getMaxY()) < 0 ? 0 : screenHeight - r.getMaxY()
					) // fill south area
		};
		// Draw shadow
		items.add(new DrawingItem(new double[]{r.getMinX()}, new double[]{r.getMinY()}, DrawingType.Shadow, contents, shadowColor, 1.0d, false));
		// Draw border
		items.add(new DrawingItem(new double[]{r.getMinX()}, new double[]{r.getMinY()}, DrawingType.Border, new Object[]{r}, themeColor, 1.0d, false));
		// Draw size info
		items.add(new DrawingItem(new double[]{r.getMinX()}, new double[]{r.getMinY()}, DrawingType.SizeInfo, new Object[]{r}, themeColor, 1.0d, false));
	
	}

	private void updateItemForRectAndOval(double x, double y, DrawingType t) {
		for (int i = myitems.size() - 1; i >= 0; i--) {
			if (myitems.get(i).isErasable()) {
				myitems.remove(i);
			}
		}
		// Check if the drawing beyond the boundary of selected area.
		double min_x = x < startX ? x : startX;
		double min_y = y < startY ? y : startY;
		double max_x = x > startX ? x : startX;
		double max_y = y > startY ? y : startY;
		if (min_x < newR2D.getMinX()) {
			min_x = newR2D.getMinX();
		}
		if (min_y < newR2D.getMinY()) {
			min_y = newR2D.getMinY();
		}
		if (max_x > newR2D.getMaxX()) {
			max_x = newR2D.getMaxX() - toolbar.getBar().getLineWeight();
		}
		if (max_y > newR2D.getMaxY()) {
			max_y = newR2D.getMaxY() - toolbar.getBar().getLineWeight();
		}
		double _w = max_x - min_x;
		double _h = max_y - min_y;
		javafx.geometry.Rectangle2D rectangle = new javafx.geometry.Rectangle2D(min_x, min_y, _w, _h);
		myitems.add(new DrawingItem(new double[]{min_x}, new double[]{min_y}, t, new Object[]{rectangle}, toolbar.getBar().getSelectedColor(), toolbar.getBar().getLineWeight(), true));
	}
	
	private void updateItemForArrow(double x, double y) {
		for (int i = myitems.size() - 1; i >= 0; i--) {
			if (myitems.get(i).isErasable()) {
				myitems.remove(i);
			}
		}
		// Check if the drawing beyond the boundary of selected area.
		if (x < newR2D.getMinX()) {
			x = newR2D.getMinX();
		}
		if (y < newR2D.getMinY()) {
			y = newR2D.getMinY();
		}
		if (x > newR2D.getMaxX()) {
			x = newR2D.getMaxX();
		}
		if (y > newR2D.getMaxY()) {
			y = newR2D.getMaxY();
		}
		// Initial point of 
		Point2D startPoint = new Point2D(startX, startY);
		Point2D endPoint = new Point2D(x, y);
		// width of side triangle
		double a;
		// height of side triangle
		double b;
		if (toolbar.getBar().getLineWeight() == 1d) {
			a = 6;
			b = 16;
		} else if (toolbar.getBar().getLineWeight() == 3d) {
			a = 10;
			b = 20;
		} else {
			a = 14;
			b = 24;
		}
		// Compute cross middle line point.
		int i = 2;
		Point2D middlePoint;
		do {
			middlePoint = new Point2D(
					endPoint.getX() + (startPoint.getX() - endPoint.getX()) / i,
					endPoint.getY() + (startPoint.getY() - endPoint.getY()) / i);

			i++;
			
		} while (endPoint.distance(middlePoint) > b);
		
		double n = a * Math.sin(Math.atan((startY - middlePoint.getY()) / (startX - middlePoint.getX())));
		double m = a * Math.cos(Math.atan((startY - middlePoint.getY()) / (startX - middlePoint.getX())));
		
		double v1_x = middlePoint.getX() - n;
		double v1_y = middlePoint.getY() + m;
		
		double v2_x = middlePoint.getX() + n;
		double v2_y = middlePoint.getY() - m;
		
		double v3_x = middlePoint.getX() - n / 2;
		double v3_y = middlePoint.getY() + m / 2;
		
		double v4_x = middlePoint.getX() + n / 2;
		double v4_y = middlePoint.getY() - m / 2;
		double[] xPoints = {startPoint.getX(), v3_x, v1_x, endPoint.getX(), v2_x, v4_x};
		double[] yPoints = {startPoint.getY(), v3_y, v1_y, endPoint.getY(), v2_y, v4_y};
		myitems.add(new DrawingItem(new double[]{startX}, new double[]{startY}, DrawingType.Arrow, new Object[]{xPoints, yPoints}, toolbar.getBar().getSelectedColor(), toolbar.getBar().getLineWeight(), true));
	
	}
	
	private void updateMagnifierTexts() {
		// Update size text
		sizeText.setText((int)newR2D.getWidth()+" x "+(int)newR2D.getHeight());
		// Update rgb text
		Point location = MouseInfo.getPointerInfo().getLocation();
		BufferedImage pixelImage = bimg.getSubimage(location.x, location.y, 1, 1);
		java.awt.Color[][] pixels = ImageUtils.loadPixelsFromImage(pixelImage);
		java.awt.Color c = pixels[0][0];
		if (!isRGBShowing) {
			rgbText.setText("RGB:("+c.getRed()+","+c.getGreen()+","+c.getBlue()+")");
		} else {
			rgbText.setText("RGB:"+ColorUtils.toHexEncoding(c));
		}
		
	}
	
	// https://de.wikipedia.org/wiki/Bresenham-Algorithmus
	private void bresenhamLine(double x0, double y0, double x1, double y1, Callback<double[], String> cb) {
		double dx = Math.abs(x1 - x0), sx = x0 < x1 ? 1. : -1.;
		double dy = -Math.abs(y1 - y0), sy = y0 < y1 ? 1. : -1.;
		double err = dx + dy, e2; /* error value e_xy */
		while (true) {
			
			cb.call(new double[]{x0, y0});
			
			if (x0 == x1 && y0 == y1)
				break;
			e2 = 2. * err;
			if (e2 > dy) {
				err += dy;
				x0 += sx;
			} /* e_xy+e_x > 0 */
			if (e2 < dx) {
				err += dx;
				y0 += sy;
			} /* e_xy+e_y < 0 */
		}
	}
	
	public void generateMosaicImage(double radius) {
		if (bimg != null) {
			// init mosaic image
			GaussianBlur gaussianBlur = new GaussianBlur();
			gaussianBlur.setRadius(radius);
			ImageView mosaicImgView = new ImageView(wimg);
			mosaicImgView.setEffect(gaussianBlur);
			SnapshotParameters params = new SnapshotParameters();
			params.setViewport(newR2D);
			mosaicImage = mosaicImgView.snapshot(params, null);
			preader = mosaicImage.getPixelReader();
		}
		
	}
	
	public void updateMosaicItems() {
		boolean updated = false;
		for (int i = 0; i < myitems.size(); i++) {
			if (myitems.get(i).getType() == DrawingType.Mosaic) {
				Map<Point2D, MosaicImage> map = (Map<Point2D, MosaicImage>) myitems.get(i).getContents()[0];
				Iterator<Point2D> iter = map.keySet().iterator();
				while (iter.hasNext()) {
					Point2D p = iter.next();
					MosaicImage mi = map.get(p);
					WritableImage newImage = new WritableImage(preader, mi.getX(), mi.getY(), mi.getW(), mi.getH());
					mi.setImage(newImage);
				}
				updated = true;
			}
		}
		if (updated) {
			drawAll();
		}
		
	}
	
	/**
	 * Draw all
	 */
	public void drawAll() {
		drawAll(false);
	}
	
	/**
	 * Draw all
	 * 
	 * @param b 
	 */
	private void drawAll(boolean b) {		
		for (int i = 0; i < items.size(); i++) {
			if (b) {
				if (items.get(i).getType() == DrawingType.Border
						|| items.get(i).getType() == DrawingType.SizeInfo
						|| items.get(i).getType() == DrawingType.Shadow) {
					continue;
				}
			}
			draw(items.get(i));
		}
		for (int i = 0; i < myitems.size(); i++) {
			draw(myitems.get(i));
		}
		
	}
	
	/**
	 * Consumers:
	 * 
	 * Draw single graph
	 * 
	 * @param item
	 */
	private void draw(DrawingItem item) {
		if (item.getType() == DrawingType.Image) {
			gc.drawImage((Image) item.getContents()[0], item.getX()[0], item.getY()[0]);
		} else if (item.getType() == DrawingType.Border) {
			gc.setFill(item.getColor());
			gc.setStroke(item.getColor());
			gc.setLineWidth(item.getLineWeight());
			javafx.geometry.Rectangle2D r = (javafx.geometry.Rectangle2D) item.getContents()[0];
			gc.strokeRect(r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
			// Paint vertexes
			gc.fillRect(r.getMinX() - 2.5, r.getMinY() - 2.5, 5, 5);
			gc.fillRect(r.getMinX() - 2.5, r.getMinY() + r.getHeight() / 2 - 2.5, 5, 5);
			gc.fillRect(r.getMinX() - 2.5, r.getMinY() + r.getHeight() - 2.5, 5, 5);
			
			gc.fillRect(r.getMinX() + r.getWidth() / 2 - 2.5, r.getMinY() - 2.5, 5, 5);
			gc.fillRect(r.getMinX() + r.getWidth() / 2 - 2.5, r.getMinY() + r.getHeight() - 2.5, 5, 5);
			
			gc.fillRect(r.getMinX() + r.getWidth() - 2.5, r.getMinY() - 2.5, 5, 5);
			gc.fillRect(r.getMinX() + r.getWidth() - 2.5, r.getMinY() + r.getHeight() / 2 - 2.5, 5, 5);
			gc.fillRect(r.getMinX() + r.getWidth() - 2.5, r.getMinY() + r.getHeight() - 2.5, 5, 5);
			
		} 
		else if (item.getType() == DrawingType.BorderWithoutVertex) {
			gc.setFill(item.getColor());
			gc.setStroke(item.getColor());
			gc.setLineWidth(item.getLineWeight());
			javafx.geometry.Rectangle2D r = (javafx.geometry.Rectangle2D) item.getContents()[0];
			gc.strokeRect(r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
			
		}
		else if (item.getType() == DrawingType.Shadow) {
			gc.setFill(item.getColor());
			for (int i = 0; i < item.getContents().length; i++) {
				javafx.geometry.Rectangle2D r = (javafx.geometry.Rectangle2D) item.getContents()[i];
				gc.fillRect(r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
			}
			
		}
		else if (item.getType() == DrawingType.SizeInfo){
			// Paint size information
			gc.setTextAlign(TextAlignment.CENTER);
			javafx.geometry.Rectangle2D r = (javafx.geometry.Rectangle2D) item.getContents()[0];
			gc.setFont(Font.font(12));
			if (r.getMinY() <= 20) {
				gc.setFill(shadowColor);
				gc.fillRect(r.getMinX(), r.getMinY() - 1, 72, 20);
				gc.setFill(Color.WHITE);
				gc.fillText((int)r.getWidth()+" x "+(int)r.getHeight(), r.getMinX() + 36, r.getMinY() + 14);
			}
			else {
				gc.setFill(shadowColor);
				gc.fillRect(r.getMinX(), r.getMinY() - 21, 72, 20);
				gc.setFill(Color.WHITE);
				gc.fillText((int)r.getWidth()+" x "+(int)r.getHeight(), r.getMinX() + 36, r.getMinY() - 6);
			}
			
		} else if (item.getType() == DrawingType.Rectangle) {
			gc.setStroke(item.getColor());
			gc.setLineWidth(item.getLineWeight());
			javafx.geometry.Rectangle2D r = (javafx.geometry.Rectangle2D) item.getContents()[0];
			gc.strokeRect(r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
			gc.strokeRect(r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
			
		} else if (item.getType() == DrawingType.Circle) {
			gc.setStroke(item.getColor());
			gc.setLineWidth(item.getLineWeight());
			javafx.geometry.Rectangle2D r = (javafx.geometry.Rectangle2D) item.getContents()[0];
			gc.strokeOval(r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
			gc.strokeOval(r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
			
		} else if (item.getType() == DrawingType.Arrow) {
			gc.setFill(item.getColor());
			double[] xPoints = (double[]) item.getContents()[0];
			double[] yPoints = (double[]) item.getContents()[1];
			gc.fillPolygon(xPoints, yPoints, xPoints.length);
			
		} else if (item.getType() == DrawingType.Brush) {
			List<Point2D> ps = (List<Point2D>) item.getContents()[0];
			for (Point2D p : ps) {
				gc.setFill(item.getColor());
				gc.fillOval(p.getX(), p.getY(), item.getLineWeight(), item.getLineWeight());
			}
			
		} else if (item.getType() == DrawingType.Mosaic) {
			Map<Point2D, MosaicImage> map = (Map<Point2D, MosaicImage>) item.getContents()[0];
			Iterator<Point2D> iter = map.keySet().iterator();
			while (iter.hasNext()) {
				Point2D p = iter.next();
				gc.drawImage(map.get(p).getImage(), p.getX(), p.getY());
			}
			
		} else if (item.getType() == DrawingType.Text) {
			TextLabel tl = (TextLabel) item.getContents()[0];
			if(!item.isIgnored()) {
				gc.setTextAlign(TextAlignment.LEFT);
				gc.setFill(item.getColor());
				gc.setFont(Font.font(item.getFontSize()));
				gc.fillText(tl.getTextArea().getText(), item.getX()[0], item.getY()[0]);
				gc.setTextAlign(TextAlignment.CENTER);
			}
			
		}
	}
	
	/**
	 * Show magnifier
	 * 
	 * @param x1
	 * @param y1
	 */
	private void showMagnifier(double x1, double y1) {
		int x = (int) (x1 - 29);
		int y = (int) (y1 - 21);
		if (x < 0) {
			x = 0;
		}
		if (y < 0) {
			y = 0;
		}
		if ((x + 60) > screenWidth) {
			x = screenWidth - 60;
		}
		if ((y + 60) > screenHeight) {
			y = screenHeight - 60;
		}
		BufferedImage bi = bimg.getSubimage(x, y, 60, 60);
		magnifierView.setImage(SwingFXUtils.toFXImage(ImageUtils.zoomInImage(bi, 2), null));
		magnifier.show(canvas, x1, y1 + 20);
		updateMagnifierTexts();
		
	}
	
	/**
	 * Hide magnifier
	 */
	private void hideMagnifier() {
		magnifier.hide();
	}
	
	/**
	 * Undo the last change
	 */
	public void undoTheLastChange() {
		if (STATUS == EditStatus.Started) {
			quit();
			
		} else if (STATUS == EditStatus.Editing) {
			if (myitems.size() > 0) {
				myitems.remove(myitems.size() - 1);
				drawAll();
				
			} else {
				backToScreen();
				
			}
			
		}
	}
	
	public void finish() {
		drawAll(true);
		SnapshotParameters params = new SnapshotParameters();
		params.setViewport(newR2D);
		WritableImage image = canvas.snapshot(params, null);
		// Gets the current system clipboard.
		Clipboard clipboard = Clipboard.getSystemClipboard();
		clipboard.clear();
		ClipboardContent content = new ClipboardContent();
	    content.putImage(image);
	    clipboard.setContent(content);
		quit();
		
	}
	
	public void quit() {
		newR2D = null;
		oldR2D = null;
    	items.clear();
    	myitems.clear();
		hideMagnifier();
		toolbar.hideAll();
		primaryStage.close();
		STATUS = EditStatus.Ended;
		mosaicImage = null;
		editable = false;
		removeAllTextLabels();
		System.gc();
		
	}
	
	private void removeAllTextLabels() {
		Iterator<Node> iter = root.getChildren().iterator();
		while (iter.hasNext()) {
			Node n = iter.next();
			if (n instanceof TextLabel) {
				iter.remove();
			}
		}
		
	}
	
	public void backToScreen() {
		editable = false;
		canvas.setCursor(Cursor.DEFAULT);
		oldR2D = null;
		Screenshot.STATUS = EditStatus.Started;
		Point location = MouseInfo.getPointerInfo().getLocation();
		selectWindow(location.x, location.y);
		showMagnifier(location.x, location.y);
		toolbar.hideAll();
	}
	
	public void save() {
		if (fileChooser == null) {
			fileChooser = new FileChooser();
		}
		fileChooser.setTitle("Save Image");
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddhhmmss");
        fileChooser.setInitialFileName("MaxTree截图"+f.format(new Date())+".png");
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            try {
            	drawAll(true);
            	SnapshotParameters params = new SnapshotParameters();
        		params.setViewport(newR2D);
        		WritableImage image = canvas.snapshot(params, null);
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } finally {
            	quit();
            	
            }
        }
	}
	
	public void send() {
		// TODO
	}
	
	public void collect() {
		// TODO
	}
	
	public void share() {
		// TODO
	}
	
	private boolean isEditingText(){
		for (Node n : root.getChildren()) {
			if (n instanceof TextLabel) {
				TextLabel tl = (TextLabel) n;
				if (tl.getTextArea().isVisible()) {
					return true;
				}
				
			}
		}
		return false;
	}
	
	private void startEditing() {
		currentTextLabel = new TextLabel(gc, root, Screenshot.this);
		currentTextLabel.updatesStyle(new TextStyle(toolbar.getBar().getFontSize(), toolbar.getBar().getSelectedColor()));
		currentTextLabel.startEditing(startX, startY);
	}
	
	private void stopEditing() {
		if (currentTextLabel != null) {
			currentTextLabel.updatesStyle(currentTextLabel.getTextStyle());
			currentTextLabel.stopEditing();
			if (currentTextLabel.getItem().getContents() == null) {
				myitems.remove(currentTextLabel.getItem());
			} else {
				currentTextLabel.getItem().setErasable(false);
				myitems.add(currentTextLabel.getItem());
			}
			currentTextLabel = null;
		}
	}
	
	public void snapshot(List<javafx.geometry.Rectangle2D> windowRects) {
		this.windowRects = windowRects;
		Rectangle bounds = new Rectangle(0, 0, screenWidth, screenHeight);
    	bimg = robot.createScreenCapture(bounds);
    	wimg = SwingFXUtils.toFXImage(bimg, null);
    	items.add(new DrawingItem(new double[]{0}, new double[]{0}, DrawingType.Image, new Object[]{wimg}, null, 0, false));
    	primaryStage.setX(0);
		primaryStage.setY(0);
		primaryStage.show();
		backToScreen();
    }
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public double getScreenHeight() {
		return screenHeight;
	}
	
	public EditToolbar getEditToolbar() {
		return toolbar;
	}
	
	public TextLabel getCurrentTextLabel() {
		return currentTextLabel;
	}
	
	public void setCurrentTextLabel(TextLabel tl) {
		this.currentTextLabel = tl;
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public void setDrawingType(DrawingType dtype) {
		this.dtype = dtype;
		stopEditing();
	}
	
	public DrawingType getDrawingType() {
		return this.dtype;
	}
	
	private Robot robot;
	private int screenWidth;
	private int screenHeight;
    private double initX;
    private double initY;
	private boolean isRGBShowing;
	private Group root;
	private Stage primaryStage;
	private Scene scene;
	private Canvas canvas;
	private GraphicsContext gc;
	private List<javafx.geometry.Rectangle2D> windowRects;
	private Text sizeText = new Text();
	private Text rgbText = new Text();
	private Color themeColor = Color.rgb(0, 174, 255);
    private Color shadowColor = Color.rgb(0, 0, 0, 0.5d);
    private Point2D upperLeft;
    private Point2D upperRight;
    private Point2D lowerLeft;
    private Point2D lowerRight;
    private javafx.geometry.Rectangle2D C;
    private javafx.geometry.Rectangle2D NW;
    private javafx.geometry.Rectangle2D W;
    private javafx.geometry.Rectangle2D SW;
    private javafx.geometry.Rectangle2D N;
    private javafx.geometry.Rectangle2D NE;
    private javafx.geometry.Rectangle2D E;
    private javafx.geometry.Rectangle2D SE;
    private javafx.geometry.Rectangle2D S;
    private double startX;
    private double startY;
    private Point2D oldPoint;
    private List<Point2D> path = new ArrayList<Point2D>();
    private WritableImage mosaicImage;
    private PixelReader preader;
    private Map<Point2D, MosaicImage> mosaicMap = new HashMap<Point2D, MosaicImage>();
    private RightClickMenu mainMenu;
	public static EditStatus STATUS = EditStatus.Ended;
	private List<DrawingItem> items = new ArrayList<DrawingItem>();  // For drawing selected window.
	private List<DrawingItem> myitems = new ArrayList<DrawingItem>();  // For drawing any graphics.
	private javafx.geometry.Rectangle2D newR2D;  // The selected area
	private javafx.geometry.Rectangle2D oldR2D;
	private BufferedImage bimg;
	private WritableImage wimg;
	private ImageView magnifierView = new ImageView();
	private Popup magnifier = new Popup();
	private EditToolbar toolbar;
	private DrawingType dtype;
	private boolean editable;
	private FileChooser fileChooser;
	private TextLabel currentTextLabel;
}
