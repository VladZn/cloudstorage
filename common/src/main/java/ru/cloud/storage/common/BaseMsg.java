package ru.cloud.storage.common;

import java.io.Serializable;

public abstract class BaseMsg implements Serializable {
    private static final long serialVersionUID = 2342727031044370181L;

    private String login;
    private int userId;
    private Command cmd;

    public BaseMsg(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Command getCmd() {
        return cmd;
    }

    public void setCmd(Command cmd) {
        this.cmd = cmd;
    }
}
