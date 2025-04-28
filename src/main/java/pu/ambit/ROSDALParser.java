package pu.ambit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Bond;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

public class ROSDALParser 
{
	static String mErrorMessages[] = {
			"",
			// 1
			"Missing atom at the end of the chain",
			// 2
			"Arbitrary bond not allowed!",		
			// 3
			"Not allowed symbol for a bond specification",
			// 4
			"Unknown atom type",
			// 5
			"Atom type for is previously defined as a different type",
	};

	public boolean allowArbitraryBond = false;

	String rosdal;	
	IAtomContainer mol;
	List<Integer> errors = new ArrayList<Integer>();
	List<String> errorParams = new ArrayList<String>();	
	Map<Integer,Integer> indexAtomMap = new TreeMap<Integer,Integer>();
	int prevAt;
	int currBond;
	boolean flagNewAtom;

	public int getNumOfErrors() {
		return errors.size();
	}
	
	public String getErrorMessages()
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < errors.size(); i++)
			sb.append(mErrorMessages[errors.get(i)]+ "  "
					+ errorParams.get(i).toString() + "\n");
		return(sb.toString());
	}

	public IAtomContainer parseROSDAL(String ros) {
		mol = new AtomContainer();
		errors.clear();
		errorParams.clear();
		indexAtomMap.clear();

		String frags[] = ros.split(",");
		int res;
		for (int i = 0; i < frags.length; i++) {
			res = parseLinearChain(frags[i]);
			if (res!=0)
				return null;
		}	

		return mol;
	}

	private int parseLinearChain(String chain)
	{
		//System.out.println("ROSDAL chain: " + chain);
		prevAt = -1;
		int pos = 0;
		int pos2 = atomEndPos(chain,pos);
		parseAtom(chain.substring(pos,pos2));

		pos = pos2;
		while (pos < chain.length())
		{
			currBond = parseBond(chain,pos);
			if (currBond < 0)
				return(currBond);

			pos++;
			if (pos>= chain.length())
			{	
				addError(1,chain);
				return(-1);
			}
			pos2 = atomEndPos(chain,pos);
			if (parseAtom(chain.substring(pos,pos2)) != 0)
				return(-1);
			pos = pos2;
		}

		return(0);
	}

	private int parseAtom(String atom)
	{
		//System.out.print("atom --> "+atom + ":  ");
		flagNewAtom = false;
		int rosdalAtIndex = 0;
		int atType = 0;
		int atNum;
		int pos;
		String atSymbol = "";

		for(pos = 0; pos<atom.length(); pos++)
		{	
			if (Character.isDigit(atom.charAt(pos)))
				rosdalAtIndex = rosdalAtIndex*10 + Character.getNumericValue(atom.charAt(pos));
			else
				break;
		}

		if (pos >= atom.length()) {
			atType = 6;
			atSymbol = "C";
		}	
		else
		{	
			int pos2 = pos+1;
			while (pos2<atom.length())
			{		
				if (atom.charAt(pos2) == 'H')
					break;
				pos2++;
			}
			atSymbol = atom.substring(pos,pos2);			
			atType = PerTable.getAtomNumber(atSymbol);

			if (atType == 0)
			{	
				addError(4,atSymbol);
				return(-1);
			}				
		}

		atNum = rosdalAtomIndexToCTAtomNumber(rosdalAtIndex);
		if (flagNewAtom) {
			//New Atom is created			
			IAtom at = new Atom(atSymbol);
			mol.addAtom(at);
		}	
		else
		{	
			if (!mol.getAtom(atNum).getSymbol().equals(atSymbol))
			{
				addError(5,atom);
				return(-1);
			}
		}


		if (prevAt != -1)
		{
			//Created new Bond
			IAtom at1 = mol.getAtom(prevAt);
			IAtom at2 = mol.getAtom(atNum);
			IBond bo = new Bond(at1, at2, getBondOrder(currBond));
			mol.addBond(bo);
		}
		
		prevAt = atNum;

		//System.out.print(atIndex + "   " + atType + "    rct num = "+ atNum);		
		//System.out.print("\n");
		return(0);
	}
	
	private IBond.Order getBondOrder(int boType) {
		switch (boType) {
		case 1:
			return IBond.Order.SINGLE;
		case 2:
			return IBond.Order.DOUBLE;
		case 3:
			return IBond.Order.TRIPLE;	
		}
		
		return IBond.Order.UNSET;
	}
	

	private int rosdalAtomIndexToCTAtomNumber(int rosdalIndex)
	{
		Integer o = indexAtomMap.get(new Integer(rosdalIndex));
		if (o == null)
		{
			int newAtNum = mol.getAtomCount();
			indexAtomMap.put(new Integer(rosdalIndex), new Integer(newAtNum));
			flagNewAtom = true;
			return(newAtNum);
		}
		else			
			return(o);
	}

	private int atomEndPos(String chain, int startPos)
	{
		int endPos;
		for(endPos=startPos; endPos<chain.length(); endPos++)
		{	
			if (!Character.isLetterOrDigit(chain.charAt(endPos)))
				break;
		}		
		return(endPos);
	}

	private int parseBond(String chain, int pos)
	{	
		switch (chain.charAt(pos))
		{
		case '-':
			return(1);
		case '=':
			return(2);
		case '#':
			return(3);
		case '?':
		{	
			if (allowArbitraryBond)
				return(0);
			else
			{	
				addError(2,chain);
				return(-1);
			}	
		}	
		}			

		addError(3,chain);
		return(-1);
	}

	private void addError(int errorType, String params)
	{
		errors.add(new Integer(errorType));
		errorParams.add(params);
	}


}
