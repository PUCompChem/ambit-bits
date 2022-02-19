package pu.ambit;

import org.openscience.cdk.interfaces.IAtomContainer;

import ambit2.smarts.IsomorphismTester;
import ambit2.smarts.SmartsHelper;
import ambit2.smarts.SmartsParser;
import ambit2.smarts.groups.GroupMatch;

public class SmartsDemo {

	public static void main(String[] args) throws Exception
	{
		searchForAllInstances("[H]O[#6]", "OC(=S)CCCCCCO");
		searchForAllInstances("[H]O[#6]", "OC(=S)CCC(CCO)CC(O)CO");		

	}
	
	
	public static int searchForAllInstances(String smarts, String smiles) throws Exception
    {
        try {
            IAtomContainer mol = SmartsHelper.getMoleculeFromSmiles(smiles, true) ;

            SmartsParser sp = new SmartsParser();
            IsomorphismTester isoTester = new IsomorphismTester();
            GroupMatch groupMatch = new GroupMatch(smarts, sp, isoTester);

            int posCount = groupMatch.matchCount(mol);
            System.out.println("Group " + smarts + " found at " + posCount + " positions in " + smiles);

            return posCount;

        } catch (Exception e) {
            System.out.println( "Cdk error smiles processing: " +smiles + " smarts: "+smarts);
            return -1;
        }
    }


	

}
