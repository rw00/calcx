package com.rw.calcx.calculator;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;


public class Calculator extends JFrame {
    private static final long serialVersionUID = 6466099539180882500L;

    private static final int SPACING = 3;

    private final JButton[] btnNum;
    private final JButton btn00, btnAdd, btnSub, btnMultiply, btnDivide, btnDelete, btnEquals;

    private final JTextField display;
    private boolean resultant;


    private Calculator() {
        super.setTitle("Calculator");
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setResizable(false);
        super.setIconImage(new ImageIcon(Calculator.class.getResource("/calcx.png")).getImage());

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(SPACING, SPACING, SPACING, SPACING));
        contentPane.setLayout(new GridBagLayout());
        super.setContentPane(contentPane);

        display = new JTextField();
        display.setFont(new Font("Tahoma", Font.PLAIN, 25));
        display.setColumns(10);
        display.setEditable(false);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(SPACING, SPACING, SPACING, SPACING);
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 5;
        contentPane.add(display, constraints);

        ButtonActionListener btnActionListener = new ButtonActionListener();
        Font font = new Font("Arial", Font.BOLD, 25);
        btnNum = new JButton[10];
        for (int i = 0; i < 10; i++) {
            btnNum[i] = new JButton(i + "");
            btnNum[i].setFont(font);
            btnNum[i].addActionListener(btnActionListener);
        }

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        contentPane.add(btnNum[1], constraints);

        constraints.gridx = 1;
        constraints.gridy = 3;
        contentPane.add(btnNum[2], constraints);

