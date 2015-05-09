

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
//add java packages 
//change  variable names
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ParseOutput 
{   
	
	//initialize the variables Variables 
	static ArrayList<ArrayList<Set<Integer>>> conceptValueSets = new ArrayList <ArrayList<Set<Integer>>>(); 
	static ArrayList<ArrayList<String>> attributeStringlist= new ArrayList<ArrayList<String>>();		
	static ArrayList<ArrayList<String>> conceptStringlist= new ArrayList<ArrayList<String>>();		
	static FileWriter fw;
	static ArrayList<ArrayList<Integer>> cardinalityList = new ArrayList <ArrayList<Integer>>(); 
	static ArrayList<ArrayList<Integer>> rowsFound = new ArrayList<ArrayList<Integer>>();
	static ArrayList<ArrayList<Integer>> columnFound = new ArrayList<ArrayList<Integer>>();	
	static ArrayList<Set<Integer>> conceptNotFound = new ArrayList<Set<Integer>>();
	static BufferedWriter bw;
	static ArrayList<Set<Integer>> attributeStar = new ArrayList<Set<Integer>>();
	static ArrayList <Set<Integer>> characteristicSet = new ArrayList<Set<Integer>>();
	static ArrayList <String>  columnHeading =new ArrayList <String>();
	static ArrayList<ArrayList <ArrayList<Set<Integer>>>> conceptRuleset= new ArrayList<ArrayList<ArrayList<Set<Integer>>>>();
	static ArrayList<ArrayList<ArrayList<String>>> conceptRulesetString= new  ArrayList<ArrayList<ArrayList<String>>>();
	static ArrayList<ArrayList <Set<Integer>>> intersectionRule = new ArrayList<ArrayList<Set<Integer>>>();
	static int noOfColumns;
	static ArrayList<ArrayList<String>> array= new ArrayList<ArrayList<String>>();
	static ArrayList<ArrayList<String>> dataTable= new ArrayList<ArrayList<String>>();
	static int noOfRows;
	static ArrayList<ArrayList<Set<Integer>>> attributeValuesets = new ArrayList <ArrayList<Set<Integer>>>(); 
	
	static int noOfAttributes;
	static ArrayList<Set<Integer>> casesFound = new ArrayList<Set<Integer>>();
	
	Set<Integer> alreadyFoundCases = new LinkedHashSet <Integer>();
		
	
	static ArrayList <Boolean> checkNumeric = new ArrayList <Boolean>(); 
	static ArrayList <Boolean> checkSymbols = new ArrayList <Boolean>(); 
	static ArrayList<ArrayList<Float>> cutpoints =new ArrayList<ArrayList<Float>>();
	
	

		// creating the new output file

	
	public static void main(String[] args) throws Exception
	{
		
			
		String inputFileName=null;
		boolean t =true;
		if(t){	
			Scanner scan_string = new Scanner(System.in); 
			boolean file_exists=false; 
				while(file_exists!=true)
				{	
					System.out.println("Please enter the name of your input file:: ");
					
					inputFileName=scan_string.next();
					File f= new File(inputFileName);
					file_exists=f.exists();
					if(!file_exists)
						
						System.out.println("NO such file exists.Please enter the correct file:: \n ");			
				}	
				scan_string.close();
				System.out.println("printing the output to the file");
				}	
		
		
		InputData f = new InputData(inputFileName);
			
		array= f.readdata();
		f.close();
		
		noOfColumns=array.get(0).size();
		  for (int i =1;i<array.size();i++)
				{	
					ArrayList<String> temp= new ArrayList<String>();
					for(int j=0;j<noOfColumns;j++)
					{	
						temp.add(j,array.get(i).get(j).toString());
					}	
				dataTable.add(i-1, temp);	
				}		
			noOfRows=dataTable.size();
			noOfAttributes=noOfColumns-1;
		
		
		
			
			
			
			
		//numericAndSymbols
		
			
			Pattern p = Pattern.compile("(^(((-)|(\\+)){0,1}))(\\d+)(((\\.)?(\\d+)){0,1}$)");
			boolean breakLoop;
			for(int j=0;j<noOfAttributes;j++)
			{
				breakLoop=false;
				for(int i=1;i<noOfRows && !breakLoop; i++)
				{
					Matcher match =p.matcher(dataTable.get(i).get(j));
					if(!match.find() )
					{
						
						breakLoop=true;
					}
					else
					{
						
					}
				}
				if(!breakLoop)
				{
					checkNumeric.add(j, true);
					checkSymbols.add(j, false);
				}
				else
				{
					checkNumeric.add(j, false);
					checkSymbols.add(j, true);
				}
					
			}
			
			
			
		
		
	//	attributePairs
		
		
		for(int j=0;j<noOfAttributes;j++)
		{	
			if(checkNumeric.get(j))
			{
				Set<Float> temp_cut_points =new HashSet<Float>();
				for(int k=1;k<noOfRows;k++)	
					temp_cut_points.add(Float.parseFloat(dataTable.get(k).get(j)));
				ArrayList<Float> floats_in_order=new ArrayList<Float>();
				for(Float w:temp_cut_points)
					floats_in_order.add(w);
				Collections.sort(floats_in_order);
			
				ArrayList<String> temp_string_list =new ArrayList<String>();
				ArrayList<Float> cut_points_in_order=new ArrayList<Float>();
				if(floats_in_order.size()>1)
					{
					for(int k=0;k<floats_in_order.size()-1;k++)
						cut_points_in_order.add((floats_in_order.get(k)+floats_in_order.get(k+1))/2);
					for(int k=0;k<cut_points_in_order.size();k++)
						{
							temp_string_list.add(floats_in_order.get(0)+".."+cut_points_in_order.get(k));
							temp_string_list.add(cut_points_in_order.get(k)+".."+floats_in_order.get(floats_in_order.size()-1));
						}		
					}
				else
					{
						cut_points_in_order.add(floats_in_order.get(0));
						temp_string_list.add(floats_in_order.get(0).toString());
					}
				
		
									
				ArrayList<Set<Integer>> conceptColumn= new ArrayList<Set<Integer>>();
				for(int k=0;k<cut_points_in_order.size();k++)
					{
						{
						
						Set<Integer> concept_set = new LinkedHashSet<Integer>();
							for(int w=1;w<noOfRows;w++)
								if(Float.parseFloat(dataTable.get(w).get(j))<cut_points_in_order.get(k))
									concept_set.add(w);
						conceptColumn.add(concept_set);				
						attributeValuesets.add(j,conceptColumn);
						
						}
						{
						
						Set<Integer> concept_set = new LinkedHashSet<Integer>();
						for(int w=1;w<noOfRows;w++)
								if(Float.parseFloat(dataTable.get(w).get(j))>cut_points_in_order.get(k))
								{
									concept_set.add(w);
								}
						conceptColumn.add(concept_set);
						attributeValuesets.add(j,conceptColumn);		
						
						}		
					}
					attributeStringlist.add(j,temp_string_list);
					cutpoints.add(j,cut_points_in_order);
				}
			if(!checkNumeric.get(j))
			{
			cutpoints.add(j,null);	
			ArrayList<Set<Integer>> conceptColumn= new ArrayList<Set<Integer>>();
			Set<String> temporary_list= new HashSet<String>();
				for(int k=1;k<noOfRows;k++)	
					temporary_list.add(dataTable.get(k).get(j).toString());			
			ArrayList<String> temp = new ArrayList<String>();	
			int count=0; 
				for(String w:temporary_list)
					{	
						Set<Integer> concept_set = new LinkedHashSet<Integer>();
							for(int k=1;k<noOfRows;k++)
									if(w.equalsIgnoreCase(dataTable.get(k).get(j)))
										concept_set.add(k);
						conceptColumn.add(concept_set);
						temp.add(count++,w);	
					}	
			attributeStringlist.add(j,temp);	
			attributeValuesets.add(j,conceptColumn);
			}
		}
	for(int j=0;j<noOfColumns;j++)
		columnHeading.add(dataTable.get(0).get(j));	
		
	
		
		//cardinality
		
		for(int j=0;j<attributeValuesets.size();j++)
		{	
			ArrayList<Integer> temp = new ArrayList <Integer>();
			for(int k=0;k<attributeValuesets.get(j).size();k++)
				{	
					temp.add(k, attributeValuesets.get(j).get(k).size());
				}
			cardinalityList.add(j,temp);
		}	
		
		
		
		
		//conceptValuePairs
		
		for(int j=noOfColumns-1;j<noOfColumns;j++)
		{	
		ArrayList<Set<Integer>> conceptColumn= new ArrayList<Set<Integer>>();
		Set<String> temporary_list= new LinkedHashSet<String>();		
			for(int k=1;k<noOfRows;k++)	
				temporary_list.add(dataTable.get(k).get(j).toString());					
		ArrayList<String> temp = new ArrayList<String>();			
		int count1=0; 	
			for(String w:temporary_list)
				{								
					Set<Integer> concept_set = new LinkedHashSet<Integer>();
						for(int k=1;k<noOfRows;k++)
							if(w.equals(dataTable.get(k).get(j)))
								concept_set.add(k);
					conceptColumn.add(concept_set);		
					temp.add(count1++, w);
					}
			conceptValueSets.add(0,conceptColumn);
			conceptStringlist.add(0,temp);		
			
		}	
		File newFile = new File("839output.txt");
		if (!newFile.exists()) {
			newFile.createNewFile();
			}

			 fw = new FileWriter(newFile.getAbsoluteFile());
			 bw = new BufferedWriter(fw);
		
		
		for(int i=0;i<conceptValueSets.get(0).size();i++)	
			findRule(i);
		
		
		for(int i=0;i<conceptRulesetString.size();i++)
		{
		
				
			for(int k=0;k<conceptRulesetString.get(i).size();k++)
			{
				bw.append("("+columnHeading.get(Integer.parseInt(columnFound.get(i).get(k).toString())));
				
				bw.append(" "+attributeStringlist.get(columnFound.get(i).get(k)).get(rowsFound.get(i).get(k))+")");
				
				
				if(k+1<conceptRulesetString.get(i).size())
					System.out.print(",");
			}
				}
		
		
		System.out.println("This is the end of the program. Printed the output to the file '839output' ");
		bw.close();
	 }
	
	
	

	
	


	public static void findRule(int a)
	{
	ArrayList <ArrayList<Set<Integer>>> rule_set= new ArrayList <ArrayList<Set<Integer>>>();
	ArrayList<ArrayList<String>> rule_set_string= new ArrayList <ArrayList<String>>();
	ArrayList <Set<Integer>> intersection_of_rule = new ArrayList <Set<Integer>>();
	ArrayList<Integer> found_rows = new ArrayList<Integer>();
	ArrayList<Integer> found_column = new ArrayList<Integer>();	
	Set<Integer> concept_block = new LinkedHashSet <Integer>();
	Set<Integer> current_goal = new LinkedHashSet <Integer>();	
	Set<Integer> current_intersecting_element = new LinkedHashSet <Integer>();	
	Set<Integer> current_intersection = new LinkedHashSet <Integer>();
	Set<Integer> could_not_find = new LinkedHashSet <Integer>();
	Set<Integer> alreadyFoundCases = new LinkedHashSet <Integer>();

	ScanInputData set_handler = new ScanInputData();

	float alpha =(float) 0.99;	
	int temp_coll=0;
	int temp_row=0;
	int rules_count=0;
	boolean found_rule=false;
	boolean atleast_one_selection=false;
	
	concept_block.addAll(conceptValueSets.get(0).get(a));
	current_goal.addAll(conceptValueSets.get(0).get(a));
	current_intersection=null;
	
	
	
	do			
	{	
	Set<Integer>current_intersection_rule =new LinkedHashSet<Integer>();	
	current_intersection_rule.addAll(concept_block);
		current_intersection=null;
	current_intersecting_element=null;
	ArrayList<Integer>rule_temp_row = new ArrayList<Integer>();
	ArrayList<Integer>rule_temp_column = new ArrayList<Integer>();
	ArrayList<Set<Integer>>possible_rule= new ArrayList<Set<Integer>>();	
	ArrayList<String>temp_string =new ArrayList<String>();	
	
	do		
	{	
				
		found_rule=false;
		int temp=0;
		atleast_one_selection=false;
		
		current_intersection_rule.retainAll(current_goal);
			int current_max_no_of_intersection=0;
		for(int j=0;j<attributeValuesets.size();j++)           
		{							
					for(int k=0;k<attributeValuesets.get(j).size();k++)		
					{	
						
						current_intersecting_element=set_handler.intersection(current_goal,attributeValuesets.get(j).get(k));   // calculate intersection of Goal and current a,v pair
						
						if(current_intersecting_element.size()>current_max_no_of_intersection && current_intersecting_element.size()!=0 && (!possible_rule.contains(attributeValuesets.get(j).get(k))) )       
								{  
									current_max_no_of_intersection=current_intersecting_element.size();
									temp_coll=j;
									temp_row=k; 
									atleast_one_selection=true;
							
								 }	
							else if(current_intersecting_element.size()==current_max_no_of_intersection && current_intersecting_element.size()!=0 && (!possible_rule.contains(attributeValuesets.get(j).get(k))))
								{	
									if(cardinalityList.get(j).get(k)<cardinalityList.get(temp_coll).get(temp_row))
										{
											current_max_no_of_intersection=current_intersecting_element.size();
											temp_coll=j;
											temp_row=k;
											atleast_one_selection=true;
										
										}
								}								
					}		
							
				
		}				
		if(atleast_one_selection==true)
		{
		if(current_intersection==null)
					current_intersection=attributeValuesets.get(temp_coll).get(temp_row);	
			else
					current_intersection=set_handler.intersection(current_intersection, attributeValuesets.get(temp_coll).get(temp_row));	
		current_goal=set_handler.intersection(current_goal,attributeValuesets.get(temp_coll).get(temp_row));
		if(set_handler.is_set1_subset_of_set2(current_intersection, concept_block))
				{
			current_intersection_rule.retainAll(current_intersection);
				rule_temp_row.add(temp, temp_row);
				rule_temp_column.add(temp,temp_coll );
				possible_rule.add(temp, attributeValuesets.get(temp_coll).get(temp_row));
				temp_string.add(temp++,attributeStringlist.get(temp_coll).get(temp_row));
				found_rule=true;
				}
		else if ((float)(current_goal.size()/current_intersection.size()) >alpha)
				{
				rule_temp_row.add(temp, temp_row);
				rule_temp_column.add(temp,temp_coll );
				possible_rule.add(temp, attributeValuesets.get(temp_coll).get(temp_row));
				temp_string.add(temp++,attributeStringlist.get(temp_coll).get(temp_row));				
				current_intersection_rule.retainAll(current_intersection);
				found_rule=true;
		
				}
			else
				{	
				
				rule_temp_row.add(temp, temp_row);
				rule_temp_column.add(temp,temp_coll );
				possible_rule.add(temp, attributeValuesets.get(temp_coll).get(temp_row));
				temp_string.add(temp++,attributeStringlist.get(temp_coll).get(temp_row));
				current_intersection_rule.retainAll(current_intersection);
					
				}
		}
		else
		{
		}
	}while(atleast_one_selection && !found_rule);   
	if(found_rule==true)
		{
			alreadyFoundCases=set_handler.union(alreadyFoundCases, current_goal);
			found_rows.addAll(rule_temp_row);
			found_column.addAll(rule_temp_column);
			rule_set.add(rules_count,possible_rule);
			intersection_of_rule.add(current_intersection_rule);
			rule_set_string.add(rules_count++,temp_string);
			current_goal=set_handler.subtract_setA_from_setB(alreadyFoundCases,concept_block);
		
		}	
	else
		{
			 current_goal=set_handler.subtract_setA_from_setB(could_not_find,concept_block);
		}
	}while(current_goal.size()!=0);        
	columnFound.add(a,found_column);
	rowsFound.add(a,found_rows);
	conceptRulesetString.add(a,rule_set_string);
	conceptRuleset.add(a,rule_set);
	intersectionRule.add(a,intersection_of_rule);
	casesFound.add(a, alreadyFoundCases);
	conceptNotFound.add(a,could_not_find);
	}
	
	

}












