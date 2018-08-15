package com.cdx.activemq.activema;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

import javax.jms.*;
import java.io.IOException;

/**
 * Created by cdx0312
 * 2018/3/9
 */
public class TestActiveMQ {
    /**
     * ActiveMQ队列模式生产者
     * @throws JMSException
     */
    @Test
    public void testQueueProducer() throws JMSException {
        //1.创建一个连接工厂对象、需要指定IP和端口/消息服务端口为61616
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
        //2.使用连接工厂来创建连接
        Connection connection = connectionFactory.createConnection();
        //3.开启连接
        connection.start();
        //4.创建一个会话，
        //第一个参数为是否开启ActiveMQ的事务，一般不使用事务
        //如果开启事务，第二个参数自动忽略，不开启事务，第二个参数表示消息的应答模式，自动应答、手动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.使用Session对象来创建一个Destination对象，topic或者queue
        Queue queue = session.createQueue("test-Queue");
        //6.使用Session对象来创建一个生产者
        MessageProducer producer = session.createProducer(queue);
        //7.创建一个TextMessage对象
        TextMessage textMessage = new ActiveMQTextMessage();
        textMessage.setText("hello!");
        //8.发送消息
        producer.send(textMessage);
        //9.关闭资源
        producer.close();
        session.close();
        connection.close();
    }

    /**
     * ActiveMQ队列模式消费者
     * @throws JMSException
     */
    @Test
    public void testQueueConsumer() throws JMSException, IOException {
        //1.创建一个连接工厂对象、需要指定IP和端口/消息服务端口为61616
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
        //2.使用连接工厂来创建连接
        Connection connection = connectionFactory.createConnection();
        //3.开启连接
        connection.start();
        //4.创建一个会话，
        //第一个参数为是否开启ActiveMQ的事务，一般不使用事务
        //如果开启事务，第二个参数自动忽略，不开启事务，第二个参数表示消息的应答模式，自动应答、手动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.使用Session对象来创建一个Destination对象，topic或者queue
        Queue queue = session.createQueue("test-Queue");
        //6.使用Session对象来创建一个消费者
        MessageConsumer consumer = session.createConsumer(queue);
        //7.接收消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;
                String text = null;
                try {
                    text = textMessage.getText();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
                System.out.println(text);
            }
        });
        //8.关闭资源
        System.in.read();
        consumer.close();
        session.close();
        connection.close();
    }

    /**
     * ActiveMQ的订阅模式生产者
     */
    @Test
    public void testTopicProducer() throws Exception {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("test-topic");
        MessageProducer producer = session.createProducer(topic);
        TextMessage activeMQ_topic = session.createTextMessage("activeMQ topic");
        producer.send(activeMQ_topic);

        producer.close();
        session.close();
        connection.close();
    }

    /**
     * ActiveMQ的订阅者模式消费者
     */
    @Test
    public void testTopicConsumer() throws Exception{
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("test-topic");
        MessageConsumer consumer = session.createConsumer(topic);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        String text = textMessage.getText();
                        System.out.println(text);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        System.in.read();
        consumer.close();
        session.close();
        connection.close();
    }
}
