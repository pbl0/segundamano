/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segundamano;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author PC15
 */
public class Main extends Application {
    private EntityManagerFactory emf;
    private EntityManager em;
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ViewUsuario.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ViewProducto.fxml"));
        Parent root = fxmlLoader.load();
        
        emf = Persistence.createEntityManagerFactory("SegundaManoPU");
        em = emf.createEntityManager();
        
        //ViewUsuarioController viewUsuarioController = (ViewUsuarioController) fxmlLoader.getController();
        ViewProductoController productoController = (ViewProductoController) fxmlLoader.getController();  
        productoController.setEntityManager(em);
        productoController.cargarTodosProductos();
        
        Scene scene = new Scene(root, 720, 480);
        

        primaryStage.setTitle("Segunda Mano");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        em.close(); 
        emf.close(); 
    try { 
        DriverManager.getConnection("jdbc:derby:/BD;shutdown=true"); 
    } catch (SQLException ex) { 
    }        
        
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
