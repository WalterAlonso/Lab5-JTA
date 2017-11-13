/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losalpes.servicios;

import com.losalpes.entities.Mueble;
import com.losalpes.entities.Pais;
import com.losalpes.entities.RegistroVenta;
import com.losalpes.entities.TarjetaCreditoAlpes;
import com.losalpes.entities.TipoMueble;
import com.losalpes.entities.TipoUsuario;
import com.losalpes.entities.Usuario;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import javax.naming.InitialContext;
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
    
     /**
     * Interface con referencia al servicio de BMT en el sistema
     */
    private IPersistenciaBMTMockRemote servicioBMTRemoto;
    
//
//private ServicioOracle....

//
//private ServicioDerby....    
    
    
    @Before
    public void setUp() throws Exception  {
        try
        {
            Properties env = new Properties();
            env.put("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
            env.put("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
            env.put("org.omg.CORBA.ORBInitialPort", "3700");
            InitialContext contexto;
            contexto = new InitialContext(env);
            servicioBMTRemoto = (IPersistenciaBMTMockRemote) contexto.lookup("com.losalpes.servicios.IPersistenciaBMTMockRemote");
            
            //Servicio oracle
            //Servicio Derby
        } 
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
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
        
        //define usuario:      
        Usuario usuario = new Usuario("user", "pepito", TipoUsuario.Cliente);
        //oracle.create(usuario);
        
        //la tarjeta con cupo de 10000
        TarjetaCreditoAlpes tarjeta = new TarjetaCreditoAlpes("pepito", "Bancolombia", 10000, new Date(2017,01,15), new Date(2019,01,15), "user");
        //derby.create(tarjeta);
        
        ArrayList<Mueble> muebles = new ArrayList();
        Mueble m1 = new Mueble(1L, "Silla clásica", "Una confortable silla con estilo del siglo XIX.", TipoMueble.Interior, 45, "sillaClasica", 123);
        //oracle.create(m1);
        
        //Define el mueble
        RegistroVenta v = new RegistroVenta();
        v.setCantidad(1);
        v.setCiudad("Bogota");
        v.setComprador(usuario);
        v.setProducto(m1);
        v.setRegistro(1);
        
        servicioBMTRemoto.comprar(v);
        //se obtiene la venta
        
        String query = "Select c FROM TarjetaCreditoAlpes c "
                        + "Where c.login = " + usuario.getLogin();
                 
        //var tarjet = derby.findByQuery(query, cantidad);
        //TarjetaCreditoAlpes tarjet = derby.findByQuery(query, cantidad);
        TarjetaCreditoAlpes target = new TarjetaCreditoAlpes();
        assertEquals(9877, target.getCupo());
        
        
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
