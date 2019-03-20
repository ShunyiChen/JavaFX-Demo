/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

public class Log4j2Initializer {
    
    public void init() {
        try {
            InputStream istream = new FileInputStream(new File("res/log4j2/log4j2.xml"));
            ConfigurationSource source = new ConfigurationSource(istream); 
            Configurator.initialize(null, source);
        } catch (IOException ex) {
        }
    }
    
}
