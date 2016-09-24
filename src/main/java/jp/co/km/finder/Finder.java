package jp.co.km.finder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.mozilla.universalchardet.UniversalDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Finder {

	private static Logger log = LoggerFactory.getLogger(Finder.class);

	public void find(FindCommand fc) {
		List<Path> paths = null;
		try {
			paths = FileVisitorImpl.createFileList(fc);
		} catch (IOException e) {
			log.error("ファイルリストの取得に失敗", e);
			return;
		}
		log.info("ファイル候補の取得終了");

		if (paths.isEmpty()) {
			log.info("対象ファイルがありません");
			return;
		}

		if (log.isDebugEnabled()) {
			paths.stream().forEach(p -> log.debug(p.toString()));
		}

		log.info("一致する文字列の検索");
		List<Result> results = phaseFindText(fc, paths);

		log.info("----------- 結果の出力 -----------");
		results.stream().filter(r -> r.isSuccess() && !r.getLines().isEmpty())
				.forEach(r -> printResult(r));
	}

	private void printResult(Result r) {
		r.getLines().stream()
				.forEach(l -> log.info("{} : {}", l.getNo(), l.getText()));
	}

	private List<Result> phaseFindText(FindCommand fc, List<Path> paths) {
		return paths.stream().map(path -> parse(fc, path))
				.collect(Collectors.toList());
	}

	private Result parse(FindCommand fc, Path path) {
		Result r = new Result();
		r.setPath(path);

		String charsetName = null;
		try {
			charsetName = getCharset(path);
		} catch (IOException e) {
			log.error("faild to retrive a charset {}", path);
			return r;
		}
		log.debug("ファイルの読み込み開始 {}", path);
		try (LineNumberReader reader = new LineNumberReader(
				Files.newBufferedReader(path, Charset.forName(charsetName)))) {
			List<Result.Line> lines = reader.lines()
					.filter(line -> fc.match(line))
					.map(line -> Result.newLine(reader.getLineNumber(), line))
					.collect(Collectors.toList());
			r.setLines(lines);
		} catch (IOException e) {
			log.error("ファイルの解析に失敗しました １{}", path, e);
			e.printStackTrace();
		}

		r.setSuccess(true);
		return r;
	}

	public String getCharset(Path path) throws IOException {
		String result = null;
		byte[] buf = new byte[4096];
		try (FileInputStream fis = new FileInputStream(path.toFile())) {
			UniversalDetector detector = new UniversalDetector(null);

			int nread;
			while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
				detector.handleData(buf, 0, nread);
			}
			detector.dataEnd();

			result = detector.getDetectedCharset();
			detector.reset();
		}
		log.debug("{} : charset = {}", path.getFileName(), result);

		if(result == null) result = "MS932"; //TODO:暫定対応
		return result;
	}
}
