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

    static boolean checkBeforeLastIsOperator(String s) {
        return (s.length() > 1) && isOperator(getLastCharAtIndex(s, 1));
    }

    static boolean checkBeforeLastIsDigit(String s) {
        return (s.length() > 1) && Character.isDigit(getLastCharAtIndex(s, 1));
    }

    static char getLastCharAtIndex(String s, int i) { // skip spaces
        if (s.charAt(s.length() - 1 - i) == ' ') {
            if (s.length() > i) {
                return getLastCharAtIndex(s, ++i);
            } else {
                return s.charAt(s.length() - 1 - i);
            }
        } else {
            return s.charAt(s.length() - 1 - i);
        }
    }

    static boolean isOperator(char ch) {
        return isSignOperator(ch) || (ch == CalcX.MULTIPLICATION_OPERATOR) || (ch == CalcX.DIVISION_OPERATOR) || (ch == CalcX.POWER_OPERATOR);
    }

    static boolean isSignOperator(char ch) {
        return isMinusSign(ch) || (ch == CalcX.ADDITION_OPERATOR);
    }

    static boolean isMinusSign(char ch) {
        return (ch == CalcX.SUBTRACTION_OPERATOR) || (ch == '-');
    }

    static String getValidInput(String input) {
        return input.replace(CalcX.SUBTRACTION_OPERATOR, '-');
    }
}
