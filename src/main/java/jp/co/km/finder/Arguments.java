package jp.co.km.finder;

import jp.co.km.finder.FindCommand.SEARCH_OPTION;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 起動時引数を解析する。
 * また<code>toFindCommand()</code>で引数を<code>FindCommand</code>に変換した結果を取得できる
 *
 */
public class Arguments {

	private static Logger log = LoggerFactory.getLogger(Arguments.class);
	private CommandLine cl;
	
	private boolean arrow;
	
	private static Options opts = new Options();
	static{
		opts.addOption(Option.builder("f").argName("--filter").hasArg().desc("検索するファイル名のパターン (* でワイルドカード指定)").build())
			.addOption(Option.builder("r").argName("--recursive").desc("サブディレクトリも検索する").build())
			.addOption(Option.builder("i").argName("--case insensitive").desc("検索するフォルダー").build())
			.addOption(Option.builder("x").argName("--regex").desc("正規表現を使用").build())
			.addOption(Option.builder("w").argName("--word unit").desc("単語単位で検索").build());
	}

	/**
	 * 起動時の引数を解析する。解析が成功すると、<code>arrow()</code>の戻り値がtrueとなり検索が実行される。
	 * 引数の解析に失敗したり、ヘルプメニューが要求されていると標準出力にヘルプを表示して処理を終了する。
	 * @param args 起動時の引数
	 */
	public void populate(String[] args){
		log.debug("args = {}", String.join(",", args));
		DefaultParser parser = new DefaultParser();
		try {
			cl = parser.parse(opts, args);
			if(cl.hasOption("-h")){
				printHelp();
				return;
			}
			
			final int minArgs = 2; // patterh + path
			if(cl.getArgs().length < minArgs){
				throw new ParseException("");
			}
		} catch (ParseException e) {
			return;
		}
		arrow = true;
		return;
	}
	
	/**
	 * <code>populate()</code>で解析された結果を<code>FindCommand</code>に変換した結果を返す
	 * @return
	 */
	public FindCommand toFindCommand() {
		FindCommand fc = new FindCommand();

		log.debug("cl.getArgs() = {}", String.join(",", cl.getArgs()));
		String pattern = cl.getArgs()[0];
		fc.setPattern(pattern);
		
		String path = cl.getArgs()[1];
		fc.setPath(path);
		
		fc.setNameFilter(cl.getOptionValue("f"));
		
		fc.setRecursive(cl.hasOption("-r"));

		if (cl.hasOption("i")) {
			fc.add(SEARCH_OPTION.CASE_INSENSITIVE);
		}
		if (cl.hasOption("x")) {
			fc.add(SEARCH_OPTION.REGEX);
		}
		if (cl.hasOption("w")) {
			fc.add(SEARCH_OPTION.WORD_UNIT);
		}
		
		log.debug("FileCommand.toString = {}", fc.toString());
		return fc;
	}
	
	public void printHelp(){
		HelpFormatter help = new HelpFormatter();
		help.printHelp("finder.jar [options] <pattern> <path>", opts);
	}
	
	public boolean arrow(){
		return arrow;
	}
}
