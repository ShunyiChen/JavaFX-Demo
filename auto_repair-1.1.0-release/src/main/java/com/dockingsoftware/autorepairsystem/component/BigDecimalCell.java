/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dockingsoftware.autorepairsystem.component;

import com.dockingsoftware.autorepairsystem.component.model.ProjectModel;
import java.math.BigDecimal;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class BigDecimalCell extends TableCell<ProjectModel, BigDecimal> {

    private NumericTextField textField;

    public BigDecimalCell() {
        setAlignment(Pos.CENTER);
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createNumericTextField();
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

//        setText((String) getItem());
        String item = "";
        if (getItem() != null) {
            item = getItem().toString();
        }
        setText(item);
        setGraphic(null);
    }

    @Override
    public void updateItem(BigDecimal item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else if (isEditing()) {
            if (textField != null) {
                textField.setText(getString());
            }
            setText(null);
            setGraphic(textField);
        } else {
            setText(getString());
            setGraphic(null);
        }
    }

    private void createNumericTextField() {
        textField = new NumericTextField(getString());
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0,
                    Boolean arg1, Boolean arg2) {
                if (!arg2) {
                    
                    commitEdit(getValue());
                }
            }
        });
        textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    commitEdit(getValue());
                }
            }
        });
    }

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
    
    private BigDecimal getValue() {
        BigDecimal val = new BigDecimal(0.00);
        if (!textField.getText().isEmpty()) {
            val = new BigDecimal(textField.getText());
        }
        return val;
    }
}
