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
                ClassValue theClassDefined = new ClassValue(def.superName,def.fields);
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
                ObjectValue neweObject = new ObjectValue(newe.className,neweclassValue.fields);
                return neweObject;

            }
        }
        return interpreter.eval(c,e,null,this);
    }

    public static class ClassValue extends Value{
        Optional<Name> superClass;
        Field[] fields;

        public ClassValue(Optional<Name> superClass, Field[] fields) {
            this.superClass = superClass;
            this.fields = fields;

    }
    }

    public static class ObjectValue extends Value{
        Name theName;
        Field[] fields;

        public ObjectValue(Name theName, Field[] fields) {
            this.theName = theName;
            this.fields = fields;
        }

    }
}
