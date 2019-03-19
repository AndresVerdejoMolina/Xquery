package Acceso_a_eXist_desde_Java;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import net.xqj.exist.ExistXQDataSource;

public class Problemas_XQJ_2_Ej2 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		/*
		 * A partir de los documentos productos.xml y zonas.xml, haz un programa que
			reciba un número de zona por parámetro y genere un documento con nombre
			zonaXX.xml donde XX es la zona solicitada. El documento debe contener los
			productos de esta zona y las siguientes etiquetas: <cod_prod>,
			<denominación>, <precio>, <nombre_zona>, <director> y <stock>. Donde el
			stock se calcula restando el stock actual y el stock mínimo.
		 */
		
		File archivo= null;
		FileReader fr = null;
		BufferedReader br = null;
		String guardarzona="";
		
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
			System.out.print("Elije la zona: ");
			int zona = leer.nextInt();
			
			consulta = conn.prepareExpression ("xquery version \"3.1\";\r\n" + 
					"for $zona in doc('nueva/zonas.xml')/zonas/zona[cod_zona = "+ zona + "]\r\n" + 
					"for $product in doc('nueva/productos.xml')/productos/produc[cod_zona = " + zona + "]\r\n" + 
					"return ($product/cod_prod, $product/denominacion, $product/precio, $zona/nombre, $zona/director)");
			resultado = consulta.executeQuery();
			
			BufferedWriter writer = new BufferedWriter(new FileWriter("zona" + zona + ".xml"));
			
			writer.write("<?xml version='1.0' encoding='UTF-8'?>");
			writer.newLine();
			
			while (resultado.next()) {
				String cad = resultado.getItemAsString(null);
				System.out.println(cad);
				writer.write(cad);
				writer.newLine();
			}		
			writer.close();
			conn.close();
		} catch (XQException ex) {System.out.println("Error al operar"+ex.getMessage());}

	}

}
