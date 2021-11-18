package edu.uic.cs474.f21.a4.solution;

import edu.uic.cs474.f21.a4.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class A4Solution extends Assignment4 {
    private Interpreter interpreter = new Interpreter();
    @Override
    public Value evaluate(Expression c, Environment e) {
        switch(c.getClass().getSimpleName()) {
            case "ClassDefExpression": {
                ClassDefExpression def = (ClassDefExpression) c;
                ClassValue theClassDefined = new ClassValue();
                theClassDefined.name = def.className;
                if(def.superName.isPresent()){
                    theClassDefined.parent = Optional.of((ClassValue) e.lookup(def.superName.get()));
                }else{
                    theClassDefined.parent = Optional.empty();
                }
                theClassDefined.fields = def.fields;
                theClassDefined.methods = def.methods;
                e = e.bind(def.className,theClassDefined);
                return evaluate(def.body,e);

            }
//            To check object of A is instance of a is true and object a instance of b is not true
//                2 and 3
            case "InstanceOfExpression": {
                InstanceOfExpression ioe = (InstanceOfExpression) c;
                Value val = evaluate(ioe.target, e);
                if (val instanceof ObjectValue) {
                    ObjectValue obj = (ObjectValue) val;
                    if (obj.klass.name.equals(ioe.className)) {
                        return new Value.BoolValue(true);
                    }
                    ClassValue clss = obj.klass;
                    if (clss.parent.isPresent()) {
                        ClassValue p = clss.parent.get();
                        if (p.name.equals(ioe.className)){
                            return new Value.BoolValue(true);
                        }
                        else
                            clss = p;
                    }
                    return new Value.BoolValue(false);
                } else {
                    return new Value.BoolValue(false);
                }
            }

            case "NewExpression": {
                NewExpression newe = (NewExpression) c;
                ObjectValue newObject = new ObjectValue();
                newObject.klass = (ClassValue) e.lookup(newe.className);
                ClassValue klass = newObject.klass;
                while (true) {
                    for (Field f : klass.fields) {
                        Value v = evaluate(f.initializer, e);
                        newObject.fields.put(f.name, v);
                    }
                    if(klass.parent.isPresent()){
                        klass = klass.parent.get();
                    }
                    else
                        break;
                }
                        return newObject;
            }

            case "ReadFieldExpression": {
                ReadFieldExpression rfe = (ReadFieldExpression) c;
                ObjectValue rfeObject = (ObjectValue) evaluate(rfe.receiver, e);
                return rfeObject.fields.get(rfe.fieldName);
            }
//            test 6 we need to climb the hierarchy and get the inherited value

            case "CallMethodExpression": {
                CallMethodExpression  call = (CallMethodExpression) c;
                Value[] actualValue = new Value[call.arguments.length];
                for(int i = 0 ; i <  actualValue.length; i++)
                {
                    actualValue[i] = evaluate(call.arguments[i],e);
                }
                ObjectValue receiver = (ObjectValue) evaluate(call.receiver,e);
                Method a;
                ClassValue klass = receiver.klass;
                while(true) {
                     Optional<Method> maybea = Arrays.stream(klass.methods).filter(method -> method.name.equals(call.methodName)).findAny();
                     if(maybea.isPresent()){
                         a = maybea.get();
                         break;
                     }
                     if(klass.parent.isPresent()){
                         klass = klass.parent.get();
                     }else{
                         throw new Error("Method "+call.methodName + " does not exist in class "+ receiver.klass.name);
                     }
                }
                Environment envThatKnows = e;
                for(int i = 0 ;i < a.arguments.length;i++){
                     envThatKnows = envThatKnows.bind(a.arguments[i],actualValue[i]);
                }
                envThatKnows = envThatKnows.bind((new Name("this")),receiver);
                return evaluate(a.body,envThatKnows);

            }
            case "WriteFieldExpression" :{
                WriteFieldExpression write = (WriteFieldExpression) c;
                ObjectValue target = (ObjectValue) evaluate(write.receiver,e);

                Value newVal = evaluate(write.newValue,e);
                target.fields.put(write.fieldName,newVal);

                return newVal;
            }
        }
        return interpreter.eval(c,e,null,this);
    }

    public static class ClassValue extends Value{
        Name name;
        Field[] fields;
        Method[] methods;
        Optional<ClassValue> parent;
    }

    public static class ObjectValue extends Value{
        ClassValue klass;
        Map<Name,Value> fields = new HashMap<>();

    }
}
