package jp.co.km.finder;

import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import jp.co.km.io.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Finder {

	private static Logger log = LoggerFactory.getLogger(Finder.class);

	public static void main(String[] args) {
		new Finder().find(args);
	}

	private void find(String[] args) {
		FindCommand fc = new FindCommand();
		fc.setPath(args[0]);
		fc.setKeyword("margin");
		fc.setFileNameRule("*.css");

		List<Path> paths = null;
		try {
			paths = FileUtils.createFileList(fc);
		} catch (IOException e) {
			log.error("ファイルリストの取得に失敗", e);
			System.exit(-1);
		}
		log.info("ファイル候補の取得終了");

		if (paths.isEmpty()) {
			log.info("対象ファイルがありません");
			System.exit(0);
		}

		if (log.isDebugEnabled()) {
			paths.stream().forEach(p -> log.debug(p.toString()));
		}

		log.info("一致する文字列の検索");
		List<Result> results = phaseFindText(fc, paths);

		log.info("----------- 結果の出力 -----------");
		results.stream()
			.filter(r -> !r.getLines().isEmpty())
			.forEach(r -> printResult(r));
	}

	private void printResult(Result r) {
		r.getLines().stream()
			.forEach(l -> log.info("{} : {}", l.getNo(), l.getText()));
	}

	private List<Result> phaseFindText(FindCommand fc, List<Path> paths) {
		return paths.stream()
			.map(path -> parse(fc, path))
			.collect(Collectors.toList());
	}

	private Result parse(FindCommand fc, Path path) {
		Result r = new Result();
		r.setPath(path);

		log.debug("ファイルの読み込み開始 {}", path);
		try (LineNumberReader reader = new LineNumberReader(
				Files.newBufferedReader(path, Charset.forName("UTF-8")))) {
			List<Result.Line> lines = reader.lines()
				.filter(line -> fc.match(line))
				.map(line -> Result.newLine(reader.getLineNumber(), line))
				.collect(Collectors.toList());
			r.setLines(lines);
		} catch (IOException e) {
			log.error("ファイルの解析に失敗しました １{}", path, e);
			e.printStackTrace();
		}

		return r;
	}
}
