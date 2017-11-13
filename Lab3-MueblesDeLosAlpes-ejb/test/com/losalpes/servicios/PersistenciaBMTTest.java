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
import com.losalpes.entities.Vendedor;
import com.losalpes.excepciones.OperacionInvalidaException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.naming.InitialContext;
import org.junit.After;
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
public class PersistenciaBMTTest {

    /**
     * Interface con referencia al servicio de BMT en el sistema
     */
    private IPersistenciaBMTMockRemote servicioBMTRemoto;
    
    /**
     * Interface con referencia al servicio de persistencia de oracle
     */
    private IServicioPersistenciaMockRemote servicioOracle;

    /**
     * Interface con referencia al servicio de persistencia de derby
     */
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
        } catch (Exception e) {
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
    public void testComprar_TransaccionSatisfactoria() throws OperacionInvalidaException {        
        Usuario usuario = crearUsuario("user");
        Mueble mueble = crearMueble(1000, 1);
        crearTarjeta(usuario, 1000);
        
        //Se crea la venta        
        RegistroVenta v = new RegistroVenta();
        v.setCantidad(1);
        v.setCiudad("Bogota");
        v.setComprador(usuario);
        v.setProducto(mueble);
        v.setRegistro(1);
                
        servicioBMTRemoto.comprar(v);
        
        TarjetaCreditoAlpes tarjet = buscarTarjetaUsuario(usuario);
        assertEquals(999.0, tarjet.getCupo(),0);
    }

    /**
     * Prueba para validar el cupo insuficiente
     */
   @Test
    public void testComprar_TransaccionConCupoInsuficiente() throws OperacionInvalidaException {
        Usuario usuario = crearUsuario("user");
        Mueble mueble = crearMueble(11, 1);
        crearTarjeta(usuario, 10);
        
        //Se crea la venta        
        RegistroVenta v = new RegistroVenta();
        v.setCantidad(11);
        v.setCiudad("Bogota");
        v.setComprador(usuario);
        v.setProducto(mueble);
        v.setRegistro(1);
                
        servicioBMTRemoto.comprar(v);
        
        TarjetaCreditoAlpes tarjet = buscarTarjetaUsuario(usuario);
        assertEquals(10.0, tarjet.getCupo(),0);
    }

    @Test
    public void testInsertar() throws Exception {

        Pais pais = new Pais();
        pais.setNombre("Venezuela");
        int actual = servicioBMTRemoto.length(Pais.class);
        System.out.println("Actual: " + actual);
        servicioBMTRemoto.insertar(pais);
        int esperado = servicioBMTRemoto.length(Pais.class);
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

        int actual = servicioBMTRemoto.length(Vendedor.class);
        System.out.println("Actual: " + actual);
        servicioBMTRemoto.insertarVendedor(vendedor);
        int esperado = servicioBMTRemoto.length(Vendedor.class);
        System.out.println("Esperado: " + esperado);
        assertEquals(esperado, actual + 1);
    }

    @Test
    public void testVendedorEliminar() throws Exception {
        long idVendedor = 1018445022;

        Vendedor vendedor = servicioBMTRemoto.buscarVendedor(idVendedor);
        int actual = servicioBMTRemoto.length(Vendedor.class);
        servicioBMTRemoto.eliminarVendedor(vendedor);
        int esperado = servicioBMTRemoto.length(Vendedor.class);
        assertEquals(esperado, actual - 1);
    }    
    
    
    /** Metodos helper **/
    private Usuario crearUsuario(String login) throws OperacionInvalidaException{
        Usuario usuario = (Usuario)servicioOracle.findById(Usuario.class, login);
        if (usuario == null){
            Ciudad bog = new Ciudad();
            bog.setNombre("Bogota");
            ArrayList<Ciudad> ciudades = new ArrayList<>();
            ciudades.add(bog);
            Pais pais = new Pais();
            pais.setNombre("Colombia");
            pais.setCiudades(ciudades);
            servicioOracle.create(pais);

            //define usuario:      
            usuario = new Usuario(login, "pepito", TipoUsuario.Cliente, "peipto p", 1014207335, TipoDocumento.CC, 4300012, 301309301, bog, "dg", Profesion.Administrador, "correotest@g.com");
            servicioOracle.create(usuario);
        }
        return usuario;
    }
        
    private TarjetaCreditoAlpes crearTarjeta(Usuario usuario, double cupo) throws OperacionInvalidaException{                  
        TarjetaCreditoAlpes tarjeta = buscarTarjetaUsuario(usuario);
        if (tarjeta == null){
            tarjeta = new TarjetaCreditoAlpes("pepito", "Bancolombia", cupo, new Date(2017, 01, 15), new Date(2019, 01, 15), usuario.getLogin());
            servicioDerby.create(tarjeta);
        } else
        {
            tarjeta.setCupo(cupo);
            servicioDerby.update(tarjeta);
        }
        return tarjeta;
    }
    
    
    private TarjetaCreditoAlpes buscarTarjetaUsuario(Usuario usuario) {
        TarjetaCreditoAlpes tarjeta =  (TarjetaCreditoAlpes)servicioDerby.findSingleByQuery
        ("Select c FROM TarjetaCreditoAlpes c "
                + "Where c.login = '" + usuario.getLogin()+"'");
        return tarjeta;
    }
    
    private Mueble crearMueble(int cantidad, double precio) throws OperacionInvalidaException{        
        Mueble m1 = (Mueble)servicioOracle.findById(Mueble.class, 1L);
        if (m1 == null){
            m1 = new Mueble(1L, "Silla clásica", "Una confortable silla con estilo del siglo XIX.", TipoMueble.Interior, cantidad, "sillaClasica", precio);
            servicioOracle.create(m1);
        }
        return m1;
    }
}
