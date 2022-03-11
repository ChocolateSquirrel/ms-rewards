package tourGuide.controller;

import org.springframework.web.bind.annotation.*;
import tourGuide.dto.DistanceDTO;
import tourGuide.dto.UserRewardDTO;
import tourGuide.model.Attraction;
import tourGuide.model.UserReward;
import tourGuide.services.RewardsService;

import java.util.List;
import java.util.UUID;

@RestController
public class RewardController {

    private final RewardsService rewardsService;

    public RewardController(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }

/*    private final GpsProxy gpsProxy;

    public RewardController(RewardsService rewardsService, GpsProxy gpsProxy) {
        this.rewardsService = rewardsService;
        this.gpsProxy = gpsProxy;
    }*/



    @GetMapping("getRewardPoints/{attractionId}/{userId}")
    public int getRewardPoint(@PathVariable String attractionId, @PathVariable String userId){
        return rewardsService.getRewardPoints(UUID.fromString(attractionId), UUID.fromString(userId));
    }

    @GetMapping("getNearByAttractions/{userId}/{number}")
    public List<Attraction> getNearByAttractions(@PathVariable String userId, @PathVariable int number){
        return rewardsService.getNearByAttractions(UUID.fromString(userId), number);
    }

    @GetMapping("getNearestAttractions/{userId}")
    public List<Attraction> getNearestAttractions(@PathVariable String userId){
        return rewardsService.getNearestAttractions(UUID.fromString(userId));
    }

    /*@GetMapping("getAttractions")
    public List<Attraction> getAttractions(){
        return gpsProxy.getAttractions();
    }*/

    @PostMapping("calculateRewards")
    public List<UserReward> calculateRewards(@RequestBody UserRewardDTO userRewardDTO){
        return rewardsService.calculateRewards(userRewardDTO);
    }

    @PostMapping("getDistance")// get distance
    public double getDistance(@RequestBody DistanceDTO distanceDTO){
        return rewardsService.getDistance(distanceDTO.getLoc1(), distanceDTO.getLoc2());
    }



}
