/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dockingsoftware.autorepairsystem.common;

import com.dockingsoftware.autorepairsystem.Constants;
import com.dockingsoftware.autorepairsystem.component.model.CustomerModel;
import com.dockingsoftware.autorepairsystem.component.model.IAEModel;
import com.dockingsoftware.autorepairsystem.component.model.InsuranceModel;
import com.dockingsoftware.autorepairsystem.component.model.Inventory;
import com.dockingsoftware.autorepairsystem.component.model.ItemModel;
import com.dockingsoftware.autorepairsystem.component.model.ProjectModel;
import com.dockingsoftware.autorepairsystem.component.model.Rent;
import com.dockingsoftware.autorepairsystem.component.model.SettlementModel;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 导出Excel
 * 
 * @author Shunyi Chen
 */
public class Excel {
    
    private Excel() {
    }
    
    private CellStyle createBorderStyle(Workbook wb) {
        // 设置边线样式
        CellStyle style = wb.createCellStyle();
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        
        return style;
    }
    
    private CellStyle createBorderAndFontStyle(Workbook wb) {
        // 设置边线样式
        CellStyle style = wb.createCellStyle();
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        HSSFFont font = (HSSFFont) wb.createFont();
//        font.setFontHeightInPoints((short) 24); // 字体高度
//        font.setFontName("宋体"); // 字体
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度
        style.setFont(font);
        return style;
    }
    
    public void exportInsurance(TableView tableView, String outputFileFullPath) throws FileNotFoundException, IOException {
        Workbook wb = new HSSFWorkbook();
        CellStyle style = createBorderStyle(wb);
        CellStyle style2 = createBorderAndFontStyle(wb);
        Sheet sheet = wb.createSheet("续保台账");
        // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet.createRow(0);
        // insert columns
        ObservableList<TableColumn> lstCols = tableView.getColumns();
        int col = 0;
        for (TableColumn tc : lstCols) {
            // Create a cell and put a date value in it.  The first cell is not styled
            // as a date.
            if ("保险金额".equals(tc.getText())) {
                ObservableList<TableColumn> lst = tc.getColumns();
                for (TableColumn subtc : lst) {
                    Cell colCell = row.createCell(col);
                    colCell.setCellStyle(style2);
                    colCell.setCellValue(withoutAsterisks(subtc.getText()));
                    col++;
                }
            } else {
                Cell colCell = row.createCell(col);
                colCell.setCellStyle(style2);
                colCell.setCellValue(withoutAsterisks(tc.getText()));
                col++;
            }
        }
        
        // insert data
        ObservableList<InsuranceModel> data = tableView.getItems();
        int rn = 1;
        for (InsuranceModel item : data) {
            Row r1 = sheet.createRow(rn);
            int c = 0;
            for (TableColumn tc : lstCols) {
                String colName = withoutAsterisks(tc.getText());
                if ("保险金额".equals(colName)) {
                    ObservableList<TableColumn> lst = tc.getColumns();
                    for (TableColumn subtc : lst) {
                        String cn = withoutAsterisks(subtc.getText());
                        Cell cell = r1.createCell(c);
                        cell.setCellStyle(style);
                        createCellForInsurance(item, cell, cn);
                        c++;
                    }
                } else {
                    Cell cell = r1.createCell(c);
                    cell.setCellStyle(style);
                    createCellForInsurance(item, cell, colName);
                    c++;
                }
            }
            rn++;
        }
        
        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(outputFileFullPath);
        wb.write(fileOut);
        fileOut.close();
    }
    
    private String withoutAsterisks(String colName) {
        if (colName.endsWith("*")) {
            colName = colName.substring(0, colName.length() - 1);
        }
        return colName;
    }
    
