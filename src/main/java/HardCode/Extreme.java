package HardCode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;

import javax.net.ssl.SSLContext;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.gson.JsonArray;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.commons.collections4.Put;
import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import app.resources.HttpComponentsClientHttpRequestFactoryBasicAuth;
import cronapi.CronapiMetaData;
import cronapi.CronapiMetaData.ObjectType;
import cronapi.ParamMetaData;
import cronapi.Var;

/**
 * Classe para criação de novas funções...
 * 
 * @author Diana Arcanjo
 * @version 1.0
 * @since 2019-07-24
 *
 */
 
@CronapiMetaData(categoryName = "Extreme") 
public class Extreme {
	
	@CronapiMetaData(type = "function", name = "Contém no Texto", description = "Função que retorna 1 se os dois textos tem compatibilidade em mais de 80%", returnType = ObjectType.STRING)
public static Var contemNotexto(
 @ParamMetaData(type = ObjectType.STRING, description = "Parâmetro: Texto de entrada") Var input, @ParamMetaData(type = ObjectType.STRING, description = "Parâmetro: Texto de busca") Var texto)
throws Exception {


int tamanhoDoInput = input.length();
int tamanhoDOTexto = texto.length();
int corteDoInput = 2;
int corteDoTexto = 2;
double porcentagemMatch = 0.0;
double tamanhoVetorA = 0;
double tamanhoVetorB = 0;
double quantidadeContem = 0;
int retorno = 0;

ArrayList < String > vetA = new ArrayList < String > ();
ArrayList < String > vetB = new ArrayList < String > ();


for (int i = 0; i <= tamanhoDoInput; i++) {

    if (corteDoInput <= tamanhoDoInput) {
        vetA.add(input.toString().substring(i, corteDoInput));
        corteDoInput++;
    }
}
vetA.remove(null);
tamanhoVetorA = vetA.size();

for (int i = 0; i <= tamanhoDOTexto; i++) {

    if (corteDoTexto <= tamanhoDOTexto) {
        vetB.add(texto.toString().substring(i, corteDoTexto));
        corteDoTexto++;
    }
}

vetB.remove(null);
tamanhoVetorB = vetB.size();

for (int i = 0; i < tamanhoVetorA; i++) {
    for (int j = 0; j < tamanhoVetorB; j++) {
        if (vetA.get(i).contains(vetB.get(j))) {
            quantidadeContem++;
        }
    }
}

if (tamanhoVetorB != 0 && quantidadeContem != 0) {
    porcentagemMatch = (quantidadeContem / tamanhoVetorB);
}


if (porcentagemMatch > 0.6) {

    corteDoInput = 3;
    corteDoTexto = 3;

    for (int i = 0; i <= tamanhoDoInput; i++) {

        if (corteDoInput <= tamanhoDoInput) {
            vetA.add(input.toString().substring(i, corteDoInput));
            corteDoInput++;
        }
    }
    vetA.remove(null);
    tamanhoVetorA = vetA.size();

    for (int i = 0; i <= tamanhoDOTexto; i++) {

        if (corteDoTexto <= tamanhoDOTexto) {
            vetB.add(texto.toString().substring(i, corteDoTexto));
            corteDoTexto++;
        }
    }

    vetB.remove(null);
    tamanhoVetorB = vetB.size();

    for (int i = 0; i < tamanhoVetorA; i++) {
        for (int j = 0; j < tamanhoVetorB; j++) {
            if (vetA.get(i).contains(vetB.get(j))) {
                quantidadeContem++;
            }
        }
    }

}

if (porcentagemMatch > 0.6) {
    retorno = 1;
}

return Var.valueOf(retorno);

}

@CronapiMetaData(type = "function", name = "Download PDF", nameTags = {
			"Auth Basic" }, description = "Download PDF in Byte", returnType = ObjectType.JSON)

