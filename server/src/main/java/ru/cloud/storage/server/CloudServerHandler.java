package ru.cloud.storage.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.ReferenceCountUtil;
import ru.cloud.storage.common.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CloudServerHandler extends ChannelInboundHandlerAdapter {

    //private File file;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg == null)
                return;
            System.out.println(msg.getClass());
            //TODO создать класс запросов действий с файлами???
            if (msg instanceof FileMsg) {
                FileMsg fileMsg= (FileMsg) msg;
                //TODO получать каталог пользователя по логину
                Path path = Paths.get("D:\\Downloads\\", fileMsg.getFileName());
                if (fileMsg.getCmd() == Command.PUT_FILE){
                    Files.write(path, fileMsg.getFileBinary());
                    ResponseMsg responseMsg = new ResponseMsg(fileMsg.getLogin(), Command.OK);
                    ctx.writeAndFlush(responseMsg);
                } else if (fileMsg.getCmd() == Command.GET_FILE){
                    if (Files.exists(path)) {
                        fileMsg.setFileBinary(path);
                        fileMsg.setCmd(Command.OK);
                        ctx.writeAndFlush(fileMsg);
                    } else {
                        fileMsg.setCmd(Command.NO_SUCH_FILE);
                    }
                }
            } else {
                System.out.printf("Server received wrong object!");
                return;
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client  connected");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}
