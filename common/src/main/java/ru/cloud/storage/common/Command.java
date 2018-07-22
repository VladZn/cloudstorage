package ru.cloud.storage.common;

//TODO может разбить на два перечисления с командами и кодами ответов?
public enum Command {
    AUTH(-10),
    AUTH_OK(-11),
    AUTH_FAILED(-12),

    GET_FILELIST(-2),
    PUT_FILE(-3),
    GET_FILE(-4),
    RENAME_FILE(-5),
    DELETE_FILE(-6),

    NO_SUCH_FILE(-7),
    OK(-100);

    int cmdCode;
    Command(int i) {
        this.cmdCode = i;
    }
}
