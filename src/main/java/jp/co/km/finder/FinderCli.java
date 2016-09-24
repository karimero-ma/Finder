package jp.co.km.finder;


public class FinderCli {

//	private static Logger log = LoggerFactory.getLogger(FinderCli.class);

	public static void main(String[] args) {
		Arguments a = new Arguments();
		a.populate(args);
		
		if(a.arrow()){
			new Finder().find(a.toFindCommand());
		}else{
			a.printHelp();
		}
	}
}
