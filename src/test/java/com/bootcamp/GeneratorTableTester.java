/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bootcamp;

import java.util.Properties;
import javax.persistence.Persistence;
import org.testng.annotations.Test;

/**
 *
 * @author root
 */
public class GeneratorTableTester {
     @Test
    public void generateTables(){
        Persistence.generateSchema("punit-mysql", new Properties());
    }
}
