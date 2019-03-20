/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.component;

import com.dockingsoftware.autorepairsystem.util.DateUtils;
import java.time.LocalDate;
import javafx.util.StringConverter;

public class StringConverterExt extends StringConverter<LocalDate> {

//    public StringConverterExt(int a) {}
    
    @Override
    public String toString(LocalDate object) {
        return DateUtils.LocalDate2String(object);
    }

    @Override
    public LocalDate fromString(String string) {
        return DateUtils.String2LocalDate(string);
    }

}
