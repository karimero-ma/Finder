package finder;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import jp.co.km.finder.FindCommand;

import org.junit.Test;

public class FindCommandTest {

	@Test
	public void ファイルのマスク_後方一致_成功() {
		FindCommand fc = new FindCommand();
		fc.setFileNameRule("*.csv");
		boolean actual = fc.accecpt("test.csv");
		assertThat(actual, is(true));
	}
	
	@Test
	public void ファイルのマスク_後方一致_不成功() {
		FindCommand fc = new FindCommand();
		fc.setFileNameRule("*.csv");
		boolean actual = fc.accecpt("test.txt");
		assertThat(actual, is(false));
	}
	
	@Test
	public void ファイルのマスク_前後方一致_成功() {
		FindCommand fc = new FindCommand();
		fc.setFileNameRule("te*.csv");
		boolean actual = fc.accecpt("test.csv");
		assertThat(actual, is(true));
	}
	
	@Test
	public void ファイルのマスク_前後方一致_不成功() {
		FindCommand fc = new FindCommand();
		fc.setFileNameRule("te*.csv");
		boolean actual = fc.accecpt("test.txt");
		assertThat(actual, is(false));
	}

	@Test
	public void ファイルのマスク_前方一致_成功() {
		FindCommand fc = new FindCommand();
		fc.setFileNameRule("test.*");
		boolean actual = fc.accecpt("test.csv");
		assertThat(actual, is(true));
	}
	
	@Test
	public void ファイルのマスク_前方一致_不成功() {
		FindCommand fc = new FindCommand();
		fc.setFileNameRule("test.*");
		boolean actual = fc.accecpt("hoge.txt");
		assertThat(actual, is(false));
	}
}
