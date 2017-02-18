package com.rw.calcx;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;


public class ExpressionTextField extends TextField {

    public ExpressionTextField(CalcX calcx) {
        setTextFormatter(new TextFormatter<>(new ExpressionFilter(calcx)));
    }


    public String getValidExpression() {
        return CalcXUtils.getValidInput(getText().replace(CalcX.SUBTRACTION_OPERATOR, "-"));
    }
}
