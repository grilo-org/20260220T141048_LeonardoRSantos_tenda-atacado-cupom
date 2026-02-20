package br.com.tenda.atacado.cupom.infrastructure.repository.converter;


import org.apache.commons.beanutils.PropertyUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

@Converter
public abstract class GenericConverter<T extends Enum<T>, K extends Serializable> implements AttributeConverter<T, K> {

	Class<T> enumType;

	private final String nameFieldId;

	public GenericConverter(String nameFieldId) {
		this.nameFieldId = nameFieldId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public K convertToDatabaseColumn(T enumObj) {
		if (enumObj == null) {
			return null;
		}
		try {
			Object idValue = PropertyUtils.getProperty(enumObj, nameFieldId);
			return (K) idValue;
		} catch (Exception e ) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T convertToEntityAttribute(K key) {
		enumType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		if (key != null) {
			T[] tipos = enumType.getEnumConstants();
			for(T tipo: tipos) {
				try {
					Object idValue = PropertyUtils.getProperty(tipo, nameFieldId);
					if (idValue.equals(key)) {
						return tipo;
					}
				} catch (Exception e ) {
					return null;
				}
			}
		}
		return null;
	}


}