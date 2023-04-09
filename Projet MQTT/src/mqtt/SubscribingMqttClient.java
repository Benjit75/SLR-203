package mqtt;

import org.eclipse.paho.client.mqttv3.*;

public class SubscribingMqttClient {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: SubscribingMqttClient <qos> <cleanSession>");
            return;
        }
        
        String topic = "labs/paho-example-topic";
        int qos = args[0].equals("0") ? 0 : args[0].equals("1") ? 1 : 2;
        boolean cleanSession = Boolean.parseBoolean(args[1]);
        String brokerURI = "tcp://localhost:1883";
        String clientId = "myClientID_Sub";
        
        MqttClient mqttClient = null;
        try {
            mqttClient = new MqttClient(brokerURI, clientId);
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(cleanSession);
            mqttClient.setCallback(new MqttCallback() {
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection lost: " + cause.getMessage());
                }
                public void messageArrived(String topic, MqttMessage message) {
                    System.out.println("Message arrived on topic: " + topic);
                    System.out.println("Message QoS: " + message.getQos());
                    System.out.println("Message content: " + new String(message.getPayload()));
                }
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("Delivery complete: " + token.isComplete());
                }
            });
            System.out.println("Connecting to MQTT broker at: " + brokerURI);
            mqttClient.connect(connectOptions);
            System.out.println("Successfully connected");
            System.out.println("Subscribing to topic: " + topic);
            mqttClient.subscribe(topic, qos);
            System.out.println("Successfully subscribed");
            
            while (true) {
                System.out.println("Waiting for messages...");
                Thread.sleep(3000);
            }
        } catch (MqttException e) {
            System.out.println("MQTT exception: " + e.getMessage());
            System.out.println("MQTT exception (cause): " + e.getCause());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("Interrupted exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (mqttClient != null) {
                try {
                    mqttClient.disconnect();
                } catch (MqttException e) {
                    System.out.println("Error disconnecting MQTT client: " + e.getMessage());
                }
            }
        }
    }
}