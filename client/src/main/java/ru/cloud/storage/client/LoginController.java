package ru.cloud.storage.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ru.cloud.storage.common.AuthMsg;
import ru.cloud.storage.common.Passwords;

import java.io.IOException;

public class LoginController {
    @FXML
    PasswordField password;
    @FXML
    TextField login;

    public void btnSignInOnAction(ActionEvent actionEvent) throws IOException {

        if (login.getText().isEmpty() || password.getCharacters().length() == 0){
            //TODO ругаемся, что логин или пароль пустые
        }
        byte[] hash = Passwords.hash(password.getText().toCharArray(), Passwords.getNextSalt());
        AuthMsg authMsg = new AuthMsg(login.getText(), hash);
        Network.getInstance().sendMsg(authMsg);
    }

}
