package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {

    private String mainText = "0";

    @FXML
    TextField textField;

    @FXML
    Button someScientificNode;

    @FXML
    Button someStandardNode;

    @FXML
    Button someAboutUsNode;

    @FXML
    private void turnOnBasic(ActionEvent e) throws IOException {
        Stage stage;

        try {
            stage = (Stage) (someScientificNode).getScene().getWindow();
        }catch (RuntimeException ignore) {
            stage = (Stage) (someAboutUsNode).getScene().getWindow();
        }

        Parent root = FXMLLoader.load(getClass().getResource("simple.fxml"));
        stage.setTitle("QuickMaths V1.3 SIMPLE");
        stage.setScene(new Scene(root, 420, 585));
    }

    @FXML
    private void turnOnScientific(ActionEvent e) throws IOException {
        Stage stage;

        try {
            stage = (Stage) (someStandardNode).getScene().getWindow();
        }catch (RuntimeException ignore) {
            stage = (Stage) (someAboutUsNode).getScene().getWindow();
        }

        Parent root = FXMLLoader.load(getClass().getResource("scientific.fxml"));
        stage.setTitle("QuickMaths V1.3 SCIENTIFIC");
        stage.setScene(new Scene(root, 420, 585));
    }

    @FXML
    private void turnOnAboutUs(ActionEvent e) throws IOException {
        Stage stage;

        try {
            stage = (Stage) (someStandardNode).getScene().getWindow();
        }catch (RuntimeException ignore) {
            stage = (Stage) (someScientificNode).getScene().getWindow();
        }

        Parent root = FXMLLoader.load(getClass().getResource("aboutUs.fxml"));
        stage.setTitle("QuickMaths V1.3");
        stage.setScene(new Scene(root, 420, 585));
    }

    @FXML
    private void onKeyPressed(KeyEvent e) {
        if (e.getCode().toString().contains("DIGIT")) {
            if (mainText.equals("0")) mainText = "";
            mainText += e.getCode().toString().substring(5, 6);
        }
        if (e.getCode().toString().equals("BACK_SPACE")) {
            if (mainText.length() == 1) {
                mainText = "0";
            } else {
                mainText = mainText.substring(0, mainText.length() - 1);
            }
        } else {
            System.out.println(e.getCode().toString());
        }
        textField.setText(mainText);
    }

    @FXML
    private void onNumberClicked(ActionEvent e) {
        if((mainText.charAt(mainText.length()-1)) == ')') return;

        if (mainText.equals("0")) mainText = "";
        mainText += e.getSource().toString().charAt(e.getSource().toString().length() - 2);
        textField.setText(mainText);
    }

    @FXML
    private void onOperatorClicked(ActionEvent e) {
        if (mainText.length() == 0) return;
        //Anything that counts as a number, including superscript
        if (Character.isDigit(mainText.charAt(mainText.length() - 1)) || mainText.charAt(mainText.length() - 1) == '²' || mainText.charAt(mainText.length() - 1) == ')') {
            mainText += e.getSource().toString().charAt(e.getSource().toString().length() - 2);
            textField.setText(mainText);
        }
    }

    //Advanced operators include squares, square roots, and inverse function
    @FXML
    private void onAdvancedOperatorClicked(ActionEvent e) {
        String input = e.getSource().toString();

        if (mainText.length() == 0) return;
        if (!Character.isDigit(mainText.charAt(mainText.length() - 1))) return;

        if (input.contains("power")) {
            mainText += "²";
        } else if (input.contains("root") || input.contains("inverse")) {
            int lastOperator = 0;
            for (int i = 0; i < mainText.length(); i++) {
                if (isOperator(mainText.charAt(i))) lastOperator = i;
            }
            StringBuilder inserter = new StringBuilder(mainText);

            if (input.contains("root")) {
                inserter.insert(lastOperator == 0 ? 0 : lastOperator + 1, '√');
            } else {
                inserter.insert(lastOperator == 0 ? 0 : lastOperator + 1, "1/");
            }
            mainText = inserter.toString();
        } else if (input.contains("negate")) {
            int lastOperator = 0;
            for (int i = 0; i < mainText.length(); i++) {
                if (isOperator(mainText.charAt(i))) lastOperator = i;
            }

            int insertLocation = lastOperator == 0 ? 0 : lastOperator + 1;
            StringBuilder inserter = new StringBuilder(mainText);
            inserter.insert(insertLocation, "-");
            mainText = inserter.toString();

        }

        textField.setText(mainText);
    }

    @FXML
    private void onBackspaceClicked(ActionEvent e) {
        if (e.getSource().toString().contains("clear")) {
            mainText = "0";
        } else if (e.getSource().toString().contains("del")) {
            if (mainText.length() == 1) {
                mainText = "0";
            } else if (mainText.length() > 1) {
                mainText = mainText.substring(0, mainText.length() - 1);
            }
        }
        textField.setText(mainText);
    }

    @FXML
    private void onBracketClicked(ActionEvent e) {
        if (e.getSource().toString().contains("(")) {
            if (mainText.equals("0")) {
                mainText = "(";
            } else {
                if (isOperator(mainText.charAt(mainText.length() - 1))) {
                    mainText += "(";
                }
            }
        }
        if (e.getSource().toString().contains(")")) {
            if(!validRightBracket(mainText)) return;
            if (mainText.equals("0")) {
                mainText = "(";
            } else {
                if (!isOperator(mainText.charAt(mainText.length() - 1))) {
                    mainText += ")";
                }
            }
        }
        textField.setText(mainText);
    }

    private boolean validRightBracket(String s) {
        int lbrackets = 0, rbrackets = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') lbrackets++;
            if (s.charAt(i) == ')') rbrackets++;
        }
        return (lbrackets > rbrackets);
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(';
    }
}
