package com.gem.sistema.web.bean;

import static com.gem.sistema.util.UtilFront.generateNotificationFront;
import static com.roonin.utils.UtilDate.getLastDayByAnoEmp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.gem.sistema.business.domain.Conctb;
import com.gem.sistema.business.domain.TcMes;
import com.gem.sistema.business.domain.TrPuestoFirma;
import com.gem.sistema.business.repository.catalogs.ConctbRepository;
import com.gem.sistema.business.repository.catalogs.PolienRepository;
import com.gem.sistema.business.repository.catalogs.TcMesRepository;
import com.gem.sistema.business.repository.catalogs.TcPeriodoRepositoy;
import com.gem.sistema.business.service.callsp.ExecutePlService;
import com.gem.sistema.business.service.catalogos.PuestosFirmasService;
import com.gem.sistema.business.service.catalogos.ValidatePolicyService;
import com.gem.sistema.business.service.reportador.ReportValidationException;
import com.gem.sistema.util.ConstantsClaveEnnum;

@ManagedBean(name = "reporte22MB")
@ViewScoped
public class Reporte22MB extends BaseDirectReport {
	private static final String DOWNLOAD_PDF = " jQuery('#form1\\\\:downPdf').click();";
	private static final String DOWNLOAD_XLS = " jQuery('#form1\\\\:downXls').click();";
	private static final String DOWNLOAD_TXT = " jQuery('#form1\\\\:downTxt').click();";
	private static final String PROCEDURE_NAME = "SP_GENERA_TXT_MAYORCTAS";

	private StreamedContent txt;
	private String mes;
	private String cuentaIni;
	private String cuentaFin;
	private List<TcMes> listMes;

	@ManagedProperty("#{polienRepository}")
	private PolienRepository polienRepository;

	@ManagedProperty("#{tcMesRepository}")
	private TcMesRepository tcMesRepository;

	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository conctbRepository;

	@ManagedProperty("#{validatePolicyService}")
	private ValidatePolicyService validatePolicyService;

	@ManagedProperty("#{executePlService}")
	protected ExecutePlService executePlService;
	
	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;
	
	public PuestosFirmasService getPuestosFirmasService() {
		return puestosFirmasService;
	}

	public void setPuestosFirmasService(PuestosFirmasService puestosFirmasService) {
		this.puestosFirmasService = puestosFirmasService;
	}

	public TcPeriodoRepositoy getPeriodoRepositoy() {
		return periodoRepositoy;
	}

	public void setPeriodoRepositoy(TcPeriodoRepositoy periodoRepositoy) {
		this.periodoRepositoy = periodoRepositoy;
	}

	@ManagedProperty("#{tcPeriodoRepositoy}")
	private TcPeriodoRepositoy periodoRepositoy;

	@PostConstruct
	public void init() {
		cuentaIni = "1111";
		cuentaFin = "9999";
		jasperReporteName = "reporte22";
		endFilename = "LIBRO_MAYOR.pdf";
		listMes = tcMesRepository.findAll();
		if (!CollectionUtils.isEmpty(listMes)) {
			mes = listMes.get(0).getMes();
		}
	}

	@Override
	public Map<String, Object> getParametersReports() throws ReportValidationException {
		Map<String, Object> paramsReport = new java.util.HashMap<String, Object>();
		Integer idSector = this.getUserDetails().getIdSector();
		Conctb conctb = conctbRepository.findByIdsector(idSector);
		TrPuestoFirma firma = new TrPuestoFirma();
		
		List<String> listQuerys = this.generaQuerys(Integer.valueOf(mes), this.getUserDetails().getIdSector(),
				cuentaIni, cuentaFin);

		paramsReport.put("pDia", getLastDayByAnoEmp(Integer.valueOf(mes), conctb.getAnoemp()));
		paramsReport.put("imagePath1", conctb.getImagePathLeft());
		paramsReport.put("imagePath2", conctb.getImagePathRigth());
		paramsReport.put("entidad", conctb.getNomDep());
		paramsReport.put("rfc", conctb.getRfc());
		paramsReport.put("mes", Integer.valueOf(mes));
		paramsReport.put("mesName",
				tcMesRepository.findByMes(StringUtils.leftPad(mes.toString(), 2, "0")).getDescripcion());
		paramsReport.put("year", conctb.getAnoemp());
		paramsReport.put("sector", this.getUserDetails().getIdSector());
		
		
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
		
		paramsReport.put("queryP", listQuerys.get(0));
		paramsReport.put("queryS", listQuerys.get(1));
		
		return paramsReport;
	}

