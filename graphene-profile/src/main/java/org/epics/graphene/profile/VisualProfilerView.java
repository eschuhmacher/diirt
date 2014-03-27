/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.epics.graphene.Graph2DRenderer;
import org.epics.graphene.profile.io.DateUtils;
import org.epics.graphene.profile.io.DateUtils.DateFormat;
import org.epics.graphene.profile.utils.DatasetFactory;
import org.epics.graphene.profile.utils.ProfileAnalysis;
import org.epics.graphene.profile.utils.Resolution;
import org.epics.graphene.profile.utils.Statistics;
import org.epics.graphene.profile.utils.StopWatch;
import org.epics.graphene.profile.utils.StopWatch.TimeType;

/**
 *
 * @author Aaron
 */
public class VisualProfilerView extends JPanel{
    
    //Data
    //-------------------------------------------------------------------------    
    private SwingWorker             thread;
    private List<JButton>           actionButtons;
    private VisualProfilerModel     model;
    private UserSettings            userSettings;
    
    private JTabbedPane tabs;
    //-------------------------------------------------------------------------

    
    //Panel Data Members
    //-------------------------------------------------------------------------
    private SettingsPanel   settingsPanel;
    private Profile1DTable  profile1DTable;
    private Profile2DTable  profile2DTable;
    private FileViewer      fileViewer;    
    private AnalyzePanel    analyzePanel;
    private Console         console;
    //-------------------------------------------------------------------------
   
    
    //Panel Structures
    //-------------------------------------------------------------------------
    private class SettingsPanel extends JPanel{
        public  JComboBox           listRendererTypes;
        private JLabel              lblRendererTypes;

        private JTextField          txtTestTime;
        private JLabel              lblTestTime;

        private JTextField          txtMaxAttempts;
        private JLabel              lblMaxAttempts;

        private JComboBox           listTimeTypes;
        private JLabel              lblTimeTypes;

        private JComboBox           listUpdateTypes;
        private JLabel              lblUpdateTypes;

        private JLabel              lblSaveMessage;
        private JTextField          txtSaveMessage;

        private JLabel              lblAuthorMessage;
        private JTextField          txtAuthorMessage;     
        
        
        public SettingsPanel(){
            this.initComponents();
            this.addComponents();
        }
        
        private void initComponents(){
            listRendererTypes   = new JComboBox(VisualProfiler.SUPPORTED_PROFILERS);
            lblRendererTypes    = new JLabel("Renderer Type: ");

            txtTestTime         = new JTextField("20");
            lblTestTime         = new JLabel("Test Time: ");

            txtMaxAttempts      = new JTextField("1000000");
            lblMaxAttempts      = new JLabel("Max Attempts: ");

            listTimeTypes       = new JComboBox(StopWatch.TimeType.values());
            lblTimeTypes        = new JLabel("Timing Based Off: ");

            listUpdateTypes     = new JComboBox();
            lblUpdateTypes      = new JLabel("Apply Update: ");

            lblSaveMessage      = new JLabel("Save Message: ");
            txtSaveMessage      = new JTextField("");

            lblAuthorMessage    = new JLabel("Author: ");
            txtAuthorMessage    = new JTextField("");
        }
        
        private void addComponents(){
            this.setLayout(new GridLayout(0, 2));

            this.add(this.lblRendererTypes);
            this.add(this.listRendererTypes);

            this.add(this.lblTestTime);
            this.add(this.txtTestTime);

            this.add(this.lblMaxAttempts);
            this.add(this.txtMaxAttempts);

            this.add(this.lblTimeTypes);
            this.add(this.listTimeTypes);            

            this.add(this.lblUpdateTypes);
            this.add(this.listUpdateTypes);

            this.add(lblSaveMessage);
            this.add(txtSaveMessage);

            this.add(lblAuthorMessage);
            this.add(txtAuthorMessage);            
        }
    }    
    private class Profile1DTable extends JPanel{
        private JLabel              lblDatasetSize;
        private JTextField          txtDatasetSize;

        private JLabel              lblImageWidth;
        private JTextField          txtImageWidth;

        private JLabel              lblImageHeight;
        private JTextField          txtImageHeight;

        private JLabel              lblShowGraph;
        private JCheckBox           chkShowGraph;

        public  JButton             btnProfile1D;
        public  JButton             btnProfile1DAll;   
        
        public Profile1DTable(){
            this.initComponents();
            this.initMnemonics();
            this.addComponents();
        }
        
        private void initComponents(){
            lblDatasetSize      = new JLabel("Number of Data Points: ");
            lblDatasetSize      .setToolTipText("Format for IntensityGraph2D: 1000x1000");        
            txtDatasetSize      = new JTextField("10000");

            lblImageWidth       = new JLabel("Image Width: ");
            txtImageWidth       = new JTextField("640");

            lblImageHeight      = new JLabel("Image Height: ");
            txtImageHeight      = new JTextField("480");

            lblShowGraph        = new JLabel("Graph Results: ");
            chkShowGraph        = new JCheckBox("Show Graph");

            btnProfile1D        = new JButton("Profile");
            actionButtons       .add(btnProfile1D);
            btnProfile1DAll     = new JButton("Profile For All Renderers");
            actionButtons       .add(btnProfile1DAll);
        }
        
