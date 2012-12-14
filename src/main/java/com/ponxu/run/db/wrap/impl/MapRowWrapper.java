package com.ponxu.run.db.wrap.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ponxu.run.db.wrap.RowWrapper;

/**
 * 
 * @author xwz
 */
public class MapRowWrapper implements RowWrapper<Map<String, String>> {
	private static MapRowWrapper instance = new MapRowWrapper();

	public static MapRowWrapper getInstance() {
		return instance;
	}

	public Map<String, String> wrap(ResultSet rs) throws SQLException {
		Map<String, String> row = new HashMap<String, String>();
		ResultSetMetaData metaData = rs.getMetaData();
		int colCount = metaData.getColumnCount();
		for (int i = 1; i <= colCount; i++) {
			String colName = metaData.getColumnName(i);
			row.put(colName.toLowerCase(), rs.getString(i));
		}
		return row;
	}
}