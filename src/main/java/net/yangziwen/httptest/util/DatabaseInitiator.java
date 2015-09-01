package net.yangziwen.httptest.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DatabaseInitiator {
	
	private DatabaseInitiator() {}

	@SuppressWarnings("resource")
	public static final void initDatabase() throws IOException {
		File dbFolder = new File(FilenameUtils.concat(SystemUtils.USER_DIR, "db"));
		if(!dbFolder.exists()) {
			dbFolder.mkdirs();
		}
		Profiles.setProfileAsSystemProperty(Profiles.DEVELOPMENT_INIT);
		new ClassPathXmlApplicationContext("spring/applicationContext.xml");
	}
	
	public static void main(String[] args) throws IOException {
		initDatabase();
	}
	
}
