package jp.co.km.finder;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static jp.co.km.finder.FindCommand.SEARCH_OPTION.*;

import org.junit.Test;

public class ArgumentsTest {

	@Test
	public void 最少引数() {
		Arguments a = new Arguments();
		a.populate(new String[]{"hoge", "hogehoge"});
		FindCommand f = a.toFindCommand();
		assertThat(f.getPattern(), is("hoge"));
		assertThat(f.getPath(), is("hogehoge"));
		assertThat(f.getNameFilter(), isEmptyOrNullString());
		assertThat(f.use(REGEX), is(false));
		assertThat(f.use(WORD_UNIT), is(false));
		assertThat(f.use(CASE_INSENSITIVE), is(false));
	}

	@Test
	public void 正規表現を指定() {
		Arguments a = new Arguments();
		a.populate(new String[]{"-x", "hoge", "hogehoge"});
		FindCommand f = a.toFindCommand();
		assertThat(f.getPattern(), is("hoge"));
		assertThat(f.getPath(), is("hogehoge"));
		assertThat(f.use(REGEX), is(true));
		assertThat(f.use(WORD_UNIT), is(false));
		assertThat(f.use(CASE_INSENSITIVE), is(false));
	}
	
	@Test
	public void 単語単位を指定() {
		Arguments a = new Arguments();
		a.populate(new String[]{"-w", "hoge", "hogehoge"});
		FindCommand f = a.toFindCommand();
		assertThat(f.getPattern(), is("hoge"));
		assertThat(f.getPath(), is("hogehoge"));
		assertThat(f.use(REGEX), is(false));
		assertThat(f.use(WORD_UNIT), is(true));
		assertThat(f.use(CASE_INSENSITIVE), is(false));
	}
	
	@Test
	public void 大文字小文字無視を指定() {
		Arguments a = new Arguments();
		a.populate(new String[]{"-i", "hoge", "hogehoge"});
		FindCommand f = a.toFindCommand();
		assertThat(f.getPattern(), is("hoge"));
		assertThat(f.getPath(), is("hogehoge"));
		assertThat(f.use(REGEX), is(false));
		assertThat(f.use(WORD_UNIT), is(false));
		assertThat(f.use(CASE_INSENSITIVE), is(true));
	}
	
	@Test
	public void ファイル名パターンを指定() {
		Arguments a = new Arguments();
		a.populate(new String[]{"-f", "*.hoge", "hoge", "hogehoge"});
		FindCommand f = a.toFindCommand();
		assertThat(f.getPattern(), is("hoge"));
		assertThat(f.getPath(), is("hogehoge"));
		assertThat(f.getNameFilter(), is("*.hoge"));
		
		assertThat(f.use(REGEX), is(false));
		assertThat(f.use(WORD_UNIT), is(false));
		assertThat(f.use(CASE_INSENSITIVE), is(false));
	}
	
	@Test
	public void ファイル名パターンを指定_値なし() {
		Arguments a = new Arguments();
		a.populate(new String[]{"-f", "hoge", "hogehoge"});
		assertThat(a.arrow(), is(false));
	}
}
