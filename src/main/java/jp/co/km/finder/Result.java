package jp.co.km.finder;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Result implements Serializable {

	private static final long serialVersionUID = 1L;

	private Path path;
	
	private List<Line> lines = new ArrayList<Line>();

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public List<Line> getLines() {
		return lines;
	}

	public void setLines(List<Line> lines) {
		this.lines = lines;
	}

	public static Line newLine(long no, String text){
		return new Line(no, text);
	}
	
	static class Line{
		Line(long no, String text){
			this.no = no;
			this.text = text;
		}
		
		private long no;
		
		private String text;

		public long getNo() {
			return no;
		}
		
		void setNo(long no) {
			this.no = no;
		}
		
		public String getText() {
			return text;
		}
		
		void setText(String text) {
			this.text = text;
		}
	}
}
