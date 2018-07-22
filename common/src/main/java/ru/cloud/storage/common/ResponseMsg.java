package ru.cloud.storage.common;

public class ResponseMsg extends BaseMsg {

    private static final long serialVersionUID = -3002945146284731796L;

    public ResponseMsg(String login, Command cmd) {
        super(login);
        setCmd(cmd);
    }
}
