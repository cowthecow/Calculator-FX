import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

public class Controller {

    //bugz
    //sin, cos, log, ln, nroot, npower don't even work
    //square root now unviable

    private Scene currentScene;
    private String mainText = "0";
    private String untrimmedText = "0";

    private ArrayList<Integer> replacedNegatives = new ArrayList<>();

    //Hex numbers
    public static String cssFile = "lightmode.css";

    @FXML
    TextField textField;

    @FXML
    ColorPicker colorPickerBg;

    @FXML
    ColorPicker colorPickerHue;

    @FXML
    Button someScientificNode;

    @FXML
    Button someStandardNode;

    @FXML
    Button someAboutUsNode;

    @FXML
    BorderPane borderPaneScientific;

    @FXML
    BorderPane borderPaneStandard;

    @FXML
    BorderPane borderPanePreferences;

    @FXML
    private void backgroundChanged() {
//        Color background = colorPickerBg.getValue();
//        base = String.format("#%02x%02x%02x", (int) (background.getRed() * 255), (int) (background.getGreen() * 255), (int) (background.getBlue() * 255));
//        borderPanePreferences.setStyle(getCSS());
    }

    @FXML
    private void hueChanged() {
//        Color hue = colorPickerHue.getValue();
//        accent = String.format("#%02x%02x%02x", (int) (hue.getRed() * 255), (int) (hue.getGreen() * 255), (int) (hue.getBlue() * 255));
//        borderPanePreferences.setStyle(getCSS());
    }

    @FXML
    private void themeToggled(ActionEvent e) throws IOException {
        if(e.getSource().toString().contains("light")) cssFile = "lightmode.css";
        if(e.getSource().toString().contains("dark")) cssFile = "darkmode.css";
        if(e.getSource().toString().contains("grey")) cssFile = "greymode.css";
        if(e.getSource().toString().contains("black")) cssFile = "blackmode.css";

        Stage stage = (Stage) (someAboutUsNode).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("aboutUs.fxml"));

