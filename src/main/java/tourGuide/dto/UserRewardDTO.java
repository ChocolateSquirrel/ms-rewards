package tourGuide.dto;

import lombok.Data;
import tourGuide.model.VisitedLocation;

import java.util.List;
import java.util.UUID;

@Data
public class UserRewardDTO {

    private List<VisitedLocation> visitedLocations;
    private UUID userId;

    public UserRewardDTO() {}

}