    private void createCellForInsurance(InsuranceModel item, Cell cell, String colName) {
        if (colName.equals("序号")) {
            cell.setCellValue(item.getNo());
        } else if (colName.equals("出单日期")) {
            cell.setCellValue(item.getOutDate() == null ? "" : item.getOutDate().toString());
        } else if (colName.equals("生效日期")) {
            cell.setCellValue(item.getFromDate() == null ? "" : item.getFromDate().toString());
        } else if (colName.equals("被保险人")) {
            cell.setCellValue(item.getName());
        } else if (colName.equals("车牌号")) {
            cell.setCellValue(item.getLicensePlateNumber());
        } else if (colName.equals("商业险金额")) {
            cell.setCellValue(item.getCommerceInsuranceAmount() == null ? "" : item.getCommerceInsuranceAmount().toString());
        } else if (colName.equals("交强险金额")) {
            cell.setCellValue(item.getCompulsoryInsuranceAmount() == null ? "" : item.getCompulsoryInsuranceAmount().toString());
        } else if (colName.equals("车船使用税")) {
            cell.setCellValue(item.getUsageTax() == null ? "" : item.getUsageTax().toString());
        } else if (colName.equals("合计")) {
            cell.setCellValue(item.getTotal() == null ? "" : item.getTotal().toString());
        } else if (colName.equals("备注")) {
            cell.setCellValue(item.getInsuranceNotes());
        } else if (colName.equals("销售员")) {
            cell.setCellValue(item.getSeller());
        } else if (colName.equals("手续费")) {
            cell.setCellValue(item.getFee() == null ? "" : item.getFee().toString());
        } else if (colName.equals("客户保费回款")) {
            cell.setCellValue(item.getPayback() == null ? "" : item.getPayback().toString());
        } else if (colName.equals("客户返利")) {
            cell.setCellValue(item.getCustomerRebate() == null ? "" : item.getCustomerRebate().toString());
        } else if (colName.equals("收入")) {
            cell.setCellValue(item.getIncome() == null ? "" : item.getIncome().toString());
        } else if (colName.equals("状态")) {
            cell.setCellValue(item.getStatus() == null ? "" : item.getStatus().toString());
        } else if (colName.equals("商业险费率")) {
            cell.setCellValue(item.getCommercialInsuranceRate() == null ? "" : item.getCommercialInsuranceRate().toString());
        } else if (colName.equals("交强险费率")) {
            cell.setCellValue(item.getCompulsoryInsuranceRate() == null ? "" : item.getCompulsoryInsuranceRate().toString());
        }
    }
    
    public void exportIncomeAndExpenseReport(TableView tableView, String outputFileFullPath) throws FileNotFoundException, IOException {
        Workbook wb = new HSSFWorkbook();
        CellStyle style = createBorderStyle(wb);
        CellStyle style2 = createBorderAndFontStyle(wb);
        Sheet sheet = wb.createSheet("收支金额");

        // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet.createRow(0);
        
        // insert columns
        ObservableList<TableColumn> lstCols = tableView.getColumns();
        int col = 0;
        for (TableColumn tc : lstCols) {
            // Create a cell and put a date value in it.  The first cell is not styled
            // as a date.
            Cell colCell = row.createCell(col);
            colCell.setCellStyle(style2);
            colCell.setCellValue(tc.getText());
            col++;
        }
        
        // insert data
        ObservableList<IAEModel> data = tableView.getItems();
        int rn = 1;
        for (IAEModel item : data) {
            Row r1 = sheet.createRow(rn);
            int c = 0;
            for (TableColumn tc : lstCols) {
                Cell cell = r1.createCell(c);
                cell.setCellStyle(style);
                if (tc.getText().equals("序号")) {
                    cell.setCellValue(item.getNo());
                } else if (tc.getText().equals("科目")) {
                    cell.setCellValue(item.getSubject());
                } else if (tc.getText().equals("摘要")) {
                    cell.setCellValue(item.getSummary());
                } else if (tc.getText().equals("收入")) {
                    cell.setCellValue(item.getIncomeAmount() == null ? "" : item.getIncomeAmount().toString());
                } else if (tc.getText().equals("支出")) {
                    cell.setCellValue(item.getExpenseAmount() == null ? "" : item.getExpenseAmount().toString());
                } else if (tc.getText().equals("经手人")) {
                    cell.setCellValue(item.getHandlerName());
                } else if (tc.getText().equals("支付方式")) {
                    cell.setCellValue(item.getPayment());
                } else if (tc.getText().equals("支付日期")) {
                    cell.setCellValue(item.getPaymentDate() == null ? "" : item.getPaymentDate().toString());
                }
                c++;
            }
            rn++;
        }
        
        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(outputFileFullPath);
        wb.write(fileOut);
        fileOut.close();
    }
    
