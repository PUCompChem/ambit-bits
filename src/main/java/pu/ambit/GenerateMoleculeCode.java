package pu.ambit;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;


public class GenerateMoleculeCode {
	public static String getCDKCodeForMolecule(String smiles) throws Exception {
		return getCDKCodeForMolecule(smiles, false);
	}
	
	public static String getCDKCodeForMolecule(String smiles, boolean kekulize) throws Exception {		
		SmilesParser parser = new SmilesParser(SilentChemObjectBuilder.getInstance());
        parser.kekulise(kekulize);
        IAtomContainer mol = parser.parseSmiles(smiles);
        return getCDKCodeForMolecule(mol, "mol", "a", "b");
		
	}
	
	public static String getCDKCodeForMolecule(IAtomContainer mol, String molVar, String atPrefix, String boPrefix) {
		StringBuilder sb = new StringBuilder();
		sb.append("IAtomContainer " + molVar + " = new AtomContainer();\n");
		for (int i = 0; i < mol.getAtomCount(); i++) {
			IAtom at = mol.getAtom(i);
			String symbol = at.getSymbol();
			sb.append("IAtom " + atPrefix + i + " = new Atom(\"" + symbol +  "\");\n");
			if (at.getFormalCharge() != null && at.getFormalCharge() != 0)
				sb.append(atPrefix + i + ".setFormalCharge(" + at.getFormalCharge() + ");\n");
			if (at.getImplicitHydrogenCount() != null)
				sb.append(atPrefix + i + ".setImplicitHydrogenCount(" + at.getImplicitHydrogenCount() + ");\n");
			if (at.isAromatic())
				sb.append(atPrefix + i + ".setIsAromatic(true);\n");
			sb.append(molVar + ".addAtom(" + atPrefix + i + ");\n");
		}
		
		
		for (int i = 0; i < mol.getBondCount(); i++) {
			IBond bo = mol.getBond(i);
			int index0 = mol.indexOf(bo.getAtom(0));
			int index1 = mol.indexOf(bo.getAtom(1));
			sb.append("IBond " + boPrefix + i + " = new Bond(" 
					+ atPrefix + index0 + " ," + atPrefix + index1 + " ," + 
					 "IBond.Order." + bo.getOrder() + ");\n");
			//Check bond flags:
			if (bo.isAromatic())
				sb.append(boPrefix + i + ".setIsAromatic(true);\n");
			if (bo.isInRing())
				sb.append(boPrefix + i + ".setIsRing(true);\n");
				
			sb.append(molVar + ".addBond(" + boPrefix + i + ");\n");
		}
		
		return sb.toString();
	}
	
	
	public static String getJnaRinchiCodeForMolecule(String smiles) throws Exception {
		return getJnaRinchiCodeForMolecule(smiles, false);
	}
	
	public static String getJnaRinchiCodeForMolecule(String smiles, boolean kekulize) throws Exception {		
		SmilesParser parser = new SmilesParser(SilentChemObjectBuilder.getInstance());
        parser.kekulise(kekulize);
        IAtomContainer mol = parser.parseSmiles(smiles);
        return getJnaRinchiCodeForMolecule(mol, "ric", "a", "b");
		
	}
	
	public static String getJnaRinchiCodeForMolecule(IAtomContainer mol, String molVar, String atPrefix, String boPrefix) {
		StringBuilder sb = new StringBuilder();
		sb.append("RinchiInputComponent " + molVar + " = new RinchiInputComponent();\n");
		for (int i = 0; i < mol.getAtomCount(); i++) {
			IAtom at = mol.getAtom(i);
			String symbol = at.getSymbol();
			sb.append("InchiAtom " + atPrefix + i + " = new InchiAtom(\"" + symbol +  "\");\n");
			if (at.getFormalCharge() != null && at.getFormalCharge() != 0)
				sb.append(atPrefix + i + ".setCharge(" + at.getFormalCharge() + ");\n");
			if (at.getImplicitHydrogenCount() != null)
				sb.append(atPrefix + i + ".setImplicitHydrogen(" + at.getImplicitHydrogenCount() + ");\n");			
			sb.append(molVar + ".addAtom(" + atPrefix + i + ");\n");
		}
				
		for (int i = 0; i < mol.getBondCount(); i++) {
			IBond bo = mol.getBond(i);
			int index0 = mol.indexOf(bo.getAtom(0));
			int index1 = mol.indexOf(bo.getAtom(1));			
			String bondOrderStr = bo.getOrder().toString();
			
			sb.append("InchiBond " + boPrefix + i + " = new InchiBond(" 
					+ atPrefix + index0 + " ," + atPrefix + index1 + " ," + 
					 "InchiBondType." + bondOrderStr + ");\n");
				
			sb.append(molVar + ".addBond(" + boPrefix + i + ");\n");
		}
		
		return sb.toString();
	}
	
}
