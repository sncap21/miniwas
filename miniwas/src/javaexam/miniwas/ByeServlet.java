package javaexam.miniwas;

import java.io.PrintWriter;

import javaexam.miniwas.HttpRequest;
import javaexam.miniwas.HttpResponse;
import javaexam.miniwas.Servlet;

public class ByeServlet extends Servlet{
    public ByeServlet(){
        System.out.println("ByeServlet");
    }

    @Override
    public void init() {
        System.out.println("ByeServlet Init");
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        PrintWriter pw = response.getOut();

        pw.println("HTTP/1.1 200 OK");
        pw.println("Content-Type: text/html");
        pw.println();

        pw.println("bye");
        pw.flush();
        pw.close();
    }
}
