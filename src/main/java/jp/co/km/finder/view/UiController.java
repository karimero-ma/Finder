package jp.co.km.finder.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import jp.co.km.finder.FindCommand;
import jp.co.km.finder.FindCommand.SEARCH_OPTION;
import jp.co.km.finder.Finder;
import jp.co.km.finder.InvalidFileCommandException;
import jp.co.km.finder.Result;
import jp.co.km.finder.view.ResultModel.ResultModelBuilder;

public class UiController {
	
	private static final Logger log = LoggerFactory.getLogger(UiController.class);
	
	private static final ObservableList<String> EXTENTIONS 
		= FXCollections.observableArrayList(
		"*.*", "*.txt", "*.html", "*.css", "*.scss"
		, "*.csv", "*.js", "*.java", "*.jsp", "*.xml");

	/*---------------------------------------------------*/
	/* Path                                              */
	/*---------------------------------------------------*/
	@FXML
	TextField pathTxt;
	
	@FXML
	Button choosePathBtn;
	
	/*---------------------------------------------------*/
	/* SearchOptions                                     */
	/*---------------------------------------------------*/
	@FXML
	/** サブフォルダーも検索 */
	ToggleButton recursiveToggleButton;
	
	@FXML
	/** 大文字・小文字を区別しない */
	ToggleButton caseInsensitiveToggleButton;
	
	@FXML
	/** 正規表現を使用する */
	ToggleButton regexToggleButton;
	
	@FXML
	/** 単語単位で検索する */
	ToggleButton wordUnitToggleButton;
	
	@FXML
	ComboBox<String> nameFilterComboBox;
	
	/*---------------------------------------------------*/
	/* Execute Search                                    */
	/*---------------------------------------------------*/
	@FXML
	/** 検索する文字列 */
	TextField patternTxt;
	
	@FXML
	/** 検索ボタン */
	Button findBtn;
	
	/*---------------------------------------------------*/
	/* StatusMessage                                     */
	/*---------------------------------------------------*/
	@FXML
	/** お知らせメッセージ */
	Label message;
	
	/*---------------------------------------------------*/
	/* ResultView                                        */
	/*---------------------------------------------------*/
	@FXML
	/** 検索結果を表示するテーブル */
	TableView<ResultModel> resultView;

	@FXML
	/**
	 * メインフレームの初期化
	 */
	void initialize(){
		nameFilterComboBox.setItems(EXTENTIONS);
		ObservableList<TableColumn<ResultModel, ?>> columns = resultView.getColumns();
		
		columns.get(0).setCellValueFactory(new PropertyValueFactory<>("path"));
		columns.get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
		columns.get(2).setCellValueFactory(new PropertyValueFactory<>("no"));
		columns.get(3).setCellValueFactory(new PropertyValueFactory<>("text"));
	}
	
	@FXML
	/**
	 * 検索ボタンクリック時のイベント
	 * @param event
	 */
	public void onclickFindBtn(ActionEvent event){
		log.info("call onclickFindBtn");
		
		FindCommand fc = createFileCommand();
		try{
			fc.validateRequired();
		}catch (InvalidFileCommandException e) {
			println("検索文字列を指定してください");
			return;
		}
		
		println("検索　開始 …");
		List<Result> results = new Finder().find(fc);
		println("検索　終了");
		
		log.trace("検索結果の描画開始");
		final List<ResultModel> srcModels = new ArrayList<>();
		results.stream().forEach(r -> {
			ResultModelBuilder builder = new ResultModelBuilder();
			builder.setPath(r.getPath().toString())
				.setName(r.getPath().getFileName().toString());
				
			r.getLines().stream().forEach(l ->{
				builder.setNo(Long.toString(l.getNo())).setText(l.getText().trim());
				srcModels.add(builder.build());
			});
		});
		resultView.setItems(FXCollections.observableArrayList(srcModels));
		log.trace("検索結果の描画開始");
	}

	private FindCommand createFileCommand() {
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
		return fc;
	}
	
	@FXML
	/**
	 * (パスの)選択ボタンクリック時のイベント
	 * @param event
	 */
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
	
	/**
	 * メッセージバーに通知を表示する
	 * @param text
	 */
	private void println(String text){
		message.setText(text);
	}
}

