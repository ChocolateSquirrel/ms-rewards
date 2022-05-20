package rewards.controller;

import org.springframework.web.bind.annotation.*;
import rewards.dto.DistanceDTO;
import rewards.dto.AskNearAttractionsDTO;
import rewards.dto.UserRewardDTO;
import rewards.model.Attraction;
import rewards.model.UserReward;
import rewards.services.RewardsService;

import java.util.List;
import java.util.UUID;

@RestController
public class RewardController {

    private final RewardsService rewardsService;

    public RewardController(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }


    @GetMapping("getRewardPoints/{attractionId}/{userId}")
    public int getRewardPoint(@PathVariable String attractionId, @PathVariable String userId){
        return rewardsService.getRewardPoints(UUID.fromString(attractionId), UUID.fromString(userId));
    }

    @PostMapping("getNearByAttractions")
    public List<Attraction> getNearByAttractions(@RequestBody AskNearAttractionsDTO askNearAttractionsDTO){
        return rewardsService.getNearByAttractions(askNearAttractionsDTO.getLocation(), askNearAttractionsDTO.getNbAttractions());
    }

    @PostMapping("getNearestAttractions")
    public List<Attraction> getNearestAttractions(@RequestBody AskNearAttractionsDTO askNearAttractionsDTO){
        return rewardsService.getNearestAttractions(askNearAttractionsDTO.getLocation(), askNearAttractionsDTO.getNbAttractions());
    }

    @PostMapping("setProximityBuffer")
    public void setProximityBuffer(@RequestBody int proximityBuffer){
        rewardsService.setProximityBuffer(proximityBuffer);
    }

    @PostMapping("setDefaultProximityBuffer")
    public void setDefaultProximityBuffer(){
        rewardsService.setDefaultProximityBuffer();
    }

    @PostMapping("calculateRewards")
    public List<UserReward> calculateRewards(@RequestBody UserRewardDTO userRewardDTO){
        return rewardsService.calculateRewards(userRewardDTO);
    }

    @PostMapping("getDistance")
    public double getDistance(@RequestBody DistanceDTO distanceDTO){
        return rewardsService.getDistance(distanceDTO.getLoc1(), distanceDTO.getLoc2());
    }



}
