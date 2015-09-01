package net.yangziwen.httptest.dao.base;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

public class VarArgsSQLFunc {
	
	private static final VarArgsSQLFunc MYSQL_CONCAT_FUNC = new VarArgsSQLFunc("concat(", ",", ")");
	private static final VarArgsSQLFunc DEFAULT_CONCAT_FUNC = new VarArgsSQLFunc("(", "||", ")");
	
	private final String begin;
	private final String sep;
	private final String end;
	
	public VarArgsSQLFunc(String begin, String sep, String end) {
		this.begin = begin;
		this.sep = sep;
		this.end = end;
	}
	
	public String render(String... args) {
		return render(Arrays.asList(args));
	}
	
	public String render(List<String> args) {
		StringBuilder buf = new StringBuilder().append(begin);
		for (int i = 0; i < args.size(); i++) {
			buf.append(transformArgument(args.get(i)));
			if (i < args.size() - 1) {
				buf.append(sep);
			}
		}
		return buf.append(end).toString();
	}
	
	protected String transformArgument(String argument) {
		return argument;
	}
	
	public static VarArgsSQLFunc getConcatFunc(DataSource dataSource) {
		String jdbcUrl = getJdbcUrlFromDataSource(dataSource);
		if(jdbcUrl.contains(":mysql:")) {
			return MYSQL_CONCAT_FUNC;
		}
		return DEFAULT_CONCAT_FUNC;
	}
	
	private static String getJdbcUrlFromDataSource(DataSource dataSource) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			if (connection == null) {
				throw new IllegalStateException("Connection returned by DataSource [" + dataSource + "] was null");
			}
			return connection.getMetaData().getURL();
		} catch (SQLException e) {
			throw new RuntimeException("Could not get database url", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
		}
	}

}