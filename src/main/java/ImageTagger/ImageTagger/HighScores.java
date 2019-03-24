package ImageTagger.ImageTagger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


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


public class HighScores implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<HighScore> highScores;

	public HighScores(List<HighScore> highScores) {
		super();
		this.highScores = highScores;
	}

	public HighScores() {
		this.highScores = new ArrayList<HighScore>();
	}
	
}
