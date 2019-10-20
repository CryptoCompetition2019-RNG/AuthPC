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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.auth.NetworkUtils.RegisterHandler;

public class RegisterFailure extends JFrame implements MouseListener {

	JLabel bacgrangd, jan, bi, PP, pp, tu;// gif,最小化，关闭，logo，QQ，头像
	JLabel an1, an2, lie1;// 暗色块|线
	JTextField user;// 账号
	JPanel bgcolor;// 白
	JLabel su1;// 缩略图
	JLabel text3, text4, text5;//二维码获取失败，请扫码注册,获取二维码
	static Point origin = new Point();// 变量，用于可拖动窗体
	int a = 0, b = 0, c = 0, d = 0;// 控制线
	int f = 0, g = 0, h = 0, j = 0;// 控制√
	JLabel submit, fp, ma, qrcode;;// 背景

	public RegisterFailure() {

		// 实例化
		bacgrangd = new JLabel(new ImageIcon("src//main//resources//image//1.gif"));
		jan = new JLabel(new ImageIcon("src//main//resources//image//最小化.png"));
		bi = new JLabel(new ImageIcon("src//main//resources//image//关闭.png"));
		//PP = new JLabel(new ImageIcon("src//main//resources//image//pp.png"));
		pp = new JLabel("TRNG");
		an1 = new JLabel();
		an2 = new JLabel();// 暗调
		//tu = new JLabel(new ImageIcon("src//main//resources//image//头像1.png"));
		user = new JTextField();
		su1 = new JLabel(new ImageIcon("src//main//resources//image//1.png"));
		lie1 = new JLabel(new ImageIcon("src//main//resources//image//直线2.png"));
		bgcolor = new JPanel();
		text3 = new JLabel("二维码获取失败，请检查账号是否正确");
		text4 = new JLabel("请扫码注册");
		text5 = new JLabel("获取二维码");
		submit = new JLabel();
		fp = new JLabel(new ImageIcon("src//main//resources//image//fingerprint.png"));
		ma = new JLabel(new ImageIcon("src//main//resources//image//lock.png"));

		// 位置
		bacgrangd.setBounds(-35, -123, 500, 250);
		jan.setBounds(364, 2, 32, 32);
		bi.setBounds(396, 3, 32, 32);
		pp.setBounds(10, 5, 100, 45);
		an1.setBounds(361, 0, 35, 35);
		an2.setBounds(395, 0, 35, 35);
		user.setBounds(130, 130, 180, 40);
		su1.setBounds(100, 140, 20, 20);
		lie1.setBounds(100, 160, 240, 10);
		bgcolor.setBounds(0, 125, 500, 300);
		text3.setBounds(90, 200, 300, 20);
		text4.setBounds(15, 300, 200, 20);
		text5.setBounds(180, 285, 120, 20);
		submit.setBounds(100, 280, 242, 35);
		fp.setBounds(355, 290, 30, 30);
		ma.setBounds(385, 290, 30, 30);
		// 属性
		pp.setFont(new Font("微软雅黑", 1, 25));
		pp.setForeground(Color.white);
		an1.setBackground(new Color(0, 0, 0, 0.3f));
		an2.setBackground(new Color(0, 0, 0, 0.3f));
		bgcolor.setBackground(new Color(255, 255, 255));

		user.setForeground(Color.gray);
		user.setText("账号");
		user.setOpaque(false);// 透明背景
		user.setBorder(null);// 去掉边框
		user.setFont(new Font("微软雅黑", Font.PLAIN, 16));// 框内文字样式

		text3.setFont(new Font("微软雅黑", 0, 15));
		text4.setFont(new Font("微软雅黑", 0, 12));
		text5.setFont(new Font("微软雅黑", 0, 15));
		text3.setForeground(new Color(170, 170, 170));
		text4.setForeground(new Color(170, 170, 170));
		text5.setForeground(Color.white);

		submit.setBackground(new Color(5, 186, 251));
		submit.setOpaque(true);

		// 事件区域
		jan.addMouseListener(this);
		bi.addMouseListener(this);
		user.addMouseListener(this);
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

		user.addFocusListener(new FocusListener() {

			public void focusLost(FocusEvent e) {// 失去焦点
				su1.setIcon(new javax.swing.ImageIcon("src//main//resources//image//1.png"));
				lie1.setIcon(new javax.swing.ImageIcon("src//main//resources//image//直线2.png"));
				c = 0;
				if (user.getText().isEmpty()) {// 判断是否为空（为了设置默认提示语）
					user.setForeground(Color.gray);
					user.setText("账号");
				}
			}

			public void focusGained(FocusEvent e) {// 得到焦点
				user.setForeground(Color.black);
				lie1.setIcon(new javax.swing.ImageIcon("src//main//resources//image//直线3.png"));
				a = 1;
				c = 1;
				b = 0;
				su1.setIcon(new javax.swing.ImageIcon("src//main//resources//image//1.png"));
				if (user.getText().equals("账号")) {
					user.setText("");
				} else {
					user.setText(user.getText());
					user.selectAll();
				}
			}
		});

		getContentPane().setLayout(null);// 布局

		getContentPane().add(jan);
		getContentPane().add(bi);
		getContentPane().add(pp);
		getContentPane().add(an1);
		getContentPane().add(an2);
		getContentPane().add(lie1);
		getContentPane().add(user);
		getContentPane().add(su1);
		getContentPane().add(text3);
		getContentPane().add(text4);
		getContentPane().add(text5);
		getContentPane().add(submit);
		getContentPane().add(fp);
		getContentPane().add(ma);
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
		}  else if (e.getSource() == ma) {
			dispose();
			new PC_UI();
		} 
	}

	public void mouseReleased(MouseEvent e) {// 点击时
		if (e.getSource() == submit || e.getSource() == text5) {
			text5.setFont(new Font("微软雅黑", 0, 14));
			dispose();
			
			String users = user.getText();
			if (users != null && users.length() != 0) {
				RegisterHandler registerHandler = new RegisterHandler(users);
				//assertTrue(registerHandler.checkStatus());//有bug

		        BufferedImage bufferedImage = registerHandler.getQrcodeImage();
		        //assertNotNull(bufferedImage);//有bug
		        // 应该在界面上展示二维码，给手机端扫描
		        try {
					ImageIO.write(bufferedImage, "png", new File("src\\main\\resources\\image\\qrcode.png"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				new RegisterQRcode();
			} else {
				new RegisterFailure();
			}
			
		}
	}

	public void mouseEntered(MouseEvent e) {// 悬停
		if (e.getSource() == jan) {
			an1.setOpaque(true);
		} else if (e.getSource() == bi) {
			an2.setOpaque(true);
		} else if (e.getSource() == user) {
			if (a == 0 && c == 0) {
				lie1.setIcon(new javax.swing.ImageIcon("src//main//resources//image//直线4.png"));
			}
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
		} else if (e.getSource() == user) {
			if (a == 0) {
				lie1.setIcon(new javax.swing.ImageIcon("src//main//resources//image//直线2.png"));
			}
		} else if (e.getSource() == fp) {
			fp.setIcon(new javax.swing.ImageIcon("src//main//resources//image//fingerprint.png"));
		} else if (e.getSource() == ma) {
			ma.setIcon(new javax.swing.ImageIcon("src//main//resources//image//lock.png"));
		}

	}
}

