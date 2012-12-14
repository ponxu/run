package com.ponxu.run.db;

import java.sql.Connection;

/**
 * 
 * @author xwz
 */
public interface Atom {
	public void run(Connection transcationConnection);
}
