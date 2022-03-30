package evaluacion_3;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.Color;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class BDAlumnosCompleto extends JFrame implements ActionListener, MouseListener {

	private JPanel contentPane;
	private JTextField txtDNI;
	private JTextField txtNombre;
	private JTextField txtApellidos;
	private JTextField txtGrupo;
	private JTextField txtNota;
	private JComboBox<String> cmbCod;
	private JTable tabla;

	private Connection conexion;
	private ArrayList<Alumno> alAlumno = new ArrayList<Alumno>();
	private Alumno alumno;
	private int regActual;
	private int regTotal;
	private int indice;

	private String DniOriginal;
	private JLabel lblRegistros;

	private JButton btnUltimo;
	private JButton btnPrimero;
	private JButton btnSiguiente;
	private JButton btnAnterior;

	private JButton btnSalir;

	private JButton btnInsertarAlumno;
	private JButton btnBorrarAlumno;
	private JButton btnActualizarAlumno;

	private JButton btnInsertarNota;
	private JButton btnBorrarNota;
	private JButton btnActualizarNota;

	private Vector<Vector<String>> datos;
	private Vector<String> columnas;

	public int fila;
	public int filaAbs;

	public String dni;
	public String cod;
	public String nota;

	public boolean seleccionado = false;
	private TableRowSorter<TableModel> metodoOrdenacion;
	private DefaultTableModel dtm;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BDAlumnosCompleto frame = new BDAlumnosCompleto();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public BDAlumnosCompleto() {
		setTitle("BDAlumnosCompleto");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 625, 354);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.window);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		contentPane.add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 51, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JLabel lblDNI_1 = new JLabel("DNI");
		GridBagConstraints gbc_lblDNI_1 = new GridBagConstraints();
		gbc_lblDNI_1.fill = GridBagConstraints.BOTH;
		gbc_lblDNI_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblDNI_1.gridx = 0;
		gbc_lblDNI_1.gridy = 0;
		panel.add(lblDNI_1, gbc_lblDNI_1);

		txtDNI = new JTextField();
		txtDNI.setText((String) null);
		txtDNI.setColumns(10);
		GridBagConstraints gbc_txtDNI = new GridBagConstraints();
		gbc_txtDNI.fill = GridBagConstraints.BOTH;
		gbc_txtDNI.insets = new Insets(0, 0, 5, 5);
		gbc_txtDNI.gridx = 1;
		gbc_txtDNI.gridy = 0;
		panel.add(txtDNI, gbc_txtDNI);

		btnInsertarAlumno = new JButton("Insertar Alumno");
		btnInsertarAlumno.addActionListener(this);
		btnInsertarAlumno.setForeground(Color.BLACK);
		btnInsertarAlumno.setFont(new Font("Tahoma", Font.ITALIC, 12));
		btnInsertarAlumno.setBackground(Color.WHITE);
		GridBagConstraints gbc_btnInsertarAlumno = new GridBagConstraints();
		gbc_btnInsertarAlumno.fill = GridBagConstraints.BOTH;
		gbc_btnInsertarAlumno.insets = new Insets(0, 0, 5, 0);
		gbc_btnInsertarAlumno.gridx = 2;
		gbc_btnInsertarAlumno.gridy = 0;
		panel.add(btnInsertarAlumno, gbc_btnInsertarAlumno);

		JLabel lblNombre_1 = new JLabel("Nombre");
		GridBagConstraints gbc_lblNombre_1 = new GridBagConstraints();
		gbc_lblNombre_1.fill = GridBagConstraints.BOTH;
		gbc_lblNombre_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNombre_1.gridx = 0;
		gbc_lblNombre_1.gridy = 1;
		panel.add(lblNombre_1, gbc_lblNombre_1);

		txtNombre = new JTextField();
		txtNombre.setText((String) null);
		txtNombre.setColumns(10);
		GridBagConstraints gbc_txtNombre = new GridBagConstraints();
		gbc_txtNombre.fill = GridBagConstraints.BOTH;
		gbc_txtNombre.insets = new Insets(0, 0, 5, 5);
		gbc_txtNombre.gridx = 1;
		gbc_txtNombre.gridy = 1;
		panel.add(txtNombre, gbc_txtNombre);

		btnBorrarAlumno = new JButton("Borrar Alumno");
		btnBorrarAlumno.addActionListener(this);
		btnBorrarAlumno.setForeground(Color.BLACK);
		btnBorrarAlumno.setFont(new Font("Tahoma", Font.ITALIC, 12));
		btnBorrarAlumno.setBackground(Color.WHITE);
		GridBagConstraints gbc_btnBorrarAlumno = new GridBagConstraints();
		gbc_btnBorrarAlumno.fill = GridBagConstraints.BOTH;
		gbc_btnBorrarAlumno.insets = new Insets(0, 0, 5, 0);
		gbc_btnBorrarAlumno.gridx = 2;
		gbc_btnBorrarAlumno.gridy = 1;
		panel.add(btnBorrarAlumno, gbc_btnBorrarAlumno);

		JLabel lblApellidos_1 = new JLabel("Apellidos");
		GridBagConstraints gbc_lblApellidos_1 = new GridBagConstraints();
		gbc_lblApellidos_1.fill = GridBagConstraints.BOTH;
		gbc_lblApellidos_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblApellidos_1.gridx = 0;
		gbc_lblApellidos_1.gridy = 2;
		panel.add(lblApellidos_1, gbc_lblApellidos_1);

		txtApellidos = new JTextField();
		txtApellidos.setText((String) null);
		txtApellidos.setColumns(10);
		GridBagConstraints gbc_txtApellidos = new GridBagConstraints();
		gbc_txtApellidos.fill = GridBagConstraints.BOTH;
		gbc_txtApellidos.insets = new Insets(0, 0, 5, 5);
		gbc_txtApellidos.gridx = 1;
		gbc_txtApellidos.gridy = 2;
		panel.add(txtApellidos, gbc_txtApellidos);

		btnActualizarAlumno = new JButton("ActualizarAlumno");
		btnActualizarAlumno.addActionListener(this);
		btnActualizarAlumno.setForeground(Color.BLACK);
		btnActualizarAlumno.setFont(new Font("Tahoma", Font.ITALIC, 12));
		btnActualizarAlumno.setBackground(Color.WHITE);
		GridBagConstraints gbc_btnActualizarAlumno = new GridBagConstraints();
		gbc_btnActualizarAlumno.fill = GridBagConstraints.BOTH;
		gbc_btnActualizarAlumno.insets = new Insets(0, 0, 5, 0);
		gbc_btnActualizarAlumno.gridx = 2;
		gbc_btnActualizarAlumno.gridy = 2;
		panel.add(btnActualizarAlumno, gbc_btnActualizarAlumno);

		JLabel lblGrupo_1 = new JLabel("Grupo");
		GridBagConstraints gbc_lblGrupo_1 = new GridBagConstraints();
		gbc_lblGrupo_1.fill = GridBagConstraints.BOTH;
		gbc_lblGrupo_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblGrupo_1.gridx = 0;
		gbc_lblGrupo_1.gridy = 3;
		panel.add(lblGrupo_1, gbc_lblGrupo_1);

		txtGrupo = new JTextField();
		txtGrupo.setText((String) null);
		txtGrupo.setColumns(10);
		GridBagConstraints gbc_txtGrupo = new GridBagConstraints();
		gbc_txtGrupo.insets = new Insets(0, 0, 5, 5);
		gbc_txtGrupo.fill = GridBagConstraints.BOTH;
		gbc_txtGrupo.gridx = 1;
		gbc_txtGrupo.gridy = 3;
		panel.add(txtGrupo, gbc_txtGrupo);

		btnSalir = new JButton("Salir");
		btnSalir.addActionListener(this);
		btnSalir.setForeground(Color.BLACK);
		btnSalir.setFont(new Font("Tahoma", Font.ITALIC, 12));
		btnSalir.setBackground(Color.WHITE);
		GridBagConstraints gbc_btnSalir = new GridBagConstraints();
		gbc_btnSalir.insets = new Insets(0, 0, 5, 0);
		gbc_btnSalir.fill = GridBagConstraints.BOTH;
		gbc_btnSalir.gridx = 2;
		gbc_btnSalir.gridy = 3;
		panel.add(btnSalir, gbc_btnSalir);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(SystemColor.window);
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.gridwidth = 3;
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 4;
		panel.add(panel_2, gbc_panel_2);

		JLabel lblNewLabel_1 = new JLabel("CodAsignatura");
		panel_2.add(lblNewLabel_1);

		cmbCod = new JComboBox<String>();
		panel_2.add(cmbCod);

		JLabel lblNewLabel_2 = new JLabel("Nota:");
		panel_2.add(lblNewLabel_2);

		txtNota = new JTextField();
		txtNota.setText("5");
		txtNota.setHorizontalAlignment(SwingConstants.CENTER);
		txtNota.setColumns(2);
		panel_2.add(txtNota);

		btnInsertarNota = new JButton("Insertar Nota");
		btnInsertarNota.addActionListener(this);
		btnInsertarNota.setForeground(Color.BLACK);
		btnInsertarNota.setFont(new Font("Tahoma", Font.ITALIC, 12));
		btnInsertarNota.setBackground(Color.WHITE);
		panel_2.add(btnInsertarNota);

		btnBorrarNota = new JButton("Borrar Nota");
		btnBorrarNota.addActionListener(this);
		btnBorrarNota.setForeground(Color.BLACK);
		btnBorrarNota.setFont(new Font("Tahoma", Font.ITALIC, 12));
		btnBorrarNota.setBackground(Color.WHITE);
		panel_2.add(btnBorrarNota);

		btnActualizarNota = new JButton("Actualizar Nota");
		btnActualizarNota.addActionListener(this);
		btnActualizarNota.setForeground(Color.BLACK);
		btnActualizarNota.setFont(new Font("Tahoma", Font.ITALIC, 12));
		btnActualizarNota.setBackground(Color.WHITE);
		panel_2.add(btnActualizarNota);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(SystemColor.window);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 5;
		panel.add(scrollPane, gbc_scrollPane);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		contentPane.add(panel_1, BorderLayout.NORTH);

		btnPrimero = new JButton("<<");
		btnPrimero.addActionListener(this);
		btnPrimero.setForeground(Color.BLACK);
		btnPrimero.setFont(new Font("Tahoma", Font.ITALIC, 12));
		btnPrimero.setBackground(Color.WHITE);
		panel_1.add(btnPrimero);

		btnAnterior = new JButton("<");
		btnAnterior.addActionListener(this);
		btnAnterior.setForeground(Color.BLACK);
		btnAnterior.setFont(new Font("Tahoma", Font.ITALIC, 12));
		btnAnterior.setBackground(Color.WHITE);
		panel_1.add(btnAnterior);

		lblRegistros = new JLabel("Resgistro 1 de 0");
		panel_1.add(lblRegistros);

		btnSiguiente = new JButton(">");
		btnSiguiente.addActionListener(this);
		btnSiguiente.setForeground(Color.BLACK);
		btnSiguiente.setFont(new Font("Tahoma", Font.ITALIC, 12));
		btnSiguiente.setBackground(Color.WHITE);
		panel_1.add(btnSiguiente);

		btnUltimo = new JButton(">>");
		btnUltimo.addActionListener(this);
		btnUltimo.setForeground(Color.BLACK);
		btnUltimo.setFont(new Font("Tahoma", Font.ITALIC, 12));
		btnUltimo.setBackground(Color.WHITE);
		panel_1.add(btnUltimo);

		try {
			conexion = DriverManager.getConnection("jdbc:mysql://localhost/bdalumnos", "root", "");
			// si se ha conectado correctamente
			System.out.println("Conexión Correcta.");

			// Creo una sentencia
			Statement st = conexion.createStatement();

			ResultSet rs = st.executeQuery("SELECT * FROM bdalumnos.alumnos;");

			columnas = new Vector<String>();
			columnas.add("DNI");
			columnas.add("Asignatura");
			columnas.add("Nota");

			datos = new Vector<Vector<String>>();
			dtm = new DefaultTableModel(datos, columnas);

			tabla = new JTable(dtm) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};

			tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tabla.addMouseListener(this);
			tabla.setGridColor(SystemColor.activeCaption);
			tabla.setFillsViewportHeight(true);
			tabla.getTableHeader().setReorderingAllowed(false);

			scrollPane.setViewportView(tabla);

			while (rs.next()) {

				alumno = new Alumno();
				alumno.setDni(rs.getString("dni"));
				alumno.setNombre(rs.getString("nombre"));
				alumno.setApellidos(rs.getString("apellidos"));
				alumno.setGrupo(rs.getString("grupo"));
				alAlumno.add(alumno);

			}

			rs = st.executeQuery("SELECT * FROM bdalumnos.calificaciones;");
			while (rs.next()) {
				Vector<String> fila = new Vector<String>();
				fila.add(rs.getString("dni"));
				fila.add(rs.getString("codasignatura"));
				fila.add(rs.getString("nota"));
				fila.add("\n\n\n\n\n\n\n");
				datos.add(fila);
			}

			rs = st.executeQuery("SELECT * FROM bdalumnos.asignaturas;");
			while (rs.next()) {
				cmbCod.addItem(rs.getString("codasignatura"));
			}

			metodoOrdenacion = new TableRowSorter<>(dtm);
			tabla.setRowSorter(metodoOrdenacion);
			List<RowSorter.SortKey> sortKeys = new ArrayList<>();
			// para que ordene por la primera columna (dni en este caso) en Ascendente
			int columnIndexToSort = 0;
			sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
			metodoOrdenacion.setSortKeys(sortKeys);
			metodoOrdenacion.sort();

			regActual = 1;
			indice = 0;
			CargarDatos();

			st.close();
			conexion.close();

		} catch (SQLException sqle) {

			System.out.println("Error SQL " + sqle.getErrorCode() + ":\n" + sqle.getMessage());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		Object o = e.getSource();
		if (o == btnSiguiente) {
			regActual = regActual + 1;
			indice = indice + 1;
			CargarDatos();

		}
		if (o == btnAnterior) {
			regActual = regActual - 1;
			indice = indice - 1;
			CargarDatos();

		}
		if (o == btnUltimo) {
			regActual = alAlumno.size();
			indice = alAlumno.size() - 1;
			CargarDatos();
		}
		if (o == btnPrimero) {
			regActual = 1;
			indice = 0;
			CargarDatos();
		}

		if (o == btnInsertarAlumno) {
			InsertarDatosAlumno();
			CargarDatos();
		}
		if (o == btnBorrarAlumno) {
			BorrarDatosAlumno();
			CargarDatos();
		}
		if (o == btnActualizarAlumno) {
			ActualizarDatosAlumno();
			CargarDatos();
		}

		if (o == btnSalir) {
			System.exit(0);
		}

		if (o == btnInsertarNota) {
			InsertarDatoNota();
		}
		if (o == btnBorrarNota) {
			BorrarDatoNota();
		}
		if (o == btnActualizarNota) {
			ActualizarDatoNota();
		}
		if (o == btnSalir) {
			System.exit(0);
		}

	}

	public void CargarDatos() {
		alAlumno.sort(Comparator.naturalOrder());
		Alumno AlumnoActual = alAlumno.get(indice);
		regTotal = alAlumno.size();
		txtDNI.setText(AlumnoActual.getDni());
		DniOriginal = txtDNI.getText();
		txtNombre.setText(AlumnoActual.getNombre());
		txtApellidos.setText(AlumnoActual.getApellidos());
		txtGrupo.setText(AlumnoActual.getGrupo());

		lblRegistros.setText("Resgistro " + regActual + " de " + regTotal);

		metodoOrdenacion.setRowFilter(RowFilter.regexFilter(AlumnoActual.getDni(), 0));

		if (indice == alAlumno.size() - 1) {
			btnUltimo.setEnabled(false);
			btnSiguiente.setEnabled(false);
		} else {
			btnUltimo.setEnabled(true);
			btnSiguiente.setEnabled(true);
		}
		if (indice == 0) {
			btnPrimero.setEnabled(false);
			btnAnterior.setEnabled(false);
		} else {
			btnPrimero.setEnabled(true);
			btnAnterior.setEnabled(true);
		}
	}

	public void InsertarDatosAlumno() {
		try {
			conexion = DriverManager.getConnection("jdbc:mysql://localhost/bdalumnos", "root", "");
			// si se ha conectado correctamente
			// System.out.println("Conexión Correcta.");

			// Creo una sentencia
			Statement st = conexion.createStatement();
			// Creo un ResultSet
			String dni = txtDNI.getText();
			String nombre = txtNombre.getText();
			String apellidos = txtApellidos.getText();
			String grupo = txtGrupo.getText();
			st.executeUpdate(String.format("INSERT INTO bdalumnos.alumnos VALUES ('%s', '%s', '%s', '%s');", dni, nombre,
					apellidos, grupo));
			JOptionPane.showMessageDialog(this, (String) "Dato insertado", "Operacion completada",
					JOptionPane.INFORMATION_MESSAGE, null);

			alumno = new Alumno();
			alumno.setDni(dni);
			alumno.setNombre(nombre);
			alumno.setApellidos(apellidos);
			alumno.setGrupo(grupo);
			alAlumno.add(alumno);

			st.close();
			conexion.close();

		} catch (SQLException sqle) {
			// TODO Auto-generated catch block
			// si NO se ha conectado correctamente
			System.out.println("Error SQL " + sqle.getErrorCode() + ":\n" + sqle.getMessage());
			if (sqle.getErrorCode() == 1062) {
				JOptionPane.showMessageDialog(this, (String) "Ese DNI ya esta en la lista", "ERROR",
						JOptionPane.INFORMATION_MESSAGE, null);
			}
		}
	}

	public void BorrarDatosAlumno() {
		try {
			conexion = DriverManager.getConnection("jdbc:mysql://localhost/bdalumnos", "root", "");
			// si se ha conectado correctamente
			// System.out.println("Conexión Correcta.");

			// Creo una sentencia
			Statement st = conexion.createStatement();
			// Creo un ResultSet

			String dni = txtDNI.getText();

			st.executeUpdate(String.format("DELETE FROM bdalumnos.alumnos WHERE dni =  '%s';", dni));

			JOptionPane.showMessageDialog(this, (String) "Dato borrado", "Operacion completada",
					JOptionPane.INFORMATION_MESSAGE, null);

			alAlumno.remove(indice);

			st.close();
			conexion.close();

		} catch (SQLException sqle) {
			// TODO Auto-generated catch block
			// si NO se ha conectado correctamente
			System.out.println("Error SQL " + sqle.getErrorCode() + ":\n" + sqle.getMessage());
		}
	}

	public void ActualizarDatosAlumno() {
		try {
			conexion = DriverManager.getConnection("jdbc:mysql://localhost/bdalumnos", "root", "");
			// si se ha conectado correctamente
			// System.out.println("Conexión Correcta.");

			// Creo una sentencia
			Statement st = conexion.createStatement();

			String dni = txtDNI.getText();
			String Nombre = txtNombre.getText();
			String Apellidos = txtApellidos.getText();
			String grupo = txtGrupo.getText();
			if (DniOriginal.equals(dni)) {
				st.executeUpdate("UPDATE bdalumnos.alumnos SET grupo = '" + grupo + "', nombre = '" + Nombre
						+ "', apellidos = '" + Apellidos + "' WHERE dni = '" + dni + "';");
				JOptionPane.showMessageDialog(this, (String) "Dato modificado", "Operacion completada",
						JOptionPane.INFORMATION_MESSAGE, null);
				alumno = alAlumno.get(indice);
				alumno.setNombre(Nombre);
				alumno.setApellidos(Apellidos);
				alumno.setGrupo(grupo);
			} else {
				JOptionPane.showMessageDialog(this, (String) "El DNI no debe modificarse", "Operacion fallida",
						JOptionPane.INFORMATION_MESSAGE, null);
			}

			st.close();
			conexion.close();

		} catch (SQLException sqle) {
			// TODO Auto-generated catch block
			// si NO se ha conectado correctamente
			System.out.println("Error SQL " + sqle.getErrorCode() + ":\n" + sqle.getMessage());
		}
	}

	public void InsertarDatoNota() {
		try {
			conexion = DriverManager.getConnection("jdbc:mysql://localhost/bdalumnos", "root", "");
			// si se ha conectado correctamente
			// System.out.println("Conexión Correcta.");

			// Creo una sentencia
			Statement st = conexion.createStatement();

			String dni = txtDNI.getText();
			String cod = (String) cmbCod.getSelectedItem();
			String nota = txtNota.getText();
			st.executeUpdate(
					String.format("INSERT INTO bdalumnos.calificaciones VALUES ('%s', '%s', '%s');", dni, cod, nota));
			JOptionPane.showMessageDialog(this, (String) "Dato insertado", "Operacion completada",
					JOptionPane.INFORMATION_MESSAGE, null);

//			alumno = new Alumno();
//			alumno.setDni(dni);
//			alumno.setNombre(nombre);
//			alumno.setApellidos(apellidos);
//			alumno.setGrupo(grupo);
//			alAlumno.add(alumno);

			DefaultTableModel dtm = (DefaultTableModel) tabla.getModel();
			Vector<String> fila = new Vector<String>();
			fila.add(dni);
			fila.add(cod);
			fila.add(nota);
			dtm.addRow(fila);

			st.close();
			conexion.close();

		} catch (SQLException sqle) {
			// TODO Auto-generated catch block
			// si NO se ha conectado correctamente
			System.out.println("Error SQL " + sqle.getErrorCode() + ":\n" + sqle.getMessage());
			if (sqle.getErrorCode() == 1062) {
				JOptionPane.showMessageDialog(this, (String) "Ese DNI ya tiene una nota para esa asignatura", "ERROR",
						JOptionPane.INFORMATION_MESSAGE, null);
			}
		}
	}

	public void BorrarDatoNota() {
		try {
			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/bdalumnos", "root", "");
			// si se ha conectado correctamente
			// System.out.println("Conexión Correcta.");

			// Creo una sentencia
			Statement st = conexion.createStatement();
			// Creo un ResultSet

			if (seleccionado) {

				st.executeUpdate(String
						.format("DELETE FROM bdalumnos.calificaciones WHERE dni =  '%s' AND codasignatura = '%s';", dni, cod));

				JOptionPane.showMessageDialog(this, (String) "Dato borrado", "Operacion completada",
						JOptionPane.INFORMATION_MESSAGE, null);
				dtm.removeRow(filaAbs);
			} else {
				JOptionPane.showMessageDialog(this, (String) "Debe seleccionar un dato", "Error",
						JOptionPane.INFORMATION_MESSAGE, null);
			}

			st.close();
			conexion.close();

		} catch (SQLException sqle) {
			// TODO Auto-generated catch block
			// si NO se ha conectado correctamente
			System.out.println("Error SQL " + sqle.getErrorCode() + ":\n" + sqle.getMessage());
		}
	}

	public void ActualizarDatoNota() {
		try {
			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/bdalumnos", "root", "");
			// si se ha conectado correctamente
			// System.out.println("Conexión Correcta.");

			// Creo una sentencia
			Statement st = conexion.createStatement();
			// Creo un ResultSet

			if (seleccionado) {
				nota = txtNota.getText();
				st.executeUpdate("UPDATE bdalumnos.calificaciones SET nota = '" + nota + "' WHERE dni = '" + dni
						+ "' AND codasignatura = '" + cod + "';");

				JOptionPane.showMessageDialog(this, (String) "Dato modificado", "Operacion completada",
						JOptionPane.INFORMATION_MESSAGE, null);

				dtm.setValueAt(nota, filaAbs, 2);
			} else {
				JOptionPane.showMessageDialog(this, (String) "Debe seleccionar un dato", "Error",
						JOptionPane.INFORMATION_MESSAGE, null);
			}

			st.close();
			conexion.close();

		} catch (SQLException sqle) {
			// TODO Auto-generated catch block
			// si NO se ha conectado correctamente
			System.out.println("Error SQL " + sqle.getErrorCode() + ":\n" + sqle.getMessage());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		fila = tabla.getSelectedRow();
		seleccionado = true;
		filaAbs = tabla.convertRowIndexToModel(fila);
		dtm = (DefaultTableModel) tabla.getModel();
		dni = (String) dtm.getValueAt(filaAbs, 0);
		cod = (String) dtm.getValueAt(filaAbs, 1);
		nota = (String) dtm.getValueAt(filaAbs, 2);
		cmbCod.setSelectedItem(cod);
		txtNota.setText(nota);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
