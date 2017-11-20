/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bootcamp.rest;

import com.bootcamp.rest.controllers.IndicateurRestController;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author root
 */
@ApplicationPath("/rest")
public class SwaggerResourcesLoader extends Application{
    /**
    * configuration pour swagger
    * du fait de l'utilisation d'une sous classe Application
    * differente de la solution qui configure le web xml
     * @return 
    */
    
     @Override
    public Set<Class<?>> getClasses() {

        Set<Class<?>> resources=new HashSet<>();
        resources.add(IndicateurRestController.class); 
        return resources;

       }
}
