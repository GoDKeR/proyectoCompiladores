package com.univ.compiladores.lexer.ast.expressions;

/**
 * Created by Facu on 6/11/2016.
 */
public class ENumber extends Expression {
    private final double value;

    public ENumber(double value) {
        super("num");
        this.value = value;
    }

    public double getValue() {
        return value;
    }

}
