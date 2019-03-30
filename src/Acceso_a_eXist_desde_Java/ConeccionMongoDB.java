
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList
;
import java.util.List;
import java.util.Scanner;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;


public class ConeccionMongoDB{
	
	static Scanner accion = new Scanner(System.in);
	static String nuevomovil[] = {"Modelo", "Tamaño de pantalla", "Cámara"};
	static int intGeneral=0;	
	static String stringGeneral="";
	static String error_general="Err:\tElije una opcion valida.";
	
	public static void main(String[] args){
		MongoClient cliente = new MongoClient();
		MongoDatabase db = cliente.getDatabase ("Segundamano");
		MongoCollection <Document> coleccion_de_moviles = db.getCollection("Moviles");//Creando base de datos
		
		do {
			System.out.println("¿Que quieres hacer?");
			System.out.println("<1>Insertar coleccion");
			System.out.println("<2>Visualizar coleccion");
			System.out.println("<3>Modificar coleccion");
			System.out.println("<4>Eliminar coleccion");
			System.out.println("<5>Volcar datos a un archivo en el sistema");
			System.out.println("<0>Salir");
			intGeneral=comprobarValorInt();//Funcion que comprueba si el valor de entrada es de tipo int (linea 263)
			
			switch(intGeneral) {
				case 1:{
					insertarDatos(coleccion_de_moviles);//En el caso de que introduzca 1, insertaremos datos (linea 71)
					break;
				}
				case 2:{
					visualizarDatos(coleccion_de_moviles);	//En el caso de que introduzca 2, visualizaremos datos (linea 122)
					break;
					
				}case 3:{
					modificarDatos(coleccion_de_moviles);	//En el caso de que introduzca 3, modificaremos datos (linea 198)
					
					break;					
				}case 4:{
					eliminarDatos(coleccion_de_moviles);	//En el caso de que introduzca 4, eliminaremos datos (linea 178)
					break;
					
				}case 5:{
					volcarDatos(coleccion_de_moviles);		//En el caso de que introduzca 5, volcaremos datos a un archivo del sistema (linea 227)
					break;
					
				}default:{
					break;					
				}
			}
		}while(intGeneral!=0);//En el caso de que introduzca 0, el programa finalizara
		System.out.println("Adios!");		

	}
	
	public static void insertarDatos(MongoCollection<org.bson.Document> coleccion_de_moviles) {
		
		do {
			String modelo_nuevo_movil;
			Double tamaño_pantalla;
			double camara;			
			Double tamaño_pantalla_max=8.0;
			
			System.out.println("Para insertar un nuevo Movil introduce");
			System.out.print("el modelo del movil: ");
			modelo_nuevo_movil=comprobarValorString();//Funcion que comprueba si el valor de entrada es de tipo String (linea 275)
			
			System.out.print("tamaño de pantalla del movil(pulgadas): ");
			tamaño_pantalla=comprobarValorDouble();//Funcion que comprueba si el valor de entrada es de tipo Double (linea 287)
			int retval = tamaño_pantalla.compareTo(tamaño_pantalla_max);//Comparando el tamaño de pantalla introducido con el valor maximo

			while(retval > 0) {//Si la variable devuelve mayor a 0, es que ha introducido un valor mayor que (tamaño_pantalla_max)
				System.out.println("Eso no es un movil, es un tablet.\nIntroduce otra vez el tamaño de pantalla");
				tamaño_pantalla=comprobarValorDouble();
				retval = tamaño_pantalla.compareTo(tamaño_pantalla_max);
			}
			
			System.out.print("los megapixeles de la cámara: ");
			camara=comprobarValorDouble();
			
			Document nuevomovil= new Document();//Creando un nuevo documento
			
			try {				
				nuevomovil.put("Modelo", modelo_nuevo_movil);//Insertando el modelo del nuevo movil
				nuevomovil.put("Tamaño de pantalla(Pulgadas)", tamaño_pantalla);//Insertando el tamaño de pantalla del nuevo movil
				nuevomovil.put("Cámara(Megapixeles)", camara);//Insertando la camara del nuevo movil
				
			}catch(Exception e) {//Si occure un problema al insertar, se imprimira el error y saldremos de la funcion
				System.out.println("Error al insertar: ");
				e.printStackTrace();
				break;
			}
			
			coleccion_de_moviles.insertOne(nuevomovil);//Insertando solamente un nuevo documento
			System.out.println("Se ha insertado correctamente el nuevo movil.");
			System.out.println("Otra vez?");
			System.out.println("<0>Si");
			System.out.println("<1>No");
			
			intGeneral=comprobarValorInt();
			
		}while(intGeneral==0);	
		System.out.println("---------------------------------------");
		
	}
	
