/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uta.interfaces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Intel
 */
public class IntUsuarios extends javax.swing.JFrame {

    /**
     * Creates new form IntUsuarios
     */
    DefaultTableModel modeloTabla;

    public IntUsuarios() {
        initComponents();
        bloquearBotones();
        bloquearTextos();
        cargarCargos();
        cargarUsuarios("");
    }

    private void bloquearTextos() {
        this.jtxtCedula.setEnabled(false);
        this.jtxtNombre.setEnabled(false);
        this.jtxtApellido.setEnabled(false);
        this.jcbxCargo.setEnabled(false);
        this.jtxtContraseña.setEnabled(false);
    }

    private void desbloquearTextos() {
        this.jtxtCedula.setEnabled(true);
        this.jtxtNombre.setEnabled(true);
        this.jtxtApellido.setEnabled(true);
        this.jcbxCargo.setEnabled(true);
        this.jtxtContraseña.setEnabled(true);
    }

    private void bloquearBotones() {
        this.jbtnNuevo.setEnabled(true);
        this.jbtnCancelar.setEnabled(false);
        this.jbtnGuardar.setEnabled(false);
        this.jbtnEliminar.setEnabled(false);
        this.jbtnModificar.setEnabled(false);
        this.jbtnSalir.setEnabled(true);
    }

    private void debloquearBotones() {
        this.jbtnNuevo.setEnabled(false);
        this.jbtnCancelar.setEnabled(true);
        this.jbtnGuardar.setEnabled(true);
        this.jbtnEliminar.setEnabled(false);
        this.jbtnModificar.setEnabled(false);
        this.jbtnSalir.setEnabled(true);
    }

    private void desbloquearBotonesModificar() {
        this.jbtnNuevo.setEnabled(false);
        jbtnGuardar.setEnabled(false);
        jbtnEliminar.setEnabled(true);
        jbtnCancelar.setEnabled(true);
        this.jbtnModificar.setEnabled(true);
        jbtnSalir.setEnabled(true);
    }

    private void cargarCargos() {
        DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();
        String[] cargos = {"SELECCIONE", "ADMIN", "SECRETARIA"};
        for (String cargo : cargos) {
            modeloCombo.addElement(cargo);
        }
        jcbxCargo.setModel(modeloCombo);
    }

