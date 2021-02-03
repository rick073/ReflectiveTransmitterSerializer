import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Scanner;

//INSPECTOR CLASS IMPORTED AND MODIFIED FROM ASSIGNMENT 2

public class Inspector
{
	public static void inspect(Object obj, boolean recursive)
	{
		Class c = obj.getClass();
		
		if(!c.isArray())
		{
			inspectClass(c, obj, recursive, 0);
		}
		else
		{
			inspectArrayClass(c, obj, recursive, 0);
		}
	}

	private static void inspectArrayClass(Class c, Object obj, boolean recursive, int depth)
	{
		//Get Class Name
		//Note, getting class name is here instead of in inspect() because the recursive methods call inspectClass() instead of inspect()
		printTabs(depth);
		System.out.println("CLASS = " + c.getName());
		
		//Get Array Stuff
		inspectArray(obj, recursive, depth);
	}
	
	private static void inspectClass(Class c, Object obj, boolean recursive, int depth)
	{
		//Get Class Name
		//Note, getting class name is here instead of in inspect() because the recursive methods call inspectClass() instead of inspect()
		printTabs(depth);
		System.out.println("CLASS = " + c.getName());
		
		//Get Superclass
		inspectSuperclass(c, obj, recursive, depth);
		
		//Get Interfaces
		inspectInterface(c, obj, recursive, depth);
		
		//Get Fields
		inspectField(c, obj, recursive, depth);
	}
	
	//SUPERCLASS METHOD
	public static void inspectSuperclass(Class c, Object obj, boolean recursive, int depth)
	{
		printTabs(depth);
		System.out.println("SUPERCLASS FOR = " + c.getName());
		
		Class sc = c.getSuperclass();
		printTabs(depth);
		if(sc != null)
		{
			System.out.println(" SUPERCLASS = " + sc.getName());
			printTabs(depth);
			System.out.println(" RECURSING INTO SUPERCLASS.");
			int tempDepth = depth;
			inspectClass(sc, obj, recursive, ++tempDepth);
		}
		else
		{
			System.out.println(" SUPERCLASS = NONE");
		}
	}
	
	//INTERFACE METHOD
	public static void inspectInterface(Class c, Object obj, boolean recursive, int depth)
	{
		printTabs(depth);
		System.out.println("INTERFACES FOR = " + c.getName());
		
		Class[] interfaces = c.getInterfaces();
		if(interfaces.length > 0)
		{
			for(Class i : interfaces)
			{
				int tempDepth = depth;
				printTabs(depth);
				System.out.println(" INTERFACE = " + i.getName());
				printTabs(depth);
				System.out.println(" RECURSING INTO INTERFACE.");
				inspectClass(i, obj, recursive, ++tempDepth);
			}
		}
		else
		{
			printTabs(depth);
			System.out.println(" INTERFACES = NONE");
		}
	}
	
	//CONSTRUCTOR METHOD
	public void inspectConstructor(Class c, Object obj, int depth)
	{
		printTabs(depth);
		System.out.println("CONSTRUCTORS FOR = " + c.getName());
		
		Constructor[] constructors = c.getDeclaredConstructors();
		for(Constructor con : constructors)
		{
			//Get Name
			printTabs(depth);
			System.out.println("CONSTRUCTOR NAME = " + con.getName());
			
			//Get Exceptions
			inspectConstructorExceptions(con, depth);
			
			//Get Parameters
			inspectConstructorParams(con, depth);
			
			//Get Modifiers
			inspectConstructorMods(con, depth);
		}
	}
	//CONSTRUCTOR EXCEPTIONS
	public void inspectConstructorExceptions(Constructor con, int depth)
	{
		printTabs(depth);
		System.out.print(" CONSTRUCTOR EXCEPTIONS = ");
		Class[] exceptions = con.getExceptionTypes();
		if(exceptions.length > 0)
		{
			for(Class e : exceptions)
			{
				System.out.print(e.getName() + ", ");
			}
		}
		else
		{
			System.out.print("NONE");
		}
		System.out.print("\n");
	}
	//CONSTRUCTOR PARAMETERS
	public void inspectConstructorParams(Constructor con, int depth)
	{
		printTabs(depth);
		System.out.print(" CONSTRUCTOR PARAMETERS = ");
		Parameter[] parameters = con.getParameters();
		if(parameters.length > 0)
		{
			for(Parameter p : parameters)
			{
				System.out.print(p.getName() + ", ");
			}
		}
		else
		{
			System.out.print("NONE");
		}
		System.out.print("\n");
	}
	//CONSTRUCTOR MODIFIERS
	public void inspectConstructorMods(Constructor con, int depth)
	{
		int mod = con.getModifiers();
		String modifiers = Modifier.toString(mod);
		printTabs(depth);
		System.out.println(" CONSTRUCTOR MODIFIERS = " + modifiers);
	}
	
