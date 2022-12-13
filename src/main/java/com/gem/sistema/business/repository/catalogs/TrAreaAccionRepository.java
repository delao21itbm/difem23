package com.gem.sistema.business.repository.catalogs;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.gem.sistema.business.domain.TrAreaAccion;

@Repository(value = "trAreaAccionRepository")
public interface TrAreaAccionRepository extends PagingAndSortingRepository<TrAreaAccion, Long> {

}
