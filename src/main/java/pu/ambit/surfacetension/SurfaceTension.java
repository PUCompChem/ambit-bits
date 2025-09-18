package pu.ambit.surfacetension;

import java.io.File;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import ambit2.groupcontribution.GroupContributionModel;
import ambit2.groupcontribution.io.GCM2Json;
import ambit2.smarts.SmartsHelper;

public class SurfaceTension 
{
	private double stCoeff = 0.75;
	
	String eCohGCMModelFile = null;
	GroupContributionModel gcmECoh = null; 
	
	String molVolGCMModelFile = null;
	GroupContributionModel gcmMolVol = null;
	
	boolean flagGCMModelsStatus = false;
	
	public SurfaceTension(String eCohGCMModelFile, String molVolGCMModelFile) {
		this.eCohGCMModelFile = eCohGCMModelFile;
		this.molVolGCMModelFile = molVolGCMModelFile;
	}
	
	public int setupGCMModels() throws Exception
	{
		GCM2Json g2j = new GCM2Json();
		
		gcmECoh = g2j.loadFromJSON(new File(eCohGCMModelFile));
		if (!g2j.configErrors.isEmpty())
		{	
			System.out.println(g2j.getAllErrorsAsString());
			flagGCMModelsStatus = false;
			return -1;
		}	
		
		gcmMolVol = g2j.loadFromJSON(new File(molVolGCMModelFile));
		if (!g2j.configErrors.isEmpty())
		{	
			System.out.println(g2j.getAllErrorsAsString());
			flagGCMModelsStatus = false;
			return -1;
		}
		
		gcmECoh.setAllowGroupRegistration(false);
		gcmMolVol.setAllowGroupRegistration(false);
		flagGCMModelsStatus = true;
		return 0;
	}
	
	public double calcSurfaceTenstion(String smiles) throws Exception 
	{
		IAtomContainer mol = SmartsHelper.getMoleculeFromSmiles(smiles, false);
		AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
		return calcSurfaceTenstion(mol);
	}
	
	public double calcSurfaceTenstion(IAtomContainer mol) 
	{					
		double eCoh = gcmECoh.calcModelValue(mol);
		double molVol = gcmMolVol.calcModelValue(mol);
		double CED = eCoh/molVol;
		return stCoeff * Math.pow(CED, 2.0/3);
	}
}
