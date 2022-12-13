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
import com.gem.sistema.business.repository.catalogs.TcPeriodoRepositoy;
import com.gem.sistema.business.service.catalogos.PuestosFirmasService;
import com.gem.sistema.business.service.catalogos.ValidatePolicyService;
import com.gem.sistema.business.service.reportador.ReportValidationException;
import com.gem.sistema.util.ConstantsClaveEnnum;

import static com.roonin.utils.UtilDate.getLastDayByAnoEmp;;

// TODO: Auto-generated Javadoc
/**
 * The Class RF0010113MB.
 */
@ManagedBean(name = "rF0010113MB")
@ViewScoped
public class RF0010113MB extends ReportePeriodos {

	/** The saldo. */
	private String saldo;

	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;

	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository conctbRepository;

	/** Servicio para validacion de mes aprturado, polizas, afectacion */

	@ManagedProperty("#{validatePolicyService}")
	private ValidatePolicyService validatePolicyService;

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

		parameters.put("mesName", getNombreMesSelected().toUpperCase());
		parameters.put("mesNameInicial", getNombreMesInicial().toUpperCase());
		parameters.put("lastDay", getLastDayByAnoEmp(getMesSelected(), conctb.getAnoemp()));
		parameters.put("year", conctb.getAnoemp());
		parameters.put("imagePath", idSector == 1 ? conctb.getImagePathRigth() : conctb.getImagePathLeft());
		parameters.put("query", this.generateQuery(idSector, saldo.equals("S") ? 1 : 0));
		parameters.put("municipio", conctb.getNomDep());

