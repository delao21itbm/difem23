package com.gem.sistema.business.predicates;

import com.gem.sistema.business.domain.QTrPresupuestoDetallado;
import com.mysema.query.types.Predicate;

public class PresupuestoDetalladoPredicate {

	public static Predicate buscaPorMovimiento(Long idArea, Long idDep, Long idProg, Long idFuente, Long idPartida) {
		return QTrPresupuestoDetallado.trPresupuestoDetallado.area.id.eq(idArea)
				.and(QTrPresupuestoDetallado.trPresupuestoDetallado.dependencia.id.eq(idDep))
				.and(QTrPresupuestoDetallado.trPresupuestoDetallado.proyecto.id.eq(idProg))
				.and(QTrPresupuestoDetallado.trPresupuestoDetallado.fuente.id.eq(idFuente))
				.and(QTrPresupuestoDetallado.trPresupuestoDetallado.partida.id.eq(idPartida));
	}
}
