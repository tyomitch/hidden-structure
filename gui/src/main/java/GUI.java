package sample;
//GUI code
//To add a new parameter, make three changes:
//1. Create a display object (textfield, radio, button, etc) and add to the grid
//2. Validate input in the run event-handler (check to make sure there is a value, save value as string)
//3. Add input to the list of arguments to the main program
//If the parameter is for both learners (EDL and GLA), it needs to be added to the list of arguments for each

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.*;
import java.io.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.awt.Desktop;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.*;
import javafx.beans.value.*;
import javafx.scene.text.*;
import java.util.*;
import java.io.PrintStream;
import learner.*;
import java.nio.file.*;

public class GUI extends Application {

    @Override
    public void start(Stage primaryStage) {

        Desktop desktop = Desktop.getDesktop();
        primaryStage.setTitle("UMass Hidden Structure Learners");//title of window

        GridPane grid = new GridPane();//This is the grid that all elements must be added to
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(20);
        grid.setPadding(new Insets(20, 20, 20, 20));

        Text scenetitle = new Text("Input, Output, & Required Parameters");//scene title
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        TextArea ta = new TextArea();
        grid.add(ta,0,10);
        ta.setPrefColumnCount(40);



        Label gramlabel = new Label("Grammar file:    ");//Grammar file upload box
        gramlabel.setStyle("-fx-text-fill: black;");
        grid.add(gramlabel, 0, 0);

        Tooltip gramTooltip = new Tooltip();
        gramTooltip.setText("Grammar file contains constraints and tableaux");
        Tooltip.install(gramlabel, gramTooltip);
        TextField gr = new TextField ();
        gr.setPrefColumnCount(35);
        HBox grammar = new HBox();
        grammar.getChildren().addAll(gramlabel,gr);
        grammar.setSpacing(5);
        grid.add(grammar, 0,1);

        final FileChooser grChooser = new FileChooser();
        final Button gramButton = new Button("Upload file");
        grid.add(gramButton,1,1);
        TextField grPath = new TextField();


        gramButton.setOnAction(
			       new EventHandler<ActionEvent>() { //Filepicker for the grammar file
				   @Override
				   public void handle(final ActionEvent e) {
				       File gramfile = grChooser.showOpenDialog(primaryStage);
				       if (gramfile != null) {
					   //openFile(file);
					   gr.setText(gramfile.getName());
					   grPath.setText(gramfile.getAbsolutePath());
				       }
				   }
			       });

        Label distlabel = new Label("Distribution file:");//Distibution file upload box
        distlabel.setStyle("-fx-text-fill: black;");
        Tooltip distTooltip = new Tooltip();
        distTooltip.setText("Distribution file stores data forms and their frequencies");
        Tooltip.install(distlabel, distTooltip);
        TextField dist = new TextField ();
        dist.setPrefColumnCount(35);
        HBox distfile = new HBox();
        distfile.getChildren().addAll(distlabel,dist);
        distfile.setSpacing(5);
        grid.add(distfile, 0,2);
        TextField distPath = new TextField();

        final FileChooser distChooser = new FileChooser();
        final Button distButton = new Button("Upload file");
        grid.add(distButton,1,2);

        distButton.setOnAction(
			       new EventHandler<ActionEvent>() { //Filepicker for the distribution file
				   @Override
				   public void handle(final ActionEvent e) {
				       File distfile = distChooser.showOpenDialog(primaryStage);
				       if (distfile != null) {
					   //openFile(file);
					   dist.setText(distfile.getName());
					   distPath.setText(distfile.getAbsolutePath());
				       }
				   }
			       });

        Label reslabel = new Label("Results file:       ");//Where to store results
        reslabel.setStyle("-fx-text-fill: black;");
        Tooltip resTooltip = new Tooltip();
        resTooltip.setText("The name of the output file to print results to.");
        Tooltip.install(reslabel, resTooltip);
        TextField resField = new TextField ();
        resField.setPrefColumnCount(35);
        HBox resBox = new HBox();
        resBox.getChildren().addAll(reslabel,resField);
        resBox.setSpacing(5);
        grid.add(resBox, 0,3);

        Label itlabel = new Label("Iterations:          ");//Iterations
        itlabel.setStyle("-fx-text-fill: black;");
        Tooltip itTooltip = new Tooltip();
        itTooltip.setText("Reasonable iterations are 1000 for online learners \nand 100 for batch learners");
        Tooltip.install(itlabel, itTooltip);
        TextField it = new TextField ("1000");
        it.setPrefColumnCount(7);
        HBox iterations = new HBox();
        iterations.getChildren().addAll(itlabel,it);
        iterations.setSpacing(5);
        grid.add(iterations, 0,4);

        ComboBox learner = new ComboBox(FXCollections.observableArrayList("EDL","GLA")); //Learner choice box
        Tooltip learnTooltip = new Tooltip();
        learnTooltip.setText("Select the GLA (Boersma 1998) or the EDL (Jarosz 2015)");
        Tooltip.install(learner, learnTooltip);
        learner.setPromptText("Learner");
        grid.add(learner,0,5);

        TitledPane edlo = new TitledPane();//Options for the EDL learner
        GridPane edlogrid = new GridPane();
        edlogrid.setVgap(4);
        edlogrid.setPadding(new Insets(5, 5, 5, 5));
        ComboBox emodel = new ComboBox(FXCollections.observableArrayList("Online","Batch")); //Type of EDL learner
        Tooltip emodelTooltip = new Tooltip();
        emodelTooltip.setText("Select online or batch EDL (Jarosz 2015)");
        Tooltip.install(emodel, emodelTooltip);
        emodel.setPromptText("Learner");
        TextField ss = new TextField("100"); //Sample size textfield
        ss.setPrefColumnCount(4);
        Label ssl = new Label("Sample Size: ");
        Tooltip ssTooltip = new Tooltip();
        ssTooltip.setText("How many samples should be taken to test each pairwise \nranking during learning?");
        Tooltip.install(ssl, ssTooltip);
        TextField edllr = new TextField("0.1"); //Learning rate text field
        edllr.setPrefColumnCount(4);
        Label edllrl = new Label("Learning rate: ");
        Tooltip edllrTooltip = new Tooltip();
        edllrTooltip.setText("This only applies to the online version. \nSet to .1 by default.");
        Tooltip.install(edllrl, edllrTooltip);
        // adding EDL UR options here
        CheckBox eur = new CheckBox();
        Label eurl = new Label("UR Learning?");
        Tooltip eurTooltip = new Tooltip();
        eurTooltip.setText("Check this box to learn underlying representations");
        Tooltip.install(eurl, eurTooltip);
        // adding UR file chooser
        TextField urf = new TextField ();
        urf.setPrefColumnCount(8);
        HBox urfile = new HBox();
        urfile.getChildren().addAll(urf);
        urfile.setSpacing(5);
        TextField urPath = new TextField();

        final FileChooser urChooser = new FileChooser();
        final Button urButton = new Button("Upload UR file");
        Tooltip urTooltip = new Tooltip();
        urTooltip.setText("UR file stores underlying forms and their parameters");
        Tooltip.install(urButton, urTooltip);

        urButton.setOnAction(
                   new EventHandler<ActionEvent>() { //Filepicker for the distribution file
                   @Override
                   public void handle(final ActionEvent e) {
                       File urfile = urChooser.showOpenDialog(primaryStage);
                       if (urfile != null) {
                       //openFile(file);
                       urf.setText(urfile.getName());
                       urPath.setText(urfile.getAbsolutePath());
                       }
                   }
                   });

        TextField phono = new TextField("0"); //Sample size textfield
        phono.setPrefColumnCount(4);
        Label phonol = new Label("Phono. Iterations: ");
        Tooltip phonoTooltip = new Tooltip();
        phonoTooltip.setText("How many iterations of phonotactic learning (no UR learning)\nshould take place?");
        Tooltip.install(phonol, phonoTooltip);

        edlogrid.add(emodel,0,0);
        edlogrid.add(ssl, 0, 1);
        edlogrid.add(ss, 1, 1);
        edlogrid.add(edllrl,0,2);
        edlogrid.add(edllr,1,2);
        edlogrid.add(eurl,0,3);
        edlogrid.add(eur,1,3);
        edlogrid.add(urButton,0,4);
        edlogrid.add(urfile, 1,4);
        edlogrid.add(phonol,0,5);
        edlogrid.add(phono,1,5);
        edlo.setText("EDL Options");
        edlo.setContent(edlogrid);
        edlo.setExpanded(false);
        edlo.setVisible(false);
        urButton.setVisible(false);
        urfile.setVisible(false);
        phono.setVisible(false);
        phonol.setVisible(false);
        grid.add(edlo,0,6);

        //This event listener hides the learning rate label and textfield if user selects batch
        emodel.getSelectionModel().selectedIndexProperty().addListener(
								       new ChangeListener<Number>() {
									   public void changed(ObservableValue ov, Number value, Number new_value) {
									       System.out.println(new_value);
									       System.out.println(new_value.intValue());
									       if(new_value.intValue()==1){
										   System.out.println("True!");
										   edllrl.setVisible(false);
										   edllr.setVisible(false);
										   //edllrl.setExpanded(false);
									       }else{
										   edllrl.setVisible(true);
										   edllr.setVisible(true);
									       }
									   }
								       });

        //This event listener displays the UR file and phonotatic options if user checks ur learning
        eur.selectedProperty().addListener(
                                       new ChangeListener<Boolean>() {
                                       public void changed(ObservableValue ov, Boolean value, Boolean new_value) {
                                           System.out.println(new_value);
                                           if(new_value){
                                           System.out.println("True!");
                                           urButton.setVisible(true);
                                            urfile.setVisible(true);
                                            phono.setVisible(true);
                                            phonol.setVisible(true);
                                           }else{
                                            urButton.setVisible(false);
                                            urfile.setVisible(false);
                                            phono.setVisible(false);
                                            phonol.setVisible(false);
                                           }
                                       }
                                       });


        TitledPane glao = new TitledPane();//Options for the GLA learner
        GridPane glaogrid = new GridPane();
        glaogrid.setVgap(4);
        glaogrid.setPadding(new Insets(5, 5, 5, 5));
        ComboBox gmodel = new ComboBox(FXCollections.observableArrayList("RIP","RRIP","EIP","randRIP")); //Type of GLA learner
        Tooltip gmodelTooltip = new Tooltip();
        gmodelTooltip.setText("RIP (Boersma 2003), RRIP (Jarosz 2015), EIP (Jarosz 2015), \nrandRIP just selects a random candidate as the parse");
        Tooltip.install(gmodel, gmodelTooltip);
        gmodel.setPromptText("Learner");
        glaogrid.add(gmodel,0,0);
        ComboBox gramtype = new ComboBox(FXCollections.observableArrayList("OT","HG","MaxEnt"));
        Tooltip typeTooltip = new Tooltip();
        typeTooltip.setText("OT and HG can be used with any parsing method. \nME should use RIP.");
        Tooltip.install(gramtype, typeTooltip);
        gramtype.setPromptText("Grammar Type");
        TextField lr = new TextField("0.1");
        lr.setPrefColumnCount(4);
        Label lrl = new Label("Learning Rate: ");
        Tooltip lrTooltip = new Tooltip();
        lrTooltip.setText("How much weights or ranking values are nudged when updated. \nA common learning rate is around .1");
        Tooltip.install(lrl, lrTooltip);
        TextField n = new TextField("2");
        n.setPrefColumnCount(4);
        Label nl = new Label("Noise: ");
        Tooltip nTooltip = new Tooltip();
        nTooltip.setText("How variable weights or ranking values are in OT or HG. \nNoise is often set to 2");
        Tooltip.install(nl, nTooltip);
        CheckBox nok = new CheckBox();
        Label nokl = new Label("NegOK?");
        Tooltip nokTooltip = new Tooltip();
        nokTooltip.setText("NegOK means that negative weights can be used.");
        Tooltip.install(nokl, nokTooltip);
        glaogrid.add(gramtype,0,1);
        glaogrid.add(lrl,0,2);
        glaogrid.add(lr,1,2);
        glaogrid.add(nl,0,3);
        glaogrid.add(n,1,3);
        glaogrid.add(nokl, 0, 4);
        glaogrid.add(nok, 1, 4);
        glao.setText("GLA Options");
        glao.setContent(glaogrid);
        glao.setExpanded(false);
        glao.setVisible(false);
        grid.add(glao,0,6);

        learner.getSelectionModel().selectedIndexProperty().addListener(//Display either EDL or GLA options based on which is selected
									new ChangeListener<Number>() {
									    public void changed(ObservableValue ov, Number value, Number new_value) {
										if(new_value.intValue()==0){
										    glao.setVisible(false);
										    glao.setExpanded(false);
										    edlo.setExpanded(true);
										    edlo.setVisible(true);
										}else{
										    edlo.setVisible(false);
										    edlo.setExpanded(false);
										    glao.setExpanded(true);
										    glao.setVisible(true);
										}
									    }
									});

        TitledPane ao = new TitledPane();//Advanced options
        GridPane aogrid = new GridPane();
        aogrid.setVgap(4);
        aogrid.setPadding(new Insets(5, 5, 5, 5));
        CheckBox iBias = new CheckBox();
        Label ibl = new Label("Initial Bias");
        Tooltip ibTooltip = new Tooltip();
        ibTooltip.setText("Initial Bias means that the weights or ranks given in the grammar file \nwill be used to initialize the grammar");
        Tooltip.install(ibl, ibTooltip);
        TextField finEvalSample = new TextField("1000");
        finEvalSample.setPrefColumnCount(4);
        Label fesl = new Label("Final Eval. Sample: ");
        Tooltip fesTooltip = new Tooltip();
        fesTooltip.setText("Final Evaluation Sampling is the number of samples used \nto evaluate the final grammar.");
        Tooltip.install(fesl, fesTooltip);
        aogrid.add(ibl, 0, 1);
        aogrid.add(iBias, 1, 1);
        aogrid.add(fesl,0,2);
        aogrid.add(finEvalSample,1,2);
        ao.setText("Advanced Options");
        ao.setContent(aogrid);
        ao.setExpanded(false);
        grid.add(ao,0,6);

        TitledPane eo = new TitledPane();//Efficiency options
        GridPane eogrid = new GridPane();
        eogrid.setVgap(4);
        eogrid.setPadding(new Insets(5, 5, 5, 5));
        TextField quitFreq = new TextField("100");
        quitFreq.setPrefColumnCount(4);
        Label qfl = new Label("Quit Early Freq.: ");
        Tooltip qfTooltip = new Tooltip();
        qfTooltip.setText("How often the program checks to see if a successful grammar\nhas already been learned. If the learning problem is simple,\nsetting a small number of generations will likely make \nthe program more efficient; if the learning problem is difficult \nand early success if unlikely, then a high value here will increase efficiency.");
        Tooltip.install(qfl, qfTooltip);
        TextField quitSample = new TextField("100");
        quitSample.setPrefColumnCount(4);
        Label qsl = new Label("Sampling Size: ");
        Tooltip qsTooltip = new Tooltip();
        qsTooltip.setText("How many samples are used to evaluate whether a successful grammar\nhas already been learned. High values, above 100,\nwill improve accuracy at the expense of efficient performance.");
        Tooltip.install(qsl, qsTooltip);
        TextField maxDepth = new TextField();
        maxDepth.setText("8");
        maxDepth.setPrefColumnCount(4);
        Label mdl = new Label("Tree MaxDepth: ");
        Tooltip mdTooltip = new Tooltip();
        mdTooltip.setText("Sets a maximum depth of the ranking tree; to increase efficiency, \nit is possible to cap the depth of data structures.\nA reasonable value may be 8. For grammars with more than \naround 12 constraints this number may need to be lowered to avoid \nrunnig out of memory.");
        Tooltip.install(mdl, mdTooltip);
        eogrid.add(qfl, 0, 0);
        eogrid.add(quitFreq, 1, 0);
        eogrid.add(qsl, 0, 1);
        eogrid.add(quitSample, 1, 1);
        eogrid.add(mdl, 0, 2);
        eogrid.add(maxDepth, 1, 2);
        eo.setText("Efficiency Options");
        eo.setContent(eogrid);
        eo.setExpanded(false);
        grid.add(eo,0,7);

        TitledPane po = new TitledPane();//Output options
        GridPane pogrid = new GridPane();
        pogrid.setVgap(4);
        pogrid.setPadding(new Insets(5, 5, 5, 5));
        CheckBox printInput = new CheckBox();
        Label pil = new Label("Print Input?");
        Tooltip piTooltip = new Tooltip();
        piTooltip.setText("Print user input at the beginning of the run?");
        Tooltip.install(pil, piTooltip);
        pogrid.add(new Label("At beginning of program:"),0,0);
        pogrid.add(pil,0,1);
        pogrid.add(printInput,1,1);
        pogrid.add(new Separator(),0,2);

        pogrid.add(new Label("Intermediate evaluation:"),0,3); //Intermediate evauation options
        Label eil = new Label("Evaluation Frequency:");
        Tooltip eiTooltip = new Tooltip();
        eiTooltip.setText("How often should learner be evaluated on their progress?");
        Tooltip.install(eil, eiTooltip);
        pogrid.add(eil,0,4);
        TextField interEvalFreq = new TextField("1");
        interEvalFreq.setPrefColumnCount(4);
        pogrid.add(interEvalFreq,1,4);

        Label eisl = new Label("Sample size: ");
        Tooltip eisTooltip = new Tooltip();
        eisTooltip.setText("How many samples are used to evaluate whether a successful grammar\nhas already been learned. High values, above 100,\nwill improve accuracy at the expense of efficient performance.");
        Tooltip.install(eisl, eisTooltip);
        pogrid.add(eisl,0,5);
        TextField interEvalSample = new TextField("100");
        interEvalSample.setPrefColumnCount(4);
        pogrid.add(interEvalSample,1,5);

        CheckBox interEvalGram = new CheckBox();
        interEvalGram.setSelected(true);
        CheckBox interEvalAcc = new CheckBox();

        pogrid.add(new Label("Print Grammar? "), 0, 6);
        pogrid.add(interEvalGram, 1, 6);
        pogrid.add(new Label("Print Accuracy Per Output? "), 0, 7);
        pogrid.add(interEvalAcc, 1, 7);
        pogrid.add(new Separator(),0,8);

        pogrid.add(new Label("Final evaluation: "),0,9); //Final evaluation options
        CheckBox finalAcc = new CheckBox();
        pogrid.add(new Label("Print Accuracy Per Output? "), 0, 11);
        pogrid.add(finalAcc, 1, 11);
        po.setText("Print Options");
        po.setContent(pogrid);
        po.setExpanded(false);
        grid.add(po,0,8);

        //grid.setGridLinesVisible(true);

        Button btn = new Button("Run");//Run the program
        Tooltip runTooltip = new Tooltip();
        //runTooltip.setText("Your parameter preferences will automatically be saved.");//Not yet
        Tooltip.install(btn, runTooltip);
        grid.add(btn, 0,9);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 4);

