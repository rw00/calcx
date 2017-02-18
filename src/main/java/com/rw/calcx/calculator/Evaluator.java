package com.rw.calcx.calculator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Evaluator {
    private Evaluator() {
    }

    public static String[] inputToPostfixExpression(String[] inputTokens) {
        List<String> expressionTokens = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();

        for (String token : inputTokens) {
            if (token.isEmpty()) {
                continue;
            }
            if (CalcUtil.isOperator(token)) {
                while (!stack.isEmpty() && CalcUtil.isOperator(stack.peek())) {
                    if (precedence(token, stack.peek())) {
                        expressionTokens.add(stack.pop());
                        continue;
                    }
                    break;
                }
                stack.push(token); // rearranged operator
            } else {
                expressionTokens.add(token); // token is a number
            }
        }
        while (!stack.isEmpty()) {
            expressionTokens.add(stack.pop());
        }
        return expressionTokens.toArray(new String[0]);
    }

    public static double postfixExpressionToResult(String[] tokens) {
        Deque<String> data = new ArrayDeque<>();

        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }

            // token is a value, push it onto the stack
            if (!CalcUtil.isOperator(token)) {
                data.push(token);
            } else {
                // token is an operator, then pop top two numbers
                double n2 = Double.parseDouble(data.pop());
                double n1 = Double.parseDouble(data.pop());

                double result = 0;
                // get the result
                switch (token) {
                case "+":
                    result = n1 + n2;
                    break;
                case "-":
                    result = n1 - n2;
                    break;
                case "*":
                    result = n1 * n2;
                    break;
                case "/":
                    result = n1 / n2;
                    break;
                }
                // push one result onto stack
                data.push(String.valueOf(result));
            }
        }
        return Double.parseDouble(data.pop()); // result
    }

    // compare precedence of operators
    // return true if token2 is higher in precedence to token1
    private static boolean precedence(String token1, String token2) {
        return CalcUtil.isHighPrecedenceOperator(token2);
    }
}
