package com.vteam.common;

import java.sql.Connection;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class JNDILocator {
	
	private Context ctx = null;
	public static final int IBM_SERVER = 1;
	public static final int TOMCAT_SERVER = 2;
	public Connection conn;

	private JNDILocator(String jndiName, int serverName) throws Exception {
		setConnection(jndiName, serverName);
	}

	public static JNDILocator getInstance(String jndiName, int serverName)
			throws Exception {
		return new JNDILocator(jndiName, serverName);
	}

	public void setConnection(String jndiName, int serverName) throws Exception {
		switch (serverName) {

		case 1:
			Hashtable<String, String> parms = new Hashtable<String, String>();
			Class<?> c = Class.forName("com.ibm.websphere.naming.WsnInitialContextFactory");
			parms.put("java.naming.factory.initial", c.getName());
			this.ctx = new InitialContext(parms);
			break;
		case 2:
			this.ctx = new InitialContext();
			this.ctx = ((Context) this.ctx.lookup("java:comp/env"));
		}
		DataSource ds = (DataSource) this.ctx.lookup(jndiName);
		this.conn = ds.getConnection();
	}

	public Connection getConnection() {
		return this.conn;
	}

	public void close() throws Exception {
		if (this.conn != null) {
			this.conn.close();
			this.conn = null;
		}
		if (this.ctx != null) {
			this.ctx = null;
		}
	}
}