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

@ManagedBean(name = "pbrm06MB")
@ViewScoped
public class Pbrm06MB extends BaseDirectReport {

	private Integer saldoCero;

	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository conctbRepository;

	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;

	@PostConstruct
	public void init() {
		saldoCero = 0;
		jasperReporteName = "pbrm06";
		endFilename = jasperReporteName + ".pdf";
	}

	@Override
	public Map<String, Object> getParametersReports() throws ReportValidationException {
		Map<String, Object> paramsReport = new java.util.HashMap<String, Object>();
		String whereSaldoCero = saldoCero == 1 ? ""
				: " WHERE T1.ENERO <> 0 OR T1.FEBRERO <> 0 OR T1.MARZO <> 0 OR T1.ABRIL <> 0 OR T1.MAYO <> 0 OR T1.JUNIO <> 0 OR T1.JULIO <> 0 OR T1.AGOSTO <> 0 OR T1.SEPTIEMBRE <> 0 OR T1.OCTUBRE <> 0 	OR T1.NOVIEMBRE <> 0 OR T1.DICIEMBRE <> 0 ";
		Integer sector = this.getUserDetails().getIdSector();
		Conctb conctb = conctbRepository.findByIdsector(sector);
		TrPuestoFirma firma = null;

		paramsReport.put("pImage", this.getUserDetails().getPathImgCab1());
		paramsReport.put("pImage2", this.getUserDetails().getPathImgCab2());
		paramsReport.put("pYear", conctb.getAnoemp());
		paramsReport.put("pEntePublico", this.getUserDetails().getMunicipio());
		paramsReport.put("pNumero", conctb.getClave());
		paramsReport.put("saldoCero", whereSaldoCero);
		paramsReport.put("pIdSector", sector);

		if (sector == 1) {
			switch (conctb.getClave().substring(0, 1)) {
			case "0": // AYUNTAMIENTO
				paramsReport.put("noFirmas", 4);
				firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F01.getValue());
				paramsReport.put("firmaL1", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN1", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F25.getValue());
				paramsReport.put("firmaL2", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN2", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F02.getValue());
				paramsReport.put("firmaL3", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN3", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F03.getValue());
				paramsReport.put("firmaL4", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN4", firma.getNombre());
				break;
			case "2": // ODAS (AGUA)
				paramsReport.put("noFirmas", 2);
				firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F10.getValue());
				paramsReport.put("firmaL2", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN2", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F11.getValue());
				paramsReport.put("firmaL3", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN3", firma.getNombre());
				break;
			case "3": // DIF
				paramsReport.put("noFirmas", 3);
				firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F07.getValue());
				paramsReport.put("firmaL1", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN1", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F08.getValue());
				paramsReport.put("firmaL2", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN2", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F09.getValue());
				paramsReport.put("firmaL3", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN3", firma.getNombre());
				break;
			case "4": // IMCUFIDE (Deporte)
				paramsReport.put("noFirmas", 2);
				firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F20.getValue());
				paramsReport.put("firmaL2", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN2", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F11.getValue());
				paramsReport.put("firmaL3", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN3", firma.getNombre());
				break;
			}
		}

		return paramsReport;
	}

	@Override
	public StreamedContent generaReporteSimple(int type) throws ReportValidationException {
		// TODO Auto-generated method stub
		return null;
	}

	public ConctbRepository getConctbRepository() {
		return conctbRepository;
	}

	public void setConctbRepository(ConctbRepository conctbRepository) {
		this.conctbRepository = conctbRepository;
	}

	public PuestosFirmasService getPuestosFirmasService() {
		return puestosFirmasService;
	}

	public void setPuestosFirmasService(PuestosFirmasService puestosFirmasService) {
		this.puestosFirmasService = puestosFirmasService;
	}

	public Integer getSaldoCero() {
		return saldoCero;
	}

	public void setSaldoCero(Integer saldoCero) {
		this.saldoCero = saldoCero;
	}

}
