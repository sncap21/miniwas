package javaexam.miniwas;

public class HttpRequest {
    private String method; // GET, POST, PUT, DELETE .......
    private String path; //
    private String host; // Host
    private int contentLength; // Content-Length body길이
    private String userAgent; // User-Agent 브라우저 정보
    private String contentType; // Content-Type 사용자가 요청한 컨텐츠의 타입

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", host='" + host + '\'' +
                ", contentLength='" + contentLength + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", contentType='" + contentType + '\'' +
                '}';
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
