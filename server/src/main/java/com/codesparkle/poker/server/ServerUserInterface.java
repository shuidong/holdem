package com.codesparkle.poker.server;

import com.codesparkle.poker.shared.SharedHelpers;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ServerUserInterface extends JFrame {

    private JTextArea text = new JTextArea();
    private JButton startButton;
    private JButton stopButton;
    private JButton restartButton;
    private JTextField portText;
    private JTextField playerNumberText;

    public ServerUserInterface() {
        setAlwaysOnTop(true);
        setIconImage(Toolkit.getDefaultToolkit().getImage(
                ServerUserInterface.class.getResource("/pokericon_bw.png")));
        setTitle("PokerServer");
        setBounds(0, 100, 580, 350);
        JPanel contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(0, 1, 0, 0));
        JSplitPane splitPane = new JSplitPane();
        contentPane.add(splitPane);
        splitPane.setResizeWeight(1.0);
        splitPane.setDividerSize(0);

        DefaultCaret caret = (DefaultCaret) text.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        text.setBorder(new EmptyBorder(0, 0, 0, 20));
        text.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        text.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(text);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        splitPane.setLeftComponent(scrollPane);
        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(new EmptyBorder(10, 0, 0, 10));
        splitPane.setRightComponent(controlPanel);
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        startButton = new JButton("Start");
        startButton.setMaximumSize(new Dimension(100, 25));
        startButton.setMinimumSize(new Dimension(80, 25));
        startButton.addActionListener(e -> Main.startServer());
        controlPanel.add(startButton);

        stopButton = new JButton("Stop");
        stopButton.setMinimumSize(new Dimension(80, 25));
        stopButton.setMaximumSize(new Dimension(100, 25));
        stopButton.addActionListener(e -> Main.stopServer());
        controlPanel.add(stopButton);

        restartButton = new JButton("Restart");
        restartButton.setMinimumSize(new Dimension(80, 25));
        restartButton.setMaximumSize(new Dimension(100, 25));
        restartButton.addActionListener(e -> {
            Main.stopServer();
            SharedHelpers.sleep(100);
            Main.startServer();
        });
        controlPanel.add(restartButton);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(arg0 -> text.setText(""));
        clearButton.setMinimumSize(new Dimension(80, 25));
        clearButton.setMaximumSize(new Dimension(100, 25));
        controlPanel.add(clearButton);
        final JFileChooser saveDialog = new JFileChooser();
        final JFrame frame = this;
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {

            int result = saveDialog.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File saveTo = saveDialog.getSelectedFile();
                try (PrintWriter out = new PrintWriter(saveTo)) {
                    for (String line : text.getText().split("\n")) {
                        out.println(line);
                        out.flush();
                    }
                } catch (FileNotFoundException ex) {
                    output("Failed to save file.");
                }
            }
        });
        saveButton.setMinimumSize(new Dimension(80, 25));
        saveButton.setMaximumSize(new Dimension(100, 25));
        controlPanel.add(saveButton);

        Component verticalGlue = Box.createVerticalGlue();
        controlPanel.add(verticalGlue);

        JPanel playerNumberPanel = new JPanel();
        playerNumberPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        playerNumberPanel.setMaximumSize(new Dimension(100, 25));
        playerNumberPanel.setAlignmentX(0.0f);
        controlPanel.add(playerNumberPanel);

        JLabel labelPlayerNumber = new JLabel("Players: ");
        playerNumberPanel.add(labelPlayerNumber);

        playerNumberText = new JTextField();
        playerNumberText.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent arg0) {
                playerNumberText.selectAll();
            }
        });
        playerNumberText.setText("2");
        playerNumberText.setColumns(1);
        playerNumberPanel.add(playerNumberText);

        JPanel portPanel = new JPanel();
        portPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        portPanel.setMaximumSize(new Dimension(100, 25));
        portPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        FlowLayout fl_portPanel = (FlowLayout) portPanel.getLayout();
        fl_portPanel.setHgap(0);
        controlPanel.add(portPanel);

        JLabel portLabel = new JLabel("Port: ");
        portPanel.add(portLabel);

        portText = new JTextField();
        portText.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                portText.selectAll();
            }
        });
        portPanel.add(portText);
        portText.setColumns(4);
        portText.setText("4444");
    }

    public void showUI() {
        setVisible(true);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void output(String output) {
        text.append(output + "\n");
    }

    public void clearOutput() {
        text.setText("");
    }

    public int getPort() {
        return Integer.parseInt(portText.getText());
    }

    public int getPlayerNumber() {
        return Integer.parseInt(playerNumberText.getText());
    }

    public void setButtonStates(boolean serverRunning) {
        startButton.setEnabled(!serverRunning);
        stopButton.setEnabled(serverRunning);
        restartButton.setEnabled(serverRunning);
        portText.setEnabled(!serverRunning);
        playerNumberText.setEnabled(!serverRunning);
    }
}
