package miniwas;

import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class wasMain {

	public static void main(String[] args) throws Exception {

		//1. property file load
		ClassLoader classLoader= Thread.currentThread().getContextClassLoader();
	    // classpath : src folder에 있을것
		InputStream propStream = classLoader.getResourceAsStream("servlet.properties");
	    Properties prop = new Properties();
	    prop.load(propStream);
	   
	    System.out.println("prop.stringPropertyNames : " + prop.stringPropertyNames());
    
	    RequestMapping reqMap = new RequestMapping();
	    
	    Set<String> keys = prop.stringPropertyNames();
	    for(String key : keys){
	    	System.out.println("prop.getProperty(key) : " + prop.getProperty(key));
	    	
	    	
	    	reqMap.setPath(prop.getProperty(key));
	    	
	    	
	    	Class wasClass = Class.forName(prop.getProperty(key));
			Object obj = wasClass.newInstance();
			System.out.println(obj.getClass().getName());
	    }
	    
		
		
		//2. socket create & accept
		
		//3. request 요청 시
		    // 3.1 request, response 내용 채우기 
			// 3.2 request mapping
		    // 3.3 servlet thread 실행
		   
		//4. servlet thread 실행 
		   // 4.1 init : servlet 생성. 최초 1회
		   // 4.2 service  : 
		   // 4.3 doGet, doPost
		   // 4.4 destory : thread delete
		
		

	}

}


