package com.gem.sistema.web.bean;

import static com.roonin.utils.UtilDate.getLastDayByAnoEmp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.collections4.CollectionUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import com.gem.sistema.business.domain.Conctb;
import com.gem.sistema.business.dto.ComparadorDTO;
import com.gem.sistema.business.repository.catalogs.ConctbRepository;
import com.gem.sistema.business.repository.catalogs.TcPeriodoRepositoy;
import com.gem.sistema.business.service.catalogos.ComparadorService;
import com.gem.sistema.business.service.reportador.ReportValidationException;
import com.gem.sistema.util.CopyFileUtil;
import com.gem.sistema.util.UtilFront;

/**
 * @author Mateo Fecha Modificacion Autor Descripcion Version ------------------
 *         ----------------------------- ---------------------------------
 *         -------- 24/03/2021 Julio Cesar de la O Espinoza Se crea MB para la
 *         comparación de 1.0 presupuesto
 * 
 *         29/03/2021 Javier Tenorio	Se agrega reporte xls, ajuste de mensajes de notificación		1.1
 *         								y se crea metodo para mandar parametros al Jasper									
 *
 */
@ManagedBean(name = "comparadorMB")
@ViewScoped
public class ComparadorMB extends BaseDirectReport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String PATH_FILE = "\\gem\\upfiles\\";

	InputStream inputStrem = null;

	String fileName;

	private List<Integer> listMes;
	private Integer mes;

	@ManagedProperty("#{conctbRepository}")
	private ConctbRepository conctbRepository;

	@ManagedProperty("#{tcPeriodoRepositoy}")
	private TcPeriodoRepositoy tcPeriodoRepositoy;

	public ConctbRepository getConctbRepository() {
		return conctbRepository;
	}

	public void setConctbRepository(ConctbRepository conctbRepository) {
		this.conctbRepository = conctbRepository;
	}

	public TcPeriodoRepositoy getTcPeriodoRepositoy() {
		return tcPeriodoRepositoy;
	}

	public void setTcPeriodoRepositoy(TcPeriodoRepositoy tcPeriodoRepositoy) {
		this.tcPeriodoRepositoy = tcPeriodoRepositoy;
	}

	@ManagedProperty("#{comparadorService}")
	private ComparadorService comparadorService;

	private List<ComparadorDTO> listComparador = new ArrayList<ComparadorDTO>();

	@PostConstruct
	public void init() {
		/*
		 * JTL29032021
		 */
		LOGGER.info(":: En postconsruct rEDOSITFINMB ");
		jasperReporteName = "comparadorPresupuesto";
		endFilename = jasperReporteName + ".xls";
		/*
		 * JTL29032021
		 */
		listMes = new ArrayList<Integer>();

		for (int i = 1; i <= 12; i++) {
			listMes.add(i);
		}
		mes = listMes.get(0);

	}

	public void handleFileUpload(FileUploadEvent event) {
		fileName = UUID.randomUUID() + event.getFile().getFileName();

		try {
			String path = CopyFileUtil.copyFile(fileName, event.getFile().getInputstream(), PATH_FILE);

			setListComparador(this.comparadorService.comparar(mes, path));
			/*
			 * JTL29032021
			 */
			if (CollectionUtils.isNotEmpty(listComparador))
				UtilFront.generateNotificationFront(FacesMessage.SEVERITY_INFO,
						"¡Los datos se cargaron correctamente, favor de revisar el resultado!", "");

		} catch (IOException e) {
			UtilFront.generateNotificationFront(FacesMessage.SEVERITY_INFO, "", "Error al cargar el archivo");
		}
		/*
		 * JTL29032021
		 */
	}
	/*
	 * JTL29032021
	 */
	public Map<String, Object> getParametersReports() {
		Integer idSector = this.getUserDetails().getIdSector();
		Conctb conctb = conctbRepository.findByIdsector(idSector);
		Map<String, Object> paramsReport = new java.util.HashMap<String, Object>();

		paramsReport.put("pAnio", conctb.getAnoemp());
		paramsReport.put("pDia", getLastDayByAnoEmp(mes, conctb.getAnoemp()));
		paramsReport.put("USUARIO", getUserDetails().getUsername());
		paramsReport.put("img1", conctb.getImagePathLeft());
		paramsReport.put("img2", conctb.getImagePathRigth());
		paramsReport.put("mes", mes);
		paramsReport.put("pMesLetra",
				tcPeriodoRepositoy.findByTipoPeriodoAndPeriodo(1, mes).getDescripcion().toUpperCase());
		paramsReport.put("idSector", idSector);
		paramsReport.put("sql", this.comparadorService.generaQuery(mes));

		paramsReport.put("nameDep", conctb.getNomDep());

		return paramsReport;
	}
	/*
	 * JTL29032021
	 */

	public List<Integer> getListMes() {
		return listMes;
	}

	public void setListMes(List<Integer> listMes) {
		this.listMes = listMes;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public ComparadorService getComparadorService() {
		return comparadorService;
	}

	public void setComparadorService(ComparadorService comparadorService) {
		this.comparadorService = comparadorService;
	}

	public static String getPathFile() {
		return PATH_FILE;
	}

	public List<ComparadorDTO> getListComparador() {
		return listComparador;
	}

	public void setListComparador(List<ComparadorDTO> listComparador) {
		this.listComparador = listComparador;
	}

	@Override
	public StreamedContent generaReporteSimple(int type) throws ReportValidationException {
		// TODO Auto-generated method stub
		return null;
	}

}
