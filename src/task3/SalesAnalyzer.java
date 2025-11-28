package task3;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

class Sale {
    private String product;
    private String category;
    private double price;
    private int quantity;
    private LocalDate date;

    public Sale(String product, String category, double price, int quantity, LocalDate date) {
        this.product = product;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.date = date;
    }

    public double getTotalAmount() {
        return price * quantity;
    }

    public String getProduct() { return product; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public LocalDate getDate() { return date; }

    @Override
    public String toString() {
        return String.format("%s (%s) - %.2f₸ x %d = %.2f₸",
                product, category, price, quantity, getTotalAmount());
    }
}

public class SalesAnalyzer {

    private static List<Sale> generateSales() {
        List<Sale> sales = new ArrayList<>();
        Random random = new Random();

        String[] categories = {"Электроника", "Одежда", "Продукты", "Книги"};
        String[][] products = {
                {"Ноутбук", "Смартфон", "Планшет", "Наушники", "Телевизор"},
                {"Футболка", "Джинсы", "Куртка", "Кроссовки", "Рубашка"},
                {"Хлеб", "Молоко", "Яйца", "Сыр", "Мясо"},
                {"Роман", "Учебник", "Комикс", "Энциклопедия", "Детектив"}
        };

        double[][] priceRanges = {
                {25000, 120000}, // Электроника
                {2000, 15000},   // Одежда
                {350, 5000},     // Продукты
                {1500, 8000}     // Книги
        };

        for (int i = 0; i < 150; i++) {
            int categoryIndex = random.nextInt(categories.length);
            String category = categories[categoryIndex];
            String product = products[categoryIndex][random.nextInt(products[categoryIndex].length)];

            double minPrice = priceRanges[categoryIndex][0];
            double maxPrice = priceRanges[categoryIndex][1];
            double price = minPrice + (maxPrice - minPrice) * random.nextDouble();

            int quantity = random.nextInt(5) + 1; // 1-5 единиц
            LocalDate date = LocalDate.now().minusDays(random.nextInt(30));

            sales.add(new Sale(product, category, price, quantity, date));
        }

        return sales;
    }

    public static void main(String[] args) {
        List<Sale> sales = generateSales();

        System.out.println("=== Анализ данных о продажах ===\n");
        System.out.println("Всего продаж: " + sales.size() + "\n");

        // 1. Общая сумма продаж
        System.out.println("1. Общая сумма продаж:");
        double totalRevenue = sales.stream()
                .mapToDouble(Sale::getTotalAmount)
                .sum();
        System.out.printf("Общая сумма: %.2f₸\n\n", totalRevenue);

        // 2. Продажи по категориям (сумма)
        System.out.println("2. Продажи по категориям:");
        Map<String, Double> revenueByCategory = sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getCategory,
                        Collectors.summingDouble(Sale::getTotalAmount)
                ));
        revenueByCategory.forEach((category, revenue) ->
                System.out.printf("%s: %.2f₸\n", category, revenue));
        System.out.println();

        // 3. Самый продаваемый товар (по количеству)
        System.out.println("3. Самый продаваемый товар:");
        Map<String, Integer> productQuantities = sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getProduct,
                        Collectors.summingInt(Sale::getQuantity)
                ));

        productQuantities.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(entry -> {
                    String topProduct = entry.getKey();
                    int totalQuantity = entry.getValue();

                    // Найти пример продажи этого товара для отображения
                    sales.stream()
                            .filter(s -> s.getProduct().equals(topProduct))
                            .findFirst()
                            .ifPresent(sale -> {
                                System.out.printf("%s (%s) - %.2f₸ x %d = %.2f₸\n",
                                        sale.getProduct(), sale.getCategory(),
                                        sale.getPrice(), sale.getQuantity(),
                                        sale.getTotalAmount());
                                System.out.println("Общее количество: " + totalQuantity + " единиц\n");
                            });
                });

        // 4. Средняя цена товара в каждой категории
        System.out.println("4. Средняя цена по категориям:");
        Map<String, Double> avgPriceByCategory = sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getCategory,
                        Collectors.averagingDouble(Sale::getPrice)
                ));
        avgPriceByCategory.forEach((category, avgPrice) ->
                System.out.printf("%s: %.2f₸\n", category, avgPrice));
        System.out.println();

        // 5. Топ-5 самых дорогих продаж
        System.out.println("5. Топ-5 самых дорогих продаж:");
        List<Sale> top5Sales = sales.stream()
                .sorted(Comparator.comparingDouble(Sale::getTotalAmount).reversed())
                .limit(5)
                .collect(Collectors.toList());

        for (int i = 0; i < top5Sales.size(); i++) {
            Sale sale = top5Sales.get(i);
            System.out.printf("%d. %s\n", i + 1, sale);
        }
        System.out.println();

        // 6. Количество продаж по категориям
        System.out.println("6. Количество продаж по категориям:");
        Map<String, Long> salesCountByCategory = sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getCategory,
                        Collectors.counting()
                ));
        salesCountByCategory.forEach((category, count) ->
                System.out.println(category + ": " + count + " продаж"));
        System.out.println();

        // 7. Статистика по ценам
        System.out.println("7. Статистика по ценам:");
        DoubleSummaryStatistics priceStats = sales.stream()
                .mapToDouble(Sale::getPrice)
                .summaryStatistics();

        System.out.printf("Минимальная цена: %.2f₸\n", priceStats.getMin());
        System.out.printf("Максимальная цена: %.2f₸\n", priceStats.getMax());
        System.out.printf("Средняя цена: %.2f₸\n", priceStats.getAverage());
        System.out.println("Общее количество товаров: " + sales.size());
    }
}