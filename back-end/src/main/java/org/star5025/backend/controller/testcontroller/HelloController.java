package org.star5025.backend.controller.testcontroller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.star5025.backend.result.Result;

@RestController
@RequestMapping
@CrossOrigin
public class HelloController {
    @GetMapping("/api/hello")
    public Result<String> sayHello() {
        String hello = "hello world";
        return Result.success(hello);
    }
}
