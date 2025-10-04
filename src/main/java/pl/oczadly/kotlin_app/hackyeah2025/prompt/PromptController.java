package pl.oczadly.kotlin_app.hackyeah2025.prompt;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/prompts")
public class PromptController {

    private final PromptRepository promptRepository;

    public PromptController(PromptRepository promptRepository) {
        this.promptRepository = promptRepository;
    }

    @PostMapping
    public void addPrompt(@RequestBody PromptDTO promptDTO) {
        Prompt prompt = new Prompt(UUID.randomUUID(), promptDTO.prompt(), promptDTO.response());
        promptRepository.save(prompt);
    }

    @GetMapping
    public List<Prompt> getAllPrompts() {
        return promptRepository.findAll();
    }
}
