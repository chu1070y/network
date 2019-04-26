package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class KeyboardTest {

	public static void main(String[] args) {
		//br을 close하는 이유는 bufferreader가 inputstreamreader와 system.in 모두를 가지고 있어서
		//br을 닫으면 나머지도 닫힌다.
		BufferedReader br = null;
		
		try {
			//기반스트림(표준입력, 키보드, System.in)
			
			
			//보조스트림1
			//byte|byte|byte -> char
			InputStreamReader isr = new InputStreamReader(System.in, "utf-8");
			
			//보조스트림2
			//char|char|char|\n -> "char1char2char3"
			br = new BufferedReader(isr);
			
			//read
			String line = null;
			while((line = br.readLine()) != null) {
				if("exit".equals(line)) {
					break;
				}
				System.out.println(">>" + line);
			};
			
		} catch (IOException e) {
			System.out.println("error: " + e);
			
		} finally {
			try {
				if(br != null) {
					br.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
