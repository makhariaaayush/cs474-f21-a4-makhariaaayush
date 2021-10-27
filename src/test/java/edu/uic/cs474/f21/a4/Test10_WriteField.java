package edu.uic.cs474.f21.a4;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.Random;

import static edu.uic.cs474.f21.a4.Assignment4.*;
import static edu.uic.cs474.f21.a4.Environment.*;
import static edu.uic.cs474.f21.a4.Expression.*;
import static edu.uic.cs474.f21.a4.Value.*;

public class Test10_WriteField {
    @Test
    public void writeSingleFieldOnSingleClass() {
        Assignment4 a4 = Assignment4.getSolution();

        int fieldValue = new Random().nextInt();
        int updatedValue = new Random().nextInt();

        // Create a class A { int f = fieldValue; }
        Expression test = new ClassDefExpression(
                new Name("A"),
                Optional.empty(),
                new Field[]{new Field(new Name("f"), new IntConstant(fieldValue))},
                new Method[]{},

                // Create an instance of A:  obj = new A();
                new LetExpression(
                        new Name("obj"),
                        new NewExpression(new Name("A")),
                        // (a.f + (a.f = updatedValue) + a.f)
                        new BinOpExpression(
                                BinOpExpression.Operator.PLUS,
                                new ReadFieldExpression(new VariableExpression(new Name("obj")), new Name("f")),
                                new BinOpExpression(
                                        BinOpExpression.Operator.PLUS,
                                        new WriteFieldExpression(new VariableExpression(new Name("obj")), new Name("f"), new IntConstant(updatedValue)),
                                        new ReadFieldExpression(new VariableExpression(new Name("obj")), new Name("f"))
                                )
                        )
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got the value of the field as the result
        Assert.assertEquals(new IntValue(fieldValue + updatedValue + updatedValue), v);
    }

    @Test
    public void writeTwoInstancesOfSameClass() {
        Assignment4 a4 = Assignment4.getSolution();

        int fieldValue = new Random().nextInt();
        int updatedValue = 0;

        while (fieldValue == updatedValue) {
            updatedValue = new Random().nextInt(10);
        }

        // Create a class A { int f = fieldValue; }
        Expression test = new ClassDefExpression(
                new Name("A"),
                Optional.empty(),
                new Field[]{new Field(new Name("f"), new IntConstant(fieldValue))},
                new Method[]{},

                // Create an instance of A:  a1 = new A();
                new LetExpression(
                        new Name("a1"),
                        new NewExpression(new Name("A")),
                        // Create an instance of A:  a2 = new A();
                        new LetExpression(
                                new Name("a2"),
                                new NewExpression(new Name("A")),
                                // (a2.f + (a2.f = updatedValue) + a2.f + a1.f)
                                new BinOpExpression(
                                        BinOpExpression.Operator.PLUS,
                                        new ReadFieldExpression(new VariableExpression(new Name("a2")), new Name("f")),
                                        new BinOpExpression(
                                                BinOpExpression.Operator.PLUS,
                                                new WriteFieldExpression(new VariableExpression(new Name("a2")), new Name("f"), new IntConstant(updatedValue)),
                                                new BinOpExpression(
                                                        BinOpExpression.Operator.PLUS,
                                                        new ReadFieldExpression(new VariableExpression(new Name("a2")), new Name("f")),
                                                        new ReadFieldExpression(new VariableExpression(new Name("a1")), new Name("f"))
                                                )

                                        )
                                )
                        )
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got the value of the field as the result
        Assert.assertEquals(new IntValue(fieldValue + updatedValue + updatedValue + fieldValue), v);
    }

    @Test
    public void writeSingleFieldTwoClasses() {
        Assignment4 a4 = Assignment4.getSolution();

        int fieldValue1 = new Random().nextInt(10);
        int fieldValue2 = new Random().nextInt(10);

        int updatedValue1 = new Random().nextInt(10);
        int updatedValue2 = new Random().nextInt(10);

        // Create a class A { int f1 = fieldValue1; }
        Expression test = new ClassDefExpression(
                new Name("A"),
                Optional.empty(),
                new Field[]{new Field(new Name("f"), new IntConstant(fieldValue1))},
                new Method[]{},

                // Create a class B { int f2 = fieldValue2; }
                new ClassDefExpression(
                        new Name("B"),
                        Optional.empty(),
                        new Field[]{new Field(new Name("f"), new IntConstant(fieldValue2))},
                        new Method[]{},

                        // Create an instance of A (varA = new A())
                        new LetExpression(
                                new Name("varA"),
                                new NewExpression(new Name("A")),
                                // Create an instance of B (varB = new B())
                                new LetExpression(
                                        new Name("varB"),
                                        new NewExpression(new Name("B")),
                                        // (varA.f + (varA.f = updatedValue1) + varB.f + (varB.f = updatedValue2) + varA.f + varB.f)
                                        new BinOpExpression(
                                                BinOpExpression.Operator.PLUS,
                                                new ReadFieldExpression(new VariableExpression(new Name("varA")), new Name("f")),
                                                new BinOpExpression(
                                                        BinOpExpression.Operator.PLUS,
                                                        new WriteFieldExpression(new VariableExpression(new Name("varA")), new Name("f"), new IntConstant(updatedValue1)),
                                                        new BinOpExpression(
                                                                BinOpExpression.Operator.PLUS,
                                                                new ReadFieldExpression(new VariableExpression(new Name("varB")), new Name("f")),
                                                                new BinOpExpression(
                                                                        BinOpExpression.Operator.PLUS,
                                                                        new WriteFieldExpression(new VariableExpression(new Name("varB")), new Name("f"), new IntConstant(updatedValue2)),
                                                                        new BinOpExpression(
                                                                                BinOpExpression.Operator.PLUS,
                                                                                new ReadFieldExpression(new VariableExpression(new Name("varA")), new Name("f")),
                                                                                new ReadFieldExpression(new VariableExpression(new Name("varB")), new Name("f"))
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )

                        )
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got the value of the field as the result
        Assert.assertEquals(new IntValue(fieldValue1 + updatedValue1 + updatedValue1 + fieldValue2 + updatedValue2 + updatedValue2), v);
    }

    @Test
    public void writeSingleTwoFieldsTwoClasses() {
        Assignment4 a4 = Assignment4.getSolution();

        int fieldValue1 = new Random().nextInt(10);
        int fieldValue2 = new Random().nextInt(10);
        int fieldValue3 = new Random().nextInt(10);

        int updatedValue1 = new Random().nextInt(10);
        int updatedValue2 = new Random().nextInt(10);
        int updatedValue3 = new Random().nextInt(10);

        // Create a class A { int f = fieldValue1; }
        Expression test = new ClassDefExpression(
                new Name("A"),
                Optional.empty(),
                new Field[]{new Field(new Name("f"), new IntConstant(fieldValue1))},
                new Method[]{},

                // Create a class B { int f = fieldValue2; int m = fieldValue3; }
                new ClassDefExpression(
                        new Name("B"),
                        Optional.empty(),
                        new Field[]{
                                new Field(new Name("f"), new IntConstant(fieldValue2)),
                                new Field(new Name("m"), new IntConstant(fieldValue3))
                        },
                        new Method[]{},

                        // Create an instance of A (varA = new A())
                        new LetExpression(
                                new Name("varA"),
                                new NewExpression(new Name("A")),
                                // Create an instance of B (varB = new B())
                                new LetExpression(
                                        new Name("varB"),
                                        new NewExpression(new Name("B")),
                                        // (varA.f + (varA.f = updatedValue1) + varB.f + (varB.f = updatedValue2) + varA.f + varB.f + varB.m + (varB.m = updatedValue3) + varB.m)
                                        new BinOpExpression(
                                                BinOpExpression.Operator.PLUS,
                                                new ReadFieldExpression(new VariableExpression(new Name("varA")), new Name("f")),
                                                new BinOpExpression(
                                                        BinOpExpression.Operator.PLUS,
                                                        new WriteFieldExpression(new VariableExpression(new Name("varA")), new Name("f"), new IntConstant(updatedValue1)),
                                                        new BinOpExpression(
                                                                BinOpExpression.Operator.PLUS,
                                                                new ReadFieldExpression(new VariableExpression(new Name("varB")), new Name("f")),
                                                                new BinOpExpression(
                                                                        BinOpExpression.Operator.PLUS,
                                                                        new WriteFieldExpression(new VariableExpression(new Name("varB")), new Name("f"), new IntConstant(updatedValue2)),
                                                                        new BinOpExpression(
                                                                                BinOpExpression.Operator.PLUS,
                                                                                new ReadFieldExpression(new VariableExpression(new Name("varA")), new Name("f")),
                                                                                new BinOpExpression(
                                                                                        BinOpExpression.Operator.PLUS,
                                                                                        new ReadFieldExpression(new VariableExpression(new Name("varB")), new Name("f")),
                                                                                        new BinOpExpression(
                                                                                                BinOpExpression.Operator.PLUS,
                                                                                                new ReadFieldExpression(new VariableExpression(new Name("varB")), new Name("m")),
                                                                                                new BinOpExpression(
                                                                                                        BinOpExpression.Operator.PLUS,
                                                                                                        new WriteFieldExpression(new VariableExpression(new Name("varB")), new Name("m"), new IntConstant(updatedValue3)),
                                                                                                        new ReadFieldExpression(new VariableExpression(new Name("varB")), new Name("m"))
                                                                                                )
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )

                        )
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got the value of the field as the result
        Assert.assertEquals(new IntValue(fieldValue1 + updatedValue1 + updatedValue1 + fieldValue2 + updatedValue2 + updatedValue2 + fieldValue3 + updatedValue3 + updatedValue3), v);
    }

    @Test
    public void writeTwoFieldsTwoClassesComplexExpression() {
        Assignment4 a4 = Assignment4.getSolution();

        int fieldValue1 = new Random().nextInt(10);
        int fieldValue2 = new Random().nextInt(10);
        int fieldValue3 = new Random().nextInt(10);

        // Create a class A { int f = fieldValue1; }
        Expression test = new ClassDefExpression(
                new Name("A"),
                Optional.empty(),
                new Field[]{ new Field(new Name("f"), new IntConstant(fieldValue1))},
                new Method[]{},

                // Create an instance of A:  obj = new A();
                // Create a class B { int f = fieldValue2; int m = fieldValue3; }
                new ClassDefExpression(
                        new Name("B"),
                        Optional.empty(),
                        new Field[]{
                                new Field(new Name("f"), new IntConstant(fieldValue2)),
                                new Field(new Name("m"), new IntConstant(fieldValue3))},
                        new Method[]{},

                        // Create an instance of A (varA = new A())
                        new LetExpression(
                                new Name("varA"),
                                new NewExpression(new Name("A")),
                                // Create an instance of B (varB = new B())
                                new LetExpression(
                                        new Name("varB"),
                                        new NewExpression(new Name("B")),
                                        // (if varA.f == (varB.f - 2) then varA else varB).f = (if 5 == (varA.f + varB.m) then fieldValue1 else fieldValue2) + varA.f + varB.f
                                        new BinOpExpression(
                                                BinOpExpression.Operator.PLUS,
                                                new WriteFieldExpression(
                                                        new IfExpression(
                                                                new EqExpression(
                                                                        new ReadFieldExpression(new VariableExpression(new Name("varA")), new Name("f")),
                                                                        new BinOpExpression(
                                                                                BinOpExpression.Operator.MINUS,
                                                                                new ReadFieldExpression(new VariableExpression(new Name("varB")), new Name("f")),
                                                                                new IntConstant(2)
                                                                        )
                                                                ),
                                                                new VariableExpression(new Name("varA")),
                                                                new VariableExpression(new Name("varB"))
                                                        ),
                                                        new Name("f"),
                                                        new IfExpression(
                                                                new EqExpression(
                                                                        new IntConstant(5),
                                                                        new BinOpExpression(
                                                                                BinOpExpression.Operator.PLUS,
                                                                                new ReadFieldExpression(new VariableExpression(new Name("varA")), new Name("f")),
                                                                                new ReadFieldExpression(new VariableExpression(new Name("varB")), new Name("m"))
                                                                        )
                                                                ),
                                                                new IntConstant(fieldValue1),
                                                                new IntConstant(fieldValue2)
                                                        )
                                                ),
                                                new BinOpExpression(
                                                        BinOpExpression.Operator.PLUS,
                                                        new ReadFieldExpression(new VariableExpression(new Name("varA")), new Name("f")),
                                                        new ReadFieldExpression(new VariableExpression(new Name("varB")), new Name("f"))
                                                )
                                        )
                                )

                        )
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got the value of the field as the result
        // (if varA.f == (varB.f - 2) then varA else varB).f = (if 5 == (varA.f + varB.f) then fieldValue1 else fieldValue2) + varA.f + varB.f
        int updatedField1, updatedField2, writeValue;
        if (fieldValue1 == fieldValue2 - 2) {
            updatedField1 = (((fieldValue1 + fieldValue3) == 5) ? fieldValue1 : fieldValue2);
            updatedField2 = fieldValue2;
            writeValue = updatedField1;
        } else {
            updatedField1 = fieldValue1;
            updatedField2 = (((fieldValue1 + fieldValue3) == 5) ? fieldValue1 : fieldValue2);
            writeValue = updatedField2;
        }
        Assert.assertEquals(new IntValue(writeValue + updatedField1 + updatedField2), v);
    }

    @Test
    public void writeTwoFieldsTwoClassesComplexExpressionNoLuck() {
        for (int i = 0 ; i < 500 ; i++)
            writeTwoFieldsTwoClassesComplexExpression();
    }

    @Test
    public void writeInheritedField() {
        Assignment4 a4 = Assignment4.getSolution();

        int fieldValue1   = new Random().nextInt(10);
        int updatedValue1 = new Random().nextInt(10);

        // Create a class A { int f = fieldValue1; }
        Expression test = new ClassDefExpression(
                new Name("A"),
                Optional.empty(),
                new Field[]{new Field(new Name("f"), new IntConstant(fieldValue1))},
                new Method[]{},

                // Create a class B extends A { }
                new ClassDefExpression(
                        new Name("B"),
                        Optional.of(new Name("A")),
                        new Field[]{},
                        new Method[]{},

                        // varB = new B();
                        new LetExpression(
                                new Name("varB"),
                                new NewExpression(new Name("B")),
                                // (varB.f = updatedValue1) + varB.f
                                new BinOpExpression(
                                        BinOpExpression.Operator.PLUS,
                                        new WriteFieldExpression(new VariableExpression(new Name("varB")), new Name("f"), new IntConstant(updatedValue1)),
                                        new ReadFieldExpression(new VariableExpression(new Name("varB")), new Name("f"))
                                )
                        )
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got the value of the field as the result
        Assert.assertEquals(new IntValue(updatedValue1 + updatedValue1), v);
    }

    @Test
    public void writeSingleInheritedFieldTwoClasses() {
        Assignment4 a4 = Assignment4.getSolution();

        int fieldValue1 = new Random().nextInt(10);
        int fieldValue2 = new Random().nextInt(10);

        int updatedValue1 = new Random().nextInt(10);
        int updatedValue2 = new Random().nextInt(10);

        // Create a class A { int fa = fieldValue1; }
        Expression test = new ClassDefExpression(
                new Name("A"),
                Optional.empty(),
                new Field[]{new Field(new Name("fa"), new IntConstant(fieldValue1))},
                new Method[]{},

                // Create a class B extends A { int fb = fieldValue2; }
                new ClassDefExpression(
                        new Name("B"),
                        Optional.of(new Name("A")),
                        new Field[]{new Field(new Name("fb"), new IntConstant(fieldValue2))},
                        new Method[]{},

                        // Create an instance of B (varB = new B())
                        new LetExpression(
                                new Name("varB"),
                                new NewExpression(new Name("B")),
                                // (varB.fa + (varB.fa = updatedValue1) + varB.fb + (varB.fb = updatedValue2) + varB.fa + varB.fb)
                                new BinOpExpression(
                                        BinOpExpression.Operator.PLUS,
                                        new ReadFieldExpression(new VariableExpression(new Name("varB")), new Name("fa")),
                                        new BinOpExpression(
                                                BinOpExpression.Operator.PLUS,
                                                new WriteFieldExpression(new VariableExpression(new Name("varB")), new Name("fa"), new IntConstant(updatedValue1)),
                                                new BinOpExpression(
                                                        BinOpExpression.Operator.PLUS,
                                                        new ReadFieldExpression(new VariableExpression(new Name("varB")), new Name("fb")),
                                                        new BinOpExpression(
                                                                BinOpExpression.Operator.PLUS,
                                                                new WriteFieldExpression(new VariableExpression(new Name("varB")), new Name("fb"), new IntConstant(updatedValue2)),
                                                                new BinOpExpression(
                                                                        BinOpExpression.Operator.PLUS,
                                                                        new ReadFieldExpression(new VariableExpression(new Name("varB")), new Name("fa")),
                                                                        new ReadFieldExpression(new VariableExpression(new Name("varB")), new Name("fb"))
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got the value of the field as the result
        Assert.assertEquals(new IntValue(fieldValue1 + updatedValue1 + updatedValue1 + fieldValue2 + updatedValue2 + updatedValue2), v);
    }

    @Test
    public void counterUsesObjectState() {
        Assignment4 a4 = Assignment4.getSolution();

        int increments = new Random().nextInt(100);

        // Create a long sequence of obj.inc(1) - obj.inc(1) - obj.inc(1) - ... + obj.get() + obj.get()
        // Which will add up to the value in the counter in the end

        Expression getCall = new CallMethodExpression(new VariableExpression(new Name("obj")), new Name("get"));
        Expression incCall = new CallMethodExpression(new VariableExpression(new Name("obj")), new Name("inc"), new IntConstant(1));

//        Expression calls = new BinOpExpression(BinOpExpression.Operator.PLUS, getCall, getCall);
        Expression calls = incCall;

        for (int i = 0 ; i < increments ; i++) {
            calls = new BinOpExpression(
                    BinOpExpression.Operator.PLUS,
                    new BinOpExpression(
                            BinOpExpression.Operator.MINUS,
                            incCall,
                            new IntConstant(increments - i)
                    ),
                    calls
            );
        }

        // Create a class Counter { int n = 0; inc(howMuch) { this.n = this.n + howMuch } ; get() { this.n; } }
        Expression test = new ClassDefExpression(
                new Name("Counter"),
                Optional.empty(),
                new Field[]{ new Field(new Name("n"), new IntConstant(0)) },
                new Method[]{
                        new Method(
                                new Name("inc"),
                                new Name[]{ new Name("howMuch") },
                                new WriteFieldExpression(
                                        new VariableExpression(new Name("this")),
                                        new Name("n"),
                                        new BinOpExpression(
                                                BinOpExpression.Operator.PLUS,
                                                new ReadFieldExpression(new VariableExpression(new Name("this")), new Name("n")),
                                                new VariableExpression(new Name("howMuch"))
                                        )
                                )
                        ),
                        new Method(
                                new Name("get"),
                                new Name[]{},
                                new ReadFieldExpression( new VariableExpression(new Name("this")), new Name("n"))
                        )
                },
                // obj = new Counter()
                new LetExpression(
                        new Name("obj"),
                        new NewExpression(new Name("Counter")),
                        calls
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got the value of the field as the result
        Assert.assertEquals(new IntValue(increments+1), v);
    }

}
