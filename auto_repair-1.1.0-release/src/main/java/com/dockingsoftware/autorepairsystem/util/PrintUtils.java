/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.util;

public class PrintUtils {

    /**
     * Prints some objects.
     * 
     * @param args 
     */
    public static void P(Object... args) {
        p(args);
    }

    /**
     * Prints some objects by blank space separated.
     * 
     * @param args 
     */
    public static void p(Object... args) {
        for (Object arg : args) {
            System.out.print(arg + " ");
        }
        System.out.println();
    }
}
