package com.gem.sistema.web.bean;

import static com.roonin.utils.UtilDate.getLastDayByAnoEmp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.StreamedContent;
import com.gem.sistema.business.domain.Conctb;
import com.gem.sistema.business.repository.catalogs.ConctbRepository;
import com.gem.sistema.business.repository.catalogs.PolideRepository;
import com.gem.sistema.business.repository.catalogs.TcPeriodoRepositoy;
import com.gem.sistema.business.service.reportador.ReportValidationException;

@ManagedBean(name = "rEgresosDetalladosMB")
@ViewScoped
public class REgresosDetalladosMB extends BaseDirectReport {
	private static final String[] NAMES = { "AUTO_ENERO", "AUTO_FEBRERO", "AUTO_MARZO", "AUTO_ABRIL", "AUTO_MAYO",
			"AUTO_JUNIO", "AUTO_JULIO", "AUTO_AGOSTO", "AUTO_SEPTIEMBRE", "AUTO_OCTUBRE", "AUTO_NOVIEMBRE",
			"AUTO_DICIEMBRE" };
	private Integer tipo = 1;
	private Integer mesFinal;
	private Integer partida;
	private Boolean partidaB;

	private Conctb conctb = null;
	private List<Integer> listMes;

	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository conctbRepository;

	@ManagedProperty(value = "#{polideRepository}")
	private PolideRepository polideRepository;

	@ManagedProperty("#{tcPeriodoRepositoy}")
	private TcPeriodoRepositoy tcPeriodoRepositoy;

	@PostConstruct
	public void init() {
		jasperReporteName = "egresosDetallados";
		endFilename = jasperReporteName + ".pdf";

		conctb = conctbRepository.findByIdsector(this.getUserDetails().getIdSector());
		listMes = new ArrayList<Integer>();
		for (int i = 1; i <= 12; i++)
			listMes.add(i);
		this.tipo = 1;
		mesFinal = listMes.get(0);
	}

	public Map<String, Object> getParametersReports() {
		Integer idSector = this.getUserDetails().getIdSector();
		Conctb conctb = conctbRepository.findByIdsector(idSector);
		Map<String, Object> paramsReport = new java.util.HashMap<String, Object>();
		paramsReport.put("year", conctb.getAnoemp());
		paramsReport.put("nameMonthF",
				tcPeriodoRepositoy.findByTipoPeriodoAndPeriodo(1, mesFinal).getDescripcion().toUpperCase());
		paramsReport.put("lastDayMonthF", getLastDayByAnoEmp(mesFinal, conctb.getAnoemp()));
		paramsReport.put("imagePath", conctb.getImagePathLeft());
		paramsReport.put("imagePath2", conctb.getImagePathRigth());
		paramsReport.put("idSector", idSector);
		paramsReport.put("entidadName", conctb.getNomDep());
		paramsReport.put("query", getQuery());
		return paramsReport;
	}

	public String getQuery() {
		String auto = "";
		String partida = "";
		if (tipo == 1) {
			for (int i = 0; i <= mesFinal - 1; i++) {
				auto += NAMES[i] + "+";
			}
			auto = auto.substring(0, auto.length() - 1);
		} else if (tipo == 2) {
			auto = NAMES[mesFinal - 1];
		}
		if (partidaB) {
			partida = "AND N.CLVGAS=" + this.partida;
		}

		StringBuilder query = new StringBuilder();
		query.append(" SELECT  D.CLVDEP||M.CAMPO7 GRUPO, D.CLVDEP,D.NOMDEP, M.CAMPO7,M.CAMPO6,N.CLVGAS,N.NOMGAS,")
				.append(auto + "  PROGRAMADO ")
				.append(" FROM TR_PRESUPUESTO_DETALLADO E, CATDEP D, NATGAS N, MUNINEP M")
				.append(" WHERE E.ID_DEPENDENCIA=D.ID AND E.ID_PROYECTO=M.ID AND E.ID_PARTIDA=N.ID ")
				.append(" " + partida + " ORDER BY 1,3,5");
		return query.toString();
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public Integer getPartida() {
		return partida;
	}

	public void setPartida(Integer partida) {
		this.partida = partida;
	}

	public Integer getMesFinal() {
		return mesFinal;
	}

	public void setMesFinal(Integer mesFinal) {
		this.mesFinal = mesFinal;
	}

	public Conctb getConctb() {
		return conctb;
	}

	public void setConctb(Conctb conctb) {
		this.conctb = conctb;
	}

	public List<Integer> getListMes() {
		return listMes;
	}

	public void setListMes(List<Integer> listMes) {
		this.listMes = listMes;
	}

	public ConctbRepository getConctbRepository() {
		return conctbRepository;
	}

	public void setConctbRepository(ConctbRepository conctbRepository) {
		this.conctbRepository = conctbRepository;
	}

	public PolideRepository getPolideRepository() {
		return polideRepository;
	}

	public void setPolideRepository(PolideRepository polideRepository) {
		this.polideRepository = polideRepository;
	}

	public TcPeriodoRepositoy getTcPeriodoRepositoy() {
		return tcPeriodoRepositoy;
	}

	public void setTcPeriodoRepositoy(TcPeriodoRepositoy tcPeriodoRepositoy) {
		this.tcPeriodoRepositoy = tcPeriodoRepositoy;
	}

	@Override
	public StreamedContent generaReporteSimple(int type) throws ReportValidationException {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean getPartidaB() {
		return partidaB;
	}

	public void setPartidaB(Boolean partidaB) {
		this.partidaB = partidaB;
	}

}
