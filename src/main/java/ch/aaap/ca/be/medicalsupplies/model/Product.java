package ch.aaap.ca.be.medicalsupplies.model;

public class Product {
    String id;
    String name;
    GenericName genericName;
    Category primaryCategory;
    Producer producer;
    LicenceHolder licenceHolder;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GenericName getGenericName() {
        return genericName;
    }

    public void setGenericName(GenericName genericName) {
        this.genericName = genericName;
    }

    public Category getPrimaryCategory() {
        return primaryCategory;
    }

    public void setPrimaryCategory(Category primaryCategory) {
        this.primaryCategory = primaryCategory;
    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public LicenceHolder getLicenceHolder() {
        return licenceHolder;
    }

    public void setLicenceHolder(LicenceHolder licenceHolder) {
        this.licenceHolder = licenceHolder;
    }
}
