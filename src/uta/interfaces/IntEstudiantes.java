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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ASUS
 */
public class IntEstudiantes extends javax.swing.JInternalFrame {

    /**
     * Creates new form IntEstudiantes
     */
    
    public IntEstudiantes() {
        initComponents();
        bloquearBts();
        bloquearTxts();
        cargarEstadoCivil();
        cargarProvincias();
        cargarCursos();
        cargarDatos("");
    }

    public void insertarEstudiante() {
        try {
            String telefono;
            String sexo = "";
            String provincia = "";
            String estadoCivil;
            if (txtCedula.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar su cédula");
                txtCedula.requestFocus();
            } else if (txtApellido.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar su apellido");
                txtApellido.requestFocus();
            } else if (txtNombre.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar su nombre");
                txtNombre.requestFocus();
            } else if (cbProvincias.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "Debes elegir una provincia");
                cbProvincias.requestFocus();
            } else if (cbEstadoCivil.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "Debes elegir un estado civil");
                cbEstadoCivil.requestFocus();
            }

            if (!rbFemenino.isSelected() && !rbMasculino.isSelected()) {
                JOptionPane.showMessageDialog(null, "Debes elegir un género");
                rbMasculino.requestFocus();
            } else if (rbMasculino.isSelected()) {
                sexo = "M";
            } else if (rbFemenino.isSelected()) {
                sexo = "F";
            }

            if (txtTelefono.getText().isEmpty()) {
                telefono = "XXXXXXXXXX";
            } else {
                telefono = txtTelefono.getText();
            }

            String cedula = txtCedula.getText();
            String apellido = txtApellido.getText();
            String nombre = txtNombre.getText();
            provincia = cbProvincias.getSelectedItem().toString();
            estadoCivil = cbEstadoCivil.getSelectedItem().toString();
            conexion cc = new conexion();
            Connection cn = cc.conectar();
            String sqlInsertar = "insert into estudiantes values(?,?,?,?,?,?,?,?)";
            PreparedStatement pst = cn.prepareStatement(sqlInsertar);
            pst.setString(1, cedula);
            pst.setString(2, apellido.toUpperCase());
            pst.setString(3, nombre.toUpperCase());
            pst.setString(4, telefono);
            pst.setString(5, sexo);
            pst.setString(6, estadoCivil);
            pst.setString(7, provincia.toUpperCase());
            pst.setString(8, "CU01");
            pst.executeUpdate();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public final void cargarProvincias() {
        String[] provincias = {"Azuay", "Bolívar", "Cañar", "Carchi", "Chimborazo", "Cotopaxi", "El Oro",
            "Esmeraldas", "Galápagos", "Guayas", "Imbabura",
            "Loja", "Los Ríos", "Manabí", "Morona Santiago", "Napo",
            "Orellana", "Pastaza", "Pichincha", "Santa Elena", "Santo Domingo de los Tsáchilas",
            "Sucumbíos", "Tungurahua", "Zamora Chinchipe"};
        DefaultComboBoxModel modeloProvincias = new DefaultComboBoxModel();

        for (int i = 0; i < provincias.length; i++) {
            modeloProvincias.addElement(provincias[i]);
        }

        cbProvincias.setModel(modeloProvincias);
    }

    DefaultTableModel modeloDatos;

