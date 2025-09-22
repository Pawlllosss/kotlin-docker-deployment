package pl.oczadly.kotlin_app.hackyeah2025.demo.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class DemoController(private val apiCallDemo: ApiCallDemo) {

    @GetMapping("/demos")
    fun getDemos(): List<String> = listOf("demo-values")

    @GetMapping("/demos3")
    fun getDemos2(): String = "new-demo-3"

    @GetMapping("/demo-api-call")
    fun apiCallDemo(): String? = apiCallDemo.makeDemoApiCall()

    @PutMapping("/dynamo-db/{email}")
    fun addEmail(@PathVariable(name = "email") email: String) = email
}
