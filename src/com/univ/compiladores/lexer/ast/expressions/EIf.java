package com.univ.compiladores.lexer.ast.expressions;

import java.util.LinkedList;

/**
 * Created by Facu on 6/11/2016.
 */
public class EIf extends Expression {
    private final Expression cond;
    private final LinkedList<Expression> thenPart;
    private LinkedList<Expression> elsePart;

    public EIf(Expression cond, LinkedList<Expression> thenPart, LinkedList<Expression> elsePart) {
        super("if");
        this.cond = cond;
        this.thenPart = thenPart;
        this.elsePart = elsePart;
    }

    public EIf(Expression cond, LinkedList<Expression> thenPart) {
        super("if");
        this.cond = cond;
        this.thenPart = thenPart;
    }

    public Expression getCond() {
        return cond;
    }

    public LinkedList<Expression> getThenPart() {
        return thenPart;
    }

    public LinkedList<Expression> getElsePart() {
        return elsePart;
    }
}
