package MQTT.publisher;

import MQTT.subscriber.MQTTSubscriber;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import tasks.Attuatore;
import tasks.Misura;
import tasksDao.GestioneAttuatore;
import tasksDao.GestioneSensore;

import java.time.LocalDate;
import java.time.Month;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Sample publisher for MQTT. It uses the Eclipse Paho library and Mosquitto as a broker.
 * Mosquitto is expected to be installed and launched locally
 * (public test servers are available, however).
 *
 * It connects to the Mosquitto broker, set up a Last Will and Testament for the connection,
 * and publish a sample temperature value (i.e., a string equal to "20 C") on a specific topic.
 *
 * @author <a href="mailto:luigi.derussis@uniupo.it">Luigi De Russis</a>
 * @version 1.1 (21/05/2019)
 */
public class MQTTPublisher {

    // init the client
    private MqttClient client;
    MemoryPersistence persistence = new MemoryPersistence();

    /**
     * Constructor. It generates a client id and instantiate the MQTT client.
     */
    public MQTTPublisher() {

        // the broker URL
        String brokerURL = "tcp://localhost:1883";

        // A randomly generated client identifier based on the user's login
        // name and the system time
        String clientId = MqttClient.generateClientId();

        try {
            // create a new MQTT client
            client = new MqttClient(brokerURL, clientId, persistence);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * The method to start the publisher. Currently, it sets a Last Will and Testament
     * message, open a non persistent connection, and publish a temperature value
     */
    public void start() {
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            // persistent, durable connection
            options.setCleanSession(false);
            options.setUserName("esp32");
            options.setPassword(new char[]{'2', '0', '1', '0', '2', '0', '1', '7', 't', 'o', 'm'});

            // connect the publisher to the broker
            client.connect(options);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * @throws MqttException
     */
    public void publishMeasurement(Misura measure) throws MqttException {

        String topicRaw = "/locali/" + measure.getLocalId() + "/attuatori/" + measure.getType();

        MqttTopic topic = client.getTopic(topicRaw);

        String message = "";
        if(measure.getType().equals("temperatura")) {
            if(Integer.valueOf(measure.getMeasurement()) > 27) message = "raffreddamento";
            else if(Integer.valueOf(measure.getMeasurement()) < 20) message = "riscaldamento";
            else message = "off";
        } else if(measure.getType().equals("luminosita")) {
            if(Integer.valueOf(measure.getMeasurement()) > 40) message = "off";
            else message = "on";
        } else if(measure.getType().equals("gas")) {
            if(Integer.valueOf(measure.getMeasurement()) > 30) message = "on";
            else message = "off";
        } else return;

        // publish the message on the given topic
        topic.publish(new MqttMessage(message.getBytes()));

        // debug
        System.out.println("Published message on topic '" + topic.getName() + "': " + message);
    }

}
