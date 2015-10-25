package net.yangziwen.httptest.util;

/**
 * Spring profile 常用方法与profile名称。
 * 
 * @author calvin
 */
public class Profiles {
	
	private Profiles() {}

	public static final String ACTIVE_PROFILE = "spring.profiles.active";
	public static final String DEFAULT_PROFILE = "spring.profiles.default";

	public static final String PROD = "prod";
	public static final String DEV = "dev";
	public static final String DEV_INIT = "dev-init";
	public static final String UNIT_TEST = "test";
	public static final String FUNC_TEST = "func-test";

	/**
	 * 在Spring启动前，设置profile的环境变量。
	 */
	public static void setProfileAsSystemProperty(String profile) {
		System.setProperty(ACTIVE_PROFILE, profile);
	}
}
