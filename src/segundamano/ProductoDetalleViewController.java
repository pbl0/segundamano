/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segundamano;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TablePosition;
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
    private boolean nuevoProducto;

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
        int numFilaSeleccionada;
        this.producto.setNombre(textFieldNombre.getText());
        this.producto.setFabricante(textFieldFabrica.getText());
        java.math.BigDecimal precio = new java.math.BigDecimal(textFieldPrecio.getText());
        this.producto.setPrecio(precio);
        java.math.BigDecimal envio = new java.math.BigDecimal(textFieldEnvio.getText());
        this.producto.setEnvio(envio);
        this.producto.setDescripcion(textAreaDesc.getText());
        this.producto.setNuevo(checkBoxNuevo.selectedProperty().get());
        
        if (datePickerFecha.getValue() != null) {
            LocalDate localDate = datePickerFecha.getValue();
            ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
            Instant instant = zonedDateTime.toInstant();
            Date date = Date.from(instant);
            producto.setFecha(date);
        } else{
            producto.setFecha(null);
        }
        
        
        
        if(nuevoProducto){
            entityManager.persist(producto);
            
        } else {
            entityManager.merge(producto);
        }
        entityManager.getTransaction().commit();
        
        this.volverLista();
        
        if(nuevoProducto) {
            tableViewPrevio.getItems().add(producto);
            numFilaSeleccionada = tableViewPrevio.getItems().size() - 1;
            tableViewPrevio.getSelectionModel().select(numFilaSeleccionada);
            tableViewPrevio.scrollTo(numFilaSeleccionada);
        } else {
            numFilaSeleccionada = tableViewPrevio.getSelectionModel().getSelectedIndex();
            tableViewPrevio.getItems().set(numFilaSeleccionada, producto); 
        }
        TablePosition pos = new TablePosition(tableViewPrevio, numFilaSeleccionada, null);
        tableViewPrevio.getFocusModel().focus(pos);
        tableViewPrevio.requestFocus();
    
    }

    @FXML
    private void onActionButtonCancelar(ActionEvent event) {
        entityManager.getTransaction().rollback();
        
        this.volverLista();
        
        int numFilaSeleccionada = tableViewPrevio.getSelectionModel().getSelectedIndex();
        TablePosition pos = new TablePosition(tableViewPrevio, numFilaSeleccionada, null);
        tableViewPrevio.getFocusModel().focus(pos);
        tableViewPrevio.requestFocus();
    }
    
    public void setRootProductosView(Pane rootProductosView) {
        this.rootProductosView = rootProductosView;
    }
    
    public void setTableViewPrevio(TableView tableViewPrevio) {
        this.tableViewPrevio = tableViewPrevio;
    }
    
    public void setProducto(EntityManager entityManager, Producto producto, boolean nuevoProducto) {
        this.entityManager = entityManager;
        entityManager.getTransaction().begin();
        if(!nuevoProducto) {
            this.producto = entityManager.find(Producto.class, producto.getId());
        } else {
            this.producto = producto;
        }
        this.nuevoProducto = nuevoProducto;
    }
    
    public void mostrarDatos() {
        textFieldNombre.setText(producto.getNombre());
        textFieldFabrica.setText(producto.getFabricante());
        textFieldPrecio.setText(producto.getPrecio().toString());
        textFieldEnvio.setText(producto.getEnvio().toString());
        textAreaDesc.setText(producto.getDescripcion());
        checkBoxNuevo.setSelected(producto.getNuevo());
        datePickerFecha.setValue(producto.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }
    
}
