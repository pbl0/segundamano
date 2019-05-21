/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segundamano;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.RollbackException;

/**
 * FXML Controller class
 * Clase controladora de la vista detalles
 * @author Pablo Barragan
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
    private CheckBox checkBoxEnvInt;
    @FXML
    private DatePicker datePickerFecha;
    @FXML
    private ComboBox<Usuario> comboBoxVendedor;
    @FXML
    private ImageView imageViewFoto;
    @FXML
    private AnchorPane rootProductosDetalleView;
    @FXML
    private RadioButton radioButtonNuevo;
    @FXML
    private RadioButton radioButtonCasi;
    @FXML
    private RadioButton radioButtonUsado;
    @FXML
    private RadioButton radioButtonEstropeado;

    private Pane rootProductosView;
    private TableView tableViewPrevio;
    private Producto producto;
    private EntityManager entityManager;
    private boolean nuevoProducto;
    
    public static final char NUEVO = 'N';
    public static final char CASI_NUEVO = 'C';
    public static final char USADO = 'U';
    public static final char ESTROPEADO = 'E';
    
    public static final String CARPETA_FOTOS = "Fotos";
    
    public static void limitTextField(TextField textField, int limit) {
        UnaryOperator<TextFormatter.Change> textLimitFilter = change -> {
            if (change.isContentChange()) {
                int newLength = change.getControlNewText().length();
                if (newLength > limit) {
                    String trimmedText = change.getControlNewText().substring(0, limit);
                    change.setText(trimmedText);
                    int oldLength = change.getControlText().length();
                    change.setRange(0, oldLength);
                }
            }
            return change;
        };
        textField.setTextFormatter(new TextFormatter(textLimitFilter));
    }
    
    public static void limitTextArea(TextArea textArea, int limit) {
        UnaryOperator<TextFormatter.Change> textLimitFilter = change -> {
            if (change.isContentChange()) {
                int newLength = change.getControlNewText().length();
                if (newLength > limit) {
                    String trimmedText = change.getControlNewText().substring(0, limit);
                    change.setText(trimmedText);
                    int oldLength = change.getControlText().length();
                    change.setRange(0, oldLength);
                }
            }
            return change;
        };
        textArea.setTextFormatter(new TextFormatter(textLimitFilter));
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Limitamos numero de caracteres en cada campo de texto.
        limitTextField(textFieldNombre, 20);
        limitTextField(textFieldFabrica, 20);
        limitTextField(textFieldPrecio, 8);
        limitTextField(textFieldEnvio, 8);
        limitTextArea(textAreaDesc, 155);
        
    }    
    
    // Metodo para volver a la lista de productos.
    private void volverLista(){
        StackPane rootMain = (StackPane)rootProductosDetalleView.getScene().getRoot();
        rootMain.getChildren().remove(rootProductosDetalleView);
        rootProductosView.setVisible(true);
    }
    
    // Boton de guardar
    @FXML
    private void onActionButtonGuardar(ActionEvent event) {
        boolean errorFormato = false;
        int numFilaSeleccionada;
        
        this.producto.setNombre(textFieldNombre.getText());
        this.producto.setFabricante(textFieldFabrica.getText());
        if (!textFieldPrecio.getText().isEmpty()){
            try {
                this.producto.setPrecio(BigDecimal.valueOf(Double.valueOf(textFieldPrecio.getText())));
            } catch (NumberFormatException e) {
                errorFormato = true;
            Alert alert = new Alert(AlertType.INFORMATION, "Precio no válido!");
            alert.showAndWait();
            textFieldPrecio.requestFocus();
            }
        }
        if (!textFieldEnvio.getText().isEmpty()){
            try {
                this.producto.setEnvio(BigDecimal.valueOf(Double.valueOf(textFieldEnvio.getText())));
            } catch (NumberFormatException e) {
                errorFormato = true;
            Alert alert = new Alert(AlertType.INFORMATION, "Coste de envio no válido!");
            alert.showAndWait();
            textFieldEnvio.requestFocus();
            }
        }
        
        this.producto.setDescripcion(textAreaDesc.getText());
        this.producto.setEnvInt(checkBoxEnvInt.selectedProperty().get());
        
        if (radioButtonNuevo.isSelected()) {
            producto.setEstado(NUEVO);
        } else if (radioButtonCasi.isSelected()) {
            producto.setEstado(CASI_NUEVO);
        } else if (radioButtonUsado.isSelected()) {
            producto.setEstado(USADO);
        } else if (radioButtonEstropeado.isSelected()) {
            producto.setEstado(ESTROPEADO);
        }
        
        if (datePickerFecha.getValue() != null) {
            LocalDate localDate = datePickerFecha.getValue();
            ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
            Instant instant = zonedDateTime.toInstant();
            Date date = Date.from(instant);
            producto.setFecha(date);
        } else{
            producto.setFecha(null);
        }
        
        if(comboBoxVendedor.getValue() != null){
            producto.setUsuario(comboBoxVendedor.getValue());
        } else {
            Alert alert = new Alert(AlertType.INFORMATION, "Debe indicar un vendedor!");
            alert.showAndWait();
            errorFormato = true;
        }
        
        // Si no hay error de formato:
        if(!errorFormato){
            try {
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
            } catch (RollbackException ex) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setHeaderText("No se han podido guardar los cambios. " + 
                        "Compruebe que los datos cumplen los requisitos");
                alert.setContentText(ex.getLocalizedMessage());
                alert.showAndWait();
                // Recomenzar transaction
                entityManager.getTransaction().begin();
            }
        }
    }

    @FXML
    private void onActionButtonCancelar(ActionEvent event) {
        entityManager.getTransaction().rollback();
        
        this.volverLista();
        
        int numFilaSeleccionada = tableViewPrevio.getSelectionModel().getSelectedIndex();
        System.out.println("numFilaSeleccionada " + numFilaSeleccionada);
        TablePosition pos = new TablePosition(tableViewPrevio, numFilaSeleccionada, null);
        tableViewPrevio.getFocusModel().focus(pos);
        tableViewPrevio.requestFocus();
        // ViewProductoController.productoSeleccionado = null;
        System.out.println("producto.getId() " + producto.getId());
        

        
    }
    
    @FXML
    private void onActionButtonExaminar(ActionEvent event) {
        File carpetaFotos = new File(CARPETA_FOTOS);
        if(!carpetaFotos.exists()) {
            carpetaFotos.mkdir();
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes (jpg, png)", "*.jpg", "*.png"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
            );
        File file = fileChooser.showOpenDialog(rootProductosDetalleView.getScene().getWindow());
        if(file != null) {
            try {
                Files.copy(file.toPath(), new File(CARPETA_FOTOS + "/"+file.getName()).toPath());
                producto.setFoto(file.getName());
                Image image = new Image(file.toURI().toString());
                imageViewFoto.setImage(image);
            } catch (FileAlreadyExistsException ex) {
                Alert alert = new Alert(AlertType.WARNING, "Nombre de archivo duplicado!");
                alert.showAndWait();
            } catch (IOException ex) {
                Alert alert = new Alert(AlertType.WARNING, "No se ha podido guardar la imagen!");
                alert.showAndWait();
            }
        }
    }
    
    @FXML
    private void onActionSuprimirFoto(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar supresión de imagen");
        alert.setHeaderText("¿Desea SUPRIMIR el archivo asociado a la imagen, \n"
                + "quitar la foto pero MANTENER el archivo, \no CANCELAR la operación?");
        alert.setContentText("Elija la opción deseada:");

        ButtonType buttonTypeEliminar = new ButtonType("Suprimir");
        ButtonType buttonTypeMantener = new ButtonType("Mantener");
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeEliminar, buttonTypeMantener, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeEliminar){
            String imageFileName = producto.getFoto();
            File file = new File(CARPETA_FOTOS + "/" + imageFileName);
            if(file.exists()) {
                file.delete();
            }
            producto.setFoto(null);
            imageViewFoto.setImage(null);
        } else if (result.get() == buttonTypeMantener) {
            producto.setFoto(null);
            imageViewFoto.setImage(null);
        } 
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
        if (producto.getPrecio() != null) {
            textFieldPrecio.setText(producto.getPrecio().toString());
        }
        
        if (producto.getEnvio() != null) {
            textFieldEnvio.setText(producto.getEnvio().toString());
        }
        
        textAreaDesc.setText(producto.getDescripcion());
        
        if (producto.getEnvInt() != null){
            checkBoxEnvInt.setSelected(producto.getEnvInt());
        }
        
        if(producto.getFecha() != null){
            datePickerFecha.setValue(producto.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        
        if (producto.getEstado() != null) {
            switch (producto.getEstado()) {
                case NUEVO:
                    radioButtonNuevo.setSelected(true);
                    break;
                case CASI_NUEVO:
                    radioButtonCasi.setSelected(true);
                    break;
                case USADO:
                    radioButtonUsado.setSelected(true);
                    break;
                case ESTROPEADO:
                    radioButtonEstropeado.setSelected(true);
                    break;
            }
        }
        
        //Consulta usuarios
        Query queryUsuarioFindAll = entityManager.createNamedQuery("Usuario.findAll");
        List<Usuario> listUsuario = queryUsuarioFindAll.getResultList();
        comboBoxVendedor.setItems(FXCollections.observableList(listUsuario));
        
        if (producto.getUsuario()!= null) {
            comboBoxVendedor.setValue(producto.getUsuario());
        }
  
        comboBoxVendedor.setCellFactory((ListView<Usuario> l) -> new ListCell<Usuario>() {
            @Override
            protected void updateItem(Usuario usuario, boolean empty) {
                super.updateItem(usuario, empty);
                if (usuario == null || empty) {
                    setText("");
                } else {
                    setText(usuario.getNombre());
                }
            }   
        });
        // Formato para el valor mostrado actualmente como seleccionado
        comboBoxVendedor.setConverter(new StringConverter<Usuario>() {
            @Override
            public String toString(Usuario usuario) {
                if (usuario == null) {
                    return null;
                } else {
                    return usuario.getNombre();
                }
            }
            @Override
            public Usuario fromString(String string) {
                return null;
            }

        });
        // Foto:
        if (producto.getFoto() != null){
            String imageFileName = producto.getFoto();
            File file = new File(CARPETA_FOTOS + "/" + imageFileName);
             if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                imageViewFoto.setImage(image);
            } else {
                Alert alert = new Alert(AlertType.INFORMATION, "No se encuentra la imagen");
                alert.showAndWait();
            }
        }
    }
}

