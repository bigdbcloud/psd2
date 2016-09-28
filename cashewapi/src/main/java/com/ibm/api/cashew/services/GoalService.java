package com.ibm.api.cashew.services;

import java.util.List;

import com.ibm.api.cashew.beans.Goal;

public interface GoalService
{
	public Goal saveGoal(Goal goal);

	public List<Goal> getGoals(String username);
}
