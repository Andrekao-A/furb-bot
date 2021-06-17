package HardCode;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.eclipse.rap.rwt.internal.util.HTTP;
import org.json.JSONObject;
import org.json.JSONString;
import org.springframework.web.client.HttpStatusCodeException;

import cronapi.CronapiMetaData;
import cronapi.CronapiMetaData.ObjectType;
import cronapi.ParamMetaData;
import cronapi.Var;



/**
 * Classe para criação de requisiçoes ...
 * 
 * @author Diana Arcanjo
 * @version 1.0
 * @since 2019-07-24
 *
 */
 
@CronapiMetaData(categoryName = "Requisitions") 
public class Requisitions {


	// FUNÇÃO PARA REQUISIÇÕES GET
	@CronapiMetaData(type = "function", name = "Requisition GET SessionID", nameTags = {
			"Auth Basic" }, description = "Requisition GET", returnType = ObjectType.JSON)
	public static final Var requisitionGet(
		@ParamMetaData(type = ObjectType.STRING, description = "Caminho requisição") Var inputUrl,
		@ParamMetaData(type = ObjectType.STRING, description = "SessionID") Var inputCookie)
		
		throws Exception {

		String urlResource = Var.valueOf(inputUrl).toString();
		String CookieString = Var.valueOf(inputCookie).toString();
				
		try {

			URL url = new URL(urlResource);//your url i.e fetch data from .
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Cookie", CookieString);							
			
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            output = br.readLine();
            conn.disconnect();

			return Var.valueOf(output);

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }
		
		return Var.valueOf("OK");	

	}

	// FUNÇÃO PARA REQUISIÇÕES POST PARA SALVAR ARQUIVO NO AWS
	@CronapiMetaData(type = "function", name = "Requisition POST with save PDF AWS", nameTags = {
			"Auth Basic" }, description = "Requisition POST with save PDF AWS", returnType = ObjectType.JSON)
	public static final void requisitionPostPDF(
		@ParamMetaData(type = ObjectType.STRING, description = "Url ") Var inputUrl,
		@ParamMetaData(type = ObjectType.STRING, description = "Params ") Var inputParams,
		@ParamMetaData(type = ObjectType.STRING, description = "SessionID ") Var inputCookie, 
		@ParamMetaData(type = ObjectType.STRING, description = "Name Path ") Var inputPath,
		@ParamMetaData(type = ObjectType.STRING, description = "Name PDF ") Var inputName)
		
