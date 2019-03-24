package ImageTagger.ImageTagger;
import java.io.Serializable;


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

public class Options implements Serializable{
	
private static final long serialVersionUID = 1L;

public int numDisparos;
public int time;

public boolean disparosB;
public boolean timeBar;
public boolean scopeBlock;

public boolean isJSON;

public Options() {
	super();
	this.numDisparos = 3;
	this.time = 20000;
	this.disparosB = true;
	this.timeBar = true;
	this.scopeBlock = true;
	isJSON = true;
}


}
