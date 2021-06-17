package HardCode;

import java.io.File;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.swing.text.MaskFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cronapi.CronapiMetaData;
import cronapi.CronapiMetaData.ObjectType;
import cronapi.ParamMetaData;
import cronapi.Var; 


/**
 * Classe para a criação de funções de ajustes e tratamento de Dados...
 * 
 * @author Diana Arcanjo
 * @version 1.0
 * @since 2019-07-24
 *
 */
 
@CronapiMetaData(categoryName = "Adjust") 
public class Adjust {

   // FUNCTION FOR REMOVE FILS PDF ON FOLDER 
	@CronapiMetaData(type = "function", name = "Remove Files PDF", description = "Função Para Excluir Arquivos PDF da Pasta", returnType = ObjectType.STRING)
	public static Var FunctionTasks(@ParamMetaData(type = ObjectType.STRING, description = "File Path") Var folder,
									@ParamMetaData(type = ObjectType.STRING, description = "Name PDF") Var nameFile) throws Exception {

		File pasta = new File(Var.valueOf(folder).toString());    
		File[] arquivos = pasta.listFiles();    
    
		for(File arquivo : arquivos) {
    		if(arquivo.getName().endsWith("Contrato_" + Var.valueOf(nameFile).toString() + ".pdf")) {
        		arquivo.delete();
    	}
}
		System.out.println("Arquivo PDF Foram Deletados com Sucesso");

		return Var.valueOf(folder);
	}

	// FUNCTION FOR GET FIRST NAME OF STRING AND REMOVE SPACES AND ALTER THE FIRST LETTER OF NAME FOR UPPERCASE AND REST TINY. 
	@CronapiMetaData(type = "function", name = "Get Fist Name and Remove Spaces", description = "Remove nome composto e deixa apenas primeito nome. Ex de entrada: 'Maria Julia', saida fica 'Maria'", returnType = ObjectType.STRING)
	public static Var FunctionFistName(@ParamMetaData(type = ObjectType.STRING, description = "Name") Var name) throws Exception {
	
	String fullName = Var.valueOf(name).toString();

	String firstName = "";
    for(int i = 0; i < fullName.length(); i++){
        if(fullName.charAt(i) == ' '){
           break;
        } else {
            firstName += fullName.charAt(i);
        }
	}

	String alterName = firstName; 
	firstName = alterName.toLowerCase();  
	firstName = Character.toUpperCase(firstName.charAt(0)) + firstName.substring(1);
	firstName.trim();

		return Var.valueOf(firstName);
	}


	// FUNCTION TO FORMATTER DATE
	@CronapiMetaData(type = "function", name = "Timestamp to Date DD/MM/YYYY", description = "Função para converter a data timestamp para Date dd/MM/yyyy", returnType = ObjectType.STRING)
	public static Var FunctionFormatDate(@ParamMetaData(type = ObjectType.STRING, description = "Date") Var date) throws Exception {
	
				Long dateJson = Long.parseLong(Var.valueOf(date).toString());
				Timestamp stamp = new Timestamp(dateJson);
				Date dateFormat = new Date(stamp.getTime());
				SimpleDateFormat fd = new SimpleDateFormat("dd/MM/yyyy");

		return Var.valueOf(fd.format(dateFormat));
	}

	// FUNCTION FOR GET VALUES AND FORMATS FOR REALS. 
	@CronapiMetaData(type = "function", name = "Format Value", description = "Função para formatar valores em Reais", returnType = ObjectType.STRING)
	public static String FunctionFormatValue(@ParamMetaData(type = ObjectType.STRING, description = "Value R$") Var value) throws Exception {
	
				String number = Var.valueOf(value).toString();
				double doubleNumber = Double.parseDouble(number);  	
				Locale.setDefault(new Locale("pt", "BR"));  // mudança global
 
				DecimalFormat df = new DecimalFormat();
				df.applyPattern("#,##0.00");
				
				return df.format(doubleNumber);
	}

