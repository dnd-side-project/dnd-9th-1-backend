package com.backend.global.event;

import com.backend.detailgoal.domain.DetailGoal;
import com.backend.detailgoal.domain.DetailGoalRepository;
import com.backend.goal.domain.RemoveRelatedDetailGoalEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GoalEventHandler {

    private final DetailGoalRepository detailGoalRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void removeDetailGoalList(RemoveRelatedDetailGoalEvent event) {

        List<DetailGoal> detailGoalList = detailGoalRepository.findAllByGoalIdAndIsDeletedFalse(event.goalId());
        detailGoalList.forEach((DetailGoal::remove));
    }



}
