package com.gem.sistema.business.service.catalogos;

import java.io.File;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.gem.sistema.business.domain.TcMovimiento;
import com.gem.sistema.business.domain.TcTipoMovimiento;
import com.gem.sistema.business.domain.TrPresupuestoDetallado;
import com.gem.sistema.business.dto.EgresoCargaDTO;
import com.gem.sistema.business.dto.PresupuestoDetalladoDTO;

public interface EgresoDetalladoService {
	EgresoCargaDTO cargaArchivoEgreso(File newFile, Integer idSector);

	List<TrPresupuestoDetallado> getAllByFilters(String area, String dep, String partida, String proyecto,
			String fuente) throws DataAccessException;

	List<String> parseDual(List<String> dualList, String programa, String fuenteF, String partida,
			TcTipoMovimiento movimiento, String separador);

	public void guardarMovimiento(String usuario, TcTipoMovimiento tcmovimiento, PresupuestoDetalladoDTO presupuestoDto, Integer mes,Integer folio,Boolean bandera);
}
