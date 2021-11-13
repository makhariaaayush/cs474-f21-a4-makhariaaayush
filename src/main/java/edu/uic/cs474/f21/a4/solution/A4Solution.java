package edu.uic.cs474.f21.a4.solution;

import edu.uic.cs474.f21.a4.*;

import java.util.Optional;

public class A4Solution extends Assignment4 {
    private Interpreter interpreter = new Interpreter();
    @Override
    public Value evaluate(Expression c, Environment e) {
        switch(c.getClass().getSimpleName()) {
            case "ClassDefExpression": {
                ClassDefExpression def = (ClassDefExpression) c;
                ClassValue theClassDefined = new ClassValue(def.superName, def.methods, def.fields);
                e = e.bind(def.className,theClassDefined);
                return evaluate(def.body,e);

            }
            case "InstanceOfExpression": {
                InstanceOfExpression ioe = (InstanceOfExpression) c;
                Value val = evaluate(ioe.target,e);
                Value t = new Value.BoolValue(true);
                Value f = new Value.BoolValue(false);
                if(val instanceof ObjectValue){
                    ObjectValue ioeobj = (ObjectValue) val;
                    ClassValue ioeclassValue = (ClassValue) e.lookup(ioeobj.theName);
                    if (!((ObjectValue) val).theName.equals(ioe.className)) {
                        if (ioeclassValue.superClass.isEmpty() || !ioeclassValue.superClass.get().
                                equals(ioe.className)) {
                            return f;
                        }
                    }
                    return  t;
                }
                return  f;
            }

            case "NewExpression": {
                NewExpression newe = (NewExpression) c;
                ClassValue neweclassValue = (ClassValue) e.lookup(newe.className);
                ObjectValue neweObject = new ObjectValue(newe.className, neweclassValue.methods, neweclassValue.fields);
                return neweObject;

            }

            case "ReadFieldExpression": {
                ReadFieldExpression rfe = (ReadFieldExpression) c;
                ObjectValue rfeObject = (ObjectValue) evaluate(rfe.receiver, e);
                for (Field field : rfeObject.fields)
                    if (rfe.fieldName.theName.equals(field.name.theName)) {
                        Value.IntValue value = (Value.IntValue) evaluate(field.initializer, e);
                        return new Value.IntValue(value.val);
                    }
            }

            case "CallMethodExpression": {
                CallMethodExpression cme = (CallMethodExpression) c;
                ObjectValue cmeObject = (ObjectValue) evaluate(cme.receiver,e);
                for(Method method : cmeObject.methods){
                    if(method.name.equals(cme.methodName)){
                        int count = 0;
                        Expression[] arguments = cme.arguments;
                        for (int i = 0, argumentsLength = arguments.length; i < argumentsLength; i++) {
                            Expression ex = arguments[i];
                            e = e.bind(method.arguments[count], evaluate(ex, e));
                            count = count + 1;
                        }
                        return evaluate(method.body,e);
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
