
import java.util.HashSet;
import java.util.Set;


public class ScanInputData
{
	Set<Integer> intersection(Set<Integer> set1,Set<Integer> set2)
		{
			Set<Integer> inter_section= new HashSet<Integer>();
				for (Integer w:set1)
					for(Integer x:set2)
						if(w==x)
							inter_section.add(x);
			return inter_section;
		}
	
	Set<Integer> union(Set<Integer> set1,Set<Integer> set2)
	{
		Set<Integer> unioun_of_set= new HashSet<Integer>();
		unioun_of_set.addAll(set1);
		unioun_of_set.addAll(set2);
		return unioun_of_set;
	}


	
	boolean is_set1_subset_of_set2 (Set<Integer> set1,Set<Integer> set2)
	{
		if(set2.containsAll(set1))
			return true;
		else
			return false;
	}
	Set<Integer> subtract_setA_from_setB (Set<Integer> setA,Set<Integer> setB)
	{
		
		Set<Integer> subtracted_set= new HashSet<Integer>();
		subtracted_set.addAll(setB);
		for(Integer w:setA)
			if(setB.contains(w))
				subtracted_set.remove(w);		
		return subtracted_set;
		
	}
}
