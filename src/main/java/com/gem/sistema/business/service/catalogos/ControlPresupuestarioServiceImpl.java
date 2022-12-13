package com.gem.sistema.business.service.catalogos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gem.sistema.business.domain.Cattpo;
import com.gem.sistema.business.domain.Conctb;
import com.gem.sistema.business.domain.TcControlPresupuestario;
import com.gem.sistema.business.domain.TcPeriodo;
import com.gem.sistema.business.domain.TrPresupuestoArea;
import com.gem.sistema.business.domain.TwAccionContrato;
import com.gem.sistema.business.domain.TwContratoFactura;
import com.gem.sistema.business.repository.catalogs.CattpoRepository;
import com.gem.sistema.business.repository.catalogs.ConctbRepository;
import com.gem.sistema.business.repository.catalogs.TcControlPresupuestarioRepository;
import com.gem.sistema.business.repository.catalogs.TcPeriodoRepositoy;
import com.gem.sistema.business.repository.catalogs.TrPresupuestoAreaRepository;
import com.gem.sistema.business.repository.catalogs.TwAccionContratoRepository;
import com.gem.sistema.business.repository.catalogs.TwContratoFacturaRepository;

@Service(value = "controlPresupuestarioService")
public class ControlPresupuestarioServiceImpl implements ControlPresupuestarioService {

	@Autowired
	private TcControlPresupuestarioRepository controlPresupuestarioRepository;

	@Autowired
	private TrPresupuestoAreaRepository presupuestoAreaRepository;

	@Autowired
	private TwContratoFacturaRepository contratoFacturaRepository;
	
	@Autowired
	private TwAccionContratoRepository accionContratoRepository;

	@Autowired
	private CattpoRepository cattpoRepository;

	@Autowired
	private ConctbRepository conctbRepository;

	@Autowired
	private TcPeriodoRepositoy periodoRepositoy;

	@Override
	public List<TcControlPresupuestario> getControlPresupuestario() {
		return (List<TcControlPresupuestario>) controlPresupuestarioRepository.findAll();
	}

	@Override
	public List<TrPresupuestoArea> getAreasWithPresupuesto() {
		return (List<TrPresupuestoArea>) presupuestoAreaRepository.findAll();
	}

	@Override
	public TcControlPresupuestario getControlPresupuestario(String claveControl) {
		return controlPresupuestarioRepository.findByClave(claveControl);
	}

	@Override
	public TwAccionContrato saveAccionConytrato(TwAccionContrato accionContrato) {
		return accionContratoRepository.save(accionContrato);
	}

	@Override
	public TcControlPresupuestario saveControlPresupuestario(TcControlPresupuestario presupuestario) {
		return controlPresupuestarioRepository.save(presupuestario);
	}

	@Override
	public TwContratoFactura saveContratoFactura(TwContratoFactura contratoFactura) {
		return contratoFacturaRepository.save(contratoFactura);
	}

	@Override
	public List<TrPresupuestoArea> savePresupuestoAreas(List<TrPresupuestoArea> presupuestoAreas) {
		return (List<TrPresupuestoArea>) presupuestoAreaRepository.save(presupuestoAreas);
	}

	@Override
	public List<TcPeriodo> getActivePeriodos(Integer sector) {

		Conctb conctb = conctbRepository.findByIdsector(sector);
		List<TcPeriodo> meses = periodoRepositoy.findByTipoPeriodoAndPeriodoLessThanEqualOrderByPeriodo(1,
				conctb.getMesemp());

		return meses;
	}

	@Override
	public List<Cattpo> getAllTiposPoliza() {
		return cattpoRepository.findAll();
	}

	public TcControlPresupuestarioRepository getControlPresupuestarioRepository() {
		return controlPresupuestarioRepository;
	}

	public void setControlPresupuestarioRepository(TcControlPresupuestarioRepository controlPresupuestarioRepository) {
		this.controlPresupuestarioRepository = controlPresupuestarioRepository;
	}

	public TrPresupuestoAreaRepository getPresupuestoAreaRepository() {
		return presupuestoAreaRepository;
	}

	public void setPresupuestoAreaRepository(TrPresupuestoAreaRepository presupuestoAreaRepository) {
		this.presupuestoAreaRepository = presupuestoAreaRepository;
	}

	public TwContratoFacturaRepository getContratoFacturaRepository() {
		return contratoFacturaRepository;
	}

	public void setContratoFacturaRepository(TwContratoFacturaRepository contratoFacturaRepository) {
		this.contratoFacturaRepository = contratoFacturaRepository;
	}

	public CattpoRepository getCattpoRepository() {
		return cattpoRepository;
	}

	public void setCattpoRepository(CattpoRepository cattpoRepository) {
		this.cattpoRepository = cattpoRepository;
	}

	public ConctbRepository getConctbRepository() {
		return conctbRepository;
	}

	public void setConctbRepository(ConctbRepository conctbRepository) {
		this.conctbRepository = conctbRepository;
	}

	public TcPeriodoRepositoy getPeriodoRepositoy() {
		return periodoRepositoy;
	}

	public void setPeriodoRepositoy(TcPeriodoRepositoy periodoRepositoy) {
		this.periodoRepositoy = periodoRepositoy;
	}

}
