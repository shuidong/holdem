package com.codesparkle.poker.client.ui;

import com.codesparkle.poker.client.Game;
import com.codesparkle.poker.shared.Chips;
import com.codesparkle.poker.shared.SharedHelpers;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;
import java.util.Random;


public class TableWindow extends JFrame {

    private static Random random = new Random();

    private ActionRepository actions;
    private PokerTable table;

    private JPanel controlPanel = new JPanel();
    private JButton callButton = new JButton("call");
    private JButton checkButton = new JButton("check");
    private JButton betButton = new JButton("bet");
    private JButton raiseButton = new JButton("raise");
    private JButton foldButton = new JButton("fold");
    private JLabel timeoutLabel = new JLabel();
    private JLabel statusLabel = new JLabel();
    private JTextArea messageDisplay = new JTextArea();
    private JTextField addressText = new JTextField("localhost", 10);
    private JTextField portText = new JTextField("4444", 4);
    private JTextField userText = new JTextField("client" + random.nextInt(1000));
    private JTextField amountText = new JTextField(5);

    private CardLayout controlSwitcher = new CardLayout(0, 0);

    private final Color validationSuccess = new Color(154, 205, 50);
    private final Color validationFailure = new Color(255, 165, 0);

    public TableWindow(Game game, PokerTable pokerTable) {
        table = pokerTable;
        actions = new ActionRepository(this, game);
        configureJFrame();
        addSplitPanel();
        addConnectPanel();
        addControlPanel();
        addMessagePanel();
        showPanel(ControlPanel.Connect);
        setButtonsEnabled(false);
        pack();
    }

    private void configureJFrame() {
        setAlwaysOnTop(true);
        URL iconPath = TableWindow.class.getResource("/pokericon.png");
        setIconImage(Toolkit.getDefaultToolkit().getImage(iconPath));
        setTitle("PokerClient");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(600, 100, 450, 300);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
    }

    private void addSplitPanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.add(table);
        JSplitPane gameControlsSplit = new JSplitPane();
        gameControlsSplit.setDividerSize(0);
        gameControlsSplit.setResizeWeight(1.0);
        gameControlsSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
        add(gameControlsSplit, BorderLayout.NORTH);
        gameControlsSplit.setLeftComponent(tablePanel);
        gameControlsSplit.setRightComponent(getControls());
    }

    private void addConnectPanel() {
        JPanel connectPanel = new JPanel();
        FlowLayout connectPanelLayout = (FlowLayout) connectPanel.getLayout();
        connectPanelLayout.setAlignment(FlowLayout.LEFT);
        controlPanel.add(connectPanel, ControlPanel.Connect.toString());
        JLabel portLabel = new JLabel("port:");
        JLabel addressLabel = new JLabel("address:");
        JLabel nameLabel = new JLabel("name:");
        JButton connectButton = new JButton("connect");
        portText.setMaximumSize(new Dimension(80, 40));
        addControls(connectPanel, portLabel, portText, addressLabel, addressText, nameLabel, userText, connectButton);
        wireUp(connectButton, Command.connectToServer);
    }

    private void addControlPanel() {
        JPanel afterDealPanel = new JPanel();
        afterDealPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(afterDealPanel, ControlPanel.AfterDeal.toString());
        JLabel amountLabel = new JLabel("bet/raise:");
        Component horizontalGlue = Box.createHorizontalGlue();
        amountText.addKeyListener(actions.getKeyAction(Command.validateAmount));
        amountText.setBackground(Color.RED);
        timeoutLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        addControls(afterDealPanel, amountLabel, amountText, checkButton, callButton, betButton, raiseButton,
                foldButton, horizontalGlue, timeoutLabel);
        wireUp(raiseButton, Command.raise);
        wireUp(foldButton, Command.fold);
        wireUp(betButton, Command.bet);
        wireUp(callButton, Command.call);
        wireUp(checkButton, Command.check);
    }

    private void addMessagePanel() {
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel subjectLabel = new JLabel("Message from Server: ");
        subjectLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
        controlPanel.add(messagePanel, ControlPanel.Message.toString());
        messageDisplay.setEditable(false);
        messageDisplay.setFont(new Font(Font.SANS_SERIF, 0, 12));
        addControls(messagePanel, subjectLabel, messageDisplay);
    }

    private Component getControls() {
        controlPanel.setLayout(controlSwitcher);
        controlPanel.add(new JPanel(), ControlPanel.Empty.toString());
        JPanel statusPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) statusPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        statusLabel.setMinimumSize(new Dimension(60, 20));
        statusLabel.setMaximumSize(new Dimension(100, 25));
        statusPanel.add(statusLabel);
        JSplitPane controlPanelSplit = new JSplitPane();
        controlPanelSplit.setResizeWeight(1.0);
        controlPanelSplit.setPreferredSize(new Dimension(600, 40));
        controlPanelSplit.setDividerSize(0);
        controlPanelSplit.setLeftComponent(controlPanel);
        controlPanelSplit.setRightComponent(statusPanel);
        JPanel controls = new JPanel();
        controls.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        controls.add(controlPanelSplit);
        return controls;

    }

    private void addControls(JPanel panel, Component... controls) {
        for (Component control : controls) {
            panel.add(control);
        }
    }

    private void wireUp(AbstractButton source, Command action) {
        source.addActionListener(actions.getAction(action));
    }

    public void displayGameStatus(String status) {
        statusLabel.setText(status);
    }

    public void showPanel(ControlPanel panel) {
        controlSwitcher.show(controlPanel, panel.toString());
    }

    public void requestUserAction(Chips minRaise, int timeout) {
        showPanel(ControlPanel.AfterDeal);
        amountText.setText("");
        callButton.setText("call (" + minRaise + ")");
        updateButtonStates();
        actions.startTimer(timeout);
        amountText.requestFocusInWindow();
    }

    public Chips getAmount() {
        return new Chips(Integer.parseInt(amountText.getText()));
    }

    public void setButtonsEnabled(boolean check, boolean call, boolean bet, boolean raise, boolean fold) {
        checkButton.setEnabled(check);
        callButton.setEnabled(call);
        betButton.setEnabled(bet);
        raiseButton.setEnabled(raise);
        foldButton.setEnabled(fold);
    }

    public void setButtonsEnabled(boolean check, boolean call, boolean bet, boolean raise) {
        setButtonsEnabled(check, call, bet, raise, true);
    }

    public void setButtonsEnabled(boolean all) {
        setButtonsEnabled(all, all, all, all, all);
    }

    public void displayServerMessage(String messageToShow) {
        showPanel(ControlPanel.Message);
        messageDisplay.setText(messageToShow);
    }

    public String getUsername() {
        return userText.getText();
    }

    public int getPort() {
        String port = portText.getText();
        return SharedHelpers.isPositiveNumber(port) ? Integer.parseInt(port) : 4444;
    }

    public String getAddress() {
        return addressText.getText();
    }

    public void updateDelay(String delay) {
        timeoutLabel.setText(delay);
    }

    public boolean isAmountValid() {
        return SharedHelpers.isPositiveNumber(amountText.getText());
    }

    public void updateButtonStates() {
        boolean canBetOrRaise = actions.canBetOrRaise();
        if (actions.isBettingStateUnchanged()) {
            setButtonsEnabled(true, false, canBetOrRaise, false);
        } else {
            setButtonsEnabled(false, actions.canCall(), false, canBetOrRaise);
        }
        amountText.setBackground(canBetOrRaise ? validationSuccess : validationFailure);
    }
}
