package com.gem.sistema.business.service.catalogos;

import static com.gem.sistema.util.UtilParseString.parseString;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import com.gem.sistema.business.domain.Catdep;
import com.gem.sistema.business.domain.FuentefGobierno;
import com.gem.sistema.business.domain.Muninep;
import com.gem.sistema.business.domain.Natgas;
import com.gem.sistema.business.domain.TcArea;
import com.gem.sistema.business.domain.TcMovimiento;
import com.gem.sistema.business.domain.TcTipoMovimiento;
import com.gem.sistema.business.domain.TrPresupuestoDetallado;
import com.gem.sistema.business.dto.EgresoCargaDTO;
import com.gem.sistema.business.dto.PresupuestoDetalladoDTO;
import com.gem.sistema.business.predicates.PresupuestoDetalladoPredicate;
import com.gem.sistema.business.repository.catalogs.CatdepRepository;
import com.gem.sistema.business.repository.catalogs.FuenteFGobiernoRepository;
import com.gem.sistema.business.repository.catalogs.MuniNepRepository;
import com.gem.sistema.business.repository.catalogs.NatgasRepository;
import com.gem.sistema.business.repository.catalogs.TcAreaRepository;
import com.gem.sistema.business.repository.catalogs.TcMovimientoRepository;
import com.gem.sistema.business.repository.catalogs.TrPresupuestoDetalladoRepository;
import com.gem.sistema.load.fileupload.model.FileUpload;
import com.gem.sistema.load.fileupload.validators.FileContentValidator;

@Service(value = "egresoDetalladoService")
public class EgresoDetalladoServiceImpl implements EgresoDetalladoService {
	private static final String AND = "and";
	private static final String DEFAULT_PATH_OUTVALID = "/gem/egresos/";
	private static final Logger logger=LoggerFactory.getLogger(EgresoDetalladoServiceImpl.class);

	@Autowired
	private EntityManager em;

	@Autowired
	@Qualifier("tcPresupuestoDetalladoRepository")
	private TrPresupuestoDetalladoRepository trPresupuestoDetalladoRepository;

	@Autowired
	@Qualifier("egresoDetalladoValidador")
	FileContentValidator egresoDetalladoValidador;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private SimpleJdbcCall jdbcCallEgresos;

	@Autowired
	private CatdepRepository catdepRepository;
	@Autowired
	private TcAreaRepository tcAreaRepository;
	@Autowired
	private NatgasRepository natgasRepository;
	@Autowired
	private MuniNepRepository muninepRepository;
	@Autowired
	private FuenteFGobiernoRepository fuenteFGobiernoRepository;
	@Autowired
	private TcMovimientoRepository tcMovimientoRepository;

	@Autowired
	public void initCallStore() {
		this.jdbcCallEgresos = new SimpleJdbcCall(jdbcTemplate).withProcedureName("SP_CARGA_EGRESOS_DETALLADO");
	}

	@Override
	public EgresoCargaDTO cargaArchivoEgreso(File newFile, Integer idSector) {
		EgresoCargaDTO salida = new EgresoCargaDTO();
		salida.setCodError(0);
		final FileUpload fileUpload = new FileUpload();
		fileUpload.setFile(newFile);
		fileUpload.setNameOriginal(newFile.getName());
		fileUpload.setNameReal(newFile.getName());
		fileUpload.setErrorPath(DEFAULT_PATH_OUTVALID);
		fileUpload.setOutPutPath(DEFAULT_PATH_OUTVALID);
		try {
			if (this.egresoDetalladoValidador.isValid(fileUpload)) {
				salida.setCodError(1);
				salida = this.cargaArchivo(newFile, salida, idSector);
			} else {
				salida.addMsn("El archivo contine errores de formato, Descargar archivo de errores ");
				salida.setCodError(-1);
			}
		} catch (Exception e) {
			salida.setCodError(0);
			salida.addMsn("Ha ocurrido un error valida el tipo de archivo");
			e.printStackTrace();
		}
		return salida;
	}

