package com.gem.sistema.business.repository.catalogs;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.gem.sistema.business.domain.TcMovimiento;
import com.gem.sistema.business.domain.TcTipoMovimiento;

@Repository(value = "movimientoRepository")
public interface TcMovimientoRepository extends PagingAndSortingRepository<TcMovimiento, Long> {
	List<TcMovimiento> findAllByMesAndTipoMovimiento(Integer mes, TcTipoMovimiento tipoMovimiento);

	@Query(value = "SELECT MAX(FOLIO)+1 FROM TC_MOVIMIENTOS ", nativeQuery = true)
	Integer getNexFolio();

	List<TcMovimiento> findAllByFolio(Integer folio);

	
}
