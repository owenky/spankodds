package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.User;
import com.sia.client.ui.control.SportsTabPane;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;

import static com.sia.client.config.Utils.log;

public class ChartHome  {
    static int fileinit;
    static String filename = "No File Selected";
    static int warnamount = 1;
    static int updatetime = 10;
    static File f1;
    static int fileCount = 0;
    User u = AppController.getUser();
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private final AnchoredLayeredPane anchoredLayeredPane;
    public ChartHome(SportsTabPane stp) {
        anchoredLayeredPane = new AnchoredLayeredPane(stp);
        anchoredLayeredPane.setTitle("Chart");
        anchoredLayeredPane.setHelpUrl(SiaConst.CHARTHELPURL);
    }
    public void show() {
        anchoredLayeredPane.openAndCenter(getUserComponent(),new Dimension(600,350),false);
    }
    private JComponent getUserComponent() {
        JPanel userComp = new JPanel();
        filename = u.getChartFileName();
        String[] WarnValues = {"1", "25", "50", "100", "250", "500", "1000", "2000", "3000", "4000", "5000", "10000", "15000", "20000", "30000", "40000", "50000", "100000", "150000", "200000", "300000", "400000", "500000"};
        String[] updatetimevalues = {"5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60"};
        jButton1 = new JButton();
        jButton2 = new JButton();
        jButton3 = new JButton();
        jComboBox1 = new JComboBox(WarnValues);
        jComboBox1.setSelectedItem(u.getChartMinAmtNotify() + "");
        jComboBox2 = new JComboBox(updatetimevalues);
        jComboBox2.setSelectedItem(u.getChartSecsRefresh() + "");
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jLabel5 = new JLabel();
        jLabel6 = new JLabel();
        jLabel7 = new JLabel();

        jButton1.setText("Choose File");
        jButton1.setActionCommand("Choose");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setText("Cancel");
        jButton2.setActionCommand("cancel");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jButton3.setText("Ok");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText(u.getChartFileName());
        if (u.getChartFileName().equals("No File Selected")) {
            jButton3.setEnabled(false);
        }

        jLabel2.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel2.setText("Set Min Warn $ Limit   ");

        jLabel3.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel3.setText("Set Chart Updater Time");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Current Limit :");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText(u.getChartMinAmtNotify() + "");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText(u.getChartSecsRefresh() + "");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Sec");

        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);

        jComboBox2.addActionListener(this::jComboBox2ActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(userComp);
        userComp.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel3)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel6)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel7))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel2)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(8, 8, 8)
                                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(24, 24, 24)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel1)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel4)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel5)))))
                                .addContainerGap(63, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(19, 19, 19)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel5))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(42, 42, 42))
        );

        return userComp;
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser jfc = new JFileChooser();
        jfc.showOpenDialog(null);
        f1 = jfc.getSelectedFile();
        fileinit = 1;
        String fname = f1.getName().substring(f1.getName().lastIndexOf('.'), f1.getName().length());
        if (fname.equals(".csv")) {
            fileCount = 0;
            ChartHome.filename = f1.getPath();
            jLabel1.setText(ChartHome.filename);

            jButton3.setEnabled(true);
        } else if (fname.equals(".txt")) {
            try {
                fileCount = 1;

                jLabel1.setText(f1.getPath());
                ChartHome.filename = f1.getPath();
                jButton3.setEnabled(true);
            } catch (Exception e) {
                jLabel1.setText("file having invalid info");
                jButton3.setEnabled(false);
                log(e);
            }
        } else {
            jLabel1.setText("file format not suppotred");
            jButton3.setEnabled(false);
        }

    }

    private void jButton2ActionPerformed(ActionEvent evt) {
        close();
    }

    private void jButton3ActionPerformed(ActionEvent evt) {
        u.setChartMinAmtNotify(ChartHome.warnamount);
        u.setChartSecsRefresh(ChartHome.updatetime);
        u.setChartFileName(ChartHome.filename);

        AppController.getChartChecker().startOver();

        close();
    }
    private void close() {
        anchoredLayeredPane.close();
    }
    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (jComboBox1.getSelectedItem().toString().equals("Select")) {
            ChartHome.warnamount = 1;
        } else {
            ChartHome.warnamount = Integer.parseInt(jComboBox1.getSelectedItem().toString());
        }

        jLabel5.setText(ChartHome.warnamount + "");
        ;

    }

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        ChartHome.updatetime = Integer.parseInt(jComboBox2.getSelectedItem().toString());

        jLabel6.setText(ChartHome.updatetime + "");

    }
}
