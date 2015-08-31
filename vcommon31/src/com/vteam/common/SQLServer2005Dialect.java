package com.vteam.common;

import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.type.StandardBasicTypes;

public class SQLServer2005Dialect extends SQLServerDialect {
	
	public SQLServer2005Dialect() {
		registerHibernateType(-9, StandardBasicTypes.STRING.getName());
		registerHibernateType(-16, StandardBasicTypes.STRING.getName());
	}

	public boolean supportsLimit() {
		return false;
	}
}
