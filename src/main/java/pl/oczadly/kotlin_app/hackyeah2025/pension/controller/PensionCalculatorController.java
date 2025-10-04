package pl.oczadly.kotlin_app.hackyeah2025.pension.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.oczadly.kotlin_app.hackyeah2025.pension.model.PensionRequest;
import pl.oczadly.kotlin_app.hackyeah2025.pension.model.PensionResponse;
import pl.oczadly.kotlin_app.hackyeah2025.pension.service.PensionCalculatorService;

@RestController
@RequestMapping("/pension")
@RequiredArgsConstructor
public class PensionCalculatorController {

  private final PensionCalculatorService pensionCalculatorService;

  @PostMapping("/calculate")
  public PensionResponse calculatePension(@RequestBody PensionRequest request) {
    return pensionCalculatorService.calculatePension(request);
  }
}
