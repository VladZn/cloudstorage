package ru.cloud.storage.client;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import ru.cloud.storage.common.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Network {

    private String host;
    private int port;
    private Path folder;

    private Socket socket;
    private String login;

    private ObjectDecoderInputStream inputStream;
    private ObjectEncoderOutputStream outputStream;

    private static Network ourInstance = new Network();

    static Network getInstance() {
        return ourInstance;
    }

    private Network() {
    }

    void connect() throws IOException {
        readClientProperties();
        socket = new Socket(host, port);
        inputStream = new ObjectDecoderInputStream(socket.getInputStream());
        outputStream = new ObjectEncoderOutputStream(socket.getOutputStream());

        Thread thread = new Thread(()->{
            while (true){
                try {
                    Object inboundMsg = Network.getInstance().readMsg();
                    if (inboundMsg instanceof ResponseMsg){
                        ResponseMsg respMsg = (ResponseMsg) inboundMsg;
                        System.out.println("cmd = " + respMsg.getCmd());
                        if (respMsg.getCmd() == Command.AUTH_OK) {
                            login = respMsg.getLogin();
                            System.out.println("login: " + login);
                            ScreenManager.showMainScreen();
                        } else if (respMsg.getCmd() == Command.OK){
                            System.out.println("File successfully uploaded");
                        }
                    } else if (inboundMsg instanceof FileListMsg) {
                        FileListMsg fileListMsg = (FileListMsg) inboundMsg;
                        System.out.println("cmd = " + fileListMsg.getCmd());
                        System.out.println("FileList: " + fileListMsg.getFileList());
                    } else if (inboundMsg instanceof FileMsg) {
                        FileMsg fileMsg = (FileMsg) inboundMsg;
                        Path path = Paths.get("C:\\Temp\\", fileMsg.getFileName());
                        System.out.println("cmd = " + fileMsg.getCmd());
                        System.out.println("File: " + fileMsg.getFileName());
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

    }

    private void readClientProperties() {
        try (Reader in = new InputStreamReader(this.getClass().getResourceAsStream("/client.properties"))) {
            Properties properties = new Properties();
            properties.load(in);
            host = properties.getProperty("host");
            port = Integer.parseInt(properties.getProperty("port"));
            folder = Paths.get(properties.getProperty("folder"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
