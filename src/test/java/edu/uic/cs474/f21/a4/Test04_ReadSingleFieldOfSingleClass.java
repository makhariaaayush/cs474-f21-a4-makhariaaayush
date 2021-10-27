package edu.uic.cs474.f21.a4;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

import static edu.uic.cs474.f21.a4.Assignment4.*;
import static edu.uic.cs474.f21.a4.Environment.*;
import static edu.uic.cs474.f21.a4.Expression.*;
import static edu.uic.cs474.f21.a4.Value.*;

public class Test04_ReadSingleFieldOfSingleClass {
    @Test
    public void readSingleFieldOnSingleClass() {
        Assignment4 a4 = Assignment4.getSolution();

        int fieldValue = new Random().nextInt();

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
                        // Read the value of the field (obj.f)
                        new ReadFieldExpression(
                                new VariableExpression(new Name("obj")),
                                new Name("f")
                        )
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got the value of the field as the result
        Assert.assertEquals(new IntValue(fieldValue), v);
    }

    @Test
    public void readSingleFieldOnSingleClassWithComplexInitializer() {
        Assignment4 a4 = Assignment4.getSolution();

        int fieldValue  = new Random().nextInt();
        int fieldValue2 = new Random().nextInt(2);
        int fieldValue3 = new Random().nextInt();
        int fieldValue4 = new Random().nextInt(2);

        // Let coin1 = fieldValue2
        Expression test = new LetExpression(
                new Name("coin1"),
                new IntConstant(fieldValue2),
                // Let coin2 = fieldValue4
                new LetExpression(
                        new Name("coin2"),
                        new IntConstant(fieldValue4),
                        // Create a class A { int f = (if coin == 1 then fieldValue else fieldValue3); }
                        new ClassDefExpression(
                                new Name("A"),
                                Optional.empty(),
                                new Field[]{
                                        new Field(new Name("f"), new IfExpression(
                                                new EqExpression(
                                                        new VariableExpression(new Name("coin2")),
                                                        new IntConstant(1)
                                                ),
                                                new IntConstant(fieldValue),
                                                new IntConstant(fieldValue3)
                                        ))
                                },
                                new Method[]{},

                                // Create an instance of A:  obj = new A();
                                new LetExpression(
                                        new Name("obj"),
                                        new NewExpression(new Name("A")),
                                        // Read the value of the field (obj.f)
                                        new ReadFieldExpression(
                                                new VariableExpression(new Name("obj")),
                                                new Name("f")
                                        )
                                )
                        )
                )

        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got the value of the field as the result
        Assert.assertEquals(new IntValue((fieldValue4 == 1 ? fieldValue : fieldValue3)), v);
    }

    @Test
    public void readSingleFieldOnSingleClassWithComplexInitializerNoLuck() {
       for (int i = 0 ; i < 100 ; i++)
           readSingleFieldOnSingleClassWithComplexInitializer();
    }

    @Test
    public void readSingleFieldOnSingleClassNoHardcode() {
        Assignment4 a4 = Assignment4.getSolution();

        int fieldValue = new Random().nextInt();

        String aName = "";
        String bName = "";
        String cName = "";

        // Generate different names
        while (Stream.of(aName, bName, cName).distinct().count() != 3) {
            aName = Test02_InstanceOfTwoClasses.generateRandomString(4);
            bName = Test02_InstanceOfTwoClasses.generateRandomString(4);
            cName = Test02_InstanceOfTwoClasses.generateRandomString(4);
        }


        // Create a class A { int f = fieldValue; }
        Expression test = new ClassDefExpression(
                new Name(aName),
                Optional.empty(),
                new Field[]{new Field(new Name(bName), new IntConstant(fieldValue))},
                new Method[]{},

                // Create an instance of A:  obj = new A();
                new LetExpression(
                        new Name(cName),
                        new NewExpression(new Name(aName)),
                        // Read the value of the field (obj.f)
                        new ReadFieldExpression(
                                new VariableExpression(new Name(cName)),
                                new Name(bName)
                        )
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got the value of the field as the result
        Assert.assertEquals(new IntValue(fieldValue), v);
    }

    @Test
    public void readSingleFieldOnSingleClassWithComplexInitializerNoHardcode() {
        Assignment4 a4 = Assignment4.getSolution();

        int fieldValue  = new Random().nextInt();
        int fieldValue2 = new Random().nextInt(2);
        int fieldValue3 = new Random().nextInt();
        int fieldValue4 = new Random().nextInt(2);

        String aName = "";
        String bName = "";
        String cName = "";
        String dName = "";
        String fName = "";

        // Generate different names
        while (Stream.of(aName, bName, cName, dName, fName).distinct().count() != 5) {
            aName = Test02_InstanceOfTwoClasses.generateRandomString(4);
            bName = Test02_InstanceOfTwoClasses.generateRandomString(4);
            cName = Test02_InstanceOfTwoClasses.generateRandomString(4);
            dName = Test02_InstanceOfTwoClasses.generateRandomString(4);
            fName = Test02_InstanceOfTwoClasses.generateRandomString(4);
        }

        // Let coin1 = fieldValue2
        Expression test = new LetExpression(
                new Name(aName),
                new IntConstant(fieldValue2),
                // Let coin2 = fieldValue4
                new LetExpression(
                        new Name(bName),
                        new IntConstant(fieldValue4),
                        // Create a class A { int f = (if coin == 1 then fieldValue else fieldValue3); }
                        new ClassDefExpression(
                                new Name(cName),
                                Optional.empty(),
                                new Field[]{
                                        new Field(new Name(dName), new IfExpression(
                                                new EqExpression(
                                                        new VariableExpression(new Name(bName)),
                                                        new IntConstant(1)
                                                ),
                                                new IntConstant(fieldValue),
                                                new IntConstant(fieldValue3)
                                        ))
                                },
                                new Method[]{},

                                // Create an instance of A:  obj = new A();
                                new LetExpression(
                                        new Name(fName),
                                        new NewExpression(new Name(cName)),
                                        // Read the value of the field (obj.f)
                                        new ReadFieldExpression(
                                                new VariableExpression(new Name(fName)),
                                                new Name(dName)
                                        )
                                )
                        )
                )

        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got the value of the field as the result
        Assert.assertEquals(new IntValue((fieldValue4 == 1 ? fieldValue : fieldValue3)), v);
    }

    @Test
    public void readSingleFieldOnSingleClassWithComplexInitializerNoLuckNoHardcode() {
        for (int i = 0 ; i < 100 ; i++)
            readSingleFieldOnSingleClassWithComplexInitializerNoHardcode();
    }

}
