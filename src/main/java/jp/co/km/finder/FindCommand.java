package jp.co.km.finder;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * ファイルの検索条件を表現するクラス
 *
 */
public class FindCommand {
	
//	private static Logger log = LoggerFactory.getLogger(FindCommand.class);

	/**
	 * 検索時のオプション
	 */
	public enum SEARCH_OPTION{
		/** 正規表現で検索*/
		REGEX
		/** 単語単位で検索*/
		, WORD_UNIT 
		/** 大文字、小文字を区別する */
		, CASE_INSENSITIVE
	}

	/** 検索する文字列 */
	private String pattern;
	
	/** 
	 * 検索するフォルダ
	 */
	private Path path;
	
	/**
	 * サブディレクトリも対象とするか否か
	 */
	private boolean isRecursive = false;

	/** 
	 * ファイル名のフィルター 
	 * ワイルドカードが使用できる(*)。複数の指定は,(半角カンマ)で区切ること。
	 * 
	 * TODO：複数指定対応
	 */
	private String nameFilter;

	/** 指定された検索オプション */
	private Set<SEARCH_OPTION> searchOptions = new HashSet<>();
	
	private Pattern regexPattern;
	
	private FindHelper helper;
	
	public FindCommand(){
		helper = new FindHelper(this);
	}
	
	/**
	 * 指定されたファイルパスの文字列が検索対象のファイルの場合にtrueを返す
	 * @param path
	 * @return
	 */
	public boolean accecpt(String path){
		return helper.accept(path);
	}
	
	/**
	 * 渡された line が pattern に一致するか否かを返す
	 * @param line
	 * @return
	 */
	public boolean match(String line){
		return helper.match(line);
	}

	/**
	 * 検索時のオプションを有効にする
	 * @param option
	 * @return
	 */
	public FindCommand add(SEARCH_OPTION option) {
		this.searchOptions.add(option);
		return this;
	}
	
	/**
	 * 指定されたオプションを使用するか否かを返す
	 * @param option
	 * @return
	 */
	public boolean use(SEARCH_OPTION option){
		return searchOptions.contains(option);
	}
	
	/* ---- getter setter ---- */
	
	public String getPattern() {
		return pattern;
	}

	public FindCommand setPattern(String keyword) {
		this.pattern = keyword;
		return this;
	}

	public Path getPath() {
		return path;
	}

	public FindCommand setPath(String path) {
		FileSystem fs = FileSystems.getDefault();
		this.path = fs.getPath(path);
		return this;
	}
	public String getNameFilter() {
		return nameFilter;
	}

	public FindCommand setNameFilter(String nameFilter) {
		this.nameFilter = nameFilter;
		return this;
	}

	/**
	 * サブフォルダーを検索するか否かを返す
	 * @return
	 */
	public boolean isRecursive() {
		return isRecursive;
	}

	/**
	 * サブフォルダーを検索するか否かを設定する
	 * @return
	 */
	public FindCommand setRecursive(boolean isRecursive) {
		this.isRecursive = isRecursive;
		return this;
	}
	
	public Pattern getRegexPattern() {
		if(regexPattern == null){
			regexPattern = Pattern.compile(pattern);
		}
		return regexPattern;
	}

	@Override
	public String toString() {
		return "FindCommand [pattern=" + pattern + ", path=" + path
				+ ", isRecursive=" + isRecursive + ", nameFilter=" + nameFilter
				+ ", searchOptions=" + searchOptions + "]";
	}
}
