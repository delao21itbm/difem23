package com.gem.sistema.business.repository.catalogs;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.gem.sistema.business.domain.TcFicha;

@Repository("tcFichaRepository")
public interface TcFichaRepository extends PagingAndSortingRepository<TcFicha, Long>{

	TcFicha findByMatrizNivelId(Long idMatrizNivel);
	
	TcFicha findByMatrizNivelIdAndIndicadorId(Long idMatrizNivel, Long idIndicador);
	
	List<TcFicha> findAllByIndicadorCodigoStartingWith(String codigo);
	
	List<TcFicha> findAll(Sort sort);
}
