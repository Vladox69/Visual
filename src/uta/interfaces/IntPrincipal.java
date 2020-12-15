/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uta.interfaces;

import java.awt.BorderLayout;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author ASUS
 */
public class IntPrincipal extends javax.swing.JFrame {

    /**
     * Creates new form IntPrincipal
     */
    IntEstudiantes estudiantes = new IntEstudiantes();
    IntReporte reportes=new IntReporte();
    
    public IntPrincipal() {
        initComponents();
    }

    public void bloquearReportes() {
        mReportes.setEnabled(false);
    }

    public void logout() {
        IntLogin login = new IntLogin();
        login.setVisible(true);
        this.dispose();
    }

    public void abrirIntEstudiantes() {
        if (!(estudiantes.isVisible())) {
            dkInterfaces.add(estudiantes);
            estudiantes.setVisible(true);
            estudiantes.setClosable(true);
        }
    }
    
    public void abrirIntReportes(){
        if(!(reportes.isVisible())){
            dkInterfaces.add(reportes);
            reportes.setVisible(true);
            reportes.setClosable(true);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dkInterfaces = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        mInterfaces = new javax.swing.JMenu();
        miEstudiantes = new javax.swing.JMenuItem();
        mReportes = new javax.swing.JMenu();
        miGeneral = new javax.swing.JMenuItem();
        mSesion = new javax.swing.JMenu();
        miLogout = new javax.swing.JMenuItem();
        miSalir = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout dkInterfacesLayout = new javax.swing.GroupLayout(dkInterfaces);
        dkInterfaces.setLayout(dkInterfacesLayout);
        dkInterfacesLayout.setHorizontalGroup(
            dkInterfacesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1228, Short.MAX_VALUE)
        );
        dkInterfacesLayout.setVerticalGroup(
            dkInterfacesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 794, Short.MAX_VALUE)
        );

        mInterfaces.setText("Interfaces");

        miEstudiantes.setText("Estudiantes");
        miEstudiantes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miEstudiantesActionPerformed(evt);
            }
        });
        mInterfaces.add(miEstudiantes);

        jMenuBar1.add(mInterfaces);

        mReportes.setText("Reportes");

        miGeneral.setText("General");
        miGeneral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miGeneralActionPerformed(evt);
            }
        });
        mReportes.add(miGeneral);

        jMenuBar1.add(mReportes);

        mSesion.setText("Cerrar sesión");
        mSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mSesionActionPerformed(evt);
            }
        });

        miLogout.setText("Logout");
        miLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miLogoutActionPerformed(evt);
            }
        });
        mSesion.add(miLogout);

        miSalir.setText("Salir");
        miSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSalirActionPerformed(evt);
            }
        });
        mSesion.add(miSalir);

        jMenuBar1.add(mSesion);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dkInterfaces)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dkInterfaces)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void miEstudiantesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miEstudiantesActionPerformed
        abrirIntEstudiantes();
    }//GEN-LAST:event_miEstudiantesActionPerformed

    private void mSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mSesionActionPerformed

    }//GEN-LAST:event_mSesionActionPerformed

    private void miLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miLogoutActionPerformed
        logout();
    }//GEN-LAST:event_miLogoutActionPerformed

    private void miSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_miSalirActionPerformed

    private void miGeneralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miGeneralActionPerformed
        abrirIntReportes();
    }//GEN-LAST:event_miGeneralActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(IntPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IntPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IntPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IntPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IntPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane dkInterfaces;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu mInterfaces;
    private javax.swing.JMenu mReportes;
    private javax.swing.JMenu mSesion;
    private javax.swing.JMenuItem miEstudiantes;
    private javax.swing.JMenuItem miGeneral;
    private javax.swing.JMenuItem miLogout;
    private javax.swing.JMenuItem miSalir;
    // End of variables declaration//GEN-END:variables
}