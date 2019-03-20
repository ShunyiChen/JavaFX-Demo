/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;


import com.dockingsoftware.autorepairsystem.Constants;
import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.developer.GetClientInfo;
import static com.dockingsoftware.autorepairsystem.developer.GetClientInfo.getSerialNumber;
import com.dockingsoftware.autorepairsystem.component.MessageBox;
import com.dockingsoftware.autorepairsystem.util.ImageUtils;
import com.dockingsoftware.autorepairsystem.component.TreeItemExt;
import com.dockingsoftware.autorepairsystem.developer.GenerateSerialNumberDialog;
import com.dockingsoftware.autorepairsystem.developer.SourceGenerator;
import com.dockingsoftware.autorepairsystem.ui.controller.CommonController;
import com.dockingsoftware.autorepairsystem.util.crypto.CipherUtils;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;

/**
 * 导航树
 * 
 * @author Shunyi Chen
 */
public class NavigationBar extends VBox {
    
    public NavigationBar() {
        initComponents();
    }

    private void initComponents() {
        this.setPrefSize(196, 0);
//        this.setStyle("-fx-background-color: rgba(65, 75, 86);");
        // Set Photo
        VBox vboxPhoto = new VBox();
        vboxPhoto.setStyle("-fx-background-color: rgb(68, 68, 68);");
        ImageView photo = ImageUtils.createImageView("profile-pic-300px.jpg");
        photo.setFitWidth(60);
        photo.setFitHeight(60);
        Ellipse ellipse1 = new Ellipse(30, 30, 30, 30);
        photo.setClip(ellipse1);
        vboxPhoto.getChildren().add(photo);
        vboxPhoto.alignmentProperty().setValue(Pos.CENTER);
        this.getChildren().add(vboxPhoto);
        // Set user name
        HBox hboxName = new HBox();
        hboxName.setStyle("-fx-background-color: rgba(68, 68, 68);");
        
        btnDown.setPadding(Insets.EMPTY);
        btnDown.setBackground(Background.EMPTY);
        ImageView light_arrow = ImageUtils.createImageView("light_arrow.png");
        ImageView dark_arrow = ImageUtils.createImageView("dark_arrow.png");
        btnDown.setGraphic(light_arrow);
        btnDown.setContentDisplay(ContentDisplay.RIGHT);
        btnDown.setTextFill(Color.rgb(250, 250, 250));
        btnDown.setFont(Font.font(15));
        hboxName.getChildren().add(btnDown);
        hboxName.alignmentProperty().setValue(Pos.CENTER);
        hboxName.setPadding(new Insets(8, 0, 8, 0));
        this.getChildren().add(hboxName);
        // Set menu
        MenuItem edit = new MenuItem("更改密码", ImageUtils.createImageView("edit_16px_505013_easyicon.net.png"));
        
        Menu develper = new Menu("开发者工具", ImageUtils.createImageView("lightbulb_16px_505032_easyicon.net.png"));
        MenuItem gettingInfo = new MenuItem("制作ID文件");
        MenuItem generator = new MenuItem("代码生成器");
        MenuItem generateSerialNumber = new MenuItem("生成序列号");
        develper.getItems().addAll(gettingInfo, generator, generateSerialNumber);
        
        MenuItem signout = new MenuItem("退   出", ImageUtils.createImageView("logout_16px_505037_easyicon.net.png"));
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(edit, new SeparatorMenuItem(), signout);
        contextMenu.setStyle("-fx-background-radius: 6 6 6 6;-fx-font-size:11pt;");
        
//        contextMenu.setStyle("-fx-background-radius: 6 6 6 6;-fx-font-size: "+Constants.MENU_FONT_SIZE+";-fx-font-family: "+Constants.FONT_NAME+";");
        btnDown.setContextMenu(contextMenu);
        
        btnDown.setOnMouseEntered((MouseEvent e) -> {
            btnDown.setTextFill(Color.rgb(26, 132, 226));
            btnDown.setGraphic(dark_arrow);
        });
        btnDown.setOnMouseExited((MouseEvent e) -> {
            btnDown.setTextFill(Color.rgb(156, 165, 175));
            btnDown.setGraphic(light_arrow);
        });
        btnDown.setOnAction((ActionEvent e) -> {
            btnDown.getContextMenu().show(btnDown, Side.BOTTOM, -40, 0);
        });
        
        edit.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                ChangePasswordDialog dia = new ChangePasswordDialog();
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.change());
            }
        });
        
        signout.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                
                Map<String, String> allParameters = (Map<String, String>) controller.listAllParameters.call("");
                String p1 = allParameters.get("RETAIN_FILES");
                String p2 = allParameters.get("SAVING_BEFORE_EXITING");
                String p3 = allParameters.get("SAVE_PATH");
                if (Boolean.parseBoolean(p2)) {
                    MainApp.getInstance().getTitleUI().backup(Boolean.parseBoolean(p1), p3);
                } else {
                    System.exit(0);
                }
            }
        });
        
        gettingInfo.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                StringBuilder sb = new StringBuilder();
                sb.append("Current MAC address:"+GetClientInfo.getMACAddress()+"\n\n");
                
                String sn = getSerialNumber("C");
                sb.append("getSerialNumber:"+sn+"\n\n");
                
                CipherUtils.testEncrypt(sb);
                
                MessageBox.showMessage(sb.toString());
            }
        });
        
        generator.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                SourceGenerator dia = new SourceGenerator();
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.generate());
            }
        });
        
        generateSerialNumber.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                GenerateSerialNumberDialog dia = new GenerateSerialNumberDialog();
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.generate());
            }
        });
        
        itemJCGL.getChildren().addAll(itemWXKD, itemJSDGL, itemKFCZ, itemSZGL, itemKHGL, itemSPGL);
        itemBB.getChildren().addAll(itemSPXSMXB, itemXMXSMXB, itemXSDHZB, itemZJBB, itemYYETJ, itemSRTJ);
        itemYSYW.getChildren().addAll(itemXBTZ);
        rootItem.getChildren().addAll(itemJCGL, itemBB, itemYSYW);
        itemWXKD.getProperties().put(TreeItemExt.ATTR_TAB_ID, Constants.WXKD);
        
        // 注册ID
        itemJSDGL.getProperties().put(TreeItemExt.ATTR_TAB_ID, Constants.JSDGL);
        itemKFCZ.getProperties().put(TreeItemExt.ATTR_TAB_ID, Constants.KFCZ);
        itemSZGL.getProperties().put(TreeItemExt.ATTR_TAB_ID, Constants.SZGL);
        itemKHGL.getProperties().put(TreeItemExt.ATTR_TAB_ID, Constants.KHGL);
        itemSPGL.getProperties().put(TreeItemExt.ATTR_TAB_ID, Constants.SPGL);
        itemGHSGL.getProperties().put(TreeItemExt.ATTR_TAB_ID, Constants.GHSGL);
        itemXBTZ.getProperties().put(TreeItemExt.ATTR_TAB_ID, Constants.XBTZ);
        itemSPXSMXB.getProperties().put(TreeItemExt.ATTR_TAB_ID, Constants.SPXSMXB);
        itemXMXSMXB.getProperties().put(TreeItemExt.ATTR_TAB_ID, Constants.XMXSMXB);
        itemXSDHZB.getProperties().put(TreeItemExt.ATTR_TAB_ID, Constants.XSDHZB);
        itemZJBB.getProperties().put(TreeItemExt.ATTR_TAB_ID, Constants.ZJBB);
        itemYYETJ.getProperties().put(TreeItemExt.ATTR_TAB_ID, Constants.YYETJ);
        itemSRTJ.getProperties().put(TreeItemExt.ATTR_TAB_ID, Constants.SRTJ);
        
        // 展开项
        rootItem.setExpanded(true);
        itemJCGL.setExpanded(true);
        itemYSYW.setExpanded(true);
        itemBB.setExpanded(true);
        
        tree = new TreeView (rootItem);
        // 针对特定部件设置全局样式
        tree.getStylesheets().add("styles/styled-unique-expanded-tree.css");
        tree.showRootProperty().setValue(false);
        StackPane root = new StackPane();
        root.getChildren().add(tree);
        this.getChildren().add(root);
        VBox.setVgrow(root, Priority.ALWAYS);
        
        tree.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> { 
            clickNode();
        });
        
