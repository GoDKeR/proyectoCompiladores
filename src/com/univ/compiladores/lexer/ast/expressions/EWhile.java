package com.univ.compiladores.lexer.ast.expressions;

import java.util.LinkedList;

/**
 * Created by Facu on 6/11/2016.
 */
public class EWhile extends Expression {
    private final Expression cond;
    private final LinkedList<Expression> statement;

    public EWhile(Expression cond, LinkedList<Expression> statement) {
        super("wh");
        this.cond = cond;
        this.statement = statement;
    }

    public Expression getCond() {
        return cond;
    }

    public LinkedList<Expression> getStatement() {
        return statement;
    }
}
