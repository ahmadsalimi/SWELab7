package codeGenerator;

import scanner.token.Token;

public class CodeGeneratorFacade {
    private final CodeGenerator codegen = new CodeGenerator();

    public void printMemory() {
        codegen.printMemory();
    }

    public void semanticFunction(int func, Token next) {
        codegen.semanticFunction(func, next);
    }
}
