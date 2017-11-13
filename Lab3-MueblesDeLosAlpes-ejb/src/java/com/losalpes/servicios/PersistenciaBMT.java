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
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    @EJB
    private IServicioPersistenciaMockLocal servicioOracle;

    /**
     * La entidad encargada de persistir en la base de datos
     */
    @EJB
    private IServicioPersistenciaDerbyMockLocal servicioDerby;
    
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
            System.out.print("antes init");
            initTransaction();
            System.out.print("antes create");
            servicioOracle.create(venta);
            System.out.print("antes descontar");
            DescontarCupoTarjeta(venta);
            System.out.print("antes commit");
            commitTransaction();
        } catch (CupoInsuficienteException e) {
            System.out.print("excepcion cupoiind");
            rollbackTransaction();
        } catch (OperacionInvalidaException e) {
            System.out.print("excep operacion inva");
            rollbackTransaction();
        } catch(Exception ex){
            System.out.print("excepcion");
            throw ex;
        }
    }

    private void DescontarCupoTarjeta(RegistroVenta venta) throws CupoInsuficienteException, OperacionInvalidaException { //,CupoInsuficienteException
        System.out.print("antes valor total");
        double valorTotal = venta.getProducto().getPrecio() * venta.getCantidad();
        System.out.print("despues valor total");
        
        String sql = "SELECT c FROM TarjetaCreditoAlpes c WHERE c.login = '"+venta.getComprador().getLogin()+"'";
        Object result= servicioDerby.findSingleByQuery(sql);
        System.out.print("tarjeta.");
        System.out.print(result);
        System.out.print("antes convbersion.");
         TarjetaCreditoAlpes tarjet = (TarjetaCreditoAlpes)result;
        if(tarjet == null)
        {
             System.out.print("No se encontro la tarjeta.");
             throw new CupoInsuficienteException();
        }
        
        System.out.print("la tarjeta la encomntro.");
        
        System.out.print("antes saldo");
        double saldoEnTargeta = tarjet.getCupo() - valorTotal;
        System.out.print("antes set cupo");
        tarjet.setCupo(saldoEnTargeta);
        System.out.print("antes create tarjeta");
        servicioDerby.update(tarjet);
        System.out.print("antes if cupo");
        if (tarjet.getCupo() < 0) {
            System.out.print("antes lanzar excepcion");
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
   /* public void create(Object obj) {
        initTransaction();
        ventas.persist(obj);
        commitTransaction();
    }*/

    /**
     * Permite modificar un objeto dentro de la persistencia del sistema.
     *
     * @param obj Objeto que representa la instancia de la entidad que se quiere
     * modificar.
     */
    /*public void update(Object obj) {
        initTransaction();
        ventas.merge(obj);
        commitTransaction();
    }*/

    /**
     * Permite borrar un objeto dentro de la persistencia del sistema.
     *
     * @param obj Objeto que representa la instancia de la entidad que se quiere
     * borrar.
     */
    /*public void delete(Object obj) {
        initTransaction();
        ventas.remove(obj);
        commitTransaction();
    }*/

    /**
     * Retorna la lista de todos los elementos de una clase dada que se
     * encuentran en el sistema.
     *
     * @param c Clase cuyos objetos quieren encontrarse en el sistema.
     * @return list Listado de todos los objetos de una clase dada que se
     * encuentran en el sistema.
     */
    /*public List findAll(Class c) {
        Query query = ventas.createQuery("select O from " + c.getSimpleName() + " as O");
        List result = query.getResultList();
        return result;
    }*/

    /**
     * Retorna la instancia de una entidad dado un identificador y la clase de
     * la entidadi.
     *
     * @param c Clase de la instancia que se quiere buscar.
     * @param id Identificador unico del objeto.
     * @return obj Resultado de la consulta.
     */
    /*public Object findById(Class c, Object id) {
        return ventas.find(c, id);
    }*/

    public void insertarVendedor(Vendedor vendedor) {
        try {
            initTransaction();
            servicioOracle.create(vendedor);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
        }
    }

    public void eliminarVendedor(Vendedor vendedor) {
        try {
            initTransaction();
            servicioOracle.delete(vendedor);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
        }
    }
    
    public void insertarTC(TarjetaCreditoAlpes tc) {
        try {
             initTransaction();
            servicioDerby.create(tc);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
        }
    }
    
    public List findAllTC() {
        return servicioDerby.findByQuery("select O from TarjetaCreditoAlpes as O");
    }
}
