package pl.oczadly.kotlin_app.hackyeah2025.demo.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DemoController {

    private final ApiCallDemo apiCallDemo;

    public DemoController(ApiCallDemo apiCallDemo) {
        this.apiCallDemo = apiCallDemo;
    }

    @GetMapping("/demos")
    public List<String> getDemos() {
        return List.of("demo-values");
    }

    @GetMapping("/demos3")
    public String getDemos2() {
        return "new-demo-3";
    }

    @GetMapping("/demo-api-call")
    public String apiCallDemo() {
        return apiCallDemo.makeDemoApiCall();
    }

    @PutMapping("/dynamo-db/{email}")
    public String addEmail(@PathVariable("email") String email) {
        return email;
    }
}
