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
import com.gem.sistema.util.ConstantsClaveEnnum;

// TODO: Auto-generated Javadoc
/**
 * The Class RegresosMB.
 */
@ManagedBean
@ViewScoped
public class RegresosMB extends BaseDirectReport {

	/** The definitivo. */
	private String definitivo;

	/** The contb repository. */
	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository contbRepository;

	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;

	/**
	 * Gets the definitivo.
	 *
	 * @return the definitivo
	 */
	public String getDefinitivo() {
		return definitivo;
	}

	/**
	 * Sets the definitivo.
	 *
	 * @param definitivo the new definitivo
	 */
	public void setDefinitivo(String definitivo) {
		this.definitivo = definitivo;
	}

	/**
	 * Gets the contb repository.
	 *
	 * @return the contb repository
	 */
	public ConctbRepository getContbRepository() {
		return contbRepository;
	}

	/**
	 * Sets the contb repository.
	 *
	 * @param contbRepository the new contb repository
	 */
	public void setContbRepository(ConctbRepository contbRepository) {
		this.contbRepository = contbRepository;
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		LOGGER.info(":: En postconsruct RegresosMB ");
		jasperReporteName = "R_egresos";
		endFilename = jasperReporteName + ".pdf";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gem.sistema.web.bean.BaseDirectReport#getParametersReports()
	 */
	public Map<String, Object> getParametersReports() {
		Map<String, Object> paramsReport = new java.util.HashMap<String, Object>();
		Integer idSector = this.getUserDetails().getIdSector();
		Conctb conctb = contbRepository.findByIdsector(idSector);
		TrPuestoFirma firma = null;

		if ("Proyecto".equals(definitivo)) {
			paramsReport.put("PROYECTO", "X");
		} else {
			paramsReport.put("DEFINITIVO", "X");
		}
		paramsReport.put("IMG_MUN", conctb.getImagePathLeft());
		paramsReport.put("IMG_ENT", conctb.getImagePathRigth());
		paramsReport.put("IDSECTOR", idSector);
		paramsReport.put("pMun", getUserDetails().getMunicipio());
		paramsReport.put("pYear", conctb.getAnoemp());
		paramsReport.put("NUMMUN", conctb.getClave());

		if (idSector == 1) {
			switch (conctb.getClave().substring(0, 1)) {
			case "0": // AYUNTAMIENTO
				paramsReport.put("noFirmas", 4);
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F01.getValue());
				paramsReport.put("firmaL1", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN1", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F25.getValue());
				paramsReport.put("firmaL2", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN2", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F02.getValue());
				paramsReport.put("firmaL3", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN3", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F03.getValue());
				paramsReport.put("firmaL4", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN4", firma.getNombre());
				break;
			case "2": // ODAS (AGUA)
				paramsReport.put("noFirmas", 2);
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F10.getValue());
				paramsReport.put("firmaL2", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN2", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F11.getValue());
				paramsReport.put("firmaL3", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN3", firma.getNombre());
				break;
			case "3": // DIF
				paramsReport.put("noFirmas", 3);
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F07.getValue());
				paramsReport.put("firmaL1", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN1", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F08.getValue());
				paramsReport.put("firmaL2", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN2", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F09.getValue());
				paramsReport.put("firmaL3", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN3", firma.getNombre());
				break;
			case "4": // IMCUFIDE (Deporte)
				paramsReport.put("noFirmas", 2);
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F20.getValue());
				paramsReport.put("firmaL2", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN2", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F11.getValue());
				paramsReport.put("firmaL3", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN3", firma.getNombre());
				break;
			}
		} else {
			paramsReport.put("noFirmas", 3);
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
					ConstantsClaveEnnum.CLAVE_F08.getValue());
			paramsReport.put("firmaL1", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN1", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
					ConstantsClaveEnnum.CLAVE_F08.getValue());
			paramsReport.put("firmaL2", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN2", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
					ConstantsClaveEnnum.CLAVE_F11.getValue());
			paramsReport.put("firmaL3", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN3", firma.getNombre());
		}

		return paramsReport;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gem.sistema.web.bean.BaseDirectReport#generaReporteSimple(int)
	 */
	public StreamedContent generaReporteSimple(int type) {
		return null;
		/*
		 * Map<String, Object> paramsQuery = new java.util.HashMap<String, Object>();
		 * paramsQuery.put("ID_REF", new Integer(0)); //FALTA return
		 * reporteadorDirectoImpl.getFileReport(tcReporte,paramsQuery,
		 * reporteName,type);
		 */
	}

	public PuestosFirmasService getPuestosFirmasService() {
		return puestosFirmasService;
	}

	public void setPuestosFirmasService(PuestosFirmasService puestosFirmasService) {
		this.puestosFirmasService = puestosFirmasService;
	}

}
