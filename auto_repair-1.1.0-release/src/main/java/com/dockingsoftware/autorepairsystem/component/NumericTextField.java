/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dockingsoftware.autorepairsystem.component;

import javafx.scene.control.TextField;

public class NumericTextField extends TextField {

    public NumericTextField() {
        this("0");
    }
    
    public NumericTextField(String text) {
        super(text);
    }
    
    @Override
    public void replaceText(int start, int end, String text) {
        String preText = getText(0, start);
        String afterText = getText(end, getLength());
        String toBeEnteredText = preText + text + afterText;

        // Treat the case where the user inputs proper text and is not inputting backspaces or other control characters
        // which would be represented by an empty text argument:
        if (!text.isEmpty() && text.matches("\\d|\\.")) {
            try {
                Double.parseDouble(toBeEnteredText);
                super.replaceText(start, end, text);
            } catch (Exception ignored) {
            }
        }

        // If the user used backspace or del, the result text is impossible to not parse as a Double/Integer so just
        // enter that text right ahead:
        if (text.isEmpty()) {
            super.replaceText(start, end, text);
        }
    }
}
