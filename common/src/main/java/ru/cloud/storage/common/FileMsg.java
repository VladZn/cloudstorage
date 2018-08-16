package ru.cloud.storage.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.CRC32;

public class FileMsg extends BaseMsg {
    private static final long serialVersionUID = -2144504239439537593L;

    private String fileName;
    private long fileSize;
    private byte[] fileBinary;
    private long crc32;

    public FileMsg(String login, Path path, Command cmd) throws IOException {
        super(login);
        setFileName(path.getFileName().toString());
        setFileBinary(path);
        setCmd(cmd);
    }

    public FileMsg(String login, String fileName, Command cmd) {
        super(login);
        setFileName(fileName);
        setCmd(cmd);
    }

    public String getFileName() {
        return fileName;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileBinary() {
        return fileBinary;
    }

    public void setFileBinary(Path path) throws IOException {
        this.fileBinary = Files.readAllBytes(path);
        setCRC32();
    }

    private void setCRC32() {
        CRC32 CRC = new CRC32();
        CRC.update(this.getFileBinary());
        this.crc32 = CRC.getValue();
    }

}
