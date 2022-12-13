package com.gem.sistema.web.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import com.gem.sistema.business.domain.Proveedor;
import com.gem.sistema.business.repository.catalogs.ProveedoresRepository;

@ManagedBean(name = "proveedoresMB")
@ViewScoped
public class ProveedoresMB extends AbstractMB {

	private Proveedor proveedor;
	/** Array principal de proveedores */
	private List<Proveedor> proveedores = new ArrayList<>();
	private String busqueda = "";
	private List<String> tipos = Proveedor.TIPOS;

	@ManagedProperty("#{proveedoresRepository}")
	private ProveedoresRepository proveedoresRepository;

	@PostConstruct
	public void init() {
		proveedor = getDefaulthProveedor();
		proveedores = proveedoresRepository.findAll();
	}

	/** Instancia por defulth de un proveedor */
	private Proveedor getDefaulthProveedor() {
		Proveedor init = new Proveedor();
		init.setDomicilio("");
		init.setNombreProveedor("");
		init.setRfc("");
		init.setTelefono("");
		init.setTipo(tipos.get(0));
		return init;
	}

	/** Guarda un proveedor */
	public void guardarProveedor() {
		List<String> errors = proveedor.isValid();
		Boolean repetido = false;
		// Elimina puntos, comas, espacion y valida si ya esiste el nombre del
		// proveedor
		for (Proveedor p : proveedores) {
			if (p.getId() != proveedor.getId()) {
				String index = p.getNombreProveedor().toLowerCase();
				String nombre = proveedor.getNombreProveedor().toLowerCase();
				nombre = nombre.replaceAll("[., ]", "");
				index = index.replaceAll("[., ]", "");
				if (nombre.equals(index)) {
					repetido = true;
					break;
				}
			}
		}

		if (errors.isEmpty()) {
			if (!repetido) {
				try {
					boolean modificado = proveedor.getId() != null && proveedor.getId() > 0;
					Proveedor nuevo = proveedoresRepository.save(proveedor);
					if (modificado) {
						proveedores = proveedores.stream().map(p -> p.getId() == nuevo.getId() ? nuevo : p)
								.collect(Collectors.toList());
					} else {
						proveedores.add(nuevo);
					}
					displayInfoMessage(
							"Se ha " + (!modificado ? "agregado" : "modificado ") + " correctamente el proveedor");
					proveedor = getDefaulthProveedor();
				} catch (Exception e) {
					e.printStackTrace();
					displayInfoMessage("Ha ocurrrido un error al guardar el proveedor");
				}
			} else {
				displayInfoMessage("El nombre del proveedor ya existe");
			}
		} else {
			errors.forEach(e -> displayInfoMessage(e));
		}
	}

	/** Elimina un proveedor */
	public void deleteProveedor() {
		if (proveedor != null) {
			try {
				proveedoresRepository.delete(proveedor.getId());
				proveedores = proveedores.stream().filter(p -> p.getId() != proveedor.getId())
						.collect(Collectors.toList());
				displayInfoMessage("El proveedor se ha eliminado correctamente");
			} catch (Exception e) {
				e.printStackTrace();
				displayInfoMessage("Ha ocurrido un error eliminando el proveedor");
			}
		}
	}

	public void resetProveedor() {
		if (proveedor.getId() != null) {
			displayInfoMessage("Se cancelo la edicion");
		}
		proveedor = getDefaulthProveedor();

	}

	public void buscar() {
		if (busqueda == null || busqueda.equals("")) {
			proveedores = proveedoresRepository.findAll();
		} else {
			proveedores = proveedoresRepository.findAllByFiltro(busqueda.toLowerCase());
		}
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public List<Proveedor> getProveedores() {
		return proveedores;
	}

	public void setProveedores(List<Proveedor> proveedores) {
		this.proveedores = proveedores;
	}

	public ProveedoresRepository getProveedoresRepository() {
		return proveedoresRepository;
	}

	public void setProveedoresRepository(ProveedoresRepository proveedoresRepository) {
		this.proveedoresRepository = proveedoresRepository;
	}

	public List<String> getTipos() {
		return tipos;
	}

	public void setTipos(List<String> tipos) {
		this.tipos = tipos;
	}

	public String getBusqueda() {
		return busqueda;
	}

	public void setBusqueda(String busqueda) {
		this.busqueda = busqueda;
	}

}
