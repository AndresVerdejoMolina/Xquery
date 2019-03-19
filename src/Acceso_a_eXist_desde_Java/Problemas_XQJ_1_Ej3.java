package Acceso_a_eXist_desde_Java;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import net.xqj.exist.ExistXQDataSource;
public class Problemas_XQJ_1_Ej3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Realiza un programa que devuelva el número de productos con precio mayor a 50.
		try{
			XQDataSource server = new ExistXQDataSource();
			server.setProperty ("serverName", "192.168.56.102");
			server.setProperty ("port","8080");
			server.setProperty ("user","admin");
			server.setProperty ("password","austria");
			XQConnection conn = server.getConnection();
			XQPreparedExpression consulta;
			XQResultSequence resultado;
			consulta = conn.prepareExpression ("for $pr in doc('nueva/productos.xml')/productos/produc[precio > 50] return $pr");
			resultado = consulta.executeQuery();
			while (resultado.next()) {
				System.out.println(resultado.getItemAsString(null));
			}
			conn.close();
		} catch (XQException ex) {System.out.println("Error al operar"+ex.getMessage());}
		

	}

}
