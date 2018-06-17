package com.sunft.helloworld;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 消息接收方,消费者
 * Created by sunft on 2018/6/12.
 */
public class Receiver {

    private static final String REMOTE_ADDRESS = "tcp://localhost:61616";

    public static void main(String[] args) throws Exception {
        //第一步：建立ConnectionFactory工厂对象，需要填入用户名、密码，以及要连接的地址，
        // 均使用默认即可，默认端口为"tcp://localhost:61616"
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                REMOTE_ADDRESS
        );

        //第二步：通过ConnectionFactory工厂对象我们创建一个Connection连接,并且调用Connection的start方法开启方法,Connection默认是关闭的
        Connection connection = connectionFactory.createConnection();
        connection.start();

        ////第三步：通过Connection对象创建Session会话(上下文环境)，用于接收消息，参数配置1为是否启用事务，
        //参数配置2为签收模式，一般我们设置为自动签收,这里写成和生产者端统一
        Session session = connection.createSession(Boolean.FALSE, /*Session.AUTO_ACKNOWLEDGE*/Session.CLIENT_ACKNOWLEDGE);

        ////第四步：通过Session创建Destination对象，指的是一个客户端用来指定生产消息目标和消费消息来源的对象，
        //在PTP模式中，Destination被称作Queue即队列：在Pub/Sub模式，Destination被称作Topic即主题。在程序中可以使用多个Queue和Topic。
        //该queue一定要和生产者对应上
        Destination destination = session.createQueue("first");

        //第五步：通过session创建MessageConsumer
        MessageConsumer consumer = session.createConsumer(destination);

        while(true) {
//            TextMessage msg = (TextMessage) consumer.receive();
//            if(msg == null) {
//                break;
//            }
//            System.out.println("收到的内容：" + msg.getText());
            //使用receive线程会阻塞到这里
            TextMessage msg = (TextMessage) consumer.receive();
            if(msg == null) {
                break;
            }
            msg.acknowledge();//设置手动签收模式
            //System.out.println(msg.getStringProperty("name"));
            System.out.println("消费数据：" + msg.getText());
        }


    }

}
