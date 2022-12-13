package com.gem.sistema.business.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TC_MOVIMIENTOS")
public class TcMovimiento extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = -4486847374725515997L;
	private String usuario;
	private Date fecha;
	@ManyToOne
	@JoinColumn(name = "TIPO_ID", nullable = false)
	private TcTipoMovimiento tipoMovimiento;
	@ManyToOne
	@JoinColumn(name = "PRESUPUESTO_ID", nullable = false)
	private TrPresupuestoDetallado presupuesto;
	private Integer mes;
	private BigDecimal cargo;
	private BigDecimal abono;
	private Integer folio;

	public TcMovimiento() {

	}

	public TcMovimiento(String usuario, Date fecha, TcTipoMovimiento tipoMovimiento, TrPresupuestoDetallado presupuesto,
			Integer mes, BigDecimal cargo, BigDecimal abono, Integer folio) {
		super();
		this.usuario = usuario;
		this.fecha = fecha;
		this.tipoMovimiento = tipoMovimiento;
		this.presupuesto = presupuesto;
		this.mes = mes;
		this.cargo = cargo;
		this.abono = abono;
		this.folio = folio;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public TcTipoMovimiento getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(TcTipoMovimiento tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public TrPresupuestoDetallado getPresupuesto() {
		return presupuesto;
	}

	public void setPresupuesto(TrPresupuestoDetallado presupuesto) {
		this.presupuesto = presupuesto;
	}

	public BigDecimal getCargo() {
		return cargo;
	}

	public void setCargo(BigDecimal cargo) {
		this.cargo = cargo;
	}

	public BigDecimal getAbono() {
		return abono;
	}

	public void setAbono(BigDecimal abono) {
		this.abono = abono;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(abono, cargo, fecha, presupuesto, tipoMovimiento, usuario);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TcMovimiento other = (TcMovimiento) obj;
		return Objects.equals(abono, other.abono) && Objects.equals(cargo, other.cargo)
				&& Objects.equals(fecha, other.fecha) && Objects.equals(presupuesto, other.presupuesto)
				&& Objects.equals(tipoMovimiento, other.tipoMovimiento) && Objects.equals(usuario, other.usuario);
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getFolio() {
		return folio;
	}

	public void setFolio(Integer folio) {
		this.folio = folio;
	}

}
