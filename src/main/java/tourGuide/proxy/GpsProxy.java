package tourGuide.proxy;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import tourGuide.model.Attraction;

import java.util.List;

@FeignClient(name = "ms-gps", url = "localhost:6060")
public interface GpsProxy {

    @GetMapping("getAttractions")
    public List<Attraction> getAttractions();

}
