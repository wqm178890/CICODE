package nd.com.server;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

public class FileZip {

	private File zipFile;

	public FileZip(String pathName) {
		zipFile = new File(pathName);
	}

	public void compress(String srcPathName, String includesName) {
		File srcdir = new File(srcPathName);
		if (!srcdir.exists())
			throw new RuntimeException(srcPathName + "不存在！");

		Project prj = new Project();
		Zip zip = new Zip();
		zip.setProject(prj);
		zip.setDestFile(zipFile);
		FileSet fileSet = new FileSet();
		fileSet.setProject(prj);
		fileSet.setDir(srcdir);
		fileSet.setIncludes("*"+includesName+"*/"); //包括哪些文件或文件夹
		// eg:zip.setIncludes("*.java");
		fileSet.setExcludes("*.zip"); //排除哪些文件或文件夹
		zip.addFileset(fileSet);

		zip.execute();
	}
}
