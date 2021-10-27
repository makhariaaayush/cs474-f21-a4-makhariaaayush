package edu.uic.cs474.f21.a4;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import static edu.uic.cs474.f21.a4.Assignment4.*;
import static edu.uic.cs474.f21.a4.Environment.*;
import static edu.uic.cs474.f21.a4.Expression.*;
import static edu.uic.cs474.f21.a4.Value.*;

public class Test05_ReadTwoFieldsOfTwoClasses {

    @Test
    public void readSingleFieldTwoClasses() {
        Assignment4 a4 = Assignment4.getSolution();

        int fieldValue1 = new Random().nextInt();
        int fieldValue2 = new Random().nextInt();

        // Create a class A { int f1 = fieldValue1; }
        Expression test = new ClassDefExpression(
                new Name("A"),
                Optional.empty(),
                new Field[]{new Field(new Name("f1"), new IntConstant(fieldValue1))},
                new Method[]{},

                // Create a class B { int f2 = fieldValue2; }
                new ClassDefExpression(
                        new Name("B"),
                        Optional.empty(),
                        new Field[]{new Field(new Name("f2"), new IntConstant(fieldValue2))},
                        new Method[]{},

                        // Create an instance of A (varA = new A())
                        new LetExpression(
                                new Name("varA"),
                                new NewExpression(new Name("A")),
                                // Create an instance of B (varB = new B())
                                new LetExpression(
                                        new Name("varB"),
                                        new NewExpression(new Name("B")),
                                        // (varA.f1 + varB.f2)
                                        new BinOpExpression(
                                                BinOpExpression.Operator.PLUS,
                                                new ReadFieldExpression(new VariableExpression(new Name("varA")), new Name("f1")),
                                                new ReadFieldExpression(new VariableExpression(new Name("varB")), new Name("f2"))
                                        )
                                )

                        )
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got the value of the field as the result
        Assert.assertEquals(new IntValue(fieldValue1 + fieldValue2), v);
    }

    @Test
    public void readTwoFieldsTwoClasses() {
        Assignment4 a4 = Assignment4.getSolution();

        int fieldValue1 = new Random().nextInt();
        int fieldValue2 = new Random().nextInt();
        int fieldValue3 = new Random().nextInt();

        // Create a class A { int f = fieldValue1; }
        Expression test = new ClassDefExpression(
                new Name("A"),
                Optional.empty(),
                new Field[]{new Field(new Name("f"), new IntConstant(fieldValue1))},
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
                                        // (varA.f + (varB.f + varB.m))
                                        new BinOpExpression(
                                                BinOpExpression.Operator.PLUS,
                                                new ReadFieldExpression(new VariableExpression(new Name("varA")), new Name("f")),
                                                new BinOpExpression(
                                                        BinOpExpression.Operator.PLUS,
                                                        new ReadFieldExpression(new VariableExpression(new Name("varB")), new Name("f")),
                                                        new ReadFieldExpression(new VariableExpression(new Name("varB")), new Name("m"))
                                                )
                                        )
                                )

                        )
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got the value of the field as the result
        Assert.assertEquals(new IntValue(fieldValue1 + fieldValue2 + fieldValue3), v);
    }

    @Test
    public void readTwoFieldsTwoClassesComplexExpression() {
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
                                        // (if varA.f == (varB.f - 2) then varA else varB).f
                                        new ReadFieldExpression(
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
                                                new Name("f")
                                        )
                                )

                        )
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got the value of the field as the result
        Assert.assertEquals(new IntValue((fieldValue1 == (fieldValue2 - 2)) ? fieldValue1 : fieldValue2), v);
    }

    @Test
    public void readTwoFieldsTwoClassesComplexExpressionNoLuck() {
        for (int i = 0 ; i < 100 ; i++)
            readTwoFieldsTwoClassesComplexExpression();
    }

    @Test
    public void readSingleFieldTwoClassesNoHardcode() {
        Assignment4 a4 = Assignment4.getSolution();

        int fieldValue1 = new Random().nextInt();
        int fieldValue2 = new Random().nextInt();

        String aName = "";
        String bName = "";
        String f1Name = "";
        String f2Name = "";
        String varAName = "";
        String varBName = "";

        while (Stream.of(aName, bName, f1Name, f2Name, varAName, varBName).distinct().count() != 6) {
            aName    = Test02_InstanceOfTwoClasses.generateRandomString(4);
            bName    = Test02_InstanceOfTwoClasses.generateRandomString(4);
            f1Name   = Test02_InstanceOfTwoClasses.generateRandomString(4);
            f2Name   = Test02_InstanceOfTwoClasses.generateRandomString(4);
            varAName = Test02_InstanceOfTwoClasses.generateRandomString(4);
            varBName = Test02_InstanceOfTwoClasses.generateRandomString(4);
        }

        // Create a class A { int f1 = fieldValue1; }
        Expression test = new ClassDefExpression(
                new Name(aName),
                Optional.empty(),
                new Field[]{new Field(new Name(f1Name), new IntConstant(fieldValue1))},
                new Method[]{},

                // Create a class B { int f2 = fieldValue2; }
                new ClassDefExpression(
                        new Name(bName),
                        Optional.empty(),
                        new Field[]{new Field(new Name(f2Name), new IntConstant(fieldValue2))},
                        new Method[]{},

                        // Create an instance of A (varA = new A())
                        new LetExpression(
                                new Name(varAName),
                                new NewExpression(new Name(aName)),
                                // Create an instance of B (varB = new B())
                                new LetExpression(
                                        new Name(varBName),
                                        new NewExpression(new Name(bName)),
                                        // (varA.f1 + varB.f2)
                                        new BinOpExpression(
                                                BinOpExpression.Operator.PLUS,
                                                new ReadFieldExpression(new VariableExpression(new Name(varAName)), new Name(f1Name)),
                                                new ReadFieldExpression(new VariableExpression(new Name(varBName)), new Name(f2Name))
                                        )
                                )

                        )
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got the value of the field as the result
        Assert.assertEquals(new IntValue(fieldValue1 + fieldValue2), v);
    }

    @Test
    public void readTwoFieldsTwoClassesNoHardcode() {
        Assignment4 a4 = Assignment4.getSolution();

        int fieldValue1 = new Random().nextInt();
        int fieldValue2 = new Random().nextInt();
        int fieldValue3 = new Random().nextInt();

        String aName = "";
        String bName = "";
        String f1Name = "";
        String f2Name = "";
        String varAName = "";
        String varBName = "";

        while (Stream.of(aName, bName, f1Name, f2Name, varAName, varBName).distinct().count() != 6) {
            aName    = Test02_InstanceOfTwoClasses.generateRandomString(4);
            bName    = Test02_InstanceOfTwoClasses.generateRandomString(4);
            f1Name   = Test02_InstanceOfTwoClasses.generateRandomString(4);
            f2Name   = Test02_InstanceOfTwoClasses.generateRandomString(4);
            varAName = Test02_InstanceOfTwoClasses.generateRandomString(4);
            varBName = Test02_InstanceOfTwoClasses.generateRandomString(4);
        }

        // Create a class A { int f = fieldValue1; }
        Expression test = new ClassDefExpression(
                new Name(aName),
                Optional.empty(),
                new Field[]{new Field(new Name(f1Name), new IntConstant(fieldValue1))},
                new Method[]{},

                // Create an instance of A:  obj = new A();
                // Create a class B { int f = fieldValue2; int m = fieldValue3; }
                new ClassDefExpression(
                        new Name(bName),
                        Optional.empty(),
                        new Field[]{
                                new Field(new Name(f1Name), new IntConstant(fieldValue2)),
                                new Field(new Name(f2Name), new IntConstant(fieldValue3))},
                        new Method[]{},

                        // Create an instance of A (varA = new A())
                        new LetExpression(
                                new Name(varAName),
                                new NewExpression(new Name(aName)),
                                // Create an instance of B (varB = new B())
                                new LetExpression(
                                        new Name(varBName),
                                        new NewExpression(new Name(bName)),
                                        // (varA.f + (varB.f + varB.m))
                                        new BinOpExpression(
                                                BinOpExpression.Operator.PLUS,
                                                new ReadFieldExpression(new VariableExpression(new Name(varAName)), new Name(f1Name)),
                                                new BinOpExpression(
                                                        BinOpExpression.Operator.PLUS,
                                                        new ReadFieldExpression(new VariableExpression(new Name(varBName)), new Name(f1Name)),
                                                        new ReadFieldExpression(new VariableExpression(new Name(varBName)), new Name(f2Name))
                                                )
                                        )
                                )

                        )
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got the value of the field as the result
        Assert.assertEquals(new IntValue(fieldValue1 + fieldValue2 + fieldValue3), v);
    }

    @Test
    public void readTwoFieldsTwoClassesComplexExpressionNoHardcode() {
        Assignment4 a4 = Assignment4.getSolution();

        int fieldValue1 = new Random().nextInt(10);
        int fieldValue2 = new Random().nextInt(10);
        int fieldValue3 = new Random().nextInt(10);

        String aName = "";
        String bName = "";
        String f1Name = "";
        String f2Name = "";
        String varAName = "";
        String varBName = "";

        while (Stream.of(aName, bName, f1Name, f2Name, varAName, varBName).distinct().count() != 6) {
            aName    = Test02_InstanceOfTwoClasses.generateRandomString(4);
            bName    = Test02_InstanceOfTwoClasses.generateRandomString(4);
            f1Name   = Test02_InstanceOfTwoClasses.generateRandomString(4);
            f2Name   = Test02_InstanceOfTwoClasses.generateRandomString(4);
            varAName = Test02_InstanceOfTwoClasses.generateRandomString(4);
            varBName = Test02_InstanceOfTwoClasses.generateRandomString(4);
        }

        // Create a class A { int f = fieldValue1; }
        Expression test = new ClassDefExpression(
                new Name(aName),
                Optional.empty(),
                new Field[]{ new Field(new Name(f1Name), new IntConstant(fieldValue1))},
                new Method[]{},

                // Create an instance of A:  obj = new A();
                // Create a class B { int f = fieldValue2; int m = fieldValue3; }
                new ClassDefExpression(
                        new Name(bName),
                        Optional.empty(),
                        new Field[]{
                                new Field(new Name(f1Name), new IntConstant(fieldValue2)),
                                new Field(new Name(f2Name), new IntConstant(fieldValue3))},
                        new Method[]{},

                        // Create an instance of A (varA = new A())
                        new LetExpression(
                                new Name(varAName),
                                new NewExpression(new Name(aName)),
                                // Create an instance of B (varB = new B())
                                new LetExpression(
                                        new Name(varBName),
                                        new NewExpression(new Name(bName)),
                                        // (if varA.f == (varB.f - 2) then varA else varB).f
                                        new ReadFieldExpression(
                                                new IfExpression(
                                                        new EqExpression(
                                                                new ReadFieldExpression(new VariableExpression(new Name(varAName)), new Name(f1Name)),
                                                                new BinOpExpression(
                                                                        BinOpExpression.Operator.MINUS,
                                                                        new ReadFieldExpression(new VariableExpression(new Name(varBName)), new Name(f1Name)),
                                                                        new IntConstant(2)
                                                                )
                                                        ),
                                                        new VariableExpression(new Name(varAName)),
                                                        new VariableExpression(new Name(varBName))
                                                ),
                                                new Name(f1Name)
                                        )
                                )

                        )
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got the value of the field as the result
        Assert.assertEquals(new IntValue((fieldValue1 == (fieldValue2 - 2)) ? fieldValue1 : fieldValue2), v);
    }

    @Test
    public void readTwoFieldsTwoClassesComplexExpressionNoLuckNoHardcode() {
        for (int i = 0 ; i < 100 ; i++)
            readTwoFieldsTwoClassesComplexExpressionNoHardcode();
    }
}