    public void exportCapitalReport(TableView tableView, String outputFileFullPath) throws FileNotFoundException, IOException {
        Workbook wb = new HSSFWorkbook();
        CellStyle style = createBorderStyle(wb);
        CellStyle style2 = createBorderAndFontStyle(wb);
        Sheet sheet = wb.createSheet("消费收款明细");

        // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet.createRow(0);
        
        // insert columns
        ObservableList<TableColumn> lstCols = tableView.getColumns();
        int col = 0;
        for (TableColumn tc : lstCols) {
            // Create a cell and put a date value in it.  The first cell is not styled
            // as a date.
            Cell colCell = row.createCell(col);
            colCell.setCellStyle(style2);
            colCell.setCellValue(tc.getText());
            col++;
        }
        
        // insert data
        ObservableList<SettlementModel> data = tableView.getItems();
        int rn = 1;
        for (SettlementModel item : data) {
            Row r1 = sheet.createRow(rn);
            int c = 0;
            for (TableColumn tc : lstCols) {
                Cell cell = r1.createCell(c);
                cell.setCellStyle(style);
                if (tc.getText().equals("序号")) {
                    cell.setCellValue(item.getNo());
                } else if (tc.getText().equals("单号")) {
                    cell.setCellValue(item.getSN());
                } else if (tc.getText().equals("结算金额")) {
                    cell.setCellValue(item.getAmountReceived() == null ? "" : item.getAmountReceived().toString());
                } else if (tc.getText().equals("结算方式")) {
                    cell.setCellValue(item.getPayment());
                } else if (tc.getText().equals("结算日期")) {
                    cell.setCellValue(item.getSettlementDate() == null ? "" : item.getSettlementDate().toString());
                } else if (tc.getText().equals("车牌号")) {
                    cell.setCellValue(item.getLicensePlateNumber());
                } else if (tc.getText().equals("客户姓名")) {
                    cell.setCellValue(item.getCustomerName());
                } else if (tc.getText().equals("联系手机")) {
                    cell.setCellValue(item.getPhoneNo());
                } else if (tc.getText().equals("开单日期")) {
                    cell.setCellValue(item.getBillingDate() == null ? "" :item.getBillingDate().toString());
                } else if (tc.getText().equals("结算备注")) {
                    cell.setCellValue(item.getSettlementNotes());
                }
                c++;
            }
            rn++;
        }
        
        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(outputFileFullPath);
        wb.write(fileOut);
        fileOut.close();
    }
    
    public void exportSalesSummaryReport(TableView tableView, String outputFileFullPath) throws FileNotFoundException, IOException {
        Workbook wb = new HSSFWorkbook();
        CellStyle style = createBorderStyle(wb);
        CellStyle style2 = createBorderAndFontStyle(wb);
        Sheet sheet = wb.createSheet("销售单汇总表");

        // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet.createRow(0);
        
        // insert columns
        ObservableList<TableColumn> lstCols = tableView.getColumns();
        int col = 0;
        for (TableColumn tc : lstCols) {
            // Create a cell and put a date value in it.  The first cell is not styled
            // as a date.
            Cell colCell = row.createCell(col);
            colCell.setCellStyle(style2);
            colCell.setCellValue(tc.getText());
            col++;
        }
        
        // insert data
        ObservableList<SettlementModel> data = tableView.getItems();
        int rn = 1;
        for (SettlementModel item : data) {
            Row r1 = sheet.createRow(rn);
            int c = 0;
            for (TableColumn tc : lstCols) {
                Cell cell = r1.createCell(c);
                cell.setCellStyle(style);
                if (tc.getText().equals("序号")) {
                    cell.setCellValue(item.getNo());
                } else if (tc.getText().equals("单号")) {
                    cell.setCellValue(item.getSN());
                } else if (tc.getText().equals("客户名称")) {
                    cell.setCellValue(item.getCustomerName());
                } else if (tc.getText().equals("号码号牌")) {
                    cell.setCellValue(item.getLicensePlateNumber());
                } else if (tc.getText().equals("车型")) {
                    cell.setCellValue(item.getModel());
                } else if (tc.getText().equals("开单日期")) {
                    cell.setCellValue(item.getBillingDate() == null ? "" :item.getBillingDate().toString());
                } else if (tc.getText().equals("优惠金额")) {
                    cell.setCellValue(item.getDiscountAmount() == null ? "" : item.getDiscountAmount().toString());
                } else if (tc.getText().equals("应收金额")) {
                    cell.setCellValue(item.getReceivableAmount() == null ? "" : item.getReceivableAmount().toString());
                } else if (tc.getText().equals("实际支付")) {
                    cell.setCellValue(item.getActuallyPay() == null ? "" : item.getActuallyPay().toString());
                } else if (tc.getText().equals("已收金额")) {
                    cell.setCellValue(item.getAmountReceived() == null ? "" : item.getAmountReceived().toString());
                } else if (tc.getText().equals("尚欠金额")) {
                    cell.setCellValue(item.getOwingAmount() == null ? "" : item.getOwingAmount().toString());
                } else if (tc.getText().equals("商品成本")) {
                    cell.setCellValue(item.getCostPrice() == null ? "" : item.getCostPrice().toString());
                } else if (tc.getText().equals("商品金额")) {
                    cell.setCellValue(item.getItemAmount() == null ? "" :item.getItemAmount().toString());
                } else if (tc.getText().equals("项目金额")) {
                    cell.setCellValue(item.getProjectAmount() == null ? "" : item.getProjectAmount().toString());
                } else if (tc.getText().equals("利润")) {
                    cell.setCellValue(item.getProfit() == null ? "" : item.getProfit().toString());
                } else if (tc.getText().equals("接待人")) {
                    cell.setCellValue(item.getReceptionist());
                } else if (tc.getText().equals("业务类型")) {
                    cell.setCellValue(item.getBusinessType());
                } else if (tc.getText().equals("备注")) {
                    cell.setCellValue(item.getNotes());
                }
                c++;
            }
            rn++;
        }
        
        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(outputFileFullPath);
        wb.write(fileOut);
        fileOut.close();
    }
    
