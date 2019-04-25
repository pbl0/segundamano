/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segundamano;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author PC15
 */
public class ConsultaUsuario {
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SegundaManoPU");
        EntityManager em = emf.createEntityManager();
        
        // Consulta todos
        System.out.println("Consulta todos:");
        Query queryUsuarioTodos = em.createNamedQuery("Usuario.findAll");
        List<Usuario> listUsuario = queryUsuarioTodos.getResultList();
        for(Usuario usuario : listUsuario){
            System.out.println(usuario.getId() + ": " + usuario.getNombre());
        }
        
        // Consulta por nombre
        System.out.println("Consulta por nombre:");
        Query queryUsuarioNombre = em.createNamedQuery("Usuario.findByNombre");
        queryUsuarioNombre.setParameter("nombre", "Juan");
        List<Usuario> listNombre = queryUsuarioNombre.getResultList();
        for(Usuario usuario : listNombre){
            System.out.println(usuario.getId() + ": " + usuario.getNombre());
        }
        
        // Encontar por id
        System.out.println("Consulta por id:");
        Usuario usuarioId = em.find(Usuario.class, 2);
        if(usuarioId != null) {
            System.out.println(usuarioId.getId()+ ": " + usuarioId.getNombre());
        }
        
        
        
        
    }
    
}
