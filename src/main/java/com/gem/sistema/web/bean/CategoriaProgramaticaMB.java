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
import com.gem.sistema.business.service.catalogos.PuestosFirmasService;
import com.gem.sistema.business.service.reportador.ReportValidationException;
import com.gem.sistema.util.ConstantsClaveEnnum;

@ManagedBean(name = "categoriaProgramaticaMB")
@ViewScoped
public class CategoriaProgramaticaMB extends BaseDirectReport {

	private Integer noDecimales;
	private Integer pesos;
	Conctb conctb = null;
	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository conctbRepository;

	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		noDecimales = 2;
		pesos = 1;
		conctb = conctbRepository.findByIdsector(this.getUserDetails().getIdSector());
		jasperReporteName = "categoriaProgramatica";
		endFilename = "categoriaProgramatica.pdf";

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gem.sistema.web.bean.BaseDirectReport#getParametersReports()
	 */
	@Override
	public Map<String, Object> getParametersReports() throws ReportValidationException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		TrPuestoFirma firma = new TrPuestoFirma();
		Integer sector = this.getUserDetails().getIdSector();
		String fecha = "31 DE DICIEMBRE DE " + conctb.getAnoemp();

		parameters.put("decimalFormat", "%,." + noDecimales + "f");
		parameters.put("pNomMun", conctb.getNomDep());
		parameters.put("fecha", fecha);
		parameters.put("pImagen1", conctb.getImagePathLeft());
		parameters.put("sector", sector);
		parameters.put("pPesos", pesos);
		parameters.put("anio", conctb.getAnoemp());
		firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, ConstantsClaveEnnum.CLAVE_F07.getValue());
		parameters.put("pL1", firma.getPuesto().getPuesto());
		parameters.put("pN1", firma.getNombre());
		firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, ConstantsClaveEnnum.CLAVE_F08.getValue());
		parameters.put("pL2", firma.getPuesto().getPuesto());
		parameters.put("pN2", firma.getNombre());
		firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, ConstantsClaveEnnum.CLAVE_F09.getValue());
		parameters.put("pL3", firma.getPuesto().getPuesto());
		parameters.put("pN3", firma.getNombre());
		parameters.put("query", this.getQuery(sector));
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

	private String getQuery(Integer sector) {
		StringBuilder query = new StringBuilder();
		StringBuilder miles = new StringBuilder();
		query.append(" SELECT ").append(" 	SUBSTR(T1.CAMPO7,1,8)GRUPO,").append(" 	T1.CAMPO7,").append(" 	T1.CAMPO6,")
				.append(" 	T1.APROBADO, ").append(" 	(T1.AMPLIACIONES -  T1.REDUCCIONES) APMLIREDU, ")
				.append(" 	(T1.APROBADO - T1.AMPLIACIONES) MODIFICADO, ").append(" 	T1.COMPROMETIDO, ")
				.append(" 	T1.DEVENGADO,").append(" 	T1.PAGADO, ").append(" 	T1.EJERCIDO,  ")
				.append(" 	((T1.APROBADO - T1.AMPLIACIONES) - T1.EJERCIDO) SUBEJERCICIO ").append(" FROM (")
				.append(" 	SELECT NA.CAMPO7,NA.CAMPO6,NA.IDSECTOR,")
				.append(" 		SUM(PA.AUTO1_1 + PA.AUTO1_2 + PA.AUTO1_3 + PA.AUTO1_4 + PA.AUTO1_5 + PA.AUTO1_6 + ")
				.append(" 			PA.AUTO1_7 + PA.AUTO1_8 + PA.AUTO1_9 + PA.AUTO1_10 + PA.AUTO1_11 +PA.AUTO1_12) APROBADO,")
				.append(" 		SUM(PA.AMPLI1_1 + PA.AMPLI1_2 + PA.AMPLI1_3 + PA.AMPLI1_4 + PA.AMPLI1_5 +PA.AMPLI1_6 + ")
				.append(" 			PA.AMPLI1_7 + PA.AMPLI1_8 + PA.AMPLI1_9 + PA.AMPLI1_10 +PA.AMPLI1_11 + PA.AMPLI1_12 ) AMPLIACIONES,")
				.append(" 		SUM(PA.REDU1_1 + PA.REDU1_2 + PA.REDU1_3 + PA.REDU1_4 + PA.REDU1_5 + PA.REDU1_6 + ")
				.append(" 			PA.REDU1_7 + PA.REDU1_8 + PA.REDU1_9 + PA.REDU1_10 + PA.REDU1_11 + PA.REDU1_12 ) REDUCCIONES,")
				.append("        	SUM(PA.COMPRO1_1 + PA.COMPRO1_2 + PA.COMPRO1_3 + PA.COMPRO1_4 + PA.COMPRO1_5 + PA.COMPRO1_6 + ")
				.append("        		PA.COMPRO1_7 + PA.COMPRO1_8 + PA.COMPRO1_9 + PA.COMPRO1_10 + PA.COMPRO1_11 + PA.COMPRO1_12 ) COMPROMETIDO,")
				.append(" 		SUM(PA.EJPA1_1 + PA.EJPA1_2 + PA.EJPA1_3 + PA.EJPA1_4 + PA.EJPA1_5 +PA.EJPA1_6 + ")
				.append(" 			PA.EJPA1_7 + PA.EJPA1_8 + PA.EJPA1_9 + PA.EJPA1_10 + PA.EJPA1_11 +PA.EJPA1_12 ) DEVENGADO,		")
				.append(" 		SUM(PA.EJXPA1_1 + PA.EJXPA1_2 + PA.EJXPA1_3 + PA.EJXPA1_4 + PA.EJXPA1_5 + PA.EJXPA1_6 + ")
				.append(" 			PA.EJXPA1_7 + PA.EJXPA1_8 + PA.EJXPA1_9 + PA.EJXPA1_10 +PA.EJXPA1_11 + PA.EJXPA1_12 ) PAGADO,")
				.append(" 		SUM(PA.TOEJE1_1 + PA.TOEJE1_2 + PA.TOEJE1_3 +PA.TOEJE1_4 + PA.TOEJE1_5 + PA.TOEJE1_6 +")
				.append(" 			PA.TOEJE1_7 + PA.TOEJE1_8 + PA.TOEJE1_9 +PA.TOEJE1_10 + PA.TOEJE1_11 + PA.TOEJE1_12 ) EJERCIDO ")
				.append(" 	FROM PASO PA,MUNINEP NA,PP_METT M").append(" 	WHERE  PA.IDSECTOR = " + sector + "	 AND ")
				.append(" 		(SUBSTR(PA.PROGRAMA,1,8)=NA.CAMPO7 OR")
				.append(" 			SUBSTR(PA.PROGRAMA,1,12)=NA.CAMPO7) AND")
				.append(" 		PA.IDSECTOR=NA.IDSECTOR AND ").append(" 		M.CLVNEP=SUBSTR(PA.PROGRAMA,1,12)")
				.append(" 	GROUP BY NA.CAMPO7,NA.CAMPO6,NA.IDSECTOR").append(" 	ORDER BY 1 ASC ").append(" ) T1");
		if (pesos != 1) {
			miles.append("SELECT SUBSTR(T2.CAMPO7,1,8)GRUPO,	T2.CAMPO7, T2.CAMPO6, ")
					.append(" (T2.APROBADO/1000) APROBADO,  ").append(" (T2.APMLIREDU/1000) APMLIREDU,")
					.append(" (T2.MODIFICADO/1000) MODIFICADO, ").append(" (T2.COMPROMETIDO/1000) COMPROMETIDO, ")
					.append(" (T2.DEVENGADO/1000) DEVENGADO, ").append(" (T2.PAGADO/1000) PAGADO, ")
					.append(" (T2.EJERCIDO/1000) EJERCIDO,").append(" (T2.SUBEJERCICIO/1000) SUBEJERCICIO")
					.append(" FROM( ");
			query.insert(0, miles);
			query.append(" )T2");
		}

		return query.toString();

	}

	public ConctbRepository getConctbRepository() {
		return conctbRepository;
	}

	public void setConctbRepository(ConctbRepository conctbRepository) {
		this.conctbRepository = conctbRepository;
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

	public PuestosFirmasService getPuestosFirmasService() {
		return puestosFirmasService;
	}

	public void setPuestosFirmasService(PuestosFirmasService puestosFirmasService) {
		this.puestosFirmasService = puestosFirmasService;
	}

}
