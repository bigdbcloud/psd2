package com.ibm.api.cashew.db.mongo;

import java.util.Set;

import com.ibm.api.cashew.beans.Tag;

public interface MongoUserRepositoryCustom
{
	public long updateUserLock(String userId, boolean locked);

	public long updatePhone(String userId, String phone);

	public long updateEmail(String userId, String email);

	public long updateDOB(String userId, String dob);

	public long addTag(Set<Tag> tags, String userId);

}
