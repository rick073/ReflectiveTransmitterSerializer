import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

class serializerDeserializerTest
{
	public static Object1 obj;
	public static String jStr;
	
	private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private static final PrintStream originalOut = System.out;
	
	@BeforeAll
	public static void setUp() throws Exception
	{
		obj = new Object1(1, 'c');
		System.setOut(new PrintStream(outContent));
	}
	
	@AfterAll
	public static void tearDown()
	{
		System.setOut(originalOut);
	}

	@Test
	@Order(1)
	void testSerializeObject() throws Exception
	{
		JsonObject jObj = Serializer.serializeObject(obj);
		jStr = jObj.toString();
		String expected = "{\"objects\":[{\"class\":\"Object1\",\"id\":\"0\",\"type\":\"object\",\"fields\":[{\"name\":\"someInt\",\"declaring class\":\"Object1\",\"value\":\"1\"},{\"name\":\"someChar\",\"declaring class\":\"Object1\",\"value\":\"c\"}]}]}";
		assertEquals(expected, jStr);
	}
	
	@Test
	@Order(2)
	void testDeserializeObject() throws Exception
	{
		JsonReader reader = Json.createReader(new StringReader(jStr));
		JsonObject jObj = reader.readObject();
		reader.close();
		
		Object o = Deserializer.deserializeObject(jObj);
		Inspector.inspect(o, true);
		String[] outArr = outContent.toString().split("\n");
		
		assertEquals("CLASS = Object1", outArr[0].trim());
		assertEquals("SUPERCLASS FOR = Object1", outArr[1].trim());
		assertEquals("SUPERCLASS = java.lang.Object", outArr[2].trim());
		assertEquals("RECURSING INTO SUPERCLASS.", outArr[3].trim());
		assertEquals("CLASS = java.lang.Object", outArr[4].trim());
		assertEquals("SUPERCLASS FOR = java.lang.Object", outArr[5].trim());
		assertEquals("SUPERCLASS = NONE", outArr[6].trim());
		assertEquals("INTERFACES FOR = java.lang.Object", outArr[7].trim());
		assertEquals("INTERFACES = NONE", outArr[8].trim());
		assertEquals("FIELDS FOR = java.lang.Object", outArr[9].trim());
		assertEquals("FIELDS = NONE", outArr[10].trim());
		assertEquals("INTERFACES FOR = Object1", outArr[11].trim());
		assertEquals("INTERFACES = NONE", outArr[12].trim());
		assertEquals("FIELDS FOR = Object1", outArr[13].trim());
		assertEquals("FIELD NAME = someInt", outArr[14].trim());
		assertEquals("FIELD TYPE = int", outArr[15].trim());
		assertEquals("MODIFIERS = public", outArr[16].trim());
		assertEquals("FIELD VALUE = 1", outArr[17].trim());
		assertEquals("FIELD NAME = someChar", outArr[18].trim());
		assertEquals("FIELD TYPE = char", outArr[19].trim());
		assertEquals("MODIFIERS = public", outArr[20].trim());
		assertEquals("FIELD VALUE = c", outArr[21].trim());
	}
}
