/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losalpes.servicios;

import com.losalpes.entities.RegistroVenta;
import com.losalpes.entities.TarjetaCreditoAlpes;
import com.losalpes.entities.Vendedor;
import com.losalpes.excepciones.CupoInsuficienteException;
import com.losalpes.excepciones.OperacionInvalidaException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.*;

/**
 *
 * @author WALTER
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PersistenciaCMT implements IPersistenciaCMTMockLocal, IPersistenciaCMTMockRemote {

     /**
     * La entidad encargada de persistir en la base de datos
     */
    @EJB
    private IServicioPersistenciaMockLocal persistenciaOracle;

    /**
     * La entidad encargada de persistir en la base de datos
     */
    @EJB
    private IServicioPersistenciaDerbyMockLocal persistenciaDerby;

    @Resource
    private SessionContext context;

    /**
     * 
     * @param venta 
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void comprar(RegistroVenta venta) {
        try {       
            persistenciaOracle.create(venta);            
            DescontarCupoTarjeta(venta);    
        } catch (CupoInsuficienteException | OperacionInvalidaException e) {            
            context.setRollbackOnly();
        } catch(Exception ex){            
            throw ex;
        }
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void insertarVendedor(Vendedor vendedor) {
        this.insertRemoteDatabase(vendedor);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Vendedor buscarVendedor(long id) {
        return (Vendedor) persistenciaOracle.findById(Vendedor.class, id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void eliminarVendedor(Vendedor vendedor) {
        this.deleteRemoteDatabase(vendedor);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public int length(Class c) {
        return persistenciaOracle.findAll(c).size();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void insertar(Object obj) {
        try {
            persistenciaOracle.create(obj);
        } catch (Exception e) {
            context.setRollbackOnly();
        }
    }
    
     /** Metodos privados **/   
    private void insertRemoteDatabase(Vendedor vendedor) {
         try {
            persistenciaOracle.create(vendedor);
        } catch (Exception e) {
            context.setRollbackOnly();
        }
    }
    
    private void deleteRemoteDatabase(Vendedor vendedor){
          try {
            persistenciaOracle.delete(vendedor);
        } catch (Exception e) {
            context.setRollbackOnly();
        }
    }
    
    private void DescontarCupoTarjeta(RegistroVenta venta) throws CupoInsuficienteException, OperacionInvalidaException { //,CupoInsuficienteException        
        double valorTotal = venta.getProducto().getPrecio() * venta.getCantidad();
        
        String sql = "SELECT c FROM TarjetaCreditoAlpes c WHERE c.login = '"+venta.getComprador().getLogin()+"'";
        Object result= persistenciaDerby.findSingleByQuery(sql);
        TarjetaCreditoAlpes tarjet = (TarjetaCreditoAlpes)result;
        if(tarjet == null)
        {
             throw new CupoInsuficienteException();
        }
        
        double saldoEnTargeta = tarjet.getCupo() - valorTotal;
        tarjet.setCupo(saldoEnTargeta);
        persistenciaDerby.update(tarjet);
        if (tarjet.getCupo() < 0) {
            throw new CupoInsuficienteException();
        }
    }
}
