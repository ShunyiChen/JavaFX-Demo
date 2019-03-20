/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.common;

import com.dockingsoftware.autorepairsystem.component.model.ProjectModel;
import com.dockingsoftware.autorepairsystem.persistence.entity.ItemDetails;
import com.dockingsoftware.autorepairsystem.persistence.entity.Project;
import com.dockingsoftware.autorepairsystem.persistence.entity.ProjectDetails;
import com.dockingsoftware.autorepairsystem.util.ImageUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PrintingTemplate extends Application {

    // Stroked rectangle
    int x = 2;
    int y = 64;
    int w = 570;
    int h = 600;
    
    int fieldWidth = 60;
    int fieldHeight = 25;
    int fieldValueWidth = 80;
    Rectangle rect = new Rectangle(x, y, w, h);
    
    Line line1 = new Line();
    Line line2 = new Line();
    Line line3 = new Line();
    Line line4 = new Line();
    Line line5 = new Line();
    Line line6 = new Line();
    Line line7 = new Line();
    Line line8 = new Line();
    Line line9 = new Line();
    // 横线-维修项目
    Line line10 = new Line();
    Line line11 = new Line();
    Line line12 = new Line();
    
    // 竖线 - 维修项目
    Line line14 = new Line();
    Line line15 = new Line();
    Line line16 = new Line();
    Line line17 = new Line();
    
    /**
     * 维修单模板
     * @param values
     * @return 
     */
    public Parent createContent(Map<String, Object> values) {
        Pane root = new Pane();
        root.setPrefSize(1024, 768);
        root.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        root.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        // Make the simple rectangle red
        // Make the stroked rectangle blue
        rect.setStroke(Color.BLACK);
        rect.setFill(null);
        
        // 标题字体
        Text title = new Text(210, y - 40, "大连骏程汽配厂服务中心");
        title.setFont(Font.font(null, FontWeight.BOLD, 14));
        Text startDate = new Text(x, y - 10, "来店时间：" + values.get("startDate").toString());
        startDate.setFont(Font.font(null, FontWeight.NORMAL, 12));
        // 画表头
        // 横线1
        line1.setStartX(x);
        line1.setStartY(y + fieldHeight);
        line1.setEndX(w + x);
        line1.setEndY(y + fieldHeight);
        line1.setStrokeWidth(0.5f);
        
        // 横线2
        line2.setStartX(x);
        line2.setStartY(y + fieldHeight * 2);
        line2.setEndX(w + x);
        line2.setEndY(y + fieldHeight * 2);
        
        // 竖线
        // 客户名称，客户电话
        line3.setStartX(x + fieldWidth);
        line3.setStartY(y);
        line3.setEndX(x + fieldWidth);
        line3.setEndY(y + fieldHeight * 2);
        line3.setStrokeWidth(0.5f);
        
        line4.setStartX(x + fieldWidth + fieldValueWidth);
        line4.setStartY(y);
        line4.setEndX(x + fieldWidth + fieldValueWidth);
        line4.setEndY(y + fieldHeight * 2);
        line4.setStrokeWidth(0.5f);
        
        // 车牌号，行驶里程
        line5.setStartX(x + fieldWidth * 2 + fieldValueWidth);
        line5.setStartY(y);
        line5.setEndX(x + fieldWidth * 2 + fieldValueWidth);
        line5.setEndY(y + fieldHeight * 2);
        line5.setStrokeWidth(0.5f);
        
        line6.setStartX(x + fieldWidth * 2 + fieldValueWidth * 2);
        line6.setStartY(y);
        line6.setEndX(x + fieldWidth * 2 + fieldValueWidth * 2);
        line6.setEndY(y + fieldHeight * 2);
        line6.setStrokeWidth(0.5f);
        
        // 车型，VIN号
        line7.setStartX(x + fieldWidth * 3 + fieldValueWidth * 2);
        line7.setStartY(y);
        line7.setEndX(x + fieldWidth * 3 + fieldValueWidth * 2);
        line7.setEndY(y + fieldHeight * 2);
        line7.setStrokeWidth(0.5f);
        
        line8.setStartX(x + fieldWidth * 3 + fieldValueWidth * 3);
        line8.setStartY(y);
        line8.setEndX(x + fieldWidth * 3 + fieldValueWidth * 3);
        line8.setEndY(y + fieldHeight);
        line8.setStrokeWidth(0.5f);
        
        // 车辆颜色
        line9.setStartX(x + fieldWidth * 4 + fieldValueWidth * 3);
        line9.setStartY(y);
        line9.setEndX(x + fieldWidth * 4 + fieldValueWidth * 3);
        line9.setEndY(y + fieldHeight);
        line9.setStrokeWidth(0.5f);
        
        root.getChildren().addAll(rect, title, startDate, line1, line2, line3, line4, line5, line6, line7, line8, line9);
        
        Text[] fields = {
            createFieldText(x, y + 13, fieldWidth, fieldHeight, "客户名称"),
            createFieldText(x, y + fieldHeight + 13, fieldWidth, fieldHeight, "客户电话"),
            createFieldText(x + fieldWidth + fieldValueWidth, y + 13, fieldWidth, fieldHeight, "车牌号"),
            createFieldText(x + fieldWidth + fieldValueWidth, y + fieldHeight + 13, fieldWidth, fieldHeight, "行驶里程"),
            createFieldText(x + fieldWidth * 2 + fieldValueWidth * 2, y + 13, fieldWidth, fieldHeight, "车型"),
            createFieldText(x + fieldWidth * 2 + fieldValueWidth * 2, y + fieldHeight + 13, fieldWidth, fieldHeight, "VIN码"),
            createFieldText(x + fieldWidth * 3 + fieldValueWidth * 3, y + 13, fieldWidth, fieldHeight, "油量")
        };
        for (Text ft : fields) {
            root.getChildren().add(ft);
        }
        
        
        Text[] fieldValues = {
            createFieldValueText(x + fieldWidth, y + 13, fieldValueWidth, fieldHeight, values.get("customerName").toString()),
            createFieldValueText(x + fieldWidth, y + fieldHeight + 13, fieldValueWidth, fieldHeight, values.get("phoneNo").toString()),
            createFieldValueText(x + fieldWidth * 2 + fieldValueWidth, y + 13, fieldValueWidth, fieldHeight, values.get("licensePlateNumber").toString()),
            createFieldValueText(x + fieldWidth * 2 + fieldValueWidth, y + fieldHeight + 13, fieldValueWidth, fieldHeight, ""),
            createFieldValueText(x + fieldWidth * 3 + fieldValueWidth * 2, y + 13, fieldValueWidth, fieldHeight, values.get("model").toString()),
            createFieldValueText(x + fieldWidth * 3 + fieldValueWidth * 2, y + fieldHeight + 13, fieldValueWidth + fieldWidth * 2, fieldHeight, values.get("vinCode").toString()),
            createFieldValueText(x + fieldWidth * 4 + fieldValueWidth * 3, y + 13, fieldValueWidth, fieldHeight, values.get("oil").toString())
        };
        for (Text ftv : fieldValues) {
            root.getChildren().add(ftv);
        }

        // 横线10 - 维修项目
        line10.setStartX(x);
        line10.setStartY(y + fieldHeight * 2 + 5);
        line10.setEndX(w + x);
        line10.setEndY(y + fieldHeight * 2 + 5);
        
        // 维修项目
        Text project = createFieldText(x, y + fieldHeight * 2 + 18, w, fieldHeight, "维修项目（产品名称）");
        
        // 横线11 - 维修项目
        line11.setStartX(x);
        line11.setStartY(y + fieldHeight * 3 + 5);
        line11.setEndX(w + x);
        line11.setEndY(y + fieldHeight * 3 + 5);
        
        root.getChildren().addAll(line10, project, line11);
        ObservableList<ProjectModel> projectData = (ObservableList<ProjectModel>) values.get("projectData");
        int i = 4;
        int rowCount = 0;
        for (ProjectModel pm : projectData) {
            if (pm.getNo().startsWith("合计")) {
                continue;
            }
            rowCount ++;
            Line line12 = new Line();
            // 横线12 - 维修项目
            line12.setStartX(x);
            line12.setStartY(y + fieldHeight * i + 5);
            line12.setEndX(w + x);
            line12.setEndY(y + fieldHeight * i + 5);
            line12.setStrokeWidth(0.5f);
 
            Text No = createFieldValueText(x, y + fieldHeight * i - 8, 60, fieldHeight, pm.getNo());
            Text name = createFieldValueText(x + fieldWidth, y + fieldHeight * i - 8, 450, fieldHeight, pm.getName());
            Rectangle rect = new Rectangle(x + 532, y + fieldHeight * i - 15, 16, 16);
            rect.setArcHeight(5);
            rect.setArcWidth(5);
            rect.setFill(null);
            rect.setStroke(Color.BLACK);
            rect.setStrokeWidth(1);
            root.getChildren().addAll(line12, No, name, rect);
            i++;
        }
        
        if (rowCount > 0) {
            // 竖线14 - 维修项目
            line14.setStartX(x + 60);
            line14.setStartY(y + fieldHeight * 3 + 5);
            line14.setEndX(x + 60);
            line14.setEndY(y + fieldHeight * (3 + rowCount) + 5);
            line14.setStrokeWidth(0.5f);

            // 竖线15 - 维修项目
            line15.setStartX(x + 510);
            line15.setStartY(y + fieldHeight * 3 + 5);
            line15.setEndX(x + 510);
            line15.setEndY(y + fieldHeight * (3 + rowCount) + 5);
            line15.setStrokeWidth(0.5f);
        }
        
        root.getChildren().addAll(line14, line15);
        
        Text sign = new Text(x + 430, y + h + 20, "客户签字：");
        root.getChildren().addAll(sign);
        
        
        
        root.setBorder(Border.EMPTY);
        root.setStyle("-fx-background-color: white;");
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setBorder(Border.EMPTY);
        scrollPane.setContent(root);
        
        
        return scrollPane;
    }
    
    /**
     * 结算单模板
     * @param values
     * @return 
     */
    public Parent createContent2(Map<String, Object> values) {
        Pane root = new Pane();
        root.setPrefSize(1024, 768);
        root.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        root.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        // Make the simple rectangle red
        // Make the stroked rectangle blue
        rect.setStroke(Color.BLACK);
        rect.setFill(null);
        
        // 标题字体
        Text title = new Text(210, y - 40, "大连骏程汽配厂结算单");
        title.setFont(Font.font(null, FontWeight.BOLD, 14));
        Text settlementDate = new Text(x, y - 10, "结算时间：" + values.get("settlementDate").toString());
        Text payment = new Text(x + 170, y - 10, "结算方式：" + values.get("payment").toString());
        settlementDate.setFont(Font.font(null, FontWeight.NORMAL, 12));
        payment.setFont(Font.font(null, FontWeight.NORMAL, 12));
        // 画表头
        // 横线1
        line1.setStartX(x);
        line1.setStartY(y + fieldHeight);
        line1.setEndX(w + x);
        line1.setEndY(y + fieldHeight);
        line1.setStrokeWidth(0.5f);
        
        // 横线2
        line2.setStartX(x);
        line2.setStartY(y + fieldHeight * 2);
        line2.setEndX(w + x);
        line2.setEndY(y + fieldHeight * 2);
        
        // 竖线
        // 客户名称，客户电话
        line3.setStartX(x + fieldWidth);
        line3.setStartY(y);
        line3.setEndX(x + fieldWidth);
        line3.setEndY(y + fieldHeight * 2);
        line3.setStrokeWidth(0.5f);
        
        line4.setStartX(x + fieldWidth + fieldValueWidth);
        line4.setStartY(y);
        line4.setEndX(x + fieldWidth + fieldValueWidth);
        line4.setEndY(y + fieldHeight * 2);
        line4.setStrokeWidth(0.5f);
        
        // 车牌号，行驶里程
        line5.setStartX(x + fieldWidth * 2 + fieldValueWidth);
        line5.setStartY(y);
        line5.setEndX(x + fieldWidth * 2 + fieldValueWidth);
        line5.setEndY(y + fieldHeight * 2);
        line5.setStrokeWidth(0.5f);
        
        line6.setStartX(x + fieldWidth * 2 + fieldValueWidth * 2);
        line6.setStartY(y);
        line6.setEndX(x + fieldWidth * 2 + fieldValueWidth * 2);
        line6.setEndY(y + fieldHeight * 2);
        line6.setStrokeWidth(0.5f);
        
        // 车型，VIN号
        line7.setStartX(x + fieldWidth * 3 + fieldValueWidth * 2);
        line7.setStartY(y);
        line7.setEndX(x + fieldWidth * 3 + fieldValueWidth * 2);
        line7.setEndY(y + fieldHeight * 2);
        line7.setStrokeWidth(0.5f);
        
        line8.setStartX(x + fieldWidth * 3 + fieldValueWidth * 3);
        line8.setStartY(y);
        line8.setEndX(x + fieldWidth * 3 + fieldValueWidth * 3);
        line8.setEndY(y + fieldHeight);
        line8.setStrokeWidth(0.5f);
        
        // 车辆颜色
        line9.setStartX(x + fieldWidth * 4 + fieldValueWidth * 3);
        line9.setStartY(y);
        line9.setEndX(x + fieldWidth * 4 + fieldValueWidth * 3);
        line9.setEndY(y + fieldHeight);
        line9.setStrokeWidth(0.5f);
        
        root.getChildren().addAll(rect, title, settlementDate, payment, line1, line2, line3, line4, line5, line6, line7, line8, line9);
        
        Text[] fields = {
            createFieldText(x, y + 13, fieldWidth, fieldHeight, "客户名称"),
            createFieldText(x, y + fieldHeight + 13, fieldWidth, fieldHeight, "客户电话"),
            createFieldText(x + fieldWidth + fieldValueWidth, y + 13, fieldWidth, fieldHeight, "车牌号"),
            createFieldText(x + fieldWidth + fieldValueWidth, y + fieldHeight + 13, fieldWidth, fieldHeight, "行驶里程"),
            createFieldText(x + fieldWidth * 2 + fieldValueWidth * 2, y + 13, fieldWidth, fieldHeight, "车型"),
            createFieldText(x + fieldWidth * 2 + fieldValueWidth * 2, y + fieldHeight + 13, fieldWidth, fieldHeight, "VIN码"),
            createFieldText(x + fieldWidth * 3 + fieldValueWidth * 3, y + 13, fieldWidth, fieldHeight, "送修人")
        };
        for (Text ft : fields) {
            root.getChildren().add(ft);
        }
        
        
        Text[] fieldValues = {
            createFieldValueText(x + fieldWidth, y + 13, fieldValueWidth, fieldHeight, values.get("customerName").toString()),
            createFieldValueText(x + fieldWidth, y + fieldHeight + 13, fieldValueWidth, fieldHeight, values.get("phoneNo").toString()),
            createFieldValueText(x + fieldWidth * 2 + fieldValueWidth, y + 13, fieldValueWidth, fieldHeight, values.get("licensePlateNumber").toString()),
            createFieldValueText(x + fieldWidth * 2 + fieldValueWidth, y + fieldHeight + 13, fieldValueWidth, fieldHeight, ""),
            createFieldValueText(x + fieldWidth * 3 + fieldValueWidth * 2, y + 13, fieldValueWidth, fieldHeight, values.get("model").toString()),
            createFieldValueText(x + fieldWidth * 3 + fieldValueWidth * 2, y + fieldHeight + 13, fieldValueWidth + fieldWidth * 2, fieldHeight, values.get("vinCode").toString()),
            createFieldValueText(x + fieldWidth * 4 + fieldValueWidth * 3, y + 13, fieldValueWidth, fieldHeight, values.get("sender").toString())
        };
        for (Text ftv : fieldValues) {
            root.getChildren().add(ftv);
        }

        // 横线10 - 维修项目
        line10.setStartX(x);
        line10.setStartY(y + fieldHeight * 2 + 5);
        line10.setEndX(w + x);
        line10.setEndY(y + fieldHeight * 2 + 5);
        
        // 维修项目
        Text project = createFieldText(x, y + fieldHeight * 2 + 18, w, fieldHeight, "维修项目（产品名称）");
        
        // 横线11 - 维修项目
        line11.setStartX(x);
        line11.setStartY(y + fieldHeight * 3 + 5);
        line11.setEndX(w + x);
        line11.setEndY(y + fieldHeight * 3 + 5);
        line11.setStrokeWidth(0.5f);
        
        // 横线12 - 维修项目
        line12.setStartX(x);
        line12.setStartY(y + fieldHeight * 4 + 5);
        line12.setEndX(w + x);
        line12.setEndY(y + fieldHeight * 4 + 5);
        line12.setStrokeWidth(0.5f);
        root.getChildren().addAll(line10, project, line11, line12);
        
        Text No = createFieldValueText(x, y + fieldHeight * 4 - 8, 60, fieldHeight, "序号");
        Text name = createFieldValueText(x + 60, y + fieldHeight *4 - 8, 320, fieldHeight, "维修项目");
        Text hours = createFieldValueText(x + 380, y + fieldHeight *4 - 8, 60, fieldHeight, "工时");
        Text price = createFieldValueText(x + 440, y + fieldHeight *4 - 8, 60, fieldHeight, "工时单价");
        Text amount = createFieldValueText(x + 500, y + fieldHeight *4 - 8, 60, fieldHeight, "金额");
        
        root.getChildren().addAll(No, name, hours, price, amount);
        
        List<ProjectDetails> projectData = (List<ProjectDetails>) values.get("projectData");
        int i = 5;
        int rowCount = 0;
        BigDecimal totalAmount1 = new BigDecimal(0.00f);
        for (ProjectDetails pd : projectData) {
            rowCount ++;
            Line l = new Line();
            // 横线12 - 维修项目
            l.setStartX(x);
            l.setStartY(y + fieldHeight * i + 5);
            l.setEndX(w + x);
            l.setEndY(y + fieldHeight * i + 5);
            l.setStrokeWidth(0.5f);
 
            Text v1 = createFieldValueText(x, y + fieldHeight * i - 8, 60, fieldHeight, rowCount + "");
            Text v2 = createFieldValueText(x + 60, y + fieldHeight * i - 8, 320, fieldHeight, pd.getName());
            Text v3 = createFieldValueText(x + 380, y + fieldHeight * i - 8, 60, fieldHeight, pd.getLaborHour() +"");
            Text v4 = createFieldValueText(x + 440, y + fieldHeight * i - 8, 60, fieldHeight, pd.getPrice().toString());
            Text v5 = createFieldValueText(x + 500, y + fieldHeight * i - 8, 60, fieldHeight, pd.getAmount().toString());
 
            totalAmount1 = totalAmount1.add(pd.getPrice().multiply(new BigDecimal(pd.getLaborHour()))).setScale(2, RoundingMode.HALF_UP);
            
            root.getChildren().addAll(l, v1, v2, v3, v4, v5);
            i++;
        }
        
        
        Text v6 = createFieldValueText(x, y + fieldHeight * i - 8, 60, fieldHeight, "合计");
        Text v7 = createFieldValueText(x + 500, y + fieldHeight * i - 8, 60, fieldHeight, totalAmount1.toString());
        Line l2 = new Line();
        // 横线12 - 合计
        l2.setStartX(x);
        l2.setStartY(y + fieldHeight * i + 5);
        l2.setEndX(w + x);
        l2.setEndY(y + fieldHeight * i + 5);
        l2.setStrokeWidth(0.5f);
        root.getChildren().addAll(v6, v7, l2);
        
        ///////// 商品部分
        
        i ++;
        // 横线 - 商品
        Line line18 = new Line();
        line18.setStartX(x);
        line18.setStartY(y + fieldHeight * i + 5);
        line18.setEndX(w + x);
        line18.setEndY(y + fieldHeight * i + 5);
        line18.setStrokeWidth(0.5f);
        
        Text c1 = createFieldValueText(x, y + fieldHeight * i - 8, 60, fieldHeight, "序号");
        Text c2 = createFieldValueText(x + 60, y + fieldHeight * i - 8, 320, fieldHeight, "商品名称");
        Text c3 = createFieldValueText(x + 380, y + fieldHeight * i - 8, 60, fieldHeight, "数量");
        Text c4 = createFieldValueText(x + 440, y + fieldHeight * i - 8, 60, fieldHeight, "配件单价");
        Text c5 = createFieldValueText(x + 500, y + fieldHeight * i - 8, 60, fieldHeight, "金额");
        
        root.getChildren().addAll(line18, c1, c2, c3, c4, c5);
        i ++;

        BigDecimal totalAmount2 = new BigDecimal(0.00f);
        List<ItemDetails> itemData = (List<ItemDetails>) values.get("itemData");
        int totalCount = rowCount + 2;
        rowCount = 0;
        for (ItemDetails id : itemData) {
            rowCount ++;
            Line l = new Line();
            // 横线12 - 维修项目
            l.setStartX(x);
            l.setStartY(y + fieldHeight * i + 5);
            l.setEndX(w + x);
            l.setEndY(y + fieldHeight * i + 5);
            l.setStrokeWidth(0.5f);
            
            Text v1 = createFieldValueText(x, y + fieldHeight * i - 8, 60, fieldHeight, rowCount + "");
            Text v2 = createFieldValueText(x + 60, y + fieldHeight * i - 8, 320, fieldHeight, id.getName());
            Text v3 = createFieldValueText(x + 380, y + fieldHeight * i - 8, 60, fieldHeight, id.getQuantity() +"");
            Text v4 = createFieldValueText(x + 440, y + fieldHeight * i - 8, 60, fieldHeight, id.getSalesPrice().toString());
            Text v5 = createFieldValueText(x + 500, y + fieldHeight * i - 8, 60, fieldHeight, id.getAmount().toString());
 
            totalAmount2 = totalAmount2.add(id.getSalesPrice().multiply(new BigDecimal(id.getQuantity()))).setScale(2, RoundingMode.HALF_UP);
            
            root.getChildren().addAll(l, v1, v2, v3, v4, v5);
            i++;
        }
        totalCount += rowCount;
        
        Text v8 = createFieldValueText(x, y + fieldHeight * i - 8, 60, fieldHeight, "合计");
        Text v9 = createFieldValueText(x + 500, y + fieldHeight * i - 8, 60, fieldHeight, totalAmount2.toString());
        Line l3 = new Line();
        // 横线12 - 合计
        l3.setStartX(x);
        l3.setStartY(y + fieldHeight * i + 5);
        l3.setEndX(w + x);
        l3.setEndY(y + fieldHeight * i + 5);
        l3.setStrokeWidth(0.5f);
        root.getChildren().addAll(v8, v9, l3);
        
        i++;
        Text v10 = createFieldValueText(x, y + fieldHeight * i - 8, 60, fieldHeight, "总计");
        Text v11 = createFieldValueText(x + 500, y + fieldHeight * i - 8, 60, fieldHeight, totalAmount1.add(totalAmount2).setScale(2, RoundingMode.HALF_UP).toString());
        Line l4 = new Line();
        // 横线12 - 总计
        l4.setStartX(x);
        l4.setStartY(y + fieldHeight * i + 5);
        l4.setEndX(w + x);
        l4.setEndY(y + fieldHeight * i + 5);
        l4.setStrokeWidth(0.5f);
        root.getChildren().addAll(v10, v11, l4);
        
        if (totalCount > 0) {
            // 竖线14 - 序号
            line14.setStartX(x + 60);
            line14.setStartY(y + fieldHeight * 3 + 5);
            line14.setEndX(x + 60);
            line14.setEndY(y + fieldHeight * (5 + totalCount) + 5);
            line14.setStrokeWidth(0.5f);

            // 竖线15 - 维修项目
            line15.setStartX(x + 380);
            line15.setStartY(y + fieldHeight * 3 + 5);
            line15.setEndX(x + 380);
            line15.setEndY(y + fieldHeight * (5 + totalCount) + 5);
            line15.setStrokeWidth(0.5f);
            
            // 竖线15 - 工时
            line16.setStartX(x + 440);
            line16.setStartY(y + fieldHeight * 3 + 5);
            line16.setEndX(x + 440);
            line16.setEndY(y + fieldHeight * (5 + totalCount) + 5);
            line16.setStrokeWidth(0.5f);
            
            // 竖线15 - 工时单价
            line17.setStartX(x + 500);
            line17.setStartY(y + fieldHeight * 3 + 5);
            line17.setEndX(x + 500);
            line17.setEndY(y + fieldHeight * (5 + totalCount) + 5);
            line17.setStrokeWidth(0.5f);
        }
        root.getChildren().addAll(line14, line15, line16, line17);
        
        Text sign = new Text(x + 430, y + h + 20, "客户签字：");
        root.getChildren().addAll(sign);
        
        
        root.setBorder(Border.EMPTY);
        root.setStyle("-fx-background-color: white;");
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setBorder(Border.EMPTY);
        scrollPane.setContent(root);
        
        
        return scrollPane;
    }
 
    private Text createFieldValueText(int cell_x, int cell_y, int cell_width, int cell_height, String text) {
        
        Text t = createFieldText(cell_x, cell_y, cell_width, cell_height, text);
        t.setFont(Font.font(null, FontWeight.NORMAL, 12));
        return t;
    }
    
    private Text createFieldText(int cell_x, int cell_y, int cell_width, int cell_height, String text) {
        
        Text t = new Text(text);
        t.setFont(Font.font(null, FontWeight.NORMAL, 12));
        
        double text_width = t.getLayoutBounds().getWidth();
        double text_height = t.getLayoutBounds().getHeight();
        
        int x_offset = (int) ((cell_width - text_width) / 2);
        int y_offset = (int) ((cell_height - text_height) / 2);
        
        t.setX(cell_x + x_offset);
        t.setY(cell_y + y_offset);
        return t;
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Map<String, Object> values = new HashMap<String, Object>();
        
        primaryStage.setScene(new Scene(createContent(values)));
        primaryStage.show();
    }
 
    /**
     * Java main for when running without JavaFX launcher
     */
    public static void main(String[] args) {
        launch(args);
    }
}
