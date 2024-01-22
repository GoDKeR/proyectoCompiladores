package com.univ.compiladores.tokenizer;

import com.univ.compiladores.tokenizer.ETokenType;

/**
 * Created by Facu on 29/8/2016.
 */
public class Token {
    private final ETokenType type;
    private final String lexema;

    public Token(ETokenType type, String lexema) {
        this.type = type;
        this.lexema = lexema.replaceAll("\\s+","");
    }

    public ETokenType getType() {
        return type;
    }
    public String getTypeString() {
        switch (type) {
            case ASIGOP:
                return "ASIGNACIÓN";
            case NUM:
                return "NUMÉRICO";
            case ID:
                return "IDENTIFICADOR";
            case PLUSMINUS:
                return "OPERACION ARITMÉTICA + -";
            case MULTDIV:
                return "OPERACION ARITMETICA * /";
            case CONOP:
                return "EXPRESIÓN";
            case IF:
                return "IF";
            case ELSE:
                return "ELSE";
            case WHILE:
                return "WHILE";
            case BREAK:
                return "BREAK";
            case NEWLN:
                return "NEWLN";
            case EPSILON:
                return "EPSILON";
            case VARTYPE:
                return "TIPO";
            case COMMA:
                return "COMA";
            case READ:
                return "READ";
            case WRITE:
                return "WRITE";
            case PERIOD:
                return ";";
            case LPAREN:
            case RPAREN:
                return "Paren";
            case ENDIF:
                return "ENDIF";
            default:
                return "SINTIPO";
        }
    }

    public String getLexema() {
        return lexema;
    }
}
