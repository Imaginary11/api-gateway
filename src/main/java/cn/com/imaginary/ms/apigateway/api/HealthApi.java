package cn.com.imaginary.ms.apigateway.api;

import org.springframework.boot.actuate.health.Status;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Imaginary
 * @version : V1.0
 * @date : 2018/11/15 14:12
 */
@RestController
public class HealthApi {
    @GetMapping("/gateway/health")
    public Status health() {
        return new Status("UP");
    }
}
