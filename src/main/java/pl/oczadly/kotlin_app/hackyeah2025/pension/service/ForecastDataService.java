package pl.oczadly.kotlin_app.hackyeah2025.pension.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Service
public class ForecastDataService {

    private static final Map<Integer, Double> WAGE_GROWTH = new HashMap<>();
    private static final Map<Integer, Double> INFLATION_RATE = new HashMap<>();
    private static final Map<Integer, Map<String, Double>> AVG_PENSION = new HashMap<>();
    private static final Map<String, Double> LIFE_EXPECTANCY_AFTER_RETIREMENT = Map.of(
            "M", 19.0,
            "F", 24.0
    );

    static {
        initializeWageGrowth();
        initializeInflationRate();

        // Initialize with sample data - average pension (simplified, constant value)
        for (var year = 2025; year <= 2100; year++) {
            // TODO: use wage growth for calculation
            AVG_PENSION.put(year, Map.of("M", 4000.0, "F", 3000.0));
        }

    }

    private static void initializeWageGrowth() {
        for (var year = 1900; year <= 2200; year++) {
            WAGE_GROWTH.put(year, 1.02);
        }
        WAGE_GROWTH.put(2022, 0.9800);
        WAGE_GROWTH.put(2023, 1.0030);
        WAGE_GROWTH.put(2024, 1.0340);
        WAGE_GROWTH.put(2025, 1.0370);
        WAGE_GROWTH.put(2026, 1.0350);
        WAGE_GROWTH.put(2027, 1.0300);
        WAGE_GROWTH.put(2028, 1.0290);
        WAGE_GROWTH.put(2029, 1.0290);
        WAGE_GROWTH.put(2030, 1.0290);
        WAGE_GROWTH.put(2031, 1.0290);
        WAGE_GROWTH.put(2032, 1.0290);
        IntStream.range(2033, 2035).forEach(value -> WAGE_GROWTH.put(value, 1.0290));
        WAGE_GROWTH.put(2035, 1.0280);
        IntStream.range(2036, 2040).forEach(value -> WAGE_GROWTH.put(value, 1.0280));
        WAGE_GROWTH.put(2040, 1.0270);
        IntStream.range(2041, 2045).forEach(value -> WAGE_GROWTH.put(value, 1.0270));
        WAGE_GROWTH.put(2045, 1.0260);
        IntStream.range(2046, 2050).forEach(value -> WAGE_GROWTH.put(value, 1.0260));
        WAGE_GROWTH.put(2050, 1.0250);
        IntStream.range(2051, 2055).forEach(value -> WAGE_GROWTH.put(value, 1.0250));
        WAGE_GROWTH.put(2055, 1.0240);
        IntStream.range(2056, 2060).forEach(value -> WAGE_GROWTH.put(value, 1.0240));
        WAGE_GROWTH.put(2060, 1.0240);
        IntStream.range(2061, 2065).forEach(value -> WAGE_GROWTH.put(value, 1.0240));
        WAGE_GROWTH.put(2065, 1.0230);
        IntStream.range(2066, 2070).forEach(value -> WAGE_GROWTH.put(value, 1.0220));
        WAGE_GROWTH.put(2070, 1.0220);
        IntStream.range(2071, 2075).forEach(value -> WAGE_GROWTH.put(value, 1.0220));
        WAGE_GROWTH.put(2075, 1.0210);
        IntStream.range(2076, 2080).forEach(value -> WAGE_GROWTH.put(value, 1.0210));
        WAGE_GROWTH.put(2080, 1.0200);

    }


    private static void initializeInflationRate() {
        // Initialize with sample data - 2.5% inflation
        for (var year = 1900; year <= 2200; year++) {
            INFLATION_RATE.put(year, 1.025);
        }
        INFLATION_RATE.put(2022, 1.1350);
        INFLATION_RATE.put(2023, 1.0980);
        INFLATION_RATE.put(2024, 1.0480);
        INFLATION_RATE.put(2025, 1.0310);
        INFLATION_RATE.put(2026, 1.0250);
        INFLATION_RATE.put(2027, 1.0250);
        INFLATION_RATE.put(2028, 1.0250);
        INFLATION_RATE.put(2029, 1.0250);
        INFLATION_RATE.put(2030, 1.0250);
        INFLATION_RATE.put(2031, 1.0250);
        INFLATION_RATE.put(2032, 1.0250);
        INFLATION_RATE.put(2035, 1.0250);
        INFLATION_RATE.put(2040, 1.0250);
        INFLATION_RATE.put(2045, 1.0250);
        INFLATION_RATE.put(2050, 1.0250);
        INFLATION_RATE.put(2055, 1.0250);
    }

    public double getWageGrowth(int year) {
        return WAGE_GROWTH.getOrDefault(year, 1.03);
    }

    public double getInflationRate(int year) {
        return INFLATION_RATE.getOrDefault(year, 0.025);
    }

    public double getAveragePension(int year, String sex) {
        return AVG_PENSION.get(year).get(sex);
    }

    public double getLifeExpectancyMonths(int year, String sex) {
        return LIFE_EXPECTANCY_AFTER_RETIREMENT.getOrDefault(sex.toLowerCase(), 20.0) * 12;
    }

    public double getCumulativeInflation(int fromYear, int toYear) {
        var cumulative = 1.0;
        for (var year = fromYear; year < toYear; year++) {
            cumulative *= (getInflationRate(year));
        }
        return cumulative;
    }

    public double getCumulativeWageGrowth(int fromYear, int toYear) {
        var cumulative = 1.0;
        for (var year = fromYear; year < toYear; year++) {
            cumulative *= (1 + getWageGrowth(year));
        }
        return cumulative;
    }
}
