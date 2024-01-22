package com.univ.compiladores.lexer.ast.expressions;

/**
 * Created by Facu on 6/11/2016.
 */
public class EOp extends Expression {
    private final String operator;
    private final Expression left, right;

    public EOp(String operator, Expression left, Expression right) {
        super("op");
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public String getOperator() {
        return operator;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }
}
