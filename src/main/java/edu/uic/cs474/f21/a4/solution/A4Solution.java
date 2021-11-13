package edu.uic.cs474.f21.a4.solution;

import edu.uic.cs474.f21.a4.*;

import java.util.Optional;

public class A4Solution extends Assignment4 {
    private Interpreter interpreter = new Interpreter();
    @Override
    public Value evaluate(Expression c, Environment e) {
        switch(c.getClass().getSimpleName()) {
//            3 4 7
            case "ClassDefExpression": {
                ClassDefExpression def = (ClassDefExpression) c;
                ClassValue theClassDefined = new ClassValue(def.superName, def.methods, def.fields);
                e = e.bind(def.className,theClassDefined);
                return evaluate(def.body,e);

            }
//            To check object of A is instance of a is true and object a instance of b is not true
//                2 and 3
            case "InstanceOfExpression": {
                InstanceOfExpression ioe = (InstanceOfExpression) c;
                Value val = evaluate(ioe.target,e);
                if(val instanceof ObjectValue){
                    ObjectValue ioeobj = (ObjectValue) val;
                    ClassValue ioeclassValue = (ClassValue) e.lookup(ioeobj.theName);
                    if (!((ObjectValue) val).theName.equals(ioe.className)) {
                        if (ioeclassValue.superClass.isEmpty() || !ioeclassValue.superClass.get().
                                equals(ioe.className)) {
                            return new Value.BoolValue(false);
                        }
                    }
                    return  new Value.BoolValue(true);
                }
                return  new Value.BoolValue(false);
            }
//Test 3 same as 2 just that here b extends a so b instance of A is true
//                2 and 4
            case "NewExpression": {
                NewExpression newe = (NewExpression) c;
                ClassValue neweclassValue = (ClassValue) e.lookup(newe.className);
                ObjectValue neweObject = new ObjectValue(newe.className, neweclassValue.methods, neweclassValue.fields);
                return neweObject;
            }

            case "ReadFieldExpression": {
//                4 6
//                Test 4 we create a class for A with single field which is then read as value of A when passed as instance of an object
//                Test 5 we need to read 2 fields and return the respective values
                ReadFieldExpression rfe = (ReadFieldExpression) c;
                ObjectValue rfeObject = (ObjectValue) evaluate(rfe.receiver, e);
                for (Field field : rfeObject.fields)
                    if (rfe.fieldName.theName.equals(field.name.theName)) {
                        Value.IntValue value = (Value.IntValue) evaluate(field.initializer, e);
                        return new Value.IntValue(value.val);
                    }
            }
//            test 6 we need to climb the hierarchy and get the inherited value

            case "CallMethodExpression": {
//                7 we take the argument convert to receiver and then bind it into and expression and reurn the value
                CallMethodExpression cme = (CallMethodExpression) c;
                ObjectValue cmeObject = (ObjectValue) evaluate(cme.receiver,e);
                Method[] methods = cmeObject.methods;
                for (Method method : methods) {
                    if (method.name.equals(cme.methodName)) {
                        int count = 0;
                        e = e.bind(new Name("this"), evaluate(cme.receiver, e));
                        Expression[] arguments = cme.arguments;
                        for (int i = 0; i < arguments.length; i++) {
                            Expression ex = arguments[i];
                            e = e.bind(method.arguments[count], evaluate(ex, e));
                            count = count + 1;
                        }
                        return evaluate(method.body, e);
                    }
                }
            }
        }
        return interpreter.eval(c,e,null,this);
    }

    public static class ClassValue extends Value{
        Optional<Name> superClass;
        Field[] fields;
        Method[] methods;

        public ClassValue(Optional<Name> superClass, Method[] methods, Field[] fields) {
            this.superClass = superClass;
            this.methods = methods;
            this.fields = fields;
        }
    }

    public static class ObjectValue extends Value{
        Method[] methods;
        Name theName;
        Field[] fields;

        public ObjectValue(Name theName, Method[] methods, Field[] fields) {
            this.theName = theName;
            this.fields = fields;
            this.methods = methods;
        }
    }
}
