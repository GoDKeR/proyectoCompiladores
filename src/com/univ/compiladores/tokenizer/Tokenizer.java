package com.univ.compiladores.tokenizer;

import com.univ.compiladores.lexer.Analyzer;
import com.univ.compiladores.lexer.ast.Statement;
import com.univ.compiladores.tokenizer.errors.ParserException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Facu on 7/11/2016.
 */
public class Tokenizer {
    /*
 Lista de patrones que contiene todas las regex
 */
    private static List<Pattern> regexList;
    private LinkedList<Token> tokenList;
    /*
    Todas las regex, declaradas para hacer mas facil la construccion
     */
    private static final String DIGITNOGROUP = "-?\\d+";
    private static final String DIGIT = "(" + DIGITNOGROUP + ")";

    private static final String ASSIGN = "\\=";
    private static final String OPERATIONS = "(\\+|\\-|\\*|\\/)";
    private static final String EXPRESSIONS = "(\\<|\\>|\\&|\\|)";
    private static final String VARIABLE = "(\\_[a-zA-Z]\\w*|\\_\\d*\\w+)";


    private static final String READFUNC = "read";
    private static final String WRITEFUNC = "write";
    private static final String OPENINGCOMMENT = "\\/\\*.*|\\/\\*";
    private static final String CLOSINGCOMMENT = "\\*\\/";
    private static final String SINGLECOMMENT = "\\/\\/(.*)";

    private static final String RESERVEDTYPES = "(double|long)";
    private static final String RESERVEDWORDS = "(if|else|then|while|break)";

    public Tokenizer() {
        regexList = new ArrayList<>();

        /*
        Lista de lista de tokens
         */
        tokenList = new LinkedList<>();

        /*
        Lleno la lista de regex
         */
        regexList.add(0, Pattern.compile("^" + RESERVEDTYPES));
        regexList.add(1, Pattern.compile("^" + RESERVEDWORDS));
        regexList.add(2, Pattern.compile("^" + DIGIT));
        regexList.add(3, Pattern.compile("^" + VARIABLE));
        regexList.add(4, Pattern.compile("^" + EXPRESSIONS));
        regexList.add(5, Pattern.compile("^" + ASSIGN));
        regexList.add(6, Pattern.compile("^" + OPERATIONS));
        regexList.add(7, Pattern.compile("^" + READFUNC));
        regexList.add(8, Pattern.compile("^" + WRITEFUNC));
        regexList.add(9, Pattern.compile("^,"));
        regexList.add(10, Pattern.compile("^;"));
        regexList.add(11, Pattern.compile("^\\("));
        regexList.add(12, Pattern.compile("^\\)"));
        regexList.add(13, Pattern.compile("^endIf"));

    }

    public LinkedList<Statement> Analyze (String[] lines){
        boolean skip = false;

        //Itero sobre cada linea del archivo
        for (String s: lines){
            String line = s;
            if (line.matches(OPENINGCOMMENT))
                skip = true;
            else if (line.matches(CLOSINGCOMMENT)) {
                skip = false;
                continue;
            }
            if (skip || line.matches(SINGLECOMMENT))
                continue;

            /*
            Remuevo 2 o mas espacios en blanco
            */
            line = line.replaceAll("\\s\\s+", "");
            boolean match;

            if (!line.equalsIgnoreCase(""))
                tokenList.add(new Token(ETokenType.NEWLN, ""));

            while (!line.equalsIgnoreCase("")) {
                match = false;
                for (int i = 0; i < regexList.size(); i++) {
                    Pattern p = regexList.get(i);

                    if (p.matcher(line).find()) {
                        match = true;
                        tokenList.addAll(parseLine(p.matcher(line), i, false));
                        line = p.matcher(line).replaceFirst("").trim();
                        break;
                    }
                }
                if (!match) throw new ParserException("Unexpected character in input: " + line);
            }
        }

        Analyzer a = new Analyzer(tokenList);

        return a.getStatements();
    }
    private static List<Token> parseLine(Matcher matcher, int i, boolean verbose) {
        List<Token> tokenList = new ArrayList<>();
        String temp;
        if (matcher.find()) {
            switch (i) {
                case 0:
                    temp = matcher.group(1);
                    if (temp.equalsIgnoreCase("double"))
                        tokenList.add(new Token(ETokenType.VARTYPE, "double"));
                    else if (temp.equalsIgnoreCase("long"))
                        tokenList.add(new Token(ETokenType.VARTYPE, "long"));

                    break;
                case 1:
                    temp = matcher.group(1);
                    if (temp.equalsIgnoreCase("if"))
                        tokenList.add(new Token(ETokenType.IF, "if"));
                    else if (temp.equalsIgnoreCase("else"))
                        tokenList.add(new Token(ETokenType.ELSE, "else"));
                    else if (temp.equalsIgnoreCase("then"))
                        tokenList.add(new Token(ETokenType.THEN, "then"));
                    else if (temp.equalsIgnoreCase("while"))
                        tokenList.add(new Token(ETokenType.WHILE, "while"));
                    else if (temp.equalsIgnoreCase("break"))
                        tokenList.add(new Token(ETokenType.BREAK, "break"));

                    break;
                case 2:
                    tokenList.add(new Token(ETokenType.NUM, matcher.group(1)));
                    break;
                case 3:
                    tokenList.add(new Token(ETokenType.ID, matcher.group(1)));
                    break;
                case 4:
                    temp = matcher.group(1);
                    if (temp.equalsIgnoreCase("<"))
                        tokenList.add(new Token(ETokenType.CONOP, "<"));
                    else if (temp.equalsIgnoreCase(">"))
                        tokenList.add(new Token(ETokenType.CONOP, ">"));
                    else if (temp.equalsIgnoreCase("&"))
                        tokenList.add(new Token(ETokenType.CONOP, "&"));
                    else if (temp.equalsIgnoreCase("|"))
                        tokenList.add(new Token(ETokenType.CONOP, "|"));
                    break;
                case 5:
                    tokenList.add(new Token(ETokenType.ASIGOP, "="));
                    break;
                case 6:
                    temp = matcher.group(1);
                    if (temp.equalsIgnoreCase("+"))
                        tokenList.add(new Token(ETokenType.PLUSMINUS, "+"));
                    else if (temp.equalsIgnoreCase("-"))
                        tokenList.add(new Token(ETokenType.PLUSMINUS, "-"));
                    else if (temp.equalsIgnoreCase("*"))
                        tokenList.add(new Token(ETokenType.MULTDIV, "*"));
                    else if (temp.equalsIgnoreCase("/"))
                        tokenList.add(new Token(ETokenType.MULTDIV, "/"));
                    break;
                case 7:
                    tokenList.add(new Token(ETokenType.READ, "read"));
                    break;
                case 8:
                    tokenList.add(new Token(ETokenType.WRITE, "write"));
                    break;
                case 9:
                    tokenList.add(new Token(ETokenType.COMMA, ","));
                    break;
                case 10:
                    tokenList.add(new Token(ETokenType.PERIOD, ";"));
                    break;
                case 11:
                    tokenList.add(new Token(ETokenType.LPAREN, "("));
                    break;
                case 12:
                    tokenList.add(new Token(ETokenType.RPAREN, ")"));
                    break;
                case 13:
                    tokenList.add(new Token(ETokenType.ENDIF, "endIf"));
                    break;
            }
        }
        if (verbose)
            for (Token t : tokenList)
                System.out.println(t.getType() + ":" + t.getLexema());

        return tokenList;
    }
}
