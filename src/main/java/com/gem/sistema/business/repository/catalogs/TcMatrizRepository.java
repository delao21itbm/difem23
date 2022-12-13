package com.gem.sistema.business.repository.catalogs;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.gem.sistema.business.domain.TcMatriz;

@Repository("tcMatrizRepository")
public interface TcMatrizRepository extends PagingAndSortingRepository<TcMatriz, Long>{

	List<TcMatriz> findAll(Sort sort);
	
	List<TcMatriz> findAllBySectorOrderByDependenciaClaveAsc(Integer sector);
	
	List<TcMatriz> findAllBySectorAndDependenciaClaveStartingWithIgnoreCaseOrderByDependenciaClaveAsc(Integer sector, String dependencia);
}
