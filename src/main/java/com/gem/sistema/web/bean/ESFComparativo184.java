package com.gem.sistema.web.bean;

import static com.roonin.utils.UtilDate.getLastDayByAnoEmp;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.StreamedContent;

import com.gem.sistema.business.domain.Conctb;
import com.gem.sistema.business.domain.TrPuestoFirma;
import com.gem.sistema.business.dto.EdoSF3211DTO;
import com.gem.sistema.business.repository.catalogs.ConctbRepository;
import com.gem.sistema.business.service.catalogos.EdoSF3211Service;
import com.gem.sistema.business.service.catalogos.PuestosFirmasService;
import com.gem.sistema.business.service.reportador.ReportValidationException;
import com.gem.sistema.util.ConstantsClaveEnnum;

// TODO: Auto-generated Javadoc
/**
 * The Class ESFComparativo184.
 */
@ManagedBean(name = "eSFComparativo184MB")
@ViewScoped
public class ESFComparativo184 extends ReportePeriodos {

	private Integer noDecimales;
	private Integer pesos;

	/** The conctb repository. */
	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository conctbRepository;

	@ManagedProperty("#{edoSF3211Service}")
	private EdoSF3211Service edoSF3211Service;

	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;

	@PostConstruct
	public void init() {
		noDecimales = 2;
		pesos = 1;
		jasperReporteName = "ESFComparativo184";
		endFilename = jasperReporteName + ".pdf";
		changePeriodo();
	}

	@Override
	public Map<String, Object> getParametersReports() throws ReportValidationException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		Integer sector = this.getUserDetails().getIdSector();
		TrPuestoFirma firma = null;
		Conctb conctb = conctbRepository.findByIdsector(getUserDetails().getIdSector());

		EdoSF3211DTO edoSF3211DTO = edoSF3211Service.executeQuery(sector, getMesInicial(), getMesSelected());
		System.out.println(":::->" + edoSF3211DTO.getTotalAct());
		parameters.put("decimalFormat", "%,." + noDecimales + "f");
		parameters.put("pImg", conctb.getImagePathLeft());
		parameters.put("pNomMun", conctb.getNomDep());
		parameters.put("pAnio", conctb.getAnoemp());
		firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
				ConstantsClaveEnnum.CLAVE_F08.getValue());
		parameters.put("pL2", firma.getPuesto().getPuesto());
		parameters.put("pN2", firma.getNombre());
		firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
				ConstantsClaveEnnum.CLAVE_F09.getValue());
		parameters.put("pL3", firma.getPuesto().getPuesto());
		parameters.put("pN3", firma.getNombre());
		firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
				ConstantsClaveEnnum.CLAVE_F10.getValue());
		parameters.put("pL4", firma.getPuesto().getPuesto());
		parameters.put("pN4", firma.getNombre());
		parameters.put("mesFinal", getNombreMesSelected().toUpperCase());
		parameters.put("mesInicial", getNombreMesInicial().toUpperCase());
		parameters.put("dia", getLastDayByAnoEmp(getMesSelected(), conctb.getAnoemp()));
		parameters.put("pSsql1", this.generaActivo());
		parameters.put("pSsql2", this.generaPasivo());
		parameters.put("pSsql3", this.generaPatrimonio());
		parameters.put("p3211Actual", edoSF3211DTO.getTotalAct());

		return parameters;
	}

	@Override
	public StreamedContent generaReporteSimple(int type) throws ReportValidationException {
		return null;
	}

	public String generaActivo() {
		return getQuery("1000", "1399");
	}

	public String generaPasivo() {
		return getQuery("2000", "2399");
	}

	public String generaPatrimonio() {
		return getQuery("3000", "3321");
	}

	public String getQuery(String cInicial, String cFinal) {

		StringBuilder sSql3 = new StringBuilder();
		String cargos = "";
		String abonos = "";
		for (int i = getMesInicial(); i <= getMesSelected(); i++) {
			cargos += "CARGOS_" + i + " +";
			abonos += "ABONOS_" + i + " +";
		}

		sSql3.append("SELECT CUENTA, NOMCTA, ANIO_ANTERIOR, ANIO_ACTUAL, ANIO_ACTUAL - ANIO_ANTERIOR VARIACION FROM( ")
				.append("SELECT CUENTA, NOMCTA, SALINIANT ANIO_ANTERIOR, CASE WHEN CUENTA IN ('1261', '1262', '1263') THEN (ANIO_ACTUAL * -1) ELSE ANIO_ACTUAL END ANIO_ACTUAL  FROM(")
				.append("SELECT 	CUENTA, STACTA, NOMCTA, SALINIANT, ")
				.append("DECODE(STACTA, 'D', SALINI + CARGOS - ABONOS, 'A', SALINI - CARGOS + ABONOS, 0) ANIO_ACTUAL ")
				.append("FROM( ")
				.append("SELECT 	SUBSTR(CUENTA,1,2) GRUPO, CUENTA, STACTA, NOMCTA, SALINIANT, SALINI, ")
				.append(cargos.substring(0, cargos.length() - 1)).append(" CARGOS, ")
				.append(abonos.substring(0, abonos.length() - 1)).append(" ABONOS ").append("FROM CUENTA ")
				.append("WHERE	IDSECTOR = ").append(this.getUserDetails().getIdSector()).append(" AND SCTA = '' ")
				.append("AND CUENTA BETWEEN '" + cInicial + "' AND '" + cFinal + "' ")
				.append("AND (SUBSTR(CUENTA,3,2) ='00' OR SUBSTR(CUENTA,4,1) <>'0') ")
				.append(")T1 )T2 )T3 ORDER BY CUENTA ");

		if (pesos != 1) {
			sSql3.insert(0,
					"SELECT CUENTA, NOMCTA, (ANIO_ANTERIOR / 1000) ANIO_ANTERIOR, (ANIO_ACTUAL/1000) ANIO_ACTUAL, (VARIACION/1000) VARIACION FROM ( ");
			sSql3.append(")T3");
		}

		System.out.println(sSql3.toString());
		return sSql3.toString();

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

	public Integer getNoDecimales() {
		return noDecimales;
	}

	public void setNoDecimales(Integer noDecimales) {
		this.noDecimales = noDecimales;
	}

	public Integer getPesos() {
		return pesos;
	}

	public void setPesos(Integer pesos) {
		this.pesos = pesos;
	}

	public EdoSF3211Service getEdoSF3211Service() {
		return edoSF3211Service;
	}

	public void setEdoSF3211Service(EdoSF3211Service edoSF3211Service) {
		this.edoSF3211Service = edoSF3211Service;
	}

}
