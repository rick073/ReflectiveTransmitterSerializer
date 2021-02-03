import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

public class ServerComponent
{
	private static final int PORT = 8080;
	
	public static void main(String[] args) throws Exception
	{
		
		Scanner input = new Scanner(System.in);
		boolean exit = false;
		
		ServerSocket listener = new ServerSocket(PORT);
		System.out.println("Server: Socket opened on port: " + PORT);
		
		Socket client = listener.accept();
		System.out.println("Server: Client Accepted");		

		PrintWriter out = new PrintWriter(client.getOutputStream(), true);
		System.out.println("Server: PrintWriter Created");
		
		do
		{
			System.out.println("Please enter an option, 1-5 for objects, anything else to exit: ");
			String nextOpt = input.next();
			
			switch(nextOpt)
			{
			case "1":
				Object1 obj1 = makeObject1(input);
				
				JsonObject obj1s = Serializer.serializeObject(obj1);
				System.out.println("Server: Sending:");
				System.out.println(prettyPrint(obj1s));

				out.println(obj1s.toString());
				break;
				
			case "2":
				Object2 obj2 = makeObject2(input);
				
				JsonObject obj2s = Serializer.serializeObject(obj2);
				System.out.println("Server: Sending:");
				System.out.println(prettyPrint(obj2s));
				
				out.println(obj2s.toString());
				break;
				
			case "3":
				
				Object3 obj3 = makeObject3(input);
				
				JsonObject obj3s = Serializer.serializeObject(obj3);
				System.out.println("Server: Sending:");
				System.out.println(prettyPrint(obj3s));
				
				out.println(obj3s.toString());
				break;
				
			case "4":
				Object4 obj4 = makeObject4(input);
				
				JsonObject obj4s = Serializer.serializeObject(obj4);
				System.out.println("Server: Sending:");
				System.out.println(prettyPrint(obj4s));
				
				out.println(obj4s.toString());
				break;
				
			case "5":
				Object5 obj5 = makeObject5(input);
				
				JsonObject obj5s = Serializer.serializeObject(obj5);
				System.out.println("Server: Sending:");
				System.out.println(prettyPrint(obj5s));
								
				out.println(obj5s.toString());
				break;
				
			default:
				exit = true;
				System.out.println("Server: Sending: END");
				out.println("END");
				break;
			}
		}
		while(!exit);
		
		input.close();
		listener.close();
		System.out.println("Server: Listener Closed");
	}
	
	public static Object1 makeObject1(Scanner input)
	{
		//Scanner input = new Scanner(System.in);
		System.out.println("Making Object1.");
		
		System.out.println("Populating field someInt of type int.");
		int inInt = intInput(input);
		
		System.out.println("Populating field someChar of type char.");
		System.out.println("Please enter a char: ");
		char inChar = input.next().charAt(0);
		
		System.out.println("Creating Object1 with fields: someInt = " + inInt + ". someChar = \'" + inChar + "\'.");
		//input.close();
		return new Object1(inInt, inChar);
	}
	
	public static Object2 makeObject2(Scanner input)
	{
		//Scanner input = new Scanner(System.in);
		System.out.println("Making Object2.");
		
		System.out.println("Populating field x of type int.");
		int inIntX = intInput(input);
		
		System.out.println("Populating field y of type int.");
		int inIntY = intInput(input);
		
		if(ynInput("Would you like to link more Object2's?", input))
		{
			System.out.println("Populating field nextPoint of type Object2.");
			Object2 tObj2 = makeObject2(input);
			
			System.out.println("Creating Object2 with fields: x = " + inIntX + ". y = " + inIntY + ". nextPoint = "/* + Bla*/);
			//input.close();
			return new Object2(inIntX, inIntY, tObj2);
		}
		else
		{
			System.out.println("Creating Object2 with fields: x = " + inIntX + ". y = " + inIntY + ". nextPoint = " + null);
			//input.close();
			return new Object2(inIntX, inIntY, null);
		}
	}
	
	public static Object3 makeObject3(Scanner input)
	{
		//Scanner input = new Scanner(System.in);
		int[] inIntArr = new int[5];
		
		System.out.println("Making Object3.");
		
		System.out.println("Populating field arr[] of type int[].");
		for(int index = 0; index < inIntArr.length; index++)
		{
			inIntArr[index] = intInput(input);
		}
		
		System.out.println("Creating Object3 with field: arr = " + inIntArr);
		//input.close();
		return new Object3(inIntArr);
	}
	
	public static Object4 makeObject4(Scanner input)
	{
		//Scanner input = new Scanner(System.in);
		Object1[] inObjArr = new Object1[3];
		
		System.out.println("Making Object4.");
		
		System.out.println("Populating field arr of type Object1[].");
		for(int index = 0; index < inObjArr.length; index++)
		{
			System.out.println("Populating index " + index);
			if(ynInput("Would you like index " + index + " to be null?", input))
			{
				inObjArr[index] = null;
			}
			else
			{				
				inObjArr[index] = makeObject1(input);
			}			
		}
		
		System.out.println("Creating Object4 with fields: arr = " + inObjArr);
		//input.close();
		return new Object4(inObjArr);
	}
	
	public static Object5 makeObject5(Scanner input)
	{
		//Scanner input = new Scanner(System.in);
		ArrayList<Object1> inArrLst = new ArrayList<Object1>();
		
		System.out.println("Making Object5.");
		
		System.out.println("Populating field arrList of type ArrayList(Object1).");
		boolean addMore = ynInput("Would you like to add Object1's to the arraylist?", input);
		while(addMore)
		{
			inArrLst.add(makeObject1(input));

			addMore = ynInput("Would you like to add more Object1's to the arraylist?", input);
		}
		
		System.out.println("Creating Object5 with fields: arrList = " + inArrLst);
		//input.close();
		return new Object5(inArrLst);
	}
	
	public static int intInput(Scanner input)
	{
		//Scanner input = new Scanner(System.in);
		boolean hasInt = false;
		int inInt = 0;
		
		System.out.println("Please enter an int: ");
		
		while(!hasInt)
		{
			if(input.hasNextInt())
			{
				inInt = input.nextInt();
				hasInt = true;
			}
			else
			{
				System.out.println("Not an integer. Please enter an integer for field someInt: ");
			}
		}
		
		//input.close();
		return inInt;
	}
	
	public static boolean ynInput(String question, Scanner input)
	{
		//Scanner input = new Scanner(System.in);
		//System.out.println("Would you like to add more Object1's?");
		System.out.println(question);
		System.out.println("Y for Yes, anything else for No.");
		String yn = input.next();
		
		//REFACTORING: Statement removed because I realized it's so much simpler if I treat anything that isn't a Y as a no.
		/*while(!yn.equalsIgnoreCase("Y") && !yn.equalsIgnoreCase("N"))
		{
			System.out.println("Please enter Y or N: ");
			yn = input.next();
		}*/
		
		//input.close();
		if(yn.equalsIgnoreCase("Y"))
		{
			return true;
		}
		else
		{
			return false;
		}
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