    public void exportProjectSalesReport(TableView tableView, String outputFileFullPath) throws FileNotFoundException, IOException {
        Workbook wb = new HSSFWorkbook();
        CellStyle style = createBorderStyle(wb);
        CellStyle style2 = createBorderAndFontStyle(wb);
        Sheet sheet = wb.createSheet("项目销售明细表");

        // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet.createRow(0);
        
        // insert columns
        ObservableList<TableColumn> lstCols = tableView.getColumns();
        int col = 0;
        for (TableColumn tc : lstCols) {
            // Create a cell and put a date value in it.  The first cell is not styled
            // as a date.
            Cell colCell = row.createCell(col);
            colCell.setCellStyle(style2);
            colCell.setCellValue(tc.getText());
            col++;
        }
        
        // insert data
        ObservableList<ProjectModel> data = tableView.getItems();
        int rn = 1;
        for (ProjectModel item : data) {
            Row r1 = sheet.createRow(rn);
            int c = 0;
            for (TableColumn tc : lstCols) {
                Cell cell = r1.createCell(c);
                cell.setCellStyle(style);
                if (tc.getText().equals("序号")) {
                    cell.setCellValue(item.getNo());
                } else if (tc.getText().equals("单号")) {
                    cell.setCellValue(item.getSN());
                } else if (tc.getText().equals("开单日期")) {
                    cell.setCellValue(item.getBillingDate() == null ? "" :item.getBillingDate().toString());
                } else if (tc.getText().equals("项目名称")) {
                    cell.setCellValue(item.getName());
                } else if (tc.getText().equals("项目单价")) {
                    cell.setCellValue(item.getPrice().toString());
                } else if (tc.getText().equals("工时")) {
                    cell.setCellValue(item.getLaborHour());
                } else if (tc.getText().equals("优惠金额")) {
                    cell.setCellValue(item.getDiscount().toString());
                } else if (tc.getText().equals("金额")) {
                    cell.setCellValue(item.getAmount().toString());
                } else if (tc.getText().equals("项目备注")) {
                    cell.setCellValue(item.getNotes());
                } else if (tc.getText().equals("开工时间")) {
                    cell.setCellValue(item.getStartTime() == null ? "" : item.getStartTime().toString());
                } else if (tc.getText().equals("完工时间")) {
                    cell.setCellValue(item.getEndTime() == null ? "" :item.getEndTime().toString());
                } else if (tc.getText().equals("施工人员")) {
                    cell.setCellValue(item.getMechanic());
                } else if (tc.getText().equals("项目分类")) {
                    cell.setCellValue(item.getProjectCategory());
                } else if (tc.getText().equals("客户名称")) {
                    cell.setCellValue(item.getCustomerName());
                } else if (tc.getText().equals("号码号牌")) {
                    cell.setCellValue(item.getLicensePlateNumber());
                } else if (tc.getText().equals("结算方式")) {
                    cell.setCellValue(item.getPayment());
                } else if (tc.getText().equals("结算日期")) {
                    cell.setCellValue(item.getSettlementDate() == null ? "" :item.getSettlementDate().toString());
                } else if (tc.getText().equals("结算备注")) {
                    cell.setCellValue(item.getSettlementNotes());
                } else if (tc.getText().equals("结算状态")) {
                    cell.setCellValue(item.getSettlementState());
                }
                c++;
            }
            rn++;
        }
        
        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(outputFileFullPath);
        wb.write(fileOut);
        fileOut.close();
    }
    
