package com.gem.sistema.business.service.catalogos;

import com.gem.sistema.util.UtilFront;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gem.sistema.business.domain.TcAdquisicion;
import com.gem.sistema.business.domain.TcAdquisicionGiroSubgiro;
import com.gem.sistema.business.domain.TcAdquisicionPrograma;
import com.gem.sistema.business.domain.TcArticulosSA;
import com.gem.sistema.business.domain.TcFirmaSolicitudDetalle;
import com.gem.sistema.business.repository.catalogs.TcAdquisicionGiroSubgiroRepository;
import com.gem.sistema.business.repository.catalogs.TcAdquisicionProgramasRepository;
import com.gem.sistema.business.repository.catalogs.TcAdquisicionRepository;
import com.gem.sistema.business.repository.catalogs.TcArticulosSARepository;
import com.gem.sistema.business.repository.catalogs.TcFirmaSolicitudDetalleRepository;

@Service(value = "adquisicionesService")
public class AdquisicionesServiceImpl implements AdquisicionesService {
	@Autowired
	private TcArticulosSARepository articulosRepository;
	@Autowired
	private TcAdquisicionRepository adquisicionRepository;
	@Autowired
	private TcAdquisicionProgramasRepository adquisicionProgramaRepository;
	@Autowired
	private TcAdquisicionGiroSubgiroRepository giroSubgiroRepository;
	@Autowired
	private TcFirmaSolicitudDetalleRepository firmaSolicitudDetalleRepository;

	@Transactional
	public void deleteAdquisicionAndRelations(TcAdquisicion adquisicion) throws Exception {
		List<String> errors = new ArrayList<String>();
		List<TcArticulosSA> articulos = articulosRepository.findAllByAdquisicion(adquisicion);
		if (articulos != null && !articulos.isEmpty()) {
			articulosRepository.delete(articulos);

			errors.add("Se han eliminado: " + articulos.size() + " articulos de la solicitud: "
					+ adquisicion.getNumeroControl());
		}
		List<TcFirmaSolicitudDetalle> firmas = firmaSolicitudDetalleRepository.getAllByAdquisicion(adquisicion);
		if (firmas != null && !firmas.isEmpty()) {
			firmaSolicitudDetalleRepository.delete(firmas);
			errors.add("Se han eliminado las firmas de la solicitud: " + adquisicion.getNumeroControl());
		}
		List<TcAdquisicionPrograma> programasToDelete = adquisicionProgramaRepository.findAllByAdquisicion(adquisicion);
		if (programasToDelete != null && !programasToDelete.isEmpty()) {
			adquisicionProgramaRepository.delete(programasToDelete);
			errors.add("Se han eliminado :" + programasToDelete.size() + " programas de la solicitud: "
					+ adquisicion.getNumeroControl());
		}
		List<TcAdquisicionGiroSubgiro> subgirosToDelete = giroSubgiroRepository.findAllByAdquisicion(adquisicion);
		if (subgirosToDelete != null && !subgirosToDelete.isEmpty()) {
			giroSubgiroRepository.delete(subgirosToDelete);
			errors.add("Se han eliminado: " + subgirosToDelete.size() + " subgiros-comerciales de la solicitud :"
					+ adquisicion.getNumeroControl());
		}
		adquisicionRepository.delete(adquisicion.getId());
		errors.add("La solicitud con numero de control: " + adquisicion.getNumeroControl()
				+ " se eliminó de manera correcta");
		errors.forEach(this::displayInfoMessage);
	}

	private void displayInfoMessage(String msn) {
		UtilFront.generateNotificationFront(FacesMessage.SEVERITY_INFO, "", msn);
	}
}
