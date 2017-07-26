package com.jh.ui.common;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.jh.common.Constants;

/**
 * java����
 * ���к󽫵�ǰ��Ļ��ȡ���������ʾ��
 * ��ק��꣬ѡ���Լ���Ҫ�Ĳ��֡�
 * ��Esc������ͼƬ�����棬���˳�����
 * ������Ͻǣ�û�пɼ��İ�ť�����˳����򣬲�����ͼƬ��
 * 
 * @author JinCeon
 */
public class Cut {
	public Cut() {
		// ȫ������
        RectD rd = new RectD();
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();
        gd.setFullScreenWindow(rd);
	}
}
 
class RectD extends JFrame {
    private static final long serialVersionUID = 1L;
    int orgx, orgy, endx, endy;
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    BufferedImage image;
    BufferedImage tempImage;
    BufferedImage saveImage;
    Graphics g;
 
    @Override
    public void paint(Graphics g) {
        RescaleOp ro = new RescaleOp(0.8f, 0, null);
        tempImage = ro.filter(image, null);
        g.drawImage(tempImage, 0, 0, this);
    }
 
    public RectD() {
        snapshot();
        setVisible(true);
        // setSize(d);//��󻯴���
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.addMouseListener(new MouseAdapter() {
        	@Override
            public void mousePressed(MouseEvent e) {
                orgx = e.getX();
                orgy = e.getY();
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
        	@Override
            public void mouseDragged(MouseEvent e) {
                endx = e.getX();
                endy = e.getY();
                g = getGraphics();
                g.drawImage(tempImage, 0, 0, RectD.this);
                int x = Math.min(orgx, endx);
                int y = Math.min(orgy, endy);
                int width = Math.abs(endx - orgx)+1;
                int height = Math.abs(endy - orgy)+1;
                // ����1����ֹwidth��heightΪ0
                g.setColor(Color.BLUE);
                g.drawRect(x-1, y-1, width+1, height+1);
                //��1����1����Ϊ�˷�ֹͼƬ�����ο򸲸ǵ�
                saveImage = image.getSubimage(x, y, width, height);
                g.drawImage(saveImage, x, y, RectD.this);
            }
        });
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // ��Esc���˳�
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    saveToFile();
                    dispose();
                }
            }
        });
    }
 
    public void saveToFile() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddHHmmss");
        String name = sdf.format(new Date());
//        File path = FileSystemView.getFileSystemView().getHomeDirectory(); // �����Ŀ¼
        String format = "jpg";
        Constants.cutName = File.separator + name + "." + format;
        File f = new File("e:/workspace/MyQQClient/image/" + Constants.cutName);
        
        try {
            ImageIO.write(saveImage, format, f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    public void snapshot() {
        try {
            Robot robot = new Robot();
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            image = robot.createScreenCapture(new Rectangle(0, 0, d.width,
                    d.height));
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
    
   
}