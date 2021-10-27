package edu.uic.cs474.f21.a4;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

import static edu.uic.cs474.f21.a4.Environment.*;
import static edu.uic.cs474.f21.a4.Assignment4.*;
import static edu.uic.cs474.f21.a4.Expression.*;
import static edu.uic.cs474.f21.a4.Value.*;

public class Test01_InstanceOfSingleClass {

    @Test
    public void intsAreNotObjects() {
        Assignment4 a4 = Assignment4.getSolution();

        // Create a class C
        // ( 1 instanceof C ) in Java
        Expression test = new ClassDefExpression(
                new Name("C"),
                Optional.empty(),
                new Field[]{},
                new Method[]{},

                // And check if an intvalue is an instance of that class
                new InstanceOfExpression(new Name("C"), new IntConstant(474))
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got false as the result
        Assert.assertEquals(new BoolValue(false), v);
    }

    @Test
    public void booleansAreNotObjects() {
        Assignment4 a4 = Assignment4.getSolution();

        // Create a class C
        Expression test = new ClassDefExpression(
                new Name("C"),
                Optional.empty(),
                new Field[]{},
                new Method[]{},

                // And check if a boolean is an instance of that class
                // ( (0 == 1) instanceof C ) in Java
                new InstanceOfExpression(
                        new Name("C"),
                        new EqExpression(
                                new IntConstant(1),
                                new IntConstant(0)
                        ))
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got false as the result
        Assert.assertEquals(new BoolValue(false), v);
    }

    @Test
    public void objectsAreObjects() {
        Assignment4 a4 = Assignment4.getSolution();

        // Create a class C
        Expression test = new ClassDefExpression(
                new Name("C"),
                Optional.empty(),
                new Field[]{},
                new Method[]{},

                // And check if an instance of class C is instanceof C
                // ( (new C()) instanceof C ) in Java
                new InstanceOfExpression(
                        new Name("C"),
                        new NewExpression(new Name("C"))
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got true as the result
        Assert.assertEquals(new BoolValue(true), v);
    }
}
