package com.gem.sistema.business.service.catalogos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gem.sistema.business.domain.TcAccion;
import com.gem.sistema.business.repository.catalogs.TcAccionRepository;

@Service(value = "accionesService")
public class AccionesServiceImpl implements AccionesService {

	@Autowired
	private TcAccionRepository accionRepository;

	@Override
	public List<TcAccion> listadoAcciones() {
		return (List<TcAccion>) accionRepository.findAll();
	}

	@Override
	public List<TcAccion> searchAcciones(String criterio) {
		return accionRepository.findByClaveContainingOrNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(
				criterio, criterio, criterio);
	}

	@Override
	public TcAccion saveAccion(TcAccion accion) {
		return accionRepository.save(accion);
	}

	@Override
	public void deleteAccion(TcAccion accion) {
		accionRepository.delete(accion);
	}

	public TcAccionRepository getAccionRepository() {
		return accionRepository;
	}

	public void setAccionRepository(TcAccionRepository accionRepository) {
		this.accionRepository = accionRepository;
	}

}
