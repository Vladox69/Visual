/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uta.interfaces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ASUS
 */
public class IntCursos extends javax.swing.JFrame {

    /**
     * Creates new form IntCursos
     */
    public IntCursos() {
        initComponents();
        cargarCursos("");
        bloquearBotones();
        bloquearCampos();
        generarCodigoCurso();
    }

    public void bloquearCampos() {
        txtNivel.setEnabled(false);
        txtNombre.setEnabled(false);
        txtDescripcion.setEnabled(false);
    }

    public void desbloquearCampos() {
        txtNivel.setEnabled(true);
        txtNombre.setEnabled(true);
        txtDescripcion.setEnabled(true);
    }

    public void bloquearBotones() {
        btInsertar.setEnabled(false);
        btModificar.setEnabled(false);
        btEliminar.setEnabled(false);
        btCancelar.setEnabled(false);
    }

    public void desbloquearBotones() {
        btCancelar.setEnabled(true);
        btInsertar.setEnabled(true);
        btEliminar.setEnabled(true);
        btModificar.setEnabled(true);
    }

    public void limpiarCampos() {
        txtNombre.setText("");
        txtNivel.setText("");
        txtDescripcion.setText("");
        txtID.setText("");
    }

    DefaultTableModel modeloDatos;
    private Integer idCurso;
    private final String prefijo = "CU";
    private String codigo;