	//METHOD METHOD
	public void inspectMethod(Class c, Object obj, int depth)
	{
		printTabs(depth);
		System.out.println("METHODS FOR = " + c.getName());
		
		Method[] methods = c.getDeclaredMethods();
		if(methods.length > 0)
		{
			for(Method m : methods)
			{
				//Get Name
				printTabs(depth);
				System.out.println("METHOD NAME =  " + m.getName());
				
				//Get Exceptions
				inspectMethodExceptions(m, depth);
				
				//Get Parameters
				inspectMethodParams(m, depth);
				
				//Get Return Type
				inspectMethodReturn(m, depth);
				
				//Get Modifiers
				inspectMethodMods(m, depth);
			}
		}
		else
		{
			printTabs(depth);
			System.out.println("METHODS = NONE");
		}
	}
	//METHOD EXCEPTIONS
	public void inspectMethodExceptions(Method m, int depth)
	{
		printTabs(depth);
		System.out.print(" METHOD EXCEPTIONS = ");
		Class[] exceptions = m.getExceptionTypes();
		if(exceptions.length > 0)
		{
			for(Class e : exceptions)
			{
				System.out.print(e.getName() + ", ");
			}
		}
		else
		{
			System.out.print("NONE");
		}
		System.out.print("\n");
	}
	//METHOD PARAMETERS
	public void inspectMethodParams(Method m, int depth)
	{
		printTabs(depth);
		System.out.print(" METHOD PARAMETERS = ");
		Class[] parameters = m.getParameterTypes();
		if(parameters.length > 0)
		{
			for(Class p : parameters)
			{
				System.out.print(p.getName() + ", ");
			}
		}
		else
		{
			System.out.print("NONE");
		}
		System.out.print("\n");
	}
	//METHOD RETURN TYPE
	public void inspectMethodReturn(Method m, int depth)
	{
		Class returnType = m.getReturnType();
		printTabs(depth);
		System.out.println(" METHOD RETURN TYPE =  " + returnType.getName());
	}
	//METHOD MODIFIERS
	public void inspectMethodMods(Method m, int depth)
	{
		int mod = m.getModifiers();
		String modifiers = Modifier.toString(mod);
		printTabs(depth);
		System.out.println(" METHOD MODIFIERS = " + modifiers);
	}
	
	//FIELD METHOD
	public static void inspectField(Class c, Object obj, boolean recursive, int depth)
	{
		printTabs(depth);
		System.out.println("FIELDS FOR = " + c.getName());
		
		Field[] fields = c.getDeclaredFields();
		if(fields.length > 0)
		{
			for(Field f : fields)
			{
				int tempDepth = depth;
				f.setAccessible(true);
				
				//Get Name
				printTabs(depth);
				System.out.println("FIELD NAME = " + f.getName());
				
				//Get Type
				printTabs(depth);
				System.out.println(" FIELD TYPE = " + f.getType().getName());
				
				//Get Modifiers
				inspectFieldMods(f, depth);

				//Get Value
				inspectFieldValues(f, obj, recursive, tempDepth);
			}
		}
		else
		{
			printTabs(depth);
			System.out.println("FIELDS = NONE");
		}
	}
	//FIELD MODIFIERS
	public static void inspectFieldMods(Field f, int depth)
	{
		int m = f.getModifiers();
		if(!Modifier.toString(m).isEmpty())
		{
			printTabs(depth);
			System.out.println(" MODIFIERS = " + Modifier.toString(m));
		}
		else
		{
			printTabs(depth);
			System.out.println(" MODIFIERS = NONE");
		}
	}
	//FIELD VALUE
	public static void inspectFieldValues(Field f, Object obj, boolean recursive, int depth)
	{
		//If field is primitive
		if(f.getType().isPrimitive())
		{
			printTabs(depth);
			System.out.println(" FIELD VALUE = " + get(f, obj));
		}
		//Else if is array
		else if(f.getType().isArray())
		{
			Object fArr = get(f, obj);
			
			inspectArray(fArr, recursive, depth);
		}
		//Else is object
		else
		{
			inspectFieldObject(f, obj, recursive, depth);
		}
	}
	//FIELD VALUE OBJECT
	public static void inspectFieldObject(Field f, Object obj, boolean recursive, int depth)
	{
		if(get(f, obj) == null)
		{
			printTabs(depth);
			System.out.println(" FIELD VALUE = NULL");
		}
		else
		{
			printTabs(depth);
			System.out.println(" FIELD VALUE = " + get(f, obj).getClass().getTypeName() + "@" + get(f, obj).hashCode());
			
			if(recursive)
			{
				printTabs(depth);
				System.out.println(" RECURSING INTO FIELD VALUE.");
				inspectClass(get(f, obj).getClass(), get(f, obj), recursive, ++depth);
			}
		}
	}
	
	//ARRAY METHOD
	public static void inspectArray(Object obj, boolean recursive, int depth)
	{		
		printTabs(depth);
		System.out.println(" COMPONENT TYPE = " + obj.getClass().getComponentType());
		
		printTabs(depth);
		System.out.println(" LENGTH = " + Array.getLength(obj));
		
		for(int i = 0; i < Array.getLength(obj); i++)
		{
			int tempDepth = depth;
			Object o = Array.get(obj, i);
			if(o == null)
			{
				printTabs(depth);
				System.out.println(" ARRAY VALUE = NULL");
			}
			else if(obj.getClass().getComponentType().isPrimitive())
			{
				printTabs(depth);
				System.out.println(" ARRAY VALUE = " + o);
			}
			else
			{
				printTabs(depth);
				System.out.println(" ARRAY VALUE = " + o.getClass().getTypeName() + "@" + o.hashCode());
				if(recursive)
				{
					printTabs(depth);
					System.out.println(" RECURSING INTO ARRAY VALUE.");
					inspectClass(o.getClass(), o, recursive, ++tempDepth);
				}
			}
		}
	}
	
	//Print tab characters equal to depth
	private static void printTabs(int depth)
	{
		for(int tabCount = 0; tabCount < depth; tabCount++)
		{
			System.out.print('\t');
		}
	}
	
	//Get method obtained from TA Filobater Ghaly
	private static Object get(Field f, Object obj)
	{
		try
		{
			return f.get(obj);
		} 
		catch(IllegalAccessException | IllegalArgumentException ex)
		{
			System.err.println("Could not access field value: " + f + " for given object: " + obj);
			ex.printStackTrace();
			System.exit(1);
		}
		return null;
	}
}
