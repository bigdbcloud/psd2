package com.ibm.api.cashew.db.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.api.cashew.beans.Goal;

public interface MongoGoalRepository extends MongoRepository<Goal, String>
{
	public List<Goal> findByUsername(String username);
}
