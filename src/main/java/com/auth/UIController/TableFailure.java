package com.auth.UIController;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TableFailure extends JFrame implements MouseListener {

	JLabel bacgrangd, jan, bi, PP, pp, tu;// gif,最小化，关闭，logo，QQ，头像
	JLabel an1, an2;// 暗色块|线
	JPanel bgcolor;// 白
	JLabel text1, text2, text3;// 忘记密码，其他方式登录，账户或密码错误
	static Point origin = new Point();// 变量，用于可拖动窗体
	JLabel submit;// 背景

	public TableFailure() {

		// 实例化
		bacgrangd = new JLabel(new ImageIcon("src//main//resources//image//1.gif"));
		jan = new JLabel(new ImageIcon("src//main//resources//image//最小化.png"));
		bi = new JLabel(new ImageIcon("src//main//resources//image//关闭.png"));
		pp = new JLabel("TRNG");
		an1 = new JLabel();
		an2 = new JLabel();
		bgcolor = new JPanel();
		text1 = new JLabel("忘记密码");
		text2 = new JLabel("其他方式登录");
		text3 = new JLabel("账户或密码错误");
		submit = new JLabel();

		// 位置
		bacgrangd.setBounds(-35, -123, 500, 250);
		jan.setBounds(364, 2, 32, 32);
		bi.setBounds(396, 3, 32, 32);
		//PP.setBounds(10, 10, 32, 32);
		pp.setBounds(10, 5, 100, 45);
		an1.setBounds(361, 0, 35, 35);
		an2.setBounds(395, 0, 35, 35);
		bgcolor.setBounds(0, 125, 500, 300);
		text1.setBounds(370, 300, 80, 20);
		text2.setBounds(15, 300, 80, 20);
		text3.setBounds(175, 195, 160, 20);
		submit.setBounds(100, 190, 242, 35);
		// 属性
		pp.setFont(new Font("微软雅黑", 1, 25));
		pp.setForeground(Color.white);
		an1.setBackground(new Color(0, 0, 0, 0.3f));
		an2.setBackground(new Color(0, 0, 0, 0.3f));
		bgcolor.setBackground(new Color(255, 255, 255));
		text1.setFont(new Font("微软雅黑", 0, 12));
		text2.setFont(new Font("微软雅黑", 0, 12));
		text3.setFont(new Font("微软雅黑", 0, 15));
		text1.setForeground(new Color(170, 170, 170));
		text2.setForeground(new Color(170, 170, 170));
		text3.setForeground(Color.white);

		submit.setBackground(new Color(5, 186, 251));
		submit.setOpaque(true);

		text1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		text2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		// 事件区域
		jan.addMouseListener(this);
		bi.addMouseListener(this);
		text1.addMouseListener(this);
		text2.addMouseListener(this);
		submit.addMouseListener(this);
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
		//this.add(PP);
		getContentPane().add(an1);
		getContentPane().add(an2);
		getContentPane().add(text1);
		getContentPane().add(text2);
		getContentPane().add(text3);
		getContentPane().add(submit);
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
		} else if (e.getSource() == submit || e.getSource() == text3 || e.getSource() == text2) {
			text3.setFont(new Font("微软雅黑", 0, 14));
			dispose();
			new PC_UI();
		}
	}

	public void mouseReleased(MouseEvent e) {// 点击时
		if (e.getSource() == submit || e.getSource() == text3) {
			text3.setFont(new Font("微软雅黑", 0, 15));
		}
	}

	public void mouseEntered(MouseEvent e) {// 悬停
		if (e.getSource() == jan) {
			an1.setOpaque(true);
		} else if (e.getSource() == bi) {
			an2.setOpaque(true);
		}
	}
	
	public void mouseExited(MouseEvent e) {// 悬停后
		if (e.getSource() == jan) {
			an1.setOpaque(false);
		} else if (e.getSource() == bi) {
			an2.setOpaque(false);
		} else if (e.getSource() == text1) {
			text1.setForeground(new Color(170, 170, 170));
		} else if (e.getSource() == text2) {
			text2.setForeground(new Color(170, 170, 170));
		} 

	}
}
