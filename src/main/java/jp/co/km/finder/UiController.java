package jp.co.km.finder;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class UiController {
	
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private static final Logger log = LoggerFactory.getLogger(UiController.class);

	@FXML
	TextField pathTxt;
	
	@FXML
	Button choosePathBtn;
	
	@FXML
	CheckBox recursiveCheckBox;
	
	@FXML
	TextField patternTxt;
	
	@FXML
	Button findBtn;
	
	@FXML
	ComboBox<String> nameFilterComboBox;
	
	@FXML
	TextArea resultTextArea;

	
	@FXML
	public void onclickFindBtn(ActionEvent event){
		log.info("call onclickFindBtn");
		
		resultTextArea.clear();
		
		resultTextArea.appendText("検索　開始 …");
		resultTextArea.appendText(LINE_SEPARATOR);
		
		FindCommand fc = new FindCommand();
		fc.setPath(pathTxt.getText())
			.setRecursive(recursiveCheckBox.isSelected())
			.setPattern(patternTxt.getText())
			.setNameFilter(nameFilterComboBox.getSelectionModel().getSelectedItem());
		
		log.info(fc.toString());
		List<Result> results = new Finder().find(fc);
		
		results.forEach(r -> {
			StringBuilder resultsBuf = new StringBuilder();
			resultsBuf.append(r.getPath());
			resultsBuf.append(LINE_SEPARATOR);
			r.getLines().stream()
				.forEach(l -> resultsBuf.append(String.format("%s : %s%s", l.getNo(), l.getText().trim(), LINE_SEPARATOR)));
			resultTextArea.appendText(resultsBuf.toString());
		});
		resultTextArea.appendText("検索　終了");
		resultTextArea.appendText(LINE_SEPARATOR);
	}
	
	@FXML
	public void onclickChoosePathBtn(ActionEvent event){
		log.info("call onclickChoosePathBtn");
	}
}