        private void initMnemonics(){
            this.btnProfile1D.setMnemonic('P');
            this.btnProfile1DAll.setMnemonic('A');
        }
        
        private void addComponents(){
            this.setLayout(new GridLayout(0, 2));
        
            this.add(lblDatasetSize);
            this.add(txtDatasetSize);

            this.add(lblImageWidth);
            this.add(txtImageWidth);

            this.add(lblImageHeight);
            this.add(txtImageHeight);

            this.add(lblShowGraph);
            this.add(chkShowGraph);

            this.add(blankPanel(btnProfile1D));
            this.add(blankPanel(btnProfile1DAll));            
        }
    }
    private class Profile2DTable extends JSplitPane{
        private JLabel                          lblResolutions,
                                                lblNPoints;

        public  JButton                         btnProfile2D;

        private JList<Resolution>               listResolutions;
        private JList<Integer>                  listNPoints;
        private DefaultListModel<Resolution>    modelResolutions;
        private DefaultListModel<Integer>       modelNPoints;   
        
        public Profile2DTable(){
            this.initComponents();
            this.initMnemonics();
            this.addComponents();
            this.loadLists();
        }
        
        private void initComponents(){
            lblResolutions      = new JLabel("Resolutions");
            lblNPoints          = new JLabel("N Points");
            btnProfile2D        = new JButton("Start");
            actionButtons       .add(btnProfile2D);

            listResolutions     = new JList<>();
            listNPoints         = new JList<>();
        }
        
        private void initMnemonics(){
            this.btnProfile2D.setMnemonic('S');
        }
       
        private void addComponents(){
                JPanel multiLayerLeft = new JPanel();
                multiLayerLeft.setLayout(new BorderLayout());
                multiLayerLeft.add(lblResolutions, BorderLayout.NORTH);
                multiLayerLeft.add(new JScrollPane(listResolutions), BorderLayout.CENTER);

                JPanel multiLayerMiddle = new JPanel();
                multiLayerMiddle.setLayout(new BorderLayout());
                multiLayerMiddle.add(lblNPoints, BorderLayout.NORTH);
                multiLayerMiddle.add(new JScrollPane(listNPoints), BorderLayout.CENTER);

                JPanel multiLayerRight = new JPanel();
                multiLayerRight.setLayout(new BorderLayout());        
                multiLayerRight.add(blankPanel(btnProfile2D), BorderLayout.NORTH);
        
            final JSplitPane multiLayerInner = new JSplitPane();

            multiLayerInner.setLeftComponent(multiLayerLeft);
            multiLayerInner.setRightComponent(multiLayerMiddle);

            this.setLeftComponent(multiLayerInner);
            this.setRightComponent(multiLayerRight);
            
            SwingUtilities.invokeLater(new Runnable(){

                @Override
                public void run() {
                    Profile2DTable.this.setDividerLocation(0.8);
                    multiLayerInner.setDividerLocation(0.8);                                        
                }
                
            });                
        }
        
        /**
         * Loads the lists for <code>Resolution</code>s and the
         * dataset sizes for the <code>MultiLevelProfiler</code>.
         */
        private void loadLists(){
            modelResolutions = new DefaultListModel<>();
            modelNPoints = new DefaultListModel<>();

            for (Resolution resolution : Resolution.defaultResolutions()){
                modelResolutions.addElement(resolution);
            }

            for (Integer datasetSize : DatasetFactory.defaultDatasetSizes()){
                modelNPoints.addElement(datasetSize);
            }

            listResolutions.setModel(modelResolutions);
            listNPoints.setModel(modelNPoints);
        }        
    }    
    private class AnalyzePanel extends JPanel{
        public  JButton             btnCompare2DTables;
        public  JButton             btnAnalyze1DTable; 
        
        public AnalyzePanel(){
            this.initComponents();
            this.addComponents();
        }
        
        private void initComponents(){
            btnCompare2DTables  = new JButton("Compare Profile Tables");
            actionButtons       .add(btnCompare2DTables);
            btnAnalyze1DTable   = new JButton("Analyze Single Profile Tables");
            actionButtons       .add(btnAnalyze1DTable);
        }
        
        private void addComponents(){
            this.add(this.btnCompare2DTables);
            this.add(this.btnAnalyze1DTable);
        }
    }
    private class FileViewer extends JSplitPane{
        private JTree                   tree;
        private DefaultTreeModel        treeModel;
        private DefaultMutableTreeNode  treeRoot;
        public  JButton                 btnOpenFiles;
        public  JButton                 btnDeleteFiles;
        public  JButton                 btnReloadFiles;    
        
        public FileViewer(){
            this.initComponents();
            this.initMenmonics();
            this.addComponents();
        }
        
