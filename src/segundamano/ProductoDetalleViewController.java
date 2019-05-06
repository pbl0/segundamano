/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segundamano;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javax.persistence.EntityManager;

/**
 * FXML Controller class
 *
 * @author PC15
 */
public class ProductoDetalleViewController implements Initializable {
    
    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldFabrica;
    @FXML
    private TextField textFieldPrecio;
    @FXML
    private TextField textFieldEnvio;
    @FXML
    private TextArea textAreaDesc;
    @FXML
    private CheckBox checkBoxNuevo;
    @FXML
    private DatePicker datePickerFecha;
    @FXML
    private ComboBox<?> comboBoxVendedor;
    @FXML
    private ImageView imageViewFoto;
    @FXML
    private AnchorPane rootProductosDetalleView;
    
    private Pane rootProductosView;
    private TableView tableViewPrevio;
    private Producto producto;
    private EntityManager entityManager;
    private boolean nuevaPersona;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    private void volverLista(){
        StackPane rootMain = (StackPane)rootProductosDetalleView.getScene().getRoot();
        rootMain.getChildren().remove(rootProductosDetalleView);
        rootProductosView.setVisible(true);
    }
    
    @FXML
    private void onActionButtonGuardar(ActionEvent event) {
        this.volverLista();
    }

    @FXML
    private void onActionButtonCancelar(ActionEvent event) {
        this.volverLista();
    }
    
    public void setRootProductosView(Pane rootProductosView) {
        this.rootProductosView = rootProductosView;
    }
    
    public void setTableViewPrevio(TableView tableViewPrevio) {
        this.tableViewPrevio = tableViewPrevio;
    }
    
    public void setProducto(EntityManager entityManager, Producto producto, boolean nuevaPersona) {
        this.entityManager = entityManager;
        entityManager.getTransaction().begin();
        if(!nuevaPersona) {
            this.producto = entityManager.find(Producto.class, producto.getId());
        } else {
            this.producto = producto;
        }
        this.nuevaPersona = nuevaPersona;
    }
    
    public void mostrarDatos() {
        
        textFieldNombre.setText(producto.getNombre());
        textFieldFabrica.setText(producto.getFabricante());
        textFieldPrecio.setText(producto.getPrecio().toString());
        textFieldEnvio.setText(producto.getEnvio().toString());
        textAreaDesc.setText(producto.getDescripcion());
        

    }
    
}