        btn.setOnAction(new EventHandler<ActionEvent>() { //Call main program with parameters

		@Override
		public void handle(ActionEvent e) {
		    ta.setText("");
		    actiontarget.setText("Running...");
		    String chosenBias;
		    if (iBias.isSelected()) { //If bias parameter is enabed
			chosenBias = "1";
		    } else {
			chosenBias = "0";
		    }
		    String chosenNeg;
		    if (nok.isSelected()) { //If NEGOK parameter is enabled
			chosenNeg = "1";
		    } else {
			chosenNeg = "0";
		    }
		    String chosenQuitFreq = quitFreq.getText(); //Quit early frequency
		    String chosenQuitSample = quitSample.getText(); //Number of quit early samples
		    String chosenMaxDepth = maxDepth.getText(); //Max depth option
            String chosenPhono = phono.getText();
		    String chosenInterEvalFreq = interEvalFreq.getText(); //Intermediate evaluation options
		    String chosenInterEvalSample = interEvalSample.getText();
		    String chosenPrintInput; //Print options
		    if (printInput.isSelected()){
			chosenPrintInput = "0";
		    } else {
			chosenPrintInput = "1";
		    }
		    String chosenInterEval;
		    if (interEvalAcc.isSelected()){
			chosenInterEval = "0";
		    } else {
			if(interEvalGram.isSelected()) {
			    chosenInterEval = "1";
			}else{
			    chosenInterEval = "2";
			}
		    }
		    String chosenFinalAcc;
		    if (finalAcc.isSelected()) {
			chosenFinalAcc = "0";
		    } else{
			chosenFinalAcc = "1";
		    }
		    String resName = resField.getText(); //Results file name
		    if(resName.equals("")){
			resName = "results.txt"; //Default result files name is results.txt
		    }
		    String resName_ = resName;
		    if(gr.getText().equals("")){
			actiontarget.setText("Error: please upload grammar file!"); //Print error message if no grammar file is uploaded
		    } else {
			String chosenGrammar = grPath.getText();
			if(dist.getText().equals("")){ //Print error message if no distribution file is uploaded
			    actiontarget.setText("Error: please upload distribution file!");
			}else{
			    String chosenDist = distPath.getText();
			    if(it.getText().equals("")){
				actiontarget.setText("Error: please specify iterations!"); //Print error if no iterations are specified
			    } else {
				String chosenIt = it.getText();
				if(learner.getValue()==null){
				    actiontarget.setText("Error: please choose learner!"); //Print error if no learner is selected
				} else {
				    String chosenLearner = learner.getValue().toString();
				    if(chosenLearner=="EDL") {
					System.out.println("EDL!");
					if(emodel.getValue()==null){
					    actiontarget.setText("Error: please choose learner type!"); //Print error if no learner type is selected
					} else{
					    String chosenLearnerType = emodel.getValue().toString();
					    String chosenLearnerNum;
					    if(chosenLearnerType=="Online"){
						chosenLearnerNum = "2";
					    } else{
						chosenLearnerNum = "1";
					    }
					    if(ss.getText().equals("")){//Throw errors if something hasn't been specified
						actiontarget.setText("Error: please specify sample size!");
					    }else{
						  String chosenSampleSize = ss.getText();
						if(edllr.getText().equals("")) {
						    actiontarget.setText("Error: please specify the learning rate!");
						}else{
                            String chosenUR;
                            if(eur.isSelected()){
                                chosenUR = "true";
                            }else{
                                chosenUR = "false";
                            }
                            if(chosenUR=="true" && urf.getText().equals("")){
                                actiontarget.setText("Error: please upload UR file!");
                            }else{
                                String chosenURFile = urPath.getText();
    						    //Else run learner with given parameters
    						    String chosenEDLLearningRate = edllr.getText();
    						    System.out.println("All EDL parameters ok!");
    						    String chosenFinEvalSample = finEvalSample.getText();//eventually move
    						    String[] args = {chosenGrammar, chosenDist, chosenIt, chosenFinEvalSample, chosenLearnerNum, chosenSampleSize,chosenBias,chosenEDLLearningRate,chosenUR,chosenURFile,chosenPhono,chosenPrintInput,chosenFinalAcc,chosenInterEval,chosenInterEvalFreq,chosenInterEvalSample,chosenQuitFreq,chosenQuitSample, chosenMaxDepth};
    						    System.out.println(Arrays.toString(args));
    						    EDL.writer = new GuiWriter(ta);//Create a writer to output results
    						    new Thread () {
    							@Override public void run () {//Must create new thread so that the GUI doesn't freeze while the learner is running
    							    EDL.main(args);
    							    Platform.runLater(new Runnable() {
    								    public void run() {
    									actiontarget.setText("Writing results to file...");
    									try {
    									    String res = EDL.writer.getText(); //Display output in textbox
    									    //Write output to file:
    									    Files.write(
    											Paths.get(resName_),
    											res.getBytes(),
    											StandardOpenOption.APPEND, StandardOpenOption.CREATE);
    									    actiontarget.setText("All done!");
    									    EDL.writer.clear(); //Clear old output (otherwise, each run is written to file multiple times
    									} catch (IOException uhoh) {
    									    uhoh.printStackTrace();
    									}
    								    };
    								});
    							}
    						    }.start();
                            }


						}
					    }
					}
				    } else{
					System.out.println("GLA!"); //GLA is beeing run
					if(gmodel.getValue()==null){
					    actiontarget.setText("Error: please choose learner type!"); //Print error message if no learner is selected
					}else{
					    String chosenLearnerType = gmodel.getValue().toString();
					    if(gramtype.getValue()==null){
						actiontarget.setText("Error: please choose grammar type!");
					    }else{
						String chosenGrammarType = gramtype.getValue().toString();
						if(lr.getText().equals("")){
						    actiontarget.setText("Error: please specify learning rate!"); //Print error message if no learning rate is selected
						}else{
						    String chosenLR = lr.getText();
						    if(n.getText().equals("")){
							actiontarget.setText("Error: please specify noise!"); //Print error message if no noise parameter is given
						    }else{
							String chosenNoise = n.getText();
							String chosenFinEvalSample = finEvalSample.getText();//eventually move
							System.out.println("All GLA parameters ok!");
							//Put all arguments together:
							String[] args = {chosenGrammar, chosenDist, chosenIt, chosenFinEvalSample, chosenLearnerType, chosenGrammarType, chosenLR, chosenNoise, chosenBias,chosenNeg, chosenPrintInput, chosenFinalAcc, chosenInterEval, chosenInterEvalFreq, chosenInterEvalSample, chosenQuitFreq, chosenQuitSample};
							System.out.println(Arrays.toString(args)); //Print arguments
							GLA.writer = new GuiWriter(ta);
							new Thread () {
							    @Override public void run () { //New thread to run main program so that GUI doesn't freeze up
								GLA.main(args);
								Platform.runLater(new Runnable() {
									public void run() {
									    actiontarget.setText("Writing results to file...");
									    try {
										String res = GLA.writer.getText(); //Check to make sure this is consistent with above
										//Write output to file:
										Files.write(
											    Paths.get(resName_),
											    res.getBytes(),
											    StandardOpenOption.APPEND, StandardOpenOption.CREATE);
										actiontarget.setText("All done!");
										GLA.writer.clear(); //Clear old output (otherwise, each run is written to file multiple times)
									    } catch (IOException uhoh) {
										uhoh.printStackTrace();
									    }
									}
								    });
							    }
							}.start();
						    }
						}
					    }
					}
				    }
				}
			    }
			}
		    }
		}
	    });

        //Window options:
        ScrollPane sp = new ScrollPane();//Scroll for GUI
        sp.setContent(grid);
        Scene scene = new Scene(sp, 700, 700); //Size of GUI window
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

}