    public void exportItemSalesReport(TableView tableView, String outputFileFullPath) throws FileNotFoundException, IOException {
        Workbook wb = new HSSFWorkbook();
        CellStyle style = createBorderStyle(wb);
        CellStyle style2 = createBorderAndFontStyle(wb);
        Sheet sheet = wb.createSheet("商品销售明细表");

        // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet.createRow(0);
        
        // insert columns
        ObservableList<TableColumn> lstCols = tableView.getColumns();
        int col = 0;
        for (TableColumn tc : lstCols) {
            // Create a cell and put a date value in it.  The first cell is not styled
            // as a date.
            Cell colCell = row.createCell(col);
            colCell.setCellStyle(style2);
            colCell.setCellValue(tc.getText());
            col++;
        }
        
        // insert data
        ObservableList<ItemModel> data = tableView.getItems();
        int rn = 1;
        for (ItemModel item : data) {
            Row r1 = sheet.createRow(rn);
            int c = 0;
            for (TableColumn tc : lstCols) {
                Cell cell = r1.createCell(c);
                cell.setCellStyle(style);
                if (tc.getText().equals("序号")) {
                    cell.setCellValue(item.getNo());
                } else if (tc.getText().equals("单号")) {
                    cell.setCellValue(item.getSN());
                } else if (tc.getText().equals("开单日期")) {
                    cell.setCellValue(item.getBillingDate() == null ? "" :item.getBillingDate().toString());
                } else if (tc.getText().equals("商品名称")) {
                    cell.setCellValue(item.getName());
                } else if (tc.getText().equals("销售单价")) {
                    cell.setCellValue(item.getSalesPrice().toString());
                } else if (tc.getText().equals("数量")) {
                    cell.setCellValue(item.getQuantity());
                } else if (tc.getText().equals("优惠金额")) {
                    cell.setCellValue(item.getDiscount().toString());
                } else if (tc.getText().equals("金额")) {
                    cell.setCellValue(item.getAmount().toString());
                } else if (tc.getText().equals("商品备注")) {
                    cell.setCellValue(item.getNotes());
                } else if (tc.getText().equals("成本价")) {
                    cell.setCellValue(item.getCostPrice().toString());
                } else if (tc.getText().equals("利润")) {
                    cell.setCellValue(item.getProfit().toString());
                } else if (tc.getText().equals("客户名称")) {
                    cell.setCellValue(item.getCustomerName());
                } else if (tc.getText().equals("号码号牌")) {
                    cell.setCellValue(item.getLicensePlateNumber());
                } else if (tc.getText().equals("结算方式")) {
                    cell.setCellValue(item.getPayment());
                } else if (tc.getText().equals("结算日期")) {
                    cell.setCellValue(item.getSettlementDate() == null ? "" :item.getSettlementDate().toString());
                } else if (tc.getText().equals("结算备注")) {
                    cell.setCellValue(item.getSettlementNotes());
                } else if (tc.getText().equals("结算状态")) {
                    cell.setCellValue(item.getSettlementState());
                }
                c++;
            }
            rn++;
        }
        
        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(outputFileFullPath);
        wb.write(fileOut);
        fileOut.close();
    }
    
