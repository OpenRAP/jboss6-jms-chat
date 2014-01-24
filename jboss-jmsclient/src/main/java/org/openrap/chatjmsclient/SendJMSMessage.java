package org.openrap.chatjmsclient;

import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SendJMSMessage {

    QueueConnection conn;
    QueueSession session;
    Queue que;

    public void setupConnection() throws JMSException, NamingException {
        Properties properties1 = new Properties();
        properties1.put(Context.INITIAL_CONTEXT_FACTORY,
                "org.jnp.interfaces.NamingContextFactory");
        properties1.put(Context.URL_PKG_PREFIXES,
                "org.jboss.naming:org.jnp.interfaces");

        properties1.put(Context.PROVIDER_URL, "jnp://127.0.0.1:1099");
        InitialContext iniCtx = new InitialContext(properties1);

        Object tmp = iniCtx.lookup("ConnectionFactory");
        QueueConnectionFactory qcf = (QueueConnectionFactory) tmp;
        conn = qcf.createQueueConnection();
        que = (Queue) iniCtx.lookup("queue/questionqueue");
        session = conn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
        conn.start();
        System.out.println("Connection Started");
    }

    public void stop() throws JMSException {
        conn.stop();
        session.close();
        conn.close();
    }

    public void sendAMessage(String msg) throws JMSException {

        QueueSender send = session.createSender(que);
        TextMessage tm = session.createTextMessage(msg);

        send.send(tm);
        send.close();
    }

    public static void main(String args[]) throws Exception {
        SendJMSMessage sm = new SendJMSMessage();
        sm.setupConnection();

        sm.sendAMessage("Hello World!");//xml.toString());
        sm.stop();

    }
}
