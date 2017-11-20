/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bootcamp.rest.application;

import com.bootcamp.rest.controllers.IndicateurRestController;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import java.net.URISyntaxException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author root
 */
public class MyServer {
    
     private static final Logger LOG = (Logger) LoggerFactory.getLogger(MyServer.class);

    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) throws URISyntaxException {

        try {
            // Workaround for resources from JAR files
            Resource.setDefaultUseCaches( false );

            // Build the Swagger Bean.
            buildSwagger();

            // Holds handlers
            final HandlerList handlers = new HandlerList();

            // Handler for Swagger UI, static handler.
            handlers.addHandler( buildSwaggerUI() );

            // Handler for Entity Browser and Swagger
            handlers.addHandler( buildContext() );

            // Start server
            Server server = new Server( SERVER_PORT );
            server.setHandler( handlers );
            server.start();
            server.join();
        } catch ( Exception e ) {
            LOG.error( "There was an error starting up the WS Programme ", e );
        }

    }

    private static void buildSwagger() {
          // This configures Swagger
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setResourcePackage(IndicateurRestController.class.getPackage().getName());        //"com.bootcamp.rest.controllers"
        beanConfig.setScan(true);
        beanConfig.setBasePath("/");
        beanConfig.setDescription("Programme Rest API to access all the programs ressources");
        beanConfig.setTitle("Bailleurs");
       System.out.println(IndicateurRestController.class.getPackage().getName() );
    }

    // This starts the Swagger UI at http://localhost:9090/docs
    private static ContextHandler buildSwaggerUI() throws URISyntaxException {
        //to configure swagger UI
       
          final ResourceHandler swaggerUIResourceHandler = new ResourceHandler();
        swaggerUIResourceHandler.setResourceBase( MyServer.class.getClassLoader().getResource("webapp" ).toURI().toString() );
        final ContextHandler swaggerUIContext = new ContextHandler();
        swaggerUIContext.setContextPath( "/docs/" );
        swaggerUIContext.setHandler( swaggerUIResourceHandler );

        return swaggerUIContext;
    }

    private static ContextHandler buildContext() {

        
         ResourceConfig resourceConfig = new ResourceConfig();
        // io.swagger.jaxrs.listing loads up Swagger resources
        resourceConfig.packages( IndicateurRestController.class.getPackage().getName(), ApiListingResource.class.getPackage().getName() );
        ServletContainer servletContainer = new ServletContainer( resourceConfig );
        ServletHolder holder = new ServletHolder( servletContainer );
        ServletContextHandler context = new ServletContextHandler( ServletContextHandler.SESSIONS );
        context.setContextPath("/");
        context.addServlet(holder,"/*");
        return context;
    }
    
//    public void startServer(){
//      
//        try {
//            // Workaround for resources from JAR files
//            Resource.setDefaultUseCaches( false );
//
//            // Build the Swagger Bean.
//            buildSwagger();
//
//            // Holds handlers
//            final HandlerList handlers = new HandlerList();
//
//            // Handler for Swagger UI, static handler.
//            handlers.addHandler( buildSwaggerUI() );
//
//            // Handler for Entity Browser and Swagger
//            handlers.addHandler( buildContext() );
//
//            // Start server
//            Server server = new Server( SERVER_PORT );
//            server.setHandler( handlers );
//            server.start();
//            server.join();
//        } catch ( Exception e ) {
//            LOG.error( "There was an error starting up the WS Programme ", e );
//        }      
//    }
}
