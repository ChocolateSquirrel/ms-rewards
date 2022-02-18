package tourGuide.services;

import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.VisitedLocation;

import java.util.UUID;

@Service
public class RewardsService {

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
    //Proximity in miles
    private static final int DEFAUlT_PROXIMITY_BUFFER = 10;;
    private static final int ATTRACTION_PROXIMITY_RANGE = 200;

    private int proximityBuffer = DEFAUlT_PROXIMITY_BUFFER;
    private final RewardCentral rewardCentral;

    public RewardsService(RewardCentral rewardCentral) {
        this.rewardCentral = rewardCentral;
    }

    public int getRewardPoints(UUID attractionId, UUID userId) {
        return rewardCentral.getAttractionRewardPoints(attractionId, userId);
    }

    /**
     * Compare the distance, between an attraction and a location, with the ATTRACTION_PROXIMITY_RANGE
     * @param attraction
     * @param location
     * @return
     *  <ul><li>true if the attraction is considered close enough of the location</li>
     *  <li>false if the attraction is considered too far form the location</li></ul>
     */
    public boolean isWithinAttractionProximity(Attraction attraction, Location location){
        return getDistance(attraction, location) > ATTRACTION_PROXIMITY_RANGE ? false : true;
    }

    /**
     * Compare the distance, between a visitedLocation and an attraction, with the proximityBuffer
     * @param visitedLocation
     * @param attraction
     * @return <ul><li>true if the attraction is considered close enough of the visitedLocation</li>
     *      *  <li>false if the attraction is considered too far form the visitedLocation</li></ul>
     */
    public boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction){
        return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
    }

    public void setProximityBuffer(int proximityBuffer){
        this.proximityBuffer = proximityBuffer;
    }

    /**
     * Retrieve distance in miles between an attraction and a location
     * @param attraction
     * @param location
     * @return
     */
    private double getDistance(Attraction attraction, Location location){
        return getDistance(new Location (attraction.latitude, attraction.longitude), location);
    }

    /**
     * Calculate distance in miles between two locations (from latitude and longitude of the location)
     * @param loc1
     * @param loc2
     * @return
     */
    private static double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
    }
}
