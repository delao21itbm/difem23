package com.gem.sistema.web.bean;

import static com.gem.sistema.util.Constants.ZERO;
import static com.gem.sistema.util.UtilFront.generateNotificationFront;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.BooleanUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.gem.sistema.business.domain.AbstractEntity;

public abstract class AbstractSimpleCatalog<E extends AbstractEntity, R extends PagingAndSortingRepository<E, Long>>
		extends CommonCatalogMB<E> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Boolean insert;
	private E rowSelected;
	private String errorMessage;
	private int selectedIndex;
	static final String name = "#{tcAreaAdministrativaRepository}";

	@PostConstruct
	public void init() {
		setBeanFind(getNewType());
		setList(new ArrayList<E>());
		setListNew(new ArrayList<E>());
	}

	public void onPageLoad() {
		cleanFieldsBean();
		restartData();
	}

	@SuppressWarnings("unchecked")
	public void onRowEdit(final RowEditEvent event) {
		final E catalog = (E) event.getObject();
		executeOperationSaveOrUpdate(catalog, getRepository());
		RequestContext.getCurrentInstance().execute("document.getElementById('form1:hideButton2').click();");
		activateRowEdit(catalog.getIndex());
	}

	public void delete() {
		if (isUsed(getBeanSelected())) {
			generateNotificationFront(SEVERITY_ERROR, "No es posible eliminar", "el registro esta siendo ocupado");
			return;
		}
		if (BooleanUtils.negate(getBeanSelected().getId().intValue() == ZERO)) {
			getRepository().delete(getBeanSelected());
		}
		generateNotificationFront(SEVERITY_INFO, MESSAGE_INFO, MESSAGE_DELETED_RECORD);
		findValuesCatalog();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void consultList() {
		addList((List<E>) repositoryCustom.findByFilters(getBeanFind()));
		restartFilteredList();
	}

	@Override
	protected void consultAll() {
		System.out.println(getRepository());
		setList((List<E>) getRepository().findAll());
		restartFilteredList();
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
		System.out.println("paso");
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

	public abstract E getNewType();

	public abstract Boolean isUsed(E entity);

	public abstract void cleanFieldsBean();

	public abstract R getRepository();

	public Boolean getInsert() {
		return insert;
	}

	public void setInsert(Boolean insert) {
		this.insert = insert;
	}

	public E getRowSelected() {
		return rowSelected;
	}

	public void setRowSelected(E rowSelected) {
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

}
