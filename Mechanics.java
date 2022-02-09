package sample;

import java.util.Scanner;
import java.util.Stack;

public class Mechanics {

    //2+3*5 > 2 3 5 * +
    //2*3*5 > 2 3 5 * *
    //2*3/5 > 2 3 * 5 /
    //2*3+5 > 2 3 * 5 +
    //(2+3)*5 > 2 3 + 5 *
    //2*(3+5) > 2 3 5 + *

    //Algorithm
    //If you see a number append it to postfix
    //If you see a left bracket push it to stack
    //If you see right bracket pop everything on the stack until you see the left bracket which is discarded
    //If you see an operator push it to stack

    //If your operator has higher or equal precedence then the one on top of the stack, push it into the stack
    //If your operator has lower precedence then the one on top of the stack, pop everything on the stack until you get something with lower precedence than the current operator

    public static boolean isSingleOperation(char c) {
        return c == '√' || c == '²';
    }

    public static boolean isDoubleOperation(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    public static String infixToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> operators = new Stack<>();
        char savedOperator = ' ';

        for(int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);

            if(Character.isDigit(c) || c == '.') {
                postfix.append(c);
            }else if(isSingleOperation(c)) {
                if(c == '√') {
                    savedOperator = c;
                }else {
                    postfix.append(c);
                }
            }else if(isDoubleOperation(c)) {
                postfix.append(savedOperator);
                postfix.append(" ");
                if(operators.size() == 0) {
                    operators.push(c);
                }else {
                    if(getOperatorPrecedence(operators.peek()) > getOperatorPrecedence(c)) {
                        while(getOperatorPrecedence(operators.peek()) > getOperatorPrecedence(c)) {
                            postfix.append(operators.pop());
                        }
                    }else {
                        operators.push(c);
                    }
                }
            }else if(c == '(') {
                operators.push(c);
            }else if(c == ')') {
                while(operators.peek() != '(') {
                    postfix.append(operators.pop());
                }
                operators.pop();
            }
        }
        while(operators.size()>0) {
            postfix.append(operators.pop());
        }
        return postfix.toString();
    }

    public static int getOperatorPrecedence(char c) {
        switch (c) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
        }
        return -1;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String infix = scanner.nextLine();
        System.out.println(infixToPostfix(infix));
    }

}
