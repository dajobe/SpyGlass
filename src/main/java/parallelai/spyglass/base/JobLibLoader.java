package parallelai.spyglass.base;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileStatus;
//import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.fs.RemoteIterator;

public class JobLibLoader {

	private static Logger logger = LogManager.getLogger(JobLibLoader.class);
	
	public static void loadJars(String libPathStr, Configuration config) {
		

		try {
			Path libPath = new Path(libPathStr);

			FileSystem fs = FileSystem.get(config);

			FileStatus[] entries = fs.listStatus(libPath);

			for(FileStatus e: entries) {
                                Path entryPath = e.getPath();
                                if (!e.isDir() && entryPath.getName().endsWith("jar")) {
					logger.info("Loading Jar : " + entryPath.getName());
					DistributedCache.addFileToClassPath(entryPath, config);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
	}

	public static void addFiletoCache(String libPathStr, Configuration config) {

		try {
			Path filePath = new Path(libPathStr);
			DistributedCache.addCacheFile(filePath.toUri(), config);
			// DistributedCache.createSymlink(config);

			// config.set("mapred.cache.files", libPathStr);
			// config.set("mapred.create.symlink", "yes");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Path[] getFileFromCache(String libPathStr,
			Configuration config) {
		Path[] localFiles = null;
		try {
			logger.info("Local Cache => " + DistributedCache.getLocalCacheFiles(config));
			logger.info("Hadoop Cache => "+ DistributedCache.getCacheFiles(config));
			if (DistributedCache.getLocalCacheFiles(config) != null) {
				localFiles = DistributedCache.getLocalCacheFiles(config);
			}
			logger.info("LocalFiles => " + localFiles);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return localFiles;
	}
}
