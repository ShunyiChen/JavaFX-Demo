package com.maxtree.screenshot.toolbar;

import java.util.ArrayList;
import java.util.List;

public class ButtonGroup {

	/**
     * Adds the button to the group.
     * @param b the button to be added
     */
    public void add(ToggleIconButton b) {
        if(b == null) {
            return;
        }
        buttons.add(b);
        b.setGroup(this);
    }
    
    /**
     * Add all buttons to the group.
     * @param b the buttons to be added
     */
    public void addAll(ToggleIconButton... bs) {
        if(bs == null) {
            return;
        }
        for (ToggleIconButton b : bs) {
        	buttons.add(b);
            b.setGroup(this);
        }
    }
    
    /**
     * Removes the button from the group.
     * @param b the button to be removed
     */
    public void remove(ToggleIconButton b) {
        if(b == null) {
            return;
        }
        buttons.remove(b);
        b.setGroup(null);
    }
    
    public List<ToggleIconButton> getButtons() {
    	return buttons;
    }
    
    public void clearSelection() {
    	for (ToggleIconButton b : buttons) {
    		b.setSelected(false);
    	}
    }
    
    private List<ToggleIconButton> buttons = new ArrayList<ToggleIconButton>();
}
