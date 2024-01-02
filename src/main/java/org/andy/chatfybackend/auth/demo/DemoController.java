package org.andy.chatfybackend.auth.demo;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
@Tag(name = "Demo")
public class DemoController {
    @GetMapping("/hello")
    public ResponseEntity<String > hello() {
        return ResponseEntity.ok("Hello World!");
    }

    @GetMapping("/goodbye")
    public ResponseEntity<String > goodbye() {
        return ResponseEntity.ok("Goodbye World!");
    }
}
