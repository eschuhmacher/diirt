/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample.graphene;

import java.awt.EventQueue;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.diirt.graphene.Graph2DRendererUpdate;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import org.diirt.datasource.graphene.Graph2DExpression;
import org.diirt.datasource.graphene.Graph2DResult;
import org.diirt.datasource.sample.SetupUtil;
import static org.diirt.datasource.util.Executors.swingEDT;
import static org.diirt.util.time.TimeDuration.ofHertz;
import org.diirt.vtype.ValueUtil;

/**
 *
 * @author carcassi
 */
public abstract class BaseGraphApp<T extends Graph2DRendererUpdate<T>> extends javax.swing.JFrame {

    /**
     * Creates new form SimpleScatterGraph
     */
    public BaseGraphApp() {
        SetupUtil.defaultCASetupForSwing();
        initComponents();
        imagePanel.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                if (graph != null) {
                    graph.update(graph.newUpdate()
                            .imageHeight(Math.max(1, imagePanel.getHeight()))
                            .imageWidth(Math.max(1, imagePanel.getWidth())));
                }
            }
        });
        imagePanel.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                onMouseMove(e);
            }
            
        });
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                dataFormulaField.setSelectedIndex(0);
            }
        });
    }
    
    protected void onMouseMove(MouseEvent e) {
        
    }
    
    private PVReader<Graph2DResult> pv;
    protected Graph2DExpression<T> graph;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        dataFormulaField = new javax.swing.JComboBox<String>();
        lastErrorField = new javax.swing.JTextField();
        imagePanel = new org.diirt.datasource.sample.ImagePanel();
        configureButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Data:");

        dataFormulaField.setEditable(true);
        dataFormulaField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dataFormulaFieldActionPerformed(evt);
            }
        });

        lastErrorField.setEditable(false);

        javax.swing.GroupLayout imagePanelLayout = new javax.swing.GroupLayout(imagePanel);
        imagePanel.setLayout(imagePanelLayout);
        imagePanelLayout.setHorizontalGroup(
            imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        imagePanelLayout.setVerticalGroup(
            imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 461, Short.MAX_VALUE)
        );

        configureButton.setText("Configure");
        configureButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                configureButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(imagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lastErrorField, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dataFormulaField, 0, 504, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(configureButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(dataFormulaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(configureButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(imagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lastErrorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dataFormulaFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dataFormulaFieldActionPerformed
        reconnect();
    }//GEN-LAST:event_dataFormulaFieldActionPerformed

    protected void reconnect() {
        if (pv != null) {
            pv.close();
            imagePanel.setImage(null);
            graph = null;
        }
        
        if (dataFormulaField.getSelectedItem() == null || dataFormulaField.getSelectedItem().toString().trim().isEmpty()) {
            return;
        }
        
        graph = createExpression(dataFormulaField.getSelectedItem().toString());
        
        graph.update(graph.newUpdate().imageHeight(imagePanel.getHeight())
                .imageWidth(imagePanel.getWidth()));
        pv = PVManager.read(graph)
                .notifyOn(swingEDT())
                .readListener(new PVReaderListener<Graph2DResult>() {

                    @Override
                    public void pvChanged(PVReaderEvent<Graph2DResult> event) {
                        setLastError(pv.lastException());
                        if (pv.getValue() != null) {
                            BufferedImage image = ValueUtil.toImage(pv.getValue().getImage());
                            imagePanel.setImage(image);
                        }
                    }
                })
                .maxRate(ofHertz(50));
    }
    
    protected abstract Graph2DExpression<T> createExpression(String dataFormula);
    protected abstract void openConfigurationDialog();
    
    private void configureButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_configureButtonActionPerformed
        openConfigurationDialog();
    }//GEN-LAST:event_configureButtonActionPerformed

    private void setLastError(Exception ex) {
        if (ex != null) {
            lastErrorField.setText(ex.getMessage());
            ex.printStackTrace();
        } else {
            lastErrorField.setText("");
        }
    }
    
    public static void main(final Class<? extends BaseGraphApp> clazz) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BaseGraphApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BaseGraphApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BaseGraphApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BaseGraphApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    clazz.newInstance().setVisible(true);
                } catch (InstantiationException ex) {
                    Logger.getLogger(BaseGraphApp.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(BaseGraphApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton configureButton;
    protected javax.swing.JComboBox<String> dataFormulaField;
    private org.diirt.datasource.sample.ImagePanel imagePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField lastErrorField;
    // End of variables declaration//GEN-END:variables
}
