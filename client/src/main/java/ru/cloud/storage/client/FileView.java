package ru.cloud.storage.client;

import java.io.File;

public class FileView {
    private File file;
    private String name;
    private String fullName;
    private long size;

    public FileView(File file) {
        this.file = file;
        this.name = file.getName();
        this.fullName = file.getAbsolutePath();
        this.size = file.length();
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }
}
