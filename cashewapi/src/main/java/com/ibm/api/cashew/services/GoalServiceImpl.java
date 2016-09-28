package com.ibm.api.cashew.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.api.cashew.beans.Goal;
import com.ibm.api.cashew.db.mongo.MongoGoalRepository;

@Service
public class GoalServiceImpl implements GoalService
{
	@Autowired
	private MongoGoalRepository goalRepo;

	@Override
	public Goal saveGoal(Goal goal)
	{
		return goalRepo.save(goal);
	}

	@Override
	public List<Goal> getGoals(String username)
	{
		return goalRepo.findByUsername(username);
	}

}