//        tree.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
//            if (e.getButton() == MouseButton.PRIMARY) {
//                clickNode();
//            }
//        });
    }
    
    private void clickNode() {
        TreeItemExt selectedItem = (TreeItemExt) tree.getSelectionModel().getSelectedItem();
        if (selectedItem.isLeaf()) {
            if (Constants.WXKD.equals(selectedItem.getProperties().get(TreeItemExt.ATTR_TAB_ID))) {

                ChooseOrderTypeDialog dia = new ChooseOrderTypeDialog();
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response ->  dia.isNormalCustomer(bool -> TabContentUI.getInstance().addTab(selectedItem, bool)));
            } else {
                TabContentUI.getInstance().addTab(selectedItem);
            }
        }
    }
    
    public TreeItemExt getItemWXKD() {
        return itemWXKD;
    }

    public Button getBtnDown() {
        return btnDown;
    }
    
    private Button btnDown = new Button("");
    private TreeItemExt rootItem = new TreeItemExt ("Root", "jhgl.png");
    private TreeItemExt itemJCGL = new TreeItemExt ("接车管理", "BMW_15.937743190661px_1133685_easyicon.net.png");
    private TreeItemExt itemWXKD = new TreeItemExt ("维修开单", "config_16px_504998_easyicon.net.png");
    private TreeItemExt itemJSDGL = new TreeItemExt ("结算单管理", "config_16px_504998_easyicon.net.png");
    private TreeItemExt itemKFCZ = new TreeItemExt ("烤房出租管理", "home_16px_505027_easyicon.net.png");
    private TreeItemExt itemSZGL = new TreeItemExt ("收支管理", "home_16px_505027_easyicon.net.png");
    
    private TreeItemExt itemBB = new TreeItemExt ("报表", "invoice_16px_505029_easyicon.net.png");
    private TreeItemExt itemSPXSMXB = new TreeItemExt ("商品销售明细表", "invoice_16px_505029_easyicon.net.png");
    private TreeItemExt itemXMXSMXB = new TreeItemExt ("项目销售明细表", "invoice_16px_505029_easyicon.net.png");
    private TreeItemExt itemXSDHZB = new TreeItemExt ("销售单汇总表", "invoice_16px_505029_easyicon.net.png");
    private TreeItemExt itemZJBB = new TreeItemExt ("消费收款明细表", "invoice_16px_505029_easyicon.net.png");
    private TreeItemExt itemYYETJ = new TreeItemExt ("营业额统计", "invoice_16px_505029_easyicon.net.png");
    private TreeItemExt itemSRTJ = new TreeItemExt ("收入统计", "invoice_16px_505029_easyicon.net.png");
    
    private TreeItemExt itemKHGL = new TreeItemExt ("客户管理", "customers_customer_man_16px_515828_easyicon.net.png");
    private TreeItemExt itemSPGL = new TreeItemExt ("商品管理", "product_16px_505054_easyicon.net.png");
    private TreeItemExt itemGHSGL = new TreeItemExt ("供货商管理", "suppliers_16px_505071_easyicon.net.png");
    private TreeItemExt itemYSYW = new TreeItemExt ("衍生业务", "collaboration_16px_504995_easyicon.net.png");
    private TreeItemExt itemXBTZ = new TreeItemExt ("续保台账", "archives_16px_504982_easyicon.net.png");
    private TreeView tree;
    private CommonController controller = new CommonController();
}
