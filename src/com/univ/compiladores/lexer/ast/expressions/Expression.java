package com.univ.compiladores.lexer.ast.expressions;

/**
 * Created by Facu on 6/11/2016.
 */
public abstract class Expression {
    private final String name;

    public Expression(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
