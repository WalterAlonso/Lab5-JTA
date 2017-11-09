/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losalpes.servicios;

import com.losalpes.entities.RegistroVenta;
import com.losalpes.entities.TarjetaCreditoAlpes;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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
public class PersistenciaCMT {

    /**
     * La entidad encargada de persistir en la base de datos
     */
    @PersistenceContext(unitName = "Lab3-MueblesDeLosAlpes-ejbPUOracle")
    private EntityManager ventas;

    /**
     * La entidad encargada de persistir en la base de datos
     */
    @PersistenceContext(unitName = "Lab3-MueblesDeLosAlpes-ejbPUDerby")
    private EntityManager tarjeta;

    @PostConstruct
    public void postConstruct() {

    }

    @Resource
    private SessionContext context;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void comprar(RegistroVenta venta) {
        try {
            ventas.persist(venta);
            DescontarCupoTarjeta(venta);

            /*} catch (CupoInsuficienteException e) {
        context.setRollbackOnly();*/
        } catch (Exception e) { //TODO: quitarla cuando se implemente la excepcion
            context.setRollbackOnly();
        }
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    private void DescontarCupoTarjeta(RegistroVenta venta) throws Exception {
        double valorTotal = venta.getProducto().getPrecio() * venta.getCantidad();
        TarjetaCreditoAlpes tarjetaCredito = venta.getComprador().getTarjetaCreditoAlpes();
        double saldoEnTargeta = tarjetaCredito.getCupo() - valorTotal;
        tarjetaCredito.setCupo(saldoEnTargeta);
        tarjeta.persist(tarjetaCredito);

        if (tarjetaCredito.getCupo() < 0) {
            //throw new CupoInsuficienteException();
            throw new Exception();
        }
    }

}
