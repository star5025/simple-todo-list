package org.star5025.backend.controller.testcontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParamController {

    @GetMapping("/fuck")
    public String fuck(@RequestParam String name) {
        return "fuck" + name;
    }
}
