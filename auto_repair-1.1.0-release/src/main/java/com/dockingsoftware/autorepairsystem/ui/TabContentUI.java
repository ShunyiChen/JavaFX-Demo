/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.Constants;
import com.dockingsoftware.autorepairsystem.component.TreeItemExt;
import com.dockingsoftware.autorepairsystem.util.ImageUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class TabContentUI extends VBox {

    public TabContentUI() {
        
        singleton = this;
        
        setStyle("-fx-background-color: rgb(68, 68, 68);");
        
        HBox titleBox = new HBox();
        title.setPadding(new Insets(15, 0, 15, 20));
        title.setTextFill(Color.BLACK);  
        title.setStyle("-fx-font-size:27");
        titleBox.alignmentProperty().setValue(Pos.CENTER_LEFT);
        titleBox.getChildren().add(title);
        
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                if (newValue != null) {
                    title.setText(newValue.getText());
                }
            }
        });
        
        chartTab.setClosable(false);
        
//        tabPane.getTabs().add(chartTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
        tabPane.getStyleClass().add(TabPane.STYLE_CLASS_FLOATING);

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(Insets.EMPTY);
        borderPane.setStyle("-fx-padding:0 0 0 0;-fx-background-radius:8 0 0 0; -fx-background-color: rgb(250, 250, 250);");
        borderPane.autosize();
        borderPane.setTop(titleBox);
        borderPane.setCenter(tabPane);
        
        this.getChildren().add(borderPane);
        setVgrow(borderPane, Priority.ALWAYS);
    }
    
    public Tab addTab(TreeItemExt item, boolean isNormalCustomer) {
        title.setText(item.getValue().toString());
        Tab tab = createTab(item.getValue().toString());
        tab.setClosable(true);
        tab.setGraphic(ImageUtils.createImageView((String) item.getProperties().get(TreeItemExt.ATTR_IMAGE_KEY)));
        // 结算单开单
        if (isNormalCustomer) {
            tab.setContent(new NormalCustomerBillingTabContent(tab));
        } else {
            tab.setContent(new FactoryBillingTabContent(tab));
        }
        
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        
        setMenu(tab);
        return tab;
    }
    
    /**
     * 
     * @param item 
     */
    public void addTab(TreeItemExt item) {
        title.setText(item.getValue().toString());
        final Tab tab = createTab(item.getValue().toString());
        tab.setClosable(true);
        tab.setGraphic(ImageUtils.createImageView((String) item.getProperties().get(TreeItemExt.ATTR_IMAGE_KEY)));
        // 结算单管理
        if (Constants.JSDGL.equals(item.getProperties().get(TreeItemExt.ATTR_TAB_ID))) {
            tab.setContent(new SettlementManagementTabContent());
        } 
        // 商品管理
        else if (Constants.SPGL.equals(item.getProperties().get(TreeItemExt.ATTR_TAB_ID))) {
            tab.setContent(new ItemManagementTabContent());
        }
        // 客户管理
        else if (Constants.KHGL.equals(item.getProperties().get(TreeItemExt.ATTR_TAB_ID))) {
            tab.setContent(new CustomerManagementTabContent());
        }
        // 续保台账
        else if (Constants.XBTZ.equals(item.getProperties().get(TreeItemExt.ATTR_TAB_ID))) {
            tab.setContent(new InsuranceTabContent());
        }
        // 烤房出租
        else if (Constants.KFCZ.equals(item.getProperties().get(TreeItemExt.ATTR_TAB_ID))) {
            tab.setContent(new RentalManagementTabContent());
        }
        // 收支管理
        else if (Constants.SZGL.equals(item.getProperties().get(TreeItemExt.ATTR_TAB_ID))) {
            tab.setContent(new IncomeAndExpenseTabContent());
        }
        // 商品销售明细表
        else if (Constants.SPXSMXB.equals(item.getProperties().get(TreeItemExt.ATTR_TAB_ID))) {
            tab.setContent(new ItemSalesReportTabContent());
        }
        // 项目销售明细表
        else if (Constants.XMXSMXB.equals(item.getProperties().get(TreeItemExt.ATTR_TAB_ID))) {
            tab.setContent(new ProjectSalesReportTabContent());
        }
        // 销售单汇总表
        else if (Constants.XSDHZB.equals(item.getProperties().get(TreeItemExt.ATTR_TAB_ID))) {
            tab.setContent(new SalesSummaryTabContent());
        }
        // 资金报表(消费收款明细)
        else if (Constants.ZJBB.equals(item.getProperties().get(TreeItemExt.ATTR_TAB_ID))) {
            tab.setContent(new CapitalReportTabContent());
        }
        // 营业额统计
        else if (Constants.YYETJ.equals(item.getProperties().get(TreeItemExt.ATTR_TAB_ID))) {
            tab.setContent(new TurnoverStatisticsTabContent());
        }
        // 收入统计
        else if (Constants.SRTJ.equals(item.getProperties().get(TreeItemExt.ATTR_TAB_ID))) {
            tab.setContent(new IncomeStatisticsTabContent());
        }
        
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        
        setMenu(tab);
    }
    
    public void addTab(String text, VBox tabContent) {
        title.setText(text);
        Tab tab = createTab(text);
        tab.setClosable(true);
        tab.setGraphic(ImageUtils.createImageView("config_16px_504998_easyicon.net.png"));
        tab.setContent(tabContent);
        
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        
        setMenu(tab);
    }
    
    private void setMenu(Tab tab) {
        ContextMenu menu = new ContextMenu();
        MenuItem itemClose = new MenuItem("关闭", ImageUtils.createImageView("close_16px_1181757_easyicon.net.png"));
        MenuItem itemCloseAll = new MenuItem("关闭全部");
        MenuItem itemCloseOther = new MenuItem("关闭其它");
        
        itemClose.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                tabPane.getTabs().remove(tab);
            }
        });
        
        itemCloseAll.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                tabPane.getTabs().clear();
            }
        });
        
        itemCloseOther.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                
                List<Tab> lstTab = new ArrayList<Tab>();
                Iterator<Tab> iter = tabPane.getTabs().iterator();
                while (iter.hasNext()) {
                    Tab t = iter.next();
                    if (t != tab) {
                        lstTab.add(t);
                    }
                }
                if (!lstTab.isEmpty()) {
                    for (int i = lstTab.size() - 1; i >= 0; i--) {
                        tabPane.getTabs().remove(lstTab.get(i));
                    }
                }
            }
        });
        
        menu.getItems().addAll(itemClose, itemCloseAll, itemCloseOther);
        tab.setContextMenu(menu);
    }
    
    private Tab createTab(String text) {
        Tab tab = new Tab(text);
        tab.setOnCloseRequest(new EventHandler() {
            @Override
            public void handle(Event event) {
                title.setText("");
            }
        });
        setMenu(tab);
        return tab;
    }
    
    public static TabContentUI getInstance() {
        return singleton;
    }
    
    private static TabContentUI singleton;
    private TabPane tabPane = new TabPane();
    private Label title = new Label();
    private Tab chartTab = new Tab("统计图表");
}
