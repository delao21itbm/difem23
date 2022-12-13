package com.gem.sistema.business.service.catalogos;

import java.util.List;

import com.gem.sistema.business.domain.Cattpo;
import com.gem.sistema.business.domain.TcControlPresupuestario;
import com.gem.sistema.business.domain.TcPeriodo;
import com.gem.sistema.business.domain.TrPresupuestoArea;
import com.gem.sistema.business.domain.TwAccionContrato;
import com.gem.sistema.business.domain.TwContratoFactura;

public interface ControlPresupuestarioService {

	List<TcControlPresupuestario> getControlPresupuestario();
	
	List<TrPresupuestoArea> getAreasWithPresupuesto();
	
	TcControlPresupuestario getControlPresupuestario(String claveControl);
	
	TwAccionContrato saveAccionConytrato(TwAccionContrato accionContrato);
	
	TcControlPresupuestario saveControlPresupuestario(TcControlPresupuestario presupuestario);
	
	List<TrPresupuestoArea> savePresupuestoAreas(List<TrPresupuestoArea> presupuestoAreas);
	
	TwContratoFactura saveContratoFactura(TwContratoFactura contratoFactura);
	
	List<TcPeriodo> getActivePeriodos(Integer sector);
	
	List<Cattpo> getAllTiposPoliza();
}
