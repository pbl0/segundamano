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
import java.util.logging.Level;
import java.util.logging.Logger;
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
        
        Usuario usuarioNuevo = new Usuario(0, "Juan", "Apellidos", "email@email.com","Direccion");
        
        // Transacción
        em.getTransaction().begin();
        
        
        em.persist(usuarioNuevo);
        String sDate = "03/06/1965";
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(sDate);
            usuarioNuevo.setFechaNacimiento(date);
        } catch (ParseException ex) {
            Logger.getLogger(SegundaMano.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Modificar/Eliminar objeto
        System.out.println("Modificar objeto:");
        Usuario usuarioId = em.find(Usuario.class, 3);
        if(usuarioId != null) {
            System.out.println(usuarioId.getId()+ ": " + usuarioId.getNombre());
            usuarioId.setNombre("Alfredo");
            //em.merge(usuarioId);
            em.remove(usuarioId);
        } else{
            System.out.println("No hay ningun usuario con ese id");
        }
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
