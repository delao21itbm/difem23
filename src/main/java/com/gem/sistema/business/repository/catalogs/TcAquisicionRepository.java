package com.gem.sistema.business.repository.catalogs;

import java.util.List;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.gem.sistema.business.domain.Actividad;
import com.gem.sistema.business.domain.TcAdquisicion;

// TODO: Auto-generated Javadoc
/**
 * The Interface TcAquisicionRepository.
 */
@Repository(value = "tcAquisicionRepository")
public interface TcAquisicionRepository
		extends PagingAndSortingRepository<TcAdquisicion, Long>, QueryDslPredicateExecutor<Actividad> {
	List<TcAdquisicion> findAll();
}