        constraints.gridx = 2;
        constraints.gridy = 3;
        contentPane.add(btnNum[3], constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPane.add(btnNum[4], constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        contentPane.add(btnNum[5], constraints);

        constraints.gridx = 2;
        constraints.gridy = 2;
        contentPane.add(btnNum[6], constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        contentPane.add(btnNum[7], constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        contentPane.add(btnNum[8], constraints);

        constraints.gridx = 2;
        constraints.gridy = 1;
        contentPane.add(btnNum[9], constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        contentPane.add(btnNum[0], constraints);

        btn00 = new JButton(".");
        constraints.gridx = 2;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        contentPane.add(btn00, constraints);
        btn00.addActionListener(btnActionListener);

        btnDivide = new JButton("/");
        constraints.gridx = 3;
        constraints.gridy = 2;
        contentPane.add(btnDivide, constraints);
        btnDivide.addActionListener(btnActionListener);

        btnMultiply = new JButton("*");
        constraints.gridx = 3;
        constraints.gridy = 1;
        contentPane.add(btnMultiply, constraints);
        btnMultiply.addActionListener(btnActionListener);

        btnSub = new JButton("-");
        constraints.gridx = 3;
        constraints.gridy = 3;
        contentPane.add(btnSub, constraints);
        btnSub.addActionListener(btnActionListener);

        btnAdd = new JButton("+");
        constraints.gridx = 3;
        constraints.gridy = 4;
        contentPane.add(btnAdd, constraints);
        btnAdd.addActionListener(btnActionListener);

        btnDelete = new JButton("<");
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.gridx = 4;
        constraints.gridy = 1;
        contentPane.add(btnDelete, constraints);
        btnDelete.addActionListener(btnActionListener);

        btnEquals = new JButton("=");
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 4;
        constraints.gridy = 2;
        constraints.gridheight = 3;
        contentPane.add(btnEquals, constraints);
        btnEquals.addActionListener(btnActionListener);

        btn00.setFont(font);
        btnDelete.setFont(font);
        btnAdd.setFont(font);
        btnSub.setFont(font);
        btnMultiply.setFont(font);
        btnDivide.setFont(font);
        btnEquals.setFont(font);
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Calculator calc = new Calculator();
                    calc.pack();
                    calc.setLocationRelativeTo(null); // center window
                    calc.setVisible(true);
                }
            });
    }

    private void calculate() {
        String input = display.getText().trim();
        String[] expression = input.split("\\s+");
        try {
            double result = Evaluator.postfixExpressionToResult(Evaluator.inputToPostfixExpression(expression));
            if ((result % 1) != 0) {
                input = String.format("%.4f", result);
                input = trimEnding(input, '0');
            } else {
                input = String.format("%.0f", result);
            }
        } catch (Exception ex) {
            input = "Error";
        }
        display.setText(input);
        resultant = true;
    }

    private void showNumber(int num) {
        if (resultant) {
            clearResult();
        }
        String input = display.getText().trim();
        if (!input.isEmpty()) {
            char c0 = CalcUtil.getLastCharFromIndex(input, 0);
            if (!CalcUtil.isOperator(c0 + "")) {
                input += num;
            } else {
                if (CalcUtil.isSign(c0 + "")) {
                    if (input.length() > 2) {
                        char c1 = CalcUtil.getLastNextChar(input, 1);
                        if (CalcUtil.isOperator(c1 + "")) {
                            input += num;
                        } else {
                            input += " " + num;
                        }
                    } else {
                        input += num;
                    }
                } else {
                    input += " " + num;
                }
            }
        } else {
            input += num;
        }
        display.setText(input);
    }

    private void showOperator(char operator) {
        if (resultant) {
            resultant = false;
        }
        String input = display.getText().trim();
        if (!input.isEmpty()) {
            if (input.length() > 1) {
                if (CalcUtil.isOperator(CalcUtil.getLastCharFromIndex(input, 0) + "")) {
                    if (CalcUtil.isOperator(CalcUtil.getLastNextChar(input, 1) + "")) {
                        // replace the 2 operators by one
                        input = input.substring(0, input.length() - 3) + operator;
                    } else if (operator == '-') {
                        input += " " + operator;
                    } else {
                        input = input.substring(0, input.length() - 1) + operator;
                    }
                } else {
                    input += " " + operator;
                }
            } else {
                if (Character.isDigit(CalcUtil.getLastCharFromIndex(input, 0))) {
                    input += " " + operator;
                } else if (CalcUtil.isSign(operator + "")) {
                    input = operator + "";
                }
            }
        } else {
            if (CalcUtil.isSign(operator + "")) {
                input += operator;
            }
        }
        display.setText(input);
    }

    private void clearResult() {
        display.setText("");
        resultant = false;
    }

    private void deleteInput() {
        if (resultant) {
            clearResult();
        } else {
            String input = display.getText().trim();
            if (!input.isEmpty()) {
                display.setText(input.substring(0, input.length() - 1));
            }
        }
    }

    private void showDecimal() {
        if (resultant) {
            clearResult();
        }
        String input = display.getText().trim();
        for (int i = input.length() - 1; i >= 0; i--) {
            char ch = input.charAt(i);
            if (!Character.isDigit(ch)) {
                if (ch == '.') {
                    return;
                }
                break;
            }
        }
        if (!input.isEmpty() && CalcUtil.isOperator(CalcUtil.getLastNextChar(input, 0) + "") && !CalcUtil.isOperator(CalcUtil.getLastNextChar(input, 1) + "")) {
            input += " ";
        }
        input += ".";
        display.setText(input);
    }

    private String trimEnding(String str, char c) {
        if (!str.isEmpty()) {
            int k = str.length();
            for (int i = str.length() - 1; i >= 0; i--) {
                if (c == str.charAt(i)) {
                    k--;
                } else {
                    break;
                }
            }
            return str.substring(0, k);
        }
        return "";
    }


    private class ButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < 10; i++) {
                if (e.getSource().equals(btnNum[i])) {
                    showNumber(i);
                    return;
                }
            }
            if (e.getSource().equals(btnDelete)) {
                deleteInput();
            } else if (e.getSource().equals(btnMultiply)) {
                showOperator('*');
            } else if (e.getSource().equals(btnDivide)) {
                showOperator('/');
            } else if (e.getSource().equals(btnAdd)) {
                showOperator('+');
            } else if (e.getSource().equals(btnSub)) {
                showOperator('-');
            } else if (e.getSource().equals(btn00)) {
                showDecimal();
            } else if (e.getSource().equals(btnEquals)) {
                calculate();
            }
        }
    }
}
