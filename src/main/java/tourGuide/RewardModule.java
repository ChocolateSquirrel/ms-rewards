package tourGuide;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rewardCentral.RewardCentral;
import tourGuide.services.RewardsService;

@Configuration
public class RewardModule {

    @Bean
    public RewardsService getRewardsService() {
        return new RewardsService(getRewardCentral());
    }

    @Bean
    public RewardCentral getRewardCentral() {
        return new RewardCentral();
    }
}
