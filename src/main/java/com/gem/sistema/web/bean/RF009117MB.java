package com.gem.sistema.web.bean;

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
import com.gem.sistema.business.service.catalogos.PuestosFirmasService;
import com.gem.sistema.util.ConstantsClaveEnnum;

// TODO: Auto-generated Javadoc
/**
 * The Class RF009117MB.
 */
@ManagedBean
@ViewScoped
public class RF009117MB extends BaseDirectReport {
	
	/** The mes. */
	private Integer mes;
	
	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository conctbRepository;

	@ManagedProperty("#{tcPeriodoRepositoy}")
	private TcPeriodoRepositoy periodoRepositoy;
	
	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;
	
	public ConctbRepository getConctbRepository() {
		return conctbRepository;
	}

	public void setConctbRepository(ConctbRepository conctbRepository) {
		this.conctbRepository = conctbRepository;
	}

	public TcPeriodoRepositoy getPeriodoRepositoy() {
		return periodoRepositoy;
	}

	public void setPeriodoRepositoy(TcPeriodoRepositoy periodoRepositoy) {
		this.periodoRepositoy = periodoRepositoy;
	}

	public PuestosFirmasService getPuestosFirmasService() {
		return puestosFirmasService;
	}

	public void setPuestosFirmasService(PuestosFirmasService puestosFirmasService) {
		this.puestosFirmasService = puestosFirmasService;
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		LOGGER.info(":: En postconsruct RF009117MB ");		
		jasperReporteName = "rf009_11_17";
		endFilename = jasperReporteName+".pdf";
	}
	
	/* (non-Javadoc)
	 * @see com.gem.sistema.web.bean.BaseDirectReport#getParametersReports()
	 */
	public Map<String, Object> getParametersReports() {
		Integer idSector = this.getUserDetails().getIdSector();
		Map<String, Object> paramsReport = new java.util.HashMap<String, Object>();
		Conctb conctb = conctbRepository.findByIdsector(idSector);
		TrPuestoFirma firma = new TrPuestoFirma();
		TcPeriodo periodo = periodoRepositoy.findByTipoPeriodoAndPeriodo(1, mes);
		
		paramsReport.put("mes", mes);
		paramsReport.put("mesName", periodo.getDescripcion().toUpperCase());
		paramsReport.put("year", conctb.getAnoemp());
		paramsReport.put("sQuery", generateQuery(mes, getUserDetails().getIdSector()));
		
		if (idSector == 2) {
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
		} else {
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
		paramsReport.put("entidadName", conctb.getNomDep());
		paramsReport.put("entidadRfc", conctb.getRfc());
		paramsReport.put("imagePath1", conctb.getImagePathLeft());
		paramsReport.put("imagePath2", conctb.getImagePathRigth());
		paramsReport.put("idSector", this.getUserDetails().getIdSector());
		
		return paramsReport;
	}	

	/* (non-Javadoc)
	 * @see com.gem.sistema.web.bean.BaseDirectReport#generaReporteSimple(int)
	 */
	public StreamedContent generaReporteSimple(int type) {
		return null;
		/*
		Map<String, Object> paramsQuery = new java.util.HashMap<String, Object>();
		paramsQuery.put("ID_REF", new Integer(0)); //FALTA
		return reporteadorDirectoImpl.getFileReport(tcReporte,paramsQuery, reporteName,type);
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

	/**
	 * Generate query.
	 *
	 * @param mes the mes
	 * @param sector the sector
	 * @return the string
	 */
	private static String generateQuery(Integer mes, Integer sector ){
		StringBuilder sSql = new StringBuilder();
		StringBuilder sToeje = new StringBuilder();
		StringBuilder sXampli = new StringBuilder();
		StringBuilder sXredu = new StringBuilder();
		StringBuilder sAuto = new StringBuilder();
		
		for(int i=1; i<=mes; i++){
			sXampli.append("PA.AMPLI1_"+i + " + ");
			sXredu.append("PA.REDU1_"+i + " + ");
			sToeje.append("PA.COMPRO1_" + i + " + PA.EJPA1_" + i + " + PA.EJXPA1_" + i + " + ");
			sAuto.append("PA.AUTO1_" + i + " + ");
		}
		
		sSql.append(" SELECT  CLVGAS || ' ' || NOMGAS CONCEPTO, PRESUPUESTO, " + 
				"		(AUTO_MES + AMPLI_MES - REDU_MES) MODIFICADO_MES, TOEJE_MES EJERCIDO_MES, " + 
				"		(AUTO_ACUM + AMPLI_ACUM - REDU_ACUM) MODIFICADO_ACUM, TOEJE_ACUM EJERCIDO_ACUM,\r\n" + 
				"		(AUTO_ACUM + AMPLI_ACUM - REDU_ACUM) - TOEJE_ACUM VAR_ABS,\r\n" + 
				" CASE WHEN (AUTO_ACUM + AMPLI_ACUM - REDU_ACUM) = 0 \r\n" + 
				" 				THEN 0 \r\n" + 
				" 			ELSE ((((AUTO_ACUM + AMPLI_ACUM - REDU_ACUM) - TOEJE_ACUM) * 100 ) / (AUTO_ACUM + AMPLI_ACUM - REDU_ACUM)) END POR " +
				" FROM (\r\n" + 
				"	SELECT	NA.CLVGAS, NA.NOMGAS, \r\n" + 
				"			SUM(PA.AUTO1_1 + PA.AUTO1_2 + PA.AUTO1_3 + PA.AUTO1_4 + PA.AUTO1_5 + PA.AUTO1_6 + PA.AUTO1_7 + PA.AUTO1_8 + PA.AUTO1_9 + PA.AUTO1_10 + PA.AUTO1_11 + PA.AUTO1_12) PRESUPUESTO,\r\n" + 
				"			SUM(PA.AUTO1_").append(mes).append(") AUTO_MES,\r\n" + 
				"			SUM(PA.AMPLI1_").append(mes).append(") AMPLI_MES,\r\n" + 
				"			SUM(PA.REDU1_").append(mes).append(") REDU_MES,\r\n" + 
				"			SUM(PA.COMPRO1_").append(mes).append(" + PA.EJPA1_").append(mes).append(" + PA.EJXPA1_").append(mes).append(") TOEJE_MES,\r\n" + 
				"			SUM(").append(sAuto.substring(0, sAuto.length()-2)).append(") AUTO_ACUM,\r\n" + 
				"			SUM(").append(sXampli.substring(0, sXampli.length()-2)).append(") AMPLI_ACUM,\r\n" + 
				"			SUM(").append(sXredu.substring(0, sXredu.length()-2)).append(") REDU_ACUM,\r\n" + 
				"			SUM(").append(sToeje.substring(0, sToeje.length()-2)).append(") TOEJE_ACUM\r\n" + 
				"		FROM PASO PA\r\n" + 
				"			INNER JOIN NATGAS NA ON NA.IDSECTOR = PA.IDSECTOR AND NA.CLVGAS = PA.PARTIDA\r\n" + 
				"	WHERE 	PA.IDSECTOR= " + sector +
				"		AND SUBSTR(PA.PARTIDA,2,3) = '000'\r\n" + 
				"	GROUP BY NA.CLVGAS, NA.NOMGAS\r\n" + 
				"	ORDER BY NA.CLVGAS\r\n" + 
				") T1");
		
		return sSql.toString();
	}

}
