package javaexam.miniwas;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class DefaultServlet extends Servlet {
	@Override
	public void service(HttpRequest request, HttpResponse response) {
		FileInputStream fis = null;
		try {
			String baseDir = "/tmp/wasroot";
			String fileName = request.getPath(); //
			if ("/".equals(fileName)) {
				fileName = "/index.html";
			}
			fileName = baseDir + fileName;
			String contentType = "text/html; charset=UTF-8";
			if (fileName.endsWith(".png")) {
				contentType = "image/png";
			}
			if (fileName.endsWith(".jpg")) {
				contentType = "image/jpg";
			}
			File file = new File(fileName); // java.io.File

			PrintWriter pw = response.getOut();
			OutputStream out = response.getOutputStream();

			try {
				fis = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				System.out.println("파일이 없어요-_-;");
				file = new File(baseDir + "/notfound.html");
				fis = new FileInputStream(file);
			}
			long fileLength = file.length();
			
			// 헤더와 빈줄을 출력 char단위 출력
			pw.println("HTTP/1.1 200 OK");
			pw.println("Content-Type: " + contentType);
			pw.println("Content-Length: " + fileLength);
			pw.println();
			pw.flush(); 
			
			// body부분을 출력. byte단위로 출력			
			byte[] buffer = new byte[1024];
			int readCount = 0;
			while ((readCount = fis.read(buffer)) != -1) {
				out.write(buffer, 0, readCount);
			}

			out.flush();
			out.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void destroy() {
		System.out.println("default Servlet의 destroy!!");
	}
}
