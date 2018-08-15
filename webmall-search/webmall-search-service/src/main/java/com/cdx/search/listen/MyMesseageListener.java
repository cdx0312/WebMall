package com.cdx.search.listen;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 接收ActiveMQ发送的消息
 * Created by cdx0312
 * 2018/3/9
 */
public class MyMesseageListener implements MessageListener{
    @Override
    public void onMessage(Message message) {
        //接收到消息
        try {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            System.out.println(text);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