    private void cargarUsuarios(String cedula) {
        try {
            String[] titulos = {"CEDULA", "APELLIDO", "NOMBRE", "CARGO", "CONTRASEÑA"};
            modeloTabla = new DefaultTableModel(null, titulos);
            String[] registros = new String[5];
            conexion cc = new conexion();
            Connection cn = cc.conectar();
            String sql = "select * from usuarios where CED_USU like '%" + cedula + "%';";
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                registros[0] = rs.getString("CED_USU");
                registros[1] = rs.getString("APE_USU");
                registros[2] = rs.getString("NOM_USU");
                registros[3] = rs.getString("PER_USU");
                registros[4] = rs.getString("CLA_USU");
                modeloTabla.addRow(registros);
            }
            jtblUsuarios.setModel(modeloTabla);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void insertarUsuario() {

        if (jtxtCedula.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar su cédula");
            jtxtCedula.requestFocus();
        } else if (jtxtNombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar su nombre");
            jtxtNombre.requestFocus();
        } else if (jtxtApellido.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar su apellido");
            jtxtApellido.requestFocus();
        } else if (jcbxCargo.getSelectedItem().toString().equals("SELECCIONE")) {
            JOptionPane.showMessageDialog(null, "Debes elegir una cargo");
            jcbxCargo.requestFocus();
        } else if (jtxtContraseña.getPassword() == null) {
            JOptionPane.showMessageDialog(null, "Debes ingresar una contraseña");
            jtxtContraseña.requestFocus();
        } else {

            try {
                if (esCedula() == false) {
                    JOptionPane.showMessageDialog(null, "Ingrese una cedula valida");
                } else {
                    String cedula = jtxtCedula.getText();
                    String apellido = jtxtApellido.getText().toUpperCase();
                    String nombre = jtxtNombre.getText().toUpperCase();
                    String clave = String.valueOf(jtxtContraseña.getText());
                    String cargo = jcbxCargo.getSelectedItem().toString();
                    conexion cc = new conexion();
                    Connection cn = cc.conectar();
                    String sql = "insert into usuarios values(?,?,?,?,?)";
                    PreparedStatement psd = cn.prepareStatement(sql);
                    psd.setString(1, cedula);
                    psd.setString(2, apellido);
                    psd.setString(3, nombre);
                    psd.setString(4, cargo);
                    psd.setString(5, clave);
                    int n = psd.executeUpdate();
                    if (n > 0) {
                        JOptionPane.showMessageDialog(null, "Se inserto correctamente");
                    }
                }

            } catch (SQLException ex) {
                JOptionPane.showConfirmDialog(null, ex);
            }
        }

    }

    private void modificarUsuario() {
        try {
            String cedula = jtxtCedula.getText();
            String apellido = jtxtApellido.getText().toUpperCase();
            String nombre = jtxtNombre.getText().toUpperCase();
            String clave = String.valueOf(jtxtContraseña.getPassword());
            String cargo = jcbxCargo.getSelectedItem().toString();
            conexion cc = new conexion();
            Connection cn = cc.conectar();
            String sql = "update usuarios set NOM_USU='" + nombre + "',APE_USU='" + apellido + "',PER_USU='" + cargo + "',CLA_USU='" + clave
                    + "' where CED_USU='" + cedula + "';";
            PreparedStatement pst = cn.prepareStatement(sql);
            int n = pst.executeUpdate(sql);
            bloquearBotones();
            bloquearTextos();
            limpiarCampos();
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "Registro actualizado");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void confirmacionModificar() {

        int validacion = JOptionPane.showConfirmDialog(null, "Seguro que desea modificar este registro", "Modificar registro", JOptionPane.YES_NO_OPTION);
        if (validacion == JOptionPane.YES_OPTION) {
            modificarUsuario();
        }
    }

    private void eliminarUsuario() {
        try {
            int validacion = JOptionPane.showConfirmDialog(null, "Esta seguro de borrar este registro", "Borrar Registro", JOptionPane.YES_NO_OPTION);
            if (validacion == JOptionPane.YES_OPTION) {
                String cedula = this.jtxtCedula.getText();
                conexion c = new conexion();
                Connection cn = c.conectar();
                String sql = "";
                sql = "delete from usuarios where CED_USU='" + cedula + "';";
                PreparedStatement psd = cn.prepareStatement(sql);
                int n = psd.executeUpdate();
                cargarUsuarios("");
                bloquearBotones();
                bloquearTextos();
                limpiarCampos();
                if (n > 0) {
                    JOptionPane.showMessageDialog(null, "Registro eliminado");
                }
            }
            bloquearBotones();
            bloquearTextos();
            limpiarCampos();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void cargaDatosModificar() {
        desbloquearTextos();
        this.jtxtCedula.setEnabled(false);
        desbloquearBotonesModificar();
        int fila = this.jtblUsuarios.getSelectedRow();
        if (fila != -1) {
            this.jtxtCedula.setText(jtblUsuarios.getValueAt(fila, 0).toString());
            this.jtxtNombre.setText(jtblUsuarios.getValueAt(fila, 2).toString());
            this.jtxtApellido.setText(jtblUsuarios.getValueAt(fila, 1).toString());
            this.jcbxCargo.setSelectedItem(jtblUsuarios.getValueAt(fila, 3));
            this.jtxtContraseña.setText(jtblUsuarios.getValueAt(fila, 4).toString());

        }
    }

    private void limpiarCampos() {
        jtxtApellido.setText("");
        jtxtNombre.setText("");
        jtxtCedula.setText("");
        jtxtContraseña.setText("");
    }

    private boolean esCedula() {
        boolean cedulaCorrecta;
        try {
            String cedula = jtxtCedula.getText();
            if (cedula.length() == 10) {
                // Coeficientes de validación cédula
                // El decimo digito se lo considera dígito verificador
                int[] coefValCedula = {2, 1, 2, 1, 2, 1, 2, 1, 2};
                int verificador = Integer.parseInt(cedula.substring(9, 10));
                int suma = 0;
                int digito;
                for (int i = 0; i < (cedula.length() - 1); i++) {
                    digito = Integer.parseInt(cedula.substring(i, i + 1)) * coefValCedula[i];
                    suma += ((digito % 10) + (digito / 10));
                }
                cedulaCorrecta = ((suma % 10 == 0 && verificador == 0) || (10 - suma % 10 == verificador));
            } else {
                cedulaCorrecta = false;
            }
        } catch (Exception e) {
            cedulaCorrecta = false;
        }
        return cedulaCorrecta;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jtxtNombre = new javax.swing.JTextField();
        jtxtApellido = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jcbxCargo = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jtxtCedula = new javax.swing.JTextField();
        jtxtContraseña = new javax.swing.JPasswordField();
        jPanel2 = new javax.swing.JPanel();
        jbtnNuevo = new javax.swing.JButton();
        jbtnGuardar = new javax.swing.JButton();
        jbtnCancelar = new javax.swing.JButton();
        jbtnSalir = new javax.swing.JButton();
        jbtnModificar = new javax.swing.JButton();
        jbtnEliminar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblUsuarios = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jtxtBuscarCedula = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jtxtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtNombreKeyTyped(evt);
            }
        });

        jtxtApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtApellidoKeyTyped(evt);
            }
        });

        jLabel1.setText("Cédula");

        jLabel2.setText("Nombre");

        jcbxCargo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

        jLabel3.setText("Apellido");

        jLabel4.setText("Cargo");

        jLabel5.setText("Contraseña");

        jtxtCedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtCedulaKeyTyped(evt);
            }
        });

        jtxtContraseña.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtContraseñaKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addGap(25, 25, 25))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addGap(34, 34, 34)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(7, 7, 7)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jcbxCargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtApellido, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                            .addComponent(jtxtContraseña))
                        .addGap(1, 1, 1))
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jtxtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGap(63, 63, 63)
                            .addComponent(jtxtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtxtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jtxtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jtxtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jcbxCargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jtxtContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jbtnNuevo.setText("NUEVO");
        jbtnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnNuevoActionPerformed(evt);
            }
        });

        jbtnGuardar.setText("GUARDAR");
        jbtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnGuardarActionPerformed(evt);
            }
        });

        jbtnCancelar.setText("CANCELAR");
        jbtnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCancelarActionPerformed(evt);
            }
        });

        jbtnSalir.setText("SALIR");
        jbtnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSalirActionPerformed(evt);
            }
        });

        jbtnModificar.setText("MODIFICAR");
        jbtnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnModificarActionPerformed(evt);
            }
        });

        jbtnEliminar.setText("ELIMINAR");
        jbtnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbtnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnModificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbtnNuevo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnModificar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbtnCancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnEliminar)
                .addGap(8, 8, 8)
                .addComponent(jbtnSalir))
        );

        jtblUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jtblUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtblUsuariosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtblUsuarios);

        jLabel6.setText("Buscar por cédula");

        jtxtBuscarCedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtBuscarCedulaKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel6))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jtxtBuscarCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jtxtBuscarCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(394, 394, 394))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(114, 114, 114))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNuevoActionPerformed
        desbloquearTextos();
        debloquearBotones();
        this.jbtnNuevo.setEnabled(false);
    }//GEN-LAST:event_jbtnNuevoActionPerformed

    private void jbtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnGuardarActionPerformed
        insertarUsuario();
        limpiarCampos();
        cargarUsuarios("");
    }//GEN-LAST:event_jbtnGuardarActionPerformed

    private void jbtnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnModificarActionPerformed
        confirmacionModificar();
        limpiarCampos();
        cargarUsuarios("");
    }//GEN-LAST:event_jbtnModificarActionPerformed

    private void jbtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCancelarActionPerformed
        bloquearBotones();
        limpiarCampos();
    }//GEN-LAST:event_jbtnCancelarActionPerformed

    private void jtblUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtblUsuariosMouseClicked
        cargaDatosModificar();
    }//GEN-LAST:event_jtblUsuariosMouseClicked

    private void jbtnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEliminarActionPerformed
        eliminarUsuario();
    }//GEN-LAST:event_jbtnEliminarActionPerformed

    private void jtxtBuscarCedulaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtBuscarCedulaKeyReleased
        cargarUsuarios(jtxtBuscarCedula.getText());
    }//GEN-LAST:event_jtxtBuscarCedulaKeyReleased

    private void jbtnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSalirActionPerformed
        this.dispose();
        limpiarCampos();
    }//GEN-LAST:event_jbtnSalirActionPerformed

    private void jtxtCedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCedulaKeyTyped
        char control = evt.getKeyChar();
        if (!(Character.isDigit(control))) {
            evt.consume();
        } else if (this.jtxtCedula.getText().length() == 10) {
            evt.consume();
        }
    }//GEN-LAST:event_jtxtCedulaKeyTyped

    private void jtxtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtNombreKeyTyped
        char control = evt.getKeyChar();
        if (!(Character.isLetter(control))) {
            evt.consume();
        }
    }//GEN-LAST:event_jtxtNombreKeyTyped

    private void jtxtApellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtApellidoKeyTyped
        char control = evt.getKeyChar();
        if (!(Character.isLetter(control))) {
            evt.consume();
        }
    }//GEN-LAST:event_jtxtApellidoKeyTyped

    private void jtxtContraseñaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtContraseñaKeyTyped
        char control = evt.getKeyChar();
        if (!(Character.isLetter(control)) && !(Character.isDigit(control))) {
            evt.consume();
        }
    }//GEN-LAST:event_jtxtContraseñaKeyTyped

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
            java.util.logging.Logger.getLogger(IntUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IntUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IntUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IntUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IntUsuarios().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtnCancelar;
    private javax.swing.JButton jbtnEliminar;
    private javax.swing.JButton jbtnGuardar;
    private javax.swing.JButton jbtnModificar;
    private javax.swing.JButton jbtnNuevo;
    private javax.swing.JButton jbtnSalir;
    private javax.swing.JComboBox<String> jcbxCargo;
    private javax.swing.JTable jtblUsuarios;
    private javax.swing.JTextField jtxtApellido;
    private javax.swing.JTextField jtxtBuscarCedula;
    private javax.swing.JTextField jtxtCedula;
    private javax.swing.JPasswordField jtxtContraseña;
    private javax.swing.JTextField jtxtNombre;
    // End of variables declaration//GEN-END:variables

}
