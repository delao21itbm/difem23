package com.gem.sistema.business.repository.catalogs;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gem.sistema.business.domain.TrPresupuestoDetallado;

@Repository(value = "tcPresupuestoDetalladoRepository")
public interface TrPresupuestoDetalladoRepository extends PagingAndSortingRepository<TrPresupuestoDetallado, Long>,
		QueryDslPredicateExecutor<TrPresupuestoDetallado> {

	List<TrPresupuestoDetallado> findAll();

	@Query("select e from TrPresupuestoDetallado e join fetch e.area a where a.clave=?1")
	List<TrPresupuestoDetallado> getByClaveArea(Integer clave);

	@Query(value = "SELECT P.* FROM TR_PRESUPUESTO_DETALLADO P " + "	LEFT JOIN CATDEP D ON D.ID=P.ID_DEPENDENCIA "
			+ "	LEFT JOIN MUNINEP M ON M.ID=P.ID_PROYECTO " + "	LEFT JOIN NATGAS N ON N.ID=P.ID_PARTIDA "
			+ "WHERE D.CLVDEP=:clvdep	AND N.CLVGAS=:clvgas	AND M.CAMPO7=:campo7 ", nativeQuery = true)
	List<TrPresupuestoDetallado> getAllByDependenciaAndProgramaAndPartida(@Param("clvdep") String clvdep,
			@Param("campo7") String campo7, @Param("clvgas") String clvgas);

	@Query(value = "SELECT DISTINCT C.CLVDEP  FROM TR_PRESUPUESTO_DETALLADO T, CATDEP C WHERE C.ID=T.ID_DEPENDENCIA ", nativeQuery = true)
	List<String> getDependencias();

	@Query(value = "SELECT DISTINCT M.CAMPO7  FROM TR_PRESUPUESTO_DETALLADO T, MUNINEP M WHERE M.ID=T.ID_PROYECTO ", nativeQuery = true)
	List<String> getProgramas();

	@Query(value = "SELECT DISTINCT N.CLVGAS  FROM TR_PRESUPUESTO_DETALLADO T, NATGAS N WHERE N.ID=T.ID_PARTIDA", nativeQuery = true)
	List<String> getPartidas();

}
