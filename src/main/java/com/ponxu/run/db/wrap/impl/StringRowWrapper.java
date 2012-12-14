package com.ponxu.run.db.wrap.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ponxu.run.db.wrap.RowWrapper;

/**
 * 
 * @author xwz
 */
public class StringRowWrapper implements RowWrapper<String> {
	private static StringRowWrapper instance = new StringRowWrapper();

	public static StringRowWrapper getInstance() {
		return instance;
	}

	public String wrap(ResultSet rs) throws SQLException {
		return rs.getString(1);
	}
}