	public static final Var downlaodPDF(
		@ParamMetaData(type = ObjectType.STRING, description = "Byte ") Var inputBytes, 
		@ParamMetaData(type = ObjectType.STRING, description = "Nome PDF ") Var inputName)
		throws Exception {


		String bytesPDF = Var.valueOf(inputBytes).toString();
		String nameFile = Var.valueOf(inputName).toString();

		
		String bucketName = CommonConstants.BUCKET_NAME;
	    String objectName = "InformeRendimento/DocInformRend_" + nameFile + ".pdf";
				
			
		AWSCredentials credentials = new BasicAWSCredentials(CommonConstants.ACCESS_KEY_ID, CommonConstants.ACCESS_SEC_KEY);

		
		try {			
		
			byte[] content = bytesPDF.getBytes();
			InputStream stream = new ByteArrayInputStream(content);
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentLength(content.length);
			meta.setContentType("contrato/pdf");

			AmazonS3 s3client = AmazonS3ClientBuilder
				  .standard()
				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.US_EAST_1)
				  .build();

			s3client.putObject(new PutObjectRequest(bucketName, objectName, stream, meta));

	        System.out.println("Arquivo transferido para o S3 Amazon"); 			
				
			return Var.valueOf("Deu Bom!!!");			
				
		}catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            System.out.println(e);
            e.printStackTrace();
        }         

		return Var.valueOf("OK");		

	}

    // REQUIcAO GET PARA TRATA ERRO 500
	@CronapiMetaData(type = "function", name = "Basic Auth error 500", nameTags = {
			"Auth Basic" }, description = "Basic Auth error 500", returnType = ObjectType.JSON)
	
	public static final Var basicAuthRequisition(
		@ParamMetaData(type = ObjectType.STRING, description = "Caminho requisição") Var url, @ParamMetaData(type = ObjectType.STRING, description = "Nome de usuário") Var username,
		
		@ParamMetaData(type = ObjectType.STRING, description = "Senha de usuário") Var password)
		throws Exception {


		String urlResource = Var.valueOf(url).toString();
		String userResource = Var.valueOf(username).toString();
		String passResource = Var.valueOf(password).toString();
		

		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();

		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
	
		HttpHost host = HttpHost.create(urlResource);


		final ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactoryBasicAuth(host);
		
    	RestTemplate restTemplate = new RestTemplate(requestFactory);
		restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(userResource, passResource)); 
		JSONObject obj = new JSONObject();
		obj.put("mensagemDetalhada", "Prezado aluno não é possivel realizar sua matricula por esse canal, por favor procure a secretaria.");	
		obj.put("contexto", "500");
		//System.out.println(obj);	

		
		try {
			((HttpComponentsClientHttpRequestFactory) requestFactory).setHttpClient(httpClient);
			System.out.println(httpClient);
			ResponseEntity<String> response = restTemplate.getForEntity(urlResource, String.class);
									
			return Var.valueOf(response.getBody());
			
		} catch (HttpStatusCodeException  exception) {
			try {
				int statusCode = exception.getStatusCode().value();
				if(statusCode == 500){
					System.out.println("\n ************* Exception ************");
					System.out.println(exception);
					System.out.println("\n ************* FImException ************");
					System.out.println(statusCode);
					return Var.valueOf(obj.toString());
					}				
			} catch (Exception exc) {
				return Var.valueOf(exc);
			}
			return Var.valueOf("OK");			
		}			

		
	}

    @CronapiMetaData(type = "function", name = "POST Download PDF From URL Authentication", nameTags = {
			"Auth Basic" }, description = "Realizar um post de uma url autenticada e realizar o download de arquivo pdf", returnType = ObjectType.JSON)
	public static final Var RequisitionPostWithDownload(
		@ParamMetaData(type = ObjectType.STRING, description = "Caminho requisição") Var url, 
		@ParamMetaData(type = ObjectType.STRING, description = "Nome de usuário") Var username,		
		@ParamMetaData(type = ObjectType.STRING, description = "Senha de usuário") Var password,
		@ParamMetaData(type = ObjectType.STRING, description = "Nome PDF (por padrão colocar o codigo do aluno)") Var codAluno,
		@ParamMetaData(type = ObjectType.JSON, description = "Parâmetros (JSON)") Var parametros,
		@ParamMetaData(type = ObjectType.STRING, description = "Content-Type") Var type)
		throws Exception {

		
		String urlResource = Var.valueOf(url).toString();
		String userResource = Var.valueOf(username).toString();
		String passResource = Var.valueOf(password).toString();
		String codigoAluno = Var.valueOf(codAluno).toString();		
		
		String bucketName = CommonConstants.BUCKET_NAME;
	    String objectName = "ContratosRematricula/ContratoRematricula_" + codigoAluno + ".pdf";
		
		HttpHeaders headers = new HttpHeaders();

		if (Var.valueOf(type).toString().equalsIgnoreCase("JSON")) {
			headers.setContentType(MediaType.APPLICATION_JSON);
		} else if (Var.valueOf(type).toString().equalsIgnoreCase("x_www_form_urlencoded")) {
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		}

		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();

		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
	
		HttpHost host = HttpHost.create(urlResource);

		HttpEntity<String> request = new HttpEntity<String>(Var.valueOf(parametros).toString(),headers);

		final ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactoryBasicAuth(host);
		
    	RestTemplate restTemplate = new RestTemplate(requestFactory);
		restTemplate.getMessageConverters()
        .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
		restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(userResource, passResource));		

		AWSCredentials credentials = new BasicAWSCredentials(CommonConstants.ACCESS_KEY_ID, CommonConstants.ACCESS_SEC_KEY);

		
		try {
			
				((HttpComponentsClientHttpRequestFactory) requestFactory).setHttpClient(httpClient);
				ResponseEntity<String> response = restTemplate.postForEntity(urlResource, request, String.class);	
				JSONObject jo = new JSONObject(response.getBody());
				String contratoPDF = jo.get("contratoPDF").toString(); 
				
				String contratoPDFDecoder = contratoPDF;			

					byte[] decoder = Base64.getDecoder().decode(contratoPDFDecoder);	
					InputStream stream = new ByteArrayInputStream(decoder);
					ObjectMetadata meta = new ObjectMetadata();
					meta.setContentLength(decoder.length);
					meta.setContentType("pdf/pdf");			

					AmazonS3 s3client = AmazonS3ClientBuilder
					  .standard()
					  .withCredentials(new AWSStaticCredentialsProvider(credentials))
					  .withRegion(Regions.US_EAST_1)
					  .build();

					s3client.putObject(new PutObjectRequest(bucketName, objectName, stream, meta));

	        	System.out.println("Arquivo transferido para o S3 Amazon"); 		

				return Var.valueOf(response.getBody());							
			
		} catch (Exception e) {
			try {
				((HttpComponentsClientHttpRequestFactory) requestFactory).setHttpClient(httpClient);						
				ResponseEntity<String> response = restTemplate.postForEntity(urlResource, request, String.class);	
				return Var.valueOf(response.getBody());
			} catch (Exception exc) {
				return Var.valueOf(exc);
			}
	
		}      

	}

	@CronapiMetaData(type = "function", name = "Escrever PDF", nameTags = {
			"Auth Basic" }, description = "Escrever PDF", returnType = ObjectType.JSON)

	public static final Var EscreverPDF(
		@ParamMetaData(type = ObjectType.STRING, description = "texto do pdf") Var texto, 
		@ParamMetaData(type = ObjectType.STRING, description = "caminho diretorio") Var caminho,
		@ParamMetaData(type = ObjectType.STRING, description = "Nome PDF") Var nomeArquivo)
		throws Exception {


		String textoResource = Var.valueOf(texto).toString();
		String caminhoResource = Var.valueOf(caminho).toString();
		String nomeArquivoResource = Var.valueOf(nomeArquivo).toString();
		
		String bucketName = CommonConstants.BUCKET_NAME;
	    String objectName = caminhoResource + nomeArquivoResource + ".pdf";

		AWSCredentials credentials = new BasicAWSCredentials(CommonConstants.ACCESS_KEY_ID, CommonConstants.ACCESS_SEC_KEY);	

		HttpHeaders headers = new HttpHeaders();

		
		
			

		try {						
			
			byte[] decoder = textoResource.getBytes();	
					InputStream stream = new ByteArrayInputStream(decoder);
					ObjectMetadata meta = new ObjectMetadata();
					//meta.setContentType("UTF-8");
					meta.setContentLength(decoder.length);
					meta.setContentType("application/pdf");	
					
					
					//meta.setContentType("application/pdf");
					//meta.setHeader("Pragma, no-cache", null);

			AmazonS3 s3client = AmazonS3ClientBuilder
				  .standard()
				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.US_EAST_1)
				  .build();

			s3client.putObject(new PutObjectRequest(bucketName, objectName, stream, meta));

	        System.out.println("Arquivo transferido para o S3 Amazon"); 			
				
			return Var.valueOf(texto);			
				
		}catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            System.out.println(e);
            e.printStackTrace();
        }         

		return Var.valueOf("OK");		

	}


	// FUNÇÃO PARA REALIZAR A ESCRITA DO PDF ATRAVÉS DE UMA STRING E ENVIAR PRO S3
	@CronapiMetaData(type = "function", name = "Write PDF and send to S3", nameTags = {
			"Auth Basic" }, description = "Write PDF and send to S3", returnType = ObjectType.JSON)

	public static final Var WritePDFandsendtoS3(
		@ParamMetaData(type = ObjectType.STRING, description = "Text") Var InputStr,
		@ParamMetaData(type = ObjectType.STRING, description = "Name Directory") Var inputNameDirectory,
		@ParamMetaData(type = ObjectType.STRING, description = "Name PDF") Var inputName)
		throws Exception {

			String textStrng = Var.valueOf(InputStr).toString();
			String nameDirectory = Var.valueOf(inputNameDirectory).toString(); 
			String namePDF = Var.valueOf(inputName).toString();
			
								
			String bucketName = CommonConstants.BUCKET_NAME;
			String objectName = nameDirectory + namePDF + ".pdf";
				
		  
		  AWSCredentials credentials = new BasicAWSCredentials(CommonConstants.ACCESS_KEY_ID, CommonConstants.ACCESS_SEC_KEY);
		  com.itextpdf.text.Document document = new com.itextpdf.text.Document();
		  ByteArrayOutputStream streamByte = new ByteArrayOutputStream();

		try {
						
            PdfWriter writer = PdfWriter.getInstance(document, streamByte);
         	document.open();
            document.addCreationDate();
			document.add(new Paragraph(textStrng));
			document.addAuthor("UNICESUMAR");
         	document.close();
         	writer.close();

			byte[] content = streamByte.toByteArray();	
			InputStream stream = new ByteArrayInputStream(content);
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentLength(content.length);
			meta.setContentType("matriz/pdf");

				AmazonS3 s3client = AmazonS3ClientBuilder
					.standard()
					.withCredentials(new AWSStaticCredentialsProvider(credentials))
					.withRegion(Regions.US_EAST_1)
					.build();

				s3client.putObject(new PutObjectRequest(bucketName, objectName, stream, meta));

	        System.out.println("Arquivo transferido para o S3 Amazon"); 			
				
			return Var.valueOf(content);			
				
		}catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }         

		return Var.valueOf("OK");		

	}

	// 
	@CronapiMetaData(type = "function", name = "Get Value", nameTags = {
			"Auth Basic" }, description = "Get Value", returnType = ObjectType.JSON)

	public static final String getArray(
		@ParamMetaData(type = ObjectType.STRING, description = "Json") String InputJson,
		@ParamMetaData(type = ObjectType.STRING, description = "Atributo") String InputAtributo)

		throws Exception {


    		JSONObject obj = null;
			JSONArray jsonArray = new JSONArray(InputJson);
			JSONArray arrayNew = new JSONArray();
			JSONArray jArray = null;
			JSONObject valores = new JSONObject();
			
			
		try {

			for(int i=0; i < jsonArray.length(); i++){
			
			 JSONObject object = jsonArray.getJSONObject(i);
			 obj = new JSONObject(object.toString());
             jArray = obj.getJSONArray(InputAtributo);
			 arrayNew.put(i, jArray);
		}

			return Var.valueOf(arrayNew).toString();	

		}catch(JSONException e) {
            System.out.println(e);
            e.printStackTrace();
        }         

		return "OK";		

	}

	@CronapiMetaData(type = "function", name = "Get Json Array", nameTags = {
			"Auth Basic" }, description = "Get Json Array", returnType = ObjectType.JSON)

	public static final String getArrayJson(
		@ParamMetaData(type = ObjectType.STRING, description = "Json") String InputJson,
		@ParamMetaData(type = ObjectType.STRING, description = "Atributo") String InputAtributo)

		throws Exception {


    		JSONObject obj = null;
			JSONArray jsonArray = new JSONArray(InputJson);
			JSONArray arrayNew = new JSONArray();
			JSONArray jArray = null;
			JSONObject valores = new JSONObject();
			
			
		try {

			for(int i=0; i < jsonArray.length(); i++){
			
			 JSONObject object = jsonArray.getJSONObject(i);
			 obj = new JSONObject(object.toString());
             jArray = obj.getJSONArray(InputAtributo);
			 arrayNew.put(i, jArray);
		}

			return Var.valueOf(arrayNew).toString();	

		}catch(JSONException e) {
            System.out.println(e);
            e.printStackTrace();
        }         

		return "OK";		

	}

}


