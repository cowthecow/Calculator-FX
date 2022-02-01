package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    TextField textField;

    @FXML
    Button button1;
    @FXML
    Button button2;
    @FXML
    Button button3;
    @FXML
    Button button4;
    @FXML
    Button button5;
    @FXML
    Button button6;
    @FXML
    Button button7;
    @FXML
    Button button8;
    @FXML
    Button button9;
    @FXML
    Button button0;

    @FXML
    Button remove;
    @FXML
    Button enter;

    @FXML
    private void onButtonClicked(ActionEvent e) {
        if (e.getSource().equals(button0)) {
            textField.appendText("0");
        }
        if (e.getSource().equals(button1)) {
            textField.appendText("1");
        }
        if (e.getSource().equals(button2)) {
            textField.appendText("2");
        }
        if (e.getSource().equals(button3)) {
            textField.appendText("3");
        }
        if (e.getSource().equals(button4)) {
            textField.appendText("4");
        }
        if (e.getSource().equals(button5)) {
            textField.appendText("5");
        }
        if (e.getSource().equals(button6)) {
            textField.appendText("6");
        }
        if (e.getSource().equals(button7)) {
            textField.appendText("7");
        }
        if (e.getSource().equals(button8)) {
            textField.appendText("8");
        }
        if (e.getSource().equals(button9)) {
            textField.appendText("9");
        }
        if (e.getSource().equals(remove)) {
            if(textField.getText().length()>0)
                textField.setText(textField.getText().substring(0, textField.getText().length()-1));
        }
    }
}
