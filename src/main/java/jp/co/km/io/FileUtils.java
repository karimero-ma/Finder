package jp.co.km.io;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import jp.co.km.finder.FindCommand;

public class FileUtils {

	public static List<Path> createFileList(FindCommand fc) throws IOException{
		
		FileSystem fs = FileSystems.getDefault();
		Path findPath = fs.getPath(fc.getPath());

		try {
			return Files.list(findPath)
					.filter(s->fc.accecpt(s.toAbsolutePath().toString()))
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
}
