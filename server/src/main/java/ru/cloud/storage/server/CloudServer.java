package ru.cloud.storage.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class CloudServer {
    private String host;
    private int port;
    private int MAX_OBJ_SIZE = 1024 * 1024 * 1000;
    private Path folder;

    public CloudServer() {
    }

    private void readServerProperties(){
        try (Reader in = new InputStreamReader(this.getClass().getResourceAsStream("/config.properties"))) {
            Properties properties = new Properties();
            properties.load(in);
            host = properties.getProperty("host");
            port = Integer.parseInt(properties.getProperty("port"));
            folder = Paths.get(properties.getProperty("folder"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() throws Exception{
        EventLoopGroup mainGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            readServerProperties();
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(mainGroup, workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .localAddress(new InetSocketAddress(host, port))
                            .handler(new LoggingHandler(LogLevel.INFO))
                            .childHandler(new CloudServerInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //.option(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            channelFuture.channel().closeFuture().sync();
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            mainGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
