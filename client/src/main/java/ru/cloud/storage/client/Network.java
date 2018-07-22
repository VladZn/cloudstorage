package ru.cloud.storage.client;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.scene.Node;
import ru.cloud.storage.common.*;
import sun.nio.ch.Net;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Network {

    private Socket socket;
    private ObjectDecoderInputStream inputStream;
    private ObjectEncoderOutputStream outputStream;

    private static Network ourInstance = new Network();

    public static Network getInstance() {
        return ourInstance;
    }

    private Network() {
    }

    public void connect() throws IOException {
        socket = new Socket("localhost", 9999);
        inputStream = new ObjectDecoderInputStream(socket.getInputStream());
        outputStream = new ObjectEncoderOutputStream(socket.getOutputStream());
        //showMainScreen((Node)actionEvent.getSource());
        //RequestProtocol reqGet = new RequestProtocol("login","cloud-storage.zip", RequestProtocol.CMD_GET_FILE);
        //RequestProtocol reqPut = new RequestProtocol("user", "123.txt", RequestProtocol.CMD_PUT_FILE);
        //Path path = Paths.get("C:\\Temp\\", "123.txt");

//        reqPut.setFile(path);
//        reqPut.setFileSize(Files.size(path));

        Thread thread = new Thread(()->{
            while (true){
                try {
                    Object inboundMsg = Network.getInstance().readMsg();
                    if (inboundMsg instanceof ResponseMsg){
                        ResponseMsg respMsg = (ResponseMsg) inboundMsg;
                        if (respMsg.getCmd() == Command.AUTH_OK) {
                            ScreenManager.showMainScreen();
                        } else if (respMsg.getCmd() == Command.OK){
                            System.out.println("File successfully uploaded");
                        }
                    } else if (inboundMsg instanceof FileListMsg) {
                        FileListMsg fileListMsg = (FileListMsg) inboundMsg;
                        System.out.println(fileListMsg.getFileList());
                    } else if (inboundMsg instanceof FileMsg) {
                        FileMsg fileMsg = (FileMsg) inboundMsg;
                        Path path = Paths.get("C:\\Temp\\", fileMsg.getFileName());
                        Files.write(path, fileMsg.getFileBinary());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
//        try (Socket socket = new Socket("localhost", 9999)) {
//            outputStream.writeObject(reqPut);
//            outputStream.flush();
//
//            outputStream.writeObject(reqGet);
//            outputStream.flush();
//
//            inputStream = new ObjectDecoderInputStream(socket.getInputStream());
//            ResponseProtocol response = null;
//            int i = 0;
//            while ( i < 2){
//                response = (ResponseProtocol) inputStream.readObject();
//                System.out.println(response.getFileName());
//                i++;
//            }
//            if (response != null) {
//                Files.write(Paths.get("c:\\Temp\\", response.getFileName()), response.getFile());
//                System.out.println("Downloaded " + response.getFileName());
//            } else
//                System.out.println("null");
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                outputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            inputStream.close();
//        }

    }

    public boolean isConnected(){
        return socket != null || socket.isConnected();
    }

    public void sendMsg(BaseMsg msg) throws IOException {
        outputStream.writeObject(msg);
        outputStream.flush();
    }

    public Object readMsg() throws IOException, ClassNotFoundException {
        return inputStream.readObject();
    }

    public void close(){
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
