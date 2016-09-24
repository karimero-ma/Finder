package jp.co.km.finder;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import jp.co.km.finder.FindCommand;
import jp.co.km.finder.FindCommand.SEARCH_OPTION;

import org.junit.Test;

public class FindCommandTest {

	@Test
	public void ファイルのマスク_後方一致_成功() {
		FindCommand fc = new FindCommand();
		fc.setNameFilter("*.csv");
		boolean actual = fc.accecpt("test.csv");
		assertThat(actual, is(true));
	}
	
	@Test
	public void ファイルのマスク_後方一致_不成功() {
		FindCommand fc = new FindCommand();
		fc.setNameFilter("*.csv");
		boolean actual = fc.accecpt("test.txt");
		assertThat(actual, is(false));
	}
	
	@Test
	public void ファイルのマスク_前後方一致_成功() {
		FindCommand fc = new FindCommand();
		fc.setNameFilter("te*.csv");
		boolean actual = fc.accecpt("test.csv");
		assertThat(actual, is(true));
	}
	
	@Test
	public void ファイルのマスク_前後方一致_不成功() {
		FindCommand fc = new FindCommand();
		fc.setNameFilter("te*.csv");
		boolean actual = fc.accecpt("test.txt");
		assertThat(actual, is(false));
	}

	@Test
	public void ファイルのマスク_前方一致_成功() {
		FindCommand fc = new FindCommand();
		fc.setNameFilter("test.*");
		boolean actual = fc.accecpt("test.csv");
		assertThat(actual, is(true));
	}
	
	@Test
	public void ファイルのマスク_前方一致_不成功() {
		FindCommand fc = new FindCommand();
		fc.setNameFilter("test.*");
		boolean actual = fc.accecpt("hoge.txt");
		assertThat(actual, is(false));
	}
	
	@Test
	public void 文字列検索_通常検索_先頭(){
		FindCommand fc = new FindCommand();
		fc.setPattern("hoge");
		boolean actual = fc.match("hoge1234");
		
		assertThat(actual, is(true));
	}
	
	@Test
	public void 文字列検索_通常検索_中央(){
		FindCommand fc = new FindCommand();
		fc.setPattern("hoge");
		boolean actual = fc.match("1234hoge5678");
		
		assertThat(actual, is(true));
	}
	
	@Test
	public void 文字列検索_通常検索_末尾(){
		FindCommand fc = new FindCommand();
		fc.setPattern("hoge");
		boolean actual = fc.match("1234hoge");
		
		assertThat(actual, is(true));
	}
	
	@Test
	public void 文字列検索_通常検索_大文字小文字を区別しない_不一致(){
		FindCommand fc = new FindCommand();
		fc.setPattern("hoge");
		boolean actual = fc.match("HOGE1234");
		
		assertThat(actual, is(false));
	}
	
	@Test
	public void 文字列検索_通常検索_大文字小文字を区別しない_一致(){
		FindCommand fc = new FindCommand();
		fc.setPattern("hoge");
		fc.add(SEARCH_OPTION.CASE_INSENSITIVE);
		boolean actual = fc.match("HOGE1234");
		
		assertThat(actual, is(true));
	}
	
	@Test
	public void 文字列検索_通常検索_一致しない(){
		FindCommand fc = new FindCommand();
		fc.setPattern("hoge");
		boolean actual = fc.match("12345678");
		
		assertThat(actual, is(false));
	}
	
	@Test
	public void 文字列検索_単語単位の検索_一致しない(){
		FindCommand fc = new FindCommand();
		fc.setPattern("hoge");
		fc.add(SEARCH_OPTION.WORD_UNIT);
		boolean actual = fc.match("1234hoge");
		
		assertThat(actual, is(false));
	}
	
	@Test
	public void 文字列検索_単語単位の検索_空文字なので一致しない(){
		FindCommand fc = new FindCommand();
		fc.setPattern("hoge");
		fc.add(SEARCH_OPTION.WORD_UNIT);
		boolean actual = fc.match("");
		
		assertThat(actual, is(false));
	}
	
	@Test
	public void 文字列検索_単語単位の検索_前方で一致する(){
		FindCommand fc = new FindCommand();
		fc.setPattern("hoge");
		fc.add(SEARCH_OPTION.WORD_UNIT);
		boolean actual = fc.match("hoge 1234");
		
		assertThat(actual, is(true));
	}
	
	@Test
	public void 文字列検索_単語単位の検索_中盤で一致する(){
		FindCommand fc = new FindCommand();
		fc.setPattern("hoge");
		fc.add(SEARCH_OPTION.WORD_UNIT);
		boolean actual = fc.match("1234 hoge 5678");
		
		assertThat(actual, is(true));
	}
	
	@Test
	public void 文字列検索_単語単位の検索_後方で一致する(){
		FindCommand fc = new FindCommand();
		fc.setPattern("hoge");
		fc.add(SEARCH_OPTION.WORD_UNIT);
		boolean actual = fc.match("1234 hoge");
		
		assertThat(actual, is(true));
	}
	
	@Test
	public void 文字列検索_正規表現_正規表現なしで一致しない(){
		FindCommand fc = new FindCommand();
		fc.setPattern("hog[e|a]");
		boolean actual = fc.match("1234 hoge");
		
		assertThat(actual, is(false));
	}
	
	@Test
	public void 文字列検索_正規表現_正規表現ありで一致する(){
		FindCommand fc = new FindCommand();
		fc.setPattern("hog[e|a]");
		fc.add(SEARCH_OPTION.REGEX);
		boolean actual = fc.match("1234 hoge");
		
		assertThat(actual, is(true));
	}
	
	@Test
	public void 文字列検索_正規表現_円マーク(){
		FindCommand fc = new FindCommand();
		fc.setPattern("hog¥¥[e|a]");
		fc.add(SEARCH_OPTION.REGEX);
		boolean actual = fc.match("1234 hog¥¥e");
		
		assertThat(actual, is(true));
	}
	
}