		throws Exception {

		String urlResource = Var.valueOf(inputUrl).toString();
		String paramans = Var.valueOf(inputParams).toString();
		String cookie = Var.valueOf(inputCookie).toString();
		String path = Var.valueOf(inputPath).toString();
		String nameFile = Var.valueOf(inputName).toString();
		

		
		String bucketName = CommonConstants.BUCKET_NAME;
	    String objectName = path + nameFile + ".pdf";
				
			
		AWSCredentials credentials = new BasicAWSCredentials(CommonConstants.ACCESS_KEY_ID, CommonConstants.ACCESS_SEC_KEY);
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		try {

			HttpPost request = new HttpPost(urlResource);
			StringEntity params = new StringEntity(paramans);
			request.addHeader("content-type", "application/json");
			request.setHeader("Cookie", cookie);
			request.setEntity(params);
 			HttpResponse response = httpClient.execute(request);

			byte[] content = EntityUtils.toByteArray(response.getEntity());
			InputStream stream = new ByteArrayInputStream(content);
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentLength(content.length);
			meta.setContentType("InformeRendimento/pdf");

			AmazonS3 s3client = AmazonS3ClientBuilder
				  .standard()
				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.US_EAST_1)
				  .build();

			s3client.putObject(new PutObjectRequest(bucketName, objectName, stream, meta));


   		 }catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            System.out.println(e);
            e.printStackTrace();
        } 	

	}

	// FUNÇÃO PARA REQUISIÇÕES POST JSON COM SESSION ID
	@CronapiMetaData(type = "function", name = "Requisition POST JSON", nameTags = {
			"Auth Basic" }, description = "Requisition POST", returnType = ObjectType.JSON)
	public static final String requisitionPost(
		@ParamMetaData(type = ObjectType.STRING, description = "Url ") Var inputUrl,
		@ParamMetaData(type = ObjectType.STRING, description = "SessionID ") Var inputCookie,
		@ParamMetaData(type = ObjectType.STRING, description = "Params ") Var inputParams)
		
		throws Exception {

		String urlResource = Var.valueOf(inputUrl).toString();
		String paramans = Var.valueOf(inputParams).toString();
		String cookie = Var.valueOf(inputCookie).toString();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();


		try {

			HttpPost request = new HttpPost(urlResource);
			StringEntity params = new StringEntity(paramans);
			request.addHeader("content-type", "application/json");
			request.setHeader("Cookie", cookie);
			request.setEntity(params);
 			HttpResponse response = httpClient.execute(request);
			String contentReturn = EntityUtils.toString(response.getEntity(), HTTP.CHARSET_UTF_8);

			return Var.valueOf(contentReturn).toString();


   		 }catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
        } 

		return null;	

	}

	// FUNÇÃO PARA REQUISIÇÕES POST JSON COM SESSION ID E VALIDAÇĀO DO RETORNO
	@CronapiMetaData(type = "function", name = "Requisition POST Valid Response", nameTags = {
			"Auth Basic" }, description = "Requisition POST", returnType = ObjectType.JSON)
	public static final String requisitionPostValidation(
		@ParamMetaData(type = ObjectType.STRING, description = "Url ") Var inputUrl,
		@ParamMetaData(type = ObjectType.STRING, description = "SessionID ") Var inputCookie,
		@ParamMetaData(type = ObjectType.STRING, description = "Params ") Var inputParams)
		
		throws Exception {

		String urlResource = Var.valueOf(inputUrl).toString();
		String paramans = Var.valueOf(inputParams).toString();
		String cookie = Var.valueOf(inputCookie).toString();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();


		try {

			HttpPost request = new HttpPost(urlResource);
			StringEntity params = new StringEntity(paramans);
			request.addHeader("content-type", "application/json");
			request.setHeader("Cookie", cookie);
			request.setEntity(params);
 			HttpResponse response = httpClient.execute(request);
			String contentReturn = EntityUtils.toString(response.getEntity(), HTTP.CHARSET_UTF_8);

			if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){

				System.out.println("\nRESPONSE" + contentReturn); 

				return Var.valueOf(contentReturn).toString();		

		  }else{

				return Var.valueOf(contentReturn).toString();
			}


   		 }catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
        } 

		return null;	

	}

	// FUNÇÃO PARA REQUISIÇÕES POST
	@CronapiMetaData(type = "function", name = "Requisition Post SessionID ", nameTags = {
			"Auth Basic" }, description = "Requisition Post SessionID", returnType = ObjectType.JSON)
	public static final String requisitionPostSessionID(
		@ParamMetaData(type = ObjectType.STRING, description = "Url ") Var inputUrl,
		@ParamMetaData(type = ObjectType.STRING, description = "Params ") Var inputParams)
		
		throws Exception {

		String urlResource = Var.valueOf(inputUrl).toString();
		String paramans = Var.valueOf(inputParams).toString();


		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		try {

			HttpPost request = new HttpPost(urlResource);
			StringEntity params = new StringEntity(paramans);
			request.addHeader("Content-Type", "application/x-www-form-urlencoded");
			request.setEntity(params);
 			HttpResponse response = httpClient.execute(request);
	
			String contentReturn = EntityUtils.toString(response.getEntity(), HTTP.CHARSET_UTF_8);

			if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){

				JSONObject json = new JSONObject();
	   			json.put("code", HttpStatus.SC_UNAUTHORIZED);

				return Var.valueOf(json).toString();		

		  }else{

				return Var.valueOf(contentReturn).toString();
			}

			
   		 }catch(Exception exception) {
            System.out.println(exception);
            exception.printStackTrace();
        } 	

		return Var.valueOf(null).toString();

	}


	// FUNÇÃO PARA REQUISIÇÕES POST
	@CronapiMetaData(type = "function", name = "Requisition Post Content ", nameTags = {
			"Auth Basic" }, description = "Requisition Post Content", returnType = ObjectType.JSON)
	public static final String requisitionPostContent(
		@ParamMetaData(type = ObjectType.STRING, description = "Url ") Var inputUrl,
		@ParamMetaData(type = ObjectType.STRING, description = "Params ") Var inputParams)
		
		throws Exception {

		String urlResource = Var.valueOf(inputUrl).toString();
		String paramans = Var.valueOf(inputParams).toString();


		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		try {

			HttpPost request = new HttpPost(urlResource);
			StringEntity params = new StringEntity(paramans);
			request.addHeader("Content-Type", "application/x-www-form-urlencoded");
			request.setEntity(params);
 			HttpResponse response = httpClient.execute(request);

			String contentReturn = EntityUtils.toString(response.getEntity(), HTTP.CHARSET_UTF_8);

		    return Var.valueOf(contentReturn).toString();	

			
   		 }catch(Exception exception) {
            System.out.println(exception);
            exception.printStackTrace();
        } 	

		return Var.valueOf(null).toString();

	} 

}


