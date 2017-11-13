package com.losalpes.entities;

import com.losalpes.entities.Ciudad;
import com.losalpes.entities.Profesion;
import com.losalpes.entities.RegistroVenta;
import com.losalpes.entities.TipoDocumento;
import com.losalpes.entities.TipoUsuario;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-11-13T12:01:48")
@StaticMetamodel(Usuario.class)
public class Usuario_ { 

    public static volatile SingularAttribute<Usuario, String> direccion;
    public static volatile SingularAttribute<Usuario, Long> documento;
    public static volatile SingularAttribute<Usuario, String> login;
    public static volatile SingularAttribute<Usuario, String> nombreCompleto;
    public static volatile SingularAttribute<Usuario, Long> telefonoCelular;
    public static volatile SingularAttribute<Usuario, TipoDocumento> tipoDocumento;
    public static volatile ListAttribute<Usuario, RegistroVenta> compras;
    public static volatile SingularAttribute<Usuario, Ciudad> ciudad;
    public static volatile SingularAttribute<Usuario, String> correo;
    public static volatile SingularAttribute<Usuario, TipoUsuario> tipoUsuario;
    public static volatile SingularAttribute<Usuario, Long> telefonoLocal;
    public static volatile SingularAttribute<Usuario, Profesion> profesion;
    public static volatile SingularAttribute<Usuario, String> contraseña;

}