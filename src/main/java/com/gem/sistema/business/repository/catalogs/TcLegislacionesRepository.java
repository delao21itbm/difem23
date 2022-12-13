package com.gem.sistema.business.repository.catalogs;

import java.util.List;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.gem.sistema.business.domain.TcLegislacion;

// TODO: Auto-generated Javadoc
/**
 * The Interface TcLegislacionesRepository.
 */
@Repository(value = "tcLegislacionesRepository")
public interface TcLegislacionesRepository
		extends PagingAndSortingRepository<TcLegislacion, Long>, QueryDslPredicateExecutor<TcLegislacion> {

	List<TcLegislacion> findAll();

}
