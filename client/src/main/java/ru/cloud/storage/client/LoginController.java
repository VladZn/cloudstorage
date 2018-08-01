package ru.cloud.storage.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ru.cloud.storage.common.AuthMsg;
import ru.cloud.storage.common.Passwords;

public class LoginController {
    @FXML
    PasswordField password;
    @FXML
    TextField login;

    public void btnSignInOnAction(ActionEvent actionEvent) throws Exception {

        if (login.getText().isEmpty() || password.getCharacters().length() == 0){
            //TODO ругаемся, что логин или пароль пустые
        }
        //byte[] hash = Passwords.hash(password.getText().toCharArray(), Passwords.getNextSalt());
        String hash = Passwords.getSaltedHash(password.getText().toCharArray());
        System.out.println("hash " + hash);

        byte[] pwdBytes = password.getText().getBytes();
        AuthMsg authMsg = new AuthMsg(login.getText(), Passwords.encrypt(pwdBytes));
        Network.getInstance().sendMsg(authMsg);
    }

}
