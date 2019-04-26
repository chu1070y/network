package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

public class NSLookup {

	public static void main(String[] args) {
		
		BufferedReader bufferedReader = null;
		
		try {
			
			while(true) {
				//보조 스트림
				System.out.print("> ");
				InputStreamReader inputStreamReader = new InputStreamReader(System.in, "utf-8");
				bufferedReader = new BufferedReader(inputStreamReader);
				
				//읽기
				String hostname = bufferedReader.readLine();
				
				//스캐너가 위에 다해줌..
//				Scanner scanner = new Scanner(System.in);
//				System.out.print("> ");
//				String hostname = scanner.nextLine();
				
				if(hostname.equals("exit")) {
					break;
				}
				
				//호스트 이름의 ip주소를 모두 가져온다.
				InetAddress[] inetAddresses = InetAddress.getAllByName(hostname);
				
				for (InetAddress inet : inetAddresses) {
					System.out.println(hostname+" : "+ inet.getHostAddress()); 
				}
				
			}
			
			
		} catch (IOException e1) {
			e1.printStackTrace();
			
		} finally {
			try {
				if(bufferedReader != null) {
					bufferedReader.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				
			}
		}
		



	}

}
