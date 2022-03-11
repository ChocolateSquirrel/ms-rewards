package tourGuide.dto;

import lombok.Data;
import tourGuide.model.Location;

@Data
public class DistanceDTO {
    private Location loc1;
    private Location loc2;

    public DistanceDTO() {}
}
