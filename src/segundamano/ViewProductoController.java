/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segundamano;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    
    @FXML
    private TableView<Producto> tableView;
    @FXML
    private TableColumn<Producto, Integer> columnId;
    @FXML
    private TableColumn<Producto, String> columnNombre;
    @FXML
    private TableColumn<Producto, String> columnFabrica;
    @FXML
    private TableColumn<Producto, String> columnDesc;
    @FXML
    private TableColumn<Producto, Boolean> columnNuevo;
    @FXML
    private TableColumn<Producto, Decimal> columnPrecio;
    @FXML
    private TableColumn<Producto, Decimal> columnEnvio;
    @FXML
    private TableColumn<Producto, String> columnFoto;
    @FXML
    private TableColumn<Producto, Date> columnFecha;
    @FXML
    private TableColumn<Producto, String> columnUsuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnFabrica.setCellValueFactory(new PropertyValueFactory<>("fabricante"));
        columnDesc.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnNuevo.setCellValueFactory(new PropertyValueFactory<>("nuevo"));
        columnPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnEnvio.setCellValueFactory(new PropertyValueFactory<>("envio"));
        columnFoto.setCellValueFactory(new PropertyValueFactory<>("foto"));
        columnFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        columnUsuario.setCellValueFactory(
            cellData -> {
                SimpleStringProperty property = new SimpleStringProperty();
                if (cellData.getValue().getUsuario() != null) {
                    property.setValue(cellData.getValue().getUsuario().getNombre());
                }
                return property;
            });		

    }
    
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public void cargarTodosProductos(){
        Query queryProductoTodos = entityManager.createNamedQuery("Producto.findAll");
        List<Producto> listProducto = queryProductoTodos.getResultList();
        tableView.setItems(FXCollections.observableArrayList(listProducto));
    }
    
}
