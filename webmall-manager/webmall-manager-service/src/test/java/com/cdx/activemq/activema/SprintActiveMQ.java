package com.cdx.activemq.activema;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * Created by cdx0312
 * 2018/3/9
 */
public class SprintActiveMQ {
    /**
     * 使用JMSTemplate来发送消息
     * @throws Exception
     */
    @Test
    public void testJmsTemplate() throws Exception {
        //初始化Spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/applicationContext-activemq.xml");
        //从容器中获得模板对象
        JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
        //从容器中获得Destination对象
        Destination destination = (Destination) applicationContext.getBean("test-queue");
        //发送消息
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("spring activemq send queue message");
            }
        });
    }

    @Test
    public void testQueueConsumer() throws Exception {
        //初始化Spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/applicationContext-activemq.xml");
        System.in.read();
    }
}
