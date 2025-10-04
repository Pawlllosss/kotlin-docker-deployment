package pl.oczadly.kotlin_app.hackyeah2025.pension.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ForecastDataService {

    private static final Map<Integer, Double> WAGE_GROWTH = new HashMap<>();
    private static final Map<Integer, Double> INFLATION_RATE = new HashMap<>();
    private static final Map<Integer, Double> AVG_PENSION = new HashMap<>();
    private static final Map<String, Double> LIFE_EXPECTANCY = new HashMap<>();

    static {
        // Initialize with sample data - 3% wage growth
        for (int year = 2025; year <= 2100; year++) {
            WAGE_GROWTH.put(year, 0.03);
        }

        // Initialize with sample data - 2.5% inflation
        for (int year = 2025; year <= 2100; year++) {
            INFLATION_RATE.put(year, 0.025);
        }

        // Initialize with sample data - average pension (simplified, constant value)
        for (int year = 2025; year <= 2100; year++) {
            AVG_PENSION.put(year, 3500.0);
        }

        // Life expectancy in years after retirement
        LIFE_EXPECTANCY.put("male", 19.0);    // 18-20 years average
        LIFE_EXPECTANCY.put("female", 24.0);  // 23-25 years average
    }

    public double getWageGrowth(int year) {
        return WAGE_GROWTH.getOrDefault(year, 0.03);
    }

    public double getInflationRate(int year) {
        return INFLATION_RATE.getOrDefault(year, 0.025);
    }

    public double getAveragePension(int year) {
        return AVG_PENSION.getOrDefault(year, 3500.0);
    }

    public double getLifeExpectancy(int year, String sex) {
        return LIFE_EXPECTANCY.getOrDefault(sex.toLowerCase(), 20.0);
    }

    public double getCumulativeInflation(int fromYear, int toYear) {
        double cumulative = 1.0;
        for (int year = fromYear; year < toYear; year++) {
            cumulative *= (1 + getInflationRate(year));
        }
        return cumulative;
    }

    public double getCumulativeWageGrowth(int fromYear, int toYear) {
        double cumulative = 1.0;
        for (int year = fromYear; year < toYear; year++) {
            cumulative *= (1 + getWageGrowth(year));
        }
        return cumulative;
    }
}
