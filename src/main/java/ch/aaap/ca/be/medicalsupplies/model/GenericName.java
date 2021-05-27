package ch.aaap.ca.be.medicalsupplies.model;

import java.util.Set;

public class GenericName {
    String name;
    Set<Category> categorySet;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Category> getCategorySet() {
        return categorySet;
    }

    public void setCategorySet(Set<Category> categorySet) {
        this.categorySet = categorySet;
    }

    public GenericName() {
    }
}
