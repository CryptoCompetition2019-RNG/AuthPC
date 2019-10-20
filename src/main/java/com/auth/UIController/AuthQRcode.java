package com.auth.UIController;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AuthQRcode extends JFrame implements MouseListener {

	JLabel bacgrangd, jan, bi, PP, pp, tu;// gif,最小化，关闭，logo，QQ，头像
	JLabel an1, an2;// 暗色块|线
	JPanel bgcolor;// 白
	JLabel text4, text5;// 请扫码认证，获取二维码
	static Point origin = new Point();// 变量，用于可拖动窗体
	int a = 0, b = 0, c = 0, d = 0;// 控制线
	int f = 0, g = 0, h = 0, j = 0;// 控制√
	JLabel submit, fp, ma, qrcode;;// 背景

	public AuthQRcode() {

		// 实例化
		bacgrangd = new JLabel(new ImageIcon("src//main//resources//image//1.gif"));
		jan = new JLabel(new ImageIcon("src//main//resources//image//最小化.png"));
		bi = new JLabel(new ImageIcon("src//main//resources//image//关闭.png"));
		//PP = new JLabel(new ImageIcon("src//main//resources//image//pp.png"));
		pp = new JLabel("TRNG");
		an1 = new JLabel();
		an2 = new JLabel();
		bgcolor = new JPanel();
		text4 = new JLabel("请扫码认证");
		text5 = new JLabel("确定");
		submit = new JLabel();
		fp = new JLabel(new ImageIcon("src//main//resources//image//fingerprint.png"));
		ma = new JLabel(new ImageIcon("src//main//resources//image//lock.png"));
		qrcode = new JLabel(new ImageIcon("src//main//resources//image//QRCode.png"));

		// 位置
		bacgrangd.setBounds(-35, -123, 500, 250);
		jan.setBounds(364, 2, 32, 32);
		bi.setBounds(396, 3, 32, 32);
		pp.setBounds(10, 5, 100, 45);
		an1.setBounds(361, 0, 35, 35);
		an2.setBounds(395, 0, 35, 35);
		bgcolor.setBounds(0, 125, 500, 300);
		text4.setBounds(15, 300, 200, 20);
		text5.setBounds(200, 285, 120, 20);
		submit.setBounds(100, 280, 242, 35);
		fp.setBounds(355, 290, 30, 30);
		ma.setBounds(385, 290, 30, 30);
		qrcode.setBounds(160, 150, 120, 120);
		// 属性
		pp.setFont(new Font("微软雅黑", 1, 25));
		pp.setForeground(Color.white);
		an1.setBackground(new Color(0, 0, 0, 0.3f));
		an2.setBackground(new Color(0, 0, 0, 0.3f));
		bgcolor.setBackground(new Color(255, 255, 255));

		text4.setFont(new Font("微软雅黑", 0, 12));
		text5.setFont(new Font("微软雅黑", Font.PLAIN, 17));
		text4.setForeground(new Color(170, 170, 170));
		text5.setForeground(Color.white);

		submit.setBackground(new Color(5, 186, 251));
		submit.setOpaque(true);

		// 事件区域
		jan.addMouseListener(this);
		bi.addMouseListener(this);
		submit.addMouseListener(this);
		fp.addMouseListener(this);
		ma.addMouseListener(this);
		this.addMouseListener(this);

		this.addMouseMotionListener(new MouseMotionListener() {// 窗体拖动事件
			public void mouseMoved(MouseEvent e) {
			}

			public void mouseDragged(MouseEvent e) {
				Point p = getLocation();
				setLocation(p.x + e.getX() - origin.x, p.y + e.getY() - origin.y);
			}
		});

		getContentPane().setLayout(null);// 布局

		getContentPane().add(jan);
		getContentPane().add(bi);
		getContentPane().add(pp);
		getContentPane().add(an1);
		getContentPane().add(an2);
		getContentPane().add(text4);
		getContentPane().add(text5);
		getContentPane().add(submit);
		getContentPane().add(fp);
		getContentPane().add(ma);
		getContentPane().add(qrcode);
		getContentPane().add(bgcolor);
		getContentPane().add(bacgrangd);

		this.setSize(430, 330);
		this.setIconImage(Toolkit.getDefaultToolkit().createImage("src//main//resources//image\\透明照片.png"));// 窗体图标
		this.setLocationRelativeTo(null);// 保持居中
		this.setUndecorated(true);// 去顶部
		this.setFocusable(true);// 面板首先获得焦点
		this.setBackground(new Color(255, 255, 255));// 背景颜色
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.setAlwaysOnTop(true);// 最顶层
		this.setVisible(true);// 显示
	}

	public void mouseClicked(MouseEvent e) {// 点击不恢复
	}

	public void mousePressed(MouseEvent e) {// 点击后
		if (e.getSource() == jan) {
			setExtendedState(JFrame.ICONIFIED);
		} else if (e.getSource() == this) {
			origin.x = e.getX();
			origin.y = e.getY();
		} else if (e.getSource() == bi) {
			System.exit(0);
		} else if (e.getSource() == ma) {
			dispose();
			new PC_UI();
		} 
	}

	public void mouseReleased(MouseEvent e) {// 点击时
		if (e.getSource() == submit || e.getSource() == text5) {
			text5.setFont(new Font("微软雅黑", 0, 15));
		}
	}

	public void mouseEntered(MouseEvent e) {// 悬停
		if (e.getSource() == jan) {
			an1.setOpaque(true);
		} else if (e.getSource() == bi) {
			an2.setOpaque(true);
		} else if (e.getSource() == fp) {
			fp.setIcon(new javax.swing.ImageIcon("src//main//resources//image//fingerprint.png"));
		}else if (e.getSource() == ma) {
			ma.setIcon(new javax.swing.ImageIcon("src//main//resources//image//lock.png"));
		}
	}

	public void mouseExited(MouseEvent e) {// 悬停后
		if (e.getSource() == jan) {
			an1.setOpaque(false);
		} else if (e.getSource() == bi) {
			an2.setOpaque(false);
		} else if (e.getSource() == fp) {
			fp.setIcon(new javax.swing.ImageIcon("src//main//resources//image//fingerprint.png"));
		} else if (e.getSource() == ma) {
			ma.setIcon(new javax.swing.ImageIcon("src//main//resources//image//lock.png"));
		}

	}
}
