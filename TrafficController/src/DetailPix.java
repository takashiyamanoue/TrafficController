import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class DetailPix {
	private String protocol;
	private String bounds;
	public String me,partner;
	private String time;
	public JScrollPane tablePane;
	
//	private String[] capterbuffer;
	public JTable capterbuffer;
	String[] tableColumnLabels = new String[]{"No.", "time","s mac","d mac", 
			      "protocol", "s ip","s port","bouns ", "d ip"," dport","","",""};
	String[][] tabledata = {
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
			   {"", "", "", "","", "", "", "","", "", "", "",},
	};	
	
	public JPanel detailPanel;
//	public JTextArea detailTxt;
	
	private int textque;//詳細のテキストの個数１０にするための、処理に用いる変数。
	private DefaultTableModel tableModel ;
	private int tableLines;
	private int tableColumns;
	
//	public DetailPix(long n, String s,String t,String b, String m,String p){
	public DetailPix(long n, String[] state, int direction, String match){
		protocol = state[2];
		me = state[3];
		partner = state[4];
		time = state[1];
//		System.out.println("n="+n);
        tablePane =new JScrollPane();
		tableModel=new DefaultTableModel(tabledata, tableColumnLabels);
	    capterbuffer = new JTable();
	    capterbuffer.setModel(tableModel);
//	    tablePane.add(capterbuffer);
	    tablePane.setViewportView(capterbuffer);
	    this.tableLines=capterbuffer.getRowCount();
	    this.tableColumns=capterbuffer.getColumnCount();
//	    System.out.println("tableLines="+tableLines);
	    capterbuffer.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				capterbufferMouseClicked(evt);
			}
		});
//		capterbuffer = new JTable(tableLines,tableColumns);
		textque=0;
//		appendDetail(n, time,states,bounds,me,partner);
		if(direction==1){
			bounds="TO";
		}
		else
		if(direction==2){
			bounds="FROM";
		}
		else{
			bounds="INSIDE";
		}
		
		
		appendDetail(n,state, bounds,match);
	}
	
	void capterbufferMouseClicked(MouseEvent evt){
		 Point point = evt.getPoint();
		  int row = capterbuffer.rowAtPoint(point);
		  int column = capterbuffer.columnAtPoint(point);
		  long n=(new Long((String)(capterbuffer.getValueAt(row, 0)))).longValue();
		  Runtime r = Runtime.getRuntime();
		  try{
			Process p = r.exec("c:\\program files\\wireshark\\wireshark -r test.pcap -g "+n);
		  }
		  catch(Exception e){
			System.out.println(e.toString()) ; 
		  }
	}
	
//	public synchronized void appendDetail(long n, String t, String s, String b, String m, String p){
	public void appendDetail(long n, String[] state, String b, String match){
		
		
//		capterbuffer[textque]= (t+"\t"+s+"\t"+b+"\t"+p+"\t\n");
		/*
		capterbuffer.setValueAt(""+n, textque, 0); // frame number
		capterbuffer.setValueAt(t,textque, 1); // time
		capterbuffer.setValueAt(s,textque, 2); // protocol
		capterbuffer.setValueAt(b,textque, 3); // bounds
		capterbuffer.setValueAt(m,textque, 4); // p
		capterbuffer.setValueAt(p,textque, 5); // partner
		*/
		capterbuffer.setValueAt(""+n, textque, 0); // frame number
		capterbuffer.setValueAt(state[1],textque, 1); // time
		capterbuffer.setValueAt(state[7], textque, 2); // source mac
		capterbuffer.setValueAt(state[8], textque, 3); // destination mac
		capterbuffer.setValueAt(state[2],textque, 4); // protocol
		capterbuffer.setValueAt(state[3], textque, 5); // source ip
		capterbuffer.setValueAt(state[5], textque, 6); // source port
		capterbuffer.setValueAt(b,textque, 7); // bounds
		capterbuffer.setValueAt(state[4],textque, 8); // destination ip
		capterbuffer.setValueAt(state[6],textque, 9); // destination port
		capterbuffer.setValueAt(state[0], textque, 10); // payload
		capterbuffer.setValueAt(match, textque, 11); // payload
		
		textque++;
		int lastLines=tableLines-7;
		if(textque>=tableLines){
			for(int i=lastLines; i<tableLines; i++){
				for(int j=0;j<tableColumns; j++){
					String w=(String)capterbuffer.getValueAt(i, j);
					capterbuffer.setValueAt(w,i-1,j);
				}
			}
			for(int j=0;j<tableColumns; j++){
				capterbuffer.setValueAt("",tableLines-1,j);
			}
			textque=tableLines-1;
		}

		
	}
	public void appendDetail(long n, String[] state, int direction, String match){
		if(direction==1){
			bounds="TO";
		}
		else
		if(direction==2){
			bounds="FROM";
		}
		else{
			bounds="INSIDE";
		}	
		appendDetail(n,state, bounds,match);
	}
}
