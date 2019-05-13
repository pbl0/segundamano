/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segundamano;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.derby.client.am.Decimal;

/**
 * FXML Controller class
 *
 * @author PC15
 */
public class ViewProductoController implements Initializable {
    private EntityManager entityManager;
    private Producto productoSeleccionado;
    @FXML
    private TableView<Producto> tableViewProducto;
    @FXML
    private TableColumn<Producto, String> columnNombre;
    @FXML
    private TableColumn<Producto, String> columnFabrica;
    @FXML
    private TableColumn<Producto, String> columnDesc;
    @FXML
    private TableColumn<Producto, String> columnNuevo;
    @FXML
    private TableColumn<Producto, Decimal> columnPrecio;
    @FXML
    private TableColumn<Producto, Decimal> columnEnvio;
    //@FXML
    //private TableColumn<Producto, String> columnFoto;
    @FXML
    private TableColumn<Producto, String> columnFecha;
    @FXML
    private TableColumn<Producto, String> columnUsuario;
    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldFabrica;
    @FXML
    private AnchorPane rootProductosView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnFabrica.setCellValueFactory(new PropertyValueFactory<>("fabricante"));
        columnDesc.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        //columnNuevo.setCellValueFactory(new PropertyValueFactory<>("nuevo"));
        columnPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnEnvio.setCellValueFactory(new PropertyValueFactory<>("envio"));
        columnNuevo.setCellValueFactory(
            cellData -> {
                SimpleStringProperty property = new SimpleStringProperty();
                
                if (cellData.getValue().getNuevo()){
                    property.setValue("\u2713");
                } else{
                    property.setValue("\u2717");
                }
                return property;
            }
        );
        
        columnFecha.setCellValueFactory(
            cellData -> {
                SimpleStringProperty property = new SimpleStringProperty();

                if (cellData.getValue() != null) {
                    String dateString = new SimpleDateFormat("dd/MM/yyyy").format(cellData.getValue().getFecha());
                    property.setValue(dateString);
                }
                return property;
            });
        columnUsuario.setCellValueFactory(
            cellData -> {
                SimpleStringProperty property = new SimpleStringProperty();
                if (cellData.getValue().getUsuario() != null) {
                    property.setValue(cellData.getValue().getUsuario().getNombre());
                }
                return property;
            });	
        tableViewProducto.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                productoSeleccionado = newValue;
                if (productoSeleccionado != null) {
                    textFieldNombre.setText(productoSeleccionado.getNombre());
                    textFieldFabrica.setText(productoSeleccionado.getFabricante());
                } else {
                    textFieldNombre.setText("");
                    textFieldFabrica.setText("");
                }
        });
        

    }
    
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public void cargarTodosProductos(){
        Query queryProductoTodos = entityManager.createNamedQuery("Producto.findAll");
        List<Producto> listProducto = queryProductoTodos.getResultList();
        tableViewProducto.setItems(FXCollections.observableArrayList(listProducto));
    }

    @FXML
    private void onActionButtonGuardar(ActionEvent event) {
        if (productoSeleccionado != null){
            productoSeleccionado.setNombre(textFieldNombre.getText());
            productoSeleccionado.setFabricante(textFieldFabrica.getText());
            
            entityManager.getTransaction().begin();
            entityManager.merge(productoSeleccionado);
            entityManager.getTransaction().commit();
            
            int numFilaSeleccionada = tableViewProducto.getSelectionModel().getSelectedIndex();
            tableViewProducto.getItems().set(numFilaSeleccionada, productoSeleccionado);
            
            TablePosition pos = new TablePosition(tableViewProducto, numFilaSeleccionada, null);
            tableViewProducto.getFocusModel().focus(pos);
            tableViewProducto.requestFocus();
        }
    }

    @FXML
    private void onActionButtonNuevo(ActionEvent event) {
        try {
            // Cargar la vista de detalle
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProductoDetalleView.fxml"));
            Parent rootDetalleView = fxmlLoader.load();
            
            // Ocultar la vista de la lista
            rootProductosView.setVisible(false);
            
            // Añadir la vista de detalle al StackPane principal para que se muestre
            StackPane rootMain = (StackPane)rootProductosView.getScene().getRoot();
            rootMain.getChildren().add(rootDetalleView);

            ProductoDetalleViewController productoDetalleViewController = (ProductoDetalleViewController) fxmlLoader.getController();  
            productoDetalleViewController.setRootProductosView(rootProductosView);

            productoDetalleViewController.setTableViewPrevio(tableViewProducto);

            productoSeleccionado = new Producto();
            productoDetalleViewController.setProducto(entityManager, productoSeleccionado, true);
            
            productoDetalleViewController.mostrarDatos();

        } catch (IOException ex) {
            Logger.getLogger(ViewProductoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }

    @FXML
    private void onActionButtonEditar(ActionEvent event) {
        if(productoSeleccionado != null) {
            try {
                // Cargar la vista de detalle
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProductoDetalleView.fxml"));
                Parent rootDetalleView = fxmlLoader.load();     

                ProductoDetalleViewController productoDetalleViewController = (ProductoDetalleViewController) fxmlLoader.getController();  
                productoDetalleViewController.setRootProductosView(rootProductosView);

                productoDetalleViewController.setTableViewPrevio(tableViewProducto);

                productoDetalleViewController.setProducto(entityManager, productoSeleccionado, false);

                productoDetalleViewController.mostrarDatos();


                // Ocultar la vista de la lista
                rootProductosView.setVisible(false);

                // Añadir la vista de detalle al StackPane principal para que se muestre
                StackPane rootMain = (StackPane)rootProductosView.getScene().getRoot();
                rootMain.getChildren().add(rootDetalleView);
            } catch (IOException ex) {
                Logger.getLogger(ViewProductoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Atención");
            alert.setHeaderText("Debe seleccionar un producto.");
            alert.showAndWait();
        }
    }

    @FXML
    private void onActionButtonSuprimir(ActionEvent event) {
        if(productoSeleccionado != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmar");
            alert.setHeaderText("¿Desea suprimir el siguiente registro?");
            alert.setContentText(productoSeleccionado.getNombre() + "-" + productoSeleccionado.getFabricante());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                // Acciones a realizar si el usuario acepta
                entityManager.getTransaction().begin();
                entityManager.merge(productoSeleccionado);
                entityManager.remove(productoSeleccionado);
                entityManager.getTransaction().commit();

                tableViewProducto.getItems().remove(productoSeleccionado);

                tableViewProducto.getFocusModel().focus(null);
                tableViewProducto.requestFocus();
            } else {
                // Acciones a realizar si el usuario cancela
                int numFilaSeleccionada = tableViewProducto.getSelectionModel().getSelectedIndex();
                tableViewProducto.getItems().set(numFilaSeleccionada, productoSeleccionado);
                TablePosition pos = new TablePosition(tableViewProducto, numFilaSeleccionada, null);
                tableViewProducto.getFocusModel().focus(pos);
                tableViewProducto.requestFocus();    
            }
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Atención");
            alert.setHeaderText("Debe seleccionar un producto.");
            alert.showAndWait();
        }
    }
}
