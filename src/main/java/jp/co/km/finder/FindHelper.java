package jp.co.km.finder;

import java.util.Arrays;
import java.util.Optional;

import jp.co.km.finder.FindCommand.SEARCH_OPTION;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FindHelper {

	private static Logger log = LoggerFactory.getLogger(FindHelper.class);
	
	private FindCommand fc;
	
	FindHelper(FindCommand fc){
		this.fc = fc;	
	}
	
	/**
	 * 指定されたファイルパスの文字列が検索対象のファイルの場合にtrueを返す
	 * @param path
	 * @return
	 */
	boolean accept(String path){
		String nameFilter = fc.getNameFilter();
		if(StringUtils.isEmpty(nameFilter)){
			log.debug("accept! {}. no specify file name rule.", path);
			return true;
		}
		
		int wildCardIdx = nameFilter.indexOf("*");
		log.trace("wildCardIdx = {}", wildCardIdx);
		
		if(wildCardIdx < 0){
			//ワイルドカード指定なし
			return StringUtils.equals(path, nameFilter);
		}
		
		//ワイルドカードありの場合は、ワイルドカード前後の文字列が一致するか確認する
		int leftLen = nameFilter.substring(0, wildCardIdx).length();
		String left = StringUtils.left(nameFilter, leftLen);
		log.trace("left = {}", left);
		
		int rightLen = nameFilter.substring(wildCardIdx + 1).length();
		String right = StringUtils.right(nameFilter, rightLen);
		log.trace("right ={}", right);
		
		if(StringUtils.isNotEmpty(left) && !path.startsWith(left)){
			log.debug("no accept {}. bat was no starts with '{}'", path, left);
			return false;
		}
		
		if(StringUtils.isNotEmpty(right) && !path.endsWith(right)){
			log.debug("no accept {}. bat was no ends with '{}'", path, right);
			return false;
		}
		
		log.debug("accept! {}.", path);
		return true;
	}
	
	/**
	 * 渡された line が pattern に一致するか否かを返す
	 * @param line
	 * @return
	 */
	boolean match(String line){
		if(fc.use(SEARCH_OPTION.REGEX)){
			log.trace("match logic : regex");
			return fc.getRegexPattern().matcher(line).find();
		}
		
		String pattern = fc.getPattern();
		
		if(fc.use(SEARCH_OPTION.WORD_UNIT)){
			Optional<String> optional = Arrays.asList(line.split(" ")).stream()
				.filter(splited-> equalsOfWorUnitd(splited, pattern))
				.findFirst();
			return optional.isPresent();
		}
		
		if(fc.use(SEARCH_OPTION.CASE_INSENSITIVE)){
			log.trace("match logic : case insensitive");
			return StringUtils.indexOfIgnoreCase(line, pattern) >= 0;
		}else{
			log.trace("match logic : case sensitve");
			return line.indexOf(pattern) >= 0;
		}
	}
	
	private boolean equalsOfWorUnitd(String word, String pattern){
		if(fc.use(SEARCH_OPTION.CASE_INSENSITIVE)){
			log.trace("match logic :word unit - case insensitive");
			return StringUtils.equalsIgnoreCase(word, pattern);
		}else{
			log.trace("match logic : word unit - sensitive");
			return StringUtils.equals(word, pattern);
		}
	}
}
