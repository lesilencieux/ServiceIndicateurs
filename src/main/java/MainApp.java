
import com.bootcamp.rest.client.ProjetRestClient;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author root
 */
public class MainApp {
    
    public static void main(String[] args) throws IOException {
        ProjetRestClient projetRestClient = new ProjetRestClient();
//        System.out.println("findAll");
//        projetRestClient.findAll();
        System.out.println("Create");
        projetRestClient.create();
    }
}
