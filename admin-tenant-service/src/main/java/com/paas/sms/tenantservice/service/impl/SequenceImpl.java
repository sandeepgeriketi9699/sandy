package com.paas.sms.tenantservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.paas.sms.tenantservice.document.SequenceId;
import com.paas.sms.tenantservice.service.Sequence;



@Repository
public class SequenceImpl implements Sequence {

	@Autowired
	private MongoOperations mongoOperation;

	@Override
	public long getNextSequenceId(String key) {
	
		Query query = new Query(Criteria.where("_id").is(key));
		Update update = new Update();
		update.inc("seq", 1);
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		SequenceId seqId = mongoOperation.findAndModify(query, update, options, SequenceId.class);
		return seqId.getSeq();
	
}

}