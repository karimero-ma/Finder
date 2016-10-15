package jp.co.km.finder;

import java.io.Serializable;
import java.nio.file.Path;

import org.apache.commons.lang.StringUtils;

public class Result implements Serializable {

	private static final long serialVersionUID = 1L;

	private Path path;
	
	private long no;
	
	private String text;
	
	private boolean isSuccess = false;

	public Path getPath() {
		return path;
	}

	public String getAbsolutePath() {
		if(path == null){
			return StringUtils.EMPTY;
		}
		
		return path.toFile().getAbsolutePath();
	}
	
	public Result setPath(Path path) {
		this.path = path;
		return this;
	}
		
	public long getNo() {
		return no;
	}
	
	public Result setNo(long no) {
		this.no = no;
		return this;
	}
		
	public String getText() {
		return text;
	}
	
	public Result setText(String text) {
		this.text = text;
		return this;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
}
