package jp.co.km.finder;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FinderCli {

	private static Logger log = LoggerFactory.getLogger(FinderCli.class);

	public static void main(String[] args) {
		Arguments a = new Arguments(args);
		
		if(!a.arrow()){
			a.printHelp();
			return;
		}
		
		List<Result> results = new Finder().find(a.toFindCommand());
		
		log.info("----------- 結果の出力 -----------");
		;
		results.stream()
			.filter(r -> r.isSuccess())
			.forEach(r->{
				StringBuilder resultsBuf = new StringBuilder();
				resultsBuf.append(r.getPath());
				resultsBuf.append(System.getProperty("line.separator"));
				resultsBuf.append(String.format("%s : %s%s", r.getNo(), r.getText().trim(), System.getProperty("line.separator")));
				System.out.println(resultsBuf);
			}
		);
	}
	
	

	
}