	//toLowerCase com primeira maiuscula 
	@CronapiMetaData(type = "function", name = "toLowerCase", description = "toLowerCase deixando a primeira lentra maiuscula", returnType = ObjectType.STRING)
	public static Var toLowerCase(
			@ParamMetaData(type = ObjectType.STRING, description = "Parâmetro: Descrição do parâmetro") Var input)
			throws Exception {
				String text = input.toString();
				text = text.substring(0,1).toUpperCase() + text.substring(1).toLowerCase();
			
			return Var.valueOf(text);
	}


	//Função para formatar CPF 
	@CronapiMetaData(type = "function", name = "Mascara CPF", description = "Coloca mascara para 000.000.000-00", returnType = ObjectType.STRING)
	public static String formatDateBR(
			@ParamMetaData(type = ObjectType.STRING, description = "Parâmetro: Descrição do parâmetro") Var inputCpf)

			throws Exception {

					String value = Var.valueOf(inputCpf).toString();
					MaskFormatter mask;
					
				try {
						mask = new MaskFormatter("###.###.###-##");
						mask.setValueContainsLiteralCharacters(false);
						return mask.valueToString(value);
					}catch(ParseException e) {
						throw new RuntimeException(e);
				}

			}

    // FUNCTION TO FORMATTER DATE
	@CronapiMetaData(type = "function", name = "Timestamp to Date MM/YYYY ", description = "Função para converter a data timestamp para Date MM/YYYY", returnType = ObjectType.STRING)
	public static Var FunctionFormatDateMesAno(@ParamMetaData(type = ObjectType.STRING, description = "Date") Var date) throws Exception {
	
				Long dateJson = Long.parseLong(Var.valueOf(date).toString());
				Timestamp stamp = new Timestamp(dateJson);
				Date dateFormat = new Date(stamp.getTime());
				SimpleDateFormat fd = new SimpleDateFormat("MM/yyyy");

		return Var.valueOf(fd.format(dateFormat));
	}

    // FUNCTION FORMAT SPECIAL CHARACTERS
	@CronapiMetaData(type = "function", name = "Format Special Characters ", description = "format special characters", returnType = ObjectType.STRING)
	public static String FunctionRemoverCaracteresEspecial(
		@ParamMetaData(type = ObjectType.STRING, description = "Words") Var inputString) throws Exception {

			 String str = Var.valueOf(inputString).toString();
			
			 System.out.println("FUNÇÃO REMOVER CARACTERES: " + RemoveEspecial(str));

			 return str;

	}

	public static String RemoveEspecial(String str) throws Exception {

			String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
        	Pattern pattern = Pattern.compile("\\p+");
        	return pattern.matcher(nfdNormalizedString).replaceAll("");

	}


	// retorna primeira letra da string 
	@CronapiMetaData(type = "function", name = "Mascara de Valores", description = "Mascara de Valores", returnType = ObjectType.STRING)
	public static Var mascaraValores(@ParamMetaData(type = ObjectType.STRING, description = "valor") Var valor) throws Exception {

		   String valorFormatado = NumberFormat.getCurrencyInstance().format(valor);
		   System.out.println(valorFormatado);
		
		   return Var.valueOf(valorFormatado);
	}

	// remover \n ou \r
	@CronapiMetaData(type = "function", name = "Remover barra n ou barra r da string", description = "função para remover barra n ou barra r", returnType = ObjectType.STRING)
	public static Var RemoverBarraNouR(@ParamMetaData(type = ObjectType.STRING, description = "texto") Var texto) throws Exception {
		
		String frase = texto.toString().replace("\\n", "").replace("\\r", "");
		//frase = texto.toString().replace("\\r", "").rel;
		
		return Var.valueOf(frase);
	}

	@CronapiMetaData(type = "function", name = "Zero esquerda", description = "Remove zero a esquerda", returnType = ObjectType.STRING)
	public static Var zeroEsquerda(
		@ParamMetaData(type = ObjectType.STRING, description = "Parâmetro: Descrição do parâmetro") Var numero)
		throws Exception {
			String num = numero.toString();

	System.out.println("\n ***** "+ num.length() +" teste 2 = " + num.replaceFirst("0*", ""));
					
		if(num.length() >=12){
			num =  num.replaceFirst("0*", "");
		}
		
	return Var.valueOf(num);
	}


