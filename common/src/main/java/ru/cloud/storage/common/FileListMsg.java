package ru.cloud.storage.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileListMsg extends BaseMsg {
    private static final long serialVersionUID = -4761828698326029877L;
    private List<String> fileList;

    public FileListMsg(String login, Command cmd) {
        super(login);
        setCmd(cmd);
    }

    public List<String> getFileList() {
        return fileList;
    }

    public void setFileList(String userFolder) throws IOException {
        //первый элемент пропустим, т.к. это каталог пользователя
        this.fileList = Files.walk(Paths.get(userFolder)).skip(1).map(p->p.toString()).collect(Collectors.toList());
    }
}
