package Acceso_a_eXist_desde_Java;

import java.util.Scanner;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import net.xqj.exist.ExistXQDataSource;

public class Problemas_XQJ_2_Ej1 {

	/*
	 * A partir del documento universidad.xml, haz un programa que muestre los
		empleados del departamento cuyo tipo es elegido por el usuario. Si no hay
		empleados o el tipo de departamento aportado por el usuario no existe, se
		debe de informar al usuario.

	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			XQDataSource server = new ExistXQDataSource();
			server.setProperty ("serverName", "192.168.56.102");
			server.setProperty ("port","8080");
			server.setProperty ("user","admin");
			server.setProperty ("password","austria");
			XQConnection conn = server.getConnection();
			XQPreparedExpression consulta;
			XQResultSequence resultado;
			Scanner leer = new Scanner(System.in);
			System.out.print("Elije el departamento: ");
			String tipo = leer.nextLine();
			
			consulta = conn.prepareExpression ("for $pr in doc('nueva/universidad.xml')/universidad/departamento[@tipo = '"+tipo+"'] return $pr");
			resultado = consulta.executeQuery();
			
			if(!resultado.next()){
				System.out.println("No existe");
			}else{
				while (resultado.next()) {
				System.out.println(resultado.getItemAsString(null));
			}
			}
			conn.close();
		} catch (XQException ex) {System.out.println("Error al operar"+ex.getMessage());}
	}

}
