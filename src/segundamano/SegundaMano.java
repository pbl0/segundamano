/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segundamano;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
/**
 *
 * @author PC15
 */
public class SegundaMano {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Conectar con la base de datos
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SegundaManoPU");
        EntityManager em = emf.createEntityManager();
        
        java.math.BigDecimal precio = new java.math.BigDecimal("150.50");
        java.math.BigDecimal envio = new java.math.BigDecimal("7.00");
        
        Producto productoNuevo = new Producto(0, "Televisor", "Panasonic", "Descripción", false, precio, envio, "foto");
        Usuario usuario = em.find(Usuario.class, 5);
        productoNuevo.setUsuario(usuario);
        
        // Transacción
        em.getTransaction().begin();
        
        String sDate = "03/07/2018";
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(sDate);
            productoNuevo.setFecha(date);
        } catch (ParseException ex) {
        }
        
        em.persist(productoNuevo);
        
        
        // Modificar/Eliminar objeto
//        System.out.println("Modificar objeto:");
//        Usuario usuarioId = em.find(Usuario.class, 1);
//        if(usuarioId != null) {
//            System.out.println(usuarioId.getId()+ ": " + usuarioId.getNombre());
//            usuarioId.setTelefono("956464646");
//            usuarioId.setDireccion("Avenida España, 55");
//            //usuarioId.setFechaNacimiento();
//            em.merge(usuarioId);
//            //em.remove(usuarioId);
//        } else{
//            System.out.println("No hay ningun usuario con ese id");
//        }
        em.getTransaction().commit();
        // em.getTransaction().rollback();
        
        // Cerrar la conexión con la base de datos
        em.close(); 
        emf.close(); 
        try { 
            DriverManager.getConnection("jdbc:derby:/BD;shutdown=true"); 
        } catch (SQLException ex) { 
        }
    } 
}