	@Override
	public StreamedContent generaReporteSimple(int type) throws ReportValidationException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void validationsGem() {
		if (polienRepository.isAfectedMesSector(this.getUserDetails().getIdSector(), Integer.parseInt(mes)) != 0) {
			generateNotificationFront(FacesMessage.SEVERITY_WARN, "ERROR", "El mes: " + mes + " No esta afectado");
		}
		if (polienRepository.isPolIncompletMesSector(this.getUserDetails().getIdSector(), Integer.parseInt(mes)) >= 1) {
			generateNotificationFront(FacesMessage.SEVERITY_WARN, "ERROR",
					"Existen polizas incorrectas en este mes: " + mes + ", las cuales no se imprimiran");
		}
		if (polienRepository.isPolizasMesSector(this.getUserDetails().getIdSector(), Integer.parseInt(mes)) == 0) {
			generateNotificationFront(FacesMessage.SEVERITY_WARN, "ERROR", "El mes: " + mes + " No tiene polizas");
		}

	}
	
	public void downloadPdf() {
		if (this.validatePolicyService.isOpenMonth(Long.valueOf(mes).intValue(), null,
				this.getUserDetails().getIdSector()) == Boolean.TRUE) {
			this.validatePolicyService.isAfectMonth(Long.valueOf(mes).intValue(), null,
					this.getUserDetails().getIdSector());
			this.validatePolicyService.existPolices(Long.valueOf(mes).intValue(), null,
					this.getUserDetails().getIdSector());

			RequestContext.getCurrentInstance().execute(DOWNLOAD_PDF);
		}
	}
	
	public void downloadXls() {
		if (this.validatePolicyService.isOpenMonth(Long.valueOf(mes).intValue(), null,
				this.getUserDetails().getIdSector()) == Boolean.TRUE) {
			this.validatePolicyService.isAfectMonth(Long.valueOf(mes).intValue(), null,
					this.getUserDetails().getIdSector());
			this.validatePolicyService.existPolices(Long.valueOf(mes).intValue(), null,
					this.getUserDetails().getIdSector());

			RequestContext.getCurrentInstance().execute(DOWNLOAD_XLS);
		}
	}
	
	public void downloadTxt() {
		if (this.validatePolicyService.isOpenMonth(Long.valueOf(mes).intValue(), null,
				this.getUserDetails().getIdSector()) == Boolean.TRUE) {
			this.validatePolicyService.isAfectMonth(Long.valueOf(mes).intValue(), null,
					this.getUserDetails().getIdSector());
			this.validatePolicyService.existPolices(Long.valueOf(mes).intValue(), null,
					this.getUserDetails().getIdSector());

			RequestContext.getCurrentInstance().execute(DOWNLOAD_TXT);
		}
	}

	public void viewPdf() {
		if (this.validatePolicyService.isOpenMonth(Long.valueOf(mes).intValue(), null,
				this.getUserDetails().getIdSector()) == Boolean.TRUE) {
			this.validatePolicyService.isAfectMonth(Long.valueOf(mes).intValue(), null,
					this.getUserDetails().getIdSector());
			this.validatePolicyService.existPolices(Long.valueOf(mes).intValue(), null,
					this.getUserDetails().getIdSector());
			this.createFilePdfInline();
			;
		}
	}
	
	public List<String> generaQuerys(Integer mes, Integer sector, String ctaIni, String ctaFin) {
		StringBuilder queryP = new StringBuilder();
		StringBuilder queryS = new StringBuilder();
		StringBuilder carAboD = new StringBuilder();
		StringBuilder carAboA = new StringBuilder();
		List<String> listQuerys = new ArrayList<String>();

		carAboD.append(" CU.SALINI ");
		carAboA.append(" CU.SALINI ");

		queryP.append("SELECT CU.CUENTA, CU.SCTA, CU.SSCTA, CU.SSSCTA, CU.SSSSCTA,")
				.append(" SUBSTR(CU.NOMCTA,1,60) NOMCTA, UPPER(MA.NATCTA) NATCTA,");

		int i = 1;
		while (i < mes && mes > 1) {
			carAboD.append("+ CU.CARGOS_" + i + " - CU.ABONOS_" + i + " ");
			carAboA.append("- CU.CARGOS_" + i + " + CU.ABONOS_" + i + " ");
			i++;
		}

		queryP.append(" CASE WHEN UPPER(MA.NATCTA) = 'D' THEN ").append(carAboD).append(" ELSE ").append(carAboA)
				.append(" END SALDOINI, ")
				.append("CASE WHEN UPPER(MA.NATCTA) = 'D' THEN (CARGOS_" + mes + " - ABONOS_" + mes + ") ELSE (CARGOS_"
						+ mes + " + ABONOS_" + mes + ") END SALDOFIN, ")
				.append(" CU.CARGOS_" + mes + " CARGOS, CU.ABONOS_" + mes + " ABONOS ").append(" FROM CUENTA CU ")
				.append(" INNER JOIN MAYCTA MA ON MA.CUENTA = CU.CUENTA AND MA.NOTCTA=0 AND MA.CUENTA BETWEEN '"
						+ ctaIni + "' AND '" + ctaFin + "' ")
				.append(" WHERE	CU.IDSECTOR = " + sector)
				.append(" AND CU.SCTA = '' ORDER BY CU.CUENTA, CU.SCTA, CU.SSCTA, CU.SSSCTA, CU.SSSSCTA");

		listQuerys.add(queryP.toString());

		queryS.append("SELECT T1.IDSECTOR, T1.CUENTA, T1.SCTA, T1.SSCTA, T1.SSSCTA, T1.SSSSCTA, SUBSTR(T1.NOMCTA,1,60) NOMCTA,")
				.append(" T1.SALIA, T1.SALFA, T1.CARGOS, T1.ABONOS,  CASE WHEN NATCTA = 'D' THEN ")
				.append(" SALIA + CARGOS - ABONOS ELSE  SALIA - CARGOS + ABONOS END TOTALES FROM (")
				.append("SELECT DISTINCT CU.IDSECTOR, CU.CUENTA CUENTA,NVL(CU.SCTA,'') SCTA,NVL(CU.SSCTA,'') SSCTA,")
				.append("NVL(CU.SSSCTA,'') SSSCTA,NVL(CU.SSSSCTA,'') SSSSCTA, CU.NOMCTA, UPPER(CU.STACTA) NATCTA,")
				.append(" CASE WHEN UPPER(CU.STACTA)='D' THEN ").append(carAboD).append(" ELSE ").append(carAboA)
				.append(" END SALIA,")
				.append("CASE WHEN UPPER(CU.STACTA)='D' THEN (CU.CARGOS_" + mes + ")-(CU.ABONOS_" + mes
						+ ") ELSE (CU.CARGOS_" + mes + ")+(CU.ABONOS_" + mes + ") END SALFA, (CU.CARGOS_" + mes
						+ ") CARGOS, (CU.ABONOS_" + mes + ") ABONOS ")
				.append(" FROM CUENTA CU ")
				.append(" WHERE CU.IDSECTOR=" + sector + " AND UPPER(CU.NIVCTA)='S' )T1 ");
		listQuerys.add(queryS.toString());
		
		return listQuerys;

	}

