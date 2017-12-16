package com.ucuau.datawarehousing.mongodb.test;

import java.io.IOException;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.ucuau.datawarehousing.mongodb.vo.MaterialDidacticoVO;

public class MongoDBCliente {
	private Scanner reader = new Scanner(System.in);
	
	public Document insertarMaterial(MongoCollection<Document> table, MaterialDidacticoVO md){
		Document std = new Document();
		std.put("idmaterial", md.getIdmaterial());
		std.put("nombre", md.getNombre());
		std.put("tipo", md.getTipo());
		std.put("url", md.getUrl());
		
		table.insertOne(std);
		return std;
	}
	
	private MaterialDidacticoVO buscarPorId(MongoCollection<Document> table){
		ObjectMapper mapper = new ObjectMapper();
		BasicDBObject search = new BasicDBObject();
		MaterialDidacticoVO obj = null;
		System.out.println("Ingrese id material: ");
		String id = reader.next();
		
		search.put("idmaterial", id);
		FindIterable<Document> cursor = table.find(search);
		MongoCursor<Document> it = cursor.iterator();
		
		if(it.hasNext()) {
			Document doc = it.next();
			String json = doc.toJson();
			try{
				obj = mapper.readValue(json, MaterialDidacticoVO.class);
			}catch (JsonMappingException err){
				err.printStackTrace();
			}catch (IOException err){
				err.printStackTrace();
			}catch (JsonParseException err){
				err.printStackTrace();
			}
			    
		}
		return obj;
	}
	
	public void actualizarMaterial(MongoCollection<Document> table){
		BasicDBObject updateQuery = new BasicDBObject();
		BasicDBObject searchQuery = new BasicDBObject();
		String valor = "";
		MaterialDidacticoVO obj = this.buscarPorId(table);
		
		if(obj != null){
			System.out.println("Seleccione elemento a actualizar ");
			System.out.println("(N)ombre ");
			System.out.println("(T)ipo ");
			System.out.println("(U)rl ");	
			
			String elem = reader.next();
		
			if(elem.toUpperCase().equals("N")){
				System.out.println("Ingrese el nombre:");
				valor = reader.next();	
			
				updateQuery.append("$set",
						new BasicDBObject().append("nombre", valor));			
			
			} else if(elem.toUpperCase().equals("T")){
				System.out.println("Ingrese tipo (L)ibro (D)ocumental (P)ágina Web: ");
				valor = reader.next();
				
				if(valor.toUpperCase().equals("L")){
					valor = "Libro";
				} else if(valor.toUpperCase().equals("D")){
					valor = "Documental";
				} else if(valor.toUpperCase().equals("P")){
					valor = "Web Page";
				} else {
					valor = "No especificado";			
				}
				updateQuery.append("$set",
						new BasicDBObject().append("tipo", valor));	
				
			} else if(elem.toUpperCase().equals("U")){
				System.out.println("Ingrese la url:");
				valor = reader.next();
				updateQuery.append("$set",
						new BasicDBObject().append("url", valor));	
				
			}
			searchQuery.append("idmaterial", obj.getIdmaterial());

			UpdateResult ur = table.updateOne(searchQuery, updateQuery);
			
			System.out.println("Se modificó el registro:" + ur.getModifiedCount());
		}
		
		System.out.println("Presione una tecla para continuar...");
		reader.next();
	}
	
	public void buscarMaterial(MongoCollection<Document> table){
		MaterialDidacticoVO obj = null;
		ObjectMapper mapper = new ObjectMapper();
		BasicDBObject whereQuery = new BasicDBObject();
		
		System.out.println("Ingrese tipo ((L)ibro (D)ocumental (P)ágina Web): ");
		String md = reader.next();
		
		if(md.toUpperCase().equals("L")){
			md = "Libro";
		} else if(md.toUpperCase().equals("D")){
			md = "Documental";
		} else if(md.toUpperCase().equals("P")){
			md = "Web Page";
		} else {
			md = "No especificado";			
		}
		
		whereQuery.put("tipo", md);
		FindIterable<Document> cursor = table.find(whereQuery);
		MongoCursor<Document> it = cursor.iterator();
		
		while(it.hasNext()) {
			obj = null;
			Document doc = it.next();
			String json = doc.toJson();
			try{
				obj = mapper.readValue(json, MaterialDidacticoVO.class);
			}catch (JsonMappingException err){
				err.printStackTrace();
			}catch (IOException err){
				err.printStackTrace();
			}catch (JsonParseException err){
				err.printStackTrace();
			}
			
		    System.out.println(obj);		    
		}
		System.out.println("Presione una tecla para continuar...");
		reader.next();
	}
	
