/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.component;

import com.dockingsoftware.autorepairsystem.Constants;
import com.dockingsoftware.autorepairsystem.component.model.ProjectModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class DatePickerCell extends TableCell<ProjectModel, LocalDate> {

    private DatePicker textField;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Constants.PATTERN);

    public DatePickerCell() {
        setAlignment(Pos.CENTER);
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createTextField();
            setText(null);
            setGraphic(textField);
//            textField.selectAll();
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

//        setText((String) getItem());
        setText(getString());
        setGraphic(null);
    }

    @Override
    public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else if (isEditing()) {
            if (textField != null) {
//                textField.setText(getString());
                textField.setValue(getItem());
            }
            setText(null);
            setGraphic(textField);
        } else {
            setText(getString());
            setGraphic(null);
        }
    }

    private void createTextField() {
//        textField = new TextField(getString());
        textField = new DatePicker(getItem());
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0,
                    Boolean arg1, Boolean arg2) {
                if (!arg2) {
//                    commitEdit(textField.getText());
                    commitEdit(textField.getValue());
                }
            }
        });
        textField.setOnKeyReleased(new EventHandler <KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    commitEdit(textField.getValue());
                }
            }
        });
    }

    private String getString() {
        return getItem() == null ? "" : dateFormatter.format(getItem());
    }
    
}