/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.developer;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.component.MessageBox;
import com.dockingsoftware.autorepairsystem.persistence.entity.User;
import com.dockingsoftware.autorepairsystem.ui.controller.ChangePasswordDialogController;
import com.dockingsoftware.autorepairsystem.util.DateUtils;
import com.dockingsoftware.autorepairsystem.util.MD5Utils;
import static com.dockingsoftware.autorepairsystem.util.PrintUtils.p;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class SourceGenerator extends Dialog {

    public SourceGenerator() {
        initComponents();
    }

    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());

        setTitle("代码生成器");
        setHeaderText("代码生成器。");

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("生成");
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("取消");

        // Do some validation (using the Java 8 lambda syntax).
        entitiesArea.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });
        VBox vbox = new VBox();
        vbox.getChildren().addAll(createGridPane());
        getDialogPane().setContent(vbox);
    }

    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));
        
        grid.add(new Label("实体类:"), 0, 0);
        entitiesArea.setText("com.dockingsoftware.autorepairsystem.persistence.entity.Statement;");
        grid.add(entitiesArea, 1, 0);

        grid.add(new Label("生成类名:"), 0, 1);
        classname.setText("Hello");
        grid.add(classname, 1, 1);
        
        generateModelClass.setSelected(true);
        grid.add(generateModelClass, 0, 2);
        
        generateTabContentClass.setSelected(true);
        grid.add(generateTabContentClass, 0, 3);
        
        return grid;
    }
    
    public void generate() {
        if (classname.getText().isEmpty()) {
            classname.setText("A_"+System.currentTimeMillis()+"");
        }
        if (generateModelClass.isSelected()) {
            generateModelClass(classname.getText()+"Model");
            MessageBox.showMessage("代码已生成成功。");
        }
        if (generateTabContentClass.isSelected()) {
            generateTabContentClass(classname.getText()+"TabContent");
            MessageBox.showMessage("代码已生成成功。");
        }
    }
    
    private void generateModelClass(String modelName) {
        try{
            StringBuilder data = new StringBuilder();
            data.append("package com.dockingsoftware.autorepairsystem.component.model;\n\n");
            data.append("import java.math.BigDecimal;\n");
            data.append("import java.time.LocalDate;\n");
            data.append("import java.util.Date;\n");
            data.append("import java.util.List;\n");
            data.append("import javafx.beans.property.SimpleBooleanProperty;\n");
            data.append("import javafx.beans.property.BooleanProperty;\n\n");
            data.append("public class "+modelName+" {\n\n");
            
            List<Field> allFields = getFieldsByEntities();
            // 生成属性
            data.append(T + "private BooleanProperty vegetarian = new SimpleBooleanProperty(false);\n");
            data.append(T + "private String No;\n");
            
            for (Field f : allFields) {
                data.append(T + "private "+f.getType().getSimpleName()+" "+f.getName()+";\n");
            }
            data.append("\n\n");
            
            // 生成set/get方法
            data.append(T + "public void setVegetarian(boolean vegetarian) {\n");
            data.append(T + T + "this.vegetarian.set(vegetarian);\n");
            data.append("}\n\n");
            
            data.append(T + "public boolean isVegetarian() {\n");
            data.append(T + T + "return vegetarian.get();\n");
            data.append("}\n\n");
            
            data.append(T + "public String getNo() {\n");
            data.append(T + T + "return No;\n");
            data.append("}\n\n");
            
            data.append(T + "public void setNo(String No) {\n");
            data.append(T + T + "this.No = No;\n");
            data.append("}\n\n");
            
            
            for (Field f : allFields) {
                String name = f.getName();
                // 首字母变大写
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                
                // Get 方法
                data.append(T + "public "+f.getType().getSimpleName()+" get"+name+"() {\n");
                data.append(T + T +"return "+f.getName()+";\n");
                data.append(T + "}\n\n");
                
                // Set 方法
                data.append(T + "public void set"+name+"("+f.getType().getSimpleName()+" "+f.getName()+") {\n");
                data.append(T + T + "this."+f.getName()+" = "+f.getName()+";\n");
                data.append(T + "}\n\n");
            }
            
            data.append("}\n");
            
            String outputPath = "C:\\Users\\chens\\Documents\\NetBeansProjects\\AutoRepairSystem\\src\\main\\java\\com\\dockingsoftware\\autorepairsystem\\component\\model";
            File modelClassFile = new File(outputPath + "\\" + modelName+".java");

            //if file doesnt exists, then create it
            if (!modelClassFile.exists()) {
                modelClassFile.createNewFile();
            }
            
            write(modelClassFile, data.toString());
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SourceGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void generateTabContentClass(String tabContentName) {
        try {
            StringBuilder data = new StringBuilder();
            data.append("package com.dockingsoftware.autorepairsystem.ui;\n\n");
            data.append("import com.dockingsoftware.autorepairsystem.MainApp;\n");
            data.append("import com.dockingsoftware.autorepairsystem.component.BigDecimalCell;\n");
            data.append("import com.dockingsoftware.autorepairsystem.component.DatePickerCell;\n");
            data.append("import com.dockingsoftware.autorepairsystem.component.EditingCell;\n");
            data.append("import com.dockingsoftware.autorepairsystem.component.MessageBox;\n");
            data.append("import com.dockingsoftware.autorepairsystem.component.model.TenantModel;\n");
            data.append("import com.dockingsoftware.autorepairsystem.persistence.entity.Tenant;\n");
            data.append("import com.dockingsoftware.autorepairsystem.ui.controller.HiresTheHouseTabContentController;\n");
            data.append("import com.dockingsoftware.autorepairsystem.util.DateUtils;\n");
            data.append("import com.dockingsoftware.autorepairsystem.util.ImageUtils;\n");
            data.append("import java.math.BigDecimal;\n");
            data.append("import java.math.RoundingMode;\n");
            data.append("import java.time.LocalDate;\n");
            data.append("import java.util.ArrayList;\n");
            data.append("import java.util.Calendar;\n");
            data.append("import java.util.Date;\n");
            data.append("import java.util.List;\n");
            data.append("import javafx.beans.value.ChangeListener;\n");
            data.append("import javafx.beans.value.ObservableValue;\n");
            data.append("import javafx.collections.FXCollections;\n");
            data.append("import javafx.collections.ObservableList;\n");
            data.append("import javafx.event.Event;\n");
            data.append("import javafx.event.EventHandler;\n");
            data.append("import javafx.geometry.Insets;\n");
            data.append("import javafx.geometry.Pos;\n");
            data.append("import javafx.scene.control.Alert;\n");
            data.append("import javafx.scene.control.Button;\n");
            data.append("import javafx.scene.control.ButtonType;\n");
            data.append("import javafx.scene.control.ComboBox;\n");
            data.append("import javafx.scene.control.DatePicker;\n");
            data.append("import javafx.scene.control.Label;\n");
            data.append("import javafx.scene.control.Separator;\n");
            data.append("import javafx.scene.control.TableCell;\n");
            data.append("import javafx.scene.control.TableColumn;\n");
            data.append("import javafx.scene.control.TableView;\n");
            data.append("import javafx.scene.control.TextField;\n");
            data.append("import javafx.scene.control.ToolBar;\n");
            data.append("import javafx.scene.control.cell.CheckBoxTableCell;\n");
            data.append("import javafx.scene.control.cell.PropertyValueFactory;\n");
            data.append("import javafx.scene.input.MouseEvent;\n");
            data.append("import javafx.scene.layout.BorderPane;\n");
            data.append("import javafx.scene.layout.GridPane;\n");
            data.append("import javafx.scene.layout.HBox;\n");
            data.append("import javafx.scene.layout.Priority;\n");
            data.append("import javafx.scene.layout.VBox;\n");
            data.append("import javafx.util.Callback;\n");
            data.append("import org.hibernate.criterion.Criterion;\n");
            data.append("import org.hibernate.criterion.MatchMode;\n");
            data.append("import org.hibernate.criterion.Restrictions;\n");
            data.append("import org.springframework.beans.BeanUtils;\n\n");
            data.append("public class "+tabContentName+" extends VBox {;\n\n");
            
            data.append(T + "public "+tabContentName+"() {\n");
            data.append(T2 + "initComponents();\n");
            data.append(T + "}\n\n");
            
            data.append(T + "private void initComponents() {\n");
            data.append(T2 + "this.getChildren().addAll(toolbar(), criteriaQueryPane(), tablePane());\n");
            data.append(T + "}\n\n");
            
            data.append(T + "private ToolBar toolbar() {\n");
            data.append(T2 + "btnAdd.setOnAction(new EventHandler() {\n");
            data.append(T3 + "@Override\n");
            data.append(T3 + "public void handle(Event event) {\n");
            data.append(T3 + "}\n");
            data.append(T2 + "});\n");
            data.append(T2 + "ToolBar bar = new ToolBar();\n");
            data.append(T2 + "bar.getItems().addAll(btnAdd, new Separator());\n");
            data.append(T2 + "return bar;\n");
            data.append(T + "}\n\n");
            
            
            
            
            data.append(T + "private GridPane criteriaQueryPane() {\n\n");
            data.append(T + "GridPane grid = new GridPane();\n");
            data.append(T + "grid.setHgap(10);\n");
            data.append(T + "grid.setVgap(10);\n");
            data.append(T + "grid.setPadding(new Insets(20, 20, 10, 10));\n");
            data.append(T + "grid.add(new Label(\"租户名:\"), 0, 0);\n");
            data.append(T + "name.setPromptText(\"租户名\");\n");
            data.append(T + "grid.add(name, 1, 0);\n");
            data.append(T + "return grid;\n");
            data.append(T + "}\n\n");
            
            data.append(T + "private Button btnAdd = new Button(\"Add\");\n");
            data.append(T + "private TextField name = new TextField();\n\n");
            
            
   
            
            data.append("}\n");
            
            String outputPath = "C:\\Users\\chens\\Documents\\NetBeansProjects\\AutoRepairSystem\\src\\main\\java\\com\\dockingsoftware\\autorepairsystem\\ui";
            File f = new File(outputPath + "\\" + tabContentName+".java");
            
            //if file doesnt exists, then create it
            if (!f.exists()) {
                f.createNewFile();
            }
            write(f, data.toString());
        } catch (IOException ex) {
            Logger.getLogger(SourceGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private List<Field> getFieldsByEntities() throws ClassNotFoundException {
        List<Field> allFields = new ArrayList<Field>();
        String text = entitiesArea.getText().replaceAll("\n", "").trim();
        String[] entityFullNames = text.split(";");
        for (String entityFullName : entityFullNames) {
            Class c = Class.forName(entityFullName);
            Field[] fields = c.getDeclaredFields();
            for (Field f : fields) {
                allFields.add(f);
            }
        }
        return allFields;
    }
    
    private void write(File f, String data) throws IOException {
        FileWriter fw = new FileWriter(f.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(data);
        bw.close();

        System.out.println("Done");
    }
    
    private TextArea entitiesArea = new TextArea();
    private CheckBox generateModelClass = new CheckBox("生成Model类");
    private CheckBox generateTabContentClass = new CheckBox("生成TabContent类");
    private TextField classname = new TextField();
    private String T = "    ";
    private String T2 = "        ";
    private String T3 = "            ";
}
