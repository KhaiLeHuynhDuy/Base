//package nro.server;
//
//
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
///*
//*
//* Author girlkun siêu cấp zíp pro
//*
// */
//import javax.swing.*;
//import nro.utils.Logger;
//
//public class Panel extends JPanel implements ActionListener {
//
//    private JButton baotri, thaydoiexp ,thaydoisk;
//    private JPanel addPanel;
//
//    public Panel() {
//        JPanel btri = new JPanel();
//        add(btri);
//        baotri = new JButton("Bảo trì");
//        baotri.addActionListener(this);
//        addPanel = new JPanel();
//        addPanel.add(baotri);
//        btri.add(addPanel);
//
//        JPanel exp = new JPanel();
//        add(exp);
//        thaydoiexp = new JButton("Thay đổi exp");
//        thaydoiexp.addActionListener(this);
//        addPanel = new JPanel();
//        addPanel.add(thaydoiexp);
//        exp.add(addPanel);
//
//        JPanel sk = new JPanel();
//        add(sk);
//        thaydoisk = new JButton("Thay đổi sự kiện");
//        thaydoisk.addActionListener(this);
//        addPanel = new JPanel();
//        addPanel.add(thaydoisk);
//        sk.add(addPanel);
//    }
//
//    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() == baotri) {
//            Maintenance.gI().start(15);
//            Logger.error("Tiến Hành Bảo Trì \n");
//        } else if (e.getSource() == thaydoiexp) {
//            String exp = JOptionPane.showInputDialog(this, "Nhập Exp muốn thay đổi\n" +
//                    "EXP hiện tại là :" +Manager.RATE_EXP_SERVER );
//            if (exp != null) {
//                Manager.RATE_EXP_SERVER = Byte.parseByte(exp);
//                Logger.error("EXP hiện tại là :" + exp);
//            }
//        } 
//    }
//}
//
//
//
///**
// * Copyright belongs to BTH, please do not copy the source code, thanks - BTH
// */