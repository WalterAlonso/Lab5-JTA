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
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
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

    public void comprar(RegistroVenta venta) {
        try {
            initTransaction();
            ventas.persist(venta);
            DescontarCupoTarjeta(venta);
            commitTransaction();
        } catch (CupoInsuficienteException e) {
            rollbackTransaction();

        }
    }

    private void DescontarCupoTarjeta(RegistroVenta venta) throws CupoInsuficienteException { //,CupoInsuficienteException
        double valorTotal = venta.getProducto().getPrecio() * venta.getCantidad();
        TarjetaCreditoAlpes tarjetaCredito = venta.getComprador().getTarjetaCreditoAlpes();
        double saldoEnTargeta = tarjetaCredito.getCupo() - valorTotal;
        tarjetaCredito.setCupo(saldoEnTargeta);
        tarjeta.persist(tarjetaCredito);

        if (tarjetaCredito.getCupo() < 0) {
            throw new CupoInsuficienteException();
        }
    }

    /**
     * metodos de persistencia normales *
     */
    /**
     * Permite crear un objeto dentro de la persistencia del sistema.
     *
     * @param obj Objeto que representa la instancia de la entidad que se quiere
     * crear.
     */
    public void create(Object obj) {
        initTransaction();
        ventas.persist(obj);
        commitTransaction();
    }

    /**
     * Permite modificar un objeto dentro de la persistencia del sistema.
     *
     * @param obj Objeto que representa la instancia de la entidad que se quiere
     * modificar.
     */
    public void update(Object obj) {
        initTransaction();
        ventas.merge(obj);
        commitTransaction();
    }

    /**
     * Permite borrar un objeto dentro de la persistencia del sistema.
     *
     * @param obj Objeto que representa la instancia de la entidad que se quiere
     * borrar.
     */
    public void delete(Object obj) {
        initTransaction();
        ventas.remove(obj);
        commitTransaction();
    }

    /**
     * Retorna la lista de todos los elementos de una clase dada que se
     * encuentran en el sistema.
     *
     * @param c Clase cuyos objetos quieren encontrarse en el sistema.
     * @return list Listado de todos los objetos de una clase dada que se
     * encuentran en el sistema.
     */
    public List findAll(Class c) {
        return ventas.createQuery("select O from " + c.getSimpleName() + " as O").getResultList();
    }

    /**
     * Retorna la instancia de una entidad dado un identificador y la clase de
     * la entidadi.
     *
     * @param c Clase de la instancia que se quiere buscar.
     * @param id Identificador unico del objeto.
     * @return obj Resultado de la consulta.
     */
    public Object findById(Class c, Object id) {
        return ventas.find(c, id);
    }

    public void insertarVendedor(Vendedor vendedor) {
        try {
            initTransaction();
            ventas.persist(vendedor);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
        }
    }

    public void eliminarVendedor(Vendedor vendedor) {
        try {
            initTransaction();
            ventas.remove(vendedor);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
        }
    }
}
