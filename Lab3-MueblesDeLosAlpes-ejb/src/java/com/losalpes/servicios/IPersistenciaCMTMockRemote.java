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
public interface IPersistenciaCMTMockRemote {

    /**
     * Metodo para crear el objeto de compra, vcalida contra la bd de Derby el
     * cupo de tarjeta.
     *
     * @param venta
     */
    public void comprar(RegistroVenta venta);

    public void insertarTC(TarjetaCreditoAlpes tc);

    public void insertar(Object obj);

    public void DescontarCupoTarjeta(RegistroVenta venta);

    public void insertarVendedor(Vendedor vendedor);

    public void eliminarVendedor(Vendedor vendedor);

    public int length(Class c);

    public Vendedor buscarVendedor(long id);

}
