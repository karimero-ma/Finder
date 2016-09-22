package jp.co.km.finder;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FindCommand {
	
	private static Logger log = LoggerFactory.getLogger(FindCommand.class);
	
	private String path;

	private String keyword;
	
	private String fileNameRule;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * 指定されたファイルパスの文字列が検索対象のファイルの場合にtrueを返す
	 * @param path
	 * @return
	 */
	public boolean accecpt(String path){
		if(StringUtils.isEmpty(fileNameRule)){
			log.debug("accept! {}. no specify file name rule.", path);
			return true;
		}
		
		int wildCardIdx = fileNameRule.indexOf("*");
		log.debug("wildCardIdx = {}", wildCardIdx);
		
		if(wildCardIdx < 0){
			//ワイルドカード指定なし
			return StringUtils.equals(path, fileNameRule);
		}
		
		//ワイルドカードありの場合は、ワイルドカード前後の文字列が一致するか確認する
		String left = StringUtils.left(fileNameRule
				, fileNameRule.substring(0, wildCardIdx).length());
		log.trace("left = {}", left);
		
		String right = StringUtils.right(fileNameRule, 
				fileNameRule.substring(wildCardIdx + 1).length());
		log.trace("right ={}", right);
		
		if(StringUtils.isNotEmpty(left) && !path.startsWith(left)){
			log.debug("no accept {}. bat was no starts with '{}'", path, left);
			return false;
		}
		
		if(StringUtils.isNotEmpty(right) && !path.endsWith(right)){
			log.debug("no accept {}. bat was no endst with '{}'", path, right);
			return false;
		}
		
		log.debug("accept! {}.", path);
		return true;
	}
	
	public boolean match(String line){
		return line.indexOf(keyword) >= 0;
	}

	public String getFileNameRule() {
		return fileNameRule;
	}

	public void setFileNameRule(String fileNameRule) {
		this.fileNameRule = fileNameRule;
	}
}
