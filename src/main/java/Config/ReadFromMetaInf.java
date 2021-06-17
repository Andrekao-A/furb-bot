package Config;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.JsonParser;

import cronapi.CronapiMetaData;
import cronapi.ParamMetaData;
import cronapi.Var;
import cronapi.CronapiMetaData.ObjectType;
import Config.ReadFromMetaInf;


/**
 * Ler json do meta-inf ...
 * 
 * @author Usuário de Teste
 * @version 1.0
 * @since 2018-06-20
 *
 */
 
@CronapiMetaData(categoryName = "Config") 
public class ReadFromMetaInf {

	@CronapiMetaData(type = "function", name = "readFromMetaInf", description = "Ler json do meta-inf", returnType = ObjectType.STRING)
	public static Var readFromMetaInf() throws Exception {
				
			ClassLoader classLoader = ReadFromMetaInf.class.getClassLoader();
			try {
				InputStream stream = classLoader.getResourceAsStream("META-INF" +File.separator +"config-api.json");
				if (stream != null) {
					InputStreamReader reader = new InputStreamReader(stream);
					 return Var.valueOf(new JsonParser().parse(reader));
					}
				}
			catch(Exception e){
			  throw new RuntimeException("Não foi possível ler o JSON");
			}
				
		
		return Var.VAR_NULL;
	}

}


