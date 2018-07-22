package ru.cloud.storage.common;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResponseProtocol implements Serializable {

    private static final long serialVersionUID = -4688751276926464222L;

    public static final int ANS_OK = 100;
    public static final int ANS_NO_SUCH_FILE = -1;

    private String login;
    private String fileName;
    private String filePath;
    private long fileSize;
    private byte[] file;
    private int answer;

    public ResponseProtocol(String login, String fileName, String filePath) {
        this.login = login;
        this.fileName = fileName;
        this.filePath = filePath;
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
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }
}
