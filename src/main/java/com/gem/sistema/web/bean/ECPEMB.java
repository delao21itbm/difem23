package com.gem.sistema.web.bean;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.StreamedContent;

import com.gem.sistema.business.domain.Conctb;
import com.gem.sistema.business.domain.TrPuestoFirma;
import com.gem.sistema.business.repository.catalogs.ConctbRepository;
import com.gem.sistema.business.repository.catalogs.TcMesRepository;
import com.gem.sistema.business.service.catalogos.PuestosFirmasService;
import com.gem.sistema.business.service.catalogos.ValidatePolicyService;
import com.gem.sistema.business.service.reportador.ReportValidationException;
import com.gem.sistema.util.ConstantsClaveEnnum;

import static com.roonin.utils.UtilDate.getLastDayByAnoEmp;

// TODO: Auto-generated Javadoc
/**
 * The Class ECPEMB.
 *
 * @author Alfredo
 */
@ManagedBean
@ViewScoped
public class ECPEMB extends BaseDirectReport {

	/** The mes. */
	private Integer mes;

	/** The conctb. */
	private Conctb conctb;

	/** The conctb repository. */
	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository conctbRepository;

	/** The tc mes repository. */
	@ManagedProperty("#{tcMesRepository}")
	private TcMesRepository tcMesRepository;

