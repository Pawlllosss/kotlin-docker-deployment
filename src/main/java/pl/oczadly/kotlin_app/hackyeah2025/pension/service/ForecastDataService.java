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
    private static final Map<Integer, Double> VALORIZATION_RATE = new HashMap<>();
    private static final Map<String, Double> LIFE_EXPECTANCY_AFTER_RETIREMENT = Map.of(
            "M", 19.0,
            "F", 24.0
    );

    static {
        initializeWageGrowth();
        initializeInflationRate();
        initializeValorizationRate();

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

    private static void initializeValorizationRate() {
        VALORIZATION_RATE.put(2014, 1.0206);
        VALORIZATION_RATE.put(2015, 1.0537);
        VALORIZATION_RATE.put(2016, 1.0637);
        VALORIZATION_RATE.put(2017, 1.0868);
        VALORIZATION_RATE.put(2018, 1.0920);
        VALORIZATION_RATE.put(2019, 1.0894);
        VALORIZATION_RATE.put(2020, 1.0541);
        VALORIZATION_RATE.put(2021, 1.0933);
        VALORIZATION_RATE.put(2022, 1.1440);
        VALORIZATION_RATE.put(2023, 1.1487);
        VALORIZATION_RATE.put(2024, 1.1441);
        VALORIZATION_RATE.put(2025, 1.0739);
        VALORIZATION_RATE.put(2026, 1.0661);
        VALORIZATION_RATE.put(2027, 1.0573);
        VALORIZATION_RATE.put(2028, 1.0519);
        VALORIZATION_RATE.put(2029, 1.0493);
        VALORIZATION_RATE.put(2030, 1.0472);
        VALORIZATION_RATE.put(2031, 1.0450);
        VALORIZATION_RATE.put(2032, 1.0432);
        VALORIZATION_RATE.put(2033, 1.0414);
        VALORIZATION_RATE.put(2034, 1.0412);
        VALORIZATION_RATE.put(2035, 1.0410);
        VALORIZATION_RATE.put(2036, 1.0412);
        VALORIZATION_RATE.put(2037, 1.0411);
        VALORIZATION_RATE.put(2038, 1.0404);
        VALORIZATION_RATE.put(2039, 1.0398);
        VALORIZATION_RATE.put(2040, 1.0391);
        VALORIZATION_RATE.put(2041, 1.0388);
        VALORIZATION_RATE.put(2042, 1.0361);
        VALORIZATION_RATE.put(2043, 1.0330);
        VALORIZATION_RATE.put(2044, 1.0322);
        VALORIZATION_RATE.put(2045, 1.0327);
        VALORIZATION_RATE.put(2046, 1.0325);
        VALORIZATION_RATE.put(2047, 1.0325);
        VALORIZATION_RATE.put(2048, 1.0334);
        VALORIZATION_RATE.put(2049, 1.0331);
        VALORIZATION_RATE.put(2050, 1.0325);
        VALORIZATION_RATE.put(2051, 1.0321);
        VALORIZATION_RATE.put(2052, 1.0317);
        VALORIZATION_RATE.put(2053, 1.0317);
        VALORIZATION_RATE.put(2054, 1.0322);
        VALORIZATION_RATE.put(2055, 1.0328);
        VALORIZATION_RATE.put(2056, 1.0331);
        VALORIZATION_RATE.put(2057, 1.0331);
        VALORIZATION_RATE.put(2058, 1.0332);
        VALORIZATION_RATE.put(2059, 1.0336);
        VALORIZATION_RATE.put(2060, 1.0340);
        VALORIZATION_RATE.put(2061, 1.0343);
        VALORIZATION_RATE.put(2062, 1.0348);
        VALORIZATION_RATE.put(2063, 1.0352);
        VALORIZATION_RATE.put(2064, 1.0356);
        VALORIZATION_RATE.put(2065, 1.0359);
        VALORIZATION_RATE.put(2066, 1.0360);
        VALORIZATION_RATE.put(2067, 1.0356);
        VALORIZATION_RATE.put(2068, 1.0352);
        VALORIZATION_RATE.put(2069, 1.0348);
        VALORIZATION_RATE.put(2070, 1.0347);
        VALORIZATION_RATE.put(2071, 1.0350);
        VALORIZATION_RATE.put(2072, 1.0354);
        VALORIZATION_RATE.put(2073, 1.0357);
        VALORIZATION_RATE.put(2074, 1.0357);
        VALORIZATION_RATE.put(2075, 1.0354);
        VALORIZATION_RATE.put(2076, 1.0349);
        VALORIZATION_RATE.put(2077, 1.0349);
        VALORIZATION_RATE.put(2078, 1.0349);
        VALORIZATION_RATE.put(2079, 1.0348);
        VALORIZATION_RATE.put(2080, 1.0347);
        VALORIZATION_RATE.put(2081, 1.0347);
        VALORIZATION_RATE.put(2082, 1.0347);
        VALORIZATION_RATE.put(2083, 1.0347);
        VALORIZATION_RATE.put(2084, 1.0347);
        VALORIZATION_RATE.put(2085, 1.0347);
        VALORIZATION_RATE.put(2086, 1.0347);
        VALORIZATION_RATE.put(2087, 1.0347);
        VALORIZATION_RATE.put(2088, 1.0347);
        VALORIZATION_RATE.put(2089, 1.0347);
        VALORIZATION_RATE.put(2090, 1.0347);
        VALORIZATION_RATE.put(2091, 1.0347);
        VALORIZATION_RATE.put(2092, 1.0347);
        VALORIZATION_RATE.put(2093, 1.0347);
        VALORIZATION_RATE.put(2094, 1.0347);
        VALORIZATION_RATE.put(2095, 1.0347);
        VALORIZATION_RATE.put(2096, 1.0347);
        VALORIZATION_RATE.put(2097, 1.0347);
        VALORIZATION_RATE.put(2098, 1.0347);
        VALORIZATION_RATE.put(2099, 1.0347);
        VALORIZATION_RATE.put(2100, 1.0347);

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

    public double getAveragePensionAfterValorization(int year, String sex) {
        var cumulativeValorization = getCumulativeValorization(2025, year);

        return getAveragePension(year, sex) * cumulativeValorization;
    }

    public Double getValorizationRate(int year) {
        return VALORIZATION_RATE.getOrDefault(year, 1.025);
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
            cumulative *= (getWageGrowth(year));
        }
        return cumulative;
    }

    public double getCumulativeValorization(int fromYear, int toYear) {
        var cumulative = 1.0;
        for (var year = fromYear; year < toYear; year++) {
            cumulative *= (getValorizationRate(year));
        }
        return cumulative;
    }
}
