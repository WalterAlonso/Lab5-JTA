/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losalpes.servicios;

import com.losalpes.entities.Ciudad;
import com.losalpes.entities.Mueble;
import com.losalpes.entities.Pais;
import com.losalpes.entities.Profesion;
import com.losalpes.entities.RegistroVenta;
import com.losalpes.entities.TarjetaCreditoAlpes;
import com.losalpes.entities.TipoDocumento;
import com.losalpes.entities.TipoMueble;
import com.losalpes.entities.TipoUsuario;
import com.losalpes.entities.Usuario;
import com.losalpes.excepciones.OperacionInvalidaException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    private IServicioPersistenciaMockRemote servicioOracle;

    private IServicioPersistenciaDerbyMockRemote servicioDerby;

    @Before
    public void setUp() throws Exception {
        try {
            Properties env = new Properties();
            env.put("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
            env.put("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
            env.put("org.omg.CORBA.ORBInitialPort", "3700");
            InitialContext contexto;
            contexto = new InitialContext(env);
            servicioBMTRemoto = (IPersistenciaBMTMockRemote) contexto.lookup("com.losalpes.servicios.IPersistenciaBMTMockRemote");
            servicioOracle = (IServicioPersistenciaMockRemote) contexto.lookup("com.losalpes.servicios.IServicioPersistenciaMockRemote");
            servicioDerby = (IServicioPersistenciaDerbyMockRemote) contexto.lookup("com.losalpes.servicios.IServicioPersistenciaDerbyMockRemote");

            initDataBase();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void initDataBase() {

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
    public void testComprar_TransaccionSatisfactoria() throws OperacionInvalidaException {
        System.out.println("comprar");

        List a = servicioOracle.findAll(Pais.class);
        Ciudad bog = new Ciudad();
        bog.setNombre("Bogota");
        ArrayList<Ciudad> ciudades = new ArrayList<>();
        ciudades.add(bog);
        Pais pais = new Pais();
        pais.setNombre("Colombia");
        pais.setCiudades(ciudades);
        //servicioOracle.create(pais);

        //define usuario:      
        Usuario usuario = new Usuario("user", "pepito", TipoUsuario.Cliente, "peipto p", 1014207335, TipoDocumento.CC, 4300012, 301309301, bog, "dg", Profesion.Administrador, "correotest@g.com");
        //servicioOracle.create(usuario);

        //la tarjeta con cupo de 10000
        TarjetaCreditoAlpes tarjeta = new TarjetaCreditoAlpes("pepito", "Bancolombia", 10000, new Date(2017, 01, 15), new Date(2019, 01, 15), "user");
        servicioDerby.create(tarjeta);

        ArrayList<Mueble> muebles = new ArrayList();
        Mueble m1 = new Mueble(1L, "Silla clásica", "Una confortable silla con estilo del siglo XIX.", TipoMueble.Interior, 45, "sillaClasica", 123);
        servicioOracle.create(m1);

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
        List<Object[]> list = servicioDerby.findByQuery(query);
        TarjetaCreditoAlpes tarjet = (TarjetaCreditoAlpes) list.get(0)[0];
        //TarjetaCreditoAlpes target = new TarjetaCreditoAlpes();
        assertEquals(9877, tarjet.getCupo());
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
    public void testInsertar() throws Exception {

        Pais pais = new Pais();
        pais.setNombre("Venezuela");
        int actual = servicioBMTRemoto.findAll(Pais.class).size();
        servicioBMTRemoto.create(pais);
        int esperado = servicioBMTRemoto.findAll(Pais.class).size();
        assertEquals(esperado, actual + 1);
    }

    @Test
    public void testInsertarTC() throws Exception {
        TarjetaCreditoAlpes tc = new TarjetaCreditoAlpes();
        tc.setBanco("Col");
        tc.setCupo(3200000);
        tc.setFechaExpedicion(new Date());
        tc.setFechaVencimiento(new Date());
        tc.setLogin("admin");
        tc.setNombreTitular("Juan Paz");
        tc.setNumero(Long.getLong("123143241231"));

        int actual = servicioBMTRemoto.findAllTC().size();
        System.out.println("Actual: " + actual);
        servicioBMTRemoto.insertarTC(tc);
        int esperado = servicioBMTRemoto.findAllTC().size();
        System.out.println("Actual: " + esperado);
        assertEquals(esperado, actual + 1);
    }

    @Test
    public void testSome() {

        Pais pais = new Pais();
        pais.setNombre("Colombia");

        assertEquals("Colombia", pais.getNombre());
    }
}
