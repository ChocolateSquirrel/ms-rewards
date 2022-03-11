package tourGuide.model;

import lombok.Data;

@Data
public class UserReward {

    private final VisitedLocation visitedLocation;
    private final Attraction attraction;
    private int rewardPoints;

    public UserReward(VisitedLocation visitedLocation, Attraction attraction, int rewardPoints) {
        this.visitedLocation = visitedLocation;
        this.attraction = attraction;
        this.rewardPoints = rewardPoints;
    }
}