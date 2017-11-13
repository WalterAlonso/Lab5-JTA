/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losalpes.servicios;

import com.losalpes.entities.Pais;
import java.util.Properties;
import javax.naming.InitialContext;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author WAlonsoR
 */
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
        int actual = servicioCMTRemoto.findAll(Pais.class).size();
        servicioCMTRemoto.create(pais);
        int esperado = servicioCMTRemoto.findAll(Pais.class).size();
        assertEquals(esperado, actual + 1);
    }
}
