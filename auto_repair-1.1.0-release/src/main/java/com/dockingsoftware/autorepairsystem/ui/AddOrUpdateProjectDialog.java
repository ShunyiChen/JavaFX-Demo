/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.component.NumericTextField;
import com.dockingsoftware.autorepairsystem.persistence.entity.Project;
import com.dockingsoftware.autorepairsystem.persistence.entity.ProjectCategory;
import com.dockingsoftware.autorepairsystem.ui.controller.AddProjectDialogController;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class AddOrUpdateProjectDialog extends Dialog {

    public AddOrUpdateProjectDialog(Project project) {
        this.project = project;
        initComponents();
    }
    
    public AddOrUpdateProjectDialog() {
        initComponents();
    }
    
    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());
        
        setTitle("添加项目");
        setHeaderText("请添加新项目。");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        grid.add(new Label("项目名称:"), 0, 0);
        name.setPromptText("项目名称");
        grid.add(name, 1, 0);
       
        grid.add(new Label("单价:"), 2, 0);
        price.setPromptText("单价");
        grid.add(price, 3, 0);
        
        grid.add(new Label("工时:"), 0, 1);
        laborHour.setPromptText("工时");
        grid.add(laborHour, 1, 1);
        
        grid.add(new Label("项目分类:"),2, 1);
        List<ProjectCategory> lstProjectCategory = (List<ProjectCategory>) controller.list.call("");
        for (ProjectCategory pc : lstProjectCategory) {
            projectCategory.getItems().add(pc.getName());
        }
        projectCategory.getSelectionModel().select(0);
        projectCategory.setPrefWidth(153);
        grid.add(projectCategory, 3, 1);
        
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        okButton.setText("确定");
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("取消");
        // Do some validation (using the Java 8 lambda syntax).
        name.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });
        
        getDialogPane().setContent(grid);
        
        if (project != null) {
            name.setText(project.getName());
            price.setText(project.getPrice().toString());
            laborHour.setText(project.getLaborHour() + "");
            projectCategory.getSelectionModel().select(project.getProjectCategory());
            okButton.setDisable(false);
        } else {
            okButton.setDisable(true);
        }
    }
    
    public void returnProject(Consumer<Project> consumer) {
        if (project == null) {
            project = new Project();
        }
        project.setName(name.getText());
        project.setPrice(new BigDecimal(price.getText().isEmpty() ? "0" : price.getText()));
        project.setLaborHour(Float.parseFloat(laborHour.getText().isEmpty() ? "0" : laborHour.getText()));
        project.setProjectCategory(projectCategory.getValue());
        controller.saveOrUpdateProject.call(project);
        consumer.accept(project);
    }
    
    private TextField name = new TextField();
    private NumericTextField price = new NumericTextField();
    private NumericTextField laborHour = new NumericTextField();
    private ComboBox<String> projectCategory = new ComboBox<String>();
    private AddProjectDialogController controller = new AddProjectDialogController();
    
    // 构造参数
    private Project project;
}
