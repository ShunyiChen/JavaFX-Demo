/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.component.MessageBox;
import com.dockingsoftware.autorepairsystem.persistence.entity.SettlementDetails;
import com.dockingsoftware.autorepairsystem.ui.controller.IncomeStatisticsTabContentController;
import com.dockingsoftware.autorepairsystem.util.DateUtils;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class IncomeStatisticsTabContent extends VBox {

    public IncomeStatisticsTabContent() {
        initComponents();
    }
    
    private void initComponents() {

        HBox queryPane = new HBox();
        queryPane.setPadding(new Insets(10, 20, 5, 10));
        queryPane.setAlignment(Pos.CENTER);
        queryPane.setSpacing(5);
        ToggleGroup tg = new ToggleGroup();
        btnDay.setToggleGroup(tg);
        btnDay.setSelected(true);
        btnMonth.setToggleGroup(tg);
        fromField.setEditable(false);
        toField.setEditable(false);
        queryPane.getChildren().addAll(btnToday, btnThisMonth, fromField, new Label("到"), toField, new Label("汇总方式"), btnDay, new Label("日"), btnMonth, new Label("月"), btnQuery);
        
        btnToday.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                Date today = new Date();
                query(today, today);
            }
        });
        
        btnThisMonth.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                // 默认查询当月的所有记录
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE,0);
                cal.set(Calendar.SECOND,0);
                cal.set(Calendar.MILLISECOND,0);
                
                query(cal.getTime(), new Date());
            }
        });
        
        btnQuery.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (fromField.getValue() == null || toField.getValue() == null) {
                    MessageBox.showMessage("查询日期不能空。");
                    return;
                }
                if (fromField.getValue().compareTo(toField.getValue()) > 0) {
                    MessageBox.showMessage("起始日期应该小于结束日期。");
                    return;
                }
                Date from = DateUtils.LocalDate2Date(fromField.getValue());
                Date to = DateUtils.LocalDate2Date(toField.getValue());
                query(from, to);
            }
        });
        
        sale1.setStyle("-fx-font-size:12pt;");
        sale2.setStyle("-fx-font-size:12pt;");
        
        this.getChildren().add(queryPane);
        this.getChildren().add(sale1);
        this.getChildren().add(sale2);
 
        // 默认查询当月的所有记录
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);

        query(cal.getTime(), new Date());
    }
    
    /**
     * 查询数据以展示图表
     * 
     * @param from
     * @param to 
     */
    private void query(Date from, Date to) {
        // 创建0时0分0秒一个date对象
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        // 创建23时59分59秒一个date对象
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(to);
        cal2.set(Calendar.HOUR_OF_DAY, 23);
        cal2.set(Calendar.MINUTE,59);
        cal2.set(Calendar.SECOND,59);
        cal2.set(Calendar.MILLISECOND,999);
        
        Criterion c = Restrictions.between("billingDate", cal.getTime(), cal2.getTime());
        List<SettlementDetails> lstSettlementDetails = (List<SettlementDetails>) controller.listSettlementDetails.call(c);
        
        SimpleDateFormat f;
        if (btnDay.isSelected()) {
            f = new SimpleDateFormat("yyyyMMdd");
        } else {
            f = new SimpleDateFormat("yyyyMM");
        }
        
        // 实收金额
        LinkedHashMap<String, Double> map1 = new LinkedHashMap();
        // 尚欠金额
        LinkedHashMap<String, Double> map2 = new LinkedHashMap();
        
        // 合计金额
        BigDecimal t1 = new BigDecimal(0);
        BigDecimal t2 = new BigDecimal(0);
        
        for (SettlementDetails sd : lstSettlementDetails) {
            String x = f.format(sd.getBillingDate());
            if (!map1.containsKey(x)) {
                map1.put(x, sd.getAmountReceived().doubleValue());
                map2.put(x, sd.getOwingAmount().doubleValue());
            } else {
                double d1 = map1.get(x);
                map1.put(x, d1 + sd.getAmountReceived().doubleValue());
                double d2 = map2.get(x);
                map2.put(x, d2 + sd.getOwingAmount().doubleValue());
            }
            
            t1 = t1.add(sd.getAmountReceived());
            t2 = t2.add(sd.getOwingAmount());
        }
        
        sale1.setText("实收金额："+ t1.toString()+ "元");
        sale2.setText("尚欠金额："+ t2.toString() + "元");
        
        if (this.getChildren().contains(chart)) {
            this.getChildren().remove(chart);
        }
        
        
        // 实收金额统计
        double maxY = 0.0d;
        ObservableList list1 = FXCollections.observableArrayList();
        Iterator<String> iter1 = map1.keySet().iterator();
        while (iter1.hasNext()) {
            String x = iter1.next();
            double y = map1.get(x);
            if (y > maxY) {
                maxY = y;
            }
            list1.add(new StackedAreaChart.Data(x, y));
        }
        
        // 尚欠金额统计
        ObservableList list2 = FXCollections.observableArrayList();
        Iterator<String> iter2 = map2.keySet().iterator();
        while (iter2.hasNext()) {
            String x = iter2.next();
            double y = map2.get(x);
            list2.add(new StackedAreaChart.Data(x, y));
        }
        
        // 定义x 和 y轴
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.<String>observableArrayList(map1.keySet()));
        
        double tickUnit = (maxY + 1000.0d) / 10.0d;
        
        NumberAxis yAxis = new NumberAxis("金额(元)", 0.0d, maxY + 1000.0d, tickUnit);
        ObservableList<StackedAreaChart.Series> areaChartData = FXCollections.observableArrayList(
            new StackedAreaChart.Series("实收金额", list1),
            new StackedAreaChart.Series("尚欠金额", list2)
        );
        chart = new AreaChart(xAxis, yAxis, areaChartData);
        
        this.getChildren().add(chart);
    }
    
    private Label sale1 = new Label();
    private Label sale2 = new Label();
    
    private Button btnToday = new Button("今日");
    private Button btnThisMonth = new Button("本月");
    private DatePicker fromField = new DatePicker();
    private DatePicker toField = new DatePicker();
    private RadioButton btnDay = new RadioButton();
    private RadioButton btnMonth = new RadioButton();
    private Button btnQuery = new Button("查询");
    private AreaChart chart;
    private IncomeStatisticsTabContentController controller = new IncomeStatisticsTabContentController();
}
