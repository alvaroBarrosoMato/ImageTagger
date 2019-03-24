package ImageTagger.ImageTagger;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**********************************************************************
* Project           : Image Tagger
*
* Description     	: Program to create Tagged Image Sets 
*
* Author            : Alvaro Barroso Mato
*
* Date created      : 11/01/2019
*
**********************************************************************/

public class HighScore {
	public String name;
	public int Points;
	public String Date;
	public int Cortes;
	public String mac;
	
	public HighScore(String name, int points, String date, int cortes) {
		super();
		byte[] MAC = null;
		try {
			Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();
			MAC =  ni.nextElement().getHardwareAddress();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(MAC != null) {
			mac = MAC.toString();
		}else {
			mac = "";
		}
		
		this.name = name;
		Points = points;
		Date = date;
		Cortes = cortes;
	}
	
	public String toTable() {
		return "<tr>\n" + 
				"    <td>" + name + "</td>\n" + 
				"    <td> " + Points + "</td>\n" + 
				"    <td>\" + Date + \"</td>\n" + 
				"    <td>\" + Cortes + \"</td>\n" + 
				"  </tr>";
	}
	
}