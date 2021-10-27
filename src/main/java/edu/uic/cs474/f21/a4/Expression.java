package edu.uic.cs474.f21.a4;

public abstract class Expression {
    public static class IntConstant extends Expression {
        public final int c;

        public IntConstant(int c) {
            this.c = c;
        }
    }

    public static class BinOpExpression extends Expression {
        // +, -, *, /
        public final Expression left, right;
        public final Operator op;

        public enum Operator { PLUS, MINUS, TIMES, DIV };

        public BinOpExpression(Operator op, Expression left, Expression right) {
            this.left = left;
            this.right = right;
            this.op = op;
        }
    }

    public static class LetExpression extends Expression {
        public final Name variableName;
        public final Expression value;
        public final Expression body;

        public LetExpression(Name variableName, Expression value, Expression body) {
            this.variableName = variableName;
            this.value = value;
            this.body = body;
        }
    }

    public static class VariableExpression extends Expression {
        public final Name variable;

        public VariableExpression(Name variable) {
            this.variable = variable;
        }
    }

    public static class EqExpression extends Expression {
        public final Expression left;
        public final Expression right;

        public EqExpression(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }
    }

    public static class IfExpression extends Expression {
        public final Expression cond;
        public final Expression thenSide;
        public final Expression elseSide;

        public IfExpression(Expression cond, Expression thenSide, Expression elseSide) {
            this.cond = cond;
            this.thenSide = thenSide;
            this.elseSide = elseSide;
        }
    }

    public static class FunctionDeclExpression extends Expression {
        public final Name name;
        public final Name[] formalArguments;
        public final Expression functionBody;
        public final Expression scope;

        public FunctionDeclExpression(Name name, Name[] formalArguments, Expression functionBody, Expression scope) {
            this.name = name;
            this.formalArguments = formalArguments;
            this.functionBody = functionBody;
            this.scope = scope;
        }
    }

    public static class FunctionCallExpression extends Expression {
        public final Name name;
        public final Expression[] actualArguments;

        public FunctionCallExpression(Name name, Expression[] actualArguments) {
            this.name = name;
            this.actualArguments = actualArguments;
        }
    }

    public static class FirstClassFunctionDeclExpression extends Expression {
        public final Name name;
        public final Name[] formalArguments;
        public final Expression functionBody;

        public FirstClassFunctionDeclExpression(Name name, Name[] formalArguments, Expression functionBody) {
            this.name = name;
            this.formalArguments = formalArguments;
            this.functionBody = functionBody;
        }
    }

    public static class FirstClassFunctionCallExpression extends Expression {
        public final Expression functionToBeCalled;
        public final Expression[] actualArguments;

        public FirstClassFunctionCallExpression(Expression functionToBeCalled, Expression[] actualArguments) {
            this.functionToBeCalled = functionToBeCalled;
            this.actualArguments = actualArguments;
        }
    }

    public static class LambdaDeclExpression extends Expression {
        public final Name[] formalArguments;
        public final Expression functionBody;

        public LambdaDeclExpression(Name[] formalArguments, Expression functionBody) {
            this.formalArguments = formalArguments;
            this.functionBody = functionBody;
        }
    }

    // Similar to ++var, increments the variable and evaluates to the new value
    public static class IncExpression extends Expression {
        public final Name var;

        public IncExpression(Name var) {
            this.var = var;
        }
    }
}