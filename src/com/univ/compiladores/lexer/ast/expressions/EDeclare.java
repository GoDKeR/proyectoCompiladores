package com.univ.compiladores.lexer.ast.expressions;

import java.util.LinkedList;

/**
 * Created by Facu on 6/11/2016.
 */
public class EDeclare extends Expression{
    private final String type;
    private final LinkedList<String> vars;

    public EDeclare(String type, LinkedList<String> vars) {
        super("Declare");
        this.type = type;
        this.vars = vars;
    }

    public String getType() {
        return type;
    }

    public LinkedList<String> getVars() {
        return vars;
    }
}
