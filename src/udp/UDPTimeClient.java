package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class UDPTimeClient {
	private static final String SERVER_IP = "192.168.1.67";
	private static final int SERVER_PORT = 7000;

	public static void main(String[] args) {
		DatagramSocket socket = null;
		
		try {
			// 1. UDP 소켓 생성
			socket = new DatagramSocket();
			
			// 2. 데이터 보내기
			String message = "";
			byte[] sendData = message.getBytes("utf-8");
			
			DatagramPacket sendPacket = new DatagramPacket(
					sendData, 
					sendData.length, 
					new InetSocketAddress(SERVER_IP, SERVER_PORT));
			socket.send(sendPacket);
			
			// 3. 데이터 받기
			DatagramPacket receivePacket = new DatagramPacket(new byte[UDPTimeServer.BUFFER_SIZE], UDPTimeServer.BUFFER_SIZE);
			socket.receive(receivePacket);
			
			// 4. 시간 및 날짜 출력
			String now = new String(receivePacket.getData(), 0, receivePacket.getLength(), "utf-8");
			System.out.println(now);
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null && socket.isClosed() == false) {
				socket.close();
			}
		}

	}

}
