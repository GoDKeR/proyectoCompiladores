package com.univ.compiladores.lexer.ast;

import com.univ.compiladores.lexer.ast.expressions.Expression;

/**
 * Created by Facu on 6/11/2016.
 */
public class Statement {
    private final Expression body;

    public Statement(Expression body) {
        this.body = body;
    }

    public Expression getBody() {
        return body;
    }
}
