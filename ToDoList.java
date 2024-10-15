import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.IOException;

// implements ActionListener
public class ToDoList extends JPanel
{
    private JFrame list = new JFrame("ToDoList");
    private String [] text_cont = {"ppt介紹", "Run code", "功能示範", "程式碼介紹"};
    private MatteBorder bd = new MatteBorder(1,0,1,0, new Color(101,101,101));
    private Icon normal = new ImageIcon(new ImageIcon("ToDoList_pt/notyet.png").getImage().getScaledInstance(75,60, Image.SCALE_DEFAULT));
    private Icon selected = new ImageIcon(new ImageIcon("ToDoList_pt/Finish.png").getImage().getScaledInstance(75,60, Image.SCALE_DEFAULT));
    private ImageIcon endline = new ImageIcon(new ImageIcon("ToDoList_pt/porniuo.gif").getImage().getScaledInstance(265,265, Image.SCALE_DEFAULT));
    private JLabel jlb = new JLabel(endline);
    private JLabel jlb2 = new JLabel("報告結束~");
    private JPanel jpn = new JPanel(new BorderLayout(1,1));
    private JPanel jpn2 = new JPanel();
    private JPanel jpn3 = new JPanel(new GridLayout(6,1));

    public ToDoList()
    {
        for(int i = 0; i < text_cont.length; i++)
        {
            plus(text_cont[i]);
        }
        jpn.setBackground(new Color(250,251,245));
        jpn3.setBackground(new Color(250,251,245));
        jpn3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(101,101,101)),"報告清單"));
        jlb2.setForeground(new Color(227,140,122));
        jlb2.setBackground(new Color(250,251,245));
        jlb2.setFont(new Font("華康妙風體W2", Font.BOLD, 48));
        // jlb2.setHorizontalAlignment(SwingConstants.LEADING);
        jpn3.add(jlb2);
        jpn2.add(jlb);
        jpn.add(jpn3);
        jpn.add(jpn2, BorderLayout.PAGE_END);
        list.add(jpn);
        list.setBounds(0,30,265,792);
        list.setResizable(false);
        list.setVisible(true);

        try {
            Image image = ImageIO.read(this.getClass().getResource("gif/咖波.png")); //建立圖片物件
            list.setIconImage(image);//設定圖示
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void plus(String text)
    {
        
        JTable table = new JTable();
        TableCellRenderer renderer = table.getDefaultRenderer(Boolean.class);
        JCheckBox checkBoxRenderer = (JCheckBox)renderer;
        checkBoxRenderer.setIcon( normal );
        checkBoxRenderer.setSelectedIcon( selected );

        DefaultCellEditor editor = (DefaultCellEditor)table.getDefaultEditor(Boolean.class);
        JCheckBox checkBoxEditor = (JCheckBox)editor.getComponent();
        checkBoxEditor.setText(" " + text);
        checkBoxEditor.setIcon( normal );
        checkBoxEditor.setSelectedIcon( selected );
        checkBoxEditor.setFont(new Font("華康妙風體W2", Font.BOLD, 28));
        checkBoxEditor.setForeground(new Color(227,140,122));
        checkBoxEditor.setBackground(new Color(250,251,245));
        checkBoxEditor.setHorizontalAlignment(SwingConstants.LEADING);
        checkBoxEditor.setFocusPainted(false);
        checkBoxEditor.setBorderPainted(true);
        checkBoxEditor.setBorder(bd);

        jpn3.add(checkBoxEditor);
    }

    public static void main(String[] args) {

        ToDoList tdl = new ToDoList();
        tdl.setVisible(true);
    }

}