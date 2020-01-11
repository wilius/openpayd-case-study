package com.openpayd.exchange.data.repository;

import com.openpayd.exchange.data.entity.Exchange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;

@Repository
public interface ExchangeRepository extends CrudRepository<Exchange, Long> {

    Page<Exchange> findAllByExchangeDateBetweenAndIdLessThanEqualOrderByIdDesc(ZonedDateTime start, ZonedDateTime end, long maxId, Pageable paging);

    Page<Exchange> findAllByExchangeDateBetweenOrderByIdDesc(ZonedDateTime start, ZonedDateTime end, Pageable paging);
}
