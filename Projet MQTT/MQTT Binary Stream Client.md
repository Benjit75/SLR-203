# MQTTBinaryClient Class - TERNOT

The MQTTBinaryClient class is a simple implementation of a MQTT client that communicates with a MQTT broker using binary data. This implementation is different from using the commonly used Paho library, as it uses binary data directly instead of using an MQTT library.

## Class structure
The class contains only the main method :
```java
package mqtt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MQTTBinaryClient {
    public static void main(String[] args) throws IOException {
        // Implementation details...
    }
}
```

## Connecting to the MQTT Broker
### Openning a connection
First, we need to connect to the MQTT Broker using a `Socket` Object, and get the `InputStream` and `OutputStream`of the socket. That allows it to read and write binary data to the MQTT Broker :
```java
// Connect to the MQTT Broker
        try (Socket socket = new Socket("localhost", 1883)) {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            // Continuation of the implementation
        }
```
Here, the Broker is located at `localhost` and is listening on port `1883`, which is the default port for MQTT communication.

### Sending the CONNNECT message
To continue the connection, we need to sent the `CONNECT`message, definied as a byte array and sent to the Broker through the `out.write()` method :
```java
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
```

### Waiting for the CONNACK response
After sending the `CONNECT` message, the MQTTBinaryClient waits for the `CONNACK` response from the broker. This response is read using the `in.read()` method and is then displayed in the console :
```java
            // Read the CONNACK response from the Broker
            byte[] connack = new byte[4];
            in.read(connack);

            // Display the contents of the CONNACK response
            System.out.println("CONNACK Response:");
            for (byte b : connack) {
                System.out.print(String.format("0x%02X ", b));
            }
```

## Publish message
### Sending a PUBLISH message
We can now start sending messages. For that, we create a byte array `publishPacket`to represent the `PUBLISH` message and send it through the `out.write()` method :
```java
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
```
### PUBACK response
After sending the `PUBLISH` message, the broker will respond with a `PUBACK` if QoS=1. The following code shows how the `PUBACK` is read (very simmilar to `CONNACK`) :
```java
            // Read the PUBACK response from the Broker
            byte[] puback = new byte[4];
            in.read(puback);

            // Display the contents of the PUBACK response
            System.out.println("PUBACK Response:");
            for (byte b : puback) {
                System.out.print(String.format("0x%02X ", b));
            }
```

## Whole code
```java
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
```