package com.ibm.api.cashew.voucher.db;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.api.cashew.beans.Voucher;

public interface MongoVoucherRepository extends MongoRepository<Voucher, String>, MongoVoucherCustomRepository
{

}
