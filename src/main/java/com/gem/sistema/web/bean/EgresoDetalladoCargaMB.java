package com.gem.sistema.web.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import com.gem.sistema.business.domain.TrPresupuestoDetallado;
import com.gem.sistema.business.dto.EgresoCargaDTO;
import com.gem.sistema.business.service.catalogos.EgresoDetalladoService;
import com.gem.sistema.util.CopyFileUtil;

@ManagedBean(name = "egresoDetalladoCargMB")
@ViewScoped
public class EgresoDetalladoCargaMB extends AbstractMB {

	private static final String PATH = "/gem/upfiles/";
	private List<TrPresupuestoDetallado> cargados;
	private String fileError = null;

	@ManagedProperty("#{egresoDetalladoService}")
	private EgresoDetalladoService egresoDetalladoService;

	@PostConstruct
	public void init() {

	}

	public void handleFileUpload(FileUploadEvent event) {
		String newName = UUID.randomUUID() + "" + event.getFile().getFileName();
		if (event.getFile() != null) {
			if (StringUtils.isNotEmpty(newName)) {
				try {
					String path = CopyFileUtil.copyFile(newName, event.getFile().getInputstream(), PATH);
					if (event.getFile().getSize() <= 0) {
						this.displayInfoMessage("El archivo no puede ser vacio");
					} else {
						File file = new File(path);
						EgresoCargaDTO salida = this.egresoDetalladoService.cargaArchivoEgreso(file,
								this.getUserDetails().getIdSector());
						cargados = salida.getCargados();
						fileError = salida.getCodError() == -1 ? "/gem/errores/" + newName
								: salida.getCodError() == 0 ? salida.getFullFilePath() : null;
						salida.getMsgsError().forEach(this::displayInfoMessage);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				this.displayInfoMessage("Selecciona un archivo");
			}
		} else {
			this.displayInfoMessage("Selecciona un archivo");
		}

	}

	public StreamedContent getFile() {
		StreamedContent st = null;
		try {
			File file = new File(this.getFileError());
			st = new DefaultStreamedContent(new FileInputStream(file), "text/csv", file.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return st;
	}

	public List<TrPresupuestoDetallado> getCargados() {
		return cargados;
	}

	public void setCargados(List<TrPresupuestoDetallado> cargados) {
		this.cargados = cargados;
	}

	public String getFileError() {
		return fileError;
	}

	public void setFileError(String fileError) {
		this.fileError = fileError;
	}

	public EgresoDetalladoService getEgresoDetalladoService() {
		return egresoDetalladoService;
	}

	public void setEgresoDetalladoService(EgresoDetalladoService egresoDetalladoService) {
		this.egresoDetalladoService = egresoDetalladoService;
	}

}
