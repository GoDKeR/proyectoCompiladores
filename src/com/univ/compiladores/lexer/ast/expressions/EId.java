package com.univ.compiladores.lexer.ast.expressions;

/**
 * Created by Facu on 6/11/2016.
 */
public class EId extends Expression {
    private final String name;

    public EId(String name) {
        super("ID");
        this.name = name;
    }

    public String getIDName() {
        return name;
    }
}
