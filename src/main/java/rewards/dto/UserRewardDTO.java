package rewards.dto;

import lombok.Data;
import rewards.model.VisitedLocation;

import java.util.List;
import java.util.UUID;

@Data
public class UserRewardDTO {

    private List<VisitedLocation> visitedLocations;
    private UUID userId;

    public UserRewardDTO() {}

}
