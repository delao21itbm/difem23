package com.gem.sistema.web.bean;

import static com.roonin.utils.UtilDate.getLastDayByAnoEmp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.collections4.CollectionUtils;
import org.primefaces.model.StreamedContent;

import com.gem.sistema.business.domain.Conctb;
import com.gem.sistema.business.domain.TcMes;
import com.gem.sistema.business.domain.TrPuestoFirma;
import com.gem.sistema.business.repository.catalogs.ConctbRepository;
import com.gem.sistema.business.repository.catalogs.TcMesRepository;
import com.gem.sistema.business.service.catalogos.PuestosFirmasService;
import com.gem.sistema.business.service.catalogos.ValidatePolicyService;
import com.gem.sistema.business.service.reportador.ReportValidationException;
import com.gem.sistema.util.ConstantsClaveEnnum;

// TODO: Auto-generated Javadoc
/**
 * The Class RF001029MB.
 */
@ManagedBean(name = "rF001029MB")
@ViewScoped
public class RF001029MB extends BaseDirectReport {

	/** The mes. */
	private String mes;

	/** The list mes. */
	private List<TcMes> listMes;

	/** The tc mes repository. */
	@ManagedProperty("#{tcMesRepository}")
	private TcMesRepository tcMesRepository;

	@ManagedProperty("#{validatePolicyService}")
	private ValidatePolicyService validatePolicyService;

	/** The conctb repository. */
	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository conctbRepository;

	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;

	public ValidatePolicyService getValidatePolicyService() {
		return validatePolicyService;
	}

	public void setValidatePolicyService(ValidatePolicyService validatePolicyService) {
		this.validatePolicyService = validatePolicyService;
	}

	/**
	 * Gets the mes.
	 *
	 * @return the mes
	 */
	public String getMes() {
		return mes;
	}

	/**
	 * Sets the mes.
	 *
	 * @param mes the new mes
	 */
	public void setMes(String mes) {
		this.mes = mes;
	}

	/**
	 * Gets the list mes.
	 *
	 * @return the list mes
	 */
	public List<TcMes> getListMes() {
		return listMes;
	}

