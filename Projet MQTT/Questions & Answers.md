# Questions and Answers - TERNOT

## *Q1: Once connected, can you find the 'Connection Options'?*
  
* *CleanSession - True or false ? What does this mean ? (Cf. lecture notes or online documentation)*

Clean session is set to `true`. This means the session can be declared as persistent or not. If `true`, the client information is stored until the client requests a clean session.

* *KeepAliveInterval - what is the value and its measuring unit ? What does it mean ?*

The `KeepAliveInterval` is the longest period, in seconds, without communication between the client and broker.

* *ConnectionTimeout - what is the value and its measuring unit ? What does it mean ?*

The `ConnectionTimeout` is the longest period, in seconds, while trying to connect to the MQTT server.

* *Is your connection secure? if so, how ?*

The connection is not secure. To make it secure, you need to set up a user/password connection.

## *Q2: Can you figure-out how to change your connection's configuration values ? E.g. change the KeepAliveInterval to 70s.*

To change the `KeepAliveInterval` value to 70s, go to `Manage connections -> detailed -> KeepAliveInterval`.

## *Q3: can you check that the message was sent correctly? (e.g. by checking the message's content, or by checking the message's properties)*

You can check if the message was sent correctly by checking the message content or properties in the window below.

## *Q4: how can you tell whether the subscription was established correctly ?*

You can tell if the subscription was established correctly by checking in the window below.

## *Clean Session and QoS Configurations*

### *Test 4.1: Clean Session = True, QoS = 0*

With `Clean Session` set to `True`, the client will not store any information about the connection. The `QoS` is set to `0`, so the message is sent and received only once, and the subscribing client will not receive it.

### *Test 4.2: Clean Session = False, QoS = 0*

With `Clean Session` set to `False`, the broker has not stored any information about the connection, so the client has not received the message.

### *Test 4.3: Clean Session = False, QoS = 1*

With `Clean Session` set to `False`, the broker has stored the information about the connection, so the client has received the message.

### *Test 4.4: Clean Session = True, QoS = 1*

With `Clean Session` set to `True`, the broker has not stored the information about the connection, so the client has not received the message.

### *Test 4.5: Clean Session = False for the subscriber and true for the publisher, QoS = 1 for both clients*

With `Clean Session` set to `False` for the subscriber and `True` for the publisher, the broker has stored the information about the connection, so the client has received the message.

### *Test 4.6: Same as above, except QoS = 0 for the publishers*

The publisher's QoS is set to 0, while the subscriber's QoS is set to 1. As a result, the client did not receive the message.

### *Test 4.7: Same as above, except QoS = 2 for both clients*

In this case, the QoS levels of the publisher and subscriber do not affect the outcome, as the message will not be stored by the broker due to the publisher's clean_session flag being set to true.

### *Clean Session = True, QoS = 0 (for both clients) and retain = True*

In this case, I only received the message once. This means the message was not stored by the broker.

## *MQTT Binary Stream Client*

### *How many bytes did the CONNACK message contain? What were their values?*

The CONNACK message contained 4 bytes, with the following values:

0x20
0x02
0x00
0x00

### *What is your interpretation of the byte values in the received CONNACK message? Do they make sense?*

The first byte (0x20) of the CONNACK message represents the Command Type and Control Flags. The value 0x20 indicates that this is a CONNACK message, and that no flags are set.

The second byte (0x02) represents the length of the remaining packet. In this case, it is 2 bytes.

The third and fourth bytes (0x00 and 0x00) represent the status code of the connection. A value of 0x00 means that the connection was accepted by the server.

The values in the received CONNACK message make sense, as they indicate a successful connection to the MQTT server.