	 //Remove caracter especial
	@CronapiMetaData(type = "function", name = "remove caracter especial", description = "function replaces ',' with '.'", returnType = ObjectType.STRING)
	public static Var removeCaracter(
			@ParamMetaData(type = ObjectType.STRING, description = "string caracter") Var string)
			throws Exception {
			String a = Var.valueOf(string).toString();
    		
			a = a.replace(".","");
			a = a.replace("(","");
			a = a.replace(")","");
			a = a.replace("-","");
    		
			return Var.valueOf(a);
	}

	//* FUNÇÃO PARA REMOVER O BRACKETS DO JSON
	@CronapiMetaData(type = "function", name = "Remove Brackets Json", nameTags = {
			"Auth Basic" }, description = "Remove Brackets Json" , returnType = ObjectType.STRING)	
	public static String RemoveListJson(@ParamMetaData(type = ObjectType.STRING, description = "JSON") String inputJson) throws JSONException  {

        String strBrackets = Var.valueOf(inputJson).toString();
		//*String strBrackets2;
		strBrackets = strBrackets.replaceAll("\\[","").replaceAll("\\]", "");
		//strBrackets2 = strBrackets.replaceAll("\\{","").replaceAll("\\}", "");

		return Var.valueOf(strBrackets).toString();    		
	}


	//* FUNÇÃO PARA ALTERAR OS BRACKETS DO JSON PARA {}
	@CronapiMetaData(type = "function", name = "Replace Brackets Json", nameTags = {
			"Auth Basic" }, description = "Replace Brackets Json" , returnType = ObjectType.STRING)	
	public static String ReplaceListJson(@ParamMetaData(type = ObjectType.STRING, description = "JSON") String inputJson) throws JSONException  {

        String strBrackets = Var.valueOf(inputJson).toString();
		//*String strBrackets2;
		strBrackets = strBrackets.replaceAll("\\[","{").replaceAll("\\]", "}");
		//strBrackets2 = strBrackets.replaceAll("\\{","").replaceAll("\\}", "");

		return Var.valueOf(strBrackets).toString();    		
	}

	//* FUNÇÃO PARA REMOVER O BRACKETS DO JSON
	@CronapiMetaData(type = "function", name = "Pegar parte de uma string", nameTags = {
			"Auth Basic" }, description = "Remove Brackets Json" , returnType = ObjectType.STRING)	
	public static String getString(
		@ParamMetaData(type = ObjectType.STRING, description = "String") String inputString,
	    @ParamMetaData(type = ObjectType.STRING, description = "Split") String inputSplit) throws JSONException  {
		
		String word = Var.valueOf(inputString).toString();
		String split = Var.valueOf(inputSplit).toString();
        String[] output = word.split(split);
		String retorno = output[1];
		
        return Var.valueOf(retorno).toString();    		
	}

	//* FUNÇÃO PARA REMOVER O PRIMEIRO BRACKETS DO JSON
	@CronapiMetaData(type = "function", name = "Pegar parte de uma string", nameTags = {
			"Auth Basic" }, description = "Remove Brackets Json" , returnType = ObjectType.STRING)	
	public static String removeFirstBracket(
		@ParamMetaData(type = ObjectType.STRING, description = "String") String inputString) throws JSONException  {
		
		String json = Var.valueOf(inputString).toString();
		String newStr = json.substring(1, json.length()-1);
        return Var.valueOf(newStr).toString();    		
	}

	//* FUNÇÃO PARA REMOVER O PRIMEIRO BRACKETS DO JSON
	@CronapiMetaData(type = "function", name = "Remove Zero Left", nameTags = {
			"Auth Basic" }, description = "Remove Zero Left" , returnType = ObjectType.STRING)	
	public static String removeZeroLeft(
		@ParamMetaData(type = ObjectType.STRING, description = "Data") String inputDate)
		
		throws Exception  {	

	try {
		   
		   String x;	

		   if(inputDate.startsWith("0")){
        		x = inputDate.replaceAll("0", "").replaceAll("", "");
    		} else {
        		x = inputDate; 
    		}


		return x;   

    } catch (Exception e) {
        e.printStackTrace();
    }

	return null;
	
	}

}



