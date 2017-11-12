/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losalpes.servicios;

import com.losalpes.entities.RegistroVenta;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author WALTER
 */
@Remote
public interface IPersistenciaBMTMockRemote {
    /**
     * Metodo para crear el objeto de compra, vcalida contra la bd de Derby el cupo de tarjeta.
     * @param venta 
     */
    public void comprar(RegistroVenta venta);
    
     /**
     * Método para crear objetos en la base de datos.
     * @param obj Variable tipo Object
     */
    public void create(Object obj);
    /**
     * Método para actualizar un objeto en la base de datos.
     * @param obj Variable tipo Object
     */
    public void update(Object obj);
    /**
     * Método para eliminar un objeto de la base de datos.
     * @param obj Variable tipo Object
     */
    public void delete(Object obj);
    /**
     * Método para retornar todos los objetos de una tabla de la base de datos.
     * @param c Clase para consultar.
     * @return List Listado con todos los osbjetos de la tabla.
     */
    public List findAll(Class c);
    /**
     * Método para consultar y retornar objetos de una tabla por medio del Id.
     * @param c Clase para consultar.
     * @param id String Id del Objeto
     * @return Object Variable tipo Object
     */
    public Object findById(Class c, Object id);   
}
