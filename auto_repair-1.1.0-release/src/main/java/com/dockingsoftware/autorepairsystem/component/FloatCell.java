/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dockingsoftware.autorepairsystem.component;

import com.dockingsoftware.autorepairsystem.component.model.ProjectModel;
import static com.dockingsoftware.autorepairsystem.util.PrintUtils.p;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class FloatCell extends TableCell<ProjectModel, Float> {

    private NumericTextField textField;

    public FloatCell() {
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
        // 取消设值取消功能
//         p("cancelEdit:"+getItem().toString());
//         setText(getValue()+"");
        // 启用设值取消功能
        setText(getString());
        setGraphic(null);
    }

    @Override
    public void updateItem(Float item, boolean empty) {
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
    
    /**
     * 
     * @return 
     */
    private Float getValue () {
        if (!textField.getText().isEmpty()) {
            return Float.parseFloat(textField.getText());
        } else {
            return null;
        }
    }
}
