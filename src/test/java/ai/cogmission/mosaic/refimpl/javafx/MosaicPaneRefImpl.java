package ai.cogmission.mosaic.refimpl.javafx;


import ai.cogmission.mosaic.LayoutImpl;
import ai.cogmission.mosaic.Node;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Random;

public class MosaicPaneRefImpl extends Application {
	/** Mapping of element id's to labels for later reference when serializing */
	private java.util.Map<String, Label> clientMap = new java.util.HashMap<>();
	/** Holds colors to use for component illustration */
	private String[] colors = new String[] { "blue", "purple", "orange", "green", "gray" };
	/** Used to randomize ui element colors */
	private Random random = new Random();

	@Override
	public void start(Stage stage) throws Exception {
//		Application.Parameters params = getParameters();
//		Map<String, String> map = params.getNamed();
//		System.out.println("params = " + params);
		
//		ModelLoader loader = new ModelLoader(map.get("file"));
//		String[] model = loader.getModel(map.get("surface"));
//
//		System.out.println("model.length = " + model.length);
		MosaicPane mosaicPane = new MosaicPane();

		mosaicPane.getSurface().setLayout(
			new LayoutImpl(new Node("x",0,0,1,1), true)
		);

		int i = 0;
		for(String def : new String[] {
				"0, 0, 0.33, 0.50",
				"0.33, 0, 0.33, 1",
				"0, 0.50, 0.33, 0.50",
				"0.66, 0, 0.33, 0.50",
				"0.66, 0.50, 0.33, 0.50"
		}) {
			String[] args = def.split("[\\s]*\\,[\\s]*");
			int offset = args.length > 4 ? args.length - 4 : 0;
			String id = args.length == 4 ? "" + (i++) : args[0];
			Label l = getLabel(i > 4 ? colors[random.nextInt(5)] : colors[i], id);
			mosaicPane.add(l, id,
				Double.parseDouble(args[offset + 0]),
				Double.parseDouble(args[offset + 1]),
				Double.parseDouble(args[offset + 2]),
				Double.parseDouble(args[offset + 3]));
			clientMap.put(id, l);
		}
		
        mosaicPane.getEngine().addSurface(mosaicPane.getSurface());


		mosaicPane.setMaxWidth(Double.MAX_VALUE);
		mosaicPane.setMaxHeight(Double.MAX_VALUE);

        Scene scene = new Scene(mosaicPane, 900, 700);

	    stage.setTitle("Mosaic Layout Engine Demo (JavaFX)");
		stage.setScene(scene);
		stage.show();
	}
	
//	public void addNewWindowTrigger(MosaicPane<Node> mp) {
////		final Surface<Node> surface = mp.getSurface();
////		final MosaicEngine<Node> engine = mp.getEngine();
//
//
//	}
	
	public Label getLabel(String color, String id) {
		Label label = new Label();
		label.textProperty().set(id);
		label.textAlignmentProperty().set(TextAlignment.CENTER);
		label.alignmentProperty().set(Pos.CENTER);
		label.setOpacity(1.0);
		label.setTextFill(Color.WHITE);
		label.setFont(Font.font("Arial", FontWeight.BOLD, 16d));
		label.setStyle("-fx-background-color: " + color.toString() + 
			";-fx-alignment:center;-fx-text-alignment:center;");
		label.setManaged(false);
		
		return label;
	}
	
	public static void main(String[] args) throws MalformedURLException {
		if(args == null || args.length < 1 || args[0] == null) {
		    //URL url = MosaicPaneRefImpl.class.getResource("testModel.txt");
			URL url = new URL("file:///tmp/model.txt");
		    String path = Paths.get(url.toExternalForm()).toAbsolutePath().toString();
		    try{
		        path = Paths.get(url.toURI()).toAbsolutePath().toString();
		    }catch(Exception e) { e.printStackTrace(); }
		    
			args = new String[] { "--file="+path, "--surface=test"};
		}
		System.out.println(System.getProperty("user.dir"));
        launch(args);
    }
}
