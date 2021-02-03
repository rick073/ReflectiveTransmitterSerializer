
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import javax.json.*;

/**
 * CPSC 501 Incomplete JSON Serializer
 *
 * @author Jonathan Hudson
 */
public class Serializer
{
	public static JsonObject serializeObject(Object object) throws Exception
	{
		JsonArrayBuilder object_list = Json.createArrayBuilder();

		serializeHelper(object, object_list, new IdentityHashMap());

		JsonObjectBuilder json_base_object = Json.createObjectBuilder();
		json_base_object.add("objects", object_list);
		return json_base_object.build();
	}

	private static void serializeHelper(Object source, JsonArrayBuilder object_list, Map object_tracking_map) throws Exception
	{
		String object_id = Integer.toString(object_tracking_map.size());
		object_tracking_map.put(source, object_id);

		Class object_class = source.getClass();
		JsonObjectBuilder object_info = Json.createObjectBuilder();
		
		object_info.add("class", object_class.getName());
		object_info.add("id", object_id);
		
		JsonArrayBuilder fieldList = Json.createArrayBuilder();
		
		for(Field f : object_class.getDeclaredFields())
		{
			f.setAccessible(true);
			
			JsonObjectBuilder fieldInfo = Json.createObjectBuilder();
			Class fieldDecClass = f.getDeclaringClass();
			Object fieldObj = f.get(source);
			
			fieldInfo.add("name", f.getName());
			fieldInfo.add("declaring class", fieldDecClass.getName());
			
			if(f.getType().isPrimitive())
			{
				fieldInfo.add("value", fieldObj.toString());
			}
			else //is object
			{
				if(fieldObj == null)
				{
					fieldInfo.add("reference", "null");
				}
				else if(object_tracking_map.containsKey(fieldObj))
				{
					fieldInfo.add("reference", object_tracking_map.get(fieldObj).toString());
				}
				else
				{
					fieldInfo.add("reference", Integer.toString(object_tracking_map.size()));
					serializeHelper(fieldObj, object_list, object_tracking_map);
				}
			}
			fieldList.add(fieldInfo);
		}
		
		if(object_class.isArray())
		{
			object_info.add("type", "array");
			
			int length = Array.getLength(source);
			object_info.add("length", Integer.toString(length));
			
			JsonArrayBuilder entryList = Json.createArrayBuilder();
			
			for(int i = 0; i < length; i++)
			{
				JsonObjectBuilder entryInfo = Json.createObjectBuilder();
				Object o = Array.get(source, i);
				
				if(object_class.getComponentType().isPrimitive())
				{
					entryInfo.add("value", o.toString());
				}
				else //is object
				{
					if(o == null)
					{
						entryInfo.add("reference", "null");
					}
					else if(object_tracking_map.containsKey(o))
					{
						entryInfo.add("reference", object_tracking_map.get(o).toString());
					}
					else
					{
						entryInfo.add("reference", Integer.toString(object_tracking_map.size()));
						serializeHelper(o, object_list, object_tracking_map);
					}
				}
				entryList.add(entryInfo);
			}
			object_info.add("entries", entryList);
		}
		else
		{
			object_info.add("type", "object");
			object_info.add("fields", fieldList);
		}
		
		object_list.add(object_info);
	}
}
