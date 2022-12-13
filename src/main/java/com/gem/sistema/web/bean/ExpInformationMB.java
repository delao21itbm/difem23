package com.gem.sistema.web.bean;

import static com.gem.sistema.util.UtilFront.generateNotificationFront;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;

import com.gem.sistema.business.domain.TcPeriodo;
import com.gem.sistema.business.repository.catalogs.TcPeriodoRepositoy;
import com.gem.sistema.business.service.catalogos.ExportInformationService;

@ManagedBean(name = "expInformationMB")
@ViewScoped
public class ExpInformationMB extends AbstractMB {

	@ManagedProperty("#{exportInformationService}")
	private ExportInformationService exportInformationService;

	private static final String DOWNLOAD_TXT = " jQuery('#form1\\\\:downTxt').click();";
	private static final String DOWNLOAD_TXT2 = " jQuery('#form1\\\\:downTxt2').click();";
	private static final String DOWNLOAD_TXT3 = " jQuery('#form1\\\\:downTxt3').click();";

	private static final Integer MENSUAL = 1;

	@ManagedProperty("#{tcPeriodoRepositoy}")
	private TcPeriodoRepositoy tcPeriodoRepositoy;

	private StreamedContent file1;
	private StreamedContent file2;
	private StreamedContent file3;
	/** The stream. */
	InputStream stream1 = null;
	InputStream stream2 = null;
	InputStream stream3 = null;
	private Integer mes;

	private Integer idSector;
	private List<TcPeriodo> listPeriodo;

	private String pathName;

	@PostConstruct
	public void init() {

		listPeriodo = this.tcPeriodoRepositoy.findByTipoPeriodo(MENSUAL);

		mes = listPeriodo.get(0).getPeriodo();

		idSector = this.getUserDetails().getIdSector();

	}

	public void downloadCuenta() {
		File[] file = new File[4]; 
		file[0] = new File(this.exportInformationService.exportCuentas(idSector, mes));
		file[1] = new File( this.exportInformationService.exportPaso(idSector, mes));
		file[2] = new File( this.exportInformationService.exportPoliza(idSector, mes));
		file[3] = new File( this.exportInformationService.exportCuentaMes(idSector, mes));
		pathName = this.exportInformationService.zipFile(file, "/gem/upfiles/export.zip");
		try {
			stream1 = new FileInputStream(new File(pathName));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		file1 = new DefaultStreamedContent(stream1, "application/zip", pathName.substring(13));
		
		generateNotificationFront(FacesMessage.SEVERITY_INFO, "Info!", "Se Generaro el archivo zip: "
				+ pathName.substring(13) );

	}

	public void downloadCuentaTxt() {
		try {
			this.downloadCuenta();
			RequestContext.getCurrentInstance().execute(DOWNLOAD_TXT);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void downloadTxt2() {
		try {
			this.downloadCuenta();
			RequestContext.getCurrentInstance().execute(DOWNLOAD_TXT2);

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void downloadTxt3() {
		try {
			this.downloadCuenta();

			RequestContext.getCurrentInstance().execute(DOWNLOAD_TXT3);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ExportInformationService getExportInformationService() {
		return exportInformationService;
	}

	public void setExportInformationService(ExportInformationService exportInformationService) {
		this.exportInformationService = exportInformationService;
	}

	public TcPeriodoRepositoy getTcPeriodoRepositoy() {
		return tcPeriodoRepositoy;
	}

	public void setTcPeriodoRepositoy(TcPeriodoRepositoy tcPeriodoRepositoy) {
		this.tcPeriodoRepositoy = tcPeriodoRepositoy;
	}

	public StreamedContent getFile1() {
		return file1;
	}

	public void setFile1(StreamedContent file1) {
		this.file1 = file1;
	}

	public StreamedContent getFile2() {
		return file2;
	}

	public void setFile2(StreamedContent file2) {
		this.file2 = file2;
	}

	public StreamedContent getFile3() {
		return file3;
	}

	public void setFile3(StreamedContent file3) {
		this.file3 = file3;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public List<TcPeriodo> getListPeriodo() {
		return listPeriodo;
	}

	public void setListPeriodo(List<TcPeriodo> listPeriodo) {
		this.listPeriodo = listPeriodo;
	}

}