        currentScene = new Scene(root, 460, 575);
        currentScene.getStylesheets().clear();
        currentScene.getStylesheets().add(cssFile);
        stage.setTitle("QuickMaths V1.5");
        stage.setScene(currentScene);
    }


    @FXML
    private void onEqualsClicked(ActionEvent e) {

        try {
            System.out.println("Infix: " + untrimmedText);
            BigDecimal result = Mechanics.evaluatePostfix(Mechanics.infixToPostfix(Mechanics.replaceAllNegatives(untrimmedText))).round(new MathContext(24));


            mainText = trim(result.toPlainString(), 24);
            untrimmedText = trim(result.toPlainString(), 24);
        }catch (StackOverflowError error) {
            mainText = "Overflow";
            untrimmedText = "Overflow";
        }

        textField.setText(mainText);
        textField.appendText("");
    }

    //Efficient algorithm

    private String trim(String str, int length) {
        if(str.length() >= 100000) return "Overflow";
        if (str.length() > length) {
            if(str.contains(".")) {
                return str.substring(0, length);
            }else {
                return new StringBuilder(str).insert(1, ".").substring(0, length+1) + "E" + str.length();
            }
        }
        return str;
    }

    //SCENE TRANSITION METHODS
    @FXML
    private void turnOnBasic(ActionEvent e) throws IOException {
        Stage stage;

        try {
            stage = (Stage) (someScientificNode).getScene().getWindow();
        } catch (RuntimeException ignore) {
            stage = (Stage) (someAboutUsNode).getScene().getWindow();
        }

        Parent root = FXMLLoader.load(getClass().getResource("simple.fxml"));
        currentScene = new Scene(root, 460, 575);
        currentScene.getStylesheets().clear();
        currentScene.getStylesheets().add(cssFile);

        stage.setTitle("QuickMaths V1.5 STANDARD");
        stage.setScene(currentScene);

    }

    @FXML
    private void turnOnScientific(ActionEvent e) throws IOException {
        Stage stage;

        try {
            stage = (Stage) (someStandardNode).getScene().getWindow();
        } catch (RuntimeException ignore) {
            stage = (Stage) (someAboutUsNode).getScene().getWindow();
        }

        Parent root = FXMLLoader.load(getClass().getResource("scientific.fxml"));
        currentScene = new Scene(root, 460, 575);
        currentScene.getStylesheets().clear();
        currentScene.getStylesheets().add(cssFile);

        stage.setTitle("QuickMaths V1.5 SCIENTIFIC");
        stage.setScene(currentScene);
    }

    @FXML
    private void turnOnAboutUs(ActionEvent e) throws IOException {
        Stage stage;

        try {
            stage = (Stage) (someStandardNode).getScene().getWindow();
        } catch (RuntimeException ignore) {
            stage = (Stage) (someScientificNode).getScene().getWindow();
        }

        Parent root = FXMLLoader.load(getClass().getResource("aboutUs.fxml"));
        currentScene = new Scene(root, 460, 575);
        currentScene.getStylesheets().clear();
        currentScene.getStylesheets().add(cssFile);

        stage.setTitle("QuickMaths V1.5");
        stage.setScene(currentScene);
    }

    //USER INPUT HANDLING
    @FXML
    private void onKeyPressed(KeyEvent e) {
        if (e.getCode().toString().contains("DIGIT")) {
            if (mainText.equals("0")) mainText = "";
            if (untrimmedText.equals("0")) untrimmedText = "";

            mainText += e.getCode().toString().substring(5, 6);
            untrimmedText += e.getCode().toString().substring(5, 6);
        }
        if (e.getCode().toString().equals("BACK_SPACE")) {
            if (mainText.length() == 1) {
                mainText = "0";
            } else {
                mainText = mainText.substring(0, mainText.length() - 1);
            }
        }
        textField.setText(mainText);
    }

    @FXML
    private void onNumberClicked(ActionEvent e) {
        char previousChar = mainText.charAt(mainText.length() - 1);

        if (previousChar == ')' || previousChar == 'e' || previousChar == 'i') return;
        if (mainText.equals("0")) mainText = "";

        if(e.getSource().toString().contains("\uD835\uDCEE")) {
            mainText += "e";
            untrimmedText += "e";
        }else if(e.getSource().toString().contains("\uD835\uDF45")) {
            mainText += "pi";
            untrimmedText += "pi";
        }else {
            mainText += e.getSource().toString().charAt(e.getSource().toString().length() - 2);
            untrimmedText += e.getSource().toString().charAt(e.getSource().toString().length() - 2);

        }

        textField.setText(mainText);
    }

    @FXML
    private void onOperatorClicked(ActionEvent e) {
        if (mainText.length() == 0) return;
        //Anything that counts as a number, including superscript
        if (Character.isDigit(mainText.charAt(mainText.length() - 1)) || mainText.charAt(mainText.length() - 1) == '²' || mainText.charAt(mainText.length() - 1) == '!' || mainText.charAt(mainText.length() - 1) == ')' || mainText.charAt(mainText.length() - 1) == 'e' || mainText.charAt(mainText.length() - 1) == 'i') {
            if (e.getSource().toString().contains("mod")) {
                mainText += "mod";
                untrimmedText += "mod";
            }else if (e.getSource().toString().contains("log")) {
                mainText += "log";
                untrimmedText += "log";
            }else {
                mainText += e.getSource().toString().charAt(e.getSource().toString().length() - 2);
                untrimmedText += e.getSource().toString().charAt(e.getSource().toString().length() - 2);
            }

            textField.setText(mainText);
        }
    }

    //− - -
    //Advanced operators include squares, square roots, and inverse function
    @FXML
    private void onAdvancedOperatorClicked(ActionEvent e) {
        String input = e.getSource().toString();

        if (mainText.length() == 0) return;
        if (!Character.isDigit(mainText.charAt(mainText.length() - 1))) return;

        if (input.contains("power")) {
            mainText += "²";
            untrimmedText += "²";
        } else if(input.contains("factorial")) {
            mainText += "!";
            untrimmedText += "!";
        }else if (input.contains("root") || input.contains("inverse")) {
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
            untrimmedText = inserter.toString();


        } else if (input.contains("negate")) {
            //Even newer algorithm
            //Don't use brackets
            //Before the infix postfix, scan every bracket:
            //If there is something like +-, replace - with another symbol detonating negation
            //If there is an operator, bracket, or nothing before it replace it

            //New algorithm for negate
            //Go from the start of the list and find the last operator
            //If you find an addition, multiplication, ..., go place down negative symbol
            //If you find a subtraction,
            //If there is a number behind the symbol, place down negate
            //Else, if there is an operator behind it, delete the symbol

            int location = 0;
            boolean insertOrDelete = true;

            for(int i = 0; i < mainText.length(); i++) {
                if(isOperator(mainText.charAt(i))) {
                    if(mainText.charAt(i) == '-') {
                        try {
                            if(isOperator(mainText.charAt(i-1))) {
                                //If this is an operator, this means that this negative sign is a negate not a subtract
                                insertOrDelete = false;
                                location = i;
                            }else {
                                insertOrDelete = true;
                                location = i+1;
                            }
                        }catch (StringIndexOutOfBoundsException outOfBounds) {
                            System.out.println("sus.");
                            insertOrDelete = false;
                            location = 0;
                        }
                    }else {
                        insertOrDelete = true;
                        location = i+1;
                    }
                }
            }

            if(insertOrDelete) {
                mainText = new StringBuilder(mainText).insert(location,'-').toString();
            }else {
                mainText = new StringBuilder(mainText).deleteCharAt(location).toString();
            }
            untrimmedText = mainText;
        }

        textField.setText(mainText);
    }

    @FXML
    private void onBackspaceClicked(ActionEvent e) {
        if (e.getSource().toString().contains("clear")) {
            mainText = "0";
            untrimmedText = "0";
            Mechanics.eraseLastOperation();
        } else if (e.getSource().toString().contains("del")) {
            if (mainText.length() == 1) {
                mainText = "0";
                untrimmedText = "0";
            } else if (mainText.length() > 1) {
                mainText = mainText.substring(0, mainText.length() - 1);
                untrimmedText = mainText.substring(0, mainText.length() - 1);
            }
        }
        textField.setText(mainText);
    }

    @FXML
    private void onBracketClicked(ActionEvent e) {
        if (e.getSource().toString().contains("(")) {
            if (mainText.equals("0")) {
                mainText = "(";
                untrimmedText += "(";
            } else {
                if (isOperator(mainText.charAt(mainText.length() - 1))) {
                    mainText += "(";
                    untrimmedText += "(";
                }
            }
        }
        if (e.getSource().toString().contains(")")) {
            if (!validRightBracket(mainText)) return;
            if (mainText.equals("0")) {
                mainText = "(";
            } else {
                if (!isOperator(mainText.charAt(mainText.length() - 1))) {
                    mainText += ")";
                    untrimmedText += ")";
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
