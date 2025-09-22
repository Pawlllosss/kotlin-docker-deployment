package pl.oczadly.kotlin_app.hackyeah2025.prompt

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/prompts")
class PromptController(private val promptRepository: PromptRepository) {

    @PostMapping
    fun addPrompt(@RequestBody promptDTO: PromptDTO) {
        val prompt = Prompt(prompt = promptDTO.prompt, response = promptDTO.response)
        promptRepository.save(prompt)
    }

    @GetMapping
    fun getAllPrompts() = promptRepository.findAll()
}