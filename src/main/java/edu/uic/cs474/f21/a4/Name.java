package edu.uic.cs474.f21.a4;

import java.util.Objects;

public class Name {
    public final String theName;

    public Name(String theName) {
        this.theName = theName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        return Objects.equals(theName, name.theName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(theName);
    }
}
