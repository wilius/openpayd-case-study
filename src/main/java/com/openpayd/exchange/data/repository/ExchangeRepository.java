package com.openpayd.exchange.data.repository;

import com.openpayd.exchange.data.entity.Exchange;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRepository extends CrudRepository<Exchange, Long> {
}
