package rewards.dto;

import lombok.Data;
import rewards.model.Location;

@Data
public class DistanceDTO {
    private Location loc1;
    private Location loc2;

    public DistanceDTO() {}
}
