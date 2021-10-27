package edu.uic.cs474.f21.a4;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.Random;

import static edu.uic.cs474.f21.a4.Assignment4.*;
import static edu.uic.cs474.f21.a4.Environment.*;
import static edu.uic.cs474.f21.a4.Expression.*;
import static edu.uic.cs474.f21.a4.Value.*;

public class Test08_InvokeInheritedMethod {
    @Test
    public void invokeInheritedMethod() {
        Assignment4 a4 = Assignment4.getSolution();

        int val1 = new Random().nextInt(10);
        int val2 = new Random().nextInt(10);
        int val3 = new Random().nextInt(10);

        // let f1 = (a) -> { a * 10 }
        Expression test =
                new LetExpression(
                        new Name("f1"),
                        new LambdaDeclExpression(
                                new Name[] { new Name("a") },
                                new BinOpExpression(
                                        BinOpExpression.Operator.TIMES,
                                        new VariableExpression(new Name("a")),
                                        new IntConstant(10)
                                )
                        ),
                        // Create a class A { add(n1,n2) { n1(n2) } }
                        new ClassDefExpression(
                                new Name("A"),
                                Optional.empty(),
                                new Field[]{},
                                new Method[]{
                                        new Method(
                                                new Name("m"),
                                                new Name[]{ new Name("n1"), new Name("n2")},
                                                new BinOpExpression(
                                                        BinOpExpression.Operator.PLUS,
                                                        new VariableExpression(new Name("n1")),
                                                        new VariableExpression(new Name("n2"))
                                                )
                                        ),
                                },

                                // Create a class B extends A { apply(f,arg) { f(arg) } }
                                new ClassDefExpression(
                                        new Name("B"),
                                        Optional.of(new Name("A")),
                                        new Field[]{},
                                        new Method[]{
                                                new Method(
                                                        new Name("apply"),
                                                        new Name[]{ new Name("f"), new Name("arg")},
                                                        new FirstClassFunctionCallExpression(
                                                                new VariableExpression(new Name("f")),
                                                                new Expression[] { new VariableExpression(new Name("arg")) }
                                                        )
                                                ),
                                        },
                                        // varB = new B()
                                        new LetExpression(
                                                new Name("varB"),
                                                new NewExpression(new Name("B")),
                                                // varB.m(val1, val2) + varB.m(f1, val3)
                                                new BinOpExpression(
                                                        BinOpExpression.Operator.PLUS,
                                                        new CallMethodExpression(
                                                                new VariableExpression(new Name("varB")),
                                                                new Name("m"),
                                                                new IntConstant(val1),
                                                                new IntConstant(val2)),
                                                        new CallMethodExpression(
                                                                new VariableExpression(new Name("varB")),
                                                                new Name("apply"),
                                                                new VariableExpression(new Name("f1")),
                                                                new IntConstant(val3))
                                                )
                                        )
                                )
                        )
                );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got the value of the field as the result
        Assert.assertEquals(new IntValue(val1 + val2 + (val3 * 10)), v);
    }

    @Test
    public void invokeOverridenMethod() {
        Assignment4 a4 = Assignment4.getSolution();

        int val1 = new Random().nextInt(10);
        int val2 = new Random().nextInt(10);

        // Create a class A { m(n1,n2) { n1 + n2 } }
        Expression test = new ClassDefExpression(
                new Name("A"),
                Optional.empty(),
                new Field[]{},
                new Method[]{
                        new Method(
                                new Name("m"),
                                new Name[]{ new Name("n1"), new Name("n2")},
                                new BinOpExpression(
                                        BinOpExpression.Operator.PLUS,
                                        new VariableExpression(new Name("n1")),
                                        new VariableExpression(new Name("n2"))
                                )
                        ),
                },

                // Create a class B extends A { m(n1,n2) { n1 * n2 } }
                new ClassDefExpression(
                        new Name("B"),
                        Optional.of(new Name("A")),
                        new Field[]{},
                        new Method[]{
                                new Method(
                                        new Name("m"),
                                        new Name[]{ new Name("n1"), new Name("n2")},
                                        new BinOpExpression(
                                                BinOpExpression.Operator.TIMES,
                                                new VariableExpression(new Name("n1")),
                                                new VariableExpression(new Name("n2"))
                                        )
                                ),
                        },
                        // new A().m(val1, val2) + new B().m(val1, val2)
                        new BinOpExpression(
                                BinOpExpression.Operator.PLUS,
                                new CallMethodExpression(
                                        new NewExpression(new Name("A")),
                                        new Name("m"),
                                        new IntConstant(val1),
                                        new IntConstant(val2)),
                                new CallMethodExpression(
                                        new NewExpression(new Name("B")),
                                        new Name("m"),
                                        new IntConstant(val1),
                                        new IntConstant(val2))
                        )
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got the value of the field as the result
        Assert.assertEquals(new IntValue((val1 + val2) + (val1 * val2)), v);
    }
}
