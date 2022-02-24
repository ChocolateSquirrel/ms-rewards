package tourGuide.services;

import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.VisitedLocation;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
public class RewardsService {

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
    //Proximity in miles
    private static final int DEFAUlT_PROXIMITY_BUFFER = 10;;
    private static final int ATTRACTION_PROXIMITY_RANGE = 200;

    private int proximityBuffer = DEFAUlT_PROXIMITY_BUFFER;
    private final RewardCentral rewardCentral;

    public RewardsService() {
        this.rewardCentral = new RewardCentral() ;
    }

    public int getRewardPoints(UUID attractionId, UUID userId) {
        return rewardCentral.getAttractionRewardPoints(attractionId, userId);
    }

    public int getBidon(UUID attractionId) {
        try {
            TimeUnit.MILLISECONDS.sleep((long) ThreadLocalRandom.current().nextInt(1, 1000));
        } catch (InterruptedException var4) {
        }

        int randomInt = ThreadLocalRandom.current().nextInt(1, 1000);
        return randomInt;
    }

/*    *//**
     * Add new rewards if the user does not have them : to add reward :
     * <ul><li>the attraction must not have already been take into account</li>
     * <li>the attraction must be close to a visitedLocation (within the meaning of proximityBuffer) of the user</li></ul>
     * @param user
     *//*
    public void calculateRewards(User user) {
        List<VisitedLocation> userLocations = user.getVisitedLocations();
        List<Attraction> attractions = gpsUtil.getAttractions();

        for(VisitedLocation visitedLocation : userLocations) {
            for(Attraction attraction : attractions) {
                if(user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
                    if(nearAttraction(visitedLocation, attraction)) {
                        user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
                    }
                }
            }
        }
    }*/

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
