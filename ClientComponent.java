import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

public class ClientComponent
{
	private static final String IP = "127.0.0.1";
	private static final int PORT = 8080;
	
	public static void main(String[] args) throws Exception
	{
		
		Socket socket = new Socket(IP, PORT);
		System.out.println("Client: Connected to Server.");
		
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		System.out.println("Client: Input reader created.");
		
		String receivedMessage = "";
		
		do
		{
			receivedMessage = input.readLine();
			System.out.println("Client received: " + receivedMessage);
			
			if(!receivedMessage.equals("END"))
			{
				try
				{
					JsonReader reader = Json.createReader(new StringReader(receivedMessage));
					JsonObject json = reader.readObject();
					reader.close();
					
					System.out.println("Client: JSON Object: \n" + prettyPrint(json));
					
					Object o = Deserializer.deserializeObject(json);
					
					Inspector.inspect(o, true);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		while(!receivedMessage.equals("END"));
		
		socket.close();
		System.out.println("Client: Socket closed.");
	}
	
	public static String prettyPrint(JsonObject o) throws Exception
	{
		StringWriter sw = new StringWriter();
		
		Map<String, Object> settings = new HashMap<>();
		settings.put(JsonGenerator.PRETTY_PRINTING, true);
		
		JsonWriterFactory wFactory = Json.createWriterFactory(settings);
		JsonWriter writer = wFactory.createWriter(sw);
		
		writer.writeObject(o);
		writer.close();
		
		return sw.toString();
	}
}
