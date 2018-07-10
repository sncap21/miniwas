package javaexam.miniwas;

import java.io.PrintWriter;

import javaexam.miniwas.HttpRequest;
import javaexam.miniwas.HttpResponse;
import javaexam.miniwas.Servlet;

public class HelloServlet extends Servlet{
    public HelloServlet(){
        System.out.println("HelloServlet 생성자");
    }
 
    @Override
    public void init() {
        System.out.println("HelloServlet Init 메소드");
    }
    
    @Override
    public void service(HttpRequest request, HttpResponse response) {
    	PrintWriter pw = response.getOut();
    	pw.println("HTTP/1.1 200 OK");
        pw.println("Content-Type: text/html");
        pw.println();
    	pw.println("hello");
        pw.flush();
        pw.close();
    }
}
