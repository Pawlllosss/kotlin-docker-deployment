package pl.oczadly.kotlin_app.hackyeah2025.pension.entity;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class PensionCalculationAuditingRepository {

    Map<UUID, PensionCalculationAuditing> db = new HashMap<>();

    public PensionCalculationAuditing save(PensionCalculationAuditing forecastResult) {
        db.put(UUID.randomUUID(), forecastResult);

        return forecastResult;
    }

    public List<PensionCalculationAuditing> getAll() {
        return db.values().stream().toList();
    }

}
