package com.rw.calcx.calculator;

public class CalcUtil {
    private CalcUtil() {
    }

    public static boolean isSign(String token) {
        return isOneChar(token) && ((token.charAt(0) == '+') || (token.charAt(0) == '-'));
    }

    public static boolean isHighPrecedenceOperator(String token) {
        return isOneChar(token) && ((token.charAt(0) == '*') || (token.charAt(0) == '/'));
    }

    public static boolean areSameLevelOperators(String operator1, String operator2) {
        return (isSign(operator1) && isSign(operator2)) || (isHighPrecedenceOperator(operator1) && isHighPrecedenceOperator(operator2));
    }

    public static boolean isOperator(String c) {
        return isOneChar(c) && (isSign(c) || isHighPrecedenceOperator(c));
    }

    public static char getLastNextChar(String s, int i) {
        if ((getLastCharFromIndex(s, i) == ' ') && (s.length() > i)) {
            return getLastNextChar(s, ++i);
        }
        return getLastCharFromIndex(s, i);
    }

    public static long factorial(int num) {
        long result = 1;
        for (int i = 1; i <= num; i++) {
            result = result * i;
        }
        return result;
    }

    public static char getLastCharFromIndex(String s, int i) {
        return s.charAt(s.length() - 1 - i);
    }

    private static boolean isOneChar(String c) {
        return c.length() == 1;
    }
}
