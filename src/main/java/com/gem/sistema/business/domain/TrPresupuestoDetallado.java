package com.gem.sistema.business.domain;

import java.io.Serializable;
import javax.persistence.*;

import com.gem.sistema.annotation.IgnoredQuery;

import java.math.BigDecimal;

/**
 * The persistent class for the TR_PRESUPUESTO_DETALLADO database table.
 * 
 */
@Entity
@Table(name = "TR_PRESUPUESTO_DETALLADO")
@NamedQuery(name = "TrPresupuestoDetallado.findAll", query = "SELECT t FROM TrPresupuestoDetallado t")
public class TrPresupuestoDetallado extends AbstractEntity implements Serializable {
	@IgnoredQuery
	private static final long serialVersionUID = 1L;

	@Column(name = "AUTO_ABRIL")
	private BigDecimal autoAbril;

	@Column(name = "AUTO_AGOSTO")
	private BigDecimal autoAgosto;

	@Column(name = "AUTO_DICIEMBRE")
	private BigDecimal autoDiciembre;

	@Column(name = "AUTO_ENERO")
	private BigDecimal autoEnero;

	@Column(name = "AUTO_FEBRERO")
	private BigDecimal autoFebrero;

	@Column(name = "AUTO_JULIO")
	private BigDecimal autoJulio;

	@Column(name = "AUTO_JUNIO")
	private BigDecimal autoJunio;

	@Column(name = "AUTO_MARZO")
	private BigDecimal autoMarzo;

	@Column(name = "AUTO_MAYO")
	private BigDecimal autoMayo;

	@Column(name = "AUTO_NOVIEMBRE")
	private BigDecimal autoNoviembre;

	@Column(name = "AUTO_OCTUBRE")
	private BigDecimal autoOctubre;

	@Column(name = "AUTO_SEPTIEMBRE")
	private BigDecimal autoSeptiembre;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns(@JoinColumn(name = "ID_AREA", referencedColumnName = "ID"))
	private TcArea area;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns(@JoinColumn(name = "ID_DEPENDENCIA", referencedColumnName = "ID"))
	private Catdep dependencia;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns(@JoinColumn(name = "ID_PARTIDA", referencedColumnName = "ID"))
	private Natgas partida;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns(@JoinColumn(name = "ID_PROYECTO", referencedColumnName = "ID"))
	private Muninep proyecto;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns(@JoinColumn(name = "ID_FUENTE", referencedColumnName = "ID"))
	private FuentefGobierno fuente;

	public TrPresupuestoDetallado() {
	}

	public Muninep getProyecto() {
		return proyecto;
	}

	public void setProyecto(Muninep proyecto) {
		this.proyecto = proyecto;
	}

	public BigDecimal getAutoAbril() {
		return this.autoAbril;
	}

	public void setAutoAbril(BigDecimal autoAbril) {
		this.autoAbril = autoAbril;
	}

	public BigDecimal getAutoAgosto() {
		return this.autoAgosto;
	}

	public void setAutoAgosto(BigDecimal autoAgosto) {
		this.autoAgosto = autoAgosto;
	}

	public BigDecimal getAutoDiciembre() {
		return this.autoDiciembre;
	}

	public void setAutoDiciembre(BigDecimal autoDiciembre) {
		this.autoDiciembre = autoDiciembre;
	}

	public BigDecimal getAutoEnero() {
		return this.autoEnero;
	}

	public void setAutoEnero(BigDecimal autoEnero) {
		this.autoEnero = autoEnero;
	}

	public BigDecimal getAutoFebrero() {
		return this.autoFebrero;
	}

	public void setAutoFebrero(BigDecimal autoFebrero) {
		this.autoFebrero = autoFebrero;
	}

	public BigDecimal getAutoJulio() {
		return this.autoJulio;
	}

	public void setAutoJulio(BigDecimal autoJulio) {
		this.autoJulio = autoJulio;
	}

	public BigDecimal getAutoJunio() {
		return this.autoJunio;
	}

	public void setAutoJunio(BigDecimal autoJunio) {
		this.autoJunio = autoJunio;
	}

	public BigDecimal getAutoMarzo() {
		return this.autoMarzo;
	}

	public void setAutoMarzo(BigDecimal autoMarzo) {
		this.autoMarzo = autoMarzo;
	}

	public BigDecimal getAutoMayo() {
		return this.autoMayo;
	}

	public void setAutoMayo(BigDecimal autoMayo) {
		this.autoMayo = autoMayo;
	}

	public BigDecimal getAutoNoviembre() {
		return this.autoNoviembre;
	}

	public void setAutoNoviembre(BigDecimal autoNoviembre) {
		this.autoNoviembre = autoNoviembre;
	}

	public BigDecimal getAutoOctubre() {
		return this.autoOctubre;
	}

	public void setAutoOctubre(BigDecimal autoOctubre) {
		this.autoOctubre = autoOctubre;
	}

	public BigDecimal getAutoSeptiembre() {
		return this.autoSeptiembre;
	}

	public void setAutoSeptiembre(BigDecimal autoSeptiembre) {
		this.autoSeptiembre = autoSeptiembre;
	}

	public TcArea getArea() {
		return area;
	}

	public void setArea(TcArea area) {
		this.area = area;
	}

	public Catdep getDependencia() {
		return dependencia;
	}

	public void setDependencia(Catdep dependencia) {
		this.dependencia = dependencia;
	}

	public Natgas getPartida() {
		return partida;
	}

	public void setPartida(Natgas partida) {
		this.partida = partida;
	}

	public FuentefGobierno getFuente() {
		return fuente;
	}

	public void setFuente(FuentefGobierno fuente) {
		this.fuente = fuente;
	}

	public BigDecimal getTotal() {
		BigDecimal total = new BigDecimal(0);
		total = autoEnero.add(total);
		total = autoFebrero.add(total);
		total = autoMarzo.add(total);
		total = autoAbril.add(total);
		total = autoMayo.add(total);
		total = autoJunio.add(total);
		total = autoJulio.add(total);
		total = autoAgosto.add(total);
		total = autoSeptiembre.add(total);
		total = autoOctubre.add(total);
		total = autoNoviembre.add(total);
		total = autoDiciembre.add(total);
		return total;
	}

	@Override
	public String toString() {
		return "TrPresupuestoDetallado [autoAbril=" + autoAbril + ", autoAgosto=" + autoAgosto + ", autoDiciembre="
				+ autoDiciembre + ", autoEnero=" + autoEnero + ", autoFebrero=" + autoFebrero + ", autoJulio="
				+ autoJulio + ", autoJunio=" + autoJunio + ", autoMarzo=" + autoMarzo + ", autoMayo=" + autoMayo
				+ ", autoNoviembre=" + autoNoviembre + ", autoOctubre=" + autoOctubre + ", autoSeptiembre="
				+ autoSeptiembre + ", area=" + area + ", dependencia=" + dependencia + ", partida=" + partida
				+ ", proyecto=" + proyecto + "]";
	}

}