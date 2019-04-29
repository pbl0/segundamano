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
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * FXML Controller class
 *
 * @author PC15
 */
public class ViewUsuarioController implements Initializable {
    private EntityManager entityManager;
    @FXML
    private TableView<Usuario> tableView;
    @FXML
    private TableColumn<Usuario, String> columnNombre;
    @FXML
    private TableColumn<Usuario, String> columnApellidos;
    @FXML
    private TableColumn<Usuario, String> columnEmail;
    @FXML
    private TableColumn<Usuario, String> columnTelefono;
    @FXML
    private TableColumn<Usuario, String> columnDireccion;
    @FXML
    private TableColumn<Usuario, Date> columnFecha;
    @FXML
    private TableColumn<Usuario, String> columnFoto;
    @FXML
    private TableColumn<Usuario, Integer> columnId;
    
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        columnDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        columnFecha.setCellValueFactory(new PropertyValueFactory<>("fecha_nacimiento"));
        columnFoto.setCellValueFactory(new PropertyValueFactory<>("foto"));
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        
    }
    
    public void cargarTodosUsuarios(){
        Query queryUsuarioTodos = entityManager.createNamedQuery("Usuario.findAll");
        List<Usuario> listUsuario = queryUsuarioTodos.getResultList();
        tableView.setItems(FXCollections.observableArrayList(listUsuario));
    }
    
}
