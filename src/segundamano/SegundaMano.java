/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segundamano;
import java.sql.DriverManager;
import java.sql.SQLException;
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
        
        Usuario usuarioNuevo = new Usuario(0, "Nombre", "Apellidos", "email@email.com","Direccion");
        
        // Transacción
        em.getTransaction().begin();
        // Añadir aquí las operaciones de modificación de la base de datos
        
        em.persist(usuarioNuevo);
        
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
