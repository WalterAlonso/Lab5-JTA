package com.losalpes.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-11-12T17:49:36")
@StaticMetamodel(TarjetaCreditoAlpes.class)
public class TarjetaCreditoAlpes_ { 

    public static volatile SingularAttribute<TarjetaCreditoAlpes, Date> fechaExpedicion;
    public static volatile SingularAttribute<TarjetaCreditoAlpes, Long> numero;
    public static volatile SingularAttribute<TarjetaCreditoAlpes, Date> fechaVencimiento;
    public static volatile SingularAttribute<TarjetaCreditoAlpes, String> banco;
    public static volatile SingularAttribute<TarjetaCreditoAlpes, String> nombreTitular;
    public static volatile SingularAttribute<TarjetaCreditoAlpes, String> login;
    public static volatile SingularAttribute<TarjetaCreditoAlpes, Double> cupo;

}