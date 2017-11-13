/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losalpes.servicios;

import com.losalpes.entities.RegistroVenta;
import com.losalpes.entities.TarjetaCreditoAlpes;
import com.losalpes.entities.Vendedor;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author WALTER
 */
@Remote
public interface IPersistenciaBMTMockRemote {
    
    /**
     * Metodo para crear el objeto de compra, valida contra la bd de Derby el
     * cupo de tarjeta.
     *
     * @param venta
     */
    public void comprar(RegistroVenta venta);

    /**
     * Inserta vendedor en la base de datos de oracle
     * @param vendedor 
     */
    public void insertarVendedor(Vendedor vendedor);
         
    /**
     * Elimina vendedor en la base de datos de oracle
     * @param vendedor 
     */
    public void eliminarVendedor(Vendedor vendedor);
    
    /**
     * Busca vendedor
     * @param id
     * @return 
     */
    public Vendedor buscarVendedor(long id);
    
    /**
     * Permite saber la cantidad de registros en entidad
     * @param c
     * @return 
     */
    public int length(Class c);
    
    /**
     * Inserta culquier entidad.
     * @param obj 
     */
    public void insertar(Object obj);
}
