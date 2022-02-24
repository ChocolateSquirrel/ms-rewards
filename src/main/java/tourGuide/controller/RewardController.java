package tourGuide.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.model.Attraction;
import tourGuide.proxy.GpsProxy;
import tourGuide.services.RewardsService;

import java.util.List;
import java.util.UUID;

@RestController
public class RewardController {

    private final RewardsService rewardsService;
    @Autowired
    GpsProxy gpsProxy;

    public RewardController(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }

    @GetMapping("getRewardPoints/{attractionId}/{userId}")
    public int getRewardPoint(@RequestParam String attractionId, @RequestParam String userID){
        System.out.println("On passe dans le controller");
        return rewardsService.getRewardPoints(UUID.fromString(attractionId), UUID.fromString(userID));
    }

    @GetMapping("getBidon/{attractionId}")
    public int getBidon(@PathVariable("attractionId") String attractionId){
        return 10;
    }

    @GetMapping("getAttractions")
    public List<Attraction> getAttractions(){
        return gpsProxy.getAttractions();
    }

    @GetMapping("getStatusRewards")
    public String status(){
        return "C'est ok ! ";
    }


}
