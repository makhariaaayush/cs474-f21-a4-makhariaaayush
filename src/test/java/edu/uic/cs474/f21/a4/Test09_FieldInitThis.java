package edu.uic.cs474.f21.a4;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.Random;

import static edu.uic.cs474.f21.a4.Assignment4.*;
import static edu.uic.cs474.f21.a4.Environment.*;
import static edu.uic.cs474.f21.a4.Expression.*;
import static edu.uic.cs474.f21.a4.Value.*;

public class Test09_FieldInitThis {

    @Test
    public void initializerShouldBeCalledPerInstantiation() {
        Assignment4 a4 = Assignment4.getSolution();

        int n = new Random().nextInt(100) + 1;

        // (let c = 0 in ...
        Expression test = new LetExpression(
                new Name("n"),
                new IntConstant(0),
                // ... Class Counter { int theCount = ++c; } ...
                new ClassDefExpression(
                        new Name("Counter"),
                        Optional.empty(),
                        new Field[] { new Field(
                                new Name("theCount"),
                                new IncExpression(new Name("n"))
                                ) },
                        new Method[] { },

                        // ... new Counter().theCount + new Counter().theCount + ...
                        new BinOpExpression(
                                BinOpExpression.Operator.PLUS,
                                new BinOpExpression(
                                        BinOpExpression.Operator.TIMES,
                                        generateNObjects(n, new Name("Counter"), new Name("theCount")),
                                        new IntConstant(0)
                                ),
                                new ReadFieldExpression(
                                        new NewExpression(new Name("Counter")),
                                        new Name("theCount")
                                )
                        )
                )
        );

        int expected = n+1;

        Assert.assertEquals(new IntValue(expected), a4.evaluate(test, Environment.EMPTY));
    }

    private Expression generateNObjects(int n, Name clName, Name fieldName) {
        Expression ret = new ReadFieldExpression(new NewExpression(clName), fieldName);

        for (int i = 0 ; i < n-1 ; i++) {
            ret = new BinOpExpression(
                    BinOpExpression.Operator.PLUS,
                    ret,
                    new ReadFieldExpression(new NewExpression(clName), fieldName)
            );
        }

        return ret;
    }

    @Test
    public void recursiveMethodsShouldWork() {
        Assignment4 a4 = Assignment4.getSolution();

        int c = new Random().nextInt(10) + 1;

        // class Factorializer { int fact(n) { if (n == 1) then 1 else n * this.fact(n-1) } }
        Expression test = new ClassDefExpression(
                new Name("Factorializer"),
                Optional.empty(),
                new Field[]{},
                new Method[]{
                        new Method(
                                new Name("fact"),
                                new Name[]{ new Name("n") },
                                new IfExpression(
                                        new EqExpression(
                                                new VariableExpression(new Name("n")),
                                                new IntConstant(1)
                                        ),
                                        new IntConstant(1),
                                        new BinOpExpression(
                                                BinOpExpression.Operator.TIMES,
                                                new VariableExpression(new Name("n")),
                                                new CallMethodExpression(
                                                        new VariableExpression(new Name("this")),
                                                        new Name("fact"),
                                                        new BinOpExpression(
                                                                BinOpExpression.Operator.MINUS,
                                                                new VariableExpression(new Name("n")),
                                                                new IntConstant(1)
                                                        )
                                                )
                                        )
                                )
                        )
                },
                // new Factorializer().fact(c)
                new CallMethodExpression(
                        new NewExpression(new Name("Factorializer")),
                        new Name("fact"),
                        new IntConstant(c)
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got the value of the field as the result
        Assert.assertEquals(new IntValue(javaFactorial(c)), v);
    }

    static int javaFactorial(int n) { return (n == 1) ? 1 : n * javaFactorial(n - 1); }
}
