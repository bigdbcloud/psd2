package com.ibm.api.cashew.db.elastic;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ibm.api.cashew.beans.Transaction;

public interface ElasticTransactionRepository extends ElasticsearchRepository<Transaction,String>{

}
