package pu.ambit;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.smiles.SmilesGenerator;

public class MoleculeUtils {
	
	
	public static String getSmiles(IAtomContainer mol) throws Exception {		
		SmilesGenerator sg = SmilesGenerator.generic();
		return sg.create(mol);
	}
	
	public static String rosdalToSmiles(String rosdal) throws Exception {
		ROSDALParser rosdalParser = new ROSDALParser();	
		IAtomContainer mol = rosdalParser.parseROSDAL(rosdal);
		if (rosdalParser.getNumOfErrors() > 0)
			throw new Exception("ROSDAL prser error:" + rosdalParser.getErrorMessages());
		
		return getSmiles(mol);
	}
	
}
