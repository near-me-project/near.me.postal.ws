package services;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MessageBrokerService {

    public static final String MOBILE_NOTIFICATIONS_QUEUE = "mobile-notifications";
    private ConnectionFactory factory;

    public MessageBrokerService() {
        factory = new ConnectionFactory();
        factory.setHost("localhost");
    }


    public void declareInfoServiceListener() {
        try {
            declareListenForIncomeUserDeleteEvent(factory);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getCause());
        }
    }

    private void declareListenForIncomeUserDeleteEvent(ConnectionFactory factory) throws IOException, TimeoutException {
        Channel channel = factory.newConnection().createChannel();

        channel.queueDeclare(MOBILE_NOTIFICATIONS_QUEUE, true, false, false, null); // MOBILE_NOTIFICATIONS_QUEUE, durable, exclusive, autoDelete, arguments
        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            new ClientNotificatorService().notifyClientWithMessage(delivery.getBody())
                    .thenAccept((resSuccess) -> {
                        if(resSuccess) accept(channel, delivery);
                        else reject(channel, delivery);
                    })
                    .exceptionally((ex) -> reject(channel, delivery));
        };

        final boolean autoAck = false;
        channel.basicConsume(MOBILE_NOTIFICATIONS_QUEUE, autoAck, deliverCallback, consumerTag -> {
        });
    }

    private Void reject(Channel channel, Delivery delivery) {
        try {
            channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void accept(Channel channel, Delivery delivery) {
        try {
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