        private void initComponents(){
            treeRoot            = new DefaultMutableTreeNode(new File(ProfileGraph2D.LOG_FILEPATH));
            treeModel           = new DefaultTreeModel(treeRoot);
            tree                = new JTree(treeRoot){
                @Override
                public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus){  
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                    try{
                        File f = (File) node.getUserObject();
                        return f.getName();
                    }
                    catch(Exception e){
                        return node.toString();
                    }
                }
            };        
            tree                .setModel(treeModel);
            tree                .expandRow(0);

            btnOpenFiles        = new JButton("Open File(s)");
            actionButtons       .add(btnOpenFiles);
            btnDeleteFiles      = new JButton("Delete File(s)");
            actionButtons       .add(btnDeleteFiles);
            btnReloadFiles      = new JButton("Refresh");
            actionButtons       .add(btnReloadFiles);
        }
        
        private void initMenmonics(){
            this.btnOpenFiles.setMnemonic('O');
            this.btnDeleteFiles.setMnemonic('D');
            this.btnReloadFiles.setMnemonic('R');
        }
        
        private void addComponents(){
            JPanel fileTabRight = new JPanel();
            fileTabRight.setLayout(new BoxLayout(fileTabRight, BoxLayout.Y_AXIS));
            fileTabRight.add(this.btnOpenFiles);
            fileTabRight.add(this.btnDeleteFiles);
            fileTabRight.add(this.btnReloadFiles);

            this.setLeftComponent(new JScrollPane(tree));
            this.setRightComponent(fileTabRight);            
        }
        
