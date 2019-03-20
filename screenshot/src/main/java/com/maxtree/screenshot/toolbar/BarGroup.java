package com.maxtree.screenshot.toolbar;

import java.util.ArrayList;
import java.util.List;

public class BarGroup {
	
	/**
     * Adds the button to the group.
     * @param b the button to be added
     */
    public void add(ToolBarIF b) {
        if(b == null) {
            return;
        }
        bars.add(b);
        b.setGroup(this);
    }
    
    /**
     * Add all buttons to the group.
     * @param b the buttons to be added
     */
    public void addAll(ToolBarIF... bs) {
        if(bs == null) {
            return;
        }
        for (ToolBarIF b : bs) {
        	bars.add(b);
            b.setGroup(this);
        }
    }
    
    /**
     * Removes the button from the group.
     * @param b the button to be removed
     */
    public void remove(ToolBarIF b) {
        if(b == null) {
            return;
        }
        bars.remove(b);
        b.setGroup(null);
    }
    
    public List<ToolBarIF> getBars() {
    	return bars;
    }
    
    private List<ToolBarIF> bars = new ArrayList<ToolBarIF>();
	public ToolBarIF selection;
}
