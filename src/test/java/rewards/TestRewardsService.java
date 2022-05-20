package rewards;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rewards.dto.UserRewardDTO;
import rewards.model.Attraction;
import rewards.model.Location;
import rewards.model.UserReward;
import rewards.model.VisitedLocation;
import rewards.proxy.GpsProxy;
import rewards.services.RewardsService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRewardsService {

    @Autowired
    GpsProxy gpsProxy;

	@BeforeClass
	public static void setup(){
		Locale.setDefault(Locale.US);
	}

    @Test
    public void getNearByAttractionsTest() {
        RewardsService rewardsService = new RewardsService(gpsProxy);
        Location randomLocation = new Location(33, -118);

        List<Attraction> attractions = rewardsService.getNearByAttractions(randomLocation, 10);
        double dist1 = RewardsService.getDistance(attractions.get(0), randomLocation);
        double dist2 = RewardsService.getDistance(attractions.get(1), randomLocation);

        assertEquals(10, attractions.size());
        assertTrue(dist1 <= dist2);
    }

    @Test
    public void getNearestAttractionsTest_LessThanAsk() {
        RewardsService rewardsService = new RewardsService(gpsProxy);
        Location randomLocation = new Location(33, -118);

        List<Attraction> attractions = rewardsService.getNearestAttractions(randomLocation, 10);//ATTRACTION_PROXIMITY_RANGE = 200
        double dist1 = RewardsService.getDistance(attractions.get(0), randomLocation);
        double dist2 = RewardsService.getDistance(attractions.get(1), randomLocation);

        assertEquals(3, attractions.size());
        assertTrue(dist1 <= dist2);
    }

    @Test
    public void getNearestAttractionsTest_MoreThanAsk() {
        RewardsService rewardsService = new RewardsService(gpsProxy);
        Location randomLocation = new Location(33, -118);

        List<Attraction> attractions = rewardsService.getNearestAttractions(randomLocation, 2);//ATTRACTION_PROXIMITY_RANGE = 200
        double dist1 = RewardsService.getDistance(attractions.get(0), randomLocation);
        double dist2 = RewardsService.getDistance(attractions.get(1), randomLocation);

        assertEquals(2, attractions.size());
        assertTrue(dist1 <= dist2);
    }
	
	@Test
	public void isWithinAttractionProximityTest_WithSameLocation() {
		RewardsService rewardsService = new RewardsService(gpsProxy);
		Attraction attraction = gpsProxy.getAttractions().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}

    @Test
    public void isWithinAttractionProximityTest_WithLocationDistant() {
        RewardsService rewardsService = new RewardsService(gpsProxy);
        Attraction attraction = gpsProxy.getAttractions().get(0);
        Location farLocation = new Location(10,10);//ATTRACTION_PROXIMITY_RANGE = 200
        assertFalse(rewardsService.isWithinAttractionProximity(attraction, farLocation));
    }

    @Test
    public void isWithinAttractionProximityTest_WithCloseLocation() {
        RewardsService rewardsService = new RewardsService(gpsProxy);
        Attraction attraction = gpsProxy.getAttractions().get(0);
        Location closeLocation = new Location(33, -118);//ATTRACTION_PROXIMITY_RANGE = 200
        assertTrue(rewardsService.isWithinAttractionProximity(attraction, closeLocation));
    }

    @Test
    public void calculateRewards_nearAllAttractions(){
        RewardsService rewardsService = new RewardsService(gpsProxy);
        rewardsService.setProximityBuffer(Integer.MAX_VALUE);

        UUID userID = UUID.randomUUID();
        Location location = new Location(33, -118);
        VisitedLocation visitedLocation1 = new VisitedLocation(userID, location, null);
        List<VisitedLocation> visitedLocationList = new ArrayList<>();
        visitedLocationList.add(visitedLocation1);
        UserRewardDTO userRewardDTO = new UserRewardDTO();
        userRewardDTO.setUserId(userID);
        userRewardDTO.setVisitedLocations(visitedLocationList);

        List<UserReward> list = rewardsService.calculateRewards(userRewardDTO);

        assertEquals(gpsProxy.getAttractions().size(), list.size());

    }

}
