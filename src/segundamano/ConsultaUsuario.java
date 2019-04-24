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
        
        Query queryUsuario = em.createNamedQuery("Usuario.findAll");
        
        List<Usuario> listUsuario = queryUsuario.getResultList();
        
        for(Usuario usuario : listUsuario){
            System.out.println(usuario.getNombre() + ' ' + usuario.getApellidos());
        }
    }
    
}
