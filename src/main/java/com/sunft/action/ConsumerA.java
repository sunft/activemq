package com.sunft.action;


import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.concurrent.*;

/**
 * Created by sunft on 2018/6/30.
 * 消费者A
 */
public class ConsumerA {

    private static final String REMOTE_ADDRESS = "tcp://localhost:61616";

    public static final String SELECTOR = "receiver = 'A'";

    //public static final String SELECTOR = "receiver = 'B'";

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

    public ConsumerA() {
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
            this.messageConsumer = this.session.createConsumer(this.destination, SELECTOR);
            System.out.println("Consumer A start ...");
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

    class Listener implements MessageListener {

        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(10000);
        //new LinkedBlockingQueue<Runnable>();
        ExecutorService executor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                20,
                120L,
                TimeUnit.SECONDS,
                queue);

        @Override
        public void onMessage(Message message) {
            try {
                if(message instanceof MapMessage) {
                    MapMessage ret = (MapMessage) message;
//                    Thread.sleep(500);
//                    System.out.println("处理任务:" + ret.getString("id"));
                    executor.execute(new MessageTask(ret));
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        ConsumerA c = new ConsumerA();
        c.receiver();
    }

}
