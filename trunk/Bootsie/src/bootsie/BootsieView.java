/*
 * BootsieView.java
 */

package bootsie;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The application's main frame.
 */
public class BootsieView extends FrameView {

   private boolean isMasterAdded = false;

    public BootsieView(SingleFrameApplication app) {
        super(app);

        initComponents();
        this.jScrollPane1.repaint();
        

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        //progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    //progressBar.setVisible(true);
                    //progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                   // progressBar.setVisible(false);
                    //progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    //progressBar.setVisible(true);
                    //progressBar.setIndeterminate(false);
                    //progressBar.setValue(value);
                }
            }
        });
    }

    public PopulationMatrixModel newDataModel(String n){
       PopulationMatrixModel newModel;
       DataSetPanel setPanel = new DataSetPanel(true);
       setPanel.setDataName(n);
       if (isMasterAdded){
          //do nothing
       } else {
          DataSetPane.add(DataSetMasterPanel.getInstance());
          isMasterAdded = true;
       }
       DataSetPane.add(setPanel);
       newModel = new PopulationMatrixModel(n, setPanel);
       setPanel.addModel(newModel);
       PopulationMatrixModelCollection.getInstance().addMatrix(newModel);
       return newModel;
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = BootsieApp.getApplication().getMainFrame();
            aboutBox = new BootsieAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        BootsieApp.getApplication().show(aboutBox);
    }

    private void openFile() {
      //get file or file array and apply app parse method
      JFileChooser fc = new JFileChooser();
      fc.setMultiSelectionEnabled(true);
      FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
      fc.setFileFilter(filter);
      int returnVal = fc.showOpenDialog(BootsieApp.getApplication().getMainFrame());
      if (returnVal == JFileChooser.APPROVE_OPTION) {
         File[] files = fc.getSelectedFiles();
         BootsieApp.parseFile(files);
         EstimationReportLabel.setText("Estimating...");
         GoButton.setEnabled(true);
         ExportMenu.setEnabled(true);
         new Thread(new BootsieEstimator()).start();
      }
   }

    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        ExportMenu = new javax.swing.JMenu();
        exportPopgene = new javax.swing.JMenuItem();
        exportNtsys = new javax.swing.JMenuItem();
        exportArlequin = new javax.swing.JMenuItem();
        exportPaup = new javax.swing.JMenuItem();
        exportTab = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        helpMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        reportArea = new javax.swing.JTextArea();
        EstimationReportLabel = new javax.swing.JLabel();
        GoButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        DataSetPane = new javax.swing.JPanel();
        helpDialog = new javax.swing.JDialog();
        helpScrollPane = new javax.swing.JScrollPane();
        helpTextPane = new javax.swing.JTextPane();

        menuBar.setName("menuBar"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(bootsie.BootsieApp.class).getContext().getResourceMap(BootsieView.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        openMenuItem.setText(resourceMap.getString("openMenuItem.text")); // NOI18N
        openMenuItem.setName("openMenuItem"); // NOI18N
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);

        jSeparator1.setName("jSeparator1"); // NOI18N
        fileMenu.add(jSeparator1);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(bootsie.BootsieApp.class).getContext().getActionMap(BootsieView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        ExportMenu.setText(resourceMap.getString("ExportMenu.text")); // NOI18N
        ExportMenu.setEnabled(false);
        ExportMenu.setName("ExportMenu"); // NOI18N

        exportPopgene.setText(resourceMap.getString("exportPopgene.text")); // NOI18N
        exportPopgene.setActionCommand(resourceMap.getString("exportPopgene.actionCommand")); // NOI18N
        exportPopgene.setName("exportPopgene"); // NOI18N
        exportPopgene.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportToPopgen(evt);
            }
        });
        ExportMenu.add(exportPopgene);

        exportNtsys.setText(resourceMap.getString("exportNtsys.text")); // NOI18N
        exportNtsys.setActionCommand(resourceMap.getString("exportNtsys.actionCommand")); // NOI18N
        exportNtsys.setName("exportNtsys"); // NOI18N
        exportNtsys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportToNtsys(evt);
            }
        });
        ExportMenu.add(exportNtsys);

        exportArlequin.setText(resourceMap.getString("exportArlequin.text")); // NOI18N
        exportArlequin.setActionCommand(resourceMap.getString("exportArlequin.actionCommand")); // NOI18N
        exportArlequin.setName("exportArlequin"); // NOI18N
        exportArlequin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportToArl(evt);
            }
        });
        ExportMenu.add(exportArlequin);

        exportPaup.setText(resourceMap.getString("exportPaup.text")); // NOI18N
        exportPaup.setActionCommand(resourceMap.getString("exportPaup.actionCommand")); // NOI18N
        exportPaup.setName("exportPaup"); // NOI18N
        exportPaup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportPaupActionPerformed(evt);
            }
        });
        ExportMenu.add(exportPaup);

        exportTab.setText(resourceMap.getString("exportTab.text")); // NOI18N
        exportTab.setActionCommand(resourceMap.getString("exportTab.actionCommand")); // NOI18N
        exportTab.setName("exportTab"); // NOI18N
        exportTab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportAsTabs(evt);
            }
        });
        ExportMenu.add(exportTab);

        menuBar.add(ExportMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        helpMenuItem.setText(resourceMap.getString("helpMenuItem.text")); // NOI18N
        helpMenuItem.setActionCommand(resourceMap.getString("helpMenuItem.actionCommand")); // NOI18N
        helpMenuItem.setName("helpMenuItem"); // NOI18N
        helpMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(helpMenuItem);

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setText(resourceMap.getString("aboutMenuItem.text")); // NOI18N
        aboutMenuItem.setActionCommand(resourceMap.getString("aboutMenuItem.actionCommand")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showHelpDialog(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N
        statusPanel.setPreferredSize(new java.awt.Dimension(714, 500));

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        reportArea.setColumns(20);
        reportArea.setEditable(false);
        reportArea.setRows(5);
        reportArea.setName("reportArea"); // NOI18N
        jScrollPane2.setViewportView(reportArea);

        EstimationReportLabel.setText(resourceMap.getString("EstimationReportLabel.text")); // NOI18N
        EstimationReportLabel.setName("EstimationReportLabel"); // NOI18N

        GoButton.setAction(actionMap.get("beginAnalysis")); // NOI18N
        GoButton.setText(resourceMap.getString("GoButton.text")); // NOI18N
        GoButton.setActionCommand(resourceMap.getString("GoButton.actionCommand")); // NOI18N
        GoButton.setFocusPainted(false);
        GoButton.setFocusable(false);
        GoButton.setName("GoButton"); // NOI18N
        GoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                beginAnalysis(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        DataSetPane.setName("DataSetPane"); // NOI18N
        DataSetPane.setLayout(new javax.swing.BoxLayout(DataSetPane, javax.swing.BoxLayout.PAGE_AXIS));
        jScrollPane1.setViewportView(DataSetPane);

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 684, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 684, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                        .addComponent(statusMessageLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 684, Short.MAX_VALUE)
                        .addComponent(statusAnimationLabel))
                    .addGroup(statusPanelLayout.createSequentialGroup()
                        .addComponent(EstimationReportLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 5, Short.MAX_VALUE)
                        .addComponent(GoButton)))
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EstimationReportLabel)
                    .addComponent(GoButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(statusMessageLabel)
                        .addComponent(statusAnimationLabel))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13))
        );

        helpDialog.setTitle(resourceMap.getString("Bootsie Help.title")); // NOI18N
        helpDialog.setName("Bootsie Help"); // NOI18N

        helpScrollPane.setName("helpScrollPane"); // NOI18N

        helpTextPane.setContentType(resourceMap.getString("helpTextPane.contentType")); // NOI18N
        helpTextPane.setEditable(false);
        helpTextPane.setText(resourceMap.getString("helpTextPane.text")); // NOI18N
        helpTextPane.setName("helpTextPane"); // NOI18N
        helpScrollPane.setViewportView(helpTextPane);

        javax.swing.GroupLayout helpDialogLayout = new javax.swing.GroupLayout(helpDialog.getContentPane());
        helpDialog.getContentPane().setLayout(helpDialogLayout);
        helpDialogLayout.setHorizontalGroup(
            helpDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(helpDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(helpScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE)
                .addContainerGap())
        );
        helpDialogLayout.setVerticalGroup(
            helpDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(helpDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(helpScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                .addGap(36, 36, 36))
        );

        setComponent(statusPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
       openFile();
       
    }//GEN-LAST:event_openMenuItemActionPerformed

    private void exportToPopgen(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportToPopgen
       
       ExportDialog exportDialog = new ExportDialog(this.getFrame(), true, new PopgeneDataExporter());
       exportDialog.setVisible(true);
    }//GEN-LAST:event_exportToPopgen

    private void exportToNtsys(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportToNtsys
       
       ExportDialog exportDialog = new ExportDialog(this.getFrame(), true, new NtsysDataExporter());
       exportDialog.setVisible(true);
    }//GEN-LAST:event_exportToNtsys

    private void exportToArl(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportToArl
      
       ExportDialog exportDialog = new ExportDialog(this.getFrame(), true, new ArlequinDataExporter());
       exportDialog.setVisible(true);
    }//GEN-LAST:event_exportToArl

    private void exportAsTabs(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportAsTabs
       
       ExportDialog exportDialog = new ExportDialog(this.getFrame(), true, new TabDelimitDataExporter());
       exportDialog.setVisible(true);
    }//GEN-LAST:event_exportAsTabs

    private void beginAnalysis(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_beginAnalysis
        PopulationMatrixModelCollection.getInstance().sendGlobalActionEvent(evt);
        GoButton.setEnabled(false);
        openMenuItem.setEnabled(false);
        DataSetMasterPanel.getInstance().setVisible(false);
        GoButton.setText("Analyzing...");
        try {
            BootsieBootstrapper.getInstance().start();
        } catch (Exception ex){
            BootsieApp.getApplication().report("Unable to begin computation.");
        }
    }//GEN-LAST:event_beginAnalysis

    private void showHelpDialog(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showHelpDialog

    }//GEN-LAST:event_showHelpDialog

    private void helpMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpMenuItemActionPerformed
        JFrame mainFrame = BootsieApp.getApplication().getMainFrame();
        helpDialog.setLocationRelativeTo(mainFrame);
        BootsieApp.getApplication().show(helpDialog);
    }//GEN-LAST:event_helpMenuItemActionPerformed

    private void exportPaupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportPaupActionPerformed
        ExportDialog exportDialog = new ExportDialog(this.getFrame(), true, new PaupDataExporter());
       exportDialog.setVisible(true);
    }//GEN-LAST:event_exportPaupActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel DataSetPane;
    private javax.swing.JLabel EstimationReportLabel;
    private javax.swing.JMenu ExportMenu;
    private javax.swing.JButton GoButton;
    private javax.swing.JMenuItem exportArlequin;
    private javax.swing.JMenuItem exportNtsys;
    private javax.swing.JMenuItem exportPaup;
    private javax.swing.JMenuItem exportPopgene;
    private javax.swing.JMenuItem exportTab;
    private javax.swing.JDialog helpDialog;
    private javax.swing.JMenuItem helpMenuItem;
    private javax.swing.JScrollPane helpScrollPane;
    private javax.swing.JTextPane helpTextPane;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JTextArea reportArea;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
    private DataSetMasterPanel masterPanel = DataSetMasterPanel.getInstance();

   void addReport(String s) {
      reportArea.append(s);
      reportArea.append("\n");
   }
   
   void setEstimate(String s){
       EstimationReportLabel.setText(s);
   }

    void finished() {
        GoButton.setVisible(false);
    }
}
