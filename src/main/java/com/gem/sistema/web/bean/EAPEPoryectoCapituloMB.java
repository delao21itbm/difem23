package com.gem.sistema.web.bean;

import static com.roonin.utils.UtilDate.getLastDayByAnoEmp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.collections4.CollectionUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

import com.gem.sistema.business.domain.Conctb;
import com.gem.sistema.business.domain.TcMes;
import com.gem.sistema.business.domain.TrPuestoFirma;
import com.gem.sistema.business.repository.catalogs.ConctbRepository;
import com.gem.sistema.business.repository.catalogs.TcMesRepository;
import com.gem.sistema.business.repository.load.PasoRepository;
import com.gem.sistema.business.service.catalogos.PuestosFirmasService;
import com.gem.sistema.business.service.catalogos.ValidatePolicyService;
import com.gem.sistema.business.service.reportador.ReportValidationException;
import com.gem.sistema.util.ConstantsClaveEnnum;

/**
 * @author José Luis
 *
 */
@ManagedBean(name = "eAPEPoryectoCapituloMB")
@ViewScoped
public class EAPEPoryectoCapituloMB extends BaseDirectReport {
	private static final String DOWNLOAD_XLS = " jQuery('#form1\\\\:downXls').click();";
	private static final String TODOS = "TODOS";

	private String mes;
	private List<TcMes> listTcMes;
	private List<String> proyectos = new ArrayList<>();
	private String proyecto = TODOS;

	@ManagedProperty("#{tcMesRepository}")
	private TcMesRepository tcmesRepository;

	@ManagedProperty("#{validatePolicyService}")
	private ValidatePolicyService validatePolicyService;

	@ManagedProperty("#{puestosFirmasService}")
	private PuestosFirmasService puestosFirmasService;

	@ManagedProperty("#{pasoRepository}")
	private PasoRepository pasoRepository;

	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository conctbRepository;

	@PostConstruct
	public void init() {
		proyectos.add(TODOS);
		proyectos.addAll(pasoRepository.getProyectoByIdSector(this.getUserDetails().getIdSector()));

		jasperReporteName = "EAPEPoryectoCapitulo";
		endFilename = "EAPEPoryectoCapitulo.pdf";
		listTcMes = tcmesRepository.findAll();
		if (!CollectionUtils.isEmpty(listTcMes)) {
			mes = listTcMes.get(0).getMes();
		}
	}

