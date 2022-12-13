package com.gem.sistema.web.bean;

import static com.gem.sistema.util.UtilFront.generateNotificationFront;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.gem.sistema.business.domain.Conctb;
import com.gem.sistema.business.domain.TrPuestoFirma;
import com.gem.sistema.business.repository.catalogs.ConctbRepository;
import com.gem.sistema.business.repository.catalogs.FirmasRepository;
import com.gem.sistema.business.repository.catalogs.TcPeriodoRepositoy;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.gem.sistema.business.service.callsp.ExecutePlService;
import com.gem.sistema.business.service.catalogos.PuestosFirmasService;
import com.gem.sistema.business.service.catalogos.ValidatePolicyService;
import com.gem.sistema.util.Constants;
import com.gem.sistema.util.ConstantsClaveEnnum;
import com.gem.sistema.web.security.model.GemUser;

// TODO: Auto-generated Javadoc
/**
 * The Class RF009115MB.
 */
@ManagedBean
@ViewScoped
public class RF009115MB extends BaseDirectReport {
	
	/** The Constant PROCEDURE_NAME. */
	private static final String PROCEDURE_NAME = "SP_GENERA_TXT_ESTADO_INGRESOSD";
	
	/** The mes. */
	private Integer mes;
	
	/** The firmas. */
	private Integer firmas;
	
	/** The txt. */
	private StreamedContent txt;
	
	/** The firmas repository. */
	@ManagedProperty("#{firmasRepository}")
	private FirmasRepository firmasRepository;

	/** The execute pl service. */
	@ManagedProperty("#{executePlService}")
    protected ExecutePlService executePlService;
	
	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository conctbRepository;

	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;

	/** The out. */
	Map<String, Object> out;
	
	/** The stream. */
	InputStream stream = null;
	
	/**
	 * Gets the txt.
	 *
	 * @return the txt
	 */
	
	public StreamedContent getTxt() {
MapSqlParameterSource parameter = new MapSqlParameterSource();
		Integer idSector = this.getUserDetails().getIdSector();
		Conctb conctb = conctbRepository.findByIdsector(idSector);
		parameter.addValue("i_usuario", this.getUserDetails().getUsername());
		parameter.addValue("i_query", this.generateQuery(this.getUserDetails().getIdSector(), mes));
		parameter.addValue("i_mes", mes);
		parameter.addValue("i_firmas", firmas);
		parameter.addValue("i_municipio", conctb.getNomDep());
		
		out = executePlService.callProcedure(PROCEDURE_NAME, parameter);
		
		if(Integer.valueOf(out.get("O_COD_ERROR").toString())>0){
            try {
                stream = new FileInputStream(
                  new File(out.get("O_RUTA_FILE").toString() + "/" + out.get("O_NAME_FILE").toString()));
               } catch (FileNotFoundException e) {
                
                e.printStackTrace();
               }
            txt = new DefaultStreamedContent(stream, "application/txt", out.get("O_NAME_FILE").toString());
               generateNotificationFront(FacesMessage.SEVERITY_INFO, "Info!", out.get("O_MSG_ERROR").toString());
        }else{
        	generateNotificationFront(FacesMessage.SEVERITY_ERROR, "Error!", out.get("O_MSG_ERROR").toString());
        }
		return txt;
	}

	/**
	 * Sets the txt.
	 *
	 * @param txt the new txt
	 */
	public void setTxt(StreamedContent txt) {
		this.txt = txt;
	}

	/**
	 * Gets the tcfirmas.
	 *
	 * @return the tcfirmas
	 */

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

