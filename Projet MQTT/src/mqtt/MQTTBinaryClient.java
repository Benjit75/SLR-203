package mqtt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MQTTBinaryClient {
	public static void main(String[] args) throws IOException {
		// Connect to the MQTT Broker
		try (Socket socket = new Socket("localhost", 1883)) {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            // Define the byte array for the CONNECT message
            byte[] connectPacket = {
            		// Message Type (CONNECT)
            		0x10,
            		// Remaining Length
            		0x13,
            		// Protocol Name Length
            		0x00, 0x04,
            		// Protocol Name (MQTT)
            		'M', 'Q', 'T', 'T',
            		// Protocol Level (4)
            		0x04,
            		// Connect Flags
            		0x02,
            		// Keep Alive
            		0x00, 0x3C,
            		// Payload Length
            		0x00, 0x08,
            		// Client Identifier
            		'B', 'e', 'n', 'j', 'a', 'm', 'i', 'n'};

            // Send the CONNECT message to the Broker
            out.write(connectPacket);

            // Read the CONNACK response from the Broker
            byte[] connack = new byte[4];
            in.read(connack);

            // Display the contents of the CONNACK response
            System.out.println("CONNACK Response:");
            for (byte b : connack) {
                System.out.print(String.format("0x%02X ", b));
            }

	        // Define the byte array for the PUBLISH message
	        byte[] publishPacket = {
	        		// Message Type (PUBLISH)
	        		0x30,
	        		// Remaining Length
	        		0x0D,
	        		// Topic Length
	        		0x00, 0x04,
	        		// Topic (test)
	        		't', 'e', 's', 't',
	        		// Message Identifier
	        		0x00, 0x01,
	        		// Payload Length
	        		0x06,
	        		// Payload
	        		'H', 'e', 'l', 'l', 'o', '!'};

	        // Write the PUBLISH message to the Broker
	        out.write(publishPacket);

	        // Read the PUBACK response from the Broker
	        byte[] puback = new byte[4];
	        in.read(puback);

	        // Display the contents of the PUBACK response
	        System.out.println("PUBACK Response:");
	        for (byte b : puback) {
	        	System.out.print(String.format("0x%02X ", b));
	        }
    	} catch (IOException e) {
            e.printStackTrace();
        }
	}
}
