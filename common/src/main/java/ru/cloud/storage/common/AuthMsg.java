package ru.cloud.storage.common;

import java.util.Arrays;

public class AuthMsg extends BaseMsg {
    private static final long serialVersionUID = 1755323896662253659L;

    private byte[] pwdHash;

    public AuthMsg(String login, byte[] pwdHash) {
        super(login);
        this.pwdHash = pwdHash.clone();
        Arrays.fill(pwdHash, Byte.MIN_VALUE);
        setCmd(Command.AUTH);
    }

    public byte[] getPassword() {
        return pwdHash;
    }
}
