    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.component.NumericTextField;
import com.dockingsoftware.autorepairsystem.persistence.entity.IAE;
import com.dockingsoftware.autorepairsystem.persistence.entity.Payment;
import com.dockingsoftware.autorepairsystem.persistence.entity.Subject;
import com.dockingsoftware.autorepairsystem.ui.controller.AddOrUpdateIAEDialogController;
import com.dockingsoftware.autorepairsystem.util.DateUtils;
import com.dockingsoftware.autorepairsystem.util.ImageUtils;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class AddOrUpdateIAEDialog extends Dialog {

    /**
     * Constructor.
     */
    public AddOrUpdateIAEDialog() {
        setTitle("添加收支");
        setHeaderText("请填写记账明细。");
        initComponents();
    }
    
    /**
     * Constructor.
     * 
     * @param editIAE
     */
    public AddOrUpdateIAEDialog(IAE editIAE) {
        this.editIAE = editIAE;
        setTitle("收支编辑");
        setHeaderText("请填写记账明细。");
        initComponents();
        
        if (editIAE != null) {
            incomeField.setText(editIAE.getIncomeAmount().toString());
            subjectField.setValue(editIAE.getSubject());
            expenseField.setText(editIAE.getExpenseAmount().toString());
            paymentDateField.setValue(DateUtils.Date2LocalDate(editIAE.getPaymentDate()));
            handlerNameField.setText(editIAE.getHandlerName());
            paymentField.setValue(editIAE.getPayment());
            summaryField.setText(editIAE.getSummary());
        }
    }

    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("收入:"), 0, 0);
        incomeField.setPromptText("收入");
        grid.add(incomeField, 1, 0);
        
        grid.add(new Label("科目:"), 2, 0);
        List<Subject> lstSubject = (List<Subject>) controller.listSubject.call(null);
        for (Subject s : lstSubject) {
            subjectField.getItems().add(s.getName());
        }
        subjectField.setEditable(true);
        subjectField.setPromptText("科目");
        subjectField.setPrefWidth(173);
        grid.add(subjectField, 3, 0);

        grid.add(btnEdit, 4, 0);
        
        grid.add(new Label("支出:"), 0, 1);
        expenseField.setPromptText("支出");
        grid.add(expenseField, 1, 1);
 
        grid.add(new Label("支付日期:"), 2, 1);
        paymentDateField.setEditable(false);
        paymentDateField.setValue(LocalDate.now());
        paymentDateField.setPromptText("支付日期");
        grid.add(paymentDateField, 3, 1);
        
        grid.add(new Label("经手人:"), 0, 2);
        handlerNameField.setPromptText("经手人");
        grid.add(handlerNameField, 1, 2);
        
        grid.add(new Label("支付方式:"), 2, 2);
        List<Payment> lstPayment = (List<Payment>) controller.listPayment.call("");
        for (Payment p : lstPayment) {
            paymentField.getItems().add(p.getName());
        }
        paymentField.getSelectionModel().select(0);
        paymentField.setPrefWidth(173);
        grid.add(paymentField, 3, 2);
        
        grid.add(new Label("摘要:"), 0, 3);
        summaryField.setPromptText("摘要");
        grid.add(summaryField, 1, 3, 3, 1);
        
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        okButton.setText("确定");
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("取消");
        // Do some validation (using the Java 8 lambda syntax).
        subjectField.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });

        btnEdit.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                ChooseSubjectDialog dia = new ChooseSubjectDialog();
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.returnSubject(s -> setSubjectField(s)));
            }
        });

        getDialogPane().setContent(grid);
        
    }

    private void setSubjectField(Subject subject) {
        subjectField.getItems().clear();;
        List<Subject> lstSubject = (List<Subject>) controller.listSubject.call(null);
        for (Subject s : lstSubject) {
            subjectField.getItems().add(s.getName());
        }
        subjectField.getSelectionModel().select(subject.getName());
    }
    
    public void saveOrUpdateIAE(Consumer<IAE> consumer) {
        if (editIAE == null) {
            editIAE = new IAE();
        }
        editIAE.setSubject(subjectField.getEditor().getText());
        editIAE.setIncomeAmount(incomeField.getText().isEmpty() ?  BigDecimal.ZERO : new BigDecimal(incomeField.getText()));
        editIAE.setExpenseAmount(expenseField.getText().isEmpty() ?  BigDecimal.ZERO : new BigDecimal(expenseField.getText()));
        editIAE.setHandlerName(handlerNameField.getText());
        editIAE.setPayment(paymentField.getValue());
        editIAE.setPaymentDate(DateUtils.LocalDate2Date(paymentDateField.getValue()));
        editIAE.setSummary(summaryField.getText());
        
        controller.saveOrUpdateIAE.call(editIAE);
        
        Criterion c = Restrictions.eq("name", editIAE.getSubject());
        List<Subject> lstSubject = (List<Subject>) controller.listSubject.call(c);
        if (lstSubject.isEmpty()) {
            Subject newSubject = new Subject();
            newSubject.setName(editIAE.getSubject());
            controller.saveOrUpdateSubject.call(newSubject);
        }
        
        consumer.accept(editIAE);
    }

    private Button btnEdit = new Button("", ImageUtils.createImageView("edit_16px_505013_easyicon.net.png"));
    private ComboBox<String> subjectField = new ComboBox<String>();
    private NumericTextField incomeField = new NumericTextField();
    private NumericTextField expenseField = new NumericTextField();
    private DatePicker paymentDateField = new DatePicker();
    private TextField handlerNameField = new TextField();
    private ComboBox<String> paymentField = new ComboBox<String>();
    private TextField summaryField = new TextField();
    
    private IAE editIAE;
    private AddOrUpdateIAEDialogController controller = new AddOrUpdateIAEDialogController();
}
