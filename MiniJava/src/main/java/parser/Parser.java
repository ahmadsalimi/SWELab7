package parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import Log.Log;
import codeGenerator.CodeGeneratorFacade;
import errorHandler.ErrorHandler;
import scanner.lexicalAnalyzer;
import scanner.token.Token;

public class Parser {
    private final List<Rule> rules;
    private final Stack<Integer> parsStack;
    private ParseTable parseTable;
    private final CodeGeneratorFacade cgf;

    public Parser() {
        parsStack = new Stack<>();
        parsStack.push(0);
        try {
            parseTable = new ParseTable(Files.readAllLines(Paths.get("src/main/resources/parseTable")).get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        rules = new ArrayList<>();
        try {
            for (String stringRule : Files.readAllLines(Paths.get("src/main/resources/Rules"))) {
                rules.add(new Rule(stringRule));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        cgf = new CodeGeneratorFacade();
    }

    public void startParse(java.util.Scanner sc) {
        scanner.lexicalAnalyzer lexicalAnalyzer = new lexicalAnalyzer(sc);
        Token lookAhead = lexicalAnalyzer.getNextToken();
        boolean finish = false;
        Action currentAction;
        while (!finish) {
            try {
                Log.print(lookAhead.toString() + "\t" + parsStack.peek());
                currentAction = parseTable.getActionTable(parsStack.peek(), lookAhead);
                Log.print(currentAction.toString());

                if (currentAction.action == act.shift) {
                    parsStack.push(currentAction.number);
                    lookAhead = lexicalAnalyzer.getNextToken();
                } else if (currentAction.action == act.reduce) {
                    Rule rule = rules.get(currentAction.number);
                    for (int i = 0; i < rule.RHS.size(); i++) {
                        parsStack.pop();
                    }

                    Log.print(parsStack.peek() + "\t" + rule.LHS);
                    parsStack.push(parseTable.getGotoTable(parsStack.peek(), rule.LHS));
                    Log.print(parsStack.peek() + "");
                    try {
                        cgf.semanticFunction(rule.semanticAction, lookAhead);
                    } catch (Exception e) {
                        Log.print("Code Genetator Error");
                    }
                } else if (currentAction.action == act.accept) {
                    finish = true;
                }
                Log.print("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!ErrorHandler.hasError) cgf.printMemory();
    }
}
