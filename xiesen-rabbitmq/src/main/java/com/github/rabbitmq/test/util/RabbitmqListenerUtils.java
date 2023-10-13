package com.github.rabbitmq.test.util;

import com.rabbitmq.client.*;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @author xiesen
 */
public class RabbitmqListenerUtils {
    public static final String COLON_STR = ":";
    private Connection connection;
    private static final int DEFAULT_PORT = 5672;
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_USERNAME = "guest";
    private static final String DEFAULT_PASSWORD = "guest";
    private Channel channel;

    public RabbitmqListenerUtils() {
    }

    public RabbitmqListenerUtils(String host, int port, String username, String password) throws IOException,
            TimeoutException {
        initConnection(host, port, username, password);
    }

    public RabbitmqListenerUtils(String address, String username, String password) throws IOException,
            TimeoutException {
        initConnection(address, username, password);
    }


    /**
     * 获取连接
     *
     * @param host
     * @param port
     * @param username
     * @param password
     * @return
     * @throws IOException
     * @throws TimeoutException
     */
    public static Connection getConnection(String host, int port, String username, String password) throws IOException, TimeoutException {
        // 获取工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setVirtualHost(username);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory.newConnection();
    }

    /**
     * 解析rabbitmq 地址
     *
     * @param addresses 地址列表 例如: "192.168.70.215:5672,192.168.70.216:5672,192.168.70.217:5672"
     * @return {@link List < Address >}
     */
    private List<Address> parseAddresses(String addresses) {
        List<Address> parsedAddresses = new ArrayList<>();
        for (String address : StringUtils.commaDelimitedListToStringArray(addresses)) {
            parsedAddresses.add(parseHostAndPort(address));
        }
        return parsedAddresses;
    }

    /**
     * 解析主机与端口
     *
     * @param input 例如: 192.168.70.215:5672
     * @return {@link Address}
     */
    private Address parseHostAndPort(String input) {
        input = input.trim();
        int portIndex = input.indexOf(COLON_STR);
        if (portIndex == -1) {
            return new Address(input, DEFAULT_PORT);
        } else {
            return new Address(input.substring(0, portIndex), Integer.parseInt(input.substring(portIndex + 1)));
        }
    }

    /**
     * 初始化连接
     *
     * @param host
     * @param port
     * @param username
     * @param password
     * @return
     * @throws IOException
     * @throws TimeoutException
     */
    private void initConnection(String host, int port, String username, String password) throws IOException,
            TimeoutException {
        // 获取工厂
        if (connection == null) {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(host);
            connectionFactory.setPort(port);
            connectionFactory.setVirtualHost(username);
            connectionFactory.setUsername(username);
            connectionFactory.setPassword(password);
            this.connection = connectionFactory.newConnection();
        }
    }

    /**
     * 初始化连接
     *
     * @param address  rabbitmq 连接地址
     * @param username 用户名,密码
     * @param password
     * @throws IOException
     * @throws TimeoutException
     */
    private void initConnection(String address, String username, String password) throws IOException,
            TimeoutException {
        // 获取工厂
        if (connection == null) {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setVirtualHost(username);
            connectionFactory.setUsername(username);
            connectionFactory.setPassword(password);

            this.connection = connectionFactory.newConnection(parseAddresses(address));
        }
    }

    /**
     * 获取channel
     *
     * @throws IOException
     * @throws TimeoutException
     */
    public Channel getChannel() throws IOException, TimeoutException {
        if (this.channel == null) {
            this.channel = this.connection.createChannel();
        }
        return this.channel;
    }

    /**
     * 监听固定channel数据
     *
     * @param exchange rabbitmq多路交换机地址
     * @param channel  连接通道
     * @param consumer 自定义消费者
     * @throws Exception
     */
    public void consumerMessage(String exchange, Channel channel, Consumer consumer) throws Exception {
        //获取一个临时队列
        String queueName = channel.queueDeclare().getQueue();
        //在信道中设置交换器
        channel.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT, true);
        channel.queueBind(queueName, exchange, "");
        channel.basicQos(1);
        //回复队列应答 -- RabbitMQ中的消息确认机制
        channel.basicConsume(queueName, false, consumer);
    }
}