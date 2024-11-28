package com.dduo.dduoj.utils;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import com.sun.mail.util.MailSSLSocketFactory;

/**
 * @author Dduo
 * @Title SendEmailService
 */

public class JavaMailUtils {

    // 仅供测试
    public static void main(String[] args) throws Exception {
            send_email("我是标题", "12345", "1507319255@qq.com");
    }

    /**
     * 传入的参数是 邮件地址 和 发送的内容 和 发送的标题
     *
     * @param subject 标题
     * @param text 正文内容
     * @param mail 收件人地址
     * @throws Exception
     */
    public static void send_email(String subject, String text, String mail) throws Exception {
        // 如果jdk版本太高 要加这个
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("https.protocols", "TLSv1.2");
        // 配置邮件发送的相关属性
        Properties props = new Properties();
        props.setProperty("mail.debug", "true");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.host", "smtp.qq.com");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.ssl.enable", "true");
        // 设置SSL连接的工厂
        MailSSLSocketFactory msf = new MailSSLSocketFactory();
        msf.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.socketFactory", msf);
        // 创建邮箱会话

        // todo 填写发发件人的QQ邮箱和授权码
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("1732446549@qq.com", "mxytgpalxokeedjd");
            }
        });

        // 创建邮件消息对象
        Message message = new MimeMessage(session);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(new InternetAddress("1732446549@qq.com"));
        // 处理收件人部分，单个收件人和多个收件人的处理方式
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(mail));
        // 获取邮件传输对象
        Transport transport = session.getTransport();
        transport.connect();
        // 发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        // 关闭连接
        transport.close();
    }

}