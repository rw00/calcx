package com.rw.calcx;

public class CalcXUtils {
    private CalcXUtils() {
    }


    static char getLastChar(String s) {
        return getLastCharAtIndex(s, 0);
    }

    static int countBrackets(String s) {
        int open = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                open++;
            } else if (s.charAt(i) == ')') {
                open--;
            }
        }
        return open;
    }

    static boolean checkLastCharIsDigit(String s) {
        return !s.isEmpty() && Character.isDigit(getLastChar(s));
    }

    static boolean checkLastCharIsOperator(String s) {
        return !s.isEmpty() && isOperator(getLastChar(s));
    }

    static boolean chechBeforeLastIsOperator(String s) {
        return (s.length() > 1) && isOperator(getLastCharAtIndex(s, 1));
    }

    static boolean chechBeforeLastIsDigit(String s) {
        return (s.length() > 1) && Character.isDigit(getLastCharAtIndex(s, 1));
    }

    static char getLastCharAtIndex(String s, int i) { // skip spaces
        return (s.charAt(s.length() - 1 - i) == ' ') ? ((s.length() > i) ? getLastCharAtIndex(s, ++i) : s.charAt(s.length() - 1 - i)) : s.charAt(s.length() - 1 - i);
    }

    static boolean isOperator(char ch) {
        if (isSignOperator(ch) || (ch == CalcX.MULTIPLICATION_OPERATOR.charAt(0)) || (ch == CalcX.DIVISION_OPERATOR.charAt(0)) || (ch == CalcX.POWER_OPERATOR.charAt(0))) {
            return true;
        }
        return false;
    }

    static boolean isSignOperator(char ch) {
        return isMinusSign(ch) || (ch == CalcX.ADDITION_OPERATOR.charAt(0));
    }

    static boolean isMinusSign(char ch) {
        return (ch == CalcX.SUBTRACTION_OPERATOR.charAt(0)) || (ch == '-');
    }

    static String getValidInput(String input) {
        return input.replace(CalcX.SUBTRACTION_OPERATOR, "-");
    }
}
