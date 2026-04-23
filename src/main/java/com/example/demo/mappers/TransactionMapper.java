package com.example.demo.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.demo.entities.Transaction;

@Mapper
public interface TransactionMapper {

    @Select("SELECT * FROM transactions WHERE account_id = #{id}")
    List<Transaction> findTransactionsById(@Param("id") Long id);

    @Insert("INSERT INTO transactions(account_id, amount, currency, direction, description) VALUES(#{accountId}, #{amount}, #{currency}, #{direction}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "transactionId")
    void insert(Transaction transaction);
}
