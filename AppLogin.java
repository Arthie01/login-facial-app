import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class AppLogin extends JFrame {

    // ── Componentes ───────────────────────────────
    private JLabel     lblEstado;
    private JLabel     lblFoto;
    private JButton    btnLogin;
    private JButton    btnCancelar;
    private JTextArea  txtLog;
    private JScrollPane scrollLog;
    private JLabel     lblUsuario;
    private JLabel     lblToken;

    public AppLogin() {
        configurarVentana();
        construirUI();
    }

    // ── Configuración de ventana ──────────────────
    private void configurarVentana() {
        setTitle("PC1 — Login Facial");
        setSize(550, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(245, 245, 245));
        setLayout(new BorderLayout(10, 10));
    }

    // ── Construcción de UI ────────────────────────
    private void construirUI() {

        // —— Panel título ——
        JLabel titulo = new JLabel("SISTEMA DE LOGIN FACIAL");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitulo = new JLabel("PC1 — Autenticación por reconocimiento facial");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitulo.setForeground(new Color(200, 220, 255));
        subtitulo.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel panelHeader = new JPanel(new GridLayout(2, 1));
        panelHeader.setBackground(new Color(34, 139, 34)); // Verde
        panelHeader.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        panelHeader.add(titulo);
        panelHeader.add(subtitulo);
        add(panelHeader, BorderLayout.NORTH);

        // —— Panel central ——
        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setBackground(new Color(245, 245, 245));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        // Instrucciones
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblInstrucciones = new JLabel("<html><center>Presiona el botón para iniciar la cámara<br>y capturar tu rostro</center></html>");
        lblInstrucciones.setFont(new Font("Arial", Font.PLAIN, 13));
        lblInstrucciones.setForeground(new Color(80, 80, 80));
        lblInstrucciones.setHorizontalAlignment(SwingConstants.CENTER);
        panelCentro.add(lblInstrucciones, gbc);

        // Preview de cámara
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        lblFoto = new JLabel("[ Vista previa de cámara ]", SwingConstants.CENTER);
        lblFoto.setPreferredSize(new Dimension(300, 200));
        lblFoto.setOpaque(true);
        lblFoto.setBackground(new Color(30, 30, 30));
        lblFoto.setForeground(new Color(150, 150, 150));
        lblFoto.setFont(new Font("Arial", Font.ITALIC, 12));
        lblFoto.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        panelCentro.add(lblFoto, gbc);

        // Panel de información de usuario
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel panelUsuario = new JPanel(new GridLayout(2, 1, 5, 5));
        panelUsuario.setBackground(new Color(245, 245, 245));
        panelUsuario.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            "Información de sesión",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.PLAIN, 11),
            new Color(100, 100, 100)
        ));

        lblUsuario = new JLabel("Usuario: ---");
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 12));
        lblToken = new JLabel("Token: ---");
        lblToken.setFont(new Font("Arial", Font.PLAIN, 10));
        lblToken.setForeground(new Color(100, 100, 100));

        panelUsuario.add(lblUsuario);
        panelUsuario.add(lblToken);
        panelCentro.add(panelUsuario, gbc);

        // Log de mensajes
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        txtLog = new JTextArea(4, 30);
        txtLog.setEditable(false);
        txtLog.setFont(new Font("Monospaced", Font.PLAIN, 11));
        txtLog.setBackground(new Color(250, 250, 250));
        txtLog.setForeground(new Color(60, 60, 60));
        txtLog.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        scrollLog = new JScrollPane(txtLog);
        scrollLog.setPreferredSize(new Dimension(300, 80));
        scrollLog.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            "Estado del proceso",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.PLAIN, 11),
            new Color(100, 100, 100)
        ));
        panelCentro.add(scrollLog, gbc);

        // Botón login
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        btnLogin = new JButton("🔐  Iniciar Login Facial");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBackground(new Color(34, 139, 34)); // Verde
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setPreferredSize(new Dimension(300, 42));
        btnLogin.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        panelCentro.add(btnLogin, gbc);

        // Botón cancelar
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        btnCancelar = new JButton("🗑  Limpiar");
        btnCancelar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnCancelar.setBackground(new Color(220, 220, 220));
        btnCancelar.setForeground(new Color(80, 80, 80));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setPreferredSize(new Dimension(300, 35));
        panelCentro.add(btnCancelar, gbc);

        add(panelCentro, BorderLayout.CENTER);

        // —— Panel inferior — indicador de estado ——
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setBackground(new Color(235, 235, 235));
        panelSur.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        lblEstado = new JLabel("🟢  Listo para autenticar", SwingConstants.CENTER);
        lblEstado.setFont(new Font("Arial", Font.BOLD, 13));
        lblEstado.setForeground(new Color(34, 139, 34));
        panelSur.add(lblEstado, BorderLayout.CENTER);

        add(panelSur, BorderLayout.SOUTH);

        // —— Acciones ——
        btnLogin.addActionListener(e -> iniciarLogin());
        btnCancelar.addActionListener(e -> limpiar());

        setVisible(true);
    }

    // ── Lógica de login ────────────────────────
    private void iniciarLogin() {
        // Deshabilitar botón mientras procesa
        btnLogin.setEnabled(false);
        lblEstado.setText("⏳  Iniciando cámara...");
        lblEstado.setForeground(new Color(200, 120, 0));
        lblFoto.setText("Esperando captura...");
        lblFoto.setForeground(Color.YELLOW);
        txtLog.setText(""); // Limpiar log

        // Llamar Python en hilo separado
        new Thread(() -> {
            try {
                // Llamar login_api.py
                ProcessBuilder pb = new ProcessBuilder("venv/bin/python", "login_api.py");
                pb.redirectErrorStream(true);
                Process proceso = pb.start();

                // Leer respuesta de Python línea por línea
                BufferedReader br = new BufferedReader(
                    new InputStreamReader(proceso.getInputStream())
                );

                String linea;
                String respuestaFinal = "";
                String token = "";
                String userId = "";
                String userName = "";
                
                while ((linea = br.readLine()) != null) {
                    System.out.println("Python: " + linea); // debug
                    
                    final String lineaActual = linea;
                    
                    // Filtrar warnings de Qt/OpenCV
                    if (linea.contains("QFontDatabase") || linea.contains("Qt no longer ships") || linea.contains("fontconfig") || linea.contains("dejavu-fonts")) {
                        continue;
                    }
                    
                    // Actualizar log en tiempo real
                    SwingUtilities.invokeLater(() -> {
                        txtLog.append(lineaActual + "\n");
                        txtLog.setCaretPosition(txtLog.getDocument().getLength());
                    });
                    
                    // Guardar última línea como respuesta final
                    if (linea.startsWith("LISTO:")) {
                        respuestaFinal = linea;
                        // Extraer nombre y ID
                        String temp = linea.replace("LISTO:", "");
                        if (temp.contains("(ID:")) {
                            userName = temp.substring(0, temp.indexOf("(ID:")).trim();
                            userId = temp.substring(temp.indexOf("(ID:") + 5, temp.indexOf(")"));
                        }
                    } else if (linea.startsWith("TOKEN:")) {
                        token = linea.replace("TOKEN:", "").trim();
                    } else if (linea.startsWith("ERROR:")) {
                        respuestaFinal = linea;
                    }
                }

                proceso.waitFor();
                final String resultado = respuestaFinal;
                final String tokenFinal = token;
                final String userIdFinal = userId;
                final String userNameFinal = userName;

                // Actualizar UI desde hilo Swing
                SwingUtilities.invokeLater(() -> {
                    if (resultado.startsWith("LISTO")) {
                        // ✅ Éxito
                        lblEstado.setText("✅  Autenticación exitosa");
                        lblEstado.setForeground(new Color(39, 174, 96));
                        lblFoto.setText("✔ Login exitoso");
                        lblFoto.setForeground(new Color(39, 174, 96));
                        
                        // Mostrar información de usuario
                        lblUsuario.setText("Usuario: " + userNameFinal);
                        lblToken.setText("Token: " + (tokenFinal.length() > 50 ? tokenFinal.substring(0, 50) + "..." : tokenFinal));
                        
                        btnLogin.setEnabled(true);

                    } else if (resultado.startsWith("ERROR")) {
                        // ❌ Error
                        String detalle = resultado.replace("ERROR:", "");
                        lblEstado.setText("❌ Error: " + detalle);
                        lblEstado.setForeground(Color.RED);
                        lblFoto.setText("[ Error en autenticación ]");
                        lblFoto.setForeground(Color.RED);
                        btnLogin.setEnabled(true);
                    } else {
                        // Cancelado o sin respuesta
                        lblEstado.setText("⚠️  Proceso cancelado");
                        lblEstado.setForeground(new Color(200, 120, 0));
                        btnLogin.setEnabled(true);
                    }
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    lblEstado.setText("Error al ejecutar Python");
                    lblEstado.setForeground(Color.RED);
                    txtLog.append("Error: " + ex.getMessage() + "\n");
                    btnLogin.setEnabled(true);
                });
            }
        }).start();
    }

    // ── Limpiar formulario ────────────────────────
    private void limpiar() {
        lblEstado.setText("🟢  Listo para autenticar");
        lblEstado.setForeground(new Color(34, 139, 34));
        lblFoto.setIcon(null);
        lblFoto.setText("[ Vista previa de cámara ]");
        lblFoto.setForeground(new Color(150, 150, 150));
        txtLog.setText("");
        lblUsuario.setText("Usuario: ---");
        lblToken.setText("Token: ---");
        btnLogin.setEnabled(true);
    }

    public static void main(String[] args) {
        // Look and feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(AppLogin::new);
    }
}