    public void exportRentals(TreeTableView treeTableView, String outputFileFullPath) throws FileNotFoundException, IOException {
        Workbook wb = new HSSFWorkbook();
        CellStyle style = createBorderStyle(wb);
        CellStyle style2 = createBorderAndFontStyle(wb);
        Sheet sheet = wb.createSheet("烤房出租");
        
        // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet.createRow(0);
        
        // insert columns
        ObservableList<TreeTableColumn> lstCols = treeTableView.getColumns();
        int col = 0;
        for (TreeTableColumn ttc : lstCols) {
            // Create a cell and put a date value in it.  The first cell is not styled
            // as a date.
            Cell colCell = row.createCell(col);
            colCell.setCellStyle(style2);
            colCell.setCellValue(ttc.getText());
            
            col++;
        }
        // insert data
        ObservableList<TreeItem<Rent>> data = treeTableView.getRoot().getChildren();//
        int rn = 1;
        for (TreeItem<Rent> item : data) {
            Row r1 = sheet.createRow(rn);
            int c = 0;
            for (TreeTableColumn ttc : lstCols) {
                Cell cell = r1.createCell(c);
                cell.setCellStyle(style);
                if (ttc.getText().equals("单号")) {
                    cell.setCellValue(item.getValue().SNProperty().get());
                } else if (ttc.getText().equals("开单日期")) {
                    cell.setCellValue(item.getValue().billingDateProperty().get());
                } else if (ttc.getText().equals("租户")) {
                    cell.setCellValue(item.getValue().tenantNameProperty().get());
                } else if (ttc.getText().equals("联系电话")) {
                    cell.setCellValue(item.getValue().phoneNoProperty().get());
                } else if (ttc.getText().equals("优惠金额")) {
                    cell.setCellValue(item.getValue().discountAmountProperty().get());
                } else if (ttc.getText().equals("优惠原因")) {
                    cell.setCellValue(item.getValue().discountReasonProperty().get());
                } else if (ttc.getText().equals("应收金额")) {
                    cell.setCellValue(item.getValue().receivableAmountProperty().get());
                } else if (ttc.getText().equals("实际支付")) {
                    cell.setCellValue(item.getValue().actuallyPayProperty().get());
                } else if (ttc.getText().equals("已收金额")) {
                    cell.setCellValue(item.getValue().amountReceivedProperty().get());
                } else if (ttc.getText().equals("尚欠金额")) {
                    cell.setCellValue(item.getValue().owingAmountProperty().get());
                } else if (ttc.getText().equals("结算日期")) {
                    cell.setCellValue(item.getValue().settlementDateProperty().get());
                } else if (ttc.getText().equals("支付方式")) {
                    cell.setCellValue(item.getValue().paymentProperty().get());
                } else if (ttc.getText().equals("结算备注")) {
                    cell.setCellValue(item.getValue().settlementNotesProperty().get());
                } else if (ttc.getText().equals("结算状态")) {
                    cell.setCellValue(item.getValue().settlementStateProperty().get());
                } else if (ttc.getText().equals("联系人")) {
                    cell.setCellValue(item.getValue().contactsProperty().get());
                } else if (ttc.getText().equals("备注")) {
                    cell.setCellValue(item.getValue().notesProperty().get());
                }
                // Auto-size the columns.
    //            sheet.autoSizeColumn(c);
                c++;
            }
            rn++;
            
            ObservableList<TreeItem<Rent>> subItems = item.getChildren();
            for(TreeItem<Rent> subItem : subItems){
                Row r2 = sheet.createRow(rn);
                int c1 = 0;
                for (TreeTableColumn ttc : lstCols) {
                    Cell cell = r2.createCell(c1);
                    cell.setCellStyle(style);
                    if (ttc.getText().equals("车牌号")) {
                        cell.setCellValue(subItem.getValue().licensePlateNumberProperty().get());
                    } else if (ttc.getText().equals("烤漆部位")) {
                        cell.setCellValue(subItem.getValue().paintPartsProperty().get());
                    } else if (ttc.getText().equals("备注")) {
                        cell.setCellValue(subItem.getValue().notesProperty().get());
                    }
                    c1++;
                }
                rn++;
            }
        }
        
        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(outputFileFullPath);
        wb.write(fileOut);
        fileOut.close();
    }
    
    public void exportItems(TableView tableView, String outputFileFullPath) throws FileNotFoundException, IOException {
        Workbook wb = new HSSFWorkbook();
        CellStyle style = createBorderStyle(wb);
        CellStyle style2 = createBorderAndFontStyle(wb);
        Sheet sheet = wb.createSheet("商品");
        // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet.createRow(0);
        
        // insert columns
        ObservableList<TableColumn> lstCols = tableView.getColumns();
        int col = 0;
        for (TableColumn tc : lstCols) {
            // Create a cell and put a date value in it.  The first cell is not styled
            // as a date.
            Cell colCell = row.createCell(col);
            colCell.setCellStyle(style2);
            colCell.setCellValue(tc.getText());
            col++;
        }
        
        // insert data
        ObservableList<ItemModel> data = tableView.getItems();
        int rn = 1;
        for (ItemModel item : data) {
            Row r1 = sheet.createRow(rn);
            int c = 0;
            for (TableColumn tc : lstCols) {
                Cell cell = r1.createCell(c);
                cell.setCellStyle(style);
                if (tc.getText().equals("序号")) {
                    cell.setCellValue(item.getNo());
                } else if (tc.getText().equals("商品名称")) {
                    cell.setCellValue(item.getName());
                } else if (tc.getText().equals("成本价")) {
                    cell.setCellValue(item.getCostPrice().toString());
                } else if (tc.getText().equals("销售单价")) {
                    cell.setCellValue(item.getSalesPrice().toString());
                } else if (tc.getText().equals("备注")) {
                    cell.setCellValue(item.getNotes());
                } else if (tc.getText().equals("标签名")) {
                    cell.setCellValue(item.getTagName());
                } 
                c++;
            }
            rn++;
        }
        
        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(outputFileFullPath);
        wb.write(fileOut);
        fileOut.close();
    }
    
