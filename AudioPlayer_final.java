import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.awt.event.*;
import java.awt.*;
import javax.swing.border.MatteBorder;

/**
 *  AudioPlayer_final
 */
public class AudioPlayer_final extends JPanel implements ActionListener
{
    //JFrame
    private JFrame player = new JFrame(" Unremarkable Wav-Audio Player");

    //int
    private int hold = 0;
    private int filecount = 0;
    private int listcount = 0;
    private int presscount = 0;
    private long currentAudioTime = 0; /*clip的音源是用「微秒」作單位，
                                        所以要看成秒的話，就要將音樂時間除以10的6次方*/

    //double
    private double db1 = 0.0;
    private double db2 = 0.0;

    //String
    private String [] file_audio = new String [256];
    private String gifimagepath0 = "gif/鸚鵡.gif";
    private String gifimagepath1 = "gif/Duck.gif";
    private String gifimagepath2 = "gif/Wave.gif";
    private String gifimagepath3 = "gif/Frog.gif";
    private String [] quotations = {"B   e      C   h   i   l   l",
                                    "F   e   e   l      M   y      R   h   y   t   h   m", 
                                    "R   e   l   a   x   i   n   g",
                                    "E   n   j   o   y   i   n   g",
                                    "I      C   a   n   '   t      S   t   o   p      M   e",
                                    "G   .   O   .   A   .   T   .",
                                    "Y      O      L      O"};
    
    //JLabel
    private JLabel bottomtext = new JLabel(quotations[0], JLabel.CENTER);
    private JLabel displayer = new JLabel("", JLabel.CENTER);
    private JLabel imageLabel1 = new JLabel(); 
    private JLabel imageLabel2 = new JLabel(); 

