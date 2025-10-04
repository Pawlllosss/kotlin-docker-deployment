package pl.oczadly.kotlin_app.hackyeah2025.pension.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.oczadly.kotlin_app.hackyeah2025.pension.entity.PensionCalculationAuditing;
import pl.oczadly.kotlin_app.hackyeah2025.pension.entity.PensionCalculationAuditingRepository;

import java.util.List;

@RestController
@RequestMapping("/pension")
@RequiredArgsConstructor
public class PensionAuditingController {

  private final PensionCalculationAuditingRepository pensionCalculationAuditingRepository;

  @GetMapping("/audit")
  public List<PensionCalculationAuditing> getAudit() {
    return pensionCalculationAuditingRepository.getAll();
  }
}
