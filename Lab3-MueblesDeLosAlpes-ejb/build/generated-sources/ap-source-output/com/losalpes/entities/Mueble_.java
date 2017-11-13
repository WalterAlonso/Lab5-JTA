package com.losalpes.entities;

import com.losalpes.entities.TipoMueble;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-11-13T17:41:47")
@StaticMetamodel(Mueble.class)
public class Mueble_ { 

    public static volatile SingularAttribute<Mueble, String> descripcion;
    public static volatile SingularAttribute<Mueble, TipoMueble> tipo;
    public static volatile SingularAttribute<Mueble, Double> precio;
    public static volatile SingularAttribute<Mueble, String> imagen;
    public static volatile SingularAttribute<Mueble, Integer> cantidad;
    public static volatile SingularAttribute<Mueble, String> nombre;
    public static volatile SingularAttribute<Mueble, Long> referencia;

}