package ru.cloud.storage.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import ru.cloud.storage.common.*;

import java.util.Arrays;

public class AuthGatewayHandler extends ChannelInboundHandlerAdapter {
    private boolean authorized = false;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg == null) return;

        System.out.println("Authentication...");
        if (!authorized){
            if (msg instanceof AuthMsg){
                AuthMsg authMsg = (AuthMsg) msg;
                String login = authMsg.getLogin();
                byte[] password = authMsg.getPassword();
                System.out.println(login);
                System.out.println(Arrays.toString(password));
                //TODO получаем id пользователя из базы
                int userId = 1;
                if (userId > 0){
                    authorized = true;
                    ResponseMsg responseMsg = new ResponseMsg(authMsg.getLogin(), Command.AUTH_OK);
                    responseMsg.setUserId(userId);
                    ctx.writeAndFlush(responseMsg);
                    //TODO отправка списка файлов без родительского каталога пользователя на сервере
                    FileListMsg fileListMsg = new FileListMsg(authMsg.getLogin());
                    fileListMsg.setFileList("d:\\Downloads\\AndroidPacman\\");
                    ctx.writeAndFlush(fileListMsg);
                    ctx.pipeline().addLast(new CloudServerHandler());
                } else {
                    ResponseMsg responseMsg = new ResponseMsg(authMsg.getLogin(), Command.AUTH_FAILED);
                    ctx.writeAndFlush(responseMsg);
                }
            } else
                ReferenceCountUtil.release(msg);
        } else {
            ctx.fireChannelRead(msg);
            ctx.flush();
        }
    }

}
