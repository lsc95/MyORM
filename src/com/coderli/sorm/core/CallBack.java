package com.coderli.sorm.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface CallBack {
	public Object doExcute(Connection conn, PreparedStatement ps, ResultSet rs);
}