    public final void cargarEstadoCivil() {
        try {
            conexion cc = new conexion();
            Connection cn = cc.conectar();
            String sql = "";
            sql = "select * from estado_civil";
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                cbEstadoCivil.addItem(rs.getString("DESC_EST_CIV"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No se pudo cargar los estados civiles");
        }
    }

    public final void cargarCursos() {
        try {
            conexion cc = new conexion();
            Connection cn = cc.conectar();
            String sql = "";
            sql = "select * from curso";
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                cbCurso.addItem(rs.getString("NOM_CUR"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No se pudo cargar los cursos");
        }
    }

    public void cargarDatos(String cedula) {
        try {
            String[] titulosTab = {
                "Cédula", "Apellido", "Nombre", "Teléfono", "Género", "Estado civil", "Provincia"
            };
            modeloDatos = new DefaultTableModel(null, titulosTab);
            String registros[] = new String[7];
            conexion cc = new conexion();
            Connection cn = cc.conectar();
            String sql = "";
            if (txtBuscar.getText().isEmpty()) {
                sql = "select * from estudiantes where CED_EST LIKE'%" + cedula + "%'";
            } else {
                sql = "select * from estudiantes where CED_EST ='" + cedula + "'";

            }
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                registros[0] = rs.getString("CED_EST");
                registros[1] = rs.getString("APE_EST");
                registros[2] = rs.getString("NOM_EST");
                registros[3] = rs.getString("TELF_EST");
                registros[4] = rs.getString("SEX_EST");
                registros[5] = rs.getString("EST_CIV_EST");
                registros[6] = rs.getString("PRO_EST");
                modeloDatos.addRow(registros);
            }
            tbRegistros.setModel(modeloDatos);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Sucedio algo en la carga de datos");
        }
    }

    public void loadToModified() {
        int fila = tbRegistros.getSelectedRow();
        if (fila >= 0) {
            txtCedula.setText(tbRegistros.getValueAt(fila, 0).toString());
            txtApellido.setText(tbRegistros.getValueAt(fila, 1).toString());
            txtNombre.setText(tbRegistros.getValueAt(fila, 2).toString());
            txtTelefono.setText(tbRegistros.getValueAt(fila, 3).toString());
            String sexo = tbRegistros.getValueAt(fila, 4).toString();

            if (sexo.equals("M")) {
                rbMasculino.setSelected(true);
            } else {
                rbFemenino.setSelected(true);
            }

            desbloquearTxts();
            cbProvincias.setEnabled(false);
            txtCedula.setEnabled(false);
            btModificar.setEnabled(true);
            btEliminar.setEnabled(true);
        }
    }

    public final void bloquearBts() {
        btInsertar.setEnabled(false);
        btModificar.setEnabled(false);
        btEliminar.setEnabled(false);
        btCancelar.setEnabled(false);
    }

    public final void bloquearTxts() {
        txtApellido.setEnabled(false);
        txtNombre.setEnabled(false);
        txtCedula.setEnabled(false);
        txtTelefono.setEnabled(false);
        cbEstadoCivil.setEnabled(false);
        cbCurso.setEnabled(false);
        cbProvincias.setEnabled(false);
        rbFemenino.setEnabled(false);
        rbMasculino.setEnabled(false);
    }

    public void desbloquearTxts() {
        txtApellido.setEnabled(true);
        txtNombre.setEnabled(true);
        txtCedula.setEnabled(true);
        txtTelefono.setEnabled(true);
        cbEstadoCivil.setEnabled(true);
        cbProvincias.setEnabled(true);
        cbCurso.setEnabled(false);
        rbFemenino.setEnabled(true);
        rbMasculino.setEnabled(true);
    }

    public void desbloquearBts() {
        btInsertar.setEnabled(true);
        btCancelar.setEnabled(true);
    }

    public void modificarEstudiante() {
        try {
            conexion cc = new conexion();
            Connection cn = cc.conectar();
            String sexo = "";

            if (rbMasculino.isSelected()) {
                sexo = "M";
            } else if (rbFemenino.isSelected()) {
                sexo = "F";
            }

            String sqlUpdate = "update estudiantes set APE_EST='" + txtApellido.getText()
                    + "',NOM_EST='" + txtNombre.getText()
                    + "',TELF_EST='" + txtTelefono.getText()
                    + "',SEX_EST='" + sexo
                    + "',EST_CIV_EST='" + cbEstadoCivil.getSelectedItem().toString()
                    + "' where CED_EST='" + txtCedula.getText() + "'";
            PreparedStatement pst = cn.prepareStatement(sqlUpdate);
            pst.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(IntEstudiantes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void extensionModificar() {
        int op = JOptionPane.showConfirmDialog(null, "Realmente desea modificar?", "Confirmar salida", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (op == 0) {
            modificarEstudiante();
            tbRegistros.setCellSelectionEnabled(false);
            bloquearBts();
            bloquearTxts();
            limpiarCampos();
            cargarDatos("");
        } else {
            tbRegistros.setCellSelectionEnabled(false);
            bloquearBts();
            bloquearTxts();
            limpiarCampos();
            JOptionPane.showMessageDialog(null, "No se modificó");
        }
    }

    public void limpiarCampos() {
        txtApellido.setText("");
        txtNombre.setText("");
        txtCedula.setText("");
        txtTelefono.setText("");
        bgSexo.clearSelection();
    }

    public boolean esCedula() {
        boolean cedulaCorrecta;
        try {
            String cedula = txtCedula.getText();
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

    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgSexo = new javax.swing.ButtonGroup();
        panelRegistro = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        txtCedula = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cbProvincias = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cbEstadoCivil = new javax.swing.JComboBox<>();
        rbMasculino = new javax.swing.JRadioButton();
        rbFemenino = new javax.swing.JRadioButton();
        cbCurso = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        panelAcciones = new javax.swing.JPanel();
        btInsertar = new javax.swing.JButton();
        btModificar = new javax.swing.JButton();
        btEliminar = new javax.swing.JButton();
        btCancelar = new javax.swing.JButton();
        btNuevo = new javax.swing.JButton();
        btSalir = new javax.swing.JButton();
        panelImpresion = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbRegistros = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();

        setClosable(true);

        jLabel1.setText("Nombre ");

        jLabel2.setText("Apellido");

        jLabel3.setText("Cédula");

        jLabel4.setText("Teléfono");

        jLabel5.setText("Ciudad");

        jLabel7.setText("Sexo");

        jLabel8.setText("Estado Civil");

        bgSexo.add(rbMasculino);
        rbMasculino.setText("M");

        bgSexo.add(rbFemenino);
        rbFemenino.setText("F");

        jLabel9.setText("Curso");

        javax.swing.GroupLayout panelRegistroLayout = new javax.swing.GroupLayout(panelRegistro);
        panelRegistro.setLayout(panelRegistroLayout);
        panelRegistroLayout.setHorizontalGroup(
            panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRegistroLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRegistroLayout.createSequentialGroup()
                        .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelRegistroLayout.createSequentialGroup()
                                .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(panelRegistroLayout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addGap(124, 124, 124)
                                        .addComponent(jLabel8))
                                    .addGroup(panelRegistroLayout.createSequentialGroup()
                                        .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(104, 104, 104)))
                                .addGap(22, 22, 22)))
                        .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cbProvincias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)))
                    .addGroup(panelRegistroLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbMasculino)
                            .addComponent(rbFemenino))
                        .addGap(108, 108, 108)
                        .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(cbCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbEstadoCivil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(248, Short.MAX_VALUE))
        );
        panelRegistroLayout.setVerticalGroup(
            panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRegistroLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRegistroLayout.createSequentialGroup()
                        .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbProvincias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelRegistroLayout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(jLabel2))
                            .addGroup(panelRegistroLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel8))))
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRegistroLayout.createSequentialGroup()
                        .addComponent(cbEstadoCivil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelRegistroLayout.createSequentialGroup()
                        .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rbMasculino))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelRegistroLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(rbFemenino))))
                .addContainerGap(57, Short.MAX_VALUE))
        );

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

        btCancelar.setText("Cancelar");
        btCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelarActionPerformed(evt);
            }
        });

        btNuevo.setText("Nuevo");
        btNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNuevoActionPerformed(evt);
            }
        });

        btSalir.setText("Salir");
        btSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAccionesLayout = new javax.swing.GroupLayout(panelAcciones);
        panelAcciones.setLayout(panelAccionesLayout);
        panelAccionesLayout.setHorizontalGroup(
            panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAccionesLayout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addGroup(panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btInsertar, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                                .addComponent(btNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(btModificar))
                    .addGroup(panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(100, Short.MAX_VALUE))
        );
        panelAccionesLayout.setVerticalGroup(
            panelAccionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAccionesLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(btNuevo)
                .addGap(18, 18, 18)
                .addComponent(btInsertar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btModificar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btCancelar)
                .addGap(18, 18, 18)
                .addComponent(btSalir)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tbRegistros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbRegistros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbRegistrosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbRegistros);

        jLabel6.setText("Buscar por cédula");

        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout panelImpresionLayout = new javax.swing.GroupLayout(panelImpresion);
        panelImpresion.setLayout(panelImpresionLayout);
        panelImpresionLayout.setHorizontalGroup(
            panelImpresionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelImpresionLayout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 625, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelImpresionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelImpresionLayout.setVerticalGroup(
            panelImpresionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelImpresionLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(panelImpresionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelImpresionLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelImpresion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 113, Short.MAX_VALUE)
                        .addComponent(panelAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(58, 58, 58))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelAcciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 19, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(panelImpresion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btInsertarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btInsertarActionPerformed
        insertarEstudiante();
        cargarDatos("");
    }//GEN-LAST:event_btInsertarActionPerformed

    private void btModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btModificarActionPerformed
        extensionModificar();
    }//GEN-LAST:event_btModificarActionPerformed

    private void btNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNuevoActionPerformed
        desbloquearTxts();
        desbloquearBts();
        btNuevo.setEnabled(false);
    }//GEN-LAST:event_btNuevoActionPerformed

    private void btCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelarActionPerformed
        bloquearBts();
        bloquearTxts();
        limpiarCampos();
        tbRegistros.setCellSelectionEnabled(false);
        btNuevo.setEnabled(true);
    }//GEN-LAST:event_btCancelarActionPerformed

    private void btSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btSalirActionPerformed

    private void tbRegistrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbRegistrosMouseClicked
        loadToModified();
        btCancelar.setEnabled(true);
    }//GEN-LAST:event_tbRegistrosMouseClicked

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        cargarDatos(txtBuscar.getText());
    }//GEN-LAST:event_txtBuscarKeyReleased

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarActionPerformed

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
            java.util.logging.Logger.getLogger(IntEstudiantes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IntEstudiantes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IntEstudiantes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IntEstudiantes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IntEstudiantes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgSexo;
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btEliminar;
    private javax.swing.JButton btInsertar;
    private javax.swing.JButton btModificar;
    private javax.swing.JButton btNuevo;
    private javax.swing.JButton btSalir;
    private javax.swing.JComboBox<String> cbCurso;
    private javax.swing.JComboBox<String> cbEstadoCivil;
    private javax.swing.JComboBox<String> cbProvincias;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelAcciones;
    private javax.swing.JPanel panelImpresion;
    private javax.swing.JPanel panelRegistro;
    private javax.swing.JRadioButton rbFemenino;
    private javax.swing.JRadioButton rbMasculino;
    private javax.swing.JTable tbRegistros;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCedula;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
