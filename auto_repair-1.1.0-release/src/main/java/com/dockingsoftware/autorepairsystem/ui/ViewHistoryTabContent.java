/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.Constants;
import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.component.DatePickerCell;
import com.dockingsoftware.autorepairsystem.component.EditingCell;
import com.dockingsoftware.autorepairsystem.component.TreeItemExt;
import com.dockingsoftware.autorepairsystem.component.model.SettlementModel;
import com.dockingsoftware.autorepairsystem.persistence.entity.Settlement;
import com.dockingsoftware.autorepairsystem.ui.controller.ViewHistoryTabContentController;
import com.dockingsoftware.autorepairsystem.util.DateUtils;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;

public class ViewHistoryTabContent extends VBox {
    
    /**
     * Constructor.
     * 
     * @param clientId 
     */
    public ViewHistoryTabContent(String clientId) {
        this.clientId = clientId;
        initComponent();
    }
    
    private void initComponent() {
        BorderPane tablePane = createTablePane();
        this.getChildren().addAll(tablePane);
        VBox.setVgrow(tablePane, Priority.ALWAYS);
    }
    
    private BorderPane createTablePane() {
        BorderPane pane = new BorderPane();
        pane.setPadding(Insets.EMPTY);
        
        Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
            
            @Override
            public TableCell call(TableColumn p) {
                if ("billingDateCol".equals(p.getId())) {
                    return new DatePickerCell();
                }
                else {
                    return new EditingCell();
                }
            }
        };
        
        TableColumn NoCol = new TableColumn("序号");
        TableColumn SNCol = new TableColumn("单号");
        TableColumn billingObjectCol = new TableColumn("开单对象");
        TableColumn billingDateCol = new TableColumn("开单日期");
        TableColumn clientNameCol = new TableColumn("客户名");
        TableColumn phoneNoCol = new TableColumn("联系电话");
        TableColumn licensePlateNumberCol = new TableColumn("车牌号");
        TableColumn contactsCol = new TableColumn("联系人");
        
        // 渲染列ID
        NoCol.setId("NoCol");
        SNCol.setId("SNCol");
        billingObjectCol.setId("billingObjectCol");
        billingDateCol.setId("billingDateCol");
        clientNameCol.setId("clientNameCol");
        phoneNoCol.setId("phoneNoCol");
        licensePlateNumberCol.setId("licensePlateNumberCol");
        contactsCol.setId("contactsCol");
      
        NoCol.setCellFactory(cellFactory);
        SNCol.setCellFactory(cellFactory);
        billingObjectCol.setCellFactory(cellFactory);
        billingDateCol.setCellFactory(cellFactory);
        clientNameCol.setCellFactory(cellFactory);
        phoneNoCol.setCellFactory(cellFactory);
        licensePlateNumberCol.setCellFactory(cellFactory);
        contactsCol.setCellFactory(cellFactory);
        
        // 设置列宽
        NoCol.setPrefWidth(50);
        SNCol.setPrefWidth(120);
        billingObjectCol.setPrefWidth(120);
        billingDateCol.setPrefWidth(120);
        clientNameCol.setPrefWidth(120);
        phoneNoCol.setPrefWidth(120);
        licensePlateNumberCol.setPrefWidth(120);
        contactsCol.setPrefWidth(120);
        
        NoCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("No"));
        SNCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("SN"));
        billingObjectCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("billingObject"));
        billingDateCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("billingDate"));
        clientNameCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("clientName"));
        phoneNoCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("phoneNo"));
        licensePlateNumberCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("licensePlateNumber"));
        contactsCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("contacts"));
        
        table.setItems(data);
        table.getColumns().addAll(NoCol, SNCol, billingObjectCol, billingDateCol, clientNameCol, phoneNoCol, licensePlateNumberCol, contactsCol); 
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//                disable(false);
            }
        });
        
        // 表格双击
        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getClickCount() == 2) {
                    if (data.size() > 0) {
                        seeDetails();
                    }
                }
            }
        });

        // 设置表格右键菜单
        ContextMenu menu = new ContextMenu();
        MenuItem seeDetailsItem = new MenuItem("查看详细");
        menu.getItems().addAll(seeDetailsItem);
        seeDetailsItem.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                seeDetails();
            }
        });
        
        table.setContextMenu(menu);

        loadTableData();
        
        pane.setCenter(table);
        return pane;
    }
    
    private void loadTableData() {
        data.clear();
        Criterion c = Restrictions.eq("clientId", clientId);
        List<Settlement> results = (List<Settlement>) controller.listSettlement.call(c);
        int rowNum = 1;
        for (Settlement sd : results) {
            SettlementModel m = new SettlementModel();
            BeanUtils.copyProperties(sd, m);
            m.setNo((rowNum ++ ) + "");
            m.setBillingDate(DateUtils.Date2LocalDate(sd.getBillingDate()));
            data.add(m);
        }
    }
    
    private void seeDetails() {
        SettlementModel selectedModel = (SettlementModel) table.getSelectionModel().getSelectedItem();
        if (selectedModel != null) {
            if (selectedModel.getBillingObject().equals(Constants.OBJ_1)) {
                TreeItemExt tix = MainApp.getInstance().getNavigationBar().getItemWXKD();
                Tab tab = TabContentUI.getInstance().addTab(tix, true);
                tab.setText(selectedModel.getSN());
                NormalCustomerBillingTabContent content = (NormalCustomerBillingTabContent) tab.getContent();
                content.getPane().edit(selectedModel.getId());
            } else if (selectedModel.getBillingObject().equals(Constants.OBJ_2)) {
                TreeItemExt tix = MainApp.getInstance().getNavigationBar().getItemWXKD();
                Tab tab = TabContentUI.getInstance().addTab(tix, false);
                tab.setText(selectedModel.getSN());
                FactoryBillingTabContent content = (FactoryBillingTabContent) tab.getContent();
                content.edit(selectedModel.getId());
            }
        }
    }
    
    private String clientId;
    private TableView table = new TableView();
    private final ObservableList<SettlementModel> data = FXCollections.observableArrayList();
    private ViewHistoryTabContentController controller = new ViewHistoryTabContentController();
}
