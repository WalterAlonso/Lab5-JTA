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
    @EJB
    private IServicioPersistenciaDerbyMockLocal persistenciaDerby;

    //@PersistenceContext(unitName = "Lab3-MueblesDeLosAlpes-ejbPUOracle")
    //private EntityManager ventas;
    /**
     * La entidad encargada de persistir en la base de datos
     */
    //@PersistenceContext(unitName = "Lab3-MueblesDeLosAlpes-ejbPUDerby")
    //private EntityManager tarjeta;
    @PostConstruct
    public void postConstruct() {

    }

    @Resource
    private SessionContext context;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED) //este se podria omitir
    public void comprar(RegistroVenta venta) {
        try {
            persistenciaOracle.create(venta);
//            ventas.persist(venta);
            DescontarCupoTarjeta(venta);

        } catch (OperacionInvalidaException ex) {
            context.setRollbackOnly();
        }
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public void DescontarCupoTarjeta(RegistroVenta venta) {

        try {
            double valorTotal = venta.getProducto().getPrecio() * venta.getCantidad();
            TarjetaCreditoAlpes tarjetaCredito = venta.getComprador().getTarjetaCreditoAlpes();
            double saldoEnTargeta = tarjetaCredito.getCupo() - valorTotal;
            tarjetaCredito.setCupo(saldoEnTargeta);
            persistenciaDerby.create(tarjetaCredito);
            //tarjeta.persist(tarjetaCredito);
            if (tarjetaCredito.getCupo() < 0) {
                throw new CupoInsuficienteException();
            }
        } catch (OperacionInvalidaException ex) {
            context.setRollbackOnly();
        } catch (CupoInsuficienteException ex) {
            context.setRollbackOnly();
        }

    }

    public void insertarVendedor(Vendedor vendedor) {
        try {
            persistenciaOracle.create(vendedor);
        } catch (Exception e) {
            context.setRollbackOnly();
        }
    }

    public void eliminarVendedor(Vendedor vendedor) {
        try {
            persistenciaOracle.delete(vendedor);
        } catch (Exception e) {
            context.setRollbackOnly();
        }
    }

    public void insertarTC(TarjetaCreditoAlpes tc) {
        try {
            persistenciaDerby.create(tc);
        } catch (Exception e) {
            context.setRollbackOnly();
        }
    }

    public void insertar(Object obj) {
        
        try {
            persistenciaOracle.create(obj);
        } catch (Exception e) {
            context.setRollbackOnly();
        }
    }

    public int length(Class c) {
        return persistenciaOracle.findAll(c).size();
    }

}
