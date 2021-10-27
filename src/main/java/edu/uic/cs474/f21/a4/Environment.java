package edu.uic.cs474.f21.a4;

import java.util.NoSuchElementException;

public class Environment {

    private Binding binding;
    private final Environment referencingEnvironment;

    public Environment(Binding binding, Environment referencingEnvironment) {
        this.binding = binding;
        this.referencingEnvironment = referencingEnvironment;
    }

    public static final Environment EMPTY = new Environment(null, null);

    public Environment bind(Name name, Value value) {
        return new Environment(new Binding(name, value), this);
    }

    public Value lookup(Name name) {
        if (this == EMPTY)
            throw new NoSuchElementException();

        if (this.binding.name.theName.equals(name.theName))
            return binding.value;

        return referencingEnvironment.lookup(name);
    }

    public void set(Name name, Value updatedValue) {
        if (this == EMPTY)
            throw new NoSuchElementException();

        if (this.binding.name.theName.equals(name.theName)) {
            this.binding = new Binding(name, updatedValue);
            return;
        }

        referencingEnvironment.set(name, updatedValue);
    }
}
