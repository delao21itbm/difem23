package com.gem.sistema.web.bean;

import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

import com.gem.sistema.business.domain.Conctb;
import com.gem.sistema.business.domain.TcPeriodo;
import com.gem.sistema.business.domain.TrPuestoFirma;
import com.gem.sistema.business.repository.catalogs.ConctbRepository;
import com.gem.sistema.business.repository.catalogs.TcPeriodoRepositoy;
import com.gem.sistema.business.service.catalogos.PuestosFirmasService;
import com.gem.sistema.business.service.catalogos.ValidatePolicyService;
import com.gem.sistema.util.ConstantsClaveEnnum;

import static com.roonin.utils.UtilDate.getLastDayByAnoEmp;

// TODO: Auto-generated Javadoc
/**
 * The Class RF00914MB.
 */
@ManagedBean
@ViewScoped
public class RF00914MB extends BaseDirectReport {

	private static final String DOWNLOAD_XLS = " jQuery('#form1\\\\:downXls').click();";

	/** The mes. */
	private Integer mes;

	/** The saldo 0. */
	private Integer saldo0;

	@ManagedProperty("#{tcPeriodoRepositoy}")
	private TcPeriodoRepositoy periodoRepositoy;

	@ManagedProperty("#{validatePolicyService}")
	private ValidatePolicyService validatePolicyService;

	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository conctbRepository;

	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		LOGGER.info(":: En postconsruct RF00914MB ");
		jasperReporteName = "RF009.1.4";
		endFilename = jasperReporteName + ".pdf";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.gem.sistema.web.bean.BaseDirectReport#getParametersReports()
	 */
	public Map<String, Object> getParametersReports() {
		Map<String, Object> paramsReport = new java.util.HashMap<String, Object>();
		TcPeriodo periodo = periodoRepositoy.findByTipoPeriodoAndPeriodo(1, mes);
		TrPuestoFirma firma = new TrPuestoFirma();
		Integer idSector = this.getUserDetails().getIdSector();
		Conctb conctb = conctbRepository.findByIdsector(idSector);

		paramsReport.put("year", conctb.getAnoemp());
		paramsReport.put("entidadName", conctb.getNomDep());
		paramsReport.put("mes", mes);
		paramsReport.put("idSector", idSector);
		paramsReport.put("where", this.generateWhere(idSector, mes, saldo0));

		if (idSector == 1) {
			switch (conctb.getClave().substring(0, 1)) {
			case "0":
				paramsReport.put("pImg1", conctb.getImagePathLeft());
				paramsReport.put("pImg2", conctb.getImagePathRigth());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F07.getValue());
				paramsReport.put("firmaP1", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN1", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F08.getValue());
				paramsReport.put("firmaP2", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN2", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F09.getValue());
				paramsReport.put("firmaP3", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN3", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F10.getValue());
				paramsReport.put("firmaP4", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN4", firma.getNombre());
				break;
			case "2": // ODAS
				paramsReport.put("pImg1", conctb.getImagePathLeft());
				paramsReport.put("pImg2", conctb.getImagePathRigth());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F07.getValue());
				paramsReport.put("firmaP1", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN1", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F08.getValue());
				paramsReport.put("firmaP2", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN2", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F09.getValue());
				paramsReport.put("firmaP3", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN3", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F10.getValue());
				paramsReport.put("firmaP4", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN4", firma.getNombre());
				break;
			case "3":
				paramsReport.put("pImg1", conctb.getImagePathLeft());
				paramsReport.put("pImg2", conctb.getImagePathRigth());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F07.getValue());
				paramsReport.put("firmaP1", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN1", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F08.getValue());
				paramsReport.put("firmaP2", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN2", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F09.getValue());
				paramsReport.put("firmaP3", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN3", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F10.getValue());
				paramsReport.put("firmaP4", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN4", firma.getNombre());
				break;
			case "4":
				paramsReport.put("pImg1", conctb.getImagePathLeft());
				paramsReport.put("pImg2", conctb.getImagePathRigth());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F07.getValue());
				paramsReport.put("firmaP1", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN1", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F08.getValue());
				paramsReport.put("firmaP2", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN2", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F09.getValue());
				paramsReport.put("firmaP3", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN3", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F10.getValue());
				paramsReport.put("firmaP4", firma.getPuesto().getPuesto());
				paramsReport.put("firmaN4", firma.getNombre());
				break;
			}
		} else {
			paramsReport.put("pImg1", conctb.getImagePathLeft());
			paramsReport.put("pImg2", conctb.getImagePathRigth());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
					ConstantsClaveEnnum.CLAVE_F07.getValue());
			paramsReport.put("firmaP1", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN1", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
					ConstantsClaveEnnum.CLAVE_F08.getValue());
			paramsReport.put("firmaP2", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN2", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
					ConstantsClaveEnnum.CLAVE_F09.getValue());
			paramsReport.put("firmaP3", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN3", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
					ConstantsClaveEnnum.CLAVE_F10.getValue());
			paramsReport.put("firmaP4", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN4", firma.getNombre());
		}

		paramsReport.put("lastDay", getLastDayByAnoEmp(mes, conctb.getAnoemp()));
		paramsReport.put("mesName", periodo.getDescripcion());

		return paramsReport;
	}

	public void downloadXls() {
		if (this.validatePolicyService.isOpenMonth(mes, null, this.getUserDetails().getIdSector()) == Boolean.TRUE) {
			this.validatePolicyService.isAfectMonth(mes, null, this.getUserDetails().getIdSector());
			this.validatePolicyService.existPolices(mes, null, this.getUserDetails().getIdSector());

			RequestContext.getCurrentInstance().execute(DOWNLOAD_XLS);
		}
	}

	public void viewPdf() {
		if (this.validatePolicyService.isOpenMonth(mes, null, this.getUserDetails().getIdSector()) == Boolean.TRUE) {
			this.validatePolicyService.isAfectMonth(mes, null, this.getUserDetails().getIdSector());
			this.validatePolicyService.existPolices(mes, null, this.getUserDetails().getIdSector());
			this.createFilePdfInline();
			;
		}
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

	/**
	 * Generate where.
	 *
	 * @param idSector the id sector
	 * @param mes      the mes
	 * @param saldos   the saldos
	 * @return the string
	 */
	public String generateWhere(Integer idSector, Integer mes, Integer saldos) {
		String str = "";
		if (saldos == 0) {
			for (int i = 1; i <= mes; i++) {
				str = str + "ABONOS_" + i + " <> 0 OR CARGOS_" + i + " <> 0 OR ";
			}
			str = " AND (" + str + " SALINI <> 0)";
		}

		str = "IDSECTOR = " + idSector + str;

		return str;
	}

	/**
	 * Gets the mes.
	 *
	 * @return the mes
	 */
	public Integer getMes() {
		return mes;
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
	 * Gets the saldo 0.
	 *
	 * @return the saldo 0
	 */
	public Integer getSaldo0() {
		return saldo0;
	}

	/**
	 * Sets the saldo 0.
	 *
	 * @param saldo0 the new saldo 0
	 */
	public void setSaldo0(Integer saldo0) {
		this.saldo0 = saldo0;
	}

	public ValidatePolicyService getValidatePolicyService() {
		return validatePolicyService;
	}

	public void setValidatePolicyService(ValidatePolicyService validatePolicyService) {
		this.validatePolicyService = validatePolicyService;
	}

	public TcPeriodoRepositoy getPeriodoRepositoy() {
		return periodoRepositoy;
	}

	public void setPeriodoRepositoy(TcPeriodoRepositoy periodoRepositoy) {
		this.periodoRepositoy = periodoRepositoy;
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

}
