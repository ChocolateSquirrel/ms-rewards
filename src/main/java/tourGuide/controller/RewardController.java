package tourGuide.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.services.RewardsService;

import java.util.UUID;

@RestController
public class RewardController {

    private final RewardsService rewardsService;

    public RewardController(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }

    @GetMapping("getRewardPoints/{attractionId}/{userId}")
    public int getRewardPoint(@PathVariable UUID attractionId, @PathVariable UUID userID){
        return rewardsService.getRewardPoints(attractionId, userID);
    }


}