    public void exportCustomers(TableView tableView, String outputFileFullPath) throws FileNotFoundException, IOException {
        Workbook wb = new HSSFWorkbook();
        CellStyle style = createBorderStyle(wb);
        CellStyle style2 = createBorderAndFontStyle(wb);
        Sheet sheet = wb.createSheet("客户");

        // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet.createRow(0);
        
        // insert columns
        ObservableList<TableColumn> lstCols = tableView.getColumns();
        int col = 0;
        for (TableColumn tc : lstCols) {
            // Create a cell and put a date value in it.  The first cell is not styled
            // as a date.
            Cell colCell = row.createCell(col);
            colCell.setCellStyle(style2);
            colCell.setCellValue(tc.getText());
            col++;
        }
        
        // insert data
        ObservableList<CustomerModel> data = tableView.getItems();
        int rn = 1;
        for (CustomerModel item : data) {
            Row r1 = sheet.createRow(rn);
            int c = 0;
            for (TableColumn tc : lstCols) {
                Cell cell = r1.createCell(c);
                cell.setCellStyle(style);
                if (tc.getText().equals("序号")) {
                    cell.setCellValue(item.getNo());
                } else if (tc.getText().equals("客户名称")) {
                    cell.setCellValue(item.getName());
                } else if (tc.getText().equals("联系手机")) {
                    cell.setCellValue(item.getPhoneNo());
                } else if (tc.getText().equals("客户地址")) {
                    cell.setCellValue(item.getAddress());
                } else if (tc.getText().equals("客户备注")) {
                    cell.setCellValue(item.getNotes());
                } else if (tc.getText().equals("车辆备注")) {
                    cell.setCellValue(item.getCarNotes());
                } else if (tc.getText().equals("最新公里数")) {
                    cell.setCellValue(item.getLatestMileage());
                } else if (tc.getText().equals("号码号牌")) {
                    cell.setCellValue(item.getLicensePlateNumber());
                } else if (tc.getText().equals("厂牌型号")) {
                    cell.setCellValue(item.getFactoryPlateModel());
                } else if (tc.getText().equals("VIN码")) {
                    cell.setCellValue(item.getVinCode());
                } else if (tc.getText().equals("机动车种类名称")) {
                    cell.setCellValue(item.getVehicleTypeName());
                } else if (tc.getText().equals("发动机号")) {
                    cell.setCellValue(item.getEngineNo());
                } else if (tc.getText().equals("登记日期")) {
                    cell.setCellValue(item.getRegistrationDate().toString());
                } else if (tc.getText().equals("车型")) {
                    cell.setCellValue(item.getModel());
                } else if (tc.getText().equals("颜色")) {
                    cell.setCellValue(item.getColor());
                } else if (tc.getText().equals("下次保养里程")) {
                    cell.setCellValue(item.getNextMaintenanceMilage());
                } else if (tc.getText().equals("下次保养时间")) {
                    cell.setCellValue(item.getNextMaintenanceDate().toString());
                } else if (tc.getText().equals("下次回访时间")) {
                    cell.setCellValue(item.getNextVisitDate().toString());
                } else if (tc.getText().equals("下次年审时间")) {
                    cell.setCellValue(item.getNextAnnualReviewDate().toString());
                } else if (tc.getText().equals("保险月/日")) {
                    cell.setCellValue(item.getInsuranceMonthDay());
                } else if (tc.getText().equals("检车月份")) {
                    cell.setCellValue(item.getInspectionMonth());
                } else if (tc.getText().equals("保险公司")) {
                    cell.setCellValue(item.getInsuranceCompany());
                } else if (tc.getText().equals("保险备注")) {
                    cell.setCellValue(item.getInsuranceNotes());
                }
                c++;
            }
            rn++;
        }
        
        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(outputFileFullPath);
        wb.write(fileOut);
        fileOut.close();
    }
    