        public void reloadNodes(){
            //Resets
            this.treeRoot.removeAllChildren();

            //Re-adds
            model.addNodes(treeRoot);

            //Updates GUI
            this.treeModel.nodeStructureChanged(this.treeRoot);
            this.repaint();              
        }         
    }
    private class Console extends JPanel{
        private JTextArea          txtConsole;
        private JLabel             lblConsole;
        public  JButton            btnClearLog;
        public  JButton            btnSaveLog;

        private JLabel             lblTime;
        private JTextField         txtTime;

        public  JButton            btnCancelThread;   
        
        public Console(){
            this.initComponents();
            this.initMnemonics();
            this.addComponents();
        }
        
        private void initComponents(){
            txtConsole = new JTextArea(20, 50);
            txtConsole.setEditable(false);
            lblConsole = new JLabel("Console");
            btnSaveLog = new JButton("Save Log");
            actionButtons.add(btnSaveLog);
            btnClearLog = new JButton("Clear Log");
            actionButtons.add(btnClearLog);
            lblTime = new JLabel("Timer:");
            txtTime = new JTextField("00:00:00");
            txtTime.setEditable(false);
            btnCancelThread = new JButton("Cancel");
            btnCancelThread.setEnabled(false);
            actionButtons.add(btnCancelThread);
        }
        
        private void initMnemonics(){
            this.btnSaveLog.setMnemonic('L');
            this.btnClearLog.setMnemonic('C');
            this.btnCancelThread.setMnemonic('T');            
        }
        
        private void addComponents(){
            this.setLayout(new BorderLayout());
            this.setBorder(BorderFactory.createLineBorder(Color.black));   

                JPanel consoleBottom = new JPanel();
                consoleBottom.setLayout(new GridLayout(3, 2));
                    consoleBottom.add(blankPanel(this.btnSaveLog));
                    consoleBottom.add(blankPanel(this.btnClearLog));

                    consoleBottom.add(blankPanel(this.lblTime));
                    consoleBottom.add(blankPanel(this.txtTime));

                    consoleBottom.add(blankPanel(this.btnCancelThread));

            this.add(lblConsole, BorderLayout.NORTH);
            this.add(new JScrollPane(txtConsole), BorderLayout.CENTER);
            this.add(consoleBottom, BorderLayout.SOUTH);            
        }
        
        public void print(List<String> chunks){
            for (String chunk: chunks){
                txtConsole.append(chunk);
            }
        }
    }
    //-------------------------------------------------------------------------
    
    
    //Action Structure
    //-------------------------------------------------------------------------
    private void startTimer(){
        SwingWorker worker = new SwingWorker<Object, String>(){

            @Override
            protected Object doInBackground() throws Exception {
                Timer t = new Timer();
                t.scheduleAtFixedRate(new TimerTask(){

                        @Override
                        public void run() {         
                            publish(getTime());
                        }
                                      
                    }
                    
                    , 1000, 1000
                );
                return null;
            }

            @Override
            protected void process(List<String> chunks){
                for (String chunk: chunks){
                    VisualProfilerView.this.console.txtTime.setText(chunk);
                }
            }
            
            /**
             * Gets the current time (HH:MM:SS) to be displayed in the
             * graphical user interface.
             * @return current time (HH:MM:SS)
             */
            private String getTime(){
                int hour = Calendar.getInstance().get(Calendar.HOUR);
                int minute = Calendar.getInstance().get(Calendar.MINUTE);
                int second = Calendar.getInstance().get(Calendar.SECOND);
                String format = "%02d";     

                return String.format(format, hour) +
                       ":" +
                       String.format(format, minute) +
                       ":" +
                       String.format(format, second);

            }            
        };
        worker.execute();   
    }    
    
    private class VisualProfilerModel{
        /**
         * Simple login feature that ensures all output files generated
         * by profiling is associated with an author.
         * <p>
         * Provides the option to exit the application if the user so chooses.
         */
        private String login(){
            String input = null;
            boolean exit = false;
            final String cancelMessage = "Do you want to exit the application?";

            //Validate
            while (!exit){
                input = JOptionPane.showInputDialog(null, "Username: ", "Login",
                                                    JOptionPane.PLAIN_MESSAGE);

                //Warning
                if (input == null || input.equals("")){
                    int cancel = JOptionPane.showConfirmDialog(null, cancelMessage,
                                                               "Cancel",
                                                               JOptionPane.YES_NO_OPTION);

                    //User wants to close program
                    if (cancel == JOptionPane.YES_OPTION){
                        exit = true;
                        System.exit(0);
                    }
                }
                //Valid, exits looop
                else{
                    exit = true;
                }
            }

            return input;
        }
        
        /**
         * Thread safe operation to start a <code>ProfileGraph2D</code>
         * for the renderer selected from the graphical user interface.
         * Uses the given settings taken from the graphical user interface
         * and saves to the specified output file.
         */    
        private void profile1D(){
            final ProfileGraph2D profiler = userSettings.getProfiler();
             
            SwingWorker worker = new SwingWorker<Object, String>(){            
                @Override
                protected Object doInBackground() throws Exception {
                    setEnabledActions(false);
                    threadStart(this);

                    ///Begin message
                    publish("--------\n");
                    publish(profiler.getGraphTitle() + ": Single Profile\n\n");

                    //Runs
                    publish("Running...\n");
                    profiler.profile();
                    publish("Running finished.\n");

                    //Saves
                    if (!Thread.currentThread().isInterrupted()){
                        publish("Saving...\n");
                        profiler.saveStatistics();
                        publish("Saving finished.\n");


                        //Displays results graph if checked
                        if (getShowGraph()){
                            publish("\nGraphing Results...\n");
                            profiler.graphStatistics();
                            publish("Graphing Complete.\n");
                        }

                        //Finish message
                        publish("\nProfiling completed.\n");
                        publish("--------\n");                    
                    }else{
                        //Finish message
                        publish("\nProfiling cancelled.\n");
                        publish("--------\n");                        
                    }

                    setEnabledActions(true);
                    threadFinish();

                    return null;
                }


                @Override
                protected void process(List<String> chunks){
                    for (String chunk: chunks){
                        print(chunk);
                    }
                }            
            };
            worker.execute();        
        }

        /**
         * Thread safe operation to start a <code>ProfileGraph2D</code>
         * for every supported renderer.
         * Uses the given settings taken from the graphical user interface
         * and saves to the specified output file.
         */
        private void profile1DAll(){

            //Profile Creation
            final List<ProfileGraph2D> profilers = new ArrayList<>();


            for (int i = 0; i < VisualProfileValues.SUPPORTED_PROFILERS.length; i++){
                profilers.add(userSettings.applySettings(userSettings.makeProfiler(VisualProfileValues.SUPPORTED_PROFILERS[i])));;

            }

            SwingWorker worker = new SwingWorker<Object, String>(){

                @Override
                protected Object doInBackground() throws Exception {
                    setEnabledActions(false);
                    threadStart(this);

                    for (final ProfileGraph2D profiler: profilers){
                        if (Thread.currentThread().isInterrupted()){
                            break;  //quits loop if interrupted
                        }

                        ///Begin message
                        publish("--------\n");
                        publish(profiler.getGraphTitle() + ": Single Profile\n\n");

                        //Runs
                        publish("Running...\n");
                        profiler.profile();
                        publish("Running finished.\n");

                        if (!Thread.currentThread().isInterrupted()){
                            //Saves
                            publish("Saving...\n");
                            profiler.saveStatistics();
                            publish("Saving finished.\n");

                            //Displays results graph if checked
                            if (getShowGraph()){
                                publish("\nGraphing Results...\n");
                                profiler.graphStatistics();
                                publish("Graphing Complete.\n");
                            }

                            //Finish message
                            publish("\nProfiling completed.\n");
                            publish("--------\n");                        
                        }else{
                            //Finish message
                            publish("\nProfiling cancelled.\n");
                            publish("--------\n");                             
                        }


                    }

                    setEnabledActions(true);
                    threadFinish();
                    return null;
                }


                @Override
                protected void process(List<String> chunks){
                    for (String chunk: chunks){
                        print(chunk);
                    }
                }            
            };
            worker.execute();   

        }

        /**
         * Thread safe operation to start a <code>MultiLevelProfiler</code>
         * profile and saves the results to an output file.
         */
        private void profile2D(){
            List<Resolution> resolutions = profile2DTable.listResolutions.getSelectedValuesList();
            List<Integer> datasetSizes = profile2DTable.listNPoints.getSelectedValuesList();
            ProfileGraph2D profiler = userSettings.getProfiler();

            if (!resolutions.isEmpty() && !datasetSizes.isEmpty() && userSettings.getProfiler() != null){
                Profile2DTableWorker worker = new Profile2DTableWorker(profiler, resolutions, datasetSizes);
                worker.execute();            
            }    
            else{
                JOptionPane.showMessageDialog(null, "Profiling was cancelled due to invalid settings.", "Run Fail", JOptionPane.ERROR_MESSAGE);
            }   
        }

        /**
         * Thread safe operation to analyze all 1D tables
         * (<code>MultiLevelProfiler</code> output, not <code>ProfileGraph2D</code> output)
         * and saves the results to an output file.
         */
        private void compare2DTables(){
            SwingWorker worker = new SwingWorker<Object, String>(){

                @Override
                protected Object doInBackground() throws Exception {
                    setEnabledActions(false);     
                    threadStart(this);
                    publish("--------\n");
                    publish("Compare Tables\n");
                    ProfileAnalysis.compareTables2D();   
                    publish("\nComparison completed.\n");
                    publish("--------\n");
                    setEnabledActions(true); 
                    threadFinish();
                    return null;
                }

                @Override
                protected void process(List<String> chunks){
                    for (String chunk: chunks){
                        print(chunk);
                    }
                }
            };
            worker.execute();
        }

        /**
         * Thread safe operation to analyze all 1D tables
         * (<code>ProfileGraph2D</code> output, not <code>MultiLevelProfiler</code> output)
         * and print the results (performance increase/decrease) to the
         * graphical user interface console.
         */
        private void analyze1DTable(){
            SwingWorker worker = new SwingWorker<Object, String>(){

                @Override
                protected Object doInBackground() throws Exception {
                    setEnabledActions(false);        
                    threadStart(this);
                    publish("--------\n");
                    publish("Comparing Single Profile Tables\n\n");

                    List<String> output = ProfileAnalysis.analyzeTables1D();
                    for (String out: output){
                        publish(out + "\n");
                    }

                    publish("\nComparison completed.\n");
                    publish("--------\n");
                    setEnabledActions(true);     
                    threadFinish();
                    return null;
                }

                @Override
                protected void process(List<String> chunks){
                    for (String chunk: chunks){
                        print(chunk);
                    }
                }
            };
            worker.execute();  
        }

        /**
         * Thread safe operation to open all selected files from the 
         * file tree with the default application to open the files.
         */
        private void openFiles(){
            SwingWorker worker = new SwingWorker<Object, String>(){

                @Override
                protected Object doInBackground() throws Exception {
                    setEnabledActions(false);     
                    threadStart(this);
                    publish("--------\n");
                    publish("Opening Files\n\n");

                    Desktop desktop = Desktop.getDesktop();
                    TreePath[] paths = fileViewer.tree.getSelectionPaths();

                    if (paths != null && desktop != null){
                        for (TreePath path: paths){
                            if (Thread.currentThread().isInterrupted()){
                                break;
                            }

                            if (path != null){
                                try{
                                    File toOpen = (File) ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
                                    desktop.open(toOpen);

                                    publish(toOpen.getName() + "...opened successfully!\n");
                                }
                                catch(IOException e){
                                    publish("Unable to open: " +
                                            ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject() +
                                            "\n"
                                           );
                                }
                                catch(ClassCastException e){
                                    //unable to open
                                }
                            }
                        }
                    }

                    if (!Thread.currentThread().isInterrupted()){
                        publish("\nFile operations completed.\n");
                        publish("--------\n");
                    }
                    else{
                        publish("\nFile operations cancelled.\n");
                        publish("--------\n");                    
                    }

                    setEnabledActions(true);    
                    threadFinish();
                    return null;

                }

                @Override
                protected void process(List<String> chunks){
                    for (String chunk: chunks){
                        print(chunk);
                    }
                }
            };
            worker.execute();   
        }

        /**
         * Thread safe operation to delete all selected files from the
         * file tree.
         */
        private void deleteFiles(){
            SwingWorker worker = new SwingWorker<Object, String>(){

                @Override
                protected Object doInBackground() throws Exception {
                    setEnabledActions(false);    
                    threadStart(this); 

                    publish("--------\n");
                    publish("Deleting Files\n\n");

                    TreePath[] paths = fileViewer.tree.getSelectionPaths();

                    if (paths != null){
                        for (TreePath path: paths){
                            if (Thread.currentThread().isInterrupted()){
                                break;  //quits loop
                            }

                            if (path != null){
                                try{
                                    File toDelete = (File) ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();

                                    Files.delete(toDelete.toPath());
                                    publish(toDelete.getName() + "...deleted successfully!\n");
                                }
                                catch(IOException e){
                                    publish("Unable to delete: " +
                                            ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject() +
                                            "\n"
                                           );
                                }
                                catch(ClassCastException e){
                                    //unable to open
                                }
                            }
                        }
                    }

                    fileViewer.reloadNodes();

                    if (!Thread.currentThread().isInterrupted()){
                        publish("\nFile operations completed.\n");
                        publish("--------\n");
                    }
                    else{
                        publish("\nFile operations cancelled.\n");
                        publish("--------\n");                    
                    }

                    setEnabledActions(true);  
                    threadFinish();
                    return null;
                }

                @Override
                protected void process(List<String> chunks){
                    for (String chunk: chunks){
                        print(chunk);
                    }
                }
            };
            worker.execute();     
        }

        /**
         * Thread safe operation to refresh nodes of the file tree.
         * Prints a 'refresh' message to the console log.
         */
        private void reloadFiles(){
            this.reloadFiles(false);
        }

        /**
         * Thread safe operation to refresh nodes of the file tree.
         * @param silent true to not print a 'refresh' message to the console log,
         *               false to print a 'refresh' message to the console log
         */
        private void reloadFiles(final boolean silent){
            SwingWorker worker = new SwingWorker<Object, String>(){

                @Override
                protected Object doInBackground() throws Exception {
                    if (!silent){
                        setEnabledActions(false);     
                        threadStart(this);
                        publish("--------\n");
                        publish("Refreshing File Browser\n");
                    }

                    fileViewer.reloadNodes();

                    if (!silent){
                        publish("Refresh completed.\n");
                        publish("--------\n");
                        setEnabledActions(true);     
                        threadFinish();
                    }

                    return null;                
                }

                @Override
                protected void process(List<String> chunks){
                    for (String chunk: chunks){
                        print(chunk);
                    }
                }
            };
            worker.execute();  
        }    
       
        /**
         * Takes a node with a <code>File</code> user object and adds
         * all subfiles as children nodes.
         * 
         * @param parentNode must contain a <code>File</code> as the user object;
         *                   adds all subfiles of the node
         */
        private void addNodes(DefaultMutableTreeNode parentNode){
            //Get subfiles of the node
            File[] subfiles = ((File)parentNode.getUserObject()).listFiles();

            //If there are files within (non-directories would not have subfiles)
            if (subfiles != null){

                //All subfiles
                for (File subfile: subfiles){

                    //Is valid subfile
                    if (subfile != null){
                        //Add node
                        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(subfile);
                        parentNode.add(childNode);
                        this.addNodes(childNode);
                    }
                }
            }
        }     
        
        /**
         * Thread safe operation to save the console log of the graphical
         * user interface.
         * The log is saved as a <b>.txt</b> file with the current timestamp.
         */
        private void saveLog(){
            SwingWorker worker = new SwingWorker<Object, String>(){

                @Override
                protected Object doInBackground() throws Exception {
                    setEnabledActions(false);                
                    threadStart(this);

                    //Where saving occurs
                    saveFile();

                    publish("--------\n");
                    publish("Saving Log\n\n");

                    //Saves beforehand to prevent this from being in log

                    publish("\nSaving completed.\n");
                    publish("--------\n");
                    setEnabledActions(true);  
                    threadFinish();
                    return null;
                }

                private void saveFile(){
                    //Creates file
                    File outputFile = new File(ProfileGraph2D.LOG_FILEPATH + 
                                      DateUtils.getDate(DateFormat.NONDELIMITED) + 
                                      "-Log.txt");

                    try {
                        outputFile.createNewFile();

                        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));

                        //Prints console
                        out.print(console.txtConsole.getText());

                        out.close();
                    } catch (IOException ex) {
                        System.err.println("Output errors exist.");
                    }                
                }

                @Override
                protected void process(List<String> chunks){
                    for (String chunk: chunks){
                        print(chunk);
                    }
                }
            };
            worker.execute();           
        }

        /**
         * Empties the console log of the graphical user interface.
         */
        private void clearLog(){
            console.txtConsole.setText("");
        }


    }

    private void threadStart(SwingWorker worker){
        if (worker == null){
            throw new IllegalArgumentException("Must have a non-null thread.");
        }

        thread = worker;
        console.btnCancelThread.setEnabled(true);
    }

    private void threadFinish(){
        thread = null;
        console.btnCancelThread.setEnabled(false);

        setEnabledActions(true);
    }

    private void cancelThread(){
        if (thread != null){
            thread.cancel(true);
            console.btnCancelThread.setEnabled(false);

            SwingWorker worker = new SwingWorker(){

                @Override
                protected Object doInBackground() throws Exception {
                    publish("\nAction Cancelled\n");                                
                    publish("--------\n");
                    return null;
                }

            };
            worker.execute();            
        }
    }
        
    /**
     * Creates a thread safe <code>SwingWorker</code> that performs
     * a <code>MultiLevelProfiler</code> profile and prints
     * the results to the console log of the graphical user interface
     * as the results are received.
     */
    private class Profile2DTableWorker extends SwingWorker<Object, String>{
        private Profile2DTableWorker.VisualMultiLevelProfiler multiProfiler;
        
        public Profile2DTableWorker(ProfileGraph2D profiler, List<Resolution> resolutions, List<Integer> datasetSizes){
            setEnabledActions(false);        
            threadStart(this);
            publish("--------\n");
            publish(profiler.getGraphTitle() + "\n\n");
            
            String strAuthor = settingsPanel.txtAuthorMessage.getText();
            String saveMessage = settingsPanel.txtSaveMessage.getText();   
            
            this.multiProfiler = new Profile2DTableWorker.VisualMultiLevelProfiler(profiler);
            this.multiProfiler.getSaveSettings().setAuthorMessage(strAuthor);
            this.multiProfiler.getSaveSettings().setSaveMessage(saveMessage);
            this.multiProfiler.setImageSizes(resolutions);
            this.multiProfiler.setDatasetSizes(datasetSizes);
        }
        
        @Override
        protected Object doInBackground() throws Exception {
            this.multiProfiler.profile();
            
            if (!Thread.currentThread().isInterrupted()){
                this.multiProfiler.saveStatistics();
                publish("\nProfiling complete." + "\n");
                publish("--------\n");
            }
            else{
                publish("\nProfiling cancelled." + "\n");
                publish("--------\n");                
            }
            
            setEnabledActions(true);
            threadFinish();
            return true;
        }
        
        @Override
        protected void process(List<String> chunks){
            for (String chunk: chunks){
                print(chunk);
            }
        }        
                
        private class VisualMultiLevelProfiler extends MultiLevelProfiler{
            public VisualMultiLevelProfiler(ProfileGraph2D profiler){
                super(profiler);
            }

            @Override
            public void processTimeWarning(int estimatedTime){
                Profile2DTableWorker.this.publish("The estimated run time is " + estimatedTime + " seconds." + "\n\n");
            }

            @Override
            public void processPreResult(Resolution resolution, int datasetSize){
                //Publishes
                Profile2DTableWorker.this.publish(resolution + ": " + datasetSize + ":" + "    ");
            }

            @Override
            public void processResult(Resolution resolution, int datasetSize, Statistics stats){
                Profile2DTableWorker.this.publish(stats.getAverageTime() + "ms" + "\n");                    
            }        
        };        
    };    
    //-------------------------------------------------------------------------
    
    
    public VisualProfilerView(){
        initPanel();
        initComponents();

        addComponents();
        addListeners();
        
        finalizePanel();
    }
    
    
    //Panel Setup
    //-------------------------------------------------------------------------
    
    private void initPanel(){
        this.setLayout(new BorderLayout());
    }
    
    /**
     * Initializes all graphical user interface components.
     */
    private void initComponents(){
        //Data
        tabs = new JTabbedPane();
        actionButtons = new ArrayList<>();
        model = new VisualProfilerModel();
        userSettings = new UserSettings();
        
        //Panels
        settingsPanel = new SettingsPanel();
        profile1DTable = new Profile1DTable();
        profile2DTable = new Profile2DTable();
        analyzePanel = new AnalyzePanel();
        fileViewer = new FileViewer();
        console = new Console();
    }    

    /**
     * Adds all graphical user interface components to the <code>JFrame</code>.
     */
    private void addComponents(){
        //Tabs
        tabs.addTab("Profile 1D Table", profile1DTable);
        tabs.addTab("Profile 2D Table", profile2DTable);
        tabs.addTab("Control Panel", analyzePanel);
        tabs.addTab("File Browser", fileViewer);
        
        //Add to panel hiearchy
        this.add(settingsPanel, BorderLayout.NORTH);
        this.add(tabs, BorderLayout.CENTER);
        this.add(console, BorderLayout.SOUTH);                
    }
    
    private void addListeners(){
        ActionListener listener = new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Object o = e.getSource();
                
                if (o == profile1DTable.btnProfile1D){
                    model.profile1D();
                }
                else if (o == profile1DTable.btnProfile1DAll){
                    model.profile1DAll();
                }
                else if (o == profile2DTable.btnProfile2D){
                    model.profile2D();
                }
                else if (o == analyzePanel.btnCompare2DTables){
                    model.compare2DTables();
                }
                else if (o == analyzePanel.btnAnalyze1DTable){
                    model.analyze1DTable();
                }
                else if (o == fileViewer.btnOpenFiles){
                    model.openFiles();
                }
                else if (o == fileViewer.btnDeleteFiles){
                    model.deleteFiles();
                }
                else if (o == fileViewer.btnReloadFiles){
                    model.reloadFiles();
                }
                else if (o == console.btnSaveLog){
                    model.saveLog();
                }
                else if (o == console.btnClearLog){
                    model.clearLog();
                }
                else if (o == console.btnCancelThread){
                    cancelThread();
                }
            }
            
        };
        
        for (JButton button: this.actionButtons){
            button.addActionListener(listener);
        }
        
        tabs.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                model.reloadFiles(true);
            }
            
        });
        
        settingsPanel.listRendererTypes.addItemListener(new ItemListener(){

           @Override
           public void itemStateChanged(ItemEvent e) {
               if (e.getStateChange() == ItemEvent.SELECTED){
                   reloadUpdateVariations();
               }
           }

        });        
    }
    
    private void finalizePanel(){
        setAuthor(model.login());
        reloadUpdateVariations();
        model.reloadFiles(true);
        startTimer();
    }
    
    //-------------------------------------------------------------------------
    
    
    //Getters
    //-------------------------------------------------------------------------
    
    public boolean getShowGraph(){
        return profile1DTable.chkShowGraph.isSelected();
    }

    private class UserSettings{
        
        
        public Integer getTestTime(){
            String strTestTime = settingsPanel.txtTestTime.getText();

            Integer testTime;

            try{
                testTime = Integer.parseInt(strTestTime);

                if (testTime <= 0){
                    throw new NumberFormatException();
                }

                return testTime;
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Enter a positive non-zero integer for test time.", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }            
        }
        
        public Integer getMaxTries(){
            String strMaxAttempts = settingsPanel.txtMaxAttempts.getText();
            int maxAttempts;
        
            try{
                maxAttempts = Integer.parseInt(strMaxAttempts);

                if (maxAttempts <= 0){
                    throw new NumberFormatException();
                }
                
                return maxAttempts;
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Enter a positive non-zero integer for max attempts.", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }            
        }
        
        public TimeType getTimeType(){
            return (StopWatch.TimeType) settingsPanel.listTimeTypes.getSelectedItem();
        }
        
        public String getUpdate(){
            return settingsPanel.listUpdateTypes.getSelectedItem().toString();
        }
        
        
        public ProfileGraph2D getProfiler(){
            ProfileGraph2D renderer = selectedProfiler();
            if (renderer == null){ return null; }
            
            return applySettings(renderer);
        }
        
        public ProfileGraph2D selectedProfiler(){
            String strClass = settingsPanel.listRendererTypes.getSelectedItem().toString();
            return makeProfiler(strClass);            
        }

        public ProfileGraph2D makeProfiler(String strClass){
            return VisualProfileValues.factory(strClass);
        }
        
        public ProfileGraph2D applySettings(ProfileGraph2D renderer){
            renderer.getProfileSettings().setTestTime(getTestTime());
            renderer.getProfileSettings().setMaxTries(getMaxTries());
            renderer.getProfileSettings().setTimeType(getTimeType());
            renderer.getRenderSettings().setUpdate(getUpdate());
            
            return renderer;
        }
    }
    
    //-------------------------------------------------------------------------
    
    
    //Setters
    //-------------------------------------------------------------------------

    public void setAuthor(String author){
        this.settingsPanel.txtAuthorMessage.setText(author);
    }
        
    public void setEnabledActions(boolean enabled){
        for (JButton button: this.actionButtons){
            button.setEnabled(enabled);
        }
    }

    //-------------------------------------------------------------------------

    
    //Actions
    //-------------------------------------------------------------------------
    
    public void print(String chunk){
        ArrayList<String> tmp = new ArrayList<>();
        tmp.add(chunk);
        console.print(tmp);
    }
    
    public void print(List<String> chunks){
        console.print(chunks);
    }
    
    public void reloadUpdateVariations(){
        if (userSettings.selectedProfiler() == null){
            return;
        }
        
        DefaultComboBoxModel tmp = new DefaultComboBoxModel(
            userSettings.selectedProfiler().getVariations().keySet().toArray()
        );
        this.settingsPanel.listUpdateTypes.setModel(tmp);        
    }    
    
    //-------------------------------------------------------------------------
   
    
    //Helper
    //-------------------------------------------------------------------------
    
    public static JFrame makeFrame(){
            JFrame frame = new JFrame(VisualProfileValues.FRAME_TITLE);
            frame.add(new VisualProfilerView());
            frame.pack();
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);  //Centers    
            
            return frame;
    }
    
    private JPanel blankPanel(Component itemToAdd){
        JPanel tmp = new JPanel();
        tmp.add(itemToAdd);
        return tmp;
    }

    //------------------------------------------------------------------------- 
    
    
    //Main
    //-------------------------------------------------------------------------
    
    /**
     * Constructs a thread safe <code>VisualProfiler</code> JFrame.
     */
    public static void invokeVisualAid(){
        EventQueue.invokeLater(new Runnable(){

            @Override
            public void run() {
                makeFrame();
            }
            
        });
    }  
    
    /**
     * Constructs a <code>VisualProfiler</code> to provide
     * graphical user interface options to profile renderers.
     * @param args no effect
     */
    public static void main(String[] args){
        VisualProfilerView.invokeVisualAid();
    }    
    
    //-------------------------------------------------------------------------     
}
