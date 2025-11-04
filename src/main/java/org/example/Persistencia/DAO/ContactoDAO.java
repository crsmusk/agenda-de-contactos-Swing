package org.example.Persistencia.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.example.Persistencia.Conector.conector;
import org.example.Persistencia.Excepciones.ContactoNoEncontrado;
import org.example.Persistencia.Excepciones.ErrorAlEstablecerLaConexion;
import org.example.Persistencia.Excepciones.ErrorEnLaConsulta;
import org.example.Persistencia.Model.Entidades.Contacto;

import java.util.List;

public class ContactoDAO {

    public Contacto save(Contacto contacto){
        EntityManager dao = conector.getEntityManager();
        try {
            dao.getTransaction().begin();
            Query query = dao.createNativeQuery("CALL guardarContacto(:nombre, :apellido, :correo, :telefono, @id)");
            query.setParameter("nombre", contacto.getNombre());
            query.setParameter("apellido", contacto.getApellido());
            query.setParameter("correo", contacto.getCorreo());
            query.setParameter("telefono", contacto.getNumeroDeTelefono());
            query.executeUpdate();
            
            
            Query idQuery = dao.createNativeQuery("SELECT @id");
            Object result = idQuery.getSingleResult();
            contacto.setId(Long.valueOf(result.toString()));
            
            dao.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return contacto;
    }
    public List<Contacto> findByNameOrlastName(String nombre){
        EntityManager dao= conector.getEntityManager();
        List<Contacto>lista=null;
        try {
            Query query=dao.createQuery("SELECT c FROM Contacto c WHERE c.nombre LIKE:nombre or c.apellido LIKE:apellido",Contacto.class);
            query.setParameter("nombre","%"+nombre+"%");
            query.setParameter("apellido","%"+nombre+"%");
           lista=query.getResultList();
        }catch (ContactoNoEncontrado e){
            e.printStackTrace();
        }finally {
            dao.close();
        }
        return lista;
    }

    public List<Contacto> getAll() {
        EntityManager dao = conector.getEntityManager();
        List<Contacto> lista = null;
        try {
            Query query = dao.createNativeQuery("CALL obtenerContactos()", Contacto.class);
            lista = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return lista;
    }

    public void delete(Long id) {
        EntityManager dao = conector.getEntityManager();
        try {
            dao.getTransaction().begin();
            Query query = dao.createNativeQuery("CALL eliminarContacto(:id)");
            query.setParameter("id", id);
            query.executeUpdate();
            dao.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
    }

    public Contacto update(Long id, Contacto contacto) {
        EntityManager dao = conector.getEntityManager();
        Contacto contactoActualizado = null;
        try {
            dao.getTransaction().begin();
            Query query = dao.createNativeQuery(
                "CALL actualizarContacto(:id, :nombre, :apellido, :correo, :telefono)",
                Contacto.class
            );
            query.setParameter("id", id);
            query.setParameter("nombre", contacto.getNombre());
            query.setParameter("apellido", contacto.getApellido());
            query.setParameter("correo", contacto.getCorreo());
            query.setParameter("telefono", contacto.getNumeroDeTelefono());
            contactoActualizado = (Contacto) query.getSingleResult();
            dao.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return contactoActualizado;
    }


}
