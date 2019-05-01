package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UDPTimeServer {
	public static final int PORT = 7000;
	public static final int BUFFER_SIZE = 1024;

	public static void main(String[] args) {
		DatagramSocket socket = null;
		
		try {
			// 1. UDP 소켓 생성
			socket = new DatagramSocket(PORT);
			
			// 2. 수신 대기
			System.out.println("packet 수신 대기");
			DatagramPacket receivePacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
			socket.receive(receivePacket);
			
			// 3. 수신 데이터 출력
			String message = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");
			System.out.println("packet 수신:" + message);
			
			// 4. 현재 날짜&시간 구하기
			SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss a" );
			Calendar now = Calendar.getInstance();
			String data = format.format(now.getTime());
			System.out.println(data);
			
			// 5. 데이터 보내기
			byte[] sendData = data.getBytes("utf-8");
			DatagramPacket sendPacket = new DatagramPacket(
					sendData, 
					sendData.length,
					receivePacket.getAddress(), 
					receivePacket.getPort());
			socket.send(sendPacket);
			
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(socket != null && socket.isClosed() == false) {
				socket.close();
			}
		}

	}
	
	

}
