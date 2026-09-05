public enum Tipo {
	INT,
	BOOL,
	CHAR,
	UNKNOWN,
	ERROR;
	
	public static Tipo fromString(String str) {
		if (str == null) return ERROR;
		switch (str.toLowerCase()) {
			case "int": return INT;
			case "bool": return BOOL;
			case "char": return CHAR;
			default: return UNKNOWN;
		}
	}
}