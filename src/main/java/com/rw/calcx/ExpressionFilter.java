package com.rw.calcx;

import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;

import java.util.function.UnaryOperator;

public class ExpressionFilter implements UnaryOperator<TextFormatter.Change> {
    public static final String ACCEPTABLE_DATA = "(Error|\\d|\\s|\\.|\\+|\\-|\\*|/|\\^|\\(|\\))+";
    private final CalcX calcx;

    public ExpressionFilter(CalcX calcx) {
        this.calcx = calcx; // 2* 2-3
    }

    public static String insertString(String s, int index, String insert) {
        if (index > (s.length() - 1)) {
            return s + insert;
        }
        return s.substring(0, index) + insert + s.substring(index);
    }

    @Override
    public Change apply(TextFormatter.Change change) { // return change to accept changes and null to reject
        String changeData = change.getText();
        String oldData = calcx.getInputField().getText();
        String newData = change.getControlNewText();
        if (CalcX.ERROR.equals(oldData)) {
            if (changeData.isEmpty()) {
                change.setAnchor(newData.length());
                change.setCaretPosition(newData.length());
                return change;
            } else {
                calcx.getInputField().setText("");
                return null;
            }
        }

        if (changeData.matches(ACCEPTABLE_DATA)) {
            if (changeData.length() > 1) { // TODO: monitor paste
                if (true) {
                    return change;
                }
            }
            if (!changeData.isEmpty()) {
                char input = changeData.charAt(0);
                if (Character.isDigit(input)) {
                    if (!oldData.equals(oldData.trim())) {
                        oldData = oldData.trim();
                        if (CalcXUtils.checkLastCharIsOperator(oldData)) {
                            calcx.getInputField().setText(oldData + " " + input);
                        } else {
                            calcx.getInputField().setText(oldData + input);
                        }
                        return null;
                    }
                } else if (input == CalcX.DECIMAL_SEPARATOR) {
                    if (!validDecimal(oldData)) {
                        return null;
                    }
                } else if (CalcXUtils.isOperator(input)) {
                    if (CalcXUtils.isSignOperator(input) && (newData.trim().length() == 1)) {
                        return change;
                    }
                    if (!validOperator(oldData, input)) {
                        return null;
                    }
                } else if (input == '(') {
                    if (!oldData.isEmpty() && (CalcXUtils.getLastChar(oldData) != '(') && !CalcXUtils.checkLastCharIsOperator(oldData)) {
                        return null;
                    }
                } else if (input == ')') {
                    if (CalcXUtils.countBrackets(oldData) < 1) {
                        return null;
                    }
                } else if (input == CalcX.DEL) {
                    if (!oldData.isEmpty()) {
                        return null;
                    }
                } else if (input == 'E') {
                } else {
                    return null;
                }
                change.setAnchor(newData.length());
                change.setCaretPosition(newData.length());
                return change;
            }
            change.setAnchor(newData.length());
            change.setCaretPosition(newData.length());
            return change;
        } else if (change.isDeleted() && (oldData.length() == 1)) {
            change.setAnchor(newData.length());
            change.setCaretPosition(newData.length());
            return change;
        }
        return null;
    }

    private boolean validDecimal(String text) {
        if (text.isEmpty()) {
            return true;
        } else {
            for (int i = text.length() - 1; i >= 0; i--) {
                if (!Character.isDigit(text.charAt(i))) {
                    if (text.charAt(i) == CalcX.DECIMAL_SEPARATOR) {
                        return false;
                    }
                    break;
                }
            }
            return CalcXUtils.checkLastCharIsDigit(text) || CalcXUtils.checkLastCharIsOperator(text);
        }
    }

    private boolean validOperator(String text, char operator) {
        if (text.isEmpty() || (CalcXUtils.getLastChar(text) == '(')) {
            return CalcXUtils.isSignOperator(operator);
        } else if (CalcXUtils.checkLastCharIsDigit(text)) {
            return true;
        } else if (CalcXUtils.getLastChar(text) == ')') {
            return true;
        } else if (CalcXUtils.checkLastCharIsOperator(text)) {
            if (CalcXUtils.checkBeforeLastIsOperator(text)) {
                calcx.getInputField().setText(text.substring(0, text.length() - 2) + operator);
                return false;
            } else {
                if (CalcXUtils.isMinusSign(operator)) {
                    return true;
                } else {
                    calcx.getInputField().setText(text.substring(0, text.length() - 1) + operator);
                    return false;
                }
            }
        }
        return false;
    }
}
