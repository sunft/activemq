package com.sunft.action;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by sunft on 2018/6/30.
 * 生产者
 */
public class Producer {

    private static final String REMOTE_ADDRESS = "tcp://localhost:61616";

    //1、连接工厂
    private ConnectionFactory connectionFactory;

    //2、连接对象
    private Connection connection;

    //3、Session对象
    private Session session;

    //4、生产者
    private MessageProducer messageProducer;

    public Producer() {
        try {
            connectionFactory = new ActiveMQConnectionFactory(
                    ActiveMQConnectionFactory.DEFAULT_USER,
                    ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                    REMOTE_ADDRESS);
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            messageProducer = session.createProducer(null);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public Session getSession() {
        return this.session;
    }

    public void send1(/*String queueName, Message message*/) {
        try {
            Destination destination = this.session.createQueue("first");
            for(int i = 0; i < 100; i ++) {
                MapMessage msg = this.session.createMapMessage();
                int id = i;
                msg.setInt("id", id);
                msg.setString("name", "张" + i);
                msg.setString("age", "" + i);
                //通过取模的方式决定消息发送给哪一方
                String receiver = id % 2 == 0? "A":"B";
                msg.setStringProperty("receiver", receiver);
                this.messageProducer.send(destination, msg, DeliveryMode.NON_PERSISTENT, 2, 1000*60*10L);
                System.out.println("message send id:" + id);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Producer p = new Producer();
        p.send1();
    }

}
