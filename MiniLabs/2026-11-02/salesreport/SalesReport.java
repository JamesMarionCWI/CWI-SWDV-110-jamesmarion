import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class SalesReport {
    // 1. Create record Sale(String product, String region, int qty, double price) and 10 sales
    public record Sale(String product, String region, int qty, double price) {}
    static double saleToRevenue(Sale sale) {
        return sale.qty * sale.price;
    }
    static List<Sale> sales = new ArrayList<>(List.of(
            new Sale("Thing 1", "NA", 1, 13.99),
            new Sale("Thing 1", "NA", 6, 13.99),
            new Sale("Thing 3", "NA", 1, 7.99),
            new Sale("Thing 4", "EU", 1, 4.99),
            new Sale("Thing 3", "NA", 3, 7.99),
            new Sale("Thing 1", "NA", 1, 6.99), // on sale 50%
            new Sale("Thing 2", "EU", 1, 2.99),
            new Sale("Thing 2", "EU", 37, 2.99),
            new Sale("Thing 3", "NA", 2, 7.99),
            new Sale("Thing 4", "EU", 2, 4.99)
        ));
    
    public static void main(String[] args) {
        System.out.println("Welcome to the Sales Report!");

        if(sales.size() == 0) {
            System.err.println("Sadly, there are no sales! Goodbye!");
            return;
        }
        
        // Empty line for formatting
        System.out.println();

        // 2. Print total revenue (qty * price) with mapToDouble().sum()
        System.out.printf("Total Revenue: %.2f%n", sales.stream().mapToDouble(SalesReport::saleToRevenue).sum());
        
        // Empty line for formatting
        System.out.println();

        // 3. Print products with revenue over $100, A to Z, no duplicates
        System.out.println("Products that sold over $100 in total value:");
        sales.stream()
            .collect(Collectors.groupingBy(
                Sale::product, // Key for the TreeMap
                TreeMap::new, // TreeMap automatically sorts and prevents duplicates
                Collectors.summingDouble(SalesReport::saleToRevenue) // Sum by the Product name
            )).entrySet().stream()
            .filter(entry -> entry.getValue() >= 100.0) // Ensure only the products that summed up to over $100 are in the map
            .map(Entry::getKey) // Get the product name to print
            .forEach(System.out::println);
        
        // Empty line for formatting
        System.out.println();

        // 4. Print the single biggest sale
        System.out.println("Single biggest individual sale:");
        sales.stream()
            .max(Comparator.comparing(SalesReport::saleToRevenue))
            .ifPresent(sale -> System.out.printf("%s for %.2f%n", sale.product, saleToRevenue(sale)));

        // Empty line for formatting
        System.out.println();

        // 5. Print revenue per region with groupingBy + summingDouble
        System.out.println("Total revenue per region:");
        sales.stream()
            .collect(Collectors.groupingBy(
                Sale::region,
                TreeMap::new,
                Collectors.summingDouble(SalesReport::saleToRevenue)
            ))
            .forEach((region, totalRevenue) -> System.out.printf("%s: %.2f%n", region, totalRevenue));
        
        // Empty line for formatting
        System.out.println();

        // Stretch: print the top 3 products by total revenue
        System.out.println("Top 3 products by total revenue:");
        sales.stream()
            .collect(Collectors.groupingBy(
                Sale::product,
                HashMap::new,
                Collectors.summingDouble(SalesReport::saleToRevenue)
            )).entrySet().stream()
            .sorted(Comparator.comparing(Entry<String, Double>::getValue).reversed())
            .limit(3)
            .forEach(entry -> System.out.printf("%s - %.2f%n", entry.getKey(), entry.getValue()));
    }
}
