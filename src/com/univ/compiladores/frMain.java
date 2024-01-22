package com.univ.compiladores;

import com.univ.compiladores.lexer.ast.Statement;
import com.univ.compiladores.lexer.ast.expressions.*;
import com.univ.compiladores.tokenizer.Tokenizer;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by Facu on 7/11/2016.
 */
public class frMain {

    private static JFrame frame;
    private JPanel root;
    private JTree ast;
    private JTextArea code;
    private JButton analizarButton;
    private JButton cargarButton;
    private static Tokenizer tknz;
    private DefaultTreeModel model;

    public frMain() {

        DefaultMutableTreeNode top =
                new DefaultMutableTreeNode("Program");

        model = new DefaultTreeModel(top);

        analizarButton.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                LinkedList<Statement> ll = tknz.Analyze(code.getText().split("\\r?\\n"));
                createNodes(top, ll);
                ast.setModel(model);
            }
        });
        cargarButton.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                JFileChooser fc = new JFileChooser();

                FileFilter filter = new FileNameExtensionFilter("Archivo de Texto", "txt");
                fc.setFileFilter(filter);

                int ret = fc.showOpenDialog(frame);

                if (ret == JFileChooser.APPROVE_OPTION) {
                    try {
                        FileReader file = new FileReader(fc.getSelectedFile());

                        BufferedReader bf = new BufferedReader(file);

                        String line;
                        while ((line = bf.readLine()) != null) {
                            code.append(line + "\n");
                        }
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

    }

    public static void main(String[] args) {
        frame = new JFrame("frMain");
        frame.setContentPane(new frMain().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(800, 600));
        frame.setVisible(true);

        tknz = new Tokenizer();

    }

    private void createNodes(DefaultMutableTreeNode top, LinkedList<Statement> data) {
        DefaultMutableTreeNode statement = null;
        DefaultMutableTreeNode child = null;
        DefaultMutableTreeNode child2;
        String name;

        for (Statement s : data) {
            //switch (name = s.getBody().getName()) {
            statement = new DefaultMutableTreeNode(name = s.getBody().getName());
            top.add(statement);

            switch (name){
                case "Declare":
                    EDeclare d = (EDeclare) s.getBody();
                    child = new DefaultMutableTreeNode(d.getType());
                    statement.add(child);

                    child = new DefaultMutableTreeNode("args");
                    statement.add(child);

                    for (String str : d.getVars()) {
                        child2 = new DefaultMutableTreeNode(str);
                        child.add(child2);
                    }
                    break;
                case "Assign":
                    EAssign a = (EAssign) s.getBody();
                    child = new DefaultMutableTreeNode("lpart");
                    statement.add(child);

                    child2 = new DefaultMutableTreeNode(a.getlPart().getName());
                    child.add(child2);

                    child = new DefaultMutableTreeNode("rpart");
                    statement.add(child);

                    createNodes(child, a.getrPart());
                    break;
                case "ID":
                    EId id = (EId) s.getBody();
                    statement.add(new DefaultMutableTreeNode((id.getIDName())));
                    break;
                case "if":
                    EIf i = (EIf) s.getBody();
                    child = new DefaultMutableTreeNode("condition");
                    statement.add(child);

                    createNodes(child, i.getCond());

                    child = new DefaultMutableTreeNode("thenPart");
                    statement.add(child);

                    for (Expression expression : i.getThenPart())
                        createNodes(child, expression);

                    if (i.getElsePart()!=null) {
                        child = new DefaultMutableTreeNode("elsePart");
                        statement.add(child);

                        for (Expression expression : i.getElsePart())
                            createNodes(child, expression);
                    }
                    break;
                case "num":
                    statement.add(new DefaultMutableTreeNode(((ENumber) s.getBody()).getValue()));
                    break;
                case "op":
                    EOp p = (EOp) s.getBody();

                    child = new DefaultMutableTreeNode(p.getOperator());
                    statement.add(child);

                    createNodes(child, p.getLeft());
                    createNodes(child, p.getRight());
                    break;
                case "wh":
                    EWhile w = (EWhile) s.getBody();

                    child = new DefaultMutableTreeNode("condition");
                    statement.add(child);

                    createNodes(child, w.getCond());

                    child = new DefaultMutableTreeNode("body");
                    statement.add(child);

                    for (Expression expression : w.getStatement())
                        createNodes(child, expression);
                    break;
            }
            //}
        }
    }

    private void createNodes(DefaultMutableTreeNode parent, Expression e){
        DefaultMutableTreeNode name;
        DefaultMutableTreeNode node;
        DefaultMutableTreeNode child;

        name = new DefaultMutableTreeNode(e.getName());
        parent.add(name);

        switch (e.getName()){
            case "Declare":
                EDeclare d = (EDeclare) e;
                node = new DefaultMutableTreeNode(d.getType());
                name.add(node);

                node = new DefaultMutableTreeNode("args");
                name.add(node);

                for (String str : d.getVars()) {
                    child = new DefaultMutableTreeNode(str);
                    node.add(child);
                }
                break;
            case "Assign":
                EAssign a = (EAssign) e;
                node = new DefaultMutableTreeNode("lpart");
                name.add(node);

                child = new DefaultMutableTreeNode(a.getlPart().getName());
                node.add(child);

                node = new DefaultMutableTreeNode("rpart");
                name.add(node);

                createNodes(node,a.getrPart());
                break;
            case "ID":
                EId id = (EId) e;
                name.add(new DefaultMutableTreeNode((id.getIDName())));
                break;
            case "if":
                EIf i = (EIf) e;
                node = new DefaultMutableTreeNode("condition");
                name.add(node);

                createNodes(node, i.getCond());

                node = new DefaultMutableTreeNode("thenPart");
                name.add(node);

                for (Expression expression : i.getThenPart())
                    createNodes(node, expression);

                if (i.getElsePart()!=null) {
                    node = new DefaultMutableTreeNode("elsePart");
                    name.add(node);

                    for (Expression expression : i.getElsePart())
                        createNodes(node, expression);
                }
                break;
            case "num":
                name.add(new DefaultMutableTreeNode(((ENumber) e).getValue()));
                break;
            case "op":
                EOp p = (EOp) e;

                node = new DefaultMutableTreeNode(((EOp) e).getOperator());
                name.add(node);

                createNodes(node, p.getLeft());
                createNodes(node, p.getRight());
                break;
            case "wh":
                EWhile w = (EWhile) e;

                node = new DefaultMutableTreeNode("condition");
                name.add(node);

                createNodes(node, w.getCond());

                node = new DefaultMutableTreeNode("body");
                name.add(node);

                for (Expression expression : w.getStatement())
                    createNodes(node,expression);
                break;
        }
    }
}
