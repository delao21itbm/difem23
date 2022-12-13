package com.gem.sistema.business.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "TC_TIPO_MOVIMIENTOS")
public class TcTipoMovimiento extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 5357975403904925316L;
	private String movimiento;

	public String getMovimiento() {
		return movimiento;
	}

	public void setMovimiento(String movimiento) {
		this.movimiento = movimiento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(movimiento);
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
		TcTipoMovimiento other = (TcTipoMovimiento) obj;
		return Objects.equals(movimiento, other.movimiento);
	}

}
