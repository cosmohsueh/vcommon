package com.vteam.common;

import org.hibernate.dialect.Oracle9iDialect;
import org.hibernate.type.StandardBasicTypes;

public class Oracle9iExtendDialect extends Oracle9iDialect {
	
	public Oracle9iExtendDialect() {
		registerHibernateType(-4, StandardBasicTypes.BINARY.getName());
		registerHibernateType(-1, StandardBasicTypes.BINARY.getName());
	}

	public boolean supportsLimit() {
		return false;
	}
}
