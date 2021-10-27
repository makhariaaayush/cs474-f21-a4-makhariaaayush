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

public class Test03_SubtypingInstanceof {

    @Test
    public void asAreAs() {
        Assignment4 a4 = Assignment4.getSolution();

        // Create a class A
        Expression test = new ClassDefExpression(
                new Name("A"),
                Optional.empty(),
                new Field[]{},
                new Method[]{},

                // Create a class B extends A
                new ClassDefExpression(
                        new Name("B"),
                        Optional.of(new Name("A")),
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

                // Create a class B extends A
                new ClassDefExpression(
                        new Name("B"),
                        Optional.of(new Name("A")),
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
    public void bsAreAs() {
        Assignment4 a4 = Assignment4.getSolution();

        // Create a class A
        Expression test = new ClassDefExpression(
                new Name("A"),
                Optional.empty(),
                new Field[]{},
                new Method[]{},

                // Create a class B extends A
                new ClassDefExpression(
                        new Name("B"),
                        Optional.of(new Name("A")),
                        new Field[]{},
                        new Method[]{},

                        // And check if an instance of class B is instanceof A
                        // ( (new B()) instanceof A ) in Java
                        new InstanceOfExpression(
                                new Name("A"),
                                new NewExpression(new Name("B"))
                        )
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got true as the result
        Assert.assertEquals(new BoolValue(true), v);
    }

    @Test
    public void bsAreNotCs() {
        Assignment4 a4 = Assignment4.getSolution();

        // Create a class A
        Expression test = new ClassDefExpression(
                new Name("A"),
                Optional.empty(),
                new Field[]{},
                new Method[]{},

                // Create a class B extends A
                new ClassDefExpression(
                        new Name("B"),
                        Optional.of(new Name("A")),
                        new Field[]{},
                        new Method[]{},

                        // Create unrelated class C
                        new ClassDefExpression(
                                new Name("C"),
                                Optional.empty(),
                                new Field[]{},
                                new Method[]{},

                                // And check if an instance of class B is instanceof C
                                // ( (new B()) instanceof C ) in Java
                                new InstanceOfExpression(
                                        new Name("C"),
                                        new NewExpression(new Name("B"))
                                )
                        )

                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got true as the result
        Assert.assertEquals(new BoolValue(false), v);

        // same as above, but class C is defined in different places

        // Create a class A
        test = new ClassDefExpression(
                new Name("A"),
                Optional.empty(),
                new Field[]{},
                new Method[]{},

                // Create unrelated class C
                new ClassDefExpression(
                        new Name("C"),
                        Optional.empty(),
                        new Field[]{},
                        new Method[]{},

                        // Create a class B extends A
                        new ClassDefExpression(
                                new Name("B"),
                                Optional.of(new Name("A")),
                                new Field[]{},
                                new Method[]{},

                                // And check if an instance of class B is instanceof C
                                // ( (new B()) instanceof C ) in Java
                                new InstanceOfExpression(
                                        new Name("C"),
                                        new NewExpression(new Name("B"))
                                )
                        )

                )
        );

        // Evaluate the test program
        v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got true as the result
        Assert.assertEquals(new BoolValue(false), v);

        // same as above, but class C is defined in different places

        // Create a class C
        test = new ClassDefExpression(
                new Name("C"),
                Optional.empty(),
                new Field[]{},
                new Method[]{},

                // Create a class A
                new ClassDefExpression(
                        new Name("A"),
                        Optional.empty(),
                        new Field[]{},
                        new Method[]{},

                        // Create class B extends A
                        new ClassDefExpression(
                                new Name("B"),
                                Optional.of(new Name("A")),
                                new Field[]{},
                                new Method[]{},

                                // And check if an instance of class B is instanceof C
                                // ( (new B()) instanceof C ) in Java
                                new InstanceOfExpression(
                                        new Name("C"),
                                        new NewExpression(new Name("B"))
                                )
                        )

                )
        );

        // Evaluate the test program
        v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got true as the result
        Assert.assertEquals(new BoolValue(false), v);
    }

    @Test
    public void noHardcodingAsAreNotBs() {
        Assignment4 a4 = Assignment4.getSolution();

        String aName = "";
        String bName = "";
        String cName = "";

        while (aName.equals(bName) || aName.equals(cName) || bName.equals(cName)) {
            aName = Test02_InstanceOfTwoClasses.generateRandomString(4);
            bName = Test02_InstanceOfTwoClasses.generateRandomString(4);
            cName = Test02_InstanceOfTwoClasses.generateRandomString(4);
        }

        // Create a class A
        Expression test = new ClassDefExpression(
                new Name(aName),
                Optional.empty(),
                new Field[]{},
                new Method[]{},

                // Create a class B extends A
                new ClassDefExpression(
                        new Name(bName),
                        Optional.of(new Name(aName)),
                        new Field[]{},
                        new Method[]{},

                        // And check if an instance of class A is instanceof B
                        // ( (new A()) instanceof B ) in Java
                        new InstanceOfExpression(
                                new Name(bName),
                                new NewExpression(new Name(aName))
                        )
                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got true as the result
        Assert.assertEquals(new BoolValue(false), v);
    }

    @Test
    public void noHardcodingBsAreAs() {
        Assignment4 a4 = Assignment4.getSolution();

        String aName = "";
        String bName = "";
        String cName = "";

        while (aName.equals(bName) || aName.equals(cName) || bName.equals(cName)) {
            aName = Test02_InstanceOfTwoClasses.generateRandomString(4);
            bName = Test02_InstanceOfTwoClasses.generateRandomString(4);
            cName = Test02_InstanceOfTwoClasses.generateRandomString(4);
        }


        // Create a class A
        Expression test = new ClassDefExpression(
                new Name(aName),
                Optional.empty(),
                new Field[]{},
                new Method[]{},

                // Create a class B extends A
                new ClassDefExpression(
                        new Name(bName),
                        Optional.of(new Name(aName)),
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
        Assert.assertEquals(new BoolValue(true), v);
    }

    @Test
    public void noHardcodingBsAreNotCs() {
        Assignment4 a4 = Assignment4.getSolution();

        String aName = "";
        String bName = "";
        String cName = "";

        // Generate different names
        while (Stream.of(aName, bName, cName).distinct().count() != 3) {
            aName = Test02_InstanceOfTwoClasses.generateRandomString(4);
            bName = Test02_InstanceOfTwoClasses.generateRandomString(4);
            cName = Test02_InstanceOfTwoClasses.generateRandomString(4);
        }

        // Create a class A
        Expression test = new ClassDefExpression(
                new Name(aName),
                Optional.empty(),
                new Field[]{},
                new Method[]{},

                // Create a class B extends A
                new ClassDefExpression(
                        new Name(bName),
                        Optional.of(new Name(aName)),
                        new Field[]{},
                        new Method[]{},

                        // Create unrelated class C
                        new ClassDefExpression(
                                new Name(cName),
                                Optional.empty(),
                                new Field[]{},
                                new Method[]{},

                                // And check if an instance of class B is instanceof C
                                // ( (new B()) instanceof C ) in Java
                                new InstanceOfExpression(
                                        new Name(cName),
                                        new NewExpression(new Name(bName))
                                )
                        )

                )
        );

        // Evaluate the test program
        Value v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got true as the result
        Assert.assertEquals(new BoolValue(false), v);

        // same as above, but class C is defined in different places

        // Create a class A
        test = new ClassDefExpression(
                new Name(aName),
                Optional.empty(),
                new Field[]{},
                new Method[]{},

                // Create unrelated class C
                new ClassDefExpression(
                        new Name(cName),
                        Optional.empty(),
                        new Field[]{},
                        new Method[]{},

                        // Create a class B extends A
                        new ClassDefExpression(
                                new Name(bName),
                                Optional.of(new Name(aName)),
                                new Field[]{},
                                new Method[]{},

                                // And check if an instance of class B is instanceof C
                                // ( (new B()) instanceof C ) in Java
                                new InstanceOfExpression(
                                        new Name(cName),
                                        new NewExpression(new Name(bName))
                                )
                        )

                )
        );

        // Evaluate the test program
        v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got true as the result
        Assert.assertEquals(new BoolValue(false), v);

        // same as above, but class C is defined in different places

        // Create a class C
        test = new ClassDefExpression(
                new Name(cName),
                Optional.empty(),
                new Field[]{},
                new Method[]{},

                // Create a class A
                new ClassDefExpression(
                        new Name(aName),
                        Optional.empty(),
                        new Field[]{},
                        new Method[]{},

                        // Create class B extends A
                        new ClassDefExpression(
                                new Name(bName),
                                Optional.of(new Name(aName)),
                                new Field[]{},
                                new Method[]{},

                                // And check if an instance of class B is instanceof C
                                // ( (new B()) instanceof C ) in Java
                                new InstanceOfExpression(
                                        new Name(cName),
                                        new NewExpression(new Name(bName))
                                )
                        )

                )
        );

        // Evaluate the test program
        v = a4.evaluate(test, Environment.EMPTY);

        // And ensure we got true as the result
        Assert.assertEquals(new BoolValue(false), v);
    }

}