    public void cargarCursos(String curso) {
        try {
            String[] titulosTab = {
                "ID", "Nombre", "Nivel", "Descripción"
            };
            modeloDatos = new DefaultTableModel(null, titulosTab);
            String[] cursos = new String[4];
            conexion cc = new conexion();
            Connection cn = cc.conectar();
            String sql = "";
            if (txtBuscar.getText().isEmpty()) {
                sql = "select * from curso where NOM_CUR LIKE'%" + curso + "%'";
            } else {
                sql = "select * from curso where NOM_CUR ='" + curso + "'";
            }
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                cursos[0] = rs.getString("ID_CUR");
                idCurso = Integer.valueOf(rs.getString("ID_CUR").substring(2, 4));
                cursos[1] = rs.getString("NOM_CUR");
                cursos[2] = rs.getString("NIV_CUR");
                cursos[3] = rs.getString("OBS_CUR");
                modeloDatos.addRow(cursos);
            }
            tbCursos.setModel(modeloDatos);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Sucedio algo en la carga de datos");
        }
    }

    public void generarCodigoCurso() {
        if (idCurso < 10) {
            codigo = prefijo + "0" + (idCurso + 1);
        } else {
            codigo = prefijo + (idCurso + 1);
        }
    }

    public void insertarCurso() {
        try {
            String nombre;
            String nivel;
            String descripcion;
            if (txtNombre.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debes ingresar un nombre de curso");
                txtNombre.requestFocus();
            } else if (txtNivel.getText().isEmpty()) {
                JOptionPane.showConfirmDialog(null, "Debes llenar el nivel");
                txtNivel.requestFocus();
            } else if (txtDescripcion.getText().isEmpty()) {
                descripcion = "SIN DESCRIPCION";
            }
            nombre = txtNombre.getText();
            nivel = txtNivel.getText();
            descripcion = txtDescripcion.getText();
            conexion cc = new conexion();
            Connection cn = cc.conectar();
            String sqlInsertar = "insert into curso values(?,?,?,?)";
            PreparedStatement pst = cn.prepareStatement(sqlInsertar);
            pst.setString(1, codigo);
            pst.setString(2, nombre.toUpperCase());
            pst.setString(3, nivel);
            pst.setString(4, descripcion.toUpperCase());
            pst.executeUpdate();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void cargarCampos() {
        int fila = tbCursos.getSelectedRow();
        if (fila >= 0) {
            txtID.setText(tbCursos.getValueAt(fila, 0).toString());
            txtNombre.setText(tbCursos.getValueAt(fila, 1).toString());
            txtNivel.setText(tbCursos.getValueAt(fila, 2).toString());
            txtDescripcion.setText(tbCursos.getValueAt(fila, 3).toString());
            desbloquearCampos();
            btModificar.setEnabled(true);
            btEliminar.setEnabled(true);
        }
    }

    public void extensionModificar() {
        int op = JOptionPane.showConfirmDialog(null, "Realmente desea modificar?", "Confirmar salida", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (op == 0) {
            modificarCurso();
            tbCursos.setCellSelectionEnabled(false);
            bloquearBotones();
            bloquearCampos();
            limpiarCampos();
            cargarCursos("");
        } else {
            tbCursos.setCellSelectionEnabled(false);
            bloquearBotones();
            bloquearCampos();
            limpiarCampos();
            JOptionPane.showMessageDialog(null, "No se modificó");
        }
    }

    public void modificarCurso() {
        try {
            conexion cc = new conexion();
            Connection cn = cc.conectar();
            String sqlUpdate = "update curso set NOM_CUR='" + txtNombre.getText()
                    + "',NIV_CUR='" + txtNivel.getText()
                    + "',OBS_CUR='" + txtDescripcion.getText()
                    + "' where ID_CUR='" + txtID.getText() + "'";
            PreparedStatement pst = cn.prepareStatement(sqlUpdate);
            pst.executeUpdate();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No se pudo modificar");
        }
    }
    
    public void extensionEliminar() {
        int op = JOptionPane.showConfirmDialog(null, "Realmente desea eliminar?", "Confirmar salida", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (op == 0) {
            eliminarCurso();
            tbCursos.setCellSelectionEnabled(false);
            bloquearBotones();
            bloquearCampos();
            limpiarCampos();
            cargarCursos("");
        } else {
            tbCursos.setCellSelectionEnabled(false);
            bloquearBotones();
            bloquearCampos();
            limpiarCampos();
            JOptionPane.showMessageDialog(null, "No se elimino");
        }
    }
    
    public void eliminarCurso(){
       try {
            String sqlDelete = "delete from curso where ID_EST=" + txtID.getText();
            conexion cc = new conexion();
            Connection cn = cc.conectar();
            PreparedStatement pt = cn.prepareStatement(sqlDelete);
             pt.executeUpdate();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No se puede eliminar este curso porque tiene estudiantes");
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

        pCampos = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtNivel = new javax.swing.JTextField();
        txtDescripcion = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        pBotones = new javax.swing.JPanel();
        btNuevo = new javax.swing.JButton();
        btInsertar = new javax.swing.JButton();
        btModificar = new javax.swing.JButton();
        btEliminar = new javax.swing.JButton();
        btCancelar = new javax.swing.JButton();
        btSalir = new javax.swing.JButton();
        pResumen = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbCursos = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Nombre");

        jLabel2.setText("Nivel");

        jLabel3.setText("Descripción");

        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });

        txtNivel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNivelKeyTyped(evt);
            }
        });

        txtDescripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescripcionActionPerformed(evt);
            }
        });
        txtDescripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDescripcionKeyTyped(evt);
            }
        });

        jLabel5.setText("Identificador");

        txtID.setEnabled(false);

        javax.swing.GroupLayout pCamposLayout = new javax.swing.GroupLayout(pCampos);
        pCampos.setLayout(pCamposLayout);
        pCamposLayout.setHorizontalGroup(
            pCamposLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pCamposLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(pCamposLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pCamposLayout.createSequentialGroup()
                        .addGroup(pCamposLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(84, 84, 84)
                        .addGroup(pCamposLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txtNivel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pCamposLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtID, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        pCamposLayout.setVerticalGroup(
            pCamposLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pCamposLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pCamposLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pCamposLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNivel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
        );

        btNuevo.setText("Nuevo");
        btNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNuevoActionPerformed(evt);
            }
        });

        btInsertar.setText("Insertar");
        btInsertar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btInsertarActionPerformed(evt);
            }
        });

        btModificar.setText("Modificar");
        btModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btModificarActionPerformed(evt);
            }
        });

        btEliminar.setText("Eliminar");
        btEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEliminarActionPerformed(evt);
            }
        });

        btCancelar.setText("Cancelar");
        btCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelarActionPerformed(evt);
            }
        });

        btSalir.setText("Salir");

        javax.swing.GroupLayout pBotonesLayout = new javax.swing.GroupLayout(pBotones);
        pBotones.setLayout(pBotonesLayout);
        pBotonesLayout.setHorizontalGroup(
            pBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pBotonesLayout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addGroup(pBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btSalir)
                    .addComponent(btCancelar)
                    .addComponent(btEliminar)
                    .addComponent(btModificar)
                    .addComponent(btInsertar)
                    .addComponent(btNuevo))
                .addContainerGap(122, Short.MAX_VALUE))
        );
        pBotonesLayout.setVerticalGroup(
            pBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btNuevo)
                .addGap(18, 18, 18)
                .addComponent(btInsertar)
                .addGap(18, 18, 18)
                .addComponent(btModificar)
                .addGap(18, 18, 18)
                .addComponent(btEliminar)
                .addGap(18, 18, 18)
                .addComponent(btCancelar)
                .addGap(18, 18, 18)
                .addComponent(btSalir)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        tbCursos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbCursos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbCursosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbCursos);

        jLabel4.setText("Buscar por nombre");

        javax.swing.GroupLayout pResumenLayout = new javax.swing.GroupLayout(pResumen);
        pResumen.setLayout(pResumenLayout);
        pResumenLayout.setHorizontalGroup(
            pResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pResumenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addGroup(pResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        pResumenLayout.setVerticalGroup(
            pResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pResumenLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pResumenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pResumenLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(92, 92, 92))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pCampos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(pResumen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pCampos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(pResumen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btInsertarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btInsertarActionPerformed
        insertarCurso();
        limpiarCampos();
        cargarCursos("");
    }//GEN-LAST:event_btInsertarActionPerformed

    private void tbCursosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbCursosMouseClicked
        cargarCampos();
        btCancelar.setEnabled(true);
        btNuevo.setEnabled(false);
    }//GEN-LAST:event_tbCursosMouseClicked

    private void btNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNuevoActionPerformed
        desbloquearCampos();
        txtID.setText(codigo);
        btCancelar.setEnabled(true);
        btInsertar.setEnabled(true);
    }//GEN-LAST:event_btNuevoActionPerformed

    private void btCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelarActionPerformed
        limpiarCampos();
        bloquearBotones();
        btNuevo.setEnabled(true);
        bloquearCampos();
    }//GEN-LAST:event_btCancelarActionPerformed

    private void txtDescripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescripcionActionPerformed
        
    }//GEN-LAST:event_txtDescripcionActionPerformed

    private void btModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btModificarActionPerformed
        extensionModificar();
    }//GEN-LAST:event_btModificarActionPerformed

    private void btEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEliminarActionPerformed
        extensionEliminar();
    }//GEN-LAST:event_btEliminarActionPerformed

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped
        char validar;
        validar = evt.getKeyChar();
        if (Character.isDigit(validar)) {
            getToolkit().beep();
            evt.consume();
            JOptionPane.showMessageDialog(null, "Enter only Letters");
        }
    }//GEN-LAST:event_txtNombreKeyTyped

    private void txtNivelKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNivelKeyTyped
        char validar;
        validar = evt.getKeyChar();
        if( Character.isLetter(validar) ){
            getToolkit().beep();
            evt.consume();
            JOptionPane.showMessageDialog(null, "Enter only numbers");
        }
    }//GEN-LAST:event_txtNivelKeyTyped

    private void txtDescripcionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescripcionKeyTyped
        char validar;
        validar = evt.getKeyChar();
        if (Character.isDigit(validar)) {
            getToolkit().beep();
            evt.consume();
            JOptionPane.showMessageDialog(null, "Enter only Letters");
        }
    }//GEN-LAST:event_txtDescripcionKeyTyped

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
            java.util.logging.Logger.getLogger(IntCursos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IntCursos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IntCursos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IntCursos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IntCursos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btEliminar;
    private javax.swing.JButton btInsertar;
    private javax.swing.JButton btModificar;
    private javax.swing.JButton btNuevo;
    private javax.swing.JButton btSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pBotones;
    private javax.swing.JPanel pCampos;
    private javax.swing.JPanel pResumen;
    private javax.swing.JTable tbCursos;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtNivel;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
