package tourGuide;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.proxy.GpsProxy;
import tourGuide.services.RewardsService;

@SpringBootTest
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
        Location farLocation = new Location(10,10);
        assertFalse(rewardsService.isWithinAttractionProximity(attraction, farLocation));
    }

    @Test
    public void isWithinAttractionProximityTest_WithCloseLocation() {
        RewardsService rewardsService = new RewardsService(gpsProxy);
        Attraction attraction = gpsProxy.getAttractions().get(0);
        Location closeLocation = new Location(33, -118);
        assertTrue(rewardsService.isWithinAttractionProximity(attraction, closeLocation));
    }

    @Test
    public void getNearByAttractionsTest(){
        RewardsService rewardsService = new RewardsService(gpsProxy);
        List<Attraction> attractions = rewardsService.getNearByAttractions(UUID.randomUUID(), 5);
        assertEquals(5, attractions.size());
    }

	
}
