package com.openpayd.exchange.data.repository;

import com.openpayd.exchange.data.entity.Conversion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversionRepository extends CrudRepository<Conversion, Long> {
}
