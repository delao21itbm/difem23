package com.gem.sistema.business.repository.catalogs;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.gem.sistema.business.domain.TcMatrizNivel;

@Repository("tcMatrizNivelRepository")
public interface TcMatrizNivelRepository extends PagingAndSortingRepository<TcMatrizNivel, Long>{
	
	List<TcMatrizNivel> findAllByMatrizIdAndNivelClave(Long idMatriz, String claveNivel);
	
	List<TcMatrizNivel> findAllByMatrizIdAndNivelClaveAndPadre(Long idMatriz, String claveNivel, Long idPadre);
}
