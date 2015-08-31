package com.vteam.common;

import java.sql.Connection;
import java.sql.DriverManager;

public class BaseDAO {
	
	private String jndiname;
	public Connection conn;
	public JNDILocator db;

	public void setDBByTomcat() throws Exception {
		this.db = JNDILocator.getInstance(this.jndiname, 2);
	}

	public void setDBgetByIBM() throws Exception {
		this.db = JNDILocator.getInstance(this.jndiname, 1);
	}

	public void connect() {
		if (this.conn == null) {
			this.conn = this.db.getConnection();
		}
	}

	public void connect(String url, String driver, String user, String password)
			throws Exception {
		if (this.conn == null) {
			Class.forName(driver);
			this.conn = DriverManager.getConnection(url, user, password);
		}
	}

	public void close() throws Exception {
		if (this.conn != null) {
			this.conn.close();
			this.conn = null;
		}
	}

	public String getJndiname() {
		return this.jndiname;
	}

	public void setJndiname(String jndiname) {
		this.jndiname = jndiname;
	}

	public JNDILocator getDb() {
		return this.db;
	}

	public void setDb(JNDILocator db) {
		this.db = db;
	}
}
