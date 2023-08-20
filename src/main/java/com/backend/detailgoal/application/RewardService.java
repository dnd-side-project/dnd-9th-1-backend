package com.backend.detailgoal.application;

import com.backend.goal.domain.RewardType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RewardService {

    private Random random = new Random();
    private static final int NUM_GEM_TYPES = RewardType.values().length;

    public RewardType provideReward()
    {
        int randomIndex = random.nextInt(NUM_GEM_TYPES);
        return RewardType.values()[randomIndex];
    }

}
