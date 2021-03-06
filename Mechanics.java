import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Stack;

public class Mechanics {

    //2+3*5 > 2 3 5 * +
    //2*3*5 > 2 3 5 * *
    //2*3/5 > 2 3 * 5 /
    //2*3+5 > 2 3 * 5 +
    //(2+3)*5 > 2 3 + 5 *
    //2*(3+5) > 2 3 5 + *
    //2+3/5 > 2 3 5 / +

    //Algorithm
    //If you see a number append it to postfix
    //If you see a left bracket push it to stack
    //If you see right bracket pop everything on the stack until you see the left bracket which is discarded
    //If you see an operator push it to stack

    //If your operator has higher or equal precedence then the one on top of the stack, push it into the stack
    //If your operator has lower precedence then the one on top of the stack, pop everything on the stack until you get something with lower precedence than the current operator


    //Algorithm
    //If you see a number, push it onto the stack
    //If you see an operator
    //If its something like square root, square, or factorial, pop a number from the stack, perform the operation, and push it down
    //If its something like division, subtraction, or modulus, pop 2 numbers and use the second number popped to perform on the first


    private static String lastOperator;
    private static String lastNumber;

    public static String lastOperation() {
        try {
            return " " + (isSingleOperation(lastOperator.charAt(0)) ? "" : lastNumber) + lastOperator;
        }catch (NullPointerException e) {
            return " " + lastOperator;
        }
    }

    public static void eraseLastOperation() {
        lastNumber = "";
        lastOperator = "";
    }

    public static boolean isSingleOperation(char c) {
        return c == '√' || c == '²' || c == 's' || c == 'c' || c == '!';
    }

    public static boolean isDoubleOperation(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == 'l';
    }

    public static String replaceAllNegatives(String s) {
        StringBuilder newString = new StringBuilder();
        boolean wait = false;

        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if(isDoubleOperation(c) && wait) {
                wait = false;
                newString.append("~");
            }

            if(c == '-') {
                try {
                    if(isDoubleOperation(s.charAt(i-1)) || s.charAt(i-1) == '(') {
                        wait = true;
                    }else {
                        newString.append(c);
                    }
                }catch (StringIndexOutOfBoundsException e) {
                    wait = true;
                }
            }else {
                newString.append(c);
            }
        }

        if(wait) newString.append("~");

        return newString.toString();
    }

    public static String infixToPostfix(String infix) throws RuntimeException {
        System.out.println("Negative Removed Infix: " + infix);
        boolean operatorExisting = false;

        //Edit the infix so it replaces certain characters and expressions
        infix = infix.replaceAll("mod", "%");
        infix = infix.replaceAll("log", "l");
        infix = infix.replaceAll("sin", "s");
        infix = infix.replaceAll("cos", "c");
        infix = infix.replaceAll("e", "2.718281828459045235360287");
        infix = infix.replaceAll("pi", "3.141592653589793238462643");
        infix = infix.replaceAll("\\(-", "(0-");

        StringBuilder postfix = new StringBuilder();
        Stack<Character> operators = new Stack<>();
        char savedOperator = ' ';

        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);
            if (Character.isDigit(c) || c == '.' || c == 'E' || c == '~') {
                postfix.append(c);
            } else if (isSingleOperation(c)) {
                lastOperator = String.valueOf(c);
                operatorExisting = true;
                if (c == '√') {
                    savedOperator = c;
                } else {
                    postfix.append(c);
                }
            } else if (isDoubleOperation(c)) {
                lastOperator = String.valueOf(c);
                operatorExisting = true;
                if (savedOperator != ' ') {
                    postfix.append(savedOperator);
                    savedOperator = ' ';
                }
                postfix.append(" ");
                if (operators.size() == 0) {
                    operators.push(c);
                } else {
                    if (getOperatorPrecedence(operators.peek()) > getOperatorPrecedence(c)) {
                        while (getOperatorPrecedence(operators.peek()) > getOperatorPrecedence(c)) {
                            postfix.append(operators.pop());
                        }
                        operators.push(c);
                    } else {
                        operators.push(c);
                    }
                }
            } else if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                while (operators.peek() != '(') {
                    postfix.append(operators.pop());
                }
                operators.pop();
            }
        }
        if (savedOperator != ' ') postfix.append(savedOperator);

        while (operators.size() > 0) {
            postfix.append(operators.pop());
        }

        return operatorExisting ? postfix.toString() : infix + lastOperation();
    }

    public static BigDecimal evaluatePostfix(String postfix) throws RuntimeException, StackOverflowError{
        System.out.println("Negative Removed Postfix: " + postfix);

        Stack<BigDecimal> numbers = new Stack<>();

        for (int i = 0; i < postfix.length(); i++) {
            char c = postfix.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                StringBuilder number = new StringBuilder(c);
                StringBuilder multiplier = new StringBuilder("0");
                boolean appendingToMultiplier = false;

                while (true) {
                    if(c != 'E') {
                        if (appendingToMultiplier) multiplier.append(c);
                        else number.append(c);
                    }

                    i++;
                    c = postfix.charAt(i);
                    if (!(Character.isDigit(c) || c == '.' || c == 'E')) {
                        break;
                    }
                    if(c == 'E') appendingToMultiplier = true;
                }

                int intMultiplier = Integer.parseInt(multiplier.toString());
                BigDecimal multiplierValue = (BigDecimal.TEN.pow(intMultiplier == 0 ? 0 : intMultiplier-1));
                BigDecimal value = new BigDecimal(number.toString()).multiply(multiplierValue);

                lastNumber = value.toString();

                numbers.push(value);

                //If you don't put i-- you're fucked up
                i--;
            } else if(c == '~') {
                BigDecimal a = numbers.pop();
                a = a.round(new MathContext(24));
                numbers.push(a.multiply(new BigDecimal(-1)));
            }else if (isSingleOperation(c)) {
                BigDecimal a = numbers.pop();
                a = a.round(new MathContext(24));

                switch (c) {
                    case '²':
                        numbers.push(a.multiply(a));
                        break;
                    case '√':
                        numbers.push(BigDecimal.valueOf(Math.sqrt(a.intValue())));
                        break;
                    case '!':
                        numbers.push(bigDecimalFactorial(a));
                        break;
                }

            } else if (isDoubleOperation(c)) {
                BigDecimal a = numbers.pop();
                BigDecimal b = numbers.pop();

                a = a.round(new MathContext(24));
                b = b.round(new MathContext(24));

                switch (c) {
                    case '+':
                        numbers.push(b.add(a));
                        break;
                    case '-':
                        numbers.push(b.subtract(a));
                        break;
                    case '*':
                        numbers.push(b.multiply(a));
                        break;
                    case '/':
                        numbers.push(b.divide(a, new MathContext(24)));
                        break;
                    case '%':
                        numbers.push(b.remainder(a));
                        break;
                    default:
                        throw new ArithmeticException("Operator not allowed");
                }

                //% means modulus here not percent
            }
        }
        //6 7 8 354 /*8√-+ -> 6 0.15 8sqrt-+ >


        return numbers.peek().round(new MathContext(24));
    }

    public static BigDecimal bigDecimalFactorial(BigDecimal sus) throws StackOverflowError {
        if (sus.equals(BigDecimal.ZERO)) return BigDecimal.ZERO;
        if (sus.compareTo(BigDecimal.ONE) <= 0) return BigDecimal.ONE;

        return sus.multiply(bigDecimalFactorial(sus.subtract(BigDecimal.ONE)));
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
        System.out.println(replaceAllNegatives("-55+-66-9/-33"));
    }

}
