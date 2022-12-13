/**
 * 
 */
package com.gem.sistema.business.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.gem.sistema.business.domain.TrPresupuestoDetallado;
import com.gem.sistema.business.domain.TwIngresoPropiosDetalle;

public class EgresoCargaDTO implements Serializable {
	private static final long serialVersionUID = 7902769915722607532L;

	private Integer codError;
	private List<String> msgsError = new ArrayList<String>();
	private String fullFilePath;
	private List<TrPresupuestoDetallado> cargados = new ArrayList<TrPresupuestoDetallado>();

	public EgresoCargaDTO() {
	}

	public EgresoCargaDTO(Integer codError, List<String> msgsError, String fullFilePath, BigDecimal total) {
		this.codError = codError;
		this.msgsError = msgsError;
		this.fullFilePath = fullFilePath;
	}

	public Integer getCodError() {
		return codError;
	}

	public void setCodError(Integer codError) {
		this.codError = codError;
	}

	public String getFullFilePath() {
		return fullFilePath;
	}

	public void setFullFilePath(String fullFilePath) {
		this.fullFilePath = fullFilePath;
	}

	public List<String> getMsgsError() {
		return msgsError;
	}

	public void setMsgsError(List<String> msgsError) {
		this.msgsError = msgsError;
	}

	public void addMsn(String msn) {
		this.msgsError.add(msn);
	}

	public List<TrPresupuestoDetallado> getCargados() {
		return cargados;
	}

	public void setCargados(List<TrPresupuestoDetallado> cargados) {
		this.cargados = cargados;
	}

	
}
