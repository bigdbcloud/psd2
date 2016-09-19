package com.ibm.api.cashew.elastic.db;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ibm.api.cashew.elastic.beans.Transaction;

public interface ElasticTransactionRepository extends ElasticsearchRepository<Transaction,String>{

}
