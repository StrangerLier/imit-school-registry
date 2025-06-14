package omsu.mim.imit.school.registry.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @RequestMapping(value = "/{path:^(?!api).*$)")
    public String forward() {
        return "forward:/index.html";
    }
}