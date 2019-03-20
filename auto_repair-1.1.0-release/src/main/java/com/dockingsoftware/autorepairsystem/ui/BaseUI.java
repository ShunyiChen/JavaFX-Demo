/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.Constants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class BaseUI extends VBox {

     public BaseUI(String title) {
        this.title = title;
        HBox hboxDashboard = new HBox();
        Label lbltitle = new Label(title);
        hboxDashboard.setPadding(new Insets(25, 5, 25, 20));
        hboxDashboard.getChildren().add(lbltitle);
        lbltitle.setTextFill(Color.BLACK);  
//        lbltitle.setFont(Font.font(Constants.FONT_NAME, FontWeight.BOLD, Constants.TITLE_FONT_SIZE));
        hboxDashboard.alignmentProperty().setValue(Pos.CENTER_LEFT);
        
        this.getChildren().add(hboxDashboard);
        this.setStyle("-fx-background-color: rgb(250, 250, 250);");
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    private String title;
}
