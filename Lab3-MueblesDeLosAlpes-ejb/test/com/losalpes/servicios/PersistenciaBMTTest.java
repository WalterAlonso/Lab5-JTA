/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losalpes.servicios;

import com.losalpes.entities.Mueble;
import com.losalpes.entities.Pais;
import com.losalpes.entities.RegistroVenta;
import com.losalpes.entities.TipoMueble;
import java.util.ArrayList;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author WAlonsoR
 */
public class PersistenciaBMTTest {

    //-----------------------------------------------------------
    // Métodos de inicialización y terminación
    //-----------------------------------------------------------
    /**
     * Método que se ejecuta antes de comenzar la prueba unitaria Se encarga de
     * inicializar todo lo necesario para la prueba
     */
    @Before
    public void setUp() {
        //insertar datos
        PersistenciaBMT instance = new PersistenciaBMT();

        //instance
    }

    /**
     * Método que se ejecuta después de haber ejecutado la prueba
     */
    @After
    public void tearDown() {
        //eliminarlos
    }

    //-----------------------------------------------------------
    // Métodos de prueba
    //-----------------------------------------------------------
    /**
     * Prueba para agregar un mueble en el sistema
     */
    @Test
    public void testComprar_TransaccionSatisfactoria() {
        System.out.println("comprar");
        PersistenciaBMT instance = new PersistenciaBMT();
//define usuario:      
//la tarjeta con cupo de 10000
//Define el mueble

        RegistroVenta v = new RegistroVenta();
        v.setCantidad(1);
        v.setCiudad("Bogota");
        //v.setComprador();
        //v.setProducto(producto);
        v.setRegistro(1);
        instance.comprar(v);
        //se obtiene la venta

        //assertEquals(esperado,actual+1);
    }

    /**
     * Prueba para agregar un mueble en el sistema
     */
    @Test
    public void testComprar_TransaccionConCupoInsuficiente() {
        System.out.println("comprar");
        PersistenciaBMT instance = new PersistenciaBMT();
//define usuario:      
//la tarjeta con cupo de 10
//Define el mueble

        RegistroVenta v = new RegistroVenta();
        v.setCantidad(1);
        v.setCiudad("Bogota");
        //v.setComprador();
        //v.setProducto(producto);
        v.setRegistro(1);
        instance.comprar(v);
        //se obtiene la venta        
        //assertEquals(esperado,actual+1);
    }
    
    @Test
    public void testInsertar() {
        PersistenciaBMT instance = new PersistenciaBMT();
        Pais pais = new Pais();
        pais.setNombre("Colombia");
        int actual = instance.findAll(Pais.class).size();
        instance.create(pais);
        int esperado = instance.findAll(Pais.class).size();
        assertEquals(esperado, actual + 1);
    }
}
