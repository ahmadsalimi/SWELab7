package codeGenerator;

/**
 * Created by mohammad hosein on 6/28/2015.
 */

public class Address {
    private final int num;
    private final TypeAddress Type;
    private final varType varType;

    public Address(int num, varType varType, TypeAddress Type) {
        this.num = num;
        this.Type = Type;
        this.varType = varType;
    }

    public Address(int num, varType varType) {
        this.num = num;
        Type = TypeAddress.Direct;
        this.varType = varType;
    }

    public String toString() {
        if (getType() == TypeAddress.Direct) {
            return getNum() + "";
        } else if (getType() == TypeAddress.Indirect) {
            return "@" + getNum();
        } else if (getType() == TypeAddress.Imidiate) {
            return "#" + getNum();
        }
        return getNum() + "";
    }

    public int getNum() {
        return num;
    }

    public TypeAddress getType() {
        return Type;
    }

    public codeGenerator.varType getVarType() {
        return varType;
    }
}
