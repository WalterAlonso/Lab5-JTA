/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losalpes.servicios;

import com.losalpes.entities.Pais;
import com.losalpes.entities.Vendedor;
import java.util.Properties;
import javax.naming.InitialContext;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author WAlonsoR
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PersistenciaCMTTest {

    /**
     * Interface con referencia al servicio de BMT en el sistema
     */
    private IPersistenciaCMTMockRemote servicioCMTRemoto;

    @Before
    public void setUp() throws Exception {
        try {
            Properties env = new Properties();
            env.put("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
            env.put("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
            env.put("org.omg.CORBA.ORBInitialPort", "3700");
            InitialContext contexto;
            contexto = new InitialContext(env);
            servicioCMTRemoto = (IPersistenciaCMTMockRemote) contexto.lookup("com.losalpes.servicios.IPersistenciaCMTMockRemote");

            //Servicio oracle
            //Servicio Derby
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Test
    public void testInsertar() throws Exception {

        Pais pais = new Pais();
        pais.setNombre("Venezuela");
        int actual = servicioCMTRemoto.length(Pais.class);
        System.out.println("Actual: " + actual);
        servicioCMTRemoto.insertar(pais);
        int esperado = servicioCMTRemoto.length(Pais.class);
        System.out.println("Esperado: " + esperado);
        assertEquals(esperado, actual + 1);
    }

    @Test
    public void testInsertarVendedor() throws Exception {

        Vendedor vendedor = new Vendedor();

        vendedor.setNombres("Juan");
        vendedor.setApellidos("Paz");
        vendedor.setComisionVentas(0.2);
        vendedor.setFoto("Foto Vendedor");
        vendedor.setIdentificacion(1018445022);
        vendedor.setPerfil("jspaz");
        vendedor.setSalario(12000000);

        int actual = servicioCMTRemoto.length(Vendedor.class);
        System.out.println("Actual: " + actual);
        servicioCMTRemoto.insertarVendedor(vendedor);
        int esperado = servicioCMTRemoto.length(Vendedor.class);
        System.out.println("Esperado: " + esperado);
        assertEquals(esperado, actual + 1);
    }

    @Test
    public void testVendedorEliminar() throws Exception {
        long idVendedor = 1018445022;

        Vendedor vendedor = servicioCMTRemoto.buscarVendedor(idVendedor);
        int actual = servicioCMTRemoto.length(Vendedor.class);
        servicioCMTRemoto.eliminarVendedor(vendedor);
        int esperado = servicioCMTRemoto.length(Vendedor.class);
        assertEquals(esperado, actual - 1);
    }
}