	/**
	 * Sets the list mes.
	 *
	 * @param listMes the new list mes
	 */
	public void setListMes(List<TcMes> listMes) {
		this.listMes = listMes;
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

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		jasperReporteName = "RF001029_Cap";
		endFilename = jasperReporteName + ".pdf";

		listMes = tcMesRepository.findAll();

		if (!CollectionUtils.isEmpty(listMes)) {
			mes = listMes.get(0).getMes();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gem.sistema.web.bean.BaseDirectReport#getParametersReports()
	 */
	@Override
	public Map<String, Object> getParametersReports() throws ReportValidationException {
		Map<String, Object> parameters;
		TcMes descripMes = tcMesRepository.findByMes(mes);
		parameters = new HashMap<String, Object>();
		Integer sector = this.getUserDetails().getIdSector();
		TrPuestoFirma firma = null;
		Conctb conctb = conctbRepository.findByIdsector(this.getUserDetails().getIdSector());

		parameters.put("pImg", conctb.getImagePathRigth());
		parameters.put("pNomMun", conctb.getNomDep());
		parameters.put("pLastDay", getLastDayByAnoEmp(Integer.valueOf(mes), conctb.getAnoemp()));
		parameters.put("pDescripMes", descripMes.getDescripcion());
		parameters.put("pAnio", conctb.getAnoemp());

		if (conctb.getClave().substring(0, 1).equals("0")) {
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
					ConstantsClaveEnnum.CLAVE_F01.getValue());
			parameters.put("pL1", firma.getPuesto().getPuesto());
			parameters.put("pN1", firma.getNombre());
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
					ConstantsClaveEnnum.CLAVE_F03.getValue());
			parameters.put("pL3", firma.getPuesto().getPuesto());
			parameters.put("pN3", firma.getNombre());
		} else if (conctb.getClave().substring(0, 1).equals("2")) {
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
					ConstantsClaveEnnum.CLAVE_F10.getValue());
			parameters.put("pL1", firma.getPuesto().getPuesto());
			parameters.put("pN1", firma.getNombre());
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
					ConstantsClaveEnnum.CLAVE_F11.getValue());
			parameters.put("pL3", firma.getPuesto().getPuesto());
			parameters.put("pN3", firma.getNombre());
		} else if (conctb.getClave().substring(0, 1).equals("3")) {
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
					ConstantsClaveEnnum.CLAVE_F07.getValue());
			parameters.put("pL1", firma.getPuesto().getPuesto());
			parameters.put("pN1", firma.getNombre());
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
					ConstantsClaveEnnum.CLAVE_F08.getValue());
			parameters.put("pL2", firma.getPuesto().getPuesto());
			parameters.put("pN2", firma.getNombre());
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
					ConstantsClaveEnnum.CLAVE_F09.getValue());
			parameters.put("pL3", firma.getPuesto().getPuesto());
			parameters.put("pN3", firma.getNombre());
		} else if (conctb.getClave().substring(0, 1).equals("4")) {

			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
					ConstantsClaveEnnum.CLAVE_F20.getValue());
			parameters.put("pL1", firma.getPuesto().getPuesto());
			parameters.put("pN1", firma.getNombre());
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
					ConstantsClaveEnnum.CLAVE_F21.getValue());
			parameters.put("pL2", firma.getPuesto().getPuesto());
			parameters.put("pN2", firma.getNombre());
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L,
					ConstantsClaveEnnum.CLAVE_F24.getValue());
			parameters.put("pL3", firma.getPuesto().getPuesto());
			parameters.put("pN3", firma.getNombre());
		}

	

	parameters.put("pSsql",this.generaQuery(this.getUserDetails().getIdSector(),Integer.valueOf(mes)));

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
	 * @param mes      the mes
	 * @return the string
	 */
	public String generaQuery(Integer idsector, Integer mes) {
		StringBuilder sSql = new StringBuilder();
		StringBuilder ampli = new StringBuilder();
		StringBuilder redu = new StringBuilder();
		StringBuilder ejpa = new StringBuilder();
		StringBuilder ejxpa = new StringBuilder();
		StringBuilder toeje = new StringBuilder();
		StringBuilder compro = new StringBuilder();

		sSql.append("SELECT ").append("T2.NOMBRE,").append("T2.PARTIDA,").append("T2.APROBADO,").append("T2.AMPLIREDU,")
				.append("T2.MODIFICADO,").append("T2.COMPROMETIDO,").append("T2.DEVENGADO,").append("T2.EJERCIDO,")
				.append("T2.PAGADO,").append("(T2.TOEJE-MODIFICADO) SUBEJERCIDO").append(" FROM ( ").append(" SELECT ")
				.append("T1.NOMBRE,").append("T1.PARTIDA,").append("T1.AUTO APROBADO,")
				.append("(T1.AMPLI+T1.REDU)AMPLIREDU,").append("((T1.AUTO)+(T1.AMPLI)-(T1.REDU))MODIFICADO,")
				.append("T1.COMPRO COMPROMETIDO,").append("T1.EJXPA DEVENGADO,").append("T1.TOEJE EJERCIDO,")
				.append("T1.EJPA PAGADO,").append("T1.TOEJE").append(" FROM (SELECT ").append("N.NOMGAS NOMBRE,")
				.append("P.PARTIDA PARTIDA,")
				.append("SUM(P.AUTO1_1+P.AUTO1_2+P.AUTO1_3+P.AUTO1_4+P.AUTO1_5+P.AUTO1_6+P.AUTO1_7+P.AUTO1_8+P.AUTO1_9+P.AUTO1_10+P.AUTO1_11+P.AUTO1_12)AUTO,");

		for (int i = 1; i <= mes; i++) {

			ampli.append(" P.AMPLI1_" + i + "+");
			redu.append(" P.REDU1_" + i + "+");
			ejpa.append(" P.EJPA1_" + i + "+");
			ejxpa.append(" P.EJXPA1_" + i + "+");
			toeje.append(" P.TOEJE1_" + i + "+");
			compro.append(" P.COMPRO1_" + i + "+");

		}

		sSql.append(" SUM(").append(ampli.substring(1, ampli.length() - 1)).append(") AMPLI,").append(" SUM(")
				.append(redu.substring(1, redu.length() - 1)).append(") REDU,").append(" SUM(")
				.append(ejpa.substring(1, ejpa.length() - 1)).append(") EJPA,").append(" SUM(")
				.append(ejxpa.substring(1, ejxpa.length() - 1)).append(") EJXPA,").append(" SUM(")
				.append(toeje.substring(1, toeje.length() - 1)).append(" )TOEJE,").append(" SUM(")
				.append(compro.substring(1, compro.length() - 1)).append(" )COMPRO").append(" FROM PASO P")
				.append(" JOIN NATGAS N ON N.CLVGAS=P.PARTIDA AND N.IDSECTOR=P.IDSECTOR")
				.append(" WHERE P.IDSECTOR=" + idsector).append(" GROUP BY N.NOMGAS,").append("P.PARTIDA")
				.append(" ORDER BY").append(" P.PARTIDA ASC)T1)T2");

		// LOGGER.info("QUERY,{}", sSql.toString());
		System.out.println(sSql.toString());
		return sSql.toString();

	}

	public void validateMonth() {
		this.validatePolicyService.isOpenMonth(Integer.valueOf(mes), null, this.getUserDetails().getIdSector());
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
