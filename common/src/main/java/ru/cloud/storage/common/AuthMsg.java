package ru.cloud.storage.common;

import java.util.Arrays;

public class AuthMsg extends BaseMsg {
    private static final long serialVersionUID = 1755323896662253659L;

    private byte[] pswd;

    public AuthMsg(String login, byte[] pwdBytes) {
        super(login);
        this.pswd = pwdBytes.clone();
        Arrays.fill(pwdBytes, Byte.MIN_VALUE);
        setCmd(Command.AUTH);
    }

    public byte[] getPassword() {
        return pswd;
    }
}
