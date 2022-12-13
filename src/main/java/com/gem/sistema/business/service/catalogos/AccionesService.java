package com.gem.sistema.business.service.catalogos;

import java.util.List;

import com.gem.sistema.business.domain.TcAccion;

public interface AccionesService {

	
	List<TcAccion> listadoAcciones();
	
	List<TcAccion> searchAcciones(String criterio);
	
	TcAccion saveAccion(TcAccion accion);
	
	void deleteAccion(TcAccion accion);
}
