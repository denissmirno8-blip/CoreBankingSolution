package com.example.demo.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.entities.Balance;

@Mapper
public interface BalanceMapper {

    @Insert("INSERT INTO balances(account_id, amount, currency) VALUES (#{accountId}, #{amount}, #{currency})")
    @Options(useGeneratedKeys = true, keyProperty = "balanceId")
    void insert(Balance balance);

    @Select("SELECT * FROM balances WHERE account_id = #{accountId}")
    List<Balance> findByAccountId(@Param("accountId") Long accountId);

    @Select("SELECT amount FROM balances WHERE account_id = #{accountId} AND currency = #{currency}")
    double findAmountByAccountIdAndCurrency(@Param("currency") String currency, @Param("accountId") Long accountId);

    @Select("SELECT DISTINCT currency FROM balances WHERE account_id = #{accountId}")
    List<String> findAccountsCurrency(@Param("accountId") Long accountId);
    // Update balance for transactions
    @Update("UPDATE balances SET amount = #{amount} WHERE currency = #{currency} AND account_id = #{accountId}")
    void updateBalanceAmount(@Param("amount") double amount, @Param("currency") String currency,
            @Param("accountId") Long accountId);
}