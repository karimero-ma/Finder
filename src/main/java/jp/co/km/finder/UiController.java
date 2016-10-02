package jp.co.km.finder;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.DirectoryChooser;
import jp.co.km.finder.FindCommand.SEARCH_OPTION;

public class UiController {
	
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private static final Logger log = LoggerFactory.getLogger(UiController.class);

	@FXML
	TextField pathTxt;
	
	@FXML
	Button choosePathBtn;
	
	@FXML
	ToggleButton recursiveToggleButton;
	
	@FXML
	ToggleButton caseInsensitiveToggleButton;
	
	@FXML
	ToggleButton regexToggleButton;
	
	@FXML
	ToggleButton wordUnitToggleButton;
	
	@FXML
	TextField patternTxt;
	
	@FXML
	Button findBtn;
	
	@FXML
	ComboBox<String> nameFilterComboBox;
	
	@FXML
	TextArea resultTextArea;

	@FXML
	void initialize(){
		resultTextArea.appendText("初期化文字列");
		
	}
	
	@FXML
	public void onclickFindBtn(ActionEvent event){
		log.info("call onclickFindBtn");
		
		resultTextArea.clear();
		
		resultTextArea.appendText("検索　開始 …");
		resultTextArea.appendText(LINE_SEPARATOR);
		
		FindCommand fc = new FindCommand();
		fc.setPath(pathTxt.getText())
			.setRecursive(recursiveToggleButton.isSelected())
			.setPattern(patternTxt.getText())
			.setNameFilter(nameFilterComboBox.getSelectionModel().getSelectedItem());
		
		if(caseInsensitiveToggleButton.isSelected()){
			fc.add(SEARCH_OPTION.CASE_INSENSITIVE);
		}
		
		if(regexToggleButton.isSelected()){
			fc.add(SEARCH_OPTION.REGEX);
		}
		
		if(wordUnitToggleButton.isSelected()){
			fc.add(SEARCH_OPTION.WORD_UNIT);
		}
				
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
		DirectoryChooser chooser = new DirectoryChooser();
		if(StringUtils.isNotEmpty(pathTxt.getText())){
			chooser.setInitialDirectory(new File(pathTxt.getText()));
		}
		
		File chooseDir = chooser.showDialog(null);
		if(chooseDir != null){
			pathTxt.setText(chooseDir.getAbsolutePath());
		}
	}
}