	public void borrarMaterial(MongoCollection<Document> table){		
		
		MaterialDidacticoVO obj = this.buscarPorId(table);
		
		if(obj != null){
			System.out.println("Está seguro de borrar el material (S/N): ");
			System.out.println(obj);
			String op = reader.next();
			if(op.toUpperCase().equals("S")){
				Bson filter = new Document("idmaterial", obj.getIdmaterial());
				
				DeleteResult dr = table.deleteOne(filter);
				
				System.out.println("Se eliminó " + dr.getDeletedCount());
			}
		} else {
			System.out.println("El material no existe");
		}
		System.out.println("Presione una tecla para continuar...");
		reader.next();
	}
	
	public void listarMateriales(MongoCollection<Document> table){
		MaterialDidacticoVO obj = null;
		ObjectMapper mapper = new ObjectMapper();
		
		FindIterable<Document> cursor = table.find();
		MongoCursor<Document> it = cursor.iterator();
		
		while(it.hasNext()) {
			obj = null;
			Document doc = it.next();
			String json = doc.toJson();
			try{
				obj = mapper.readValue(json, MaterialDidacticoVO.class);
			}catch (JsonMappingException err){
				err.printStackTrace();
			}catch (IOException err){
				err.printStackTrace();
			}catch (JsonParseException err){
				err.printStackTrace();
			}
			
		    System.out.println(obj);		    
		}
		System.out.println("Presione una tecla para continuar...");
		reader.next();
	}
	
	public void capturaMaterial(MongoCollection<Document> table){
		String op = "";
		do{
			MaterialDidacticoVO md = new MaterialDidacticoVO();
			System.out.println(repeat("\n", 80));
			System.out.println(repeat("*", 40));
			
			System.out.println("Ingrese nombre:");
			md.setNombre(reader.next());
			System.out.println("Ingrese tipo (L)ibro (D)ocumental (P)ágina Web:");
			md.setTipo(reader.next());
			md.setTipo(md.getTipo().toUpperCase());
			System.out.println("Ingrese URL:");
			md.setUrl(reader.next());
			
			if(md.getTipo().equals("L")){
				md.setTipo("Libro");
			} else if(md.getTipo().equals("D")){
				md.setTipo("Documental");
			} else if(md.getTipo().equals("P")){
				md.setTipo("Web Page");
			} else {
				md.setTipo("No especificado");			
			}
			System.out.println("Count " + table.count());
			md.setIdmaterial( "" + (table.count() + 1) );
						
			this.insertarMaterial(table, md);
			System.out.println(md);
			System.out.println("Correcto (S/N)?");
			op = reader.next();
			
		}while(op.toUpperCase().equals("N"));
	}

	public MongoClient conectarMongo(String host){
		MongoClient mongo = new MongoClient( host , 27017 );
		return mongo;
	}
	
	
	public String repeat(String str, int times) {
        return new String(new char[times]).replace("\0", str);
    }
	