	public static void visualizarDatos(MongoCollection<org.bson.Document> coleccion_de_moviles) {
		
		do {
			System.out.println("Visualizar...");
			System.out.println("<1>toda la coleccion");
			System.out.println("<2>por un id en concreto");
			intGeneral=comprobarValorInt();
			
			switch(intGeneral){
			
					case 1:{
							List<Document> consulta = coleccion_de_moviles.find().into(new ArrayList<Document> ());//Realizando query que devuelve toda la coleccion de moviles
							
							if(consulta.size()==0){//En el caso de que la coleccion este vacia
								System.out.println("No hay nada que mostrar");
								break;
							}
							
							for (int i =0; i < consulta.size(); i++) {
								System.out.println(i + "nº " + consulta.get(i).toString());//Recorriendo la consulta e imprimiendola
							}
							break;
							
					}case 2:{
							MongoCursor<Document> cursor = coleccion_de_moviles.find().iterator();
							System.out.println("Escribe el id del movil que quieres buscar: ");
							try {
								stringGeneral = comprobarValorString();
								
								while (cursor.hasNext()) {
									Document doc = cursor.next(); 
									if(doc.getObjectId("_id").equals(new ObjectId(stringGeneral))) {//Obteniendo id del objecto, en el caso de que la id sea igual al que ha introducido por consola, se imprimira ese documento 
										System.out.println (doc.toJson().toString()); 
									}
								}
							}catch(IllegalArgumentException e) {//En el caso de que la id introducida no coincida en ninguna id de un documento
								System.out.println("No se ha encotrado nada con esa id(" + stringGeneral +").");
								break;
								
							}
							cursor.close();
							break;
					}default:{
						break;
					}
			}

			System.out.println("Otra vez?");
			System.out.println("<0>Si");
			System.out.println("<1>No");
			
			intGeneral=comprobarValorInt();
		
		}while(intGeneral==0);	
		System.out.println("---------------------------------------");
		
	}
	
	public static void eliminarDatos(MongoCollection<org.bson.Document> coleccion_de_moviles) {
		
		do {
			
			System.out.println("Para borrar necesito el id:");
			stringGeneral=comprobarValorString();
			coleccion_de_moviles.deleteOne(new Document("_id", new ObjectId(stringGeneral)));//Borrando documento por la id introducida por consola
			
			System.out.println("¿Otra vez?");
			System.out.println("<0>Si");
			System.out.println("<1>No");
			
			intGeneral=comprobarValorInt();
			
		}while(intGeneral==0);
		System.out.println("---------------------------------------");
	}
	
	public static void modificarDatos(MongoCollection<org.bson.Document> coleccion_de_moviles) {
		
		do {
			System.out.println("Para modificar necesito el modelo del movil:");
			String modelo=comprobarValorString();
			Document antiguo_documento = new Document().append("Modelo", modelo);//Obteniendo antiguo documento, filtrando por el modelo del movil
			
			System.out.println("Nuevo valor:");
			String nuevo_valor = comprobarValorString();
			
			Document nuevo_documento = new Document().append("$set", new Document().append("Modelo", nuevo_valor));//Obteniendo creando nuevo valor

			try {
				coleccion_de_moviles.updateOne(antiguo_documento, nuevo_documento);//Actualizando del antiguo documento, al nuevo documento
			}catch(Exception e) {
				System.out.println("Error al modificar:");
				e.printStackTrace();
			}
			
			System.out.println("¿Otra vez?");
			System.out.println("<0>Si");
			System.out.println("<1>No");
			
			intGeneral=comprobarValorInt();
			
		}while(intGeneral==0);
		System.out.println("---------------------------------------");
	}
	
	public static void volcarDatos(MongoCollection<org.bson.Document> coleccion_de_moviles) {
		
		MongoCursor<Document> cursor = coleccion_de_moviles.find().iterator(); 
		
		FileWriter fichero = null;
		PrintWriter pw = null;
		System.out.println("Necesito saber donde quieres guardar los datos, con el nombre del archivo.");
		System.out.println("(Ruta por defecto C:\\Users\\Usuario\\Desktop\\proyectoMongoDB.txt)\nPara aplicar la ruta por defecto, simplemente presiona Enter.");
		String rutaFichero = comprobarValorString();
		try{
			if(rutaFichero.isEmpty() || rutaFichero.equals(" ")) {
				fichero = new FileWriter("C:\\Users\\Usuario\\Desktop\\proyectoMongoDB.txt");
				rutaFichero = "C:\\Users\\Usuario\\Desktop\\proyectoMongoDB.txt";
			}else {
				fichero = new FileWriter(rutaFichero);
			}
			pw = new PrintWriter(fichero);

			System.out.println("Escribe el id del movil que quieres volcar al fichero: ");
			stringGeneral = comprobarValorString();
			while (cursor.hasNext()) {
				Document doc = cursor.next(); 
				if(doc.getObjectId("_id").equals(new ObjectId(stringGeneral))) {//Obteniendo id del objecto, en el caso de que la id sea igual al que ha introducido por consola, se insertaran los datos de ese docunento
					pw.print(doc.toJson());//Ecribiendo en documento
				}
			} 
			cursor.close();
			fichero.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Se ha añadido correctamente a: " + rutaFichero);
		System.out.println("---------------------------------------");
		
	}
	
	public static int comprobarValorInt() {
		Scanner opcion = new Scanner(System.in);
		while(!opcion.hasNextInt()) {
			
			opcion.next();
			System.out.println(error_general);
			
		}
		return opcion.nextInt();		
		
	}
	
	public static String comprobarValorString() {
		Scanner opcion = new Scanner(System.in);
		while(!opcion.hasNextLine()) {
			
			opcion.next();
			System.out.println(error_general);
			
		}
		return opcion.nextLine();		
		
	}
	
	public static Double comprobarValorDouble() {
		Scanner opcion = new Scanner(System.in);
		while(!opcion.hasNextDouble()) {
			
			opcion.next();
			System.out.println(error_general);
			
		}
		return opcion.nextDouble();		
		
	}


}
