/**
 * Copyright CodeJava.net To Present
 * All rights reserved.
 */
package com.dockingsoftware.autorepairsystem.config;

public class AppInitializer {

    /**
     * Startup
     */
    public void onStartup() {
        
        // Log4j2 initializer
        new Log4j2Initializer().init();
        // System initializer
        new SystemInitializer().init();
    }
}
