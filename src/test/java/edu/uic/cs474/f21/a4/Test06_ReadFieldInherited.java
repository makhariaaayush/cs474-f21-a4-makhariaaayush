package edu.uic.cs474.f21.a4;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.Random;

import static edu.uic.cs474.f21.a4.Assignment4.*;
import static edu.uic.cs474.f21.a4.Environment.*;
import static edu.uic.cs474.f21.a4.Expression.*;
import static edu.uic.cs474.f21.a4.Value.*;

public class Test06_ReadFieldInherited {

    @Test
    public void readInheritedField() {
        Assignment4 a4 = Assignment4.getSolution();

        int fieldValue1 = new Random().nextInt(10);

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

                        // new B().f
                        new ReadFieldExpression(
                                new NewExpression(new Name("B")),
                                new Name("f")
                        )
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got the value of the field as the result
        Assert.assertEquals(new IntValue(fieldValue1), v);
    }


}
