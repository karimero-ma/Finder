package jp.co.km.finder.view;

import javafx.beans.property.SimpleStringProperty;

public class ResultModel {

	private SimpleStringProperty path;
	
	private SimpleStringProperty name;
	
	private SimpleStringProperty no;
	
	private SimpleStringProperty text;

	private ResultModel(SimpleStringProperty path, SimpleStringProperty name, SimpleStringProperty no, SimpleStringProperty text) {
		super();
		this.path = path;
		this.name = name;
		this.no = no;
		this.text = text;
	}

	public String getPath() {
		return path.get();
	}
	
	public String getName() {
		return name.get();
	}

	public String getNo() {
		return no.get();
	}

	public String getText() {
		return text.get();
	}	

	static class ResultModelBuilder{
		private String path;
		
		private String name;
		
		private String no;
		
		private String text;

		public ResultModelBuilder setPath(String path) {
			this.path = path;
			return this;
		}
		
		public ResultModelBuilder setName(String name) {
			this.name = name;
			return this;
		}

		public ResultModelBuilder setNo(String no) {
			this.no = no;
			return this;
		}

		public ResultModelBuilder setText(String text) {
			this.text = text;
			return this;
		}
		
		ResultModel build(){
			return new ResultModel(
					new SimpleStringProperty(path)
					, new SimpleStringProperty(name)
					, new SimpleStringProperty(no)
					, new SimpleStringProperty(text));
		}
	}
}
