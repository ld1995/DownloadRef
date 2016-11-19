package gui;

import downloader.Data;
import downloader.State;
import gui.components.HintTextField;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Display extends JFrame {

    private JPanel rootPanel;
    private JButton stopButton;
    private JButton startPauseButton;
    private JButton saveButton;
    private JTextField urlField;
    private JLabel timeLabel;
    private JLabel linksInProcessCount;
    private JLabel linksDetectedCount;
    private JLabel stateLabel;
    private JButton openButton;
    private JTextField path;
    private JLabel finishCountOfLinks;
    private File file;
    private Timer timer;
    private Data data;

    {
        setContentPane(rootPanel);
        Dimension size = new Dimension(350, 400);
        setSize(new Dimension(size));
        setMinimumSize(new Dimension(size));
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setStoppedState();
        this.path.setBorder(BorderFactory.createEmptyBorder());

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileopen = new JFileChooser();
                fileopen.setFileFilter(new FileNameExtensionFilter("Текстовый документ", "txt"));
                int ret = fileopen.showSaveDialog(null);
                if (ret == JFileChooser.APPROVE_OPTION ) {
                    if (getExtension(fileopen.getSelectedFile()) != null)
                        file = fileopen.getSelectedFile();
                    else
                        file = new File(fileopen.getSelectedFile().getPath() + ".txt");
                    path.setText(file.getName());
                }
                else if (file == null)
                    warning("Пожалуйста, выберите файл для сохранения","Внимание!");
            }
        });

        startPauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (urlField.getText().trim().isEmpty()){
                    warning("Пожалуйста, введите сайт в поле для ввода", "Внимание");
                }
                else if (file == null) {
                    warning("Пожалуйста, выберите файл для сохранения","Внимание!");
                } else {
                    if (data == null) {
                        setActiveState();
                        data = new Data(Runtime.getRuntime().availableProcessors() * 2, urlField.getText().trim());
                        timer = new Timer(1, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                State state = data.getState();
                                setTime(state.getTimeElapsed());
                                linksDetectedCount.setText(String.valueOf(state.getLinksDetectedCount()));
                                linksInProcessCount.setText(String.valueOf(state.getLinksInProcessCount()));
                                finishCountOfLinks.setText(String.valueOf(state.getLinksDetectedCount() - state.getLinksInProcessCount()));
                                stateLabel.setText(state.isPaused() ? "Остановка" : "Сканирование");
                                if (state.getLinksInProcessCount() == 0) {
                                    stateLabel.setText("Запись");
                                    WriteData writeData = new WriteData(file, data.getUrlMap(), urlField.getText());
                                    writeData.write();
                                    setActiveEnd();
                                    openButton.setEnabled(true);
                                }
                            }
                        });
                        timer.start();
                    } else if (startPauseButton.getText().equals("Continue")) {
                        data.setPaused(false);
                        setActiveState();
                    } else if (startPauseButton.getText().equals("Pause")) {
                        data.setPaused(true);
                        setPausedState();
                    }
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (data != null) {
                    stateLabel.setText("Остановлен");
                    setActiveEnd();
                }
            }
        });

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.open(file);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        urlField = HintTextField.printHint("Поле для ввода адреса сайта");
    }

    private void setStoppedState() {
        startPauseButton.setText("Start");
        urlField.setEnabled(true);
        saveButton.setEnabled(true);
        stopButton.setEnabled(false);
        openButton.setEnabled(false);
    }

    private void setPausedState() {
        startPauseButton.setText("Continue");
        urlField.setEnabled(false);
        saveButton.setEnabled(true);
        stopButton.setEnabled(true);
    }

    private void setActiveState() {
        startPauseButton.setText("Pause");
        saveButton.setEnabled(false);
        urlField.setEnabled(false);
        stopButton.setEnabled(true);
        openButton.setEnabled(false);
    }

    private void setTime(long time) {
        long millis = time % 1000;
        time /= 1000;
        long sec = time % 60;
        time /= 60;
        long min = time % 60;
        time /= 60;
        long hours = time % 100;
        timeLabel.setText(String.format("%02d:%02d:%02d.%03d", hours, min, sec, millis));
    }

    private void setActiveEnd() {
        stateLabel.setText("Завершено");
        setStoppedState();
        timer.stop();
        data.close();
        data = null;
    }

    private void warning (String field, String title) {
        JOptionPane.showMessageDialog(this, field, title, JOptionPane.WARNING_MESSAGE);
    }

    private static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 &&  i < s.length() - 1)
            ext = s.substring(i+1).toLowerCase();
        return ext;
    }
}