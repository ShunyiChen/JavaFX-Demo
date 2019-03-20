/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.component;

import com.dockingsoftware.autorepairsystem.util.ImageUtils;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.TreeItem;

public class TreeItemExt extends TreeItem {

    /**
     * Constructor.
     * 
     * @param value
     * @param imageName 
     */
    public TreeItemExt(String value, String imageName) {
        super(value, ImageUtils.createImageView(imageName));
        properties.put(ATTR_IMAGE_KEY, imageName);
    }

    public Map<String, Object> getProperties() {
        return properties;
    }
    
    private Map<String, Object> properties = new HashMap<String, Object>();
    
    // 属性key
    public static final String ATTR_TAB_ID = "ATTR_TAB_ID";
    public static final String ATTR_IMAGE_KEY = "ATTR_IMAGE_KEY";
    public static final String ATTR_ITEM_TAG_KEY = "ATTR_ITEM_TAG_KEY";
}
