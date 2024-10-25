package kr.homeservice;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Value("${message.domain}")
    private String domain;

    @GetMapping("/")
    public String home() {
        return "Hello World"+ domain;
    }
}
