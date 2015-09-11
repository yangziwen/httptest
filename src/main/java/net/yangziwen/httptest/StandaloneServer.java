package net.yangziwen.httptest;

import java.io.File;
import java.net.URL;
import java.security.ProtectionDomain;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class StandaloneServer {
	
	public static void main(String[] args) throws Exception {
		
		String contextPath = System.getProperty("contextpath", "/");
		int port = Integer.getInteger("port", 8989);

		Server server = createServer(contextPath, port);

		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(100);
		}
	}

	private static Server createServer(String contextPath, int port) {
		// use Eclipse JDT compiler
		System.setProperty("org.apache.jasper.compiler.disablejsr199", "true");

		Server server = new Server(port);
		server.setStopAtShutdown(true);

		ProtectionDomain protectionDomain = Server.class.getProtectionDomain();
		URL location = protectionDomain.getCodeSource().getLocation();

		String warFile = location.toExternalForm();
		WebAppContext context = new WebAppContext(warFile, contextPath);
		context.setServer(server);

		// 设置work dir,war包将解压到该目录，jsp编译后的文件也将放入其中。
		String currentDir = new File(location.getPath()).getParent();
		File workDir = new File(currentDir, "work");
		context.setTempDirectory(workDir);

		server.setHandler(context);
		return server;
	}
	
}
