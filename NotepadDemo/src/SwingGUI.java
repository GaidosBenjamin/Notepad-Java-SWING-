import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class SwingGUI implements ActionListener {
    private JFrame mainFrame;
    private JTextArea jText;
    private JMenu file, format, edit;
    private JMenuItem save, open, font, find, replace;
    private JMenuBar mBar;
    private JScrollPane jScrollText;

    SwingGUI(){
        showInterface();
    }

    private void showInterface(){
        mainFrame = new JFrame("BenjiPad");
        mainFrame.getContentPane().setLayout(new GridLayout(1,1));
        mainFrame.setSize(600,700);

        mBar = new JMenuBar();
        file = new JMenu("File");
        edit = new JMenu("Edit");
        format = new JMenu("Format");
        save = new JMenuItem("Save");
        open = new JMenuItem("Open");
        font = new JMenuItem("Font");
        find = new JMenuItem("Find");
        replace = new JMenuItem("Replace");

        save.addActionListener(this::actionPerformed);
        open.addActionListener(this::actionPerformed);
        find.addActionListener(this::actionPerformed);
        replace.addActionListener(this::actionPerformed);
        font.addActionListener(this::actionPerformed);

        jText = new JTextArea();
        jScrollText = new JScrollPane(jText);
        jScrollText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        file.add(save);
        file.addSeparator();
        file.add(open);
        edit.add(find);
        edit.addSeparator();
        edit.add(replace);
        format.add(font);
        mBar.add(file);
        mBar.add(edit);
        mBar.add(format);


        mainFrame.setJMenuBar(mBar);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //mainFrame.add(jText);
        mainFrame.getContentPane().add(jScrollText);
        mainFrame.setVisible(true);
    }

    //Maybe I should implement a separate class for FInd/ replace methods
    //Need to study more OOP and Design Patterns
    private String findReplaceDialog(String text, String oldStr, String newStr, int option){
        switch(option){
            case 1:
                return text.replace(oldStr, newStr);
            case 2:
                //regex to not change whole text to lower case ???
                return text.toLowerCase().replace(oldStr.toLowerCase(), newStr);
            default:
                return text;
        }
    }

    private String findCount(String text, String findStr, int option){
        String newStr;
        switch(option){
            case 2:
                newStr = text.toLowerCase().replace(findStr.toLowerCase(), "");
                return Integer.toString((text.length() - newStr.length())/findStr.length());
            case 1:
                newStr = text.replace(findStr, "");
                return Integer.toString((text.length() - newStr.length())/findStr.length());
            default:
                return "0";
        }

    }

    public void actionPerformed(ActionEvent ev){
        String text = jText.getText();
        if(ev.getSource() == save) {
            String txt = jText.getText();
            String fName = JOptionPane.showInputDialog(mainFrame, "Enter file name");
            if(fName != null) {
                IOFile fObj = new IOFile(fName);
                if (fObj.createFile(txt)) {
                    JOptionPane.showMessageDialog(mainFrame, "File saved!");
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Error at creating file!");
                }
            }
        }
        if(ev.getSource() == open){
            JFileChooser jFile = new JFileChooser("C:/Users/gaido/Documents/Java/NotepadDemo");
            int x = jFile.showOpenDialog(mainFrame);
            if(x == JFileChooser.APPROVE_OPTION){
                File f = jFile.getSelectedFile();
                String txt = IOFile.openFile(f.getPath());
                if(txt == "")
                    JOptionPane.showMessageDialog(mainFrame, "Error at opening!");
                else
                    jText.setText(txt);
            }
        }
        if(ev.getSource() == font) {
            //Use of pop-up factory and not JDialog
            //which would be more optimal, and Aesthetic
            //In the find/replace menu JDialog is used
            PopupFactory pf = new PopupFactory();

            JPanel panel = new JPanel(new GridLayout(2,2,2,2));
            panel.setSize(200,200);
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panel.setBackground(Color.BLACK);
            Popup po = pf.getPopup(mainFrame, panel, 200 , 200);

            final DefaultListModel<String> l1 = new DefaultListModel<>();
            l1.addElement("Monaco");
            l1.addElement("Serif");
            l1.addElement("Arial");
            l1.addElement("Boulder");
            final JList<String> fontList = new JList<>(l1);
            final DefaultListModel<String> l2 = new DefaultListModel<>();
            l2.addElement("14");
            l2.addElement("18");
            l2.addElement("24");
            l2.addElement("32");
            final JList<String> sizeList = new JList<>(l2);
            JButton ok = new JButton("Ok");
            ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(fontList.getSelectedIndex() != -1 && sizeList.getSelectedIndex() != -1){
                        Font font = new Font(fontList.getSelectedValue(), Font.PLAIN, Integer.parseInt(sizeList.getSelectedValue()));
                        jText.setFont(font);
                        po.hide();
                    }else{
                        JOptionPane.showMessageDialog(mainFrame, "Pick Font and Size!");
                    }
                }
            });
            JButton cancel = new JButton("Cancel");
            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    po.hide();
                }
            });
            panel.add(fontList);
            panel.add(sizeList);
            panel.add(ok);
            panel.add(cancel);
            po.show();
        }
        if(ev.getSource() == replace){

            JDialog jD = new JDialog(mainFrame, "Find and Replace", true);
            jD.setSize(400, 160);
            jD.setLayout(new GridLayout(4,1,0,10));
            JTextField jFind = new JTextField();
            JTextField jReplace = new JTextField();
            JLabel f = new JLabel("   Find:");
            JLabel r = new JLabel("   Replace:");
            JButton ok = new JButton("Ok");
            JButton cancel = new JButton("Cancel");
            JRadioButton r2 = new JRadioButton("Case insensitive");
            JRadioButton r1 = new JRadioButton("Case sensitive");

            ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String oldStr = jFind.getText();
                    String newStr = jReplace.getText();
                    if(r1.isSelected()){
                        jText.setText(findReplaceDialog(text, oldStr, newStr, 1));
                    }
                    else{
                        jText.setText(findReplaceDialog(text, oldStr, newStr, 2));
                    }
                    jD.setVisible(false);
                }
            });

            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    jD.setVisible(false);
                }
            });

            jD.add(f);
            jD.add(jFind);
            jD.add(r);
            jD.add(jReplace);
            jD.add(r1);
            jD.add(r2);
            jD.add(ok);
            jD.add(cancel);
            jD.setVisible(true);
        }
        if(ev.getSource() == find){
            JDialog jD = new JDialog(mainFrame, "Find and Replace", true);
            jD.setSize(400, 140);
            jD.setLayout(new GridLayout(3,1,0,10));
            JTextField jFind = new JTextField();
            JLabel f = new JLabel("   Find:");
            JButton ok = new JButton("Ok");
            JButton cancel = new JButton("Cancel");
            JRadioButton r2 = new JRadioButton("Case insensitive");
            JRadioButton r1 = new JRadioButton("Case sensitive");

            ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String oldStr = jFind.getText();
                    if(r1.isSelected()){

                        JOptionPane.showMessageDialog(mainFrame,"Found " + findCount(text,oldStr,1) + " times.");
                    }
                    else{
                        JOptionPane.showMessageDialog(mainFrame, "Found " + findCount(text,oldStr,2) + " times.");
                    }
                    jD.setVisible(false);
                }
            });

            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    jD.setVisible(false);
                }
            });

            jD.add(f);
            jD.add(jFind);
            jD.add(r1);
            jD.add(r2);
            jD.add(ok);
            jD.add(cancel);

            jD.setVisible(true);
        }
    }
}

