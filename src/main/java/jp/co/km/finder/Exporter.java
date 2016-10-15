package jp.co.km.finder;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public interface Exporter {

	public void execute(List<Result> src, Writer writer) throws IOException;
	
}
