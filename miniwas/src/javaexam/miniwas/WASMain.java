package javaexam.miniwas;

public class WASMain {
    public static void main(String[] args){
    	// Runnable을 구현하고 있는 WebApplicationServer객체를 생성한다.
        WebApplicationServer was = new WebApplicationServer(8888);
        was.start();
    }
}