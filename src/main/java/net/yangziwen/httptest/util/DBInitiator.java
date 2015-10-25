package net.yangziwen.httptest.util;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

/**
 * 如果不存在sqlite数据库文件，则自动生成并初始化
 * 也可以调用 mvn compile exec:exec -Pinit-db手动生成数据库文件
 */
@Component
public class DBInitiator implements ApplicationListener<ContextRefreshedEvent>, ServletContextListener {
	
	private static final File DB_FOLDER = new File(FilenameUtils.concat(SystemUtils.USER_DIR, "db"));
	
	public static final File DB_FILE = new File(FilenameUtils.concat(DB_FOLDER.getAbsolutePath(), "httptest.db"));
	
	private static boolean needToInitDb = false;
	
	public static boolean createDbFileIfNotExist() {
		if(DB_FILE.exists()) {
			return false;
		}
		try {
			FileUtils.touch(DB_FILE);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static final void initDatabase(DataSource dataSource) {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("sql/sqlite/schema.sql"));
		DatabasePopulatorUtils.execute(populator, dataSource);
	}
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		needToInitDb = createDbFileIfNotExist();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {}
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(event.getApplicationContext().getParent() != null) {
			return;
		}
		doAfterRootApplicationContextInitialized(event);
	}
	
	public void doAfterRootApplicationContextInitialized(ContextRefreshedEvent event) {
		if(needToInitDb) {
			DataSource dataSource = event.getApplicationContext().getBean(DataSource.class);
			DBInitiator.initDatabase(dataSource);
			needToInitDb = false;
		}
	}
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		createDbFileIfNotExist();
		Profiles.setProfileAsSystemProperty(Profiles.DEV_INIT);
		new ClassPathXmlApplicationContext("spring/applicationContext.xml");
	}

}
