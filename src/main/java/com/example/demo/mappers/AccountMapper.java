package com.example.demo.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.demo.entities.Account;

@Mapper
public interface AccountMapper {

    @Insert("INSERT INTO accounts(customer_id) VALUES(#{customerId})")
    @Options(useGeneratedKeys = true, keyProperty = "accountId")
    void insert(Account account);

    @Select("SELECT * FROM accounts WHERE account_id = #{id}")
    Account findById(@Param("id") Long id);

    @Select("SELECT account_id FROM accounts")
    List<Long> findAllIds();

    @Select("SELECT COUNT(*) FROM accounts")
    Long countAccountTableRows();
}
