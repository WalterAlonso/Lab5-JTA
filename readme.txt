Integrantes:

Walter Alonso.
Juan Sebastian Paz.

Instrucciones:
Las pruebas implementadas consultan las interfaces remotas de los Beans asociados, por lo cual se debe desplegar primero
el  proyecto EJB.

Al correr las pruebas, por favor realice deploys independientes de EJB, por cada clase que valla a correr.
Es decir, si va a correr las pruebas unitarias de PersistenciaBMTTest haga deploy de EJB.
Y si va a testear PersistenciaCMTTest vuelva a hacer despliegue de EJB (esto con el objetivo de que se redesplieguen los objetos de la bd)

---------------------------------------------------------------------------------------------------------------
**Para correr el proyecto usd debe crear los respectivos pools, por lo cual corra estos comandos en glassfish:

-asadmin
-create-jdbc-connection-pool --datasourceclassname org.apache.derby.jdbc.ClientXADataSource --restype javax.sql.XADataSource --property portNumber=1527:password=app:user=app:serverName=localhost:databaseName=sample:connectionAttributes=\;create\\=true MueblesAlpesDerby
-create-jdbc-resource --connectionpoolid MueblesAlpesDerby jdbc/BDDerby
-create-jdbc-connection-pool --restype javax.sql.DataSource --datasourceclassname oracle.jdbc.pool.OracleDataSource --property "user=system:password=walter: url=jdbc\\:oracle\\:thin\\:@localhost\\:1521\\:XE" muebles_pool
-create-jdbc-resource --connectionpoolid muebles_pool jdbc/MuebleDeLosAlpesOracle

---------------------------------------------------------------------------------------------------------------
Este proyecto contiene:
- sentencia.txt: la sentencia para la creacion de la entidad TarjetaCreditoAlpes.
- PersistenciaBMT : contiene transaccionalidad BMT.
- PersistenciaCMT: contiene transaccionalidad CMT.
- CupoInsuficienteException: Excepcion de negocio que se llama cuando no hay cupo insuficiente.
- PersistenciaBMTTest: contiene las pruebas sobre el bean PersistenciaBMT.
- PersistenciaCMTTest: Contiene las pruebas sobre el bean PersistenciaCMT.