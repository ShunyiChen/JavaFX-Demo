/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dockingsoftware.autorepairsystem.component;

import com.dockingsoftware.autorepairsystem.component.model.ProjectModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class CheckBoxCell extends TableCell<ProjectModel, Boolean> {

    private CheckBox checkbox;

    @Override
    protected void updateItem(Boolean arg0, boolean arg1) {
        super.updateItem(arg0, arg1);
        paintCell();
    }

    private void paintCell() {
        if (checkbox == null) {
            checkbox = new CheckBox();
            checkbox.selectedProperty().addListener(new ChangeListener<Boolean>() {

                @Override
                public void changed(ObservableValue<? extends Boolean> ov,
                        Boolean old_val, Boolean new_val) {
//                    setItem(new_val);
//                    ((ProjectModel) getTableView().getItems().get(getTableRow().getIndex())).setSelected(new_val);
                }
            });
        }
        checkbox.setSelected(getValue());
        setText(null);
        setGraphic(checkbox);
    }

    private Boolean getValue() {
        return getItem() == null ? false : getItem();
    }
}
