package pu.ambit;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.smiles.SmilesGenerator;

public class MoleculeUtils {
	
	
	public static String getSmiles(IAtomContainer mol) throws Exception {		
		SmilesGenerator sg = SmilesGenerator.generic();
		return sg.create(mol);
	}
	
}