    public void exportSettlements(TreeTableView treeTableView, String outputFileFullPath) throws FileNotFoundException, IOException {
        Workbook wb = new HSSFWorkbook();
        CellStyle style = createBorderStyle(wb);
        CellStyle style2 = createBorderAndFontStyle(wb);
        Sheet sheet = wb.createSheet("结算单");

        // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet.createRow(0);
        
        // insert columns
        ObservableList<TreeTableColumn> lstCols = treeTableView.getColumns();
        int col = 0;
        for (TreeTableColumn ttc : lstCols) {
            // Create a cell and put a date value in it.  The first cell is not styled
            // as a date.
            Cell colCell = row.createCell(col);
            colCell.setCellStyle(style2);
            colCell.setCellValue(ttc.getText());
            col++;
        }
        // insert data
        ObservableList<TreeItem<Inventory>> data = treeTableView.getRoot().getChildren();//
        int rn = 1;
        for (TreeItem<Inventory> item : data) {
            Row r1 = sheet.createRow(rn);
            setRowValues(item, lstCols, r1, style);
            rn++;
            if (item.getValue().billingObjectProperty().get().equals(Constants.OBJ_2)) {

                ObservableList<TreeItem<Inventory>> subItems = item.getChildren();
                for(TreeItem<Inventory> subItem : subItems){
                    Row r2 = sheet.createRow(rn);
                    setRowValues(subItem, lstCols, r2, style);
                    rn++;
                }
            }
        }
        
        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(outputFileFullPath);
        wb.write(fileOut);
        fileOut.close();
    }
    
    private void setRowValues(TreeItem<Inventory> item, ObservableList<TreeTableColumn> lstCols, Row row, CellStyle style) {
        int c = 0;
        for (TreeTableColumn ttc : lstCols) {
            Cell cell = row.createCell(c);
            cell.setCellStyle(style);
            if (ttc.getText().equals("单号")) {
                cell.setCellValue(item.getValue().SNProperty().get());
            } else if (ttc.getText().equals("开单日期")) {
                cell.setCellValue(item.getValue().billingDateProperty().get());
            } else if (ttc.getText().equals("客户名")) {
                cell.setCellValue(item.getValue().clientNameProperty().get());
            } else if (ttc.getText().equals("客户电话")) {
                cell.setCellValue(item.getValue().phoneNoProperty().get());
            } else if (ttc.getText().equals("车牌号")) {
                cell.setCellValue(item.getValue().licensePlateNumberProperty().get());
            } else if (ttc.getText().equals("优惠金额")) {
                cell.setCellValue(item.getValue().discountAmountProperty().get());
            } else if (ttc.getText().equals("优惠原因")) {
                cell.setCellValue(item.getValue().discountReasonProperty().get());
            } else if (ttc.getText().equals("应收金额")) {
                cell.setCellValue(item.getValue().receivableAmountProperty().get());
            } else if (ttc.getText().equals("实际支付")) {
                cell.setCellValue(item.getValue().actuallyPayProperty().get());
            } else if (ttc.getText().equals("已收金额")) {
                cell.setCellValue(item.getValue().amountReceivedProperty().get());
            } else if (ttc.getText().equals("尚欠金额")) {
                cell.setCellValue(item.getValue().owingAmountProperty().get());
            } else if (ttc.getText().equals("结算日期")) {
                cell.setCellValue(item.getValue().settlementDateProperty().get());
            } else if (ttc.getText().equals("支付方式")) {
                cell.setCellValue(item.getValue().paymentProperty().get());
            } else if (ttc.getText().equals("结算备注")) {
                cell.setCellValue(item.getValue().settlementNotesProperty().get());
            } else if (ttc.getText().equals("业务状态")) {
                cell.setCellValue(item.getValue().businessStateProperty().get());
            } else if (ttc.getText().equals("结算状态")) {
                cell.setCellValue(item.getValue().settlementStateProperty().get());
            } else if (ttc.getText().equals("开单对象")) {
                cell.setCellValue(item.getValue().billingObjectProperty().get());
            } else if (ttc.getText().equals("联系人")) {
                cell.setCellValue(item.getValue().contactsProperty().get());
            } else if (ttc.getText().equals("备注")) {
                cell.setCellValue(item.getValue().notesProperty().get());
            }
            // Auto-size the columns.
//            sheet.autoSizeColumn(c);
            c++;
        }
    }
    
    public static Excel getInstance() {
        if (singleton == null) {
            singleton = new Excel();
        }
        return singleton;
    }
    
    private static Excel singleton;
}
