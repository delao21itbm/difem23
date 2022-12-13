package com.gem.sistema.web.bean;

import static com.gem.sistema.util.Constants.ZERO;
import static com.gem.sistema.util.UtilFront.generateNotificationFront;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.BooleanUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import com.gem.sistema.business.domain.TcArea;
import com.gem.sistema.business.repository.catalogs.TcAreaRepository;
import com.gem.sistema.business.repository.catalogs.TrPresupuestoDetalladoRepository;

@ManagedBean(name = "catalogAreasMB")
@ViewScoped
public class CatalogAreasMB extends CommonCatalogMB<TcArea> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Boolean insert;
	private TcArea rowSelected;
	private String errorMessage;
	private int selectedIndex;

	@ManagedProperty("#{tcAreaRepository}")
	private TcAreaRepository tcAreaRepository;

	@ManagedProperty("#{tcPresupuestoDetalladoRepository}")
	private TrPresupuestoDetalladoRepository trPresupuestoDetalladoRepository;

	@PostConstruct
	public void init() {
		setBeanFind(new TcArea());
		setList(new ArrayList<TcArea>());
		setListNew(new ArrayList<TcArea>());
	}

	public void onPageLoad() {
		getBeanFind().setDescripcion(null);
		getBeanFind().setClave(null);
		restartData();
	}

	public void onRowEdit(final RowEditEvent event) {
		final TcArea catalog = (TcArea) event.getObject();
		catalog.setClave(catalog.getId() != null && catalog.getId() != ZERO ? catalog.getClave()
				: tcAreaRepository.getNewClave());
		executeOperationSaveOrUpdate(catalog, tcAreaRepository);
		RequestContext.getCurrentInstance().execute("document.getElementById('form1:hideButton2').click();");
		activateRowEdit(catalog.getIndex());
	}

	private Boolean haveEgresos(Integer clave) {
		return !trPresupuestoDetalladoRepository.getByClaveArea(clave).isEmpty();
	}

	public void delete() {
		if (haveEgresos(getBeanSelected().getClave())) {
			generateNotificationFront(SEVERITY_ERROR, "No es posible eliminar",
					"La clave ha sido ocupada en egresos detallados");
			return;
		}
		if (BooleanUtils.negate(getBeanSelected().getId().intValue() == ZERO)) {
			tcAreaRepository.delete(getBeanSelected());
		}
		generateNotificationFront(SEVERITY_INFO, MESSAGE_INFO, MESSAGE_DELETED_RECORD);
		findValuesCatalog();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void consultList() {
		addList((List<TcArea>) repositoryCustom.findByFiltersOrderBy(getBeanFind(), "clave", "="));
		restartFilteredList();
	}

	@Override
	protected void consultAll() {
		setList(tcAreaRepository.findAllByOrderByClave());
		restartFilteredList();
	}

	public List<TcArea> completeTcAreaName(String query) {
		consultAll();
		return getList().stream()
				.filter(a -> a.getDescripcion() != null && a.getDescripcion().toLowerCase().startsWith(query))
				.collect(Collectors.toList());
	}

	@Override
	protected boolean isValidRequest() {
		return Boolean.TRUE;
	}

	public void activateRowEdit() {
		super.activateRowEdit(rowSelected.getIndex());
	}

	@Override
	protected void addNewOriginalList() {

	}

	@Override
	public void onRowCancel(RowEditEvent event) {
		restartData();
		super.onRowCancel(event);
	}

	@Override
	public void insertRow() throws InstantiationException, IllegalAccessException {
		super.insertRow();
		this.insert = Boolean.TRUE;
	}

	public void showSuccesMessage() {
		consultList();
		generateNotificationFront(SEVERITY_INFO, MESSAGE_INFO, MESSAGE_EDITION_SUCCESS);
	}

	public void showErrorMessage() {
		activateRowEdit(this.selectedIndex);
		generateNotificationFront(SEVERITY_ERROR, MESSAGE_ERROR, this.errorMessage);
	}

	public void showCancelEdition() {
		generateNotificationFront(SEVERITY_INFO, MESSAGE_INFO, MESSAGE_EDITION_CANCELED);
	}

	public Boolean getInsert() {
		return insert;
	}

	public void setInsert(Boolean insert) {
		this.insert = insert;
	}

	public TcArea getRowSelected() {
		return rowSelected;
	}

	public void setRowSelected(TcArea rowSelected) {
		this.rowSelected = rowSelected;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	public TcAreaRepository getTcAreaRepository() {
		return tcAreaRepository;
	}

	public void setTcAreaRepository(TcAreaRepository tcAreaRepository) {
		this.tcAreaRepository = tcAreaRepository;
	}

	public TrPresupuestoDetalladoRepository getTrPresupuestoDetalladoRepository() {
		return trPresupuestoDetalladoRepository;
	}

	public void setTrPresupuestoDetalladoRepository(TrPresupuestoDetalladoRepository trPresupuestoDetalladoRepository) {
		this.trPresupuestoDetalladoRepository = trPresupuestoDetalladoRepository;
	}

}
