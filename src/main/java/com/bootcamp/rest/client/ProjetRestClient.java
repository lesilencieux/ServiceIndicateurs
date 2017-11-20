/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bootcamp.rest.client;

import com.bootcamp.entities.Projet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.ws.rs.core.Response;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class ProjetRestClient {
    private String SERVICE_URL ="http://localhost:8080/bootcamp/rest/projet" ;
    private DefaultHttpClient httpClient;
    private String FIND_ALL_URL="/list";
    private String CREATE="/create";

    public ProjetRestClient() {
        httpClient = new DefaultHttpClient();
    }
    
    // client pour utiliser le findAll   List<Projet>
    public void findAll() throws IOException{
        String uri = SERVICE_URL + FIND_ALL_URL;
        
        // pour les requetes de type get
        HttpGet getRequest = new HttpGet(uri);

        getRequest.addHeader("accept", "application/json");
         // executer la requetes
         try {
                HttpResponse response = httpClient.execute(getRequest);
                if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
               + response.getStatusLine().getStatusCode());
              
        }
             parseResppnse(response);
        } catch (Exception e) {
        }
    }
    
    // pour utiliser le create
    public void create() throws UnsupportedEncodingException, IOException{
        String uri = SERVICE_URL + CREATE;
        
        HttpPost postRequest = new HttpPost(uri);
       Projet pro = new Projet();
       pro.setNom("le notre");
        
        //postRequest.setEntity((HttpEntity) ben);
       StringEntity input = new StringEntity(pro.toString());
//        StringEntity input = new StringEntity()
//        StringEntity input = new StringEntity("{\"nom\":\"leNotre\"}");
        
//        input.setContentType("application/json");
        postRequest.setEntity(input);    

        HttpResponse response = httpClient.execute(postRequest);
    }
    
    private void parseResppnse(HttpResponse response) throws IOException{
         BufferedReader br = new BufferedReader(
    new InputStreamReader((response.getEntity().getContent())));
        String output;

        System.out.println("Output from Server .... \n");

        while ((output = br.readLine()) != null) {

            System.out.println(output);

           

        }
    }
   
}