	@ManagedProperty("#{validatePolicyService}")
	private ValidatePolicyService validatePolicyService;

	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		jasperReporteName = "ECPE";
		endFilename = jasperReporteName + ".pdf";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gem.sistema.web.bean.BaseDirectReport#getParametersReports()
	 */
	@Override
	public Map<String, Object> getParametersReports() throws ReportValidationException {
		Map<String, Object> paramsReport = new java.util.HashMap<String, Object>();
		Integer sector = this.getUserDetails().getIdSector();
		String mesName = tcMesRepository.findByMes(StringUtils.leftPad(mes.toString(), 2, "0")).getDescripcion();
		conctb = conctbRepository.findByIdsector(sector);
		TrPuestoFirma firma = new TrPuestoFirma();

		paramsReport.put("imagePath1", sector==1? conctb.getImagePathRigth(): conctb.getImagePathLeft());
		paramsReport.put("imagePath2", sector==1?"": conctb.getImagePathRigth());
		paramsReport.put("entidadName", conctb.getNomDep());
		paramsReport.put("entidadClave", conctb.getClave());
		paramsReport.put("lastDayOfMonth", getLastDayByAnoEmp(mes, conctb.getAnoemp()));
		paramsReport.put("mesName", mesName);
		paramsReport.put("year", conctb.getAnoemp());
		paramsReport.put("sql", this.generateQueryECPE(mes, sector));

		if (sector == 1) {
			switch (conctb.getClave().substring(0, 1)) {
			case "0":
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F01.getValue());
				paramsReport.put("firmaP1", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN1", firma.getNombre());

				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F25.getValue());
				paramsReport.put("firmaP2", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN2", firma.getNombre());

				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F03.getValue());
				paramsReport.put("firmaP3", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN3", firma.getNombre());

				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F24.getValue());
				paramsReport.put("firmaP4", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN4", firma.getNombre());
				break;
			case "2":
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F10.getValue());
				paramsReport.put("firmaP1", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN1", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F11.getValue());
				paramsReport.put("firmaP2", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN2", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F24.getValue());
				paramsReport.put("firmaP3", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN3", firma.getNombre());
				break;
			case "3":
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F07.getValue());
				paramsReport.put("firmaP1", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN1", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F08.getValue());
				paramsReport.put("firmaP2", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN2", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F09.getValue());
				paramsReport.put("firmaP3", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN3", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F24.getValue());
				paramsReport.put("firmaP4", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN4", firma.getNombre());
				break;
			case "4":

				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F20.getValue());
				paramsReport.put("firmaP1", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN1", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F21.getValue());
				paramsReport.put("firmaP2", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN2", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
						ConstantsClaveEnnum.CLAVE_F24.getValue());
				paramsReport.put("firmaP3", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN3", firma.getNombre());
				break;
			}
		} else {
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
					ConstantsClaveEnnum.CLAVE_F04.getValue());
			paramsReport.put("firmaP1", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN1", firma.getNombre());
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
					ConstantsClaveEnnum.CLAVE_F05.getValue());
			paramsReport.put("firmaP2", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN2", firma.getNombre());
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
					ConstantsClaveEnnum.CLAVE_F27.getValue());
			paramsReport.put("firmaP3", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN43", firma.getNombre());
		}

		return paramsReport;
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
	 * Generate query ECPE.
	 *
	 * @param mes    the mes
	 * @param sector the sector
	 * @return the string
	 */
	public String generateQueryECPE(Integer mes, Integer sector) {
		StringBuilder sQuery = new StringBuilder();
		StringBuilder toej = new StringBuilder();
		StringBuilder redu = new StringBuilder();
		StringBuilder ampl = new StringBuilder();
		StringBuilder auto = new StringBuilder();

		for (Integer i = 1; i <= mes; i++) {
			auto.append(" PA.AUTO1_" + i + " +");
			redu.append(" PA.REDU1_" + i + " +");
			ampl.append(" PA.AMPLI1_" + i + " +");
			toej.append(" PA.TOEJE1_" + i + " +");
		}

		sQuery.append("SELECT	T1.*, (T1.MODIFICADO_ACUMULADO - T1.EJERCIDO_ACUMULADO) ABSOLUTA,")
				.append("DECODE(SUBSTR(T1.PARTIDA,2,3),'000',1,0) SUMAR ").append("FROM( SELECT PA.PARTIDA,NA.NOMGAS,")
				.append("SUM(PA.AUTO1_1 + PA.AUTO1_2 + PA.AUTO1_3 + PA.AUTO1_4 + PA.AUTO1_5 + PA.AUTO1_6 + PA.AUTO1_7 + PA.AUTO1_8 + PA.AUTO1_9 + PA.AUTO1_10 + PA.AUTO1_11 + PA.AUTO1_12) AUTORIZADO,")
				.append("SUM(PA.AUTO1_" + mes + " + PA.AMPLI1_" + mes + " - PA.REDU1_" + mes + ")MODIFICADO_MES,")
				.append("SUM(PA.TOEJE1_" + mes + ") EJERCIDO_MES,")
				.append("SUM((" + auto.substring(0, auto.length() - 2) + ") + (" + ampl.substring(0, ampl.length() - 2)
						+ ") - (" + redu.substring(0, redu.length() - 2) + ")) MODIFICADO_ACUMULADO,")
				.append("SUM(" + toej.substring(0, toej.length() - 2) + ") EJERCIDO_ACUMULADO")
				.append(" FROM PASO PA, NATGAS NA")
				.append(" WHERE PA.IDSECTOR = NA.IDSECTOR AND PA.PARTIDA = NA.CLVGAS AND PA.IDSECTOR= " + sector)
				.append(" GROUP BY PA.PARTIDA,NA.NOMGAS ").append("ORDER BY PA.PARTIDA )T1");

		return sQuery.toString();
	}

	/**
	 * Gets the mes.
	 *
	 * @return the mes
	 */
	public Integer getMes() {
		return mes;
	}

	public void viewPdf() {
		if (this.validatePolicyService.isOpenMonth(mes, null, this.getUserDetails().getIdSector()) == Boolean.TRUE) {
			this.validatePolicyService.isAfectMonth(mes, null, this.getUserDetails().getIdSector());
			this.validatePolicyService.existPolices(mes, null, this.getUserDetails().getIdSector());
			this.createFilePdfInline();
		}
	}

	/**
	 * Sets the mes.
	 *
	 * @param mes the new mes
	 */
	public void setMes(Integer mes) {
		this.mes = mes;
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

	/**
	 * Gets the tc mes repository.
	 *
	 * @return the tc mes repository
	 */
	public TcMesRepository getTcMesRepository() {
		return tcMesRepository;
	}

	/**
	 * Sets the tc mes repository.
	 *
	 * @param tcMesRepository the new tc mes repository
	 */
	public void setTcMesRepository(TcMesRepository tcMesRepository) {
		this.tcMesRepository = tcMesRepository;
	}

	public ValidatePolicyService getValidatePolicyService() {
		return validatePolicyService;
	}

	public void setValidatePolicyService(ValidatePolicyService validatePolicyService) {
		this.validatePolicyService = validatePolicyService;
	}

	public PuestosFirmasService getPuestosFirmasService() {
		return puestosFirmasService;
	}

	public void setPuestosFirmasService(PuestosFirmasService puestosFirmasService) {
		this.puestosFirmasService = puestosFirmasService;
	}

}
