package com.ponxu.run.db.wrap.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ponxu.run.db.wrap.RowWrapper;

/**
 * 
 * @author xwz
 */
public class LongRowWrapper implements RowWrapper<Long> {
	private static LongRowWrapper instance = new LongRowWrapper();

	public static LongRowWrapper getInstance() {
		return instance;
	}

	public Long wrap(ResultSet rs) throws SQLException {
		return Long.valueOf(rs.getLong(1));
	}
}