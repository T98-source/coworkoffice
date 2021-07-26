package MQTT;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class GestioneMisureLocaliMQTT {

    // init the client
    private MqttClient mqttClient;
    MemoryPersistence persistence = new MemoryPersistence();

    /**
     * Constructor. It generates a client id and instantiate the MQTT client.
     */
    public GestioneMisureLocaliMQTT() {

        // the broker URL
        String brokerURL = "tcp://localhost:1883";

        try {
            mqttClient = new MqttClient(brokerURL, MqttClient.generateClientId(), persistence);

        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    /**
     * The method to start the subscriber. It listen to all the homestation-related topics.
     */
    public void start() {
        try {
            // set a callback and connect to the broker
            mqttClient.setCallback(new SubscribeCallBack());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("esp32");
            options.setPassword(new char[]{'2', '0', '1', '0', '2', '0', '1', '7', 't', 'o', 'm'});
            mqttClient.connect(options);

            //Subscribe to all subtopics of home
            final String topic = "/locali/+/sensori/temperatura";
            mqttClient.subscribe(topic);
            System.out.println("The subscriber is now listening to " + topic);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}