/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.Constants;
import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.component.BigDecimalCell;
import com.dockingsoftware.autorepairsystem.component.DatePickerCell;
import com.dockingsoftware.autorepairsystem.component.EditingCell;
import com.dockingsoftware.autorepairsystem.component.FloatCell;
import com.dockingsoftware.autorepairsystem.component.MessageBox;
import com.dockingsoftware.autorepairsystem.component.NumericTextField;
import com.dockingsoftware.autorepairsystem.common.Generator;
import com.dockingsoftware.autorepairsystem.component.model.ItemModel;
import com.dockingsoftware.autorepairsystem.component.model.ProjectModel;
import com.dockingsoftware.autorepairsystem.persistence.entity.BusinessType;
import com.dockingsoftware.autorepairsystem.persistence.entity.Customer;
import com.dockingsoftware.autorepairsystem.persistence.entity.Item;
import com.dockingsoftware.autorepairsystem.persistence.entity.ItemDetails;
import com.dockingsoftware.autorepairsystem.persistence.entity.ItemTag;
import com.dockingsoftware.autorepairsystem.persistence.entity.Project;
import com.dockingsoftware.autorepairsystem.persistence.entity.ProjectDetails;
import com.dockingsoftware.autorepairsystem.persistence.entity.Settlement;
import com.dockingsoftware.autorepairsystem.persistence.entity.SettlementDetails;
import com.dockingsoftware.autorepairsystem.ui.controller.StatementDetailsPaneController;
import com.dockingsoftware.autorepairsystem.util.DateUtils;
import com.dockingsoftware.autorepairsystem.util.ImageUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.springframework.beans.BeanUtils;

public class SettlementDetailsPane extends VBox {

    public SettlementDetailsPane(boolean isFactoryBilling) {
        this.isFactoryBilling = isFactoryBilling;
        initComponents();
    }

    private void initComponents() {
        this.getChildren().addAll(createToolBar(), createInputPane(), projectPane(), itemPane(), sumTotalFieldPane());
    }
    
