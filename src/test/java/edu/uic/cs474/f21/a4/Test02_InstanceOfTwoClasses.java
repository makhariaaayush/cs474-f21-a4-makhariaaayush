package edu.uic.cs474.f21.a4;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.Random;

import static edu.uic.cs474.f21.a4.Assignment4.*;
import static edu.uic.cs474.f21.a4.Environment.*;
import static edu.uic.cs474.f21.a4.Expression.*;
import static edu.uic.cs474.f21.a4.Value.*;

public class Test02_InstanceOfTwoClasses {

    @Test
    public void asAreAs() {
        Assignment4 a4 = Assignment4.getSolution();

        // Create a class A
        Expression test = new ClassDefExpression(
                new Name("A"),
                Optional.empty(),
                new Field[]{},
                new Method[]{},

                // Create a class B
                new ClassDefExpression(
                        new Name("B"),
                        Optional.empty(),
                        new Field[]{},
                        new Method[]{},

                        // And check if an instance of class A is instanceof A
                        // ( (new A()) instanceof A ) in Java
                        new InstanceOfExpression(
                                new Name("A"),
                                new NewExpression(new Name("A"))
                        )
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got true as the result
        Assert.assertEquals(new BoolValue(true), v);
    }

    @Test
    public void asAreNotBs() {
        Assignment4 a4 = Assignment4.getSolution();

        // Create a class A
        Expression test = new ClassDefExpression(
                new Name("A"),
                Optional.empty(),
                new Field[]{},
                new Method[]{},

                // Create a class B
                new ClassDefExpression(
                        new Name("B"),
                        Optional.empty(),
                        new Field[]{},
                        new Method[]{},

                        // And check if an instance of class A is instanceof B
                        // ( (new A()) instanceof B ) in Java
                        new InstanceOfExpression(
                                new Name("B"),
                                new NewExpression(new Name("A"))
                        )
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got true as the result
        Assert.assertEquals(new BoolValue(false), v);
    }

    @Test
    public void noHardcodeAsAreAs() {
        Assignment4 a4 = Assignment4.getSolution();

        String aName = "";
        String bName = "";

        while (aName.equals(bName)) {
            aName = generateRandomString(4);
            bName = generateRandomString(4);
        }

        // Create a class A
        Expression test = new ClassDefExpression(
                new Name(aName),
                Optional.empty(),
                new Field[]{},
                new Method[]{},

                // Create a class B
                new ClassDefExpression(
                        new Name(bName),
                        Optional.empty(),
                        new Field[]{},
                        new Method[]{},

                        // And check if an instance of class A is instanceof A
                        // ( (new A()) instanceof A ) in Java
                        new InstanceOfExpression(
                                new Name(aName),
                                new NewExpression(new Name(aName))
                        )
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got true as the result
        Assert.assertEquals(new BoolValue(true), v);
    }

    @Test
    public void noHardcodesAreNotBs() {
        Assignment4 a4 = Assignment4.getSolution();

        String aName = "";
        String bName = "";

        while (aName.equals(bName)) {
            aName = generateRandomString(4);
            bName = generateRandomString(4);
        }

        // Create a class A
        Expression test = new ClassDefExpression(
                new Name(aName),
                Optional.empty(),
                new Field[]{},
                new Method[]{},

                // Create a class B
                new ClassDefExpression(
                        new Name(bName),
                        Optional.empty(),
                        new Field[]{},
                        new Method[]{},

                        // And check if an instance of class B is instanceof A
                        // ( (new B()) instanceof A ) in Java
                        new InstanceOfExpression(
                                new Name(aName),
                                new NewExpression(new Name(bName))
                        )
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got true as the result
        Assert.assertEquals(new BoolValue(false), v);
    }

    public static String generateRandomString(int size) {
        return new Random()
                .ints('0', 'z' + 1)
                .limit(size)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
