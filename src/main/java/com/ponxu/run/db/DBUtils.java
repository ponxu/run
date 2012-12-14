package com.ponxu.run.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ponxu.run.db.wrap.RowWrapper;
import com.ponxu.run.lang.ExceptionUtils;
import com.ponxu.run.log.Log;
import com.ponxu.run.log.LogFactory;

/**
 * 数据库操作类
 * 
 * @author xwz
 */
public class DBUtils {
	private static final Log LOG = LogFactory.getLog();

	public static void loadDriver(String dirver) {
		try {
			Class.forName(dirver);
		} catch (ClassNotFoundException e) {
			LOG.error("can not load driver %s", e, dirver);
		}
	}

	public static Connection getConnection(String url, String user,
			String password) {
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			LOG.error("%s, %s, %s", e, url, user, password);
		}
		return null;
	}

	// -------------------------------------------------------------
	public static <T> List<T> query(Connection conn, RowWrapper<T> wrapper,
			String sql, Object... values) {
		List<T> list = new ArrayList<T>();
		try {
			PreparedStatement pstmt = prepareStatement(conn, sql);
			setPreparedStatement(pstmt, values);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(wrapper.wrap(rs));
			}

			close(rs);
			close(pstmt);
		} catch (SQLException e) {
			ExceptionUtils.makeRuntime(e);
		}
		return list;
	}

	public static int execute(Connection conn, String sql, Object... values) {
		try {
			PreparedStatement pstmt = prepareStatement(conn, sql);
			setPreparedStatement(pstmt, values);
			int r = pstmt.executeUpdate();
			// 插入语句, 返回自增ID
			if (sql.toLowerCase().trim().startsWith("insert ")) {
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					r = rs.getInt(1);
				}
			}
			close(pstmt);
			return r;
		} catch (SQLException e) {
			ExceptionUtils.makeRuntime(e);
		}
		return -1;
	}

	public void executeTranscation(Connection conn, Atom atom) {
		try {
			boolean oldIsAuotCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);

			atom.run(conn);

			conn.commit();
			conn.setAutoCommit(oldIsAuotCommit);
		} catch (SQLException e) {
			ExceptionUtils.makeRuntime(e);
		}
	}

	// -------------------------------------------------------------
	private static PreparedStatement prepareStatement(Connection conn,
			String sql) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		return pstmt;
	}

	private static void setPreparedStatement(PreparedStatement pstmt,
			Object... values) throws SQLException {
		if (values == null)
			return;
		for (int i = 0; i < values.length; i++)
			pstmt.setObject(i + 1, values[i]);
	}

	// -------------------------------------------------------------
	public static void beginTranscation(Connection conn) {
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			// quiet
		}
	}

	public static void commit(Connection conn) {
		try {
			conn.commit();
		} catch (SQLException e) {
			// quiet
		}
	}

	public static void close(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			// quiet
		}
	}

	public static void commitAndClose(Connection conn) {
		commit(conn);
		close(conn);
	}

	public static void close(ResultSet rs) {
		try {
			rs.close();
		} catch (SQLException e) {
			// quiet
		}
	}

	public static void close(Statement stmt) {
		try {
			stmt.close();
		} catch (SQLException e) {
			// quiet
		}
	}

	public static void close(PreparedStatement pstmt) {
		try {
			pstmt.close();
		} catch (SQLException e) {
			// quiet
		}
	}
}