    private ToolBar createToolBar() {
        ToolBar bar = new ToolBar();
        
        btnCustomerChooser.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                ChooseCustomerDialog dia = new ChooseCustomerDialog();
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.returnCustomer(c -> setCustomerFieldValues(c)));
            }
        });
        
        btnCustomerCreator.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                AddOrUpdateCustomerDialog dia = new AddOrUpdateCustomerDialog();
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateCustomer(c -> setCustomerFieldValues(c)));
            }
        });
        
        btnSave.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (checkCustomerInfo()) {
                    saveOrUpdateSettlement(true);
                }
            }
        });
        
        btnCancel.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                Callback callback = new Callback() {
                    @Override
                    public Object call(Object param) {
                        
                        if (editSettlement != null) {
                            controller.deleteSettlement.call(editSettlement);
                        }
                        
                        tab.getTabPane().getTabs().remove(tab);
                        
                        return "";
                    }
                };
                
                MessageBox.showMessage("请确认是否作废该订单。", Alert.AlertType.CONFIRMATION, callback);
            }
        });
        
        btnStatement.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (editSettlement == null) {
                    if (checkCustomerInfo()) {
                        saveOrUpdateSettlement(false);
                    }
                } 
                
                saveProjectsAndItems(editSettlement.getDetails().get(0));
                SettlementDialog dia = new SettlementDialog(editSettlement.getDetails());
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.pay(null));
                
                settlementStateField.getSelectionModel().select(editSettlement.getDetails().get(0).getSettlementState());
            }
        });
        
        GridPane descPane = createDescriptionPane();
        showMore.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (showMore.isSelected()) {
                    getChildren().add(2, descPane);
                } else {
                    getChildren().remove(descPane);
                }
            }
        });
        
        if (isFactoryBilling) {
            bar.getItems().addAll(btnCustomerChooser, btnCustomerCreator, new Separator(), showMore);
        } else {
            bar.getItems().addAll(btnCustomerChooser, btnCustomerCreator, new Separator(), btnSave, btnCancel, btnStatement, new Separator(), showMore);
        }
        return bar;
    }
    
    private GridPane createInputPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 20, 5, 10));
        
        grid.add(new Label("车牌号:"), 0, 0);
        licensePlateNumberField.setEditable(false);
        grid.add(licensePlateNumberField, 1, 0);
        
        grid.add(new Label("客户名称:"), 2, 0);
        customerNameField.setEditable(false);
        grid.add(customerNameField, 3, 0);
        
        grid.add(new Label("联系电话:"), 4, 0);
        phoneNoField.setEditable(false);
        phoneNoField.setPrefWidth(173);
        grid.add(phoneNoField, 5, 0);
        
        grid.add(btnEdit, 6, 0);
        
        grid.add(new Label("最新公里*:"), 0, 1);
        latestMileageField.setPromptText("最新公里");
        grid.add(latestMileageField, 1, 1);
        
        grid.add(new Label("接待人*:"), 0, 2);
        receptionistField.setEditable(true);
        receptionistField.setPromptText("接待人");
        grid.add(receptionistField, 1, 2);
        
        grid.add(new Label("业务类型*:"), 0, 3);
        List<BusinessType> lstBusinessType = (List<BusinessType>) controller.listBusinessType.call("");
        for (BusinessType bt : lstBusinessType) {
            businessTypeField.getItems().add(bt.getName());
        }
        businessTypeField.getSelectionModel().select(0);
        businessTypeField.setPrefWidth(173);
        grid.add(businessTypeField, 1, 3);
        
        grid.add(new Label("开单日期*:"), 0, 4);
        billingDateField.setEditable(false);
        billingDateField.setValue(LocalDate.now());
        billingDateField.setPromptText("开单日期");
        grid.add(billingDateField, 1, 4);
        
        grid.add(new Label("送修人*:"), 2, 4);
        senderField.setPromptText("送修人");
        grid.add(senderField, 3, 4);
        
        grid.add(new Label("业务状态*:"), 4, 4);
        businessStateField.getItems().addAll("已预约", "已派工", "施工中", "已完工");
        businessStateField.getSelectionModel().select(0);
        businessStateField.setPrefWidth(173);
        grid.add(businessStateField, 5, 4);

        grid.add(new Label("进厂日期*:"), 0, 5);
        enteringDateField.setEditable(false);
        enteringDateField.setValue(LocalDate.now());
        enteringDateField.setPromptText("进厂日期");
        grid.add(enteringDateField, 1, 5);

        grid.add(new Label("油量*:"), 2, 5);
        oilField.setPromptText("油量");
        grid.add(oilField, 3, 5);

        grid.add(new Label("结算状态*:"), 4, 5);
        settlementStateField.getItems().addAll("未结算", "结算中", "已结算");
        settlementStateField.getSelectionModel().select(0);
        settlementStateField.setPrefWidth(173);
        grid.add(settlementStateField, 5, 5);
        
        grid.add(new Label("上次里程:"), 2, 1);
        lastMileageField.setEditable(false);
        lastMileageField.setPromptText("上次里程");
        grid.add(lastMileageField, 3, 1);
        
        grid.add(new Label("车型:"), 2, 2);
        modelField.setEditable(false);
        modelField.setPromptText("车型");
        grid.add(modelField, 3, 2);
        
        grid.add(new Label("车身颜色:"), 2, 3);
        colorField.setEditable(false);
        colorField.setPromptText("车身颜色");
        grid.add(colorField, 3, 3);
        
        grid.add(new Label("历史消费:"), 4, 1);
        historicalAmountField.setEditable(false);
        grid.add(historicalAmountField, 5, 1);
        
        grid.add(btnShowHistory, 6, 1);
        
        grid.add(new Label("历史单数:"), 4, 2);
        historicalNumberField.setEditable(false);
        grid.add(historicalNumberField, 5, 2);
        
        grid.add(new Label("历史欠款:"), 4, 3);
        historicalOwingField.setEditable(false);
        grid.add(historicalOwingField, 5, 3);
        
        btnEdit.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                AddOrUpdateCustomerDialog dia = new AddOrUpdateCustomerDialog(editCustomer);
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateCustomer(c -> setCustomerFieldValues(c)));
            }
        });
        
        btnShowHistory.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (checkCustomerInfo()) {
                    MainApp.getInstance().getTabContentUI().addTab("历史单据", new ViewHistoryTabContent(editCustomer.getId()));
                }
            }
        });
        return grid;
    }
    
    private GridPane createDescriptionPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 20, 5, 10));
        
        descriptionField.setPrefWidth(600);
        recommendField.setPrefWidth(600);
        notesField.setPrefWidth(600);
        
        grid.add(new Label("故障描述*:"), 0, 0);
        descriptionField.setPromptText("故障描述");
        grid.add(descriptionField, 1, 0);
        
        grid.add(new Label("维修建议*:"), 0, 1);
        recommendField.setPromptText("维修建议");
        grid.add(recommendField, 1, 1);
        
        Label lab = new Label("备注*:");
        lab.setMinWidth(58);
        grid.add(lab, 0, 2);
        notesField.setPromptText("备注");
        grid.add(notesField, 1, 2);
        
        return grid;
    }
    
    private BorderPane projectPane() {
        BorderPane pane = new BorderPane();
        pane.setPadding(Insets.EMPTY);
        
        ToolBar toolbar = new ToolBar();
        toolbar.setPadding(new Insets(5, 5, 5, 5));
        
        btnNewProject.setOnAction(new EventHandler() { 
            @Override
            public void handle(Event event) {
                AddOrUpdateProjectDialog dia = new AddOrUpdateProjectDialog();
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.returnProject(p -> insertProjectRow(p)) );
            }
        });
        
        btnChooseProject.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                ChooseProjectDialog dia = new ChooseProjectDialog();
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.returnProject(p -> insertProjectRow(p)) );
            }
        });
        
        btnRemoveProject.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                ProjectModel selectedRow = (ProjectModel) projectTable.getSelectionModel().getSelectedItem();
                if (selectedRow != projectAmountModel) {
                    projectData.remove(selectedRow);
                    
                    updateProjectAmount();
                }
            }
        });
        
        btnChooseProject.setGraphic(ImageUtils.createImageView("paypal_16px_505045_easyicon.net.png"));
        btnNewProject.setGraphic(ImageUtils.createImageView("plus_16px_505050_easyicon.net.png"));
        btnRemoveProject.setGraphic(ImageUtils.createImageView("library_16px_505031_easyicon.net.png"));
        
        toolbar.getItems().addAll(
            btnChooseProject,
            btnNewProject,
            btnRemoveProject
        );
        pane.setTop(toolbar);
        
        projectTable.setEditable(true);
        
        Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                if ("startTime".equals(p.getId())
                        || "endTime".equals(p.getId())
                        || "paymentDate".equals(p.getId())) {
                    return new DatePickerCell();
                } else if ("price".equals(p.getId())
                        || "discount".equals(p.getId())
                        || "amount".equals(p.getId())) {
                    return new BigDecimalCell();
                } else if ("laborHour".equals(p.getId())) { 
                    return new FloatCell();
                } else {
                    return new EditingCell();
                }
            }
        };
        
        TableColumn No = new TableColumn("序号");
        TableColumn name = new TableColumn("项目名称*");
        TableColumn price = new TableColumn("单价*");
        TableColumn laborHour = new TableColumn("工时*");
        TableColumn discount = new TableColumn("优惠*");
        TableColumn amount = new TableColumn("销售金额");
        TableColumn startTime = new TableColumn("开工时间*");
        TableColumn endTime = new TableColumn("完工时间*");
        TableColumn mechanic = new TableColumn("施工人员*");
        TableColumn projectCategory = new TableColumn("项目分类");
        TableColumn projectNotes = new TableColumn("项目备注*");
        
        // 设置不同的ID,渲染不同的单元格。
        price.setId("price");
        discount.setId("discount");
        amount.setId("amount");
        laborHour.setId("laborHour");
        startTime.setId("startTime");
        endTime.setId("endTime");
        
        // 设置列宽
        No.setPrefWidth(50);
        name.setPrefWidth(120);
        price.setPrefWidth(120);
        laborHour.setPrefWidth(120);
        discount.setPrefWidth(120);
        amount.setPrefWidth(120);
        startTime.setPrefWidth(120);
        endTime.setPrefWidth(120);
        mechanic.setPrefWidth(120);
        projectCategory.setPrefWidth(120);
        projectNotes.setPrefWidth(120);
        
        // 设置取消编辑列
        No.setEditable(false);
        amount.setEditable(false);
        projectCategory.setEditable(false);
        
        // 设置可编辑列
        No.setCellFactory(cellFactory);
        name.setCellFactory(cellFactory);
        price.setCellFactory(cellFactory);
        laborHour.setCellFactory(cellFactory);
        discount.setCellFactory(cellFactory);
        amount.setCellFactory(cellFactory);
        startTime.setCellFactory(cellFactory);
        endTime.setCellFactory(cellFactory);
        mechanic.setCellFactory(cellFactory);
        projectCategory.setCellFactory(cellFactory);
        projectNotes.setCellFactory(cellFactory);
         
        No.setCellValueFactory(new PropertyValueFactory<ProjectModel, String>("No"));
        name.setCellValueFactory(new PropertyValueFactory<ProjectModel, String>("name"));
        price.setCellValueFactory(new PropertyValueFactory<ProjectModel, BigDecimal>("price"));
        laborHour.setCellValueFactory(new PropertyValueFactory<ProjectModel, Float>("laborHour"));
        discount.setCellValueFactory(new PropertyValueFactory<ProjectModel, BigDecimal>("discount"));
        amount.setCellValueFactory(new PropertyValueFactory<ProjectModel, BigDecimal>("amount"));
        startTime.setCellValueFactory(new PropertyValueFactory<ProjectModel, LocalDate>("startTime"));
        endTime.setCellValueFactory(new PropertyValueFactory<ProjectModel, LocalDate>("endTime"));
        mechanic.setCellValueFactory(new PropertyValueFactory<ProjectModel, String>("mechanic"));
        projectCategory.setCellValueFactory(new PropertyValueFactory<ProjectModel, String>("projectCategory"));
        projectNotes.setCellValueFactory(new PropertyValueFactory<ProjectModel, String>("notes"));
        
        projectTable.setItems(projectData);
        projectTable.getColumns().addAll(No, name, price, laborHour, discount, amount, startTime, endTime, mechanic, projectCategory, projectNotes);
        projectTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY );
        pane.setCenter(projectTable);
        
        name.setOnEditCommit(new EventHandler<CellEditEvent<ProjectModel, String>>() {
            @Override
            public void handle(CellEditEvent<ProjectModel, String> t) {
                ProjectModel m = ((ProjectModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (m != projectAmountModel) {
                    m.setName(t.getNewValue());
                }
            }
        });
        
        price.setOnEditCommit(new EventHandler<CellEditEvent<ProjectModel, BigDecimal>>() {
            @Override
            public void handle(CellEditEvent<ProjectModel, BigDecimal> t) {
                ProjectModel m = ((ProjectModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (m != projectAmountModel) {
                    m.setPrice(t.getNewValue());
                    
                    BigDecimal price = m.getPrice();
                    BigDecimal discount = m.getDiscount();
                    float hours = m.getLaborHour();
                    m.setAmount(price.multiply(new BigDecimal(hours)).subtract(discount).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
                    
                    updateProjectAmount();
                }
            }
        });
        
        laborHour.setOnEditCommit(new EventHandler<CellEditEvent<ProjectModel, Float>>() {
            @Override
            public void handle(CellEditEvent<ProjectModel, Float> t) {
                
                ProjectModel m = ((ProjectModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (m != projectAmountModel) {
                    m.setLaborHour(t.getNewValue());
                    
                    BigDecimal price = m.getPrice();
                    BigDecimal discount = m.getDiscount();
                    float hours = m.getLaborHour();
                    m.setAmount(price.multiply(new BigDecimal(hours)).subtract(discount).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
                    
                    updateProjectAmount();
                }
            }
        });
        
        discount.setOnEditCommit(new EventHandler<CellEditEvent<ProjectModel, BigDecimal>>() {
            @Override
            public void handle(CellEditEvent<ProjectModel, BigDecimal> t) {
                ProjectModel m = ((ProjectModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (m != projectAmountModel) {
                    m.setDiscount(t.getNewValue());
                    
                    BigDecimal price = m.getPrice();
                    BigDecimal discount = m.getDiscount();
                    float hours = m.getLaborHour();
                    m.setAmount(price.multiply(new BigDecimal(hours)).subtract(discount).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
                    
                    updateProjectAmount();
                }
            }
        });
        
        startTime.setOnEditCommit(new EventHandler<CellEditEvent<ProjectModel, LocalDate>>() {
            @Override
            public void handle(CellEditEvent<ProjectModel, LocalDate> t) {
                ProjectModel m = ((ProjectModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (m != projectAmountModel) {
                    m.setStartTime(t.getNewValue());
                }
            }
        });
        endTime.setOnEditCommit(new EventHandler<CellEditEvent<ProjectModel, LocalDate>>() {
            @Override
            public void handle(CellEditEvent<ProjectModel, LocalDate> t) {
                ProjectModel m = ((ProjectModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (m != projectAmountModel) {
                    m.setEndTime(t.getNewValue());
                }
            }
        });
        
        mechanic.setOnEditCommit(new EventHandler<CellEditEvent<ProjectModel, String>>() {
            @Override
            public void handle(CellEditEvent<ProjectModel, String> t) {
                ProjectModel m = ((ProjectModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (m != projectAmountModel) {
                    m.setMechanic(t.getNewValue());
                }
            }
        });
        
        projectNotes.setOnEditCommit(new EventHandler<CellEditEvent<ProjectModel, String>>() {
            @Override
            public void handle(CellEditEvent<ProjectModel, String> t) {
                ProjectModel m = ((ProjectModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (m != projectAmountModel) {
                    m.setNotes(t.getNewValue());
                }
            }
        });
        
        return pane;
    }
    
    private BorderPane itemPane() {
        BorderPane pane = new BorderPane();
        pane.setPadding(Insets.EMPTY);
   
        ToolBar toolbar = new ToolBar();
        toolbar.setPadding(new Insets(5, 5, 5, 5));
        
        btnChooseItem.setGraphic(ImageUtils.createImageView("product_16px_505054_easyicon.net.png"));
        btnChooseItem.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                ChooseItemDialog dia = new ChooseItemDialog(Constants.WXKD);
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.returnItem(i -> insertItemRow(i)) );
            }
        });
        
        btnNewItem.setGraphic(ImageUtils.createImageView("plus_16px_505050_easyicon.net.png"));
        btnNewItem.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                ItemTag tag = (ItemTag) controller.getRootItemTag.call("");
                AddOrUpdateItemDialog dia = new AddOrUpdateItemDialog(tag, null);
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateItem(item -> insertItemRow(item)));
            }
        });
        
        btnRemoveItem.setGraphic(ImageUtils.createImageView("library_16px_505031_easyicon.net.png"));
        btnRemoveItem.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                ItemModel selectedRow = (ItemModel) itemTable.getSelectionModel().getSelectedItem();
                if (selectedRow != itemAmountModel) {
                    itemData.remove(selectedRow);
                    
                    updateItemAmount();
                }
            }
        });
        
        CheckBox details = new CheckBox("");
        
        toolbar.getItems().addAll(
            btnChooseItem,
            btnNewItem,
            btnRemoveItem,
            new Separator(),
            details
        );
        pane.setTop(toolbar);
        
        itemTable.setEditable(true);
        
        Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                if ("salesPrice".equals(p.getId())
                        || "discount".equals(p.getId())
                        || "amount".equals(p.getId())
                        || "costPrice".equals(p.getId())
                        || "profit".equals(p.getId())) {
                    return new BigDecimalCell();
                } else if ("quantity".equals(p.getId())) { 
                    return new FloatCell();
                } else {
                    return new EditingCell();
                }
            }
        };
        
        TableColumn No = new TableColumn("序号");
        TableColumn productName = new TableColumn("商品名称*");
        TableColumn salesPrice = new TableColumn("销售单价*");
        TableColumn quantity = new TableColumn("数量*");
        TableColumn discount = new TableColumn("优惠*");
        TableColumn amount = new TableColumn("金额");
        TableColumn itemNotes = new TableColumn("商品备注*");
        // 附加列
        TableColumn costPrice = new TableColumn("成本价");
        TableColumn profit = new TableColumn("利润");
        
        // 设置不同的ID,渲染不同的单元格。
        salesPrice.setId("salesPrice");
        quantity.setId("quantity");
        discount.setId("discount");
        amount.setId("amount");
        costPrice.setId("costPrice");
        profit.setId("profit");
        itemNotes.setId("notes");
        
        // 设置列宽
        No.setPrefWidth(50);
        productName.setPrefWidth(120);
        salesPrice.setPrefWidth(120);
        quantity.setPrefWidth(120);
        discount.setPrefWidth(120);
        amount.setPrefWidth(120);
        costPrice.setPrefWidth(120);
        profit.setPrefWidth(120);
        itemNotes.setPrefWidth(120);
        
        // 设置取消编辑列
        No.setEditable(false);
        amount.setEditable(false);
        costPrice.setEditable(false);
        profit.setEditable(false);
        // 设置可编辑列
        No.setCellFactory(cellFactory);
        productName.setCellFactory(cellFactory);
        salesPrice.setCellFactory(cellFactory);
        quantity.setCellFactory(cellFactory);
        discount.setCellFactory(cellFactory);
        amount.setCellFactory(cellFactory);
        itemNotes.setCellFactory(cellFactory);
        costPrice.setCellFactory(cellFactory);
        profit.setCellFactory(cellFactory);
        
        No.setCellValueFactory(new PropertyValueFactory<ProjectModel, String>("No"));
        productName.setCellValueFactory(new PropertyValueFactory<ProjectModel, String>("name"));
        salesPrice.setCellValueFactory(new PropertyValueFactory<ProjectModel, BigDecimal>("salesPrice"));
        quantity.setCellValueFactory(new PropertyValueFactory<ProjectModel, Float>("quantity"));
        discount.setCellValueFactory(new PropertyValueFactory<ProjectModel, BigDecimal>("discount"));
        amount.setCellValueFactory(new PropertyValueFactory<ProjectModel, BigDecimal>("amount"));
        costPrice.setCellValueFactory(new PropertyValueFactory<ProjectModel, BigDecimal>("costPrice"));
        profit.setCellValueFactory(new PropertyValueFactory<ProjectModel, BigDecimal>("profit"));
        itemNotes.setCellValueFactory(new PropertyValueFactory<ProjectModel, String>("notes"));
        
        salesPrice.setOnEditCommit(new EventHandler<CellEditEvent<ItemModel, BigDecimal>>() {
            @Override
            public void handle(CellEditEvent<ItemModel, BigDecimal> t) {
                ItemModel m = ((ItemModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (m != itemAmountModel) {
                    m.setSalesPrice(t.getNewValue());
                    
                    // 计算合计金额
                    BigDecimal price = m.getSalesPrice();
                    BigDecimal discount = m.getDiscount();
                    float quantity = m.getQuantity();
                    BigDecimal costPrice = m.getCostPrice();
                    m.setAmount(price.multiply(new BigDecimal(quantity)).subtract(discount).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
                    BigDecimal totalCost = costPrice.multiply(new BigDecimal(quantity)).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
                    m.setProfit(m.getAmount().subtract(totalCost).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
                    
                    updateItemAmount();
                }
            }
        });
        
        quantity.setOnEditCommit(new EventHandler<CellEditEvent<ItemModel, Float>>() {
            @Override
            public void handle(CellEditEvent<ItemModel, Float> t) {
                
                ItemModel m = ((ItemModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (m != itemAmountModel) {
                    m.setQuantity(t.getNewValue());
                    
                    // 计算合计金额
                    BigDecimal price = m.getSalesPrice();
                    BigDecimal discount = m.getDiscount();
                    float quantity = m.getQuantity();
                    BigDecimal costPrice = m.getCostPrice();
                    m.setAmount(price.multiply(new BigDecimal(quantity)).subtract(discount).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
                    BigDecimal totalCost = costPrice.multiply(new BigDecimal(quantity)).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
                    m.setProfit(m.getAmount().subtract(totalCost).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
                    
                    updateItemAmount();
                }
            }
        });
        
        discount.setOnEditCommit(new EventHandler<CellEditEvent<ItemModel, BigDecimal>>() {
            @Override
            public void handle(CellEditEvent<ItemModel, BigDecimal> t) {
                ItemModel m = ((ItemModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (m != itemAmountModel) {
                    m.setDiscount(t.getNewValue());
                    
                    // 计算合计金额
                    BigDecimal price = m.getSalesPrice();
                    BigDecimal discount = m.getDiscount();
                    float quantity = m.getQuantity();
                    BigDecimal costPrice = m.getCostPrice();
                    m.setAmount(price.multiply(new BigDecimal(quantity)).subtract(discount).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
                    BigDecimal totalCost = costPrice.multiply(new BigDecimal(quantity)).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
                    m.setProfit(m.getAmount().subtract(totalCost).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
                    
                    updateItemAmount();
                }
            }
        });
        
        itemNotes.setOnEditCommit(new EventHandler<CellEditEvent<ItemModel, String>>() {
            @Override
            public void handle(CellEditEvent<ItemModel, String> t) {
                ItemModel m = ((ItemModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                if (m != itemAmountModel) {
                    m.setNotes(t.getNewValue());
                }
            }
        });
        
        details.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                itemTable.getColumns().clear();
                if (details.isSelected()) {
                    itemTable.getColumns().addAll(No, productName, salesPrice, quantity, discount, amount, itemNotes, costPrice, profit);
                } else {
                    itemTable.getColumns().addAll(No, productName, salesPrice, quantity, discount, amount, itemNotes);
                }
            }
        });
        
        itemTable.setItems(itemData);
        itemTable.getColumns().addAll(No, productName, salesPrice, quantity, discount, amount, itemNotes);
        itemTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY );
        pane.setCenter(itemTable);
        return pane;
    }
    
    private HBox sumTotalFieldPane() {
        HBox hbox = new HBox();
        sumTotalField.setEditable(false);
        hbox.getChildren().addAll(new Label("总计消费："), sumTotalField);
        hbox.setAlignment(Pos.CENTER);
        HBox.setHgrow(sumTotalField, Priority.ALWAYS);
        return hbox;
    }
    
    private void setCustomerFieldValues(Customer customer) {
        editCustomer = customer;
        
        licensePlateNumberField.setText(customer.getLicensePlateNumber());
        customerNameField.setText(customer.getName());
        phoneNoField.setText(customer.getPhoneNo());
        lastMileageField.setText(customer.getLatestMileage() + "");
        modelField.setText(customer.getModel());
        colorField.setText(customer.getColor());
        
        // 历史消费记录查询
        List<Settlement> lstSettlement = (List<Settlement>) controller.listSettlementByFactoryId.call(editCustomer.getId());
        BigDecimal historicalAmount = new BigDecimal(0);
        int num = 0;
        BigDecimal historicalOwing = new BigDecimal(0);
        
        for (Settlement s : lstSettlement) {
            List<SettlementDetails> lstDetails = s.getDetails();
            for (SettlementDetails d : lstDetails) {
                historicalAmount = historicalAmount.add(d.getActuallyPay());
                historicalOwing = historicalOwing.add(d.getOwingAmount());
            }
            num ++;
        }
        
        historicalAmountField.setText(historicalAmount.setScale(2, RoundingMode.HALF_UP).toString());
        historicalNumberField.setText(num + "");
        historicalOwingField.setText(historicalOwing.setScale(2, RoundingMode.HALF_UP).toString());
    }
    
    private void saveOrUpdateSettlement(boolean prompt) {
        String msg = "";
        if (editSettlement == null) {
            editSettlement = new Settlement();
            String SN = Generator.generateSN(Constants.WXKD);
            editSettlement.setSN(SN);
            msg = "新订单："+SN+"已生成并保存。";
        } else {
            msg = "订单："+editSettlement.getSN()+"已保存。";
        }
        editSettlement.setBillingObject(Constants.OBJ_1);
        editSettlement.setBillingDate(DateUtils.LocalDate2Date(billingDateField.getValue()));
        editSettlement.setClientId(editCustomer.getId());
        editSettlement.setClientName(editCustomer.getName());
        editSettlement.setPhoneNo(editCustomer.getPhoneNo());
        editSettlement.setLicensePlateNumber(editCustomer.getLicensePlateNumber());

        if (editSettlement.getDetails().isEmpty()) {
            SettlementDetails details = createSettlementDetails(null);
            details.setSN(editSettlement.getSN());
            editSettlement.getDetails().add(details);
        } else {
            createSettlementDetails(editSettlement.getDetails().get(0));
        }
        
        editCustomer.setLatestMileage(latestMileageField.getText().isEmpty() ? 0 : Integer.parseInt(latestMileageField.getText()));//editSettlement.getDetails().get(0).getLatestMileage());
 
        controller.saveOrUpdateCustomer.call(editCustomer);
        controller.saveOrUpdateSettlement.call(editSettlement);
        
        if (prompt) {
            MessageBox.showMessage(msg);
        }
        
        tab.setText(editSettlement.getSN());
    }
    
    public void setInputFieldValues(SettlementDetails details) {
        editCustomer = (Customer) controller.getCustomerById.call(details.getCustomerId());
        
        licensePlateNumberField.setText(editCustomer.getLicensePlateNumber());
        customerNameField.setText(editCustomer.getName());
        phoneNoField.setText(editCustomer.getPhoneNo());
        latestMileageField.setText(details.getLatestMileage() + "");
        lastMileageField.setText(details.getLastMileage() + "");
        modelField.setText(editCustomer.getModel());
        colorField.setText(editCustomer.getColor());
        receptionistField.getEditor().setText(details.getReceptionist());
        businessTypeField.setValue(details.getBusinessType());
        senderField.setText(details.getSender());
        billingDateField.setValue(DateUtils.Date2LocalDate(details.getBillingDate()));
        enteringDateField.setValue(DateUtils.Date2LocalDate(details.getEnteringDate()));
        oilField.setText(details.getOil());
        businessStateField.setValue(details.getBusinessState());
        settlementStateField.setValue(details.getSettlementState());
        descriptionField.setText(details.getDescription());
        recommendField.setText(details.getRecommend());
        notesField.setText(details.getNotes());
        
        lastMileageField.setText(details.getLastMileage() + "");
        latestMileageField.setText(details.getLatestMileage() + "");
        
        for (ProjectDetails p : details.getProjects()) {
            ProjectModel m = new ProjectModel();
            BeanUtils.copyProperties(p, m);
            m.setNo((projectData.size() + 1) +"");
            m.setStartTime(DateUtils.Date2LocalDate(p.getStartTime()));
            m.setEndTime(DateUtils.Date2LocalDate(p.getEndTime()));
            projectData.add(m);

            // 计算合计金额
            BigDecimal price = m.getPrice();
            BigDecimal discount = m.getDiscount();
            float hours = m.getLaborHour();
            m.setAmount(price.multiply(new BigDecimal(hours)).subtract(discount).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
        }
        // 计算总计金额
        updateProjectAmount();
        projectData.add(projectAmountModel);
        
        for (ItemDetails i : details.getItems()) {
            ItemModel m = new ItemModel();
            BeanUtils.copyProperties(i, m);
            m.setNo((itemData.size() + 1) +"");
            itemData.add(m);

            // 计算合计金额
            BigDecimal price = m.getSalesPrice();
            BigDecimal discount = m.getDiscount();
            float quantity = m.getQuantity();
            BigDecimal costPrice = m.getCostPrice();
            m.setAmount(price.multiply(new BigDecimal(quantity)).subtract(discount).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
            BigDecimal totalCost = costPrice.multiply(new BigDecimal(quantity)).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
            m.setProfit(m.getAmount().subtract(totalCost).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
        }
        // 计算总计金额
        updateItemAmount();
        itemData.add(itemAmountModel);
    }
    
    public SettlementDetails createSettlementDetails(SettlementDetails details) {
        if (details == null) {
            details = new SettlementDetails();
            
            details.setActuallyPay(BigDecimal.ZERO);
            details.setAmountReceived(BigDecimal.ZERO);
            details.setOwingAmount(BigDecimal.ZERO);
            details.setDiscountAmount(BigDecimal.ZERO);
        }
        details.setBillingDate(DateUtils.LocalDate2Date(billingDateField.getValue()));
        details.setEnteringDate(DateUtils.LocalDate2Date(enteringDateField.getValue()));
        details.setSender(senderField.getText());
        details.setCustomerId(editCustomer.getId());
        details.setCustomerName(editCustomer.getName());
        details.setPhoneNo(editCustomer.getPhoneNo());
        details.setLicensePlateNumber(editCustomer.getLicensePlateNumber());
        details.setFactoryPlateModel(editCustomer.getFactoryPlateModel());
        details.setVinCode(editCustomer.getVinCode());
        details.setVehicleTypeName(editCustomer.getVehicleTypeName());
        details.setEngineNo(editCustomer.getEngineNo());
        details.setModel(editCustomer.getModel());
        details.setColor(colorField.getText());
        details.setLatestMileage(latestMileageField.getText().isEmpty() ? 0 : Integer.parseInt(latestMileageField.getText()));
        details.setLastMileage(lastMileageField.getText().isEmpty() ? 0 : Integer.parseInt(lastMileageField.getText()));
        details.setRegistrationDate(editCustomer.getRegistrationDate());
        details.setNextMaintenanceMilage(editCustomer.getNextMaintenanceMilage());
        details.setNextMaintenanceDate(editCustomer.getNextMaintenanceDate());
        details.setInsuranceMonthDay(editCustomer.getInsuranceMonthDay());
        details.setInspectionMonth(editCustomer.getInspectionMonth());
        details.setBusinessType(businessTypeField.getValue());
        details.setOil(oilField.getText());
        details.setReceptionist(receptionistField.getEditor().getText());
        details.setDescription(descriptionField.getText());
        details.setRecommend(recommendField.getText());
        details.setNotes(notesField.getText());
        details.setBusinessState(businessStateField.getValue());
        details.setSettlementState(settlementStateField.getValue());
        
        saveProjectsAndItems(details);
        
        return details;
    }
    
    private void saveProjectsAndItems(SettlementDetails details) {
        
        details.getProjects().clear();
        details.getItems().clear();
        
        for (ProjectModel m : projectData) {
            if (m == projectAmountModel) {
                continue;
            }
            ProjectDetails pd = new ProjectDetails();
            BeanUtils.copyProperties(m, pd);
            pd.setId(null);
            pd.setProjectId(m.getId());
            pd.setStartTime(DateUtils.LocalDate2Date(m.getStartTime()));
            pd.setEndTime(DateUtils.LocalDate2Date(m.getEndTime()));
            details.getProjects().add(pd);
        }
        
        for (ItemModel m : itemData) {
            if (m == itemAmountModel) {
                continue;
            }
            ItemDetails ids = new ItemDetails();
            BeanUtils.copyProperties(m, ids);
            ids.setId(null);
            ids.setItemId(m.getId());
            details.getItems().add(ids);
        }
        
        details.setReceivableAmount(new BigDecimal(sumTotalField.getText()).setScale(2, RoundingMode.HALF_UP));
    }
    
    private void insertProjectRow(Project project) {
        projectData.remove(projectAmountModel);
        
        ProjectModel m = new ProjectModel();
        BeanUtils.copyProperties(project, m);
        m.setNo((projectData.size() + 1) +"");
        projectData.add(m);
        
        // 计算合计金额
        BigDecimal price = m.getPrice();
        BigDecimal discount = m.getDiscount();
        float hours = m.getLaborHour();
        m.setAmount(price.multiply(new BigDecimal(hours)).subtract(discount).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
        // 计算总计金额
        updateProjectAmount();
        
        projectData.add(projectAmountModel);
    }
    
    private void insertItemRow(Item item) {
        itemData.remove(itemAmountModel);
        
        ItemModel m = new ItemModel();
        BeanUtils.copyProperties(item, m);
        m.setNo((itemData.size() + 1) +"");
        itemData.add(m);
        
        // 计算合计金额
        BigDecimal price = m.getSalesPrice();
        BigDecimal discount = m.getDiscount();
        float quantity = m.getQuantity();
        BigDecimal costPrice = m.getCostPrice();
        m.setAmount(price.multiply(new BigDecimal(quantity)).subtract(discount).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
        BigDecimal totalCost = costPrice.multiply(new BigDecimal(quantity)).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        m.setProfit(m.getAmount().subtract(totalCost).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
        
        // 计算总计金额
        updateItemAmount();
        
        itemData.add(itemAmountModel);
    }
    
    private void updateProjectAmount() {
        // 优惠合计
        BigDecimal totalDiscount = new BigDecimal(0.00d);
        // 合计金额
        BigDecimal totalSalesAmount = new BigDecimal(0.00d);
        int rowNum = 1;
        for (ProjectModel pm : projectData) {
            if (pm != projectAmountModel) {
                pm.setNo((rowNum ++) + "");
                totalDiscount = totalDiscount.add(pm.getDiscount());
                totalSalesAmount = totalSalesAmount.add(pm.getAmount());
            }
        }
        totalDiscount.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        totalSalesAmount.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        
        projectAmountModel.setNo("合计：");
        projectAmountModel.setDiscount(totalDiscount);
        projectAmountModel.setAmount(totalSalesAmount);
        
        // 加上商品合计金额
        totalSalesAmount = totalSalesAmount.add(itemAmountModel.getAmount());
        
        sumTotalField.setText(totalSalesAmount.toString());
        
        projectTable.refresh();
    }
    
    private void updateItemAmount() {
        // 出库金额
        BigDecimal totalSalesAmount = new BigDecimal(0.00d);
        // 利润
        BigDecimal totalProfitAmount = new BigDecimal(0.00d);
        // 总折扣
        BigDecimal totalDiscount = new BigDecimal(0.00d);
        
        int rowNum = 1;
        float totalQuantity = 0;
        for (ItemModel im : itemData) {
            if (im != itemAmountModel) {
                im.setNo((rowNum ++) + "");
                totalSalesAmount = totalSalesAmount.add(im.getAmount());
                totalQuantity += im.getQuantity();
                totalProfitAmount = totalProfitAmount.add(im.getProfit());
                totalDiscount = totalDiscount.add(im.getDiscount());
            }
        }
        totalSalesAmount.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        totalProfitAmount.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        totalDiscount.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        
        itemAmountModel.setNo("合计：");
        itemAmountModel.setAmount(totalSalesAmount);
        itemAmountModel.setQuantity(totalQuantity);
        itemAmountModel.setProfit(totalProfitAmount);
        itemAmountModel.setDiscount(totalDiscount);
        
        // 加上项目明细合计金额
        totalSalesAmount = totalSalesAmount.add(projectAmountModel.getAmount());
        
        sumTotalField.setText(totalSalesAmount.toString());
        
        itemTable.refresh();
    }
    
    private boolean checkCustomerInfo() {
        if (licensePlateNumberField.getText().isEmpty()) {
            MessageBox.showMessage("请选择或新建一个客户。");
            return false;
        }
        return true;
    }
    
    public void edit(String id) {
        editSettlement = (Settlement) controller.getSettlementById.call(id);
        billingDateField.setValue(DateUtils.Date2LocalDate(editSettlement.getBillingDate()));
        
        Customer c = (Customer) controller.getCustomerById.call(editSettlement.getClientId());
        setCustomerFieldValues(c);
        
        setInputFieldValues(editSettlement.getDetails().get(0));
    }
    
    public TextField getLicensePlateNumberField() {
        return licensePlateNumberField;
    }
    
    public Tab getTab() {
        return tab;
    }

    public void setTab(Tab tab) {
        this.tab = tab;
    }
    
    private Button btnCustomerChooser = new Button("选择客户");
    private Button btnCustomerCreator = new Button("新建客户");
    private Button btnSave = new Button("保存");
    private Button btnCancel = new Button("作废");
    private Button btnStatement = new Button("结算");
    private Button btnEdit = new Button("", ImageUtils.createImageView("edit_16px_505013_easyicon.net.png"));
    private Button btnShowHistory = new Button("", ImageUtils.createImageView("publish_16px_505058_easyicon.net.png"));
    
    private Button btnChooseProject = new Button("选择维修项目");
    private Button btnNewProject = new Button("添加");
    private Button btnRemoveProject = new Button("删除");
    private Button btnChooseItem = new Button("选择配件");
    private Button btnNewItem = new Button("添加");
    private Button btnRemoveItem = new Button("删除");
    
    private TextField licensePlateNumberField = new TextField();
    private TextField customerNameField = new TextField();
    private TextField phoneNoField = new TextField();
    private TextField modelField = new TextField();
    private TextField colorField  = new TextField();
    private NumericTextField latestMileageField  = new NumericTextField();
    private NumericTextField lastMileageField  = new NumericTextField();
    private ComboBox<String> receptionistField = new ComboBox<String>();
    private ComboBox<String> businessTypeField = new ComboBox<String>();
    private TextField senderField  = new TextField();
    private DatePicker billingDateField  = new DatePicker();
    private DatePicker enteringDateField  = new DatePicker();
    private ComboBox<String> businessStateField = new ComboBox<String>();
    private ComboBox<String> settlementStateField = new ComboBox<String>();
    private TextField oilField  = new TextField();
    
    private NumericTextField historicalAmountField  = new NumericTextField();
    private NumericTextField historicalNumberField  = new NumericTextField();
    private NumericTextField historicalOwingField  = new NumericTextField();
    private CheckBox showMore = new CheckBox("显示更多");
    
    private TextField descriptionField  = new TextField();
    private TextField recommendField  = new TextField();
    private TextField notesField  = new TextField();
    
    private TableView projectTable = new TableView();
    private final ObservableList<ProjectModel> projectData = FXCollections.observableArrayList();
    private TableView itemTable = new TableView();
    private final ObservableList<ItemModel> itemData = FXCollections.observableArrayList();
    private NumericTextField sumTotalField = new NumericTextField();
    private boolean isFactoryBilling;
    private Tab tab;
    private ProjectModel projectAmountModel = new ProjectModel();
    private ItemModel itemAmountModel = new ItemModel();
    private Customer editCustomer;
    private Settlement editSettlement;
    private StatementDetailsPaneController controller = new StatementDetailsPaneController();
}
