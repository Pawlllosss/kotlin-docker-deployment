package pl.oczadly.kotlin_app.hackyeah2025.pension.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Component
public class ForecastDataService {

    private final Map<Integer, Double> wageGrowth;
    private final Map<Integer, Double> inflationRate;
    private final Map<Integer, Map<String, Double>> averagePension;
    private final Map<Integer, Double> valorizationRate;
    private final Map<String, Double> lifeExpectancyAfterRetirement;

    public ForecastDataService() {
        this.wageGrowth = new HashMap<>();
        this.inflationRate = new HashMap<>();
        this.averagePension = new HashMap<>();
        this.valorizationRate = new HashMap<>();
        this.lifeExpectancyAfterRetirement = Map.of(
                "M", 19.0,
                "F", 24.0
        );
        initializeWageGrowth();
        initializeInflationRate();
        initializeValorizationRate();

        // Initialize with sample data - average pension (simplified, constant value)
        for (var year = 2025; year <= 2100; year++) {
            // TODO: use wage growth for calculation
            averagePension.put(year, Map.of("M", 4000.0, "F", 3000.0));
        }

    }

    private void initializeWageGrowth() {
        for (var year = 1900; year <= 2200; year++) {
            wageGrowth.put(year, 1.02);
        }
        wageGrowth.put(2022, 0.9800);
        wageGrowth.put(2023, 1.0030);
        wageGrowth.put(2024, 1.0340);
        wageGrowth.put(2025, 1.0370);
        wageGrowth.put(2026, 1.0350);
        wageGrowth.put(2027, 1.0300);
        wageGrowth.put(2028, 1.0290);
        wageGrowth.put(2029, 1.0290);
        wageGrowth.put(2030, 1.0290);
        wageGrowth.put(2031, 1.0290);
        wageGrowth.put(2032, 1.0290);
        IntStream.range(2033, 2035).forEach(value -> wageGrowth.put(value, 1.0290));
        wageGrowth.put(2035, 1.0280);
        IntStream.range(2036, 2040).forEach(value -> wageGrowth.put(value, 1.0280));
        wageGrowth.put(2040, 1.0270);
        IntStream.range(2041, 2045).forEach(value -> wageGrowth.put(value, 1.0270));
        wageGrowth.put(2045, 1.0260);
        IntStream.range(2046, 2050).forEach(value -> wageGrowth.put(value, 1.0260));
        wageGrowth.put(2050, 1.0250);
        IntStream.range(2051, 2055).forEach(value -> wageGrowth.put(value, 1.0250));
        wageGrowth.put(2055, 1.0240);
        IntStream.range(2056, 2060).forEach(value -> wageGrowth.put(value, 1.0240));
        wageGrowth.put(2060, 1.0240);
        IntStream.range(2061, 2065).forEach(value -> wageGrowth.put(value, 1.0240));
        wageGrowth.put(2065, 1.0230);
        IntStream.range(2066, 2070).forEach(value -> wageGrowth.put(value, 1.0220));
        wageGrowth.put(2070, 1.0220);
        IntStream.range(2071, 2075).forEach(value -> wageGrowth.put(value, 1.0220));
        wageGrowth.put(2075, 1.0210);
        IntStream.range(2076, 2080).forEach(value -> wageGrowth.put(value, 1.0210));
        wageGrowth.put(2080, 1.0200);

    }


    private void initializeInflationRate() {
        // Initialize with sample data - 2.5% inflation
        for (var year = 1900; year <= 2200; year++) {
            inflationRate.put(year, 1.025);
        }
        inflationRate.put(2022, 1.1350);
        inflationRate.put(2023, 1.0980);
        inflationRate.put(2024, 1.0480);
        inflationRate.put(2025, 1.0310);
        inflationRate.put(2026, 1.0250);
        inflationRate.put(2027, 1.0250);
        inflationRate.put(2028, 1.0250);
        inflationRate.put(2029, 1.0250);
        inflationRate.put(2030, 1.0250);
        inflationRate.put(2031, 1.0250);
        inflationRate.put(2032, 1.0250);
        inflationRate.put(2035, 1.0250);
        inflationRate.put(2040, 1.0250);
        inflationRate.put(2045, 1.0250);
        inflationRate.put(2050, 1.0250);
        inflationRate.put(2055, 1.0250);
    }