		if (idSector == 1) {
			switch (conctb.getClave().substring(0, 1)) {
			case "0":
				parameters.put("pNoFirmas", 3);
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F01.getValue());
				parameters.put("firmaP1", firma.getPuesto().getPuesto());
				parameters.put("firmaN1", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F03.getValue());
				parameters.put("firmaP2", firma.getPuesto().getPuesto());
				parameters.put("firmaN2", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F02.getValue());
				parameters.put("firmaP3", firma.getPuesto().getPuesto());
				parameters.put("firmaN3", firma.getNombre());
				break;
			case "2": // ODAS
				parameters.put("pNoFirmas", 3);
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F10.getValue());
				parameters.put("firmaP1", firma.getPuesto().getPuesto());
				parameters.put("firmaN1", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F11.getValue());
				parameters.put("firmaP2", firma.getPuesto().getPuesto());
				parameters.put("firmaN2", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F12.getValue());
				parameters.put("firmaP3", firma.getPuesto().getPuesto());
				parameters.put("firmaN3", firma.getNombre());
				break;
			case "3":
				parameters.put("pNoFirmas", 3);
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F07.getValue());
				parameters.put("firmaP1", firma.getPuesto().getPuesto());
				parameters.put("firmaN1", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F08.getValue());
				parameters.put("firmaP2", firma.getPuesto().getPuesto());
				parameters.put("firmaN2", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F09.getValue());
				parameters.put("firmaP3", firma.getPuesto().getPuesto());
				parameters.put("firmaN3", firma.getNombre());
				break;
			case "4":
				parameters.put("pNoFirmas", 2);
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F20.getValue());
				parameters.put("firmaP1", firma.getPuesto().getPuesto());
				parameters.put("firmaN1", firma.getNombre());
				firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F21.getValue());
				parameters.put("firmaP3", firma.getPuesto().getPuesto());
				parameters.put("firmaN3", firma.getNombre());
				break;
			}

		} else {
			parameters.put("pNoFirmas", 3);
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
					ConstantsClaveEnnum.CLAVE_F08.getValue());
			parameters.put("firmaP1", firma.getPuesto().getPuesto());
			parameters.put("firmaN1", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
					ConstantsClaveEnnum.CLAVE_F09.getValue());
			parameters.put("firmaP2", firma.getPuesto().getPuesto());
			parameters.put("firmaN2", firma.getNombre());
			firma = puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
					ConstantsClaveEnnum.CLAVE_F10.getValue());
			parameters.put("firmaP3", firma.getPuesto().getPuesto());
			parameters.put("firmaN3", firma.getNombre());
		}

		return parameters;
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		jasperReporteName = "RF0010_1_13";
		endFilename = jasperReporteName + ".pdf";
		this.mesAnterior = true;
		changePeriodo();
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
	 * Generate query.
	 *
	 * @param mes        the mes
	 * @param idSector   the id sector
	 * @param saldosCero the saldos cero
	 * @return the string
	 */
	public String generateQuery(Integer idSector, Integer saldosCero) {

		StringBuilder sQuery = new StringBuilder();
		StringBuilder sCargosAnt = new StringBuilder();
		StringBuilder sAbonosAnt = new StringBuilder();

		sQuery.append("SELECT CUENTA, NOMCTA, CASE WHEN CUENTA IN ('1261', '1262', '1263') THEN (SALDOINICIAL* -1) ELSE SALDOINICIAL END SALDOINICIAL, CASE WHEN CUENTA IN ('1261', '1262', '1263') THEN (CARGOS_MES* -1) ELSE CARGOS_MES END CARGOS_MES, CASE WHEN CUENTA IN ('1261', '1262', '1263') THEN (ABONOS_MES* -1) ELSE ABONOS_MES END ABONOS_MES, CASE WHEN CUENTA IN ('1261', '1262', '1263') THEN (SALDOFINAL* -1) ELSE SALDOFINAL END SALDOFINAL, CASE WHEN CUENTA IN ('1261', '1262', '1263') THEN (FLUJO* -1) ELSE FLUJO END FLUJO FROM (");
		sQuery.append(
				"SELECT CUENTA, NOMCTA, SALDOINICIAL, CARGOS_MES, ABONOS_MES,CASE WHEN NATCTA = 'A' THEN CASE WHEN CUENTA IN ('1115','1161') THEN (SALDOINICIAL + CARGOS_MES - ABONOS_MES) ELSE (SALDOINICIAL - CARGOS_MES + ABONOS_MES) END ELSE (SALDOINICIAL + CARGOS_MES - ABONOS_MES) END SALDOFINAL,(SALDOINICIAL - (SALDOINICIAL + CARGOS_MES - ABONOS_MES)) FLUJO FROM ( SELECT CUENTA, NOMCTA, NATCTA, CASE WHEN NATCTA IN('A') THEN CASE WHEN CUENTA IN ('1115','1161') THEN ABS(SALINI + CARGOS_ANT - ABONOS_ANT) ELSE ABS(SALINI - CARGOS_ANT + ABONOS_ANT) END ELSE ABS(SALINI + CARGOS_ANT - ABONOS_ANT) END SALDOINICIAL, CARGOS_MES, ABONOS_MES FROM ( SELECT CU.CUENTA, MA.NATCTA, CU.NOMCTA, CU.SALINI,");

		if (getMesSelected() > 1) {
			for (int i = getMesInicial(); i < getMesSelected(); i++) {
				sCargosAnt.append(" CU.CARGOS_" + i + " +");
				sAbonosAnt.append(" CU.ABONOS_" + i + " +");
			}
		} else {
			sCargosAnt.append("  '0'  ");
			sAbonosAnt.append("  '0'  ");
		}

		sQuery.append(sCargosAnt.substring(0, sCargosAnt.length() - 1) + "CARGOS_ANT,")
				.append(sAbonosAnt.substring(0, sAbonosAnt.length() - 1) + "ABONOS_ANT,")
				.append(" CU.CARGOS_" + getMesSelected() + " CARGOS_MES, CU.ABONOS_" + getMesSelected()
						+ " ABONOS_MES ")
				.append(" FROM CUENTA AS CU, MAYCTA AS MA WHERE CU.IDSECTOR=" + idSector
						+ " AND CU.CUENTA = MA.CUENTA AND CU.SCTA = '' AND CU.SSCTA = '' AND CU.SSSCTA = '' AND CU.SSSSCTA = '' AND CU.CUENTA <= 1300 AND SUBSTR(CU.CUENTA,2,3) <> '000'  AND CU.NOTCTA <> 3 ) T1 )T2 )T3");
		if (saldosCero == 0) {
			sQuery.append(
					" WHERE SALDOINICIAL <> 0 OR CARGOS_MES <> 0 OR ABONOS_MES <> 0 OR SALDOFINAL <> 0 OR FLUJO <>0");
		}
		
		System.out.println("::::::->" + sQuery.toString()); 
		return sQuery.toString();
	}

	/** Metodo de validacion al descargar el PDF */
	public void viewPdf() {
		if (this.validatePolicyService.isOpenMonth(getMesSelected(), null,
				this.getUserDetails().getIdSector()) == Boolean.TRUE) {
			this.validatePolicyService.isAfectMonth(getMesSelected(), null, this.getUserDetails().getIdSector());
			this.validatePolicyService.existPolices(getMesSelected(), null, this.getUserDetails().getIdSector());
			this.createFilePdfInline();
		}
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

	public TcPeriodoRepositoy getPeriodoRepositoy() {
		return periodoRepositoy;
	}

	public void setPeriodoRepositoy(TcPeriodoRepositoy periodoRepositoy) {
		this.periodoRepositoy = periodoRepositoy;
	}

	/**
	 * Gets the saldo.
	 *
	 * @return the saldo
	 */
	public String getSaldo() {
		return saldo;
	}

	/**
	 * Sets the saldo.
	 *
	 * @param saldo the new saldo
	 */
	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}

	public ValidatePolicyService getValidatePolicyService() {
		return validatePolicyService;
	}

	public void setValidatePolicyService(ValidatePolicyService validatePolicyService) {
		this.validatePolicyService = validatePolicyService;
	}
}