	@Override
	public Map<String, Object> getParametersReports() throws ReportValidationException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		Integer sector = this.getUserDetails().getIdSector();
		TrPuestoFirma firma = null;
		Conctb conctb = conctbRepository.findByIdsector(this.getUserDetails().getIdSector());
		parameters.put("p_IdSector", sector);
		parameters.put("p_Mes", Integer.valueOf(mes));
		parameters.put("p_Mun", conctb.getNomDep());
		firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, ConstantsClaveEnnum.CLAVE_F07.getValue());
		parameters.put("p_L1", firma.getPuesto().getPuesto());
		parameters.put("p_N1", firma.getNombre());
		firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, ConstantsClaveEnnum.CLAVE_F08.getValue());
		parameters.put("p_L2", firma.getPuesto().getPuesto());
		parameters.put("p_N2", firma.getNombre());
		firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, ConstantsClaveEnnum.CLAVE_F09.getValue());
		parameters.put("p_L3", firma.getPuesto().getPuesto());
		parameters.put("p_N3", firma.getNombre());
		firma = puestosFirmasService.getFirmaBySectorAnioClave(sector, 0L, ConstantsClaveEnnum.CLAVE_F10.getValue());
		parameters.put("p_L4", firma.getPuesto().getPuesto());
		parameters.put("p_N4", firma.getNombre());

		parameters.put("p_Img", conctb.getImagePathLeft());
		parameters.put("p_Img2", conctb.getImagePathRigth());
		parameters.put("p_Query", this.generateQuery1(sector, Integer.valueOf(mes)));
		parameters.put("p_An", conctb.getAnoemp());
		parameters.put("p_LastDay", getLastDayByAnoEmp(Integer.valueOf(mes), conctb.getAnoemp()));
		parameters.put("p_DescrpMes", tcmesRepository.findByMes(mes).getDescripcion());
		parameters.put("p_Dependencia", conctb.getNomDep());

		return parameters;
	}

	@Override
	public StreamedContent generaReporteSimple(int type) throws ReportValidationException {
		// TODO Auto-generated method stub
		return null;
	}

	public void generatePDF() {
		if (this.validatePolicyService.isOpenMonth(Integer.valueOf(mes), null,
				this.getUserDetails().getIdSector()) == Boolean.TRUE) {
			this.validatePolicyService.isAfectMonth(Integer.valueOf(mes), null, this.getUserDetails().getIdSector());
			this.validatePolicyService.existPolices(Integer.valueOf(mes), null, this.getUserDetails().getIdSector());
			this.createFilePdfInline();
		}
	}

	public void downloadXls() {
		if (this.validatePolicyService.isOpenMonth(Integer.valueOf(mes), null,
				this.getUserDetails().getIdSector()) == Boolean.TRUE) {
			this.validatePolicyService.isAfectMonth(Integer.valueOf(mes), null, this.getUserDetails().getIdSector());
			this.validatePolicyService.existPolices(Integer.valueOf(mes), null, this.getUserDetails().getIdSector());

			RequestContext.getCurrentInstance().execute(DOWNLOAD_XLS);
		}
	}

	public String generateQuery1(Integer idsector, Integer mes) {
		StringBuilder sSql = new StringBuilder();
		StringBuilder ejempa = new StringBuilder();
		StringBuilder ejxpa = new StringBuilder();
		StringBuilder toeje = new StringBuilder();
		StringBuilder ampli = new StringBuilder();
		StringBuilder redu = new StringBuilder();
		StringBuilder compro = new StringBuilder();
		sSql.append("SELECT T1.TIPO, T1.DESC_TIPO, T1.PARTIDA, T1.DESCRIPCION, T1.A APROBADO, ")
				.append(" T1.F AMPLIACION, T1.G REDUCCION, (T1.A+T1.F-T1.G) MODIFICADO, ")
				.append(" (T1.CPT)COMPROMETIDO, T1.C DEVENGADO, T1.B PAGADO, ")
				.append(" T1.D EJERCIDO, ((T1.A +T1.F-T1.G)-D) POR_EJERCER FROM ( ").append(" SELECT ")
				.append("SUBSTR(P.PROGRAMA,1,12 ) ").append(" TIPO,").append("M.CAMPO6 DESC_TIPO,")
				.append(" P.PARTIDA PARTIDA,  N.NOMGAS DESCRIPCION, ")
				.append("SUM(P.AUTO1_1+P.AUTO1_2+P.AUTO1_3+P.AUTO1_4+P.AUTO1_5+P.AUTO1_6+P.AUTO1_7+P.AUTO1_8+P.AUTO1_9+P.AUTO1_10+P.AUTO1_11+P.AUTO1_12)A,");

		for (int i = 1; i <= mes; i++) {

			ejempa.append(" P.EJPA1_" + i + "+");
			ejxpa.append(" P.EJXPA1_" + i + "+");
			toeje.append(" P.TOEJE1_" + i + "+");
			ampli.append(" P.AMPLI1_" + i + "+");
			redu.append(" P.REDU1_" + i + "+");
			compro.append(" P.COMPRO1_" + i + "+");

		}

		sSql.append(" SUM( ").append(ejempa.substring(1, ejempa.length() - 1)).append(")B,").append(" SUM( ")
				.append(ejxpa.substring(1, ejxpa.length() - 1)).append(")C,").append(" SUM( ")
				.append(toeje.substring(1, toeje.length() - 1)).append(")D,").append(" SUM( ")
				.append(ampli.substring(1, ampli.length() - 1)).append(")F,").append(" SUM( ")
				.append(redu.substring(1, redu.length() - 1)).append(")G,").append(" SUM( ")
				.append(compro.substring(1, compro.length() - 1)).append(")CPT ");

		sSql.append(" FROM PASO P JOIN NATGAS N ON N.CLVGAS= P.PARTIDA AND N.IDSECTOR=P.IDSECTOR  ")
				.append(" JOIN MUNINEP M ON SUBSTR(P.PROGRAMA,1,12)=M.CAMPO7 AND M.IDSECTOR=P.IDSECTOR ")
				.append("WHERE  P.IDSECTOR = " + idsector)
				.append(proyecto.equals(TODOS) ? "" : "	AND SUBSTR(P.PROGRAMA,1,12 )='" + proyecto + "'")
				.append(" AND SUBSTR(P.PARTIDA,2,1) IN ('0') GROUP BY ").append("SUBSTR(P.PROGRAMA,1,12 ) ,M.CAMPO6 ,")
				.append(" P.PARTIDA,N.NOMGAS  ORDER BY 1 ) T1 WHERE A+B+C+D+F+G+CPT<>0 ");
		return sSql.toString();
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public List<TcMes> getListTcMes() {
		return listTcMes;
	}

	public void setListTcMes(List<TcMes> listTcMes) {
		this.listTcMes = listTcMes;
	}

	public TcMesRepository getTcmesRepository() {
		return tcmesRepository;
	}

	public void setTcmesRepository(TcMesRepository tcmesRepository) {
		this.tcmesRepository = tcmesRepository;
	}

	public ValidatePolicyService getValidatePolicyService() {
		return validatePolicyService;
	}

	public void setValidatePolicyService(ValidatePolicyService validatePolicyService) {
		this.validatePolicyService = validatePolicyService;
	}

	public PuestosFirmasService getPuestosFirmasService() {
		return puestosFirmasService;
	}

	public void setPuestosFirmasService(PuestosFirmasService puestosFirmasService) {
		this.puestosFirmasService = puestosFirmasService;
	}

	public ConctbRepository getConctbRepository() {
		return conctbRepository;
	}

	public void setConctbRepository(ConctbRepository conctbRepository) {
		this.conctbRepository = conctbRepository;
	}

	public List<String> getProyectos() {
		return proyectos;
	}

	public void setProyectos(List<String> proyectos) {
		this.proyectos = proyectos;
	}

	public String getProyecto() {
		return proyecto;
	}

	public void setProyecto(String proyecto) {
		this.proyecto = proyecto;
	}

	public PasoRepository getPasoRepository() {
		return pasoRepository;
	}

	public void setPasoRepository(PasoRepository pasoRepository) {
		this.pasoRepository = pasoRepository;
	}

}