	private EgresoCargaDTO cargaArchivo(File file, EgresoCargaDTO salida, Integer idSector) {
		try {
			SqlParameterSource in = new MapSqlParameterSource().addValue("i_id_sector", idSector)
					.addValue("i_file_name", file.getName()).addValue("i_path_file", "/gem/upfiles/");
			Map<String, Object> out = jdbcCallEgresos.execute(in);
			salida.setCodError(MapUtils.getInteger(out, "O_COD_ERROR"));
			if (salida.getCodError() == 1) {
				salida.setCargados(trPresupuestoDetalladoRepository.findAll());
				salida.addMsn(MapUtils.getString(out, "O_MSG_ERROR"));
			} else {
				salida.setFullFilePath(MapUtils.getString(out, "O_MSG_ERROR"));
				salida.addMsn("El archivo contine errores de formato, Descargar archivo de errores ");
			}
		} catch (DataAccessException e) {
			salida.setCodError(0);
			salida.addMsn("Ha ocurrido un error valida el tipo de archivo");
			e.printStackTrace();
		}

		return salida;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TrPresupuestoDetallado> getAllByFilters(String area, String dep, String partida, String proyecto,
			String fuente) throws DataAccessException {
		String condiciones = "";
		if (area != null && area != "") {
			condiciones += AND + " a.clave = " + area + " ";
		}
		if (dep != null && dep != "") {
			condiciones += AND + " upper(d.clvdep) LIKE upper('%" + dep + "%') ";
		}
		if (partida != null && partida != "") {
			condiciones += AND + "  n.clvgas LIKE '%" + partida + "%' ";
		}
		if (proyecto != null && proyecto != "") {
			condiciones += AND + "  m.campo7 LIKE '%" + proyecto + "%' ";
		}

		if (StringUtils.isNotEmpty(fuente)) {
			condiciones += AND + "  f.cuenta||f.scta||f.sscta||f.ssscta LIKE '%" + fuente + "%' ";
		}

		if (condiciones != "") {
			condiciones = condiciones.substring(3, condiciones.length());
			condiciones = "WHERE " + condiciones;
		} else {
			condiciones = "";
		}

		StringBuilder query = new StringBuilder("select e,a,n,d,m,f from TrPresupuestoDetallado e  join fetch e.area a "
				+ "join fetch e.partida n " + "join fetch e.dependencia d " + "join fetch e.proyecto m "
				+ "join fetch e.fuente f " + condiciones);
		query.append("order by e,a,n,d,m asc");

		System.out.println("::::::::::::::::" + query.toString());
		Query results = em.createQuery(query.toString());
		List<TrPresupuestoDetallado> salida = new ArrayList<TrPresupuestoDetallado>();
		salida = (List<TrPresupuestoDetallado>) results.getResultList();
		return salida;
	}
	String[] filterDetallado =null;
	String progr = null;
	String partidaTmp =null;
	@Override
	public List<String> parseDual(List<String> dualList, String programa, String fuenteF, String partida,
			TcTipoMovimiento movimiento, String separador) {
		List<String> listTmp = new ArrayList<String>();
		System.out.println(programa);
		System.out.println(fuenteF);
		System.out.println(partida);
		System.out.println(movimiento.getId().toString());
		for (String cadTmp : dualList) {
			filterDetallado = parseString(cadTmp, separador);
			progr = filterDetallado[2].substring(0, 8);
		    partidaTmp = filterDetallado[4].substring(0, 1);
			// Se valida si el tipo de movimiento es un Traspaso Internof

			if (movimiento.getId() == 2 && progr.equals(programa) && fuenteF.equals(filterDetallado[3])
					&& partidaTmp.equals(partida))
				listTmp.add(cadTmp);
			// Se valida si el tipo de movimiento es un Traspaso Externo o Cancelacion
			else if ((movimiento.getId() == 1 || movimiento.getId() == 3) && partidaTmp.equals(partida))
				listTmp.add(cadTmp);
			else if (movimiento.getId() >= 4)
				listTmp.add(cadTmp);
		}
		return listTmp;
	}

	@Override
	public void guardarMovimiento(String usuario, TcTipoMovimiento tmovimiento, PresupuestoDetalladoDTO presupuestoDto,
			Integer mes, Integer folio, Boolean bandera) {

		Catdep catdep = this.catdepRepository.findFirstByIdsectorAndClvdep(2, presupuestoDto.getDependencia());
		TcArea tcarea = this.tcAreaRepository.findOneByClave(presupuestoDto.getArea());
		Natgas natgas = this.natgasRepository.findFirstByClvgasAndIdsector(presupuestoDto.getPartida(), 2);
		Muninep muninep = this.muninepRepository.findFirstByCampo7AndIdsector(presupuestoDto.getProyecto(), 2);
		FuentefGobierno fuentefg = this.fuenteFGobiernoRepository.findByFuente(presupuestoDto.getFuente());
		TrPresupuestoDetallado preDetallado = this.trPresupuestoDetalladoRepository
				.findOne(PresupuestoDetalladoPredicate.buscaPorMovimiento(tcarea.getId(), catdep.getId(),
						muninep.getId(), fuentefg.getId(), natgas.getId()));
		preDetallado.setAutoAbril(presupuestoDto.getTotal());

		this.trPresupuestoDetalladoRepository.save(preDetallado);
		TcMovimiento tcMovimiento = new TcMovimiento(usuario, new Date(), tmovimiento, preDetallado, mes,
				presupuestoDto.getReduccion(), presupuestoDto.getAmpliacion(), folio);
		if (bandera)
			this.tcMovimientoRepository.save(tcMovimiento);

	}

}
