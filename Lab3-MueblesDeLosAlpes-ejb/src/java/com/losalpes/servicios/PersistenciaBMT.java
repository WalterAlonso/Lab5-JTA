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
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.UserTransaction;

/**
 *
 * @author WALTER
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class PersistenciaBMT implements IPersistenciaBMTMockLocal, IPersistenciaBMTMockRemote {

    @Resource
    private UserTransaction userTransaction;
    
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
    
    /**
     * 
     * @param venta 
     */
    @Override
    public void comprar(RegistroVenta venta) {
        try {
            initTransaction();            
            persistenciaOracle.create(venta);            
            DescontarCupoTarjeta(venta);            
            commitTransaction();
        } catch (CupoInsuficienteException | OperacionInvalidaException e) {            
            rollbackTransaction();
        } catch(Exception ex){            
            throw ex;
        }
    }

    @Override
    public void insertarVendedor(Vendedor vendedor) {
       this.insertRemoteDatabase(vendedor);
    }

    @Override
    public void eliminarVendedor(Vendedor vendedor) {
       this.deleteRemoteDatabase(vendedor);
    }
    
    @Override
    public int length(Class c) {
        return persistenciaOracle.findAll(c).size();
    }
     
    @Override
    public Vendedor buscarVendedor(long id) {
        return (Vendedor) persistenciaOracle.findById(Vendedor.class, id);
    }
     
    @Override
    public void insertar(Object obj) {
        try {
            initTransaction();
            persistenciaOracle.create(obj);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
        }
    }
    
    /** Metodos privados **/
    private void initTransaction() {
        try {
            userTransaction.begin();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void commitTransaction() {
        try {
            userTransaction.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void rollbackTransaction() {
        try {
            userTransaction.rollback();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void insertRemoteDatabase(Vendedor vendedor) {
         try {
            initTransaction();
            persistenciaOracle.create(vendedor);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
        }
    }
    
    private void deleteRemoteDatabase(Vendedor vendedor){
         try {
            initTransaction();
            persistenciaOracle.delete(vendedor);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
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
