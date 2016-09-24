package jp.co.km.finder;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文字列を検索するファイルのリストを取得する
 *
 */
public class FileVisitorImpl extends SimpleFileVisitor<Path> {
	
	private static Logger log = LoggerFactory.getLogger(FileVisitorImpl.class);
	
	private List<Path> acceptFiles;
	
	private FindCommand fc;
	
	/**
	 * 外部からのインスタンス化を禁止する
	 */
	private FileVisitorImpl(FindCommand fc){
		this.fc = fc;
		this.acceptFiles = new ArrayList<Path>();
	}
	
	/**
	 * ファイルのリストを作成する
	 * @param fc
	 * @return ファイルのリスト。
	 * @throws IOException
	 */
	public static List<Path> createFileList(FindCommand fc) throws IOException{
		FileSystem fs = FileSystems.getDefault();
		Path startPath = fs.getPath(fc.getPath());

		FileVisitorImpl visitor = new FileVisitorImpl(fc);

		if(fc.isRecursive()){
			Files.walkFileTree(startPath, visitor);
		}else{
			Set<FileVisitOption> options = EnumSet.allOf(FileVisitOption.class);
			int maxDepth = 1;
			Files.walkFileTree(startPath, options, maxDepth, visitor);
		}
		log.debug("accept files {}", visitor.getAcceptFiles());		
		return visitor.getAcceptFiles();
	}

	@Override
	/**
	 * ファイル名が指定されたフィルター文字列に適合するか判定する処理を追加するためオーバーライドする
	 */
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		if(!file.toFile().isDirectory()
				&& fc.accecpt(file.getFileName().toString())){
			acceptFiles.add(file);
		}
		return FileVisitResult.CONTINUE;
	}

	public List<Path> getAcceptFiles() {
		return acceptFiles;
	}
}
