package com.gem.sistema.web.converters;

import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gem.sistema.business.domain.AbstractEntity;

@FacesConverter("pickListConverter")
public class PickListConverter implements Converter {
	private static final Logger LOG = LoggerFactory.getLogger(PickListConverter.class);

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		LOG.trace("String value: {}", value);

		return getObjectFromUIPickListComponent(component, value);
	}

	public String getAsString(FacesContext context, UIComponent component, Object object) {
		String string;
		LOG.trace("Object value: {}", object);
		if (object == null) {
			string = "";
		} else {
			try {
				string = String.valueOf(((AbstractEntity) object).getId());
			} catch (ClassCastException cce) {
				throw new ConverterException();
			}
		}
		if (string == null) {
			System.out.println("Id null encontrado");
		}
		return string;
	}

	@SuppressWarnings("unchecked")
	private AbstractEntity getObjectFromUIPickListComponent(UIComponent component, String value) {
		final DualListModel<AbstractEntity> dualList;
		try {
			dualList = (DualListModel<AbstractEntity>) ((PickList) component).getValue();
			AbstractEntity object = getObjectFromList(dualList.getSource(), Long.valueOf(value));
			if (object == null) {
				object = getObjectFromList(dualList.getTarget(), Long.valueOf(value));
			}
			if (object == null) {
				System.out.println("Null value encontrado");
			}
			return object;
		} catch (ClassCastException cce) {
			throw new ConverterException();
		} catch (NumberFormatException nfe) {
			throw new ConverterException();
		}
	}

	private AbstractEntity getObjectFromList(final List<AbstractEntity> list, final Long identifier) {
		for (final AbstractEntity object : list) {
			final AbstractEntity entity = object;
			if (entity.getId().equals(identifier)) {
				return entity;
			}
		}
		return null;
	}
}