	public StreamedContent getTxt() {
		Map<String, Object> out;
		InputStream stream = null;
		MapSqlParameterSource parameter = new MapSqlParameterSource();
		List<String> listQuerys = this.generaQuerys(Integer.valueOf(mes), this.getUserDetails().getIdSector(), cuentaIni, cuentaFin);

		parameter.addValue("i_mes", Integer.valueOf(mes));
		parameter.addValue("i_sector",this.getUserDetails().getIdSector());
		parameter.addValue("i_queryp", listQuerys.get(0));
		parameter.addValue("i_querys", listQuerys.get(1));
		parameter.addValue("i_municipio", this.getUserDetails().getMunicipio());

		out = executePlService.callProcedure(PROCEDURE_NAME, parameter);

		if (Integer.valueOf(out.get("O_COD_ERROR").toString()) > 0) {
			try {
				stream = new FileInputStream(
						new File(out.get("O_RUTA_FILE").toString() + "/" + out.get("O_NAME_FILE").toString()));
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			}
			txt = new DefaultStreamedContent(stream, "application/txt", out.get("O_NAME_FILE").toString());
			generateNotificationFront(FacesMessage.SEVERITY_INFO, "Info!", out.get("O_MSG_ERROR").toString());
		} else {
			generateNotificationFront(FacesMessage.SEVERITY_ERROR, "Error!", out.get("O_MSG_ERROR").toString());
		}
		return txt;
	}

	public void setTxt(StreamedContent txt) {
		this.txt = txt;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getCuentaIni() {
		return cuentaIni;
	}

	public void setCuentaIni(String cuentaIni) {
		this.cuentaIni = cuentaIni;
	}

	public String getCuentaFin() {
		return cuentaFin;
	}

	public void setCuentaFin(String cuentaFin) {
		this.cuentaFin = cuentaFin;
	}

	public List<TcMes> getListMes() {
		return listMes;
	}

	public void setListMes(List<TcMes> listMes) {
		this.listMes = listMes;
	}

	public PolienRepository getPolienRepository() {
		return polienRepository;
	}

	public void setPolienRepository(PolienRepository polienRepository) {
		this.polienRepository = polienRepository;
	}

	public TcMesRepository getTcMesRepository() {
		return tcMesRepository;
	}

	public void setTcMesRepository(TcMesRepository tcMesRepository) {
		this.tcMesRepository = tcMesRepository;
	}

	public ConctbRepository getConctbRepository() {
		return conctbRepository;
	}

	public void setConctbRepository(ConctbRepository conctbRepository) {
		this.conctbRepository = conctbRepository;
	}

	public ValidatePolicyService getValidatePolicyService() {
		return validatePolicyService;
	}

	public void setValidatePolicyService(ValidatePolicyService validatePolicyService) {
		this.validatePolicyService = validatePolicyService;
	}

	public ExecutePlService getExecutePlService() {
		return executePlService;
	}

	public void setExecutePlService(ExecutePlService executePlService) {
		this.executePlService = executePlService;
	}

	public static String getDownloadXls() {
		return DOWNLOAD_XLS;
	}

	public static String getDownloadTxt() {
		return DOWNLOAD_TXT;
	}

	public static String getProcedureName() {
		return PROCEDURE_NAME;
	}

}
