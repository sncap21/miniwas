package javaexam.miniwas;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WebApplicationServer {
	private int port;
	private DefaultServlet defaultServlet;
	private Map<String, RequestMapping> map; // path(key), RequestMapping(value)

	public WebApplicationServer(int port) {
		this.port = port;
		// 정적자원을 처리해주는 기본 서블릿
		defaultServlet = new DefaultServlet();

		// 딱한번 초기화된다.
		// 모든 서블릿 정보를 메모리에 올린다. 그리고 서블릿의 init()메소드를 호출한다.
		initServlet();

		WasShutdownHook wasShutdownHook = new WasShutdownHook(this);
		// jvm 이 종료될 때 실행할 Thread를 등록한다.
		Runtime.getRuntime().addShutdownHook(wasShutdownHook);
	}

	// Map을 초기화
	private void initServlet() {
		// servlet.properties에서 정보를 읽어들여 map을 초기화한다.
		map = new HashMap<>();
		// ClassLoader classLoader = getClass().getClassLoader();
		// InputStream propStream =
		// classLoader.getResourceAsStream("servlet.properties");

		try {
			try (InputStream propStream = new FileInputStream("./src/servlet.properties");) {

				Properties prop = new Properties();
				try {
					prop.load(propStream);

					Set<String> keys = prop.stringPropertyNames();
					for (String path : keys) {
						String className = prop.getProperty(path);
						RequestMapping mapping = new RequestMapping();
						mapping.setPath(path);
						mapping.setServletClassName(className);

						map.put(path, mapping);
					}
				} catch (Exception ex) {
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException("file not found : servlet.properties");
		}
	}

	public void destroy() {
		defaultServlet.destroy();
	}

	public void start() {
		ServerSocket listener = null;
		try {
			listener = new ServerSocket(port);
			System.out.println("client를 기다립니다.");
			while (true) {
				Socket client = listener.accept();

				new Thread(() -> {
					try {
						handleSocket(client);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}).start();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				listener.close(); // 서버소켓을 close
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	} // run

	private void handleSocket(Socket client) throws IOException {
		OutputStream out = client.getOutputStream();
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));

		// HttpResponse를 초기화한다.
		HttpResponse response = new HttpResponse();
		response.setOut(pw);
		response.setOutputStream(out);

		InputStream in = client.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line = null;

		// HttpRequest를 초기화한다.
		HttpRequest request = new HttpRequest();
		// 첫번째 줄을 읽어들인다. GET /hello
		line = br.readLine();
		System.out.println("*********line:::"+line);
		String[] firstLineArgs = line.split(" ");
		request.setMethod(firstLineArgs[0]);
		request.setPath(firstLineArgs[1]);

		while ((line = br.readLine()) != null) {
			if ("".equals(line)) { // 헤더를 읽고 빈줄을 만나면
				break;
			}
			String[] headerArray = line.split(" ");
			if (headerArray[0].startsWith("Host:")) {
				request.setHost(headerArray[1].trim());
			} else if (headerArray[0].startsWith("Content-Length:")) {
				int length = Integer.parseInt(headerArray[1].trim());
				request.setContentLength(length);
			} else if (headerArray[0].startsWith("User-Agent:")) {
				request.setUserAgent(line.substring(12));
			} else if (headerArray[0].startsWith("Content-Type")) {
				request.setContentType(headerArray[1].trim());
			}
		}
		System.out.println(request);

		// 사용자가 요청한 path가 map에 있으면 map에 있는 Servlet을 실행
		// 없으면 defaultServlet을 실행
		if (map.containsKey(request.getPath())) {
			System.out.println("path :" + request.getPath());
			RequestMapping mapping = map.get(request.getPath());
			if (mapping.getSerlvet() == null) {
				// class이름을 가지고 인스턴스를 생성한다.
				// Servlet s = new HiServlet();
				try {
					synchronized (mapping) {						
						Class clazz = Class.forName(mapping.getServletClassName());
						Servlet s = (Servlet) clazz.newInstance();
						mapping.setSerlvet(s);
						s.init();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			Servlet servlet = mapping.getSerlvet();
			servlet.service(request, response);
		} else {
			defaultServlet.service(request, response);
		}

		out.close();
		in.close();
		client.close(); // 클라이언트와 접속이 close된다.
	}
}
