package com.sunft.pb;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 消息发布方
 * Created by sunft on 2018/6/30.
 */
public class Publish {

    private static final String REMOTE_ADDRESS = "tcp://localhost:61616";

    private ConnectionFactory factory;

    private Connection connection;

    private Session session;

    private MessageProducer producer;

    public Publish() {
        try {
            factory = new ActiveMQConnectionFactory(
                    ActiveMQConnectionFactory.DEFAULT_USER,
                    ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                    REMOTE_ADDRESS);
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            producer = session.createProducer(null);
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage() throws Exception {
        Destination destination = session.createTopic("topic1");
        TextMessage textMessage = session.createTextMessage("我是内容");
        producer.send(destination, textMessage);
    }

    public static void main(String[] args) throws Exception {
        Publish p = new Publish();
        p.sendMessage();
    }

}
