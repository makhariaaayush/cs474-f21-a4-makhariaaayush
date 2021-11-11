package edu.uic.cs474.f21.a4.solution;

import edu.uic.cs474.f21.a4.*;

public class A4Solution extends Assignment4 {
    private Interpreter interpreter = new Interpreter();
    @Override
    public Value evaluate(Expression c, Environment e) {
        switch(c.getClass().getSimpleName()) {
            case "ClassDefExpression": {
                ClassDefExpression def = (ClassDefExpression) c;
                ClassValue theClassDefined = new ClassValue();
                e = e.bind(def.className,theClassDefined);
                return evaluate(def.body,e);

            }
            case "InstanceOfExpression": {
                InstanceOfExpression ioe = (InstanceOfExpression) c;
                Value val = evaluate(ioe.target,e);
                if(val instanceof ObjectValue){
                    ObjectValue obj = (ObjectValue) val;
                    return new Value.BoolValue(true);
                }else{
                    return new Value.BoolValue(false);
                }

            }
            case "NewExpression": {
                NewExpression newe =  (NewExpression) c;
                ObjectValue newObject = new ObjectValue();
                return newObject;

            }
        }
        return interpreter.eval(c,e,null,this);
    }

    public static class ClassValue extends Value{

    }
    public static class ObjectValue extends Value{

    }
}
