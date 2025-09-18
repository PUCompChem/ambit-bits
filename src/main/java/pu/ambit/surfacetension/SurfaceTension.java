package pu.ambit.surfacetension;

import java.io.File;

import ambit2.groupcontribution.GroupContributionModel;
import ambit2.groupcontribution.io.GCM2Json;

public class SurfaceTension 
{
	public String eCohGCMModelFile = null;
	GroupContributionModel gcmECoh = null; 
	
	public String molVolGCMModelFile = null;
	GroupContributionModel gcmMolVol = null;
	
	boolean flagGCMModelsStatus = false;
	
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
		
		flagGCMModelsStatus = true;
		return 0;
	}
}
