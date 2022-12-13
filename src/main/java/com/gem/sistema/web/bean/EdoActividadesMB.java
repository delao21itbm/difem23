package com.gem.sistema.web.bean;

/*

Object: SP_GENERA_TXT_EDO_ACTIVIDADES, EdoActividadesMB

Fecha creacion                    Autor                             Descripcion                      Versión                          Linea de Codigo

---------------       ---------------------------------     -----------------------------          -----------                     -------------------

08/03/2021            Javier Tenorio López          		Se agrega cuenta 4151 al 
           													Reporte Estado de Actividades				1.0                   				5

*/
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.gem.sistema.business.domain.Conctb;
import com.gem.sistema.business.domain.TrPuestoFirma;
import com.gem.sistema.business.repository.catalogs.ConctbRepository;
import com.gem.sistema.business.service.callsp.ExecutePlService;
import com.gem.sistema.business.service.catalogos.PuestosFirmasService;
import com.gem.sistema.business.service.reportador.ReportValidationException;
import com.gem.sistema.util.ConstantsClaveEnnum;

// TODO: Auto-generated Javadoc
/**
 * The Class EdoActividadesMB.
 */
@ManagedBean
@ViewScoped
public class EdoActividadesMB extends ReportePeriodos {

	/** The Constant PROCEDURE_NAME. */
	private static final String PROCEDURE_NAME = "SP_GENERA_TXT_EDO_ACTIVIDADES";

	private String fideicomiso;
	private Integer numeroFirmas = 4;

	/** The listquery. */
	private List<String> listquery;

	/** The file txt. */
	private StreamedContent fileTxt;

	/** The conctb repository. */
	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository conctbRepository;

	/** The execute pl service. */
	@ManagedProperty("#{executePlService}")
	protected ExecutePlService executePlService;

	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		jasperReporteName = "edoactividadesc";
		endFilename = jasperReporteName + ".pdf";