    private void initializeValorizationRate() {
        valorizationRate.put(2014, 1.0206);
        valorizationRate.put(2015, 1.0537);
        valorizationRate.put(2016, 1.0637);
        valorizationRate.put(2017, 1.0868);
        valorizationRate.put(2018, 1.0920);
        valorizationRate.put(2019, 1.0894);
        valorizationRate.put(2020, 1.0541);
        valorizationRate.put(2021, 1.0933);
        valorizationRate.put(2022, 1.1440);
        valorizationRate.put(2023, 1.1487);
        valorizationRate.put(2024, 1.1441);
        valorizationRate.put(2025, 1.0739);
        valorizationRate.put(2026, 1.0661);
        valorizationRate.put(2027, 1.0573);
        valorizationRate.put(2028, 1.0519);
        valorizationRate.put(2029, 1.0493);
        valorizationRate.put(2030, 1.0472);
        valorizationRate.put(2031, 1.0450);
        valorizationRate.put(2032, 1.0432);
        valorizationRate.put(2033, 1.0414);
        valorizationRate.put(2034, 1.0412);
        valorizationRate.put(2035, 1.0410);
        valorizationRate.put(2036, 1.0412);
        valorizationRate.put(2037, 1.0411);
        valorizationRate.put(2038, 1.0404);
        valorizationRate.put(2039, 1.0398);
        valorizationRate.put(2040, 1.0391);
        valorizationRate.put(2041, 1.0388);
        valorizationRate.put(2042, 1.0361);
        valorizationRate.put(2043, 1.0330);
        valorizationRate.put(2044, 1.0322);
        valorizationRate.put(2045, 1.0327);
        valorizationRate.put(2046, 1.0325);
        valorizationRate.put(2047, 1.0325);
        valorizationRate.put(2048, 1.0334);
        valorizationRate.put(2049, 1.0331);
        valorizationRate.put(2050, 1.0325);
        valorizationRate.put(2051, 1.0321);
        valorizationRate.put(2052, 1.0317);
        valorizationRate.put(2053, 1.0317);
        valorizationRate.put(2054, 1.0322);
        valorizationRate.put(2055, 1.0328);
        valorizationRate.put(2056, 1.0331);
        valorizationRate.put(2057, 1.0331);
        valorizationRate.put(2058, 1.0332);
        valorizationRate.put(2059, 1.0336);
        valorizationRate.put(2060, 1.0340);
        valorizationRate.put(2061, 1.0343);
        valorizationRate.put(2062, 1.0348);
        valorizationRate.put(2063, 1.0352);
        valorizationRate.put(2064, 1.0356);
        valorizationRate.put(2065, 1.0359);
        valorizationRate.put(2066, 1.0360);
        valorizationRate.put(2067, 1.0356);
        valorizationRate.put(2068, 1.0352);
        valorizationRate.put(2069, 1.0348);
        valorizationRate.put(2070, 1.0347);
        valorizationRate.put(2071, 1.0350);
        valorizationRate.put(2072, 1.0354);
        valorizationRate.put(2073, 1.0357);
        valorizationRate.put(2074, 1.0357);
        valorizationRate.put(2075, 1.0354);
        valorizationRate.put(2076, 1.0349);
        valorizationRate.put(2077, 1.0349);
        valorizationRate.put(2078, 1.0349);
        valorizationRate.put(2079, 1.0348);
        valorizationRate.put(2080, 1.0347);
        valorizationRate.put(2081, 1.0347);
        valorizationRate.put(2082, 1.0347);
        valorizationRate.put(2083, 1.0347);
        valorizationRate.put(2084, 1.0347);
        valorizationRate.put(2085, 1.0347);
        valorizationRate.put(2086, 1.0347);
        valorizationRate.put(2087, 1.0347);
        valorizationRate.put(2088, 1.0347);
        valorizationRate.put(2089, 1.0347);
        valorizationRate.put(2090, 1.0347);
        valorizationRate.put(2091, 1.0347);
        valorizationRate.put(2092, 1.0347);
        valorizationRate.put(2093, 1.0347);
        valorizationRate.put(2094, 1.0347);
        valorizationRate.put(2095, 1.0347);
        valorizationRate.put(2096, 1.0347);
        valorizationRate.put(2097, 1.0347);
        valorizationRate.put(2098, 1.0347);
        valorizationRate.put(2099, 1.0347);
        valorizationRate.put(2100, 1.0347);

    }

    public double getWageGrowth(int year) {
        return wageGrowth.getOrDefault(year, 1.03);
    }

    public double getInflationRate(int year) {
        return this.inflationRate.getOrDefault(year, 1.025);
    }

    public double getAveragePension(int year, String sex) {
        return averagePension.get(year).get(sex);
    }

    public double getAveragePensionAfterValorization(int year, String sex) {
        var cumulativeValorization = getCumulativeValorization(2025, year);

        return getAveragePension(year, sex) * cumulativeValorization;
    }

    public double getValorizationRate(Integer year) {
        return valorizationRate.getOrDefault(year, 1.025);
    }

    public double getLifeExpectancyMonths(int year, String sex) {
        return lifeExpectancyAfterRetirement.getOrDefault(sex.toLowerCase(), 20.0) * 12;
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
