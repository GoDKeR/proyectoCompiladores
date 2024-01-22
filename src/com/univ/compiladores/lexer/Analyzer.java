package com.univ.compiladores.lexer;

import com.univ.compiladores.tokenizer.ETokenType;
import com.univ.compiladores.tokenizer.Token;
import com.univ.compiladores.lexer.ast.Statement;
import com.univ.compiladores.lexer.ast.expressions.*;
import com.univ.compiladores.lexer.errors.DeclarationParsingException;
import com.univ.compiladores.lexer.errors.UnexpectedSymbolException;

import java.util.Arrays;
import java.util.LinkedList;

import static com.univ.compiladores.tokenizer.ETokenType.*;


/**
 * Created by Facu on 7/10/2016.
 */
public final class Analyzer {

    private LinkedList<Statement> statements = new LinkedList<>();
    private LinkedList<Token> tokenList;
    Token token;

    public Analyzer(LinkedList<Token> tokenList) {
        this.tokenList = (LinkedList<Token>) tokenList.clone();

        for(Token t : tokenList){
            System.out.println(t.getType() + ":" + t.getLexema());
        }

        while(foundToken(NEWLN)){
            statements.add(parse());
        }
    }

    public Statement parse() {
        if (nextTokenMatches(VARTYPE))
                return new Statement(parseDeclaration());

        return new Statement(parseExpression());
    }

    private Expression parseDeclaration() {
        /*
        EBNF FORM of declarations
        dec     = <vartype>, (<id> | { ",", <id>} ), "¬"
        vartype = "double"|"long","¬"
        id      = "_"{"a"|"b"|...}+
         */
        expected(VARTYPE);
        String type = token.getLexema();
        if (foundToken(ID)) {
            String vars = token.getLexema();
            if (nextTokenMatches(COMMA)) {
                while (!nextTokenMatches(NEWLN)) {
                    expected(COMMA);
                    vars += token.getLexema();
                    expected(ID);
                    vars += token.getLexema();
                }
            }
            return new EDeclare(type, new LinkedList<>(Arrays.asList(vars.split(","))));
        } else {
            throw new DeclarationParsingException("Declaration error, a variable was expected.");
        }
    }

    private Expression parseExpression(){
        return parseAdd();
    }

    private Expression parseAdd() {
        Expression left = parseMul();

        while (foundToken(ETokenType.PLUSMINUS)) {
            String operator = token.getLexema();

            Expression right = parseMul();

            left = new EOp(operator, left, right);
        }
        return left;

    }

    private Expression parseMul() {
        Expression left = parseAtom();

        while (foundToken(ETokenType.MULTDIV)) {
            String operator = token.getLexema();
            Expression right = parseAtom();

            left = new EOp(operator, left, right);
        }
        return left;
    }

    private Expression parseAtom() {
        if (foundToken(NUM)) {
            return new ENumber(Double.parseDouble(token.getLexema()));
        }else if (foundToken(ID)){
            return new EId(token.getLexema());
        } else if (foundToken(LPAREN)) {
            Expression e = parseExpression();
            if (foundToken(ETokenType.RPAREN)) {
                return e;
            } else {
                throw new UnexpectedSymbolException("Missing ')'");
            }
        }else if (foundToken(IF)) {
            expected(LPAREN);
            Expression cond = parseExpression();
            expected(RPAREN);

            if (nextTokenMatches(NEWLN))
                expected(NEWLN);

            LinkedList<Expression> thenPart = new LinkedList<>();
            //thenPart.add(parseExpression());
            while(!(nextTokenMatches(ELSE) || nextTokenMatches(ENDIF))) {
                thenPart.add(parseExpression());
                if (nextTokenMatches(NEWLN))
                    expected(NEWLN);
            }

            if (nextTokenMatches(NEWLN))
                expected(NEWLN);

            if (nextTokenMatches(ENDIF))
                expected(ENDIF);

            if (foundToken(ELSE)) {
                if (nextTokenMatches(NEWLN))
                    expected(NEWLN);

                LinkedList<Expression> elsePart = new LinkedList<>();

                while(!nextTokenMatches(ENDIF)){
                    elsePart.add(parseExpression());
                    if (nextTokenMatches(NEWLN))
                        expected(NEWLN);
                }
                if (nextTokenMatches(NEWLN))
                    expected(NEWLN);

                expected(ENDIF);
                return new EIf(cond, thenPart, elsePart);
            }
            return new EIf(cond, thenPart);
        }else if (foundToken(WHILE)){
            expected(LPAREN);
            Expression cond = parseExpression();
            expected(RPAREN);

            if (nextTokenMatches(NEWLN))
                expected(NEWLN);

            LinkedList<Expression> stmnt = new LinkedList<>();
            while(!nextTokenMatches(BREAK)) {
                stmnt.add(parseExpression());
                if (nextTokenMatches(NEWLN))
                    expected(NEWLN);
            }

            expected(BREAK);
            return new EWhile(cond,stmnt);
        } else {
            throw new UnexpectedSymbolException("Unexpected symbol: " + tokenList.peek().getLexema() +
                    "(" + tokenList.peek().getTypeString() +")" + " founded.");
        }
    }

    private ETokenType nextToken(){
        token = tokenList.pop();
        return token.getType();
    }

    private boolean nextTokenMatches(ETokenType expectedToken) {
        return tokenList.peek().getType() == expectedToken;
    }
    private boolean foundToken(ETokenType expectedToken){
        if (tokenList.isEmpty())
            return false;

        Token tk = tokenList.peek();

        if (tk.getType() == expectedToken){
            tokenList.pop();
            token = tk;
            return true;
        }else{
            return false;
        }
    }
    private void expected(ETokenType expectedToken){
        if (!foundToken(expectedToken))
            throw new UnexpectedSymbolException("Unexpected " + tokenList.peek().getTypeString() + " a " +
                    expectedToken.toString() + " was expected.");
    }

    public LinkedList<Statement> getStatements() {
        return statements;
    }
}
