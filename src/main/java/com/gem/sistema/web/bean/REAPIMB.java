package com.gem.sistema.web.bean;

import static com.roonin.utils.UtilDate.getLastDayByAnoEmp;

import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.StreamedContent;

import com.gem.sistema.business.domain.Conctb;
import com.gem.sistema.business.domain.TcPeriodo;
import com.gem.sistema.business.domain.TrPuestoFirma;
import com.gem.sistema.business.repository.catalogs.ConctbRepository;
import com.gem.sistema.business.repository.catalogs.TcPeriodoRepositoy;
import com.gem.sistema.business.service.catalogos.DataYearSystemService;
import com.gem.sistema.business.service.catalogos.PuestosFirmasService;
import com.gem.sistema.util.ConstantsClaveEnnum;

// TODO: Auto-generated Javadoc
/**
 * The Class REAPIMB.
 */
@ManagedBean
@ViewScoped
public class REAPIMB extends BaseDirectReport {

	/** The conctb. */
	private Conctb conctb;

	/** The conctb repository. */
	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository conctbRepository;

	/** The data year system service. */
	@ManagedProperty(value = "#{dataYearSystemService}")
	private DataYearSystemService dataYearSystemService;

	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository cocntbRepository;

	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;
	
	@ManagedProperty("#{tcPeriodoRepositoy}")
	private TcPeriodoRepositoy periodoRepositoy;

	public TcPeriodoRepositoy getPeriodoRepositoy() {
		return periodoRepositoy;
	}

	public void setPeriodoRepositoy(TcPeriodoRepositoy periodoRepositoy) {
		this.periodoRepositoy = periodoRepositoy;
	}

	/**
	 * Gets the data year system service.
	 *
	 * @return the data year system service
	 */
	public DataYearSystemService getDataYearSystemService() {
		return dataYearSystemService;
	}

	/**
	 * Sets the data year system service.
	 *
	 * @param dataYearSystemService the new data year system service
	 */
	public void setDataYearSystemService(DataYearSystemService dataYearSystemService) {
		this.dataYearSystemService = dataYearSystemService;
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

	/** The mes. */
	private Integer mes;

	// private String logo = "../reports/img/cuautitlan.jpg";

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		LOGGER.info(":: En postconsruct EAPI ");
		// reportId = 2;
		// tcReporte = reportesRepository.findOne(reportId);
		jasperReporteName = "EAPI";
		endFilename = jasperReporteName + ".pdf";
	}

	/* (non-Javadoc)
	 * @see com.gem.sistema.web.bean.BaseDirectReport#getParametersReports()
	 */
	public Map<String, Object> getParametersReports() {

		Map<String, Object> paramsReport = new java.util.HashMap<String, Object>();
		Integer idSector = this.getUserDetails().getIdSector();
		conctb = conctbRepository.findByIdsector(idSector);
		TcPeriodo periodo = periodoRepositoy.findByTipoPeriodoAndPeriodo(1, mes);
		TrPuestoFirma firma = new TrPuestoFirma();
		
		paramsReport.put("mesName", periodo.getDescripcion().toUpperCase());
		paramsReport.put("AN", conctb.getAnoemp());
		paramsReport.put("MES", mes);
		paramsReport.put("DIA", getLastDayByAnoEmp(mes, conctb.getAnoemp()));
		paramsReport.put("LOGO1", conctb.getImagePathLeft());
		paramsReport.put("LOGO2", conctb.getImagePathRigth());
		paramsReport.put("idSector", this.getUserDetails().getIdSector());
		if (idSector == 2) {
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F07.getValue());
			paramsReport.put("firmaP1", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN1", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F08.getValue());
			paramsReport.put("firmaP2", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN2", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F09.getValue());
			paramsReport.put("firmaP3", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN3", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F10.getValue());
			paramsReport.put("firmaP4", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN4", firma.getNombre());
		} else {
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F07.getValue());
			paramsReport.put("firmaP1", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN1", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F08.getValue());
			paramsReport.put("firmaP2", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN2", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F09.getValue());
			paramsReport.put("firmaP3", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN3", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F10.getValue());
			paramsReport.put("firmaP4", firma.getPuesto().getPuesto());
			paramsReport.put("firmaN4", firma.getNombre());
		}
		paramsReport.put("dependenciaName", conctb.getNomDep());
		if (idSector == 2)
			paramsReport.put("where", this.generateWhere(mes));
		else
			paramsReport.put("where", "");

		paramsReport.put("NumMun", conctb.getClave());

		return paramsReport;
	}

	/**
	 * Generate where.
	 *
	 * @param mes the mes
	 * @return the string
	 */
	public String generateWhere(Integer mes) {
		StringBuilder cargos10 = new StringBuilder();
		StringBuilder abonos10 = new StringBuilder();
		StringBuilder cargos40 = new StringBuilder();
		StringBuilder abonos40 = new StringBuilder();
		StringBuilder cargos50 = new StringBuilder();
		StringBuilder abonos50 = new StringBuilder();
		StringBuilder where = new StringBuilder();
		Integer i = 1;

		while (i <= mes) {
			cargos10.append(" OR DI.CARGOS_" + i + " <> 0 ");
			abonos10.append(" OR DI.ABONOS_" + i + " <> 0 ");
			cargos40.append(" OR CU.CARGOS_" + i + " <> 0 ");
			abonos40.append(" OR CU.ABONOS_" + i + " <> 0 ");
			cargos50.append(" OR CI.CARGOS_" + i + " <> 0 ");
			abonos50.append(" OR CI.ABONOS_" + i + " <> 0 ");
			i++;
		}

		where.append("AND (DI.SALINI <> 0" + cargos10 + abonos10).append(" OR CU.SALINI <> 0 " + cargos40 + abonos40)
				.append(" OR CI.SALINI <> 0 " + cargos50 + abonos50).append(")");
		return where.toString();
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

	public ConctbRepository getCocntbRepository() {
		return cocntbRepository;
	}

	public void setCocntbRepository(ConctbRepository cocntbRepository) {
		this.cocntbRepository = cocntbRepository;
	}

	public PuestosFirmasService getPuestosFirmasService() {
		return puestosFirmasService;
	}

	public void setPuestosFirmasService(PuestosFirmasService puestosFirmasService) {
		this.puestosFirmasService = puestosFirmasService;
	}



}
