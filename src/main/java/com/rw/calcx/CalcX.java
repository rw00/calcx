package com.rw.calcx;

import javafx.animation.PauseTransition;

import javafx.application.Application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.Insets;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;

import javafx.util.Duration;


public class CalcX extends Application {
    static final String ERROR = "Error";
    static final String DECIMAL_SEPARATOR = ".";
    static final String SUBTRACTION_OPERATOR = "\u2013";
    static final String ADDITION_OPERATOR = "+";
    static final String MULTIPLICATION_OPERATOR = "*";
    static final String DIVISION_OPERATOR = "/";
    static final String POWER_OPERATOR = "^";
    static final String DEL = "\u232B";
    private static final int MARGIN_SPACE = 5;
    private static final int SPACING = 5;
    private static final int BUTTON_SIZE = 32;
    private static final double WIDTH = (5 * (BUTTON_SIZE + SPACING)) + (2 * MARGIN_SPACE);
    private static final double HEIGHT = (6 * (BUTTON_SIZE + SPACING)) + (MARGIN_SPACE / 2.0);
    private ExpressionTextField inputField;
    private boolean resultant;
    private Button[] buttonNumbers;
    private Button buttonDecimalPoint;
    private Button buttonAdd, buttonSubtract, buttonMultiply, buttonDivide;
    private Button buttonDelete, buttonPower;
    private Button buttonRightBracket, buttonLeftBracket;
    private Button buttonEqual;


    public CalcX() {
    }


    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Calculator CalcX");
        primaryStage.getIcons().add(new Image(CalcX.class.getResourceAsStream("/calcx.png")));
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        VBox container = new VBox(SPACING);
        container.setPadding(new Insets(MARGIN_SPACE));
        initControls();

        GridPane buttonsGridPane = new GridPane();
        buttonsGridPane.setVgap(SPACING);
        buttonsGridPane.setHgap(SPACING);
        buttonsGridPane.add(buttonNumbers[7], 0, 0);
        buttonsGridPane.add(buttonNumbers[8], 1, 0);
        buttonsGridPane.add(buttonNumbers[9], 2, 0);
        buttonsGridPane.add(buttonNumbers[4], 0, 1);
        buttonsGridPane.add(buttonNumbers[5], 1, 1);
        buttonsGridPane.add(buttonNumbers[6], 2, 1);
        buttonsGridPane.add(buttonNumbers[1], 0, 2);
        buttonsGridPane.add(buttonNumbers[2], 1, 2);
        buttonsGridPane.add(buttonNumbers[3], 2, 2);
        buttonsGridPane.add(buttonNumbers[0], 1, 3);
        buttonsGridPane.add(buttonDecimalPoint, 0, 3);
        buttonsGridPane.add(buttonEqual, 2, 3);
        buttonsGridPane.add(buttonAdd, 3, 0);
        buttonsGridPane.add(buttonSubtract, 3, 1);
        buttonsGridPane.add(buttonMultiply, 3, 2);
        buttonsGridPane.add(buttonDivide, 3, 3);
        buttonsGridPane.add(buttonDelete, 4, 0);
        buttonsGridPane.add(buttonPower, 4, 1);
        buttonsGridPane.add(buttonRightBracket, 4, 2);
        buttonsGridPane.add(buttonLeftBracket, 4, 3);

        ButtonActionListener buttonActionListener = new ButtonActionListener();
        for (Node node : buttonsGridPane.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                setButtonPrefSize(button);
                button.setOnAction(buttonActionListener);
            }
        }
        initControlsFunctionality();

        container.getChildren().add(inputField);
        container.getChildren().add(buttonsGridPane);
        Scene primaryScene = new Scene(container);
        primaryStage.setScene(primaryScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public ExpressionTextField getInputField() {
        return inputField;
    }

    String evaluate() {
        String data;
        try {
            double result = ExpressionParserEvaluator.evaluate(inputField.getValidExpression());
            if ((result % 1) != 0) {
                data = String.format("%.4f", result);
                for (int i = data.length() - 1; i >= 0; i--) { // trim ending zeros
                    if (data.charAt(i) == '0') {
                        data = data.substring(0, i);
                    } else {
                        break;
                    }
                }
            } else {
                data = String.format("%.0f", result);
            }
        } catch (Exception ex) {
            data = ERROR;
        }
        resultant = true;
        return data;
    }

    void handleEquals() {
        inputField.setText(evaluate());
    }

    void handleBackspace() {
        String text = inputField.getText();
        if (!text.isEmpty()) {
            if (ERROR.equals(text)) {
                inputField.setText("");
            } else {
                text = text.substring(0, text.length() - 1);
                inputField.setText(text);
            }
        }
    }

    private static void setButtonPrefSize(Button button) {
        button.setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
    }

    private static void addClickAndHoldHandler(Node node, Duration holdTime, EventHandler<MouseEvent> handler) {
        MutableWrapper<MouseEvent> eventWrapper = new MutableWrapper<>();

        PauseTransition holdTimer = new PauseTransition(holdTime);
        holdTimer.setOnFinished(event -> handler.handle(eventWrapper.content));

        node.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
                eventWrapper.content = event;
                holdTimer.playFromStart();
            });
        node.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> holdTimer.stop());
        node.addEventHandler(MouseEvent.DRAG_DETECTED, event -> holdTimer.stop());
    }

    private void initControls() {
        inputField = new ExpressionTextField(CalcX.this);
        inputField.setPrefHeight(BUTTON_SIZE);

        buttonNumbers = new Button[10];
        for (int i = 0; i < buttonNumbers.length; i++) {
            buttonNumbers[i] = new Button(i + "");
        }
        buttonAdd = new Button(ADDITION_OPERATOR);
        buttonSubtract = new Button(SUBTRACTION_OPERATOR);
        buttonMultiply = new Button(MULTIPLICATION_OPERATOR);
        buttonDivide = new Button(DIVISION_OPERATOR);
        buttonPower = new Button(POWER_OPERATOR);
        buttonDelete = new Button(DEL);
        buttonDecimalPoint = new Button(DECIMAL_SEPARATOR);
        buttonRightBracket = new Button("(");
        buttonLeftBracket = new Button(")");
        buttonEqual = new Button("=");
    }

    private void initControlsFunctionality() {
        addClickAndHoldHandler(buttonDelete, Duration.seconds(1), new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    inputField.setText("");
                }
            });

        inputField.addEventHandler(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if ("=".equals(event.getCharacter())) {
                        handleEquals();
                        event.consume(); // don't type it
                    }
                }
            });

        inputField.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (KeyCode.BACK_SPACE == event.getCode()) {
                        handleBackspace();
                    }
                }
            });
    }

    private void showInput(char input) {
        inputField.appendText(CalcXUtils.getValidInput(input + ""));
    }


    private class ButtonActionListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String data = ((Button) event.getSource()).getText();
            if (resultant) {
                inputField.setText("");
                resultant = false;
            }
            if (DEL.equals(data)) {
                handleBackspace();
            } else if ("=".equals(data)) {
                handleEquals();
            } else {
                showInput(data.charAt(0));
            }
        }
    }

    private static class MutableWrapper<T> {
        public T content;
    }
}