	public void consola(MongoDatabase db){			
		MongoCollection<Document> table = db.getCollection("materiales");	
	
		do{
			System.out.println(repeat("\n", 80));
			
			System.out.println(                                                                                                                                       
			"MMMMMMMM               MMMMMMMM                                                                        DDDDDDDDDDDDD      BBBBBBBBBBBBBBBBB      \n" + 
			"M:::::::M             M:::::::M                                                                        D::::::::::::DDD   B::::::::::::::::B     \n" +
			"M::::::::M           M::::::::M                                                                        D:::::::::::::::DD B::::::BBBBBB:::::B    \n" + 
			"M:::::::::M         M:::::::::M                                                                        DDD:::::DDDDD:::::DBB:::::B     B:::::B   \n" + 
			"M::::::::::M       M::::::::::M   ooooooooooo   nnnn  nnnnnnnn       ggggggggg   ggggg   ooooooooooo     D:::::D    D:::::D B::::B     B:::::B   \n" + 
			"M:::::::::::M     M:::::::::::M oo:::::::::::oo n:::nn::::::::nn    g:::::::::ggg::::g oo:::::::::::oo   D:::::D     D:::::DB::::B     B:::::B   \n" + 
			"M:::::::M::::M   M::::M:::::::Mo:::::::::::::::on::::::::::::::nn  g:::::::::::::::::go:::::::::::::::o  D:::::D     D:::::DB::::BBBBBB:::::B    \n" + 
			"M::::::M M::::M M::::M M::::::Mo:::::ooooo:::::onn:::::::::::::::ng::::::ggggg::::::ggo:::::ooooo:::::o  D:::::D     D:::::DB:::::::::::::BB     \n" + 
			"M::::::M  M::::M::::M  M::::::Mo::::o     o::::o  n:::::nnnn:::::ng:::::g     g:::::g o::::o     o::::o  D:::::D     D:::::DB::::BBBBBB:::::B    \n" + 
			"M::::::M   M:::::::M   M::::::Mo::::o     o::::o  n::::n    n::::ng:::::g     g:::::g o::::o     o::::o  D:::::D     D:::::DB::::B     B:::::B   \n" + 
			"M::::::M    M:::::M    M::::::Mo::::o     o::::o  n::::n    n::::ng:::::g     g:::::g o::::o     o::::o  D:::::D     D:::::DB::::B     B:::::B   \n" + 
			"M::::::M     MMMMM     M::::::Mo::::o     o::::o  n::::n    n::::ng::::::g    g:::::g o::::o     o::::o  D:::::D    D:::::D B::::B     B:::::B   \n" + 
			"M::::::M               M::::::Mo:::::ooooo:::::o  n::::n    n::::ng:::::::ggggg:::::g o:::::ooooo:::::oDDD:::::DDDDD:::::DBB:::::BBBBBB::::::B   \n" + 
			"M::::::M               M::::::Mo:::::::::::::::o  n::::n    n::::n g::::::::::::::::g o:::::::::::::::oD:::::::::::::::DD B:::::::::::::::::B    \n" +
			"M::::::M               M::::::M oo:::::::::::oo   n::::n    n::::n  gg::::::::::::::g  oo:::::::::::oo D::::::::::::DDD   B::::::::::::::::B     \n" + 
			"MMMMMMMM               MMMMMMMM   ooooooooooo     nnnnnn    nnnnnn    gggggggg::::::g    ooooooooooo   DDDDDDDDDDDDD      BBBBBBBBBBBBBBBBB      \n" + 
			"                                                                              g:::::g                                                            \n" + 
			"                                                                  gggggg      g:::::g                                                            \n" +
			"                                                                  g:::::gg   gg:::::g                                                            \n" + 
			"                                                                   g::::::ggg:::::::g                                                            \n" +
			"                                                                    gg:::::::::::::g                                                             \n" + 
			"                                                                      ggg::::::ggg                                                               \n" +
			"                                                                         gggggg 																 \n"
			);		                                                                         
			System.out.println(repeat("*", 40));
			System.out.println("Universidad Cuauhtémoc. DataWarehousing e Inteligencia de Negocios\n");			
			System.out.println("(I)nsertar material");
			System.out.println("(M)odificar material");
			System.out.println("(E)liminar material");
			System.out.println("(B)uscar material");			
			System.out.println("(L)istar materiales\n");
			System.out.println("(D)estruir colección");
			System.out.println("(S)alir");
			System.out.println(repeat("*", 40));
			try{
				char c = (char) System.in.read();
				switch(c){
				case 'i':
					capturaMaterial(table);
					break;
				case 'm':
					actualizarMaterial(table);
					break;
				case 'e':
					borrarMaterial(table);
					break;
				case 'b':
					buscarMaterial(table);
					break;
				case 'd':
					System.out.println("Esta acción borrará todos los datos, continuar? (S/N)");
					String op = reader.next();
					if(op.toUpperCase().equals("S")){
						table.drop();
					}
					break;
				case 'l':
					listarMateriales(table);
					break;
				case 's':
					System.out.println("Adios!!!");
					System.exit(0);
				}
			}catch(IOException err){
				err.printStackTrace();
			}
		}while(true);
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args){		
		final MongoDBCliente cliente = new MongoDBCliente();			
		final MongoClient mongo = cliente.conectarMongo("localhost");
		
		// Si la base no existe, mongo la crea automaticamente			
		final MongoDatabase db = mongo.getDatabase("ucuaudb");
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				cliente.consola(db);
			}
		});
		
	}

}
