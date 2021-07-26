package MQTT;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import tasksDao.GestioneMisura;

public class SubscribeCallBack implements MqttCallback {

    GestioneMisura measureDao = new GestioneMisura();

    @Override
    public void connectionLost(Throwable cause) {
        // what happens when the connection is lost. We could reconnect here, for example.
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        if ("/locali/1/sensori/temperatura".equals(topic)) {
            System.out.println(message.toString());
        }
        System.out.println("Message arrived for the topic '" + topic + "': " + message.toString());

        // additional action for the Last Will and Testament message
        if ("home/LWT".equals(topic)) {
            System.err.println("Publisher is gone!");
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // called when delivery for a message has been completed, and all acknowledgments have been received
        // no-op, here
    }
}