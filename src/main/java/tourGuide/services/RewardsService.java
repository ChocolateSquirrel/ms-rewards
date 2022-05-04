package tourGuide.services;

import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;
import tourGuide.dto.UserRewardDTO;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.UserReward;
import tourGuide.model.VisitedLocation;
import tourGuide.proxy.GpsProxy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RewardsService {

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
    //Proximity in miles
    private static final int DEFAUlT_PROXIMITY_BUFFER = 10;;
    private static final int ATTRACTION_PROXIMITY_RANGE = 200;

    private int proximityBuffer = DEFAUlT_PROXIMITY_BUFFER;

    private final RewardCentral rewardCentral;
    private final GpsProxy gpsProxy;

    public RewardsService(GpsProxy gpsProxy) {
        this.gpsProxy = gpsProxy;
        this.rewardCentral = new RewardCentral() ;
    }

    public void setProximityBuffer(int proximityBuffer) {
        this.proximityBuffer = proximityBuffer;
    }

    public void setDefaultProximityBuffer() {
        proximityBuffer = DEFAUlT_PROXIMITY_BUFFER;
    }

    public int getRewardPoints(UUID attractionId, UUID userId) {
        return rewardCentral.getAttractionRewardPoints(attractionId, userId);
    }

    public List<Attraction> getNearByAttractions(Location location, int nbAttractions) {
        return sortedAttractionsByDistanceFromLocation(location).subList(0, nbAttractions);
    }

    public List<Attraction> getNearestAttractions(Location location, int nbAttractions){
        List<Attraction> nearestAttractions = new ArrayList<>();
        List<Attraction> nearByAttractions = sortedAttractionsByDistanceFromLocation(location).subList(0, nbAttractions);
        for (Attraction att : nearByAttractions){
            if(isWithinAttractionProximity(att, location)){
                nearestAttractions.add(att);
            }
        }
        return nearestAttractions;
    }

    private List<Attraction> sortedAttractionsByDistanceFromLocation(Location location) {
        return gpsProxy.getAttractions().stream().sorted(
                (att1, att2) -> Double.compare(getDistance(att1, location), getDistance(att2, location))
        ).collect(Collectors.toList());
    }

    public List<UserReward> calculateRewards(UserRewardDTO calculateRewardDTO) {
        List<UserReward> rewards = new ArrayList<>();
        for (VisitedLocation visitedLoc : calculateRewardDTO.getVisitedLocations()){
            for (Attraction attraction : gpsProxy.getAttractions()){
                if (nearAttraction(visitedLoc, attraction)){
                    //Make sure an attraction is counted only once
                    if (!isAttractionAlreadyInRewards(rewards, attraction)){
                        int rewardPoints = getRewardPoints(attraction.getAttractionId(), calculateRewardDTO.getUserId());
                        rewards.add(new UserReward(visitedLoc, attraction, rewardPoints));
                    }
                }
            }
        }
        return rewards;
    }

    private boolean isAttractionAlreadyInRewards(List<UserReward> userRewards, Attraction attraction){
        return userRewards.stream().filter(userReward -> userReward.getAttraction().getAttractionName().equals(attraction.getAttractionName())).count() == 0 ? false : true;
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
        return getDistance(attraction, visitedLocation.getLocation()) > proximityBuffer ? false : true;
    }

    /**
     * Retrieve distance in miles between an attraction and a location
     * @param attraction
     * @param location
     * @return
     */
    private static double getDistance(Attraction attraction, Location location){
        return getDistance(new Location (attraction.getLatitude(), attraction.getLongitude()), location);
    }

    /**
     * Calculate distance in miles between two locations (from latitude and longitude of the location)
     * @param loc1
     * @param loc2
     * @return
     */
    public static double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.getLatitude());
        double lon1 = Math.toRadians(loc1.getLongitude());
        double lat2 = Math.toRadians(loc2.getLatitude());
        double lon2 = Math.toRadians(loc2.getLongitude());

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
    }

}
