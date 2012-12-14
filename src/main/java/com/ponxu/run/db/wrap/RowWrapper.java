package com.ponxu.run.db.wrap;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author xwz
 */
public abstract interface RowWrapper<T> {
	public abstract T wrap(ResultSet rs) throws SQLException;
}