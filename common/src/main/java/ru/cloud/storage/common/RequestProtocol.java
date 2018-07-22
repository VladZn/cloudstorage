package ru.cloud.storage.common;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.CRC32;

public class RequestProtocol implements Serializable {

    private static final long serialVersionUID = -7613330353386033143L;

    private String login;
    private String fileName;
    private String filePath;
    private long fileSize;
    private byte[] file;
    private long crc32;
    private Command cmd;

    public RequestProtocol(String login, String fileName, Command cmd) {
        this.login = login;
        this.fileName = fileName;
        this.cmd = cmd;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(Path path) throws IOException {
        this.file = Files.readAllBytes(path);
        setCRC32();
    }

    public long getCrc32() {
        return crc32;
    }

    public void setCRC32() {
        CRC32 CRC = new CRC32();
        CRC.update(this.getFile());
        this.crc32 = CRC.getValue();
    }

    public Command getCmd() {
        return cmd;
    }

    public void setCmd(Command cmd) {
        this.cmd = cmd;
    }
}
