package pu.ambit.surfacetension;

import java.io.File;

import ambit2.groupcontribution.GroupContributionModel;
import ambit2.groupcontribution.io.GCM2Json;

public class SurfaceTension 
{
	public String eCohGCMModelFile = null;
	GroupContributionModel gcmECoh = null; 
	
	public int setupGCMModel() throws Exception
	{
		GCM2Json g2j = new GCM2Json();
		gcmECoh = g2j.loadFromJSON(new File(eCohGCMModelFile));
		
		if (!g2j.configErrors.isEmpty())
		{	
			System.out.println(g2j.getAllErrorsAsString());
			return -1;
		}	
		else if (!g2j.configErrors.isEmpty())
			System.out.println(g2j.getAllErrorsAsString());
		
		return 0;
	}
}
