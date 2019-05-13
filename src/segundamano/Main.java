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
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
        // Carga el view
        
        //Parent root = fxmlLoader.load();
        
        StackPane rootMain = new StackPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ViewProducto.fxml"));
        Pane rootProductosView = fxmlLoader.load();
        rootMain.getChildren().add(rootProductosView);
        // Carga del EntityManager
        emf = Persistence.createEntityManagerFactory("SegundaManoPU");
        em = emf.createEntityManager();
        
        // Controlador del view
        ViewProductoController productoController = (ViewProductoController) fxmlLoader.getController();  
        productoController.setEntityManager(em);
        
        // Carga los productos en la tabla
        productoController.cargarTodosProductos();
        
        // Escena
        Scene scene = new Scene(rootMain, 720, 540);
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
