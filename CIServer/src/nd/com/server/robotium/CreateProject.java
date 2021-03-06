package nd.com.server.robotium;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import nd.com.server.FileUtil;
import nd.com.server.Util;

public class CreateProject {
	FileUtil fileUtil = new FileUtil();

	public void createTestProject(String projectName, String packageName) {
		String projectPath = Util.rootPath + projectName;
		fileUtil.deleteDir(new File(projectPath));
		createAndroidProject("test", "android-19", projectPath, packageName);
		updateProject(projectPath, packageName);
		fileUtil.copyFile(System.getProperty("user.dir")
				+ "/files/robotium.jar", projectPath + "/libs/robotium.jar");
		fileUtil.copyFile(System.getProperty("user.dir")
				+ "/files/android-junit-report.jar", projectPath
				+ "/libs/android-junit-report.jar");
	}

	private void execCmd(String cmd) {
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			InputStream is = p.getErrorStream();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is,"GBK"));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			p.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createAndroidProject(String name, String target, String path,
			String packageName) {
		String cmd = String
				.format("cmd /c android create project -n %s -t %s -p %s -k %s.test -a MainActivityTest",
						name, target, path, packageName);
		System.out.println(cmd);
		execCmd(cmd);
	}

	private void updateProject(String projectPath, String packageName) {
		System.out.println(projectPath + "/AndroidManifest.xml");
		fileUtil.writeFile(new File(projectPath + "/AndroidManifest.xml"),
				androidManifast(packageName));
		String path = packageName.replaceAll("\\.", "/");
		fileUtil.deleteFile(projectPath + "/src/" + path
				+ "/test/MainActivityTest.java");
	}

	public String androidManifast(String packageName) {
		String xml = String
				.format("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
						+ "<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\"\n"
						+ "    package=\"%s.test\"\n"
						+ "    android:versionCode=\"1\"\n"
						+ "    android:versionName=\"1.0\" >\n\n"
						+ "    <uses-permission android:name=\"android.permission.WRITE_EXTERNAL_STORAGE\" />\n\n"
						+ "    <uses-sdk android:minSdkVersion=\"8\" />\n\n"
						+ "    <instrumentation\n"
						+ "        android:name=\"com.zutubi.android.junitreport.JUnitReportTestRunner\"\n"
						+ "        android:targetPackage=\"%s\" />\n\n"
						+ "    <application android:label=\"%s.test\" >\n"
						+ "        <uses-library android:name=\"android.test.runner\" />\n"
						+ "    </application>\n\n" + "</manifest>",
						packageName, packageName, packageName);
		return xml;
	}
}
