package ch.aaap.ca.be.medicalsupplies;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import ch.aaap.ca.be.medicalsupplies.data.CSVUtil;
import ch.aaap.ca.be.medicalsupplies.data.MSGenericNameRow;
import ch.aaap.ca.be.medicalsupplies.data.MSProductRow;
import ch.aaap.ca.be.medicalsupplies.model.Category;
import ch.aaap.ca.be.medicalsupplies.model.Product;
import ch.aaap.ca.be.medicalsupplies.model.LicenceHolder;
import ch.aaap.ca.be.medicalsupplies.model.Producer;
import ch.aaap.ca.be.medicalsupplies.model.GenericName;

public class MSApplication {

    private final Set<MSGenericNameRow> genericNames;
    private final Set<MSProductRow> registry;
    private final Set<Product> products;

    public MSApplication() {
        genericNames = CSVUtil.getGenericNames();
        registry = CSVUtil.getRegistry();
        products = this.createModel(genericNames, registry);
    }

    public static void main(String[] args) {
        MSApplication main = new MSApplication();

        System.err.println("generic names count: " + main.genericNames.size());
        System.err.println("registry count: " + main.registry.size());

        System.err.println("1st of generic name list: " + main.genericNames.iterator().next());
        System.err.println("1st of registry list: " + main.registry.iterator().next());
    }

    /**
     * Create a model / data structure that combines the input sets.
     * 
     * @param genericNameRows
     * @param productRows
     * @return Set<Product>
     */
    public Set<Product> createModel(Set<MSGenericNameRow> genericNameRows, Set<MSProductRow> productRows) {

        Set<GenericName> genericNames = new HashSet<GenericName>();
        Set<Product> products = new HashSet<Product>();


        this.genericNames.stream().forEach(msGenericNameRow -> {
            Set<Category> categories = new HashSet<>();
            GenericName genericName = new GenericName();
            genericName.setName(msGenericNameRow.getName());
            this.addCategory(msGenericNameRow.getCategory1(), categories);
            this.addCategory(msGenericNameRow.getCategory2(), categories);
            this.addCategory(msGenericNameRow.getCategory3(), categories);
            this.addCategory(msGenericNameRow.getCategory4(), categories);
            genericName.setCategorySet(categories);
            genericNames.add(genericName);
        });

        this.registry.stream().forEach(msProductRow -> {
            Product product = new Product();
            Producer producer = new Producer();
            LicenceHolder licenceHolder = new LicenceHolder();
            producer.setProducerId(msProductRow.getProducerId());
            producer.setProducerName(msProductRow.getProducerName());
            producer.setProducerAddress(msProductRow.getProducerAddress());
            licenceHolder.setLicenseHolderId(msProductRow.getLicenseHolderId());
            licenceHolder.setLicenseHolderName(msProductRow.getLicenseHolderName());
            licenceHolder.setLicenseHolderAddress(msProductRow.getLicenseHolderAddress());
            product.setName(msProductRow.getName());
            product.setId(msProductRow.getId());
            product.setPrimaryCategory(new Category(msProductRow.getPrimaryCategory()));
            product.setProducer(producer);
            product.setLicenceHolder(licenceHolder);
            GenericName genName = genericNames.stream().filter(name -> name.getName().equals(msProductRow.getGenericName())).findFirst().orElse(null);
            product.setGenericName(genName);
            products.add(product);
        });

        return products;
    }

    /**
     * Add category to Set<Category> for every GenericName
     *
     * @param categoryName
     * @param categories
     */
    private void addCategory(String categoryName, Set<Category> categories) {
        if(!categoryName.equals("") && categoryName != null) {
            if(!categories.stream().anyMatch(c -> c.getName().equals(categoryName))) {
                Category category = new Category();
                category.setName(categoryName);
                categories.add(category);
            }
        }
    }

    /**
     * Utility method for filtering distinct by object property.
     *
     * @return
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /* MS Generic Names */
    /**
     * Method find the number of unique generic names.
     * 
     * @return
     */
    public Object numberOfUniqueGenericNames() {
        return genericNames.stream()
            .filter( distinctByKey(genericName -> genericName.getName()) )
            .collect( Collectors.toList()).size();
    }

    /**
     * Method finds the number of generic names which are duplicated.
     * 
     * @return
     */
    public Object numberOfDuplicateGenericNames() {
        return genericNames.size() - (Integer) numberOfUniqueGenericNames();
    }

    /* MS Products */
    /**
     * Method finds the number of products which have a generic name which can be
     * determined.
     * 
     * @return
     */
    public Object numberOfMSProductsWithGenericName() {
        return products.stream()
                .filter(product -> product.getGenericName() != null)
                .collect(Collectors.toSet()).size();
    }

    /**
     * Method finds the number of products which have a generic name which can NOT
     * be determined.
     * 
     * @return
     */
    public Object numberOfMSProductsWithoutGenericName() {
        return products.stream()
                .filter(product -> product.getGenericName() == null)
                .collect(Collectors.toSet()).size();
    }

    /**
     * Method finds the name of the company which is both the producer and license holder for the
     * most number of products.
     * 
     * @return
     */
    public Object nameOfCompanyWhichIsProducerAndLicenseHolderForMostNumberOfMSProducts() {
        List<Product> sortedLIst = products.stream()
                .filter(product -> product.getLicenceHolder().getLicenseHolderName().equals(product.getProducer().getProducerName()))
                .collect(Collectors.toList());

        Map<String, Long> producerOccurrenceMap =
                sortedLIst.stream().collect(Collectors.groupingBy(product -> product.getProducer().getProducerName(), Collectors.counting()));

        long counter = Collections.max(producerOccurrenceMap.values());

        return producerOccurrenceMap.entrySet()
                .stream()
                .filter(entry -> counter == entry.getValue())
                .map(Map.Entry::getKey)
                .findFirst().get();
    }

    /**
     * Method finds the number of products whose producer name starts with
     * <i>companyName</i>.
     * 
     * @param companyName
     * @return
     */
    public Object numberOfMSProductsByProducerName(String companyName) {
        return products.stream()
            .filter(product -> (product.getProducer().getProducerName()).toLowerCase().startsWith(companyName.toLowerCase()))
            .collect(Collectors.toSet()).size();
    }

    /**
     * Method finds the products whose generic name has the category of interest.
     * 
     * @param category
     * @return
     */
    public Set<Product> findMSProductsWithGenericNameCategory(String category) {

        Set<Product> productsByCategory = products.stream()
                .filter(product -> product.getGenericName() != null)
                .filter(product -> product.getGenericName().getCategorySet() != null)
                .filter(product -> product.getGenericName().getCategorySet()
                .stream().filter(cat -> cat.getName().equals(category)).findFirst().isPresent())
                .collect(Collectors.toSet());

        return productsByCategory;
    }
}