    //ImagerIcon
    private ImageIcon play = new ImageIcon(new ImageIcon("icon/play.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
    private ImageIcon pause = new ImageIcon(new ImageIcon("icon/pause.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
    private ImageIcon previous = new ImageIcon(new ImageIcon("icon/previous.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
    private ImageIcon next = new ImageIcon(new ImageIcon("icon/next.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
    private ImageIcon backten = new ImageIcon(new ImageIcon("icon/backward.png").getImage().getScaledInstance(50,50, Image.SCALE_SMOOTH));
    private ImageIcon forwardten = new ImageIcon(new ImageIcon("icon/forward.png").getImage().getScaledInstance(50,50, Image.SCALE_SMOOTH));
    private ImageIcon gifimage0 = new ImageIcon(new ImageIcon(gifimagepath0).getImage().getScaledInstance(100,100, Image.SCALE_DEFAULT));
    private ImageIcon gifimage1 = new ImageIcon(new ImageIcon(gifimagepath1).getImage().getScaledInstance(100,100, Image.SCALE_DEFAULT));
    private ImageIcon gifimage2 = new ImageIcon(new ImageIcon(gifimagepath2).getImage().getScaledInstance(100,100, Image.SCALE_DEFAULT));
    private ImageIcon gifimage3 = new ImageIcon(new ImageIcon(gifimagepath3).getImage().getScaledInstance(100,100, Image.SCALE_DEFAULT));
    private ImageIcon [] allgifimage = {gifimage0,gifimage2,gifimage3};
    
    //JPanel
    private JPanel displayerpn = new JPanel(new BorderLayout(0,0));
    private JPanel operaterpn = new JPanel(new GridLayout(1,5,2,2));
    private JPanel mixpn = new JPanel(new GridLayout(2,1,2,2));

    //JButton
    private JButton playBt = new JButton(play);
    private JButton previousBt = new JButton(previous);
    private JButton nextBt = new JButton(next);
    private JButton backtenBt = new JButton(backten);
    private JButton forwardtenBt = new JButton(forwardten);

    //JComboBox
    private JComboBox<String> list = new JComboBox<String>();
    
    //File
    private File sound;
    private File localfolder = new File("MusicList_wav");//C:/Users/user/Desktop/程式設計/大一下/程式設計4/專題/音樂播放器/MusicList_wav
    
    //AudioInputStream
    private AudioInputStream ais;

    //Clip
    private Clip clip;

    //Boolean
    private Boolean isnowstart = false;

    //Border
    private MatteBorder deepbluemb = new MatteBorder(2, 2, 2, 2, new Color(8, 46, 84));
    private MatteBorder whitemb = new MatteBorder(0, 2, 0, 2, Color.WHITE);
    
    //Random
    private Random ran = new Random();


    //BigDecimal
    private BigDecimal bd;

    public static void main(String[] args) {
        AudioPlayer_final apFinal = new AudioPlayer_final();
        apFinal.setVisible(true);

        ToDoList tdl = new ToDoList();
        tdl.setVisible(true);
    }

    AudioPlayer_final()
    {
        //讀取資料夾裡的音源檔(wav)
        if(localfolder.list().length > 0)
        {
            findAllFilesInFolder(localfolder);
        }
        else
        {
            list.addItem("");
            displayer.setText("No Files.");
            list.setSelectedIndex(0);
        }
        
        //設定list跟顯示器預設的文字
        list.setSelectedIndex(0);
        displayer.setText(file_audio[0]);

        //displayerpn設定
        /* 1.設定文字顯示器 */
        displayer.setFont(new Font("標楷體", Font.BOLD, 18));
        displayer.setOpaque(true);
        displayer.setBackground(new Color(75,92,116));
        displayer.setForeground(new Color(188, 194, 212));
        /* 2.設定gif圖檔(左) */
        imageLabel1.setOpaque(true);
        imageLabel1.setBackground(new Color(75,92,116));
        imageLabel1.setIcon(gifimage0);
        /* 3.設定gif圖檔(右) */
        imageLabel2.setOpaque(true);
        imageLabel2.setBackground(new Color(75,92,116));
        imageLabel2.setIcon(gifimage0);
        /* 4.設定好後，加到顯示器中 */
        displayerpn.add(imageLabel1, BorderLayout.WEST);
        displayerpn.add(displayer);
        displayerpn.add(imageLabel2, BorderLayout.EAST);
        displayerpn.setBorder(whitemb);
        
        //operater設定
        /* 1.設定play按鈕 */
        playBt.addActionListener(this);
        playBt.setBackground(new Color(75,92,116));
        playBt.setBorder(deepbluemb);
        playBt.setFocusPainted(false); // 是否填滿按鈕所在的區域
        /* 2.設定previous按鈕 */
        previousBt.addActionListener(this);
        previousBt.setBackground(new Color(75,92,116));
        previousBt.setBorder(deepbluemb);
        previousBt.setFocusPainted(false);
        /* 3.設定next按鈕 */
        nextBt.addActionListener(this);
        nextBt.setBackground(new Color(75,92,116));
        nextBt.setBorder(deepbluemb);
        nextBt.setFocusPainted(false);
        /* 4.設定 */
        backtenBt.addActionListener(this);
        backtenBt.setBackground(new Color(75,92,116));
        backtenBt.setBorder(deepbluemb);
        backtenBt.setFocusPainted(false);
        /* 5.設定 */
        forwardtenBt.addActionListener(this);
        forwardtenBt.setBackground(new Color(75,92,116));
        forwardtenBt.setBorder(deepbluemb);
        forwardtenBt.setFocusPainted(false);
        /* 6.設定好後，加到控制列中 */
        operaterpn.add(previousBt);
        operaterpn.add(backtenBt);
        operaterpn.add(playBt);
        operaterpn.add(forwardtenBt);
        operaterpn.add(nextBt);

        //mixpn設定
        mixpn.add(displayerpn);
        mixpn.add(operaterpn);

        //底部文字
        bottomtext.setFont(new Font("Magneto", Font.BOLD + Font.ITALIC, 12));
        bottomtext.setForeground(new Color(75,92,116));

        //設定combobox
        list.setBackground(new Color(188, 194, 212));
        list.setForeground(new Color(224, 255, 255));
        list.setFont(new Font("標楷體", Font.BOLD, 12));
        list.addActionListener(this);

        //播放器add元件
        player.add(list, BorderLayout.PAGE_START);
        player.add(mixpn);
        player.add(bottomtext, BorderLayout.PAGE_END);

        //播放器設定
        player.setSize(600,255);
        player.setLocationRelativeTo(null);
        player.setResizable(false);
        player.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //設定TitleBar圖示
        try {
            Image image = ImageIO.read(this.getClass().getResource("gif/咖波.png")); //建立圖片物件
            player.setIconImage(image);//設定圖示
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        player.setVisible(true);
    }

    
    //類:按鈕跟combobox的傾聽器運作
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        /* 1.觸發播放按鈕 */
        if(ae.getSource() == playBt)
        {
            try
            {
                if(isnowstart == false)
                {   
                    for(listcount = 0; listcount < 256; listcount++)
                    {
                        if(list.getSelectedIndex() == listcount && presscount == 0)
                        {
                            sound = new File(localfolder.toString() + "/" + file_audio[list.getSelectedIndex()]);
                            ais = AudioSystem.getAudioInputStream(sound);
                            clip = AudioSystem.getClip();
                            clip.open(ais);

                            //顯示指令                  
                            System.out.println("\n" + "↓=====================================↓");
                            System.out.println(list.getSelectedItem().toString());
                            System.out.println("執行指令 : 播放");
                            db1 = (double)currentAudioTime / 1000000;
                            bd = new BigDecimal(db1);
                            db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
                            System.out.println("歌曲進度 : " + db2 + "s");

                            db1 = (double)clip.getMicrosecondLength() / 1000000;
                            bd = new BigDecimal(db1);
                            db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
                            System.out.println("歌曲總長 : " + db2 + "s");
                            System.out.println("↑=====================================↑" + "\n");
                            //顯示指令

                            clip.start();
                            clip.loop(Clip.LOOP_CONTINUOUSLY);//無限播放
                            isnowstart = true;
                            playBt.setIcon(pause);
                            presscount++;
                        }
                        else if(list.getSelectedIndex() == listcount && presscount >= 1)
                        {
                            sound = new File(localfolder.toString() + "/" + file_audio[list.getSelectedIndex()]);
                            ais = AudioSystem.getAudioInputStream(sound);
                            clip = AudioSystem.getClip();
                            clip.open(ais);
                            clip.setMicrosecondPosition(currentAudioTime);

                            //顯示指令
                            System.out.println("\n" + "↓=====================================↓");
                            System.out.println(list.getSelectedItem().toString());
                            System.out.println("執行指令 : 播放");
                            db1 = (double)currentAudioTime / 1000000;
                            bd = new BigDecimal(db1);
                            db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
                            System.out.println("歌曲進度 : " + db2 + "s");

                            db1 = (double)clip.getMicrosecondLength() / 1000000;
                            bd = new BigDecimal(db1);
                            db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
                            System.out.println("歌曲總長 : " + db2 + "s");
                            System.out.println("↑=====================================↑" + "\n");
                            //顯示指令
                            
                            clip.start();
                            clip.loop(Clip.LOOP_CONTINUOUSLY);
                            isnowstart = true;
                            playBt.setIcon(pause);
                        }
                    }
                }
                else
                {
                    currentAudioTime = clip.getMicrosecondPosition();
                    if(currentAudioTime > clip.getMicrosecondLength())
                    {
                        while(currentAudioTime > clip.getMicrosecondLength())
                        {
                            currentAudioTime -= clip.getMicrosecondLength();
                        }
                    }
                    else
                    {
                        currentAudioTime = clip.getMicrosecondPosition();
                    }

                    //顯示指令
                    System.out.println("\n" + "↓=====================================↓");
                    System.out.println(list.getSelectedItem().toString());
                    System.out.println("執行指令 : 暫停");
                    db1 = (double)currentAudioTime / 1000000;
                    bd = new BigDecimal(db1);
                    db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
                    System.out.println("暫停節點 : " + db2 + "s");

                    db1 = (double)clip.getMicrosecondLength() / 1000000;
                    bd = new BigDecimal(db1);
                    db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
                    System.out.println("歌曲總長 : " + db2 + "s");
                    System.out.println("↑=====================================↑" + "\n");
                    //顯示指令

                    clip.stop();
                    isnowstart = false;
                    playBt.setIcon(play);
                    
                }
            }
            catch(Exception e) 
            {
                JLabel notice = new JLabel("You don't had any audio yet.");
                notice.setFont(new Font("Magic R", Font.PLAIN, 16));
                JOptionPane.showMessageDialog(player , notice, "⚠️系統提示", JOptionPane.PLAIN_MESSAGE, gifimage1);    
            }
        }

        /* 2-1.播放時，觸發上一首按鈕 */
        if(ae.getSource() == previousBt && isnowstart == true)
        {
            clip.stop();
            hold = list.getSelectedIndex();
            if(hold > 0 )
            {
                hold--;
                int random = ran.nextInt(3);
                imageLabel1.setIcon(allgifimage[random]);
                imageLabel2.setIcon(allgifimage[random]);
                list.setSelectedIndex(hold);
                bottomtext.setText(quotations[ran.nextInt(7)]);
                displayer.setText((String)list.getSelectedItem());

                try {
                    sound = new File(localfolder.toString() + "/" + file_audio[list.getSelectedIndex()]);
                    ais = AudioSystem.getAudioInputStream(sound);
                    clip = AudioSystem.getClip();
                    clip.open(ais);
                    clip.start();
                } catch (Exception e) {
                    JLabel notice = new JLabel("This is the First song.");
                    notice.setFont(new Font("Magic R", Font.PLAIN, 16));
                    JOptionPane.showMessageDialog(player , notice, "⚠️系統提示", JOptionPane.PLAIN_MESSAGE, gifimage1);
                }

                //顯示指令
                System.out.println("\n" + "↓=====================================↓");
                System.out.println(list.getSelectedItem().toString());
                System.out.println("執行指令 : 上一首");
                db1 = (double)currentAudioTime / 1000000;
                bd = new BigDecimal(db1);
                db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
                System.out.println("歌曲進度 : " + db2 + "s");

                db1 = (double)clip.getMicrosecondLength() / 1000000;
                bd = new BigDecimal(db1);
                db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
                System.out.println("歌曲總長 : " + db2 + "s");
                System.out.println("↑=====================================↑" + "\n");
                //顯示指令

            }
            else
            {
                isnowstart = false;
                playBt.setIcon(play);
            }
        }

        /* 2-2.停止時，觸發上一首按鈕 */
        if(ae.getSource() == previousBt && isnowstart == false)
        {
            currentAudioTime = 0;
            hold = list.getSelectedIndex();
            if(hold > 0 )
            {
                hold--;
                int random = ran.nextInt(3);
                imageLabel1.setIcon(allgifimage[random]);
                imageLabel2.setIcon(allgifimage[random]);
                list.setSelectedIndex(hold);
                bottomtext.setText(quotations[ran.nextInt(7)]);
                displayer.setText(list.getSelectedItem().toString());
                
                try {
                    sound = new File(localfolder.toString() + "/" + file_audio[list.getSelectedIndex()]);
                    ais = AudioSystem.getAudioInputStream(sound);
                    clip = AudioSystem.getClip();
                    clip.open(ais);

                } catch (Exception e) {
                    JLabel notice = new JLabel("This is the First song.");
                    notice.setFont(new Font("Magic R", Font.PLAIN, 16));
                    JOptionPane.showMessageDialog(player , notice, "⚠️系統提示", JOptionPane.PLAIN_MESSAGE, gifimage1);
                }

                //顯示指令
                System.out.println("\n" + "↓=====================================↓");
                System.out.println(list.getSelectedItem().toString());
                System.out.println("執行指令 : 上一首");
                db1 = (double)currentAudioTime / 1000000;
                bd = new BigDecimal(db1);
                db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
                System.out.println("歌曲進度 : " + db2 + "s");

                db1 = (double)clip.getMicrosecondLength() / 1000000;
                bd = new BigDecimal(db1);
                db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
                System.out.println("歌曲總長 : " + db2 + "s");
                System.out.println("↑=====================================↑" + "\n");
                //顯示指令
            }
            else
            {
                JLabel notice = new JLabel("This is the First song.");
                notice.setFont(new Font("Magic R", Font.PLAIN, 16));
                JOptionPane.showMessageDialog(player , notice, "⚠️系統提示", JOptionPane.PLAIN_MESSAGE, gifimage1);
            }
        }

        /* 3-1.播放時，觸發下一首按鈕 */
        if(ae.getSource() == nextBt && isnowstart == true)
        {
            clip.stop();
            hold = list.getSelectedIndex();
            if(hold < filecount-1)
            {
                hold++;
                int random = ran.nextInt(3);
                imageLabel1.setIcon(allgifimage[random]);
                imageLabel2.setIcon(allgifimage[random]);
                list.setSelectedIndex(hold);
                bottomtext.setText(quotations[ran.nextInt(7)]);
                displayer.setText((String)list.getSelectedItem());

                try {
                    sound = new File(localfolder.toString() + "/" + file_audio[list.getSelectedIndex()]);
                    ais = AudioSystem.getAudioInputStream(sound);
                    clip = AudioSystem.getClip();
                    clip.open(ais);
                    clip.start();
                } catch (Exception e) {
                    JLabel notice = new JLabel("This is the last song.");
                    notice.setFont(new Font("Magic R", Font.PLAIN, 16));
                    JOptionPane.showMessageDialog(player , notice, "⚠️系統提示", JOptionPane.PLAIN_MESSAGE, gifimage1);  
                }

                //顯示指令
                System.out.println("\n" + "↓=====================================↓");
                System.out.println(list.getSelectedItem().toString());
                System.out.println("執行指令 : 下一首");
                db1 = (double)currentAudioTime / 1000000;
                bd = new BigDecimal(db1);
                db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
                System.out.println("歌曲進度 : " + db2 + "s");

                db1 = (double)clip.getMicrosecondLength() / 1000000;
                bd = new BigDecimal(db1);
                db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
                System.out.println("歌曲總長 : " + db2 + "s");
                System.out.println("↑=====================================↑" + "\n");
                //顯示指令

            }
            else
            {
                isnowstart = false;
                playBt.setIcon(play);
            }
        }

        /* 3-2.停止時，觸發下一首按鈕 */
        if(ae.getSource() == nextBt && isnowstart == false)
        {
            currentAudioTime = 0;
            hold = list.getSelectedIndex();
            
            if(hold < filecount-1)
            {
                hold++;
                int random = ran.nextInt(3);
                imageLabel1.setIcon(allgifimage[random]);
                imageLabel2.setIcon(allgifimage[random]);
                list.setSelectedIndex(hold);
                bottomtext.setText(quotations[ran.nextInt(7)]);
                displayer.setText(list.getSelectedItem().toString());

                try {
                    sound = new File(localfolder.toString() + "/" + file_audio[list.getSelectedIndex()]);
                    ais = AudioSystem.getAudioInputStream(sound);
                    clip = AudioSystem.getClip();
                    clip.open(ais);

                } catch (Exception e) {
                    JLabel notice = new JLabel("This is the last song.");
                    notice.setFont(new Font("Magic R", Font.PLAIN, 16));
                    JOptionPane.showMessageDialog(player , notice, "⚠️系統提示", JOptionPane.PLAIN_MESSAGE, gifimage1);
                }
                
                //顯示指令
                System.out.println("\n" + "↓=====================================↓");
                System.out.println(list.getSelectedItem().toString());
                System.out.println("執行指令 : 下一首");
                db1 = (double)currentAudioTime / 1000000;
                bd = new BigDecimal(db1);
                db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
                System.out.println("歌曲進度 : " + db2 + "s");

                db1 = (double)clip.getMicrosecondLength() / 1000000;
                bd = new BigDecimal(db1);
                db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
                System.out.println("歌曲總長 : " + db2 + "s");
                System.out.println("↑=====================================↑" + "\n");
                //顯示指令

            }
            else
            {
                isnowstart = false;
                playBt.setIcon(play);
                JLabel notice = new JLabel("This is the last song.");
                notice.setFont(new Font("Magic R", Font.PLAIN, 16));
                JOptionPane.showMessageDialog(player , notice, "⚠️系統提示", JOptionPane.PLAIN_MESSAGE, gifimage1);
            }
        }
        /* 4-1.播放時，觸發倒回十秒按鈕 */
        if(ae.getSource() == backtenBt && isnowstart == true)
        {
            currentAudioTime = clip.getMicrosecondPosition();
            clip.stop();
            while(currentAudioTime >= clip.getMicrosecondLength())
            {
                currentAudioTime -= clip.getMicrosecondLength();
            }

            currentAudioTime -= 10 * 1000000;
            if(currentAudioTime < 0)
            {
                currentAudioTime = 0;
            }

            //顯示指令
            System.out.println("\n" + "↓=====================================↓");
            System.out.println(list.getSelectedItem().toString());
            System.out.println("執行指令 : 倒回10秒");
            db1 = (double)currentAudioTime / 1000000;
            bd = new BigDecimal(db1);
            db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
            System.out.println("歌曲進度 : " + db2 + "s");
            
            db1 = (double)clip.getMicrosecondLength() / 1000000;
            bd = new BigDecimal(db1);
            db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
            System.out.println("歌曲總長 : " + db2 + "s");
            System.out.println("↑=====================================↑" + "\n");
            //顯示指令

            try
            {
                clip.setMicrosecondPosition(currentAudioTime);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            
            }catch(Exception e) 
            {
                JLabel notice = new JLabel("You don't had any audio yet.");
                notice.setFont(new Font("Magic R", Font.PLAIN, 16));
                JOptionPane.showMessageDialog(player , notice, "⚠️系統提示", JOptionPane.PLAIN_MESSAGE, gifimage1);

            }
        }

        /* 4-2.停止時，觸發倒回十秒按鈕 */
        if(ae.getSource() == backtenBt && isnowstart == false)
        {
            presscount++;
            while(currentAudioTime >= clip.getMicrosecondLength())
            {
                currentAudioTime -= clip.getMicrosecondLength();
            }

            currentAudioTime -= 10 * 1000000;
            if(currentAudioTime <  0)
            {
                currentAudioTime = 0;
            }

            //顯示指令
            System.out.println("\n" + "↓=====================================↓");
            System.out.println(list.getSelectedItem().toString());
            System.out.println("執行指令 : 倒回10秒");
            db1 = (double)currentAudioTime / 1000000;
            bd = new BigDecimal(db1);
            db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
            System.out.println("歌曲進度 : " + db2 + "s");
            
            db1 = (double)clip.getMicrosecondLength() / 1000000;
            bd = new BigDecimal(db1);
            db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
            System.out.println("歌曲總長 : " + db2 + "s");
            System.out.println("↑=====================================↑" + "\n");
            //顯示指令
        }

        /* 5-1.播放時，觸發快進十秒按鈕 */
        if(ae.getSource() == forwardtenBt && isnowstart == true)
        {
            currentAudioTime = clip.getMicrosecondPosition();
            clip.stop();
            while(currentAudioTime >= clip.getMicrosecondLength())
            {
                currentAudioTime -= clip.getMicrosecondLength();
            }

            currentAudioTime += 10 * 1000000;
            if(currentAudioTime >= clip.getMicrosecondLength())
            {
                currentAudioTime = 0;
            }

            //顯示指令
            System.out.println("\n" + "↓=====================================↓");
            System.out.println(list.getSelectedItem().toString());
            System.out.println("執行指令 : 快進10秒");
            db1 = (double)currentAudioTime / 1000000;
            bd = new BigDecimal(db1);
            db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
            System.out.println("歌曲進度 : " + db2 + "s");
            
            db1 = (double)clip.getMicrosecondLength() / 1000000;
            bd = new BigDecimal(db1);
            db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
            System.out.println("歌曲總長 : " + db2 + "s");
            System.out.println("↑=====================================↑" + "\n");
            //顯示指令

            try
            {
                clip.setMicrosecondPosition(currentAudioTime);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            
            }catch(Exception e) 
            {
                JLabel notice = new JLabel("You don't had any audio yet.");
                notice.setFont(new Font("Magic R", Font.PLAIN, 16));
                JOptionPane.showMessageDialog(player , notice, "⚠️系統提示", JOptionPane.PLAIN_MESSAGE, gifimage1);
            }
        }

        /* 5-2.停止時，觸發快進十秒按鈕 */
        if(ae.getSource() == forwardtenBt && isnowstart == false)
        {
            presscount++;
            while(currentAudioTime >= clip.getMicrosecondLength())
            {
                currentAudioTime -= clip.getMicrosecondLength();
            }

            currentAudioTime += 10 * 1000000;
            if(currentAudioTime >= clip.getMicrosecondLength())
            {
                currentAudioTime = 0;
            }

            //顯示指令
            System.out.println("\n" + "↓=====================================↓");
            System.out.println(list.getSelectedItem().toString());
            System.out.println("執行指令 : 快進10秒");
            db1 = (double)currentAudioTime / 1000000;
            bd = new BigDecimal(db1);
            db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
            System.out.println("歌曲進度 : " + db2 + "s");
            
            db1 = (double)clip.getMicrosecondLength() / 1000000;
            bd = new BigDecimal(db1);
            db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
            System.out.println("歌曲總長 : " + db2 + "s");
            System.out.println("↑=====================================↑" + "\n");
            //顯示指令
        }

        /* 6.combobox觸發 */
        if(ae.getSource() == list)
        {
            String s = (String)list.getSelectedItem();
            currentAudioTime = 0;
            if (isnowstart == false && list.getSelectedIndex() != hold)
            {
                hold = list.getSelectedIndex();
                displayer.setText(s);
                int random = ran.nextInt(3);
                imageLabel1.setIcon(allgifimage[random]);
                imageLabel2.setIcon(allgifimage[random]);

                try {
                    sound = new File(localfolder.toString() + "/" + file_audio[list.getSelectedIndex()]);
                    ais = AudioSystem.getAudioInputStream(sound);
                    clip = AudioSystem.getClip();
                    clip.open(ais);
                } catch (Exception e) {

                }

                //顯示指令
                System.out.println("\n" + "↓=====================================↓");
                System.out.println(list.getSelectedItem().toString());
                System.out.println("執行指令 : 曲目選擇");
                db1 = (double)currentAudioTime / 1000000;
                bd = new BigDecimal(db1);
                db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
                System.out.println("歌曲進度 : " + db2 + "s");

                db1 = (double)clip.getMicrosecondLength() / 1000000;
                bd = new BigDecimal(db1);
                db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
                System.out.println("歌曲總長 : " + db2 + "s");
                System.out.println("↑=====================================↑" + "\n");
                //顯示指令

            }
            else if (list.getSelectedIndex() != hold)
            {
                clip.stop();
                displayer.setText(s);
                hold = list.getSelectedIndex();
                list.setSelectedIndex(hold);
                bottomtext.setText(quotations[ran.nextInt(7)]);
                int random = ran.nextInt(3);
                imageLabel1.setIcon(allgifimage[random]);
                imageLabel2.setIcon(allgifimage[random]);

                //顯示指令
                System.out.println("\n" + "↓=====================================↓");
                System.out.println(list.getSelectedItem().toString());
                System.out.println("執行指令 : 曲目選擇");
                db1 = (double)currentAudioTime / 1000000;
                bd = new BigDecimal(db1);
                db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
                System.out.println("歌曲進度 : " + db2 + "s");

                db1 = (double)clip.getMicrosecondLength() / 1000000;
                bd = new BigDecimal(db1);
                db2 = bd.setScale(1,RoundingMode.HALF_UP).doubleValue();
                System.out.println("歌曲總長 : " + db2 + "s");
                System.out.println("↑=====================================↑" + "\n");
                //顯示指令

                try {
                    sound = new File(localfolder.toString() + "/" + file_audio[list.getSelectedIndex()]);
                    ais = AudioSystem.getAudioInputStream(sound);
                    clip = AudioSystem.getClip();
                    clip.open(ais);
                    clip.start();
                } catch (Exception e) {
    
                }
            }
        }
    }

    //類:把wav檔加到combobox
    public void findAllFilesInFolder(File localfolder) {
		for (File file : localfolder.listFiles()) {
			if (!file.isDirectory()) {
                file_audio[filecount] = file.getName();
                list.addItem(file_audio[filecount]);
                displayer.setText(file_audio[filecount]);
                // list.setSelectedIndex(filecount);
                filecount++;
			}
            else 
            {
				findAllFilesInFolder(file);
			}
		}
	}
}