		if (numeroFirmas == 4) {
			tipoPeriodo = MES;
		}
		changePeriodo();
	}

	/**
	 * Generate querys.
	 *
	 * @param mes    the mes
	 * @param sector the sector
	 * @return the list
	 */
	public List<String> generateQuerys(Integer sector, String fideicomisos) {
		List<String> list = new ArrayList<String>();
		StringBuilder cargos = new StringBuilder();
		StringBuilder abonos = new StringBuilder();
		StringBuilder cargo = new StringBuilder();
		StringBuilder abono = new StringBuilder();
		StringBuilder queryEgresos = new StringBuilder();
		StringBuilder queryIngresos = new StringBuilder();
		Integer i = 1;

		while (i <= getMesSelected()) {
			cargos.append(" NVL(CARGOS_" + i + ",0) + ");
			abonos.append(" NVL(ABONOS_" + i + ",0) + ");
			cargo.append(" NVL(C.CARGOS_" + i + ",0) + ");
			abono.append(" NVL(C.ABONOS_" + i + ",0) + ");
			i++;
		}

		if (fideicomisos.equals("N")) {
			queryEgresos.append("SELECT	CLVGAS, ").append(
					"DECODE(CLVGAS,'1000','5100','2000','5100','3000','5100','4000','5200','5000','5700','6000','5600','8000','5300','9000','5400','')")
					.append("||' '||CLVGAS || ' ' || NOMGAS NOMBRE, MES, ACUMULADO ")
					.append("FROM(SELECT CLVGAS, NOMGAS, ")
					.append("SUM((" + cargo.substring(0, cargo.length() - 2) + ") - ("
							+ abono.substring(0, abono.length() - 2) + ") + NVL(C.SALINI,0)) ACUMULADO, ")
					.append("SUM(NVL(C.CARGOS_" + getMesSelected() + ",0) - NVL(C.ABONOS_" + getMesSelected()
							+ ",0) + NVL(C.SALINI,0)) MES ")
					.append("FROM NATGAS N LEFT JOIN CUENTA C ON N.CLVGAS = SUBSTR(C.SSSCTA,1,1)||'000' ")
					.append("AND C.CUENTA IN ('5100', '5200', '5300', '5400','5600','5700') AND C.IDSECTOR=N.IDSECTOR ")
					.append("WHERE N.IDSECTOR=" + sector + " AND SUBSTR(N.CLVGAS,2,3)='000' ")
					.append("GROUP BY CLVGAS, NOMGAS ) T1 ").append("WHERE CLVGAS <> '7000' ").append(" UNION ALL ")
					.append("SELECT '9999', C.CUENTA||' '||C.NOMCTA, ")
					.append("(NVL(C.CARGOS_" + getMesSelected() + ",0) - NVL(C.ABONOS_" + getMesSelected()
							+ ",0) + NVL(C.SALINI,0)) MES, ")
					.append("((" + cargo.substring(0, cargo.length() - 2) + ") - ("
							+ abono.substring(0, abono.length() - 2) + ") + NVL(C.SALINI,0)) ACUMULADO ")
					.append(" FROM CUENTA C WHERE C.CUENTA IN ('5500') AND C.SCTA='' AND C.IDSECTOR=" + sector
							+ " ORDER BY CLVGAS");

			queryIngresos.append("SELECT	2,SUBSTR(CU.CUENTA,1,3)||'0' CUENTA, MA.NOMMAY, SUM(("
					/*
					 * 
					 * Se agregarón las 5 líneas siguientes para mostrar la cuenta "4151" en el
					 * Reporte Estado de Actividades
					 * 
					 */
					/* JTL08032021 */
					+ abonos.substring(0, abonos.length() - 2) + ") - (" + cargos.substring(0, cargos.length() - 2)
					+ ") + NVL(SALINI,0)) ACUMULADO,SUM((ABONOS_" + getMesSelected() + " - CARGOS_" + getMesSelected()
					+ ") + NVL(SALINI,0)) MENSUAL FROM CUENTA CU, MAYCTA MA WHERE SUBSTR(CU.CUENTA,1,3)||'0'=MA.CUENTA AND SUBSTR(CU.CUENTA,1,4) = '4151' AND CU.SSSCTA <> '' AND CU.IDSECTOR= "
					+ sector + " GROUP BY SUBSTR(CU.CUENTA,1,3)||'0', MA.NOMMAY ")
					.append("UNION SELECT	2,SUBSTR(CU.CUENTA,1,3)||'0' , MA.NOMMAY, SUM(("
							/* JTL08032021 */
							+ abonos.substring(0, abonos.length() - 2) + ") - ("
							+ cargos.substring(0, cargos.length() - 2) + ") + NVL(SALINI,0)) ACUMULADO,SUM((ABONOS_"
							+ getMesSelected() + " - CARGOS_" + getMesSelected()
							+ ") + NVL(SALINI,0)) MENSUAL FROM CUENTA CU, MAYCTA MA WHERE SUBSTR(CU.CUENTA,1,3)||'0'=MA.CUENTA AND SUBSTR(CU.CUENTA,1,2) = '43' AND CU.SSSCTA <> '' AND CU.IDSECTOR= "
							+ sector + " GROUP BY SUBSTR(CU.CUENTA,1,3)||'0', MA.NOMMAY ")
					.append("UNION SELECT	3,SUBSTR(CU.CUENTA,1,3)||'0' , MA.NOMMAY, SUM(("
							+ abonos.substring(0, abonos.length() - 2) + ") - ("
							+ cargos.substring(0, cargos.length() - 2) + ") + NVL(SALINI,0)) ACUMULADO,SUM((ABONOS_"
							+ getMesSelected() + " - CARGOS_" + getMesSelected()
							+ ") + NVL(SALINI,0)) MENSUAL FROM CUENTA CU, MAYCTA MA WHERE SUBSTR(CU.CUENTA,1,3)||'0'=MA.CUENTA AND SUBSTR(CU.CUENTA,1,2) = '42' AND CU.SSSCTA <> '' AND CU.IDSECTOR= "
							+ sector + " GROUP BY SUBSTR(CU.CUENTA,1,3)||'0', MA.NOMMAY ORDER BY 1,2");
		} else {
			queryEgresos.append("SELECT 0 CLVGAS, SCTA||' '||CLVGAS || ' ' || NOMCTA NOMBRE, MES, ACUMULADO FROM ( ")
					.append("	SELECT	C.SCTA, C.NOMCTA, CUENTA CLVGAS,")
					.append(" SUM(NVL(C.CARGOS_" + getMesSelected() + ",0) - NVL(C.ABONOS_" + getMesSelected()
							+ ",0) + NVL(C.SALINI,0)) MES, ")
					.append(" SUM(( " + cargos.substring(0, cargos.length() - 2) + " ) - " + "( "
							+ abonos.substring(0, abonos.length() - 2) + " ) + " + " NVL(C.SALINI,0)) ACUMULADO ")
					.append("		FROM CUENTA C ")
					.append("	WHERE C.CUENTA IN ('5100', '5200', '5300', '5400','5500','5600','5700') ")
					.append("		AND C.IDSECTOR=" + sector + " AND C.SCTA <>'' AND C.SSCTA='' ")
					.append("	GROUP BY C.SCTA, C.NOMCTA,CUENTA ORDER BY C.SCTA ) T1");

			queryIngresos.append("SELECT 0, SCTA CUENTA, NOMCTA NOMMAY, ACUMULADO, MENSUAL FROM ( ")
					.append("	SELECT	CU.SCTA, MAX(CU.NOMCTA) NOMCTA, ")
					.append(" SUM((ABONOS_" + getMesSelected() + " - CARGOS_" + getMesSelected()
							+ ") + NVL(SALINI,0)) MENSUAL, ")
					.append(" SUM(( " + abonos.substring(0, abonos.length() - 2) + " ) - " + "( "
							+ cargos.substring(0, cargos.length() - 2) + " ) + " + "NVL(SALINI,0)) ACUMULADO ")
					.append("		FROM CUENTA CU ").append("	WHERE CU.IDSECTOR=").append(sector)
					.append("		AND SUBSTR(CU.CUENTA,1,2) IN ('41', '42', '43') ")
					.append("		AND CU.SCTA<>'' AND CU.SSCTA='' ").append("	GROUP BY CU.SCTA ORDER BY CU.SCTA )T1");
		}

		list.add(queryEgresos.toString());
		list.add(queryIngresos.toString());
		System.out.println("Egresos: " + queryEgresos.toString());
		System.out.println("Ingresos: " + queryIngresos.toString());
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gem.sistema.web.bean.BaseDirectReport#getParametersReports()
	 */
	@Override
	public Map<String, Object> getParametersReports() throws ReportValidationException {
		Map<String, Object> parameters = new java.util.HashMap<String, Object>();
		TrPuestoFirma firma = new TrPuestoFirma();
		Integer idSector = this.getUserDetails().getIdSector();
		Conctb conctb = conctbRepository.findByIdsector(idSector);
		listquery = generateQuerys(this.getUserDetails().getIdSector(), fideicomiso);

		parameters.put("lastDayOfMonth", getLastDayByAnoEmp(getMesSelected(), conctb.getAnoemp()));
		parameters.put("mesName", getNombreMesSelected().toUpperCase());
		parameters.put("year", conctb.getAnoemp());
		parameters.put("pathImage1", conctb.getImagePathLeft());
		parameters.put("pathImage2", conctb.getImagePathRigth());
		parameters.put("queryEgresos", listquery.get(0));
		parameters.put("queryIngresos", listquery.get(1));
		parameters.put("usuario", getUserDetails().getUsername());
		parameters.put("entidadName", conctb.getNomDep());
		parameters.put("entidadRfc", conctb.getRfc());

		if (idSector == 1) {
			switch (conctb.getClave().substring(0, 1)) {
			case "0":
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F07.getValue());
				parameters.put("firmaP1", firma.getPuesto().getPuesto());
				parameters.put("firmaN1", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F08.getValue());
				parameters.put("firmaP2", firma.getPuesto().getPuesto());
				parameters.put("firmaN2", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F09.getValue());
				parameters.put("firmaP3", firma.getPuesto().getPuesto());
				parameters.put("firmaN3", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F10.getValue());
				parameters.put("firmaP4", firma.getPuesto().getPuesto());
				parameters.put("firmaN4", firma.getNombre());
				break;
			case "2":
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F07.getValue());
				parameters.put("firmaP1", firma.getPuesto().getPuesto());
				parameters.put("firmaN1", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F08.getValue());
				parameters.put("firmaP2", firma.getPuesto().getPuesto());
				parameters.put("firmaN2", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F09.getValue());
				parameters.put("firmaP3", firma.getPuesto().getPuesto());
				parameters.put("firmaN3", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F10.getValue());
				parameters.put("firmaP4", firma.getPuesto().getPuesto());
				parameters.put("firmaN4", firma.getNombre());
				break;
			case "3":
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F07.getValue());
				parameters.put("firmaP1", firma.getPuesto().getPuesto());
				parameters.put("firmaN1", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F08.getValue());
				parameters.put("firmaP2", firma.getPuesto().getPuesto());
				parameters.put("firmaN2", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F09.getValue());
				parameters.put("firmaP3", firma.getPuesto().getPuesto());
				parameters.put("firmaN3", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F10.getValue());
				parameters.put("firmaP4", firma.getPuesto().getPuesto());
				parameters.put("firmaN4", firma.getNombre());
				break;
			case "4":
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F07.getValue());
				parameters.put("firmaP1", firma.getPuesto().getPuesto());
				parameters.put("firmaN1", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F08.getValue());
				parameters.put("firmaP2", firma.getPuesto().getPuesto());
				parameters.put("firmaN2", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F09.getValue());
				parameters.put("firmaP3", firma.getPuesto().getPuesto());
				parameters.put("firmaN3", firma.getNombre());
				firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
						ConstantsClaveEnnum.CLAVE_F10.getValue());
				parameters.put("firmaP4", firma.getPuesto().getPuesto());
				parameters.put("firmaN4", firma.getNombre());
				break;
			}
		} else {
			parameters.put("firmas", numeroFirmas);
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
					ConstantsClaveEnnum.CLAVE_F07.getValue());
			parameters.put("firmaP1", firma.getPuesto().getPuesto());
			parameters.put("firmaN1", firma.getNombre());
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
					ConstantsClaveEnnum.CLAVE_F08.getValue());
			parameters.put("firmaP2", firma.getPuesto().getPuesto());
			parameters.put("firmaN2", firma.getNombre());
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
					ConstantsClaveEnnum.CLAVE_F09.getValue());
			parameters.put("firmaP3", firma.getPuesto().getPuesto());
			parameters.put("firmaN3", firma.getNombre());
			firma = this.puestosFirmasService.getFirmaBySectorAnioClave(idSector, 0L,
					ConstantsClaveEnnum.CLAVE_F10.getValue());
			parameters.put("firmaP4", firma.getPuesto().getPuesto());
			parameters.put("firmaN4", firma.getNombre());
		}
		return parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gem.sistema.web.bean.BaseDirectReport#generaReporteSimple(int)
	 */
	@Override
	public StreamedContent generaReporteSimple(int type) throws ReportValidationException {

		return null;
	}

	/** The out. */
	Map<String, Object> out;

	/** The stream. */
	InputStream stream = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gem.sistema.web.bean.BaseDirectReport#getFileTxt()
	 */
	public StreamedContent getFileTxt() {
		MapSqlParameterSource parameter = new MapSqlParameterSource();
		listquery = generateQuerys(this.getUserDetails().getIdSector(), fideicomiso);

		parameter.addValue("i_mes", getMesSelected());
		parameter.addValue("i_idsector", this.getUserDetails().getIdSector());
		parameter.addValue("i_queryIng", listquery.get(1));
		parameter.addValue("i_queryEgr", listquery.get(0));
		parameter.addValue("i_municipio", this.getUserDetails().getMunicipio());
		parameter.addValue("i_firmas", numeroFirmas);

		out = executePlService.callProcedure(PROCEDURE_NAME, parameter);

		if (Integer.valueOf(out.get("O_COD_ERROR").toString()) > 0) {
			try {
				stream = new FileInputStream(
						new File(out.get("O_RUTA_FILE").toString() + "/" + out.get("O_NAME_FILE").toString()));
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			}
			fileTxt = new DefaultStreamedContent(stream, "application/txt", out.get("O_NAME_FILE").toString());
			generateNotificationFront(FacesMessage.SEVERITY_INFO, "Info!", out.get("O_MSG_ERROR").toString());
		} else {
			generateNotificationFront(FacesMessage.SEVERITY_ERROR, "Error!", out.get("O_MSG_ERROR").toString());
		}
		return fileTxt;
	}

	/**
	 * Sets the file txt.
	 *
	 * @param fileTxt the new file txt
	 */
	public void setFileTxt(StreamedContent fileTxt) {
		this.fileTxt = fileTxt;
	}

	public String getFideicomiso() {
		return fideicomiso;
	}

	public void setFideicomiso(String fideicomiso) {
		this.fideicomiso = fideicomiso;
	}

	public Integer getNumeroFirmas() {
		return numeroFirmas;
	}

	public void setNumeroFirmas(Integer numeroFirmas) {
		this.numeroFirmas = numeroFirmas;
	}

	public PuestosFirmasService getPuestosFirmasService() {
		return puestosFirmasService;
	}

	public void setPuestosFirmasService(PuestosFirmasService puestosFirmasService) {
		this.puestosFirmasService = puestosFirmasService;
	}

	/**
	 * Gets the listquery.
	 *
	 * @return the listquery
	 */
	public List<String> getListquery() {
		return listquery;
	}

	/**
	 * Sets the listquery.
	 *
	 * @param listquery the new listquery
	 */
	public void setListquery(List<String> listquery) {
		this.listquery = listquery;
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
	 * Gets the execute pl service.
	 *
	 * @return the execute pl service
	 */
	public ExecutePlService getExecutePlService() {
		return executePlService;
	}

	/**
	 * Sets the execute pl service.
	 *
	 * @param executePlService the new execute pl service
	 */
	public void setExecutePlService(ExecutePlService executePlService) {
		this.executePlService = executePlService;
	}
}
