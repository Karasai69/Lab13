package task2;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamPerformanceComparison {

    private static final int DATA_SIZE = 10_000_000;
    private static final int PRIME_RANGE = 100_000;

    public static void main(String[] args) {
        // Создание данных
        List<Integer> numbers = IntStream.rangeClosed(1, DATA_SIZE)
                .boxed()
                .collect(Collectors.toList());

        System.out.println("=== Сравнение производительности потоков ===\n");
        System.out.println("Размер данных: " + DATA_SIZE + " элементов");
        System.out.println("Количество процессоров: " +
                Runtime.getRuntime().availableProcessors() + "\n");

        // Прогрев JVM
        warmUp(numbers);

        // Тесты
        testFiltering(numbers);
        testSquareAndSum(numbers);
        testPrimeNumbers();
    }

    private static void warmUp(List<Integer> numbers) {
        System.out.println("Прогрев JVM...");
        for (int i = 0; i < 3; i++) {
            numbers.stream().filter(n -> n % 2 == 0).count();
            numbers.parallelStream().filter(n -> n % 2 == 0).count();
        }
    }

    private static void testFiltering(List<Integer> numbers) {
        System.out.println("--- Операция 1: Фильтрация чётных чисел ---");

        // Последовательный поток
        long startSeq = System.currentTimeMillis();
        long countSeq = numbers.stream()
                .filter(n -> n % 2 == 0)
                .count();
        long timeSeq = System.currentTimeMillis() - startSeq;

        // Параллельный поток
        long startPar = System.currentTimeMillis();
        long countPar = numbers.parallelStream()
                .filter(n -> n % 2 == 0)
                .count();
        long timePar = System.currentTimeMillis() - startPar;

        printResults("Последовательный", countSeq, timeSeq);
        printResults("Параллельный", countPar, timePar);
        printSpeedup(timeSeq, timePar);
    }

    private static void testSquareAndSum(List<Integer> numbers) {
        System.out.println("\n--- Операция 2: Возведение в квадрат и суммирование ---");

        // Последовательный поток
        long startSeq = System.currentTimeMillis();
        long sumSeq = numbers.stream()
                .mapToLong(n -> (long) n * n)
                .sum();
        long timeSeq = System.currentTimeMillis() - startSeq;

        // Параллельный поток
        long startPar = System.currentTimeMillis();
        long sumPar = numbers.parallelStream()
                .mapToLong(n -> (long) n * n)
                .sum();
        long timePar = System.currentTimeMillis() - startPar;

        printResults("Последовательный", sumSeq, timeSeq);
        printResults("Параллельный", sumPar, timePar);
        printSpeedup(timeSeq, timePar);
    }

    private static void testPrimeNumbers() {
        System.out.println("\n--- Операция 3: Поиск простых чисел (1-" + PRIME_RANGE + ") ---");

        // Последовательный поток
        long startSeq = System.currentTimeMillis();
        long primeCountSeq = IntStream.rangeClosed(1, PRIME_RANGE)
                .filter(StreamPerformanceComparison::isPrime)
                .count();
        long timeSeq = System.currentTimeMillis() - startSeq;

        // Параллельный поток
        long startPar = System.currentTimeMillis();
        long primeCountPar = IntStream.rangeClosed(1, PRIME_RANGE)
                .parallel()
                .filter(StreamPerformanceComparison::isPrime)
                .count();
        long timePar = System.currentTimeMillis() - startPar;

        printResults("Последовательный", primeCountSeq, timeSeq);
        printResults("Параллельный", primeCountPar, timePar);
        printSpeedup(timeSeq, timePar);
    }

    private static boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;

        return IntStream.rangeClosed(3, (int) Math.sqrt(n))
                .filter(i -> i % 2 != 0)
                .noneMatch(i -> n % i == 0);
    }

    private static void printResults(String type, long result, long time) {
        System.out.println(type + " поток:");
        System.out.println("Результат: " + result);
        System.out.println("Время: " + time + " мс");
    }

    private static void printSpeedup(long seqTime, long parTime) {
        double speedup = (double) seqTime / parTime;
        System.out.printf("Ускорение: %.2fx\n", speedup);
    }
}