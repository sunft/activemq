package com.sunft.pb;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 消费者3订阅了topic主题
 * Created by sunft on 2018/6/30.
 */
public class Consumer3 {

    private static final String REMOTE_ADDRESS = "tcp://localhost:61616";

    private ConnectionFactory factory;

    private Connection connection;

    private Session session;

    private MessageConsumer consumer;

    public Consumer3() {
        try{
            factory = new ActiveMQConnectionFactory(
                    ActiveMQConnectionFactory.DEFAULT_USER,
                    ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                    REMOTE_ADDRESS);
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void receive() throws Exception {
        Destination destination = session.createTopic("topic1");
        consumer = session.createConsumer(destination);
        consumer.setMessageListener(new Listener());
    }

    class Listener implements MessageListener {

        @Override
        public void onMessage(Message message) {
            try{
                if(message instanceof TextMessage) {
                    System.out.println("c3收到消息：---------------");
                    TextMessage m = (TextMessage) message;
                    System.out.println(m.getText());
                }
            }catch (JMSException e){
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) throws Exception {
        Consumer3 c3 = new Consumer3();
        c3.receive();
    }

}
