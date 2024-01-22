package com.univ.compiladores.lexer.ast.expressions;

/**
 * Created by Facu on 6/11/2016.
 */
public class EAssign extends Expression{
    private final EId lPart;
    private final Expression rPart;

    public EAssign(EId lPart, Expression rPart) {
        super("Assign");
        this.lPart = lPart;
        this.rPart = rPart;
    }

    public EId getlPart() {
        return lPart;
    }

    public Expression getrPart() {
        return rPart;
    }
}
