package miniwas;

import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class propLoadTest {

	public static void main(String[] args) throws Exception {
		ClassLoader classLoader= Thread.currentThread().getContextClassLoader();
	    // classpath
		InputStream propStream = classLoader.getResourceAsStream("servlet.properties");
	    Properties prop = new Properties();
	    prop.load(propStream);
	    
	    Set<String> keys = prop.stringPropertyNames();
	    for(String key : keys){
			Class wasClass = Class.forName(prop.getProperty(key));
			Object obj = wasClass.newInstance();
			//System.out.println(obj.getClass().getName());
	    }
	}
}
