package com.gem.sistema.web.bean;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.StreamedContent;

import com.gem.sistema.business.domain.Conctb;
import com.gem.sistema.business.domain.TrPuestoFirma;
import com.gem.sistema.business.repository.catalogs.ConctbRepository;
import com.gem.sistema.business.service.catalogos.PuestosFirmasService;
import com.gem.sistema.business.service.reportador.ReportValidationException;
import com.gem.sistema.util.ConstantsClaveEnnum;

// TODO: Auto-generated Javadoc
/**
 * The Class Pbrm04cMB.
 */
@ManagedBean(name = "pbrm04cMB")
@ViewScoped
public class Pbrm04cMB extends BaseDirectReport {

	/** The conctb. */
	private Conctb conctb;

	/** The conctb repository. */
	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository conctbRepository;

	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		jasperReporteName = "pbrm04c";
		endFilename = jasperReporteName + ".pdf";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gem.sistema.web.bean.BaseDirectReport#getParametersReports()
	 */
	@Override
	public Map<String, Object> getParametersReports() throws ReportValidationException {
		Map<String, Object> parameters = new java.util.HashMap<String, Object>();
		Integer idSector = this.getUserDetails().getIdSector();
		conctb = conctbRepository.findByIdsector(getUserDetails().getIdSector());
		TrPuestoFirma firma = null;

		parameters.put("pathImage1", conctb.getImagePathLeft());
		parameters.put("pathImage2", conctb.getImagePathRigth());
		parameters.put("idSector", this.getUserDetails().getIdSector());
		parameters.put("year", conctb.getAnoemp());
		parameters.put("municipioName", this.getUserDetails().getMunicipio());
		parameters.put("municipioClave", conctb.getClave());

		if (idSector == 1) {
			switch (conctb.getClave().substring(0, 1)) {
			case "0": // AYUNTAMIENTO
				parameters.put("noFirmas", 4);
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F01.getValue());
				parameters.put("firmaL1", firma.getPuesto().getPuesto());
				parameters.put("firmaN1", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F25.getValue());
				parameters.put("firmaL2", firma.getPuesto().getPuesto());
				parameters.put("firmaN2", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F02.getValue());
				parameters.put("firmaL3", firma.getPuesto().getPuesto());
				parameters.put("firmaN3", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F03.getValue());
				parameters.put("firmaL4", firma.getPuesto().getPuesto());
				parameters.put("firmaN4", firma.getNombre());
				break;
			case "2": // ODAS (AGUA)
				parameters.put("noFirmas", 2);
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F10.getValue());
				parameters.put("firmaL2", firma.getPuesto().getPuesto());
				parameters.put("firmaN2", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F11.getValue());
				parameters.put("firmaL3", firma.getPuesto().getPuesto());
				parameters.put("firmaN3", firma.getNombre());
				break;
			case "3": // DIF
				parameters.put("noFirmas", 3);
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F07.getValue());
				parameters.put("firmaL1", firma.getPuesto().getPuesto());
				parameters.put("firmaN1", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F08.getValue());
				parameters.put("firmaL2", firma.getPuesto().getPuesto());
				parameters.put("firmaN2", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F09.getValue());
				parameters.put("firmaL3", firma.getPuesto().getPuesto());
				parameters.put("firmaN3", firma.getNombre());
				break;
			case "4": // IMCUFIDE (Deporte)
				parameters.put("noFirmas", 2);
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F20.getValue());
				parameters.put("firmaL2", firma.getPuesto().getPuesto());
				parameters.put("firmaN2", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F11.getValue());
				parameters.put("firmaL3", firma.getPuesto().getPuesto());
				parameters.put("firmaN3", firma.getNombre());
				break;
			}
		} else {
			parameters.put("noFirmas", 3);
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
					ConstantsClaveEnnum.CLAVE_F08.getValue());
			parameters.put("firmaL1", firma.getPuesto().getPuesto());
			parameters.put("firmaN1", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
					ConstantsClaveEnnum.CLAVE_F09.getValue());
			parameters.put("firmaL2", firma.getPuesto().getPuesto());
			parameters.put("firmaN2", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
					ConstantsClaveEnnum.CLAVE_F11.getValue());
			parameters.put("firmaL3", firma.getPuesto().getPuesto());
			parameters.put("firmaN3", firma.getNombre());
		}

		return parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gem.sistema.web.bean.BaseDirectReport#generaReporteSimple(int)
	 */
	@Override
	public StreamedContent generaReporteSimple(int type) throws ReportValidationException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Gets the conctb.
	 *
	 * @return the conctb
	 */
	public Conctb getConctb() {
		return conctb;
	}

	/**
	 * Sets the conctb.
	 *
	 * @param conctb the new conctb
	 */
	public void setConctb(Conctb conctb) {
		this.conctb = conctb;
	}

	/**
	 * Gets the conctb repository.
	 *
	 * @return the conctb repository
	 */
	public ConctbRepository getConctbRepository() {
		return conctbRepository;
	}

	/**
	 * Sets the conctb repository.
	 *
	 * @param conctbRepository the new conctb repository
	 */
	public void setConctbRepository(ConctbRepository conctbRepository) {
		this.conctbRepository = conctbRepository;
	}

	public PuestosFirmasService getPuestosFirmasService() {
		return puestosFirmasService;
	}

	public void setPuestosFirmasService(PuestosFirmasService puestosFirmasService) {
		this.puestosFirmasService = puestosFirmasService;
	}
}
