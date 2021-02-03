
import java.lang.reflect.*;
import java.util.*;
import javax.json.*;

/**
 * CPSC 501 Incomplete JSON deserializer
 *
 * @author Jonathan Hudson
 */
public class Deserializer
{
	public static Object deserializeObject(JsonObject json_object) throws Exception
	{
		JsonArray object_list = json_object.getJsonArray("objects");
		Map object_tracking_map = new HashMap();

		createInstances(object_tracking_map, object_list);
		assignFieldValues(object_tracking_map, object_list);

		return object_tracking_map.get("0");
	}

	private static void createInstances(Map object_tracking_map, JsonArray object_list) throws Exception
	{
		for (int i = 0; i < object_list.size(); i++)
		{
			JsonObject object_info = object_list.getJsonObject(i);
			Class object_class = Class.forName(object_info.getString("class"));
			
			if(object_info.getString("type").equals("array"))
			{
				Class compType = object_class.getComponentType();
				Object objectInstance = Array.newInstance(compType, Integer.valueOf(object_info.getString("length")));
				JsonArray objectEntries = object_info.getJsonArray("entries");
				
				for(int j = 0; j < objectEntries.size(); j++)
				{
					JsonObject entryInfo = objectEntries.getJsonObject(j);
					if(compType.isPrimitive())
					{
						Array.set(objectInstance, j, getValue(compType, entryInfo.getString("value")));
					}
					else
					{
						Array.set(objectInstance, j, object_tracking_map.get(entryInfo.getString("reference")));
					}
				}
				
				object_tracking_map.put(object_info.getString("id"), objectInstance);
			}
			else
			{
				Constructor constructor = object_class.getDeclaredConstructor();
				if (!Modifier.isPublic(constructor.getModifiers()))
				{
					constructor.setAccessible(true);
				}
	
				Object object_instance = constructor.newInstance();
				// Make object
				object_tracking_map.put(object_info.getString("id"), object_instance);
			}
		}
	}

	private static void assignFieldValues(Map object_tracking_map, JsonArray object_list) throws Exception
	{
		for (int i = 0; i < object_list.size(); i++)
		{
			JsonObject objectInfo = object_list.getJsonObject(i);
			Object objectInstance = object_tracking_map.get(objectInfo.getString("id"));

			if(!objectInfo.getString("type").equals("array"))
			{
				JsonArray objectFields = objectInfo.getJsonArray("fields");
				for(int j = 0; j < objectFields.size(); j++)
				{
					JsonObject fieldInfo = objectFields.getJsonObject(j);
					
					String fieldDecClass = fieldInfo.getString("declaring class");
					Class decClass = Class.forName(fieldDecClass);
					
					String fieldName = fieldInfo.getString("name");
					
					Field f = decClass.getDeclaredField(fieldName);
					f.setAccessible(true);
					
					Class fieldType = f.getType();
					
					if(fieldType.isPrimitive())
					{
						f.set(objectInstance, getValue(fieldType, fieldInfo.getString("value")));
					}
					else //is object
					{
						f.set(objectInstance, object_tracking_map.get(fieldInfo.getString("reference")));
					}
				}
			}
			//else already handled in createInstance()
		}
	}

	//Getting Primitive Values method taken from: https://stackoverflow.com/questions/13943550/how-to-convert-from-string-to-a-primitive-type-or-standard-java-wrapper-types
	//If there's an easier way of converting a string to an arbitrary primitive than this method, I haven't found it.
	public static Object getValue(Class fieldType, String fieldValue)
	{
		if(fieldType == boolean.class) return Boolean.valueOf(fieldValue);
	    if(fieldType == byte.class) return Byte.valueOf(fieldValue);
	    if(fieldType == short.class) return Short.valueOf(fieldValue);
	    if(fieldType == int.class) return Integer.valueOf(fieldValue);
	    if(fieldType == long.class) return Long.valueOf(fieldValue);
	    if(fieldType == float.class) return Float.valueOf(fieldValue);
	    if(fieldType == double.class) return Double.valueOf(fieldValue);
	    if(fieldType == char.class) return fieldValue.charAt(0);
	    return fieldValue;
	}
}
