import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
//Runnable実装は12/17試用
public class MatrixPanel extends JPanel implements Runnable{
	Graphics G;
	private BufferedImage bi;
	private JButton[][] mbutton;
	private JLabel ip50,ip100,ip150,ip200,ip250;
	private JLabel pr50,pr100,pr150,pr200,pr250;
	Graphics g;
	MainWatch mainWatch;
	//SimpleLogRead slogread; 12/17以前
	Thread me;
	
	public MatrixPanel(){
		super();
	}
	
	public MatrixPanel(MainWatch mw){
		mainWatch=mw;
		setSize(1024,1024);
		setBackground(Color.WHITE);
		setLayout(new GridLayout(20,20));
		
		addMouseListener(new LetShowDetailListener());
		this.start();
	}
	
	public void paintComponent(Graphics g){
		int pw = 1024;
		int ph = 1024;
		
		if(bi == null){
			bi = new BufferedImage(pw,ph,BufferedImage.TYPE_INT_RGB);
			G = bi.createGraphics();
			G.setColor(Color.WHITE);
			G.fillRect(0,0,pw,ph);
			G.setColor(Color.GRAY);
			for(int i=0; i<=256; i++){
				G.drawLine(0,i*4,1024,i*4);
				}
			for(int i=0; i<=256; i++){
				G.drawLine(i*4,0,i*4,1024);
			}
			G.setColor(Color.BLACK);
			G.drawString("50",193,10);
			G.drawString("100",386,10);
			G.drawString("150",586,10);
			G.drawString("200",786,10);
			G.drawString("250",986,10);
			G.drawString("50",0,206);
			G.drawString("100",0,402);
			G.drawString("150",0,602);
			G.drawString("200",0,802);
			G.drawString("250",0,1002);
		}
		
		super.paintComponent(g);
		g.drawImage(bi,0,0,null);
		
		askVisualTrf(g); //12/17試用
		this.mainWatch.repaint();
	}
	
	//このメソッドは12/17試用
	public void askVisualTrf(Graphics g){
	    if(mainWatch==null) return;
	    if(mainWatch.vtraffic==null) return;
		for(int i=0; i<=255; i++){
			for(int j=0; j<=255; j++){
				if(mainWatch.vtraffic[i][j] != null){
					mainWatch.vtraffic[i][j].showPoint(g,mainWatch.directionBits,mainWatch.protocolBits);
				}
			}
        }
	}
	
	public void run(){
		while(me!=null){
			SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
			       repaint();
			    }
			});
			try{
			Thread.sleep(2000);
			}catch (InterruptedException ie){
				ie.printStackTrace();
			}
		}
	}
	
	class LetShowDetailListener implements MouseListener{
		public LetShowDetailListener(){
		}
		public void mouseClicked(MouseEvent e){
			try{
				int x=e.getX();
				int y=e.getY();
				x=x/4;
				y=y/4;
				mainWatch.main.writeApplicationMessage("["+x+":"+y+"] is clicked");
				if((mainWatch.vtraffic[x][y] != null)){
					mainWatch.vtraffic[x][y].showDetail(mainWatch.protocolBits, mainWatch.directionBits);
					//System.out.println(SimpleLogRead.vtraffic[x][y].capterform);
				}
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		}
		public void mouseEntered(MouseEvent e){
			System.out.println("In!");
			
		}
		public void mouseExited(MouseEvent e){
			
		}
		public void mousePressed(MouseEvent e){
			
		}
		public void mouseReleased(MouseEvent e){
			
		}
	}
	public void start(){
		if(me==null){
			me=new Thread(this,"MatrixPanelThread");
			me.start();
		}
	}
	public void stop(){
		me=null;
	}


}