package http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.file.Files;

public class RequestHandler extends Thread {
	
	private static String documentRoot = "";
	
	static {
		//클래스가 로딩될 때 이부분이 실행이 된다.
		try {
			//경로 얻는 방법
			documentRoot = new File(RequestHandler.
					class.
					getProtectionDomain().
					getCodeSource().
					getLocation().
					toURI()).
					getPath();
			documentRoot += "/webapp";
			System.out.println("-------->" + documentRoot);
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private Socket socket;
	
	public RequestHandler( Socket socket ) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			// logging Remote Host IP Address & Port
			InetSocketAddress inetSocketAddress = ( InetSocketAddress )socket.getRemoteSocketAddress();
			consoleLog( "connected from " + inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort() );
			
			// get IOStream
			OutputStream os = socket.getOutputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
			
			String request = null;
			
			while(true) {
				String line = br.readLine();
				
				//브라우저가 연결을 끊으면...
				if(line == null) {
					break;
				}
				
				// Rquest Header만 읽음
				if("".equals(line)) {
					break;
				}
				
				// Header의 첫번째 라인만 처리
				if(request == null) {
					request = line;
				}	
			}
			
			String[] tokens = request.split(" ");
			if("GET".contentEquals(tokens[0])) {
				consoleLog("request:" + tokens[1]);
				responseStaticResource(os, tokens[1], tokens[2]);
			} else {
				// POST, DELETE, PUT, HEAD, CONNECT
				// 와 같은 Methods는 무시
				
				// 응답예시
				// HTTP/1.1 400 Bad Request\r\n
				// Content-Type:text/html; charset=utf-8\r\n
				// \r\n
				// HTML 에러 문서(./webapp/error/400.html)
				
				response400Error(os, tokens[2]);
				consoleLog("Bad Request:" + tokens[1]);

			}
			
			
		} catch( Exception ex ) {
			consoleLog( "error:" + ex );
		} finally {
			// clean-up
			try{
				if( socket != null && socket.isClosed() == false ) {
					socket.close();
				}
				
			} catch( IOException ex ) {
				consoleLog( "error:" + ex );
			}
		}			
	}
	
	public void responseStaticResource(OutputStream os, String url, String protocol) throws IOException{
		
		if("/".equals(url)) {
			url = "/index.html";
		}
		
		File file = new File( documentRoot + url);
		if(file.exists() == false) {
			// 응답예시
			// HTTP/1.1 404 File Not Found\r\n
			// Content-Type:text/html; charset=utf-8\r\n
			// \r\n
			// HTML 에러 문서(./webapp/error/404.html)
			
			response404Error(os, protocol);
			return;
		}
		
		//nio
		byte[] body = Files.readAllBytes(file.toPath());
		String contentType = Files.probeContentType(file.toPath());
		
		//응답
		os.write( (protocol + " 200 OK\r\n").getBytes( "UTF-8" ) );
		os.write( ("Content-Type:" + contentType + "; charset=utf-8\r\n").getBytes( "UTF-8" ) );
		os.write( "\r\n".getBytes() );
		os.write( body );
		
	}
	
	public void response400Error(OutputStream os, String protocol) throws IOException{
		
		ErrorPage(os, protocol, "400");
	}
	
	public void response404Error(OutputStream os, String protocol) throws IOException {
		ErrorPage(os, protocol, "404");
	}

	public void ErrorPage(OutputStream os, String protocol, String url) throws IOException{
		File file = new File( documentRoot + "/error/"+ url + ".html");
		if(file.exists() == false) {
			return;
		}
		
		//nio
		byte[] body = Files.readAllBytes(file.toPath());
		String contentType = Files.probeContentType(file.toPath());
		
		//응답
		os.write( (protocol + " 200 OK\r\n").getBytes( "UTF-8" ) );
		os.write( ("Content-Type:" + contentType + "; charset=utf-8\r\n").getBytes( "UTF-8" ) );
		os.write( "\r\n".getBytes() );
		os.write( body );
	}

	public void consoleLog( String message ) {
		System.out.println( "[RequestHandler#" + getId() + "] " + message );
	}
}
