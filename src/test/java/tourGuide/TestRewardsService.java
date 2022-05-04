package tourGuide;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tourGuide.dto.UserRewardDTO;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.UserReward;
import tourGuide.model.VisitedLocation;
import tourGuide.proxy.GpsProxy;
import tourGuide.services.RewardsService;

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