	/**
	 * Sets the tcfirmas.
	 *
	 * @param tcfirmas the new tcfirmas
	 */
	

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
		LOGGER.info(":: En postconsruct RF009115MB ");		
		jasperReporteName = "RF009_1_15";
		endFilename = jasperReporteName+".pdf";
	}

	/* (non-Javadoc)
	 * @see com.gem.sistema.web.bean.BaseDirectReport#getParametersReports()
	 */
	public Map<String, Object> getParametersReports() {
		Integer idSector = this.getUserDetails().getIdSector();
		Map<String, Object> paramsReport = new java.util.HashMap<String, Object>();
		Conctb conctb = conctbRepository.findByIdsector(idSector);
		GemUser user = this.getUserDetails();
		TrPuestoFirma firma = new TrPuestoFirma();
		
		paramsReport.put("idSector", idSector); 
		paramsReport.put("year", conctb.getAnoemp());
		paramsReport.put("mes", mes); 
		paramsReport.put("NFIRMAS", firmas); 
		paramsReport.put("entidadName", conctb.getNomDep());
		paramsReport.put("entidadRfc", conctb.getRfc());
		paramsReport.put("imagePath1", conctb.getImagePathLeft());
		paramsReport.put("imagePath2", conctb.getImagePathRigth());
		paramsReport.put("sql", this.generateQuery(user.getIdSector(), mes));
		if(idSector == 2){
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
		}else{
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
		
		return paramsReport;
	}	
	
	/**
	 * Generate query.
	 *
	 * @param idSector the id sector
	 * @param mes the mes
	 * @return the string
	 */
	public String generateQuery(Integer idSector, Integer mes) {
		StringBuilder cargo10 = new StringBuilder();
		StringBuilder abono10 = new StringBuilder();
		StringBuilder cargo50 = new StringBuilder();
		StringBuilder abono50 = new StringBuilder();
		StringBuilder ingreso = new StringBuilder();
		StringBuilder query = new StringBuilder();
		Integer i = 1;

		while (i <= mes) {
			cargo10.append(" NVL(DI.CARGOS_" + i + ",0) + ");
			abono10.append(" NVL(DI.ABONOS_" + i + ",0) + ");
			cargo50.append(" NVL(CI.CARGOS_" + i + ",0) + ");
			abono50.append(" NVL(CI.ABONOS_" + i + ",0) + ");
			ingreso.append(" NVL(II.AUTOI_" + i + ",0) + ");
			i++;
		}
		query.append("SELECT DISTINCT CUENTA, SCTA, SSCTA, SSSCTA, SSSSCTA, NOMCTA, ESTIMADA, MODIFICADO_MES, RECAUDADO_MES, ")
				.append("MODIFICADO_ACUMULADO, RECAUDADO_ACUMULADO, (RECAUDADO_ACUMULADO - MODIFICADO_ACUMULADO) VARABS, ")
				.append("CASE WHEN MODIFICADO_ACUMULADO = 0 THEN 0 ")
				.append("ELSE ABS(((RECAUDADO_ACUMULADO - MODIFICADO_ACUMULADO) / MODIFICADO_ACUMULADO) * 100) END ABSOLUTO ")
				.append("FROM (SELECT DI.CUENTA, DI.SCTA, DI.SSCTA, DI.SSSCTA, DI.SSSSCTA, DI.NOMCTA, ")
				.append("(NVL(DI.SALINI,0) + ((" + abono10.substring(0, abono10.length() - 2) + ") - ("
						+ cargo10.substring(0, cargo10.length() - 2) + "))) ESTIMADA, ")
				.append("NVL(II.AUTOI_" + mes + ",0) MODIFICADO_MES, ")
				.append("(NVL(CI.ABONOS_" + mes + ",0) - NVL(CI.CARGOS_" + mes + ",0)) RECAUDADO_MES, ")
				.append("(" + ingreso.substring(0, ingreso.length() - 2) + ") MODIFICADO_ACUMULADO, ")
				.append("((" + abono50.substring(0, abono50.length() - 2) + ") - ("
						+ cargo50.substring(0, cargo50.length() - 2) + ")) RECAUDADO_ACUMULADO")
				.append(" FROM CUENTA AS DI ")
				.append("INNER JOIN CUENTA AS CI ON CI.CUENTA= '8150' AND CI.SCTA=DI.SCTA AND CI.SSCTA=DI.SSCTA ")
				.append("AND CI.SSSCTA=DI.SSSCTA AND CI.SSSSCTA=DI.SSSSCTA AND CI.IDSECTOR = DI.IDSECTOR ")
				.append("LEFT JOIN INGRESO AS II ON DI.CUENTA = II.CUENTA AND DI.SCTA = II.SCTA AND DI.SSCTA=II.SSCTA ")
				.append("AND DI.SSSCTA = II.SSSCTA AND DI.SSSSCTA=II.SSSSCTA AND DI.IDSECTOR=II.IDSECTOR ")
				.append("WHERE (DI.CUENTA='8110') AND DI.SCTA <> '' AND DI.SSCTA <> '' AND DI.SSSCTA <> '' ")
				.append("AND DI.SSSSCTA <> '' AND DI.IDSECTOR=" + idSector)
				.append(" AND ( (NVL(DI.SALINI,0) + (( NVL(DI.ABONOS_1,0) ) - ( NVL(DI.CARGOS_1,0) ))) <> 0 OR ")
				.append("NVL(II.AUTOI_1,0) <> 0 OR ")
				.append("(NVL(CI.ABONOS_1,0) - NVL(CI.CARGOS_1,0)) <> 0 OR ")
				.append("(NVL(II.AUTOI_1,0) ) <> 0 OR ")
				.append("((NVL(CI.ABONOS_1,0) ) - ( NVL(CI.CARGOS_1,0) )) <> 0 )")
				.append(" ORDER BY DI.CUENTA,DI.SCTA, DI.SSCTA, DI.SSSCTA, DI.SSSSCTA ) T1");

		System.out.println(query.toString());
		return query.toString();
	}
	
	/* (non-Javadoc)
	 * @see com.gem.sistema.web.bean.BaseDirectReport#generaReporteSimple(int)
	 */
	public StreamedContent generaReporteSimple(int type) {
		return null;
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
	 * Gets the firmas.
	 *
	 * @return the firmas
	 */
	public Integer getFirmas() {
		return firmas;
	}

	/**
	 * Sets the firmas.
	 *
	 * @param firmas the new firmas
	 */
	public void setFirmas(Integer firmas) {
		this.firmas = firmas;
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
/*
 SELECT DISTINCT CUENTA, SCTA, SSCTA, SSSCTA, SSSSCTA, NOMCTA, ESTIMADA, MODIFICADO_MES, RECAUDADO_MES, 
 MODIFICADO_ACUMULADO, RECAUDADO_ACUMULADO, (RECAUDADO_ACUMULADO - MODIFICADO_ACUMULADO) VARABS, CASE 
 WHEN MODIFICADO_ACUMULADO = 0 THEN 0 ELSE ABS(((RECAUDADO_ACUMULADO - MODIFICADO_ACUMULADO) / MODIFICADO_ACUMULADO) * 100)
  END ABSOLUTO FROM (SELECT DI.CUENTA, DI.SCTA, DI.SSCTA, DI.SSSCTA, DI.SSSSCTA, DI.NOMCTA, (NVL(DI.SALINI,0) + 
  (( NVL(DI.ABONOS_1,0) ) - ( NVL(DI.CARGOS_1,0) ))) ESTIMADA, NVL(II.AUTOI_1,0) MODIFICADO_MES, (NVL(CI.ABONOS_1,0) - 
  NVL(CI.CARGOS_1,0)) RECAUDADO_MES, ( NVL(II.AUTOI_1,0) ) MODIFICADO_ACUMULADO, (( NVL(CI.ABONOS_1,0) ) - 
  ( NVL(CI.CARGOS_1,0) )) RECAUDADO_ACUMULADO FROM CUENTA AS DI INNER JOIN CUENTA AS CI ON CI.CUENTA= '8150' AND 
  CI.SCTA=DI.SCTA AND CI.SSCTA=DI.SSCTA AND CI.SSSCTA=DI.SSSCTA AND CI.SSSSCTA=DI.SSSSCTA AND CI.IDSECTOR = DI.IDSECTOR 
  LEFT JOIN INGRESO AS II ON DI.CUENTA = II.CUENTA AND DI.SCTA = II.SCTA AND DI.SSCTA=II.SSCTA AND DI.SSSCTA = II.SSSCTA AND 
  DI.SSSSCTA=II.SSSSCTA AND DI.IDSECTOR=II.IDSECTOR WHERE (DI.CUENTA='8110') AND DI.SCTA <> '' AND DI.SSCTA <> '' 
  AND DI.SSSCTA <> '' AND DI.SSSSCTA <> '' AND DI.IDSECTOR=2 ORDER BY DI.CUENTA,DI.SCTA, DI.SSCTA, DI.SSSCTA, DI.SSSSCTA ) T1

 * */
