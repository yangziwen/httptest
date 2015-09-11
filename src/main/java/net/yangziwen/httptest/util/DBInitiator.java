package net.yangziwen.httptest.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DBInitiator {
	
	private static final File DB_FOLDER = new File(FilenameUtils.concat(SystemUtils.USER_DIR, "db"));
	
	private DBInitiator() {}

	@SuppressWarnings("resource")
	public static final void initDatabase() throws IOException {
		if(!DB_FOLDER.exists()) {
			DB_FOLDER.mkdirs();
		}
		Profiles.setProfileAsSystemProperty(Profiles.DEVELOPMENT_INIT);
		new ClassPathXmlApplicationContext("spring/applicationContext.xml");
	}
	
	public static final boolean existsDb() {
		if(!DB_FOLDER.exists()) {
			return false;
		}
		File dbFile = new File(FilenameUtils.concat(DB_FOLDER.getAbsolutePath(), "httptest.db"));
		return dbFile.exists();
	}
	
	public static void main(String[] args) throws IOException {
		initDatabase();
	}
	
}
