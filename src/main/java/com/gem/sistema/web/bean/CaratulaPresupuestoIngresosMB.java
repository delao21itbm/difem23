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
 * The Class CaratulaPresupuestoIngresosMB.
 */
@ManagedBean
@ViewScoped
public class CaratulaPresupuestoIngresosMB extends BaseDirectReport {

	/** The tipo rep. */
	private String tipoRep;

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
		LOGGER.info(":: En postconsruct CaratulaPresupuestoIngresosMB ");
		jasperReporteName = "CaratulaPresupuestoIngresos";
		endFilename = jasperReporteName + ".pdf";
	}

	/* (non-Javadoc)
	 * @see com.gem.sistema.web.bean.BaseDirectReport#getParametersReports()
	 */
	public Map<String, Object> getParametersReports() {
		Map<String, Object> paramsReport = new java.util.HashMap<String, Object>();
		Integer idSector = this.getUserDetails().getIdSector();
		conctb = conctbRepository.findByIdsector(idSector);
		TrPuestoFirma firma = new TrPuestoFirma();
		
		if ("pro".equals(getTipoRep())) {
			paramsReport.put("Proyecto", "X");
			paramsReport.put("Definitivo", "");
		} else {
			paramsReport.put("Proyecto", "");
			paramsReport.put("Definitivo", "X");
		}
		paramsReport.put("USUARIO", getUserDetails().getUsername());
		
		paramsReport.put("Image", conctb.getImagePathLeft()); 
		paramsReport.put("Image2", conctb.getImagePathRigth()); 
		paramsReport.put("Ente", this.getUserDetails().getMunicipio()); 
		paramsReport.put("No", conctb.getClave());
		paramsReport.put("year", conctb.getAnoemp());
		paramsReport.put("idSector", idSector);
		if(idSector == 1) {
			switch (conctb.getClave().substring(0, 1)) {
			case "0": //AYUNTAMIENTO
				paramsReport.put("noFirmas",4);
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F01.getValue());
				paramsReport.put("firmaL1",firma.getPuesto().getPuesto());
				paramsReport.put("firmaN1",firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F25.getValue());
				paramsReport.put("firmaL2",firma.getPuesto().getPuesto());
				paramsReport.put("firmaN2",firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F02.getValue());
				paramsReport.put("firmaL3",firma.getPuesto().getPuesto());
				paramsReport.put("firmaN3",firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F03.getValue());
				paramsReport.put("firmaL4",firma.getPuesto().getPuesto());
				paramsReport.put("firmaN4",firma.getNombre());
				break;
			case "2": //ODAS (AGUA)
				paramsReport.put("noFirmas",2);
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F10.getValue());
				paramsReport.put("firmaL2",firma.getPuesto().getPuesto());
				paramsReport.put("firmaN2",firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F11.getValue());
				paramsReport.put("firmaL3",firma.getPuesto().getPuesto());
				paramsReport.put("firmaN3",firma.getNombre());
				break;
			case "3":  // DIF
				paramsReport.put("noFirmas",3);
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F07.getValue());
				paramsReport.put("firmaL1",firma.getPuesto().getPuesto());
				paramsReport.put("firmaN1",firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F08.getValue());
				paramsReport.put("firmaL2",firma.getPuesto().getPuesto());
				paramsReport.put("firmaN2",firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F09.getValue());
				paramsReport.put("firmaL3",firma.getPuesto().getPuesto());
				paramsReport.put("firmaN3",firma.getNombre());
				break;
			case "4":  // IMCUFIDE (Deporte)
				paramsReport.put("noFirmas",2);
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F20.getValue());
				paramsReport.put("firmaL2",firma.getPuesto().getPuesto());
				paramsReport.put("firmaN2",firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F11.getValue());
				paramsReport.put("firmaL3",firma.getPuesto().getPuesto());
				paramsReport.put("firmaN3",firma.getNombre());
				break;
			}
		}else {
			paramsReport.put("noFirmas",3);
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F04.getValue());
			paramsReport.put("firmaL1",firma.getPuesto().getPuesto());
			paramsReport.put("firmaN1",firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F05.getValue());
			paramsReport.put("firmaL2",firma.getPuesto().getPuesto());
			paramsReport.put("firmaN2",firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F27.getValue());
			paramsReport.put("firmaL3",firma.getPuesto().getPuesto());
			paramsReport.put("firmaN3",firma.getNombre());
		}
		
		return paramsReport;
	}

	/* (non-Javadoc)
	 * @see com.gem.sistema.web.bean.BaseDirectReport#generaReporteSimple(int)
	 */
	public StreamedContent generaReporteSimple(int type) {
		return null;
		/*
		 * Map<String, Object> paramsQuery = new java.util.HashMap<String,
		 * Object>(); paramsQuery.put("ID_REF", new Integer(0)); //FALTA return
		 * reporteadorDirectoImpl.getFileReport(tcReporte,paramsQuery,
		 * reporteName,type);
		 */
	}

	/**
	 * Gets the tipo rep.
	 *
	 * @return the tipo rep
	 */
	public String getTipoRep() {
		return tipoRep;
	}

	/**
	 * Sets the tipo rep.
	 *
	 * @param tipoRep the new tipo rep
	 */
	public void setTipoRep(String tipoRep) {
		this.tipoRep = tipoRep;
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
