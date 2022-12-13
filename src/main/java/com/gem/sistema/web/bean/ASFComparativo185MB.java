package com.gem.sistema.web.bean;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.StreamedContent;

import com.gem.sistema.business.domain.Conctb;
import com.gem.sistema.business.domain.TrPuestoFirma;
import com.gem.sistema.business.repository.catalogs.ConctbRepository;
import com.gem.sistema.business.repository.catalogs.FirmasRepository;
import com.gem.sistema.business.service.catalogos.PuestosFirmasService;
import com.gem.sistema.business.service.reportador.ReportValidationException;
import com.gem.sistema.util.ConstantsClaveEnnum;

// TODO: Auto-generated Javadoc
/**
 * The Class ASFComparativo185MB.
 */
@ManagedBean(name = "aSFComparativo185MB")
@ViewScoped
public class ASFComparativo185MB extends BaseDirectReport {

	private Integer noDecimales;
	private Integer pesos;

	/** The firmas repository. */
	@ManagedProperty("#{firmasRepository}")
	private FirmasRepository firmasRepository;

	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository conctbRepository;

	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;

	/**
	 * Gets the firmas repository.
	 *
	 * @return the firmas repository
	 */
	public FirmasRepository getFirmasRepository() {
		return firmasRepository;
	}

	/**
	 * Sets the firmas repository.
	 *
	 * @param firmasRepository the new firmas repository
	 */
	public void setFirmasRepository(FirmasRepository firmasRepository) {
		this.firmasRepository = firmasRepository;
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		jasperReporteName = "ASFComparativo185";
		endFilename = jasperReporteName + ".pdf";
		noDecimales = 2;
		pesos = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gem.sistema.web.bean.BaseDirectReport#getParametersReports()
	 */
	@Override
	public Map<String, Object> getParametersReports() throws ReportValidationException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		Integer idSector = this.getUserDetails().getIdSector();
		Conctb conctb = conctbRepository.findByIdsector(idSector);
		TrPuestoFirma firma = new TrPuestoFirma();

		parameters.put("decimalFormat", "%,." + noDecimales + "f");
		parameters.put("pImg", conctb.getImagePathLeft());
		parameters.put("pNomMun", this.getUserDetails().getMunicipio());
		parameters.put("pAnio", conctb.getAnoemp().toString());
		firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F27.getValue());
		parameters.put("pL1", firma.getPuesto().getPuesto());
		parameters.put("pN1", firma.getNombre());
		firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F05.getValue());
		parameters.put("pL2", firma.getPuesto().getPuesto());
		parameters.put("pN2", firma.getNombre());
		firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L, ConstantsClaveEnnum.CLAVE_F03.getValue());
		parameters.put("pL3", firma.getPuesto().getPuesto());
		parameters.put("pN3", firma.getNombre());
		parameters.put("pSsql", this.generaQuery(this.getUserDetails().getIdSector(), 12));

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
	 * Genera query.
	 *
	 * @param idsector the idsector
	 * @param xmes     the xmes
	 * @return the string
	 */
	public String generaQuery(Integer idsector, Integer xmes) {

		StringBuilder cargos = new StringBuilder();
		StringBuilder abonos = new StringBuilder();
		StringBuilder sSql = new StringBuilder();

		sSql.append(
				"SELECT (T1.CUENTA||' '||NVL(T1.SCTA,'')||' '||NVL(T1.SSCTA,'')||' '||NVL(T1.SSSCTA,'')||' '||NVL(T1.SSSSCTA,'')) CUENTA,")
				.append("T1.NOMBRE,").append("T1.NATCTA,").append(" T1.SALINI,").append(" T1.CARGOS,")
				.append(" T1.ABONOS,").append(" CASE WHEN T1.NATCTA ='D' THEN (T1.SALINI+T1.CARGOS-T1.ABONOS)")
				.append(" ELSE (T1.SALINI-T1.CARGOS+T1.ABONOS)").append(" END XSF")
				.append(" FROM (SELECT C.CUENTA CUENTA,").append(" C.SCTA SCTA,").append(" C.SSCTA SSCTA,")
				.append("C.SSSCTA SSSCTA,").append("C.SSSSCTA SSSSCTA,").append("C.NOMCTA NOMBRE,")
				.append(" M.NATCTA NATCTA,").append(" C.SALINI SALINI,");

		for (int i = 1; i <= xmes; i++) {

			cargos.append(" C.CARGOS_" + i + "+");
			abonos.append(" C.ABONOS_" + i + "+");

		}
		sSql.append(" SUM(").append(cargos.substring(1, cargos.length() - 1)).append(")CARGOS,").append(" SUM(")
				.append(abonos.substring(1, abonos.length() - 1)).append(" )ABONOS").append(" FROM CUENTA C")
				.append(" JOIN MAYCTA M ON M.CUENTA = C.CUENTA").append(" WHERE C.IDSECTOR=" + idsector)
				.append(" AND C.CUENTA>='1000' AND C.CUENTA<='3999'").append(" GROUP BY C.CUENTA,").append(" C.SCTA,")
				.append(" C.SSCTA,").append(" C.SSSCTA,").append(" C.SSSSCTA,").append("C.NOMCTA,").append(" M.NATCTA,")
				.append(" C.SALINI").append(" ORDER BY C.CUENTA,").append(" C.SCTA,").append(" C.SSCTA,")
				.append(" C.SSSCTA,").append(" C.SSSSCTA ASC)T1");

		if (pesos > 0) {
			sSql.insert(0, "SELECT CUENTA, NOMBRE, NATCTA, (SALINI / 1000.0) SALINI, "
					+ "(CARGOS / 1000.0) CARGOS, (ABONOS / 1000.0) ABONOS, (XSF / 1000.0) XSF FROM (");
			sSql.append(" )T2");
		}
		System.out.println(sSql.toString());
		return sSql.toString();

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

}
