package ch.aaap.ca.be.medicalsupplies.model;

import java.util.Objects;

public class Category {
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    @Override
    public boolean equals (Object object) {
        boolean result = false;
        if (object == null || object.getClass() != getClass()) {
            result = false;
        } else {
            Category category = (Category) object;
            if (this.name == category.getName()) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
