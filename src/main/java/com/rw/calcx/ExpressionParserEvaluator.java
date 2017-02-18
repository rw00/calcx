package com.rw.calcx;

public class ExpressionParserEvaluator {
    private static String expression = ""; // the given math expression to be evaluated
    private static int index = -1; // the current index in the expression
    private static int character = -1; // the character at index

    public ExpressionParserEvaluator() {
    }

    public static double evaluate(String expression) {
        ExpressionParserEvaluator.expression = expression;
        index = -1;
        character = -1;
        return parse();
    }

    private static void getChar() {
        character = (++index < expression.length()) ? expression.charAt(index) : -1;
    }

    private static void eatSpace() {
        while (Character.isWhitespace(character)) {
            getChar();
        }
    }

    private static double parse() {
        getChar();
        double value = parseExpression();
        if (character != -1) {
            throw new RuntimeException("Error; unexpected: " + (char) character);
        }
        return value;
    }

    // Grammar:
    // expression = term | expression `+` term | expression `-` term
    // term = factor | term `*` factor | term `/` factor | term brackets
    // factor = brackets | number | factor `^` factor
    // brackets = `(` expression `)`

    private static double parseExpression() {
        double value = parseTerm();
        while (true) {
            eatSpace();
            if (character == '+') { // addition
                getChar();
                value += parseTerm();
            } else if (character == '-') { // subtraction
                getChar();
                value -= parseTerm();
            } else {
                return value;
            }
        }
    }

    private static double parseTerm() {
        double value = parseFactor();
        while (true) {
            eatSpace();
            if (character == '/') { // division
                getChar();
                value /= parseFactor();
            } else if ((character == '*') || (character == '(')) { // multiplication
                if (character == '*') {
                    getChar();
                }
                value *= parseFactor();
            } else {
                return value;
            }
        }
    }

    private static double parseFactor() {
        double value;
        boolean negate = false;
        eatSpace();
        if (character == '(') { // brackets
            getChar();
            value = parseExpression();
            if (character == ')') {
                getChar();
            }
        } else { // numbers
            if ((character == '+') || (character == '-')) { // unary plus & minus
                negate = (character == '-');
                getChar();
                eatSpace();
            }
            StringBuilder strBuild = new StringBuilder();
            while (((character >= '0') && (character <= '9')) || (character == '.')) {
                strBuild.append((char) character);
                getChar();
            }
            if (strBuild.length() == 0) {
                throw new RuntimeException("Error; unexpected: " + (char) character);
            }
            value = Double.parseDouble(strBuild.toString());
        }
        eatSpace();
        if (character == '^') { // exponentiation
            getChar();
            value = Math.pow(value, parseFactor());
        }
        if (negate) { // exponentiation has higher priority than unary minus: -2^3=-8
            value = -value;
        }
        return value;
    }
}
