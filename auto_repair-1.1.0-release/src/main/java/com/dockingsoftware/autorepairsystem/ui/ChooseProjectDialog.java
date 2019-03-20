/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.component.BigDecimalCell;
import com.dockingsoftware.autorepairsystem.component.EditingCell;
import com.dockingsoftware.autorepairsystem.component.FloatCell;
import com.dockingsoftware.autorepairsystem.component.MessageBox;
import com.dockingsoftware.autorepairsystem.component.model.ProjectModel;
import com.dockingsoftware.autorepairsystem.persistence.entity.Project;
import com.dockingsoftware.autorepairsystem.ui.controller.ChooseProjectDialogController;
import com.dockingsoftware.autorepairsystem.util.ImageUtils;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.springframework.beans.BeanUtils;

public class ChooseProjectDialog extends Dialog {

    public ChooseProjectDialog() {
        initComponents();
    }
    
    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());
        
        setTitle("选择维修项目");
        setHeaderText("请选择维修项目。");
 
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("选择");
        okButton.setDisable(true);
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("取消");

        VBox vbox = new VBox();
        vbox.getChildren().addAll(createSearchPane(), createTablePane());
        getDialogPane().setContent(vbox);
    }
    
    private HBox createSearchPane() {
        HBox hbox = new HBox();
        searchField.setPromptText("请输入项目名称或项目分类。");
        searchField.setPrefWidth(400);
        btnGoback.setPrefWidth(60);
        btnSearch.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                String keyword = searchField.getText();
                loadData(keyword);
            }
        });
        btnGoback.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                loadData("");
            }
        });
        hbox.getChildren().addAll(searchField, btnSearch, btnGoback);
        HBox.setHgrow(searchField, Priority.ALWAYS);
        
        return hbox;
    }
    
    private VBox createTablePane() {
        
        loadData("");
    
        Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                if ("price".equals(p.getId())) {
                    return new BigDecimalCell();
                } else if ("laborHour".equals(p.getId())) {
                    return new FloatCell();
                } else {
                    return new EditingCell();
                }
            }
        };
        
        TableColumn No = new TableColumn("序号");
        TableColumn name = new TableColumn("项目名称");
        TableColumn price = new TableColumn("单价");
        TableColumn laborHour = new TableColumn("工时");
        TableColumn projectCategory = new TableColumn("项目分类");
        TableColumn notes = new TableColumn("备注");
        
        price.setId("price");
        laborHour.setId("laborHour");
        
        // 设置列宽
        No.setPrefWidth(50);
        name.setPrefWidth(120);
        price.setPrefWidth(120);
        laborHour.setPrefWidth(120);
        projectCategory.setPrefWidth(120);
        notes.setPrefWidth(120);
        
        // 设置取消编辑列
        No.setEditable(false);
        name.setEditable(false);
        price.setEditable(false);
        laborHour.setEditable(false);
        projectCategory.setEditable(false);
        notes.setEditable(false);
        
        // 设置可编辑列
        No.setCellFactory(cellFactory);
        name.setCellFactory(cellFactory);
        price.setCellFactory(cellFactory);
        laborHour.setCellFactory(cellFactory);
        projectCategory.setCellFactory(cellFactory);
        notes.setCellFactory(cellFactory);
        
        No.setCellValueFactory(new PropertyValueFactory<ProjectModel, String>("No"));
        name.setCellValueFactory(new PropertyValueFactory<ProjectModel, String>("name"));
        price.setCellValueFactory(new PropertyValueFactory<ProjectModel, BigDecimal>("price"));
        laborHour.setCellValueFactory(new PropertyValueFactory<ProjectModel, Float>("laborHour"));
        projectCategory.setCellValueFactory(new PropertyValueFactory<ProjectModel, String>("projectCategory"));
        notes.setCellValueFactory(new PropertyValueFactory<ProjectModel, String>("notes"));
        
        table.setItems(data);
        table.getColumns().addAll(No, name, price, laborHour, projectCategory);
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY );
        
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                disable(false);
            }
        });
        
        // 表格双击
        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getClickCount() == 2) {
                    if (data.size() > 0) {
                        
                        ProjectModel selectedModel = (ProjectModel) table.getSelectionModel().getSelectedItem();
                        if (selectedModel != null) {
                            Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
                            okButton.fire();
                        }
                    }
                }
            }
        });
        
        ContextMenu menu = new ContextMenu();
        MenuItem itemAdd = new MenuItem("添加新项目");
        menu.getItems().addAll(itemAdd, new SeparatorMenuItem(), itemUpdate, itemRemove);
        table.setContextMenu(menu);
        
        itemAdd.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                AddOrUpdateProjectDialog dia = new AddOrUpdateProjectDialog();
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.returnProject(p -> insertProjectRow(p)));
            }
        });
        itemUpdate.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                ProjectModel selectedModel = (ProjectModel) table.getSelectionModel().getSelectedItem();
                if (selectedModel != null) {
                    Project p = (Project) controller.getProjectById.call(selectedModel.getId());
                    AddOrUpdateProjectDialog dia = new AddOrUpdateProjectDialog(p);
                    dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.returnProject(p2 -> updateProjectRow(p2)));
                }
            }
        });
        itemRemove.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                Callback callback = new Callback() {
                    @Override
                    public Object call(Object param) {
                        ProjectModel selectedModel = (ProjectModel) table.getSelectionModel().getSelectedItem();
                        if (selectedModel != null) {
                            Project p = new Project();
                            BeanUtils.copyProperties(selectedModel, p);
                            controller.deleteProject.call(p);
                            data.remove(selectedModel);
                            resortTableRows();
                            if (data.size() == 0) {
                                disable(true);
                            }
                        }
                        return "";
                    }
                };
                MessageBox.showMessage("请确认是否删除该项目。", Alert.AlertType.CONFIRMATION, callback);
            }
        });
        disable(true);
        VBox vbox = new VBox();
        vbox.getChildren().addAll(table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return vbox;
    }
     
    private void insertProjectRow(Project p) {
        ProjectModel projectModel = new ProjectModel();
        BeanUtils.copyProperties(p, projectModel);
        projectModel.setNo((data.size() + 1) + "");
        projectModel.setAmount(new BigDecimal(projectModel.getLaborHour()).multiply(projectModel.getPrice()).subtract(projectModel.getDiscount()));
        data.add(projectModel);
    }
    
    private void updateProjectRow(Project p) {
        ProjectModel selectedModel = (ProjectModel) table.getSelectionModel().getSelectedItem();
        BeanUtils.copyProperties(p, selectedModel);
        selectedModel.setNo(selectedModel.getNo());
        selectedModel.setAmount(new BigDecimal(selectedModel.getLaborHour()).multiply(selectedModel.getPrice()).subtract(selectedModel.getDiscount()));
        table.refresh();
    }
    
    public void returnProject(Consumer<Project> consumer) {
        ProjectModel selectedModel = (ProjectModel) table.getSelectionModel().getSelectedItem();
        Project project = (Project) controller.getProjectById.call(selectedModel.getId());
        consumer.accept(project);
    }
 
    private void loadData(String keyword) {
        data.clear();
        List<Project> lstProject = (List<Project>) controller.list.call(keyword);
        int rowNum = 1;
        for (Project p : lstProject) {
            ProjectModel model = new ProjectModel();
            BeanUtils.copyProperties(p, model);
            // 初始折扣金额
            model.setDiscount(new BigDecimal(0));
            model.setAmount(new BigDecimal(model.getLaborHour()).multiply(model.getPrice()).subtract(model.getDiscount()));
            model.setNo((rowNum++) + "");
            data.add(model);
        }
        disable(true);
    }
    
    private void resortTableRows() {
        int rowNum = 1;
        for (ProjectModel pm : data) {
            pm.setNo((rowNum ++) + "");
        }
    }
    
    private void disable(boolean b) {
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(b);
        itemUpdate.setDisable(b);
        itemRemove.setDisable(b);
    }
    
    private TableView table = new TableView();
    private final ObservableList<ProjectModel> data = FXCollections.observableArrayList();
    private TextField searchField = new TextField();
    private Button btnSearch = new Button("搜索", ImageUtils.createImageView("search_16px_505060_easyicon.net.png"));
    private Button btnGoback = new Button("返回");
    private MenuItem itemUpdate = new MenuItem("变更");
    private MenuItem itemRemove = new MenuItem("删除");
    private ChooseProjectDialogController controller = new ChooseProjectDialogController();
}