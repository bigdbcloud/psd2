package com.ibm.api.cashew.db.elastic;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ibm.api.cashew.beans.ElasticTransaction;

public interface ElasticTransactionRepository extends ElasticsearchRepository<ElasticTransaction,String>,ElasticTransactionCustomRepository{

}
