/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample;

import org.diirt.vtype.VStatistics;
import org.diirt.datasource.sim.SimulationDataSource;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReaderListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.diirt.datasource.PVReaderEvent;
import static org.diirt.datasource.vtype.ExpressionLanguage.*;
import static org.diirt.datasource.util.Executors.*;
import static org.diirt.util.time.TimeDuration.*;

/**
 *
 * @author carcassi
 */
public class MockTablePVFrame extends javax.swing.JFrame {

    /** Creates new form MockPVFrame */
    public MockTablePVFrame() {
        PVManager.setDefaultNotificationExecutor(swingEDT());
        PVManager.setDefaultDataSource(SimulationDataSource.simulatedData());
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        pvTable = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        scanRateSpinner = new javax.swing.JSpinner();
        createPVButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        nPVSpinner = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        updateRateSpinner = new javax.swing.JSpinner();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pvTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Average", "Standard deviation", "Minimum", "Maximum"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(pvTable);

        jLabel6.setText("UI scan rate (Hz):");

        scanRateSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, 50, 1));

        createPVButton.setText("Create ");
        createPVButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createPVButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("N PVs:");

        nPVSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));

        jLabel3.setText("PV update rate (Hz):");

        updateRateSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, 1000, 1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 719, Short.MAX_VALUE)
                    .addComponent(createPVButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 719, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scanRateSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 629, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nPVSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 684, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(updateRateSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 739, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(updateRateSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(scanRateSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nPVSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(createPVButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    List<PVReader<VStatistics>> pvs = new ArrayList<PVReader<VStatistics>>();

    private void createPVButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createPVButtonActionPerformed
        if (pvs != null && !pvs.isEmpty()) {
            for (PVReader<VStatistics> pv : pvs) {
                pv.close();
            }
        }

        int nPvs = ((Integer) nPVSpinner.getModel().getValue()).intValue();
        double timeIntervalSec = (1.0 / ((Integer) updateRateSpinner.getModel().getValue()).intValue());
        String pvName = "gaussian(0.0, 1.0, " + timeIntervalSec + ")";
        int scanRate = ((Integer) scanRateSpinner.getModel().getValue()).intValue();

        final DefaultTableModel model = (DefaultTableModel) pvTable.getModel();
        model.setRowCount(nPvs);
        pvs.clear();
        for (int n = 0; n < nPvs; n++) {
            final PVReader<VStatistics> pv = PVManager.read(statisticsOf(vDouble(pvName))).maxRate(ofHertz(scanRate));
            final int nRow = n;
            pv.addPVReaderListener(new PVReaderListener<VStatistics>() {
            @Override
                public void pvChanged(PVReaderEvent<VStatistics> event) {
                    model.setValueAt(pv.getValue().getAverage(), nRow, 0);
                    model.setValueAt(pv.getValue().getStdDev(), nRow, 1);
                    model.setValueAt(pv.getValue().getMin(), nRow, 2);
                    model.setValueAt(pv.getValue().getMax(), nRow, 3);
                }
            });
            pvs.add(pv);
        }

    }//GEN-LAST:event_createPVButtonActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MockTablePVFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton createPVButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSpinner nPVSpinner;
    private javax.swing.JTable pvTable;
    private javax.swing.JSpinner scanRateSpinner;
    private javax.swing.JSpinner updateRateSpinner;
    // End of variables declaration//GEN-END:variables

}
