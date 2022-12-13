package com.gem.sistema.util;

/**
 * @author Alfredo Neri
 *
 */
public enum ConstantsClaveEnnum {
	CLAVE_F01("F01"), CLAVE_F02("F02"), CLAVE_F03("F03"), CLAVE_F04("F04"), CLAVE_F05("F05"), CLAVE_F06("F06"),
	CLAVE_F07("F07"), CLAVE_F08("F08"), CLAVE_F09("F09"), CLAVE_F10("F10"), CLAVE_F11("F11"), CLAVE_F12("F12"),
	CLAVE_F13("F13"), CLAVE_F14("F14"), CLAVE_F15("F15"), CLAVE_F16("F16"), CLAVE_F17("F17"), CLAVE_F18("F18"),
	CLAVE_F19("F19"), CLAVE_F20("F20"), CLAVE_F21("F21"), CLAVE_F22("F22"), CLAVE_F23("F23"), CLAVE_F24("F24"),
	CLAVE_F25("F25"), CLAVE_F26("F26"), CLAVE_F27("F27"), CLAVE_F28("F28"), CLAVE_F29("F29"), CLAVE_F30("F30"),
	CLAVE_F31("F31"), CLAVE_F32("F32"), CLAVE_F33("F33"), CLAVE_F34("F34"), CLAVE_F35("F35"), CLAVE_F36("F36"),
	CLAVE_F37("F37"), CLAVE_F38("F38"), CLAVE_F39("F39"), CLAVE_F40("F40");

	private String value;

	private ConstantsClaveEnnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
