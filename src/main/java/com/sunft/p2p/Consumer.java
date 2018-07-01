package com.sunft.p2p;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 消费者
 * Created by sunft on 2018/6/30.
 */
public class Consumer {

    private static final String REMOTE_ADDRESS = "tcp://localhost:61616";

    //定义选择器
    public static final String SELECTOR_0 = "age > 25";

    public static final String SELECTOR_1 = "color = 'blue'";

    public static final String SELECTOR_2 = "color = 'blue' AND sal > 2000";

    public static final String SELECTOR_3 = "receiver = 'A'";

    //public static final String SELECTOR_4 = "receiver = 'B'";

    //1、连接工厂
    private ConnectionFactory connectionFactory;

    //2、连接对象
    private Connection connection;

    //3、Session对象
    private Session session;

    //4、消费者
    private MessageConsumer messageConsumer;

    //5、目标地址
    private Destination destination;

    public Consumer() {
        try {
            this.connectionFactory = new ActiveMQConnectionFactory(
                    ActiveMQConnectionFactory.DEFAULT_USER,
                    ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                    REMOTE_ADDRESS);
            this.connection = this.connectionFactory.createConnection();
            this.connection.start();
            this.session = this.connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            this.destination = this.session.createQueue("first");
            //创建消费者的时候发生了变化,使用选择器,消费指定的消息
            this.messageConsumer = this.session.createConsumer(this.destination, SELECTOR_1);

        }catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void receiver() {
        try {
            this.messageConsumer.setMessageListener(new Listener());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义监听器
     */
    class Listener implements MessageListener {

        @Override
        public void onMessage(Message message) {

            try {
                if(message instanceof TextMessage) {
                    //do something
                }

                if(message instanceof MapMessage) {
                    MapMessage ret = (MapMessage) message;
                    System.out.println(ret.toString());
                    System.out.println(ret.getString("name"));
                    System.out.println(ret.getString("age"));
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        Consumer c = new Consumer();
        c.receiver();
    }

}





