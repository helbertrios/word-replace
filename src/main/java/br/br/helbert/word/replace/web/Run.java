package br.br.helbert.word.replace.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.br.helbert.word.replace.dto.Client;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("serial")
@WebServlet("/run")
public class Run extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)  throws IOException {

		   PrintWriter writer = response.getWriter();
		   ObjectMapper mapper = new ObjectMapper();
		   
	        
	        final Client client = new Client("Rios", "Helbet");
	        client.getQuestions().add("Ser ou não ser eis a questão.");
	        client.getQuestions().add("Minha terra tem palmeiras onde canta o sabiá.");
	        client.getQuestions().add("Muito ajuda quem não atrapalha.");
	        client.getQuestions().add("Ser ou não ser eis a questão.");
	        String json = mapper.writeValueAsString(client);
	        
	        writer.println("JSON: "+json);
	        writer.println("");
	        
	        
	        Map<String, Object> treeMap = mapper.readValue(json, Map.class);
	        List<String> keys  = new ArrayList<>();
	        findKeys(treeMap, keys);
	        
	        writer.println("keys: ");
	        
	        for (String key : keys) {
	        	 writer.println(key);
			}
	        
	        
	    }
	   
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<String> findKeys(Map<String, Object> treeMap , List<String> keys) {
		   for (Map.Entry<String, Object> entry : treeMap.entrySet()) {
			   final String key = entry.getKey();
			   final Object value = entry.getValue();
			   if (value instanceof LinkedHashMap) {
				   Map<String, Object> map = (LinkedHashMap) value;
				   findKeys(map, keys);
			   }
			   keys.add(key);
		   }
		   return keys;
	   }


	   public static void x1(Object obj)  {
		   //String clientJson = "";
		   //Map<String, String> map = mapper.readValue(clientJson, Map.class);
	       // for (Map.Entry<String, String> entry : map.entrySet()) {
	        	//writer.println("Key: "+entry.getKey()+ ", value "+entry.getValue());
	        //}
	      
	   }
	   
}
