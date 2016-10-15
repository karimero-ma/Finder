package jp.co.km.finder;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

/**
 * 検索した結果をCSVファイルに加工して出力する
 *
 */
public class CsvExporterImpl implements Exporter {

	@Override
	public void execute(List<Result> src, Writer writer) {
		src.stream()
			.forEach(r->{
				List<String> columns = new ArrayList<>();
				columns.add(StringEscapeUtils.escapeCsv(r.getAbsolutePath()));
				columns.add(StringEscapeUtils.escapeCsv(r.getPath().getFileName().toString()));
				columns.add(StringEscapeUtils.escapeCsv(Long.toString(r.getNo())));
				columns.add(StringEscapeUtils.escapeCsv(r.getText().trim()));
				try {
					writer.write(StringUtils.join(columns, ","));
					writer.write(System.getProperty(SystemUtils.LINE_SEPARATOR));
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
		});
	}
}
