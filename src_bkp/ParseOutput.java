

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
	static Set<Integer> fat_D = new HashSet <Integer>();
	
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
		
			
		//	Pattern p = Pattern.compile("(^(((-)|(\\+)){0,1}))(\\d+)(((\\.)?(\\d+)){0,1}$)");
		//Pattern p = Pattern.compile("((((-)|(\\*)|(\\?)))|(\\d+)(((\\.?)(\\d+)){0,1}))");
			Pattern p = Pattern.compile("(^(\\d+)(((\\.)?(\\d+)){0,1}$))");
			boolean isSymbolic;
			boolean isNumeric;
			for(int j=0;j<noOfAttributes;j++)
			{
				isSymbolic=false;
				isNumeric=false;
				for(int i=1;i<noOfRows && !isSymbolic && !isNumeric; i++)
				{
					if((dataTable.get(i).get(j).equalsIgnoreCase("?")) || (dataTable.get(i).get(j).equalsIgnoreCase("*")) || (dataTable.get(i).get(j).equalsIgnoreCase("-")))
						continue;
					Matcher match =p.matcher(dataTable.get(i).get(j));
					if(!match.find() )
					{
						
						isSymbolic=true;
					}
					else
					{
						isNumeric=true;
					}
				}
				if(isNumeric)
				{
					checkNumeric.add(j, true);
					checkSymbols.add(j, false);
				}
				else if(isSymbolic)
				{
					checkNumeric.add(j, false);
					checkSymbols.add(j, true);
				}
					
			}
			
			
			
			//conceptValuePairs - separating cases with different concepts into different arraylists
			
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
		
	//	attributePairs
		
		
		for(int j=0;j<noOfAttributes;j++)
		{	
			if(checkNumeric.get(j))
			{
				Set<Float> temp_cut_points =new HashSet<Float>();
				for(int k=1;k<noOfRows;k++){
					if((!dataTable.get(k).get(j).equalsIgnoreCase("?")) && (!dataTable.get(k).get(j).equalsIgnoreCase("*")) && (!dataTable.get(k).get(j).equalsIgnoreCase("-")))
						temp_cut_points.add(Float.parseFloat(dataTable.get(k).get(j)));
				}
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
					//Lesser than cutpoint block
						{
					//Checking for * - ? cases and adding the cases to corresponding blocks	
						Set<Integer> concept_set = new LinkedHashSet<Integer>();
							for(int w=1;w<noOfRows;w++){
								int splvalueconcept=0;	
								int splvaluecase=0;
								boolean breakspl=false;
								if((!dataTable.get(w).get(j).equalsIgnoreCase("?"))){
									if(dataTable.get(w).get(j).equals("-")){
										splvaluecase=w;
										breakspl=false;
										for(int row=0;row<conceptValueSets.size() && !breakspl;row++){
											for(int colm=0;colm<conceptValueSets.get(row).size() && !breakspl;colm++){
												for(int setnum=0;setnum<conceptValueSets.get(row).get(colm).size();setnum++){
													if(conceptValueSets.get(row).get(colm).contains(w)){
														splvalueconcept=colm;
														breakspl =true;
														break;
													}
												}
											}
										}
										//splvalueconcept gives me which concept my case belongs to by pointing to row number in arraylist
										//for(int colm=0;colm<conceptValueSets.get(splvalueconcept).size();colm++){
										for(int cn:conceptValueSets.get(0).get(splvalueconcept)){
											int casenum=0;
											float caseval=0.0f;
											casenum= cn;
											if(casenum != splvaluecase){
												caseval=Float.parseFloat(dataTable.get(casenum).get(j).toString());
												if(caseval < cut_points_in_order.get(k)){
													concept_set.add(w);
													break;
												}
											}
										}
									}
									else if(dataTable.get(w).get(j).equals("*") || Float.parseFloat(dataTable.get(w).get(j))<cut_points_in_order.get(k)){
										concept_set.add(w);
									}
								}
							}
						conceptColumn.add(concept_set);		
						
						}
						//Greater than cutpoint block
						{
						
						Set<Integer> concept_set = new LinkedHashSet<Integer>();
						for(int w=1;w<noOfRows;w++){
						int splvalueconcept=0;	
						int splvaluecase=0;
						boolean breakspl=false;
						if((!dataTable.get(w).get(j).equalsIgnoreCase("?"))){
							if(dataTable.get(w).get(j).equals("-")){
								splvaluecase=w;
								breakspl=false;
								for(int row=0;row<conceptValueSets.size() && !breakspl;row++){
									for(int colm=0;colm<conceptValueSets.get(row).size() && !breakspl;colm++){
										for(int setnum=0;setnum<conceptValueSets.get(row).get(colm).size();setnum++){
											if(conceptValueSets.get(row).get(colm).contains(w)){
												splvalueconcept=colm;
												breakspl =true;
												break;
											}
										}
									}
								}
								//splvalueconcept gives me which concept my case belongs to by pointing to row number in arraylist
								for(int cn:conceptValueSets.get(0).get(splvalueconcept)){
									int casenum=0;
									float caseval=0.0f;
									casenum= cn;
									if(casenum != splvaluecase){
										caseval=Float.parseFloat(dataTable.get(casenum).get(j).toString());
										if(caseval > cut_points_in_order.get(k)){
											concept_set.add(w);
											break;
										}
									}
								}
							}
							else if(dataTable.get(w).get(j).equals("*") || Float.parseFloat(dataTable.get(w).get(j)) > cut_points_in_order.get(k)){
								concept_set.add(w);
							}
						}
					}
					conceptColumn.add(concept_set);		
				}		
				}
				attributeValuesets.add(j,conceptColumn);
				attributeStringlist.add(j,temp_string_list);
				cutpoints.add(j,cut_points_in_order);
			}
			if(!checkNumeric.get(j))
			{
			cutpoints.add(j,null);	
			ArrayList<Set<Integer>> conceptColumn= new ArrayList<Set<Integer>>();
			Set<String> temporary_list= new LinkedHashSet<String>();
				for(int k=1;k<noOfRows;k++)	{
					if(!dataTable.get(k).get(j).equals("?") && !dataTable.get(k).get(j).equals("*") && !dataTable.get(k).get(j).equals("-"))
					temporary_list.add(dataTable.get(k).get(j).toString());
				}
			ArrayList<String> temp = new ArrayList<String>();	
			int count=0; 
				for(String w:temporary_list)
					{	
						Set<Integer> concept_set = new LinkedHashSet<Integer>();
							for(int k=1;k<noOfRows;k++){
								int splvalueconcept=0;	
								int splvaluecase=0;
								boolean breakspl=false;
								if((!dataTable.get(k).get(j).equalsIgnoreCase("?"))){
									if(dataTable.get(k).get(j).equals("-")){
										splvaluecase=k;
										breakspl=false;
										for(int row=0;row<conceptValueSets.size() && !breakspl;row++){
											for(int colm=0;colm<conceptValueSets.get(row).size() && !breakspl;colm++){
												for(int setnum=0;setnum<conceptValueSets.get(row).get(colm).size();setnum++){
													if(conceptValueSets.get(row).get(colm).contains(k)){
														splvalueconcept=colm;
														breakspl =true;
														break;
													}
												}
											}
										}
										//splvalueconcept gives me which concept my case belongs to by pointing to row number in arraylist
										for(int cn:conceptValueSets.get(0).get(splvalueconcept)){
											int casenum=0;
											String caseval=null;
											casenum= cn;
											if(casenum != splvaluecase){
												caseval=dataTable.get(casenum).get(j).toString();
												if(caseval.equalsIgnoreCase(w)){
													concept_set.add(k);
													break;
												}
											}
										}
									}
									else if(dataTable.get(k).get(j).equals("*") || w.equalsIgnoreCase(dataTable.get(k).get(j)))
									concept_set.add(k);
							}
					}

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
		
		
		
			
		File newFile = new File("839output.txt");
		if (!newFile.exists()) {
			newFile.createNewFile();
			}

			 fw = new FileWriter(newFile.getAbsoluteFile());
			 bw = new BufferedWriter(fw);
		
		
		for(int i=0;i<conceptValueSets.get(0).size();i++){
			findRule(i);
		
			ScanInputData set_handler = new ScanInputData();
			Set<Integer> remaining_conditions_inrule = new LinkedHashSet <Integer>();	
			Set<Integer> remaining_conditions_inrule_temp = new LinkedHashSet <Integer>();
			int skiprows=0;
			ArrayList<Set<Integer>> covered_cases_previousrules = new ArrayList<Set<Integer>>();
		for(int j=0;j<conceptRulesetString.get(i).size();j++)
		{
			//set_handler.is_set1_subset_of_set2(current_intersection, fat_D)	
			//for(int k=0;k<conceptRulesetString.get(i).get(j).size();k++)
			//{
				//Set<Integer> covered_cases_previousrules = new LinkedHashSet <Integer>();	
				ArrayList<Integer>rule_row = new ArrayList<Integer>();
				ArrayList<Integer>rule_column = new ArrayList<Integer>();
				//Set<Integer>rule_row_set = new LinkedHashSet<Integer>();
				//Set<Integer>rule_column_set = new LinkedHashSet<Integer>();
				if(j!=0){
					skiprows=skiprows+conceptRulesetString.get(i).get(j-1).size();
				}
				int z;
				for(z=0;z<conceptRulesetString.get(i).get(j).size();z++){
					rule_row.add(z,rowsFound.get(i).get(z+skiprows));
					rule_column.add(z,columnFound.get(i).get(z+skiprows));
					//rule_row_set.add(rowsFound.get(i).get(z));
					//rule_column_set.add(columnFound.get(i).get(z));
				}
				int colno=0;
				int whichcol=0;
				do{
					//Dropping conditions
					remaining_conditions_inrule=null;
					remaining_conditions_inrule_temp=null;
					if(rule_row.size() != 1){
					rule_row.remove(whichcol);
					rule_column.remove(whichcol);
					int colm;
					for(colm=0;colm < rule_row.size()-1 || rule_row.size()==1;colm++){
						if(rule_row.size() == 1){
							remaining_conditions_inrule=attributeValuesets.get(rule_column.get(colm)).get(rule_row.get(colm));
							break;
						}
						else
							remaining_conditions_inrule_temp=set_handler.intersection(attributeValuesets.get(rule_column.get(colm)).get(rule_row.get(colm)),
									attributeValuesets.get(rule_column.get(colm+1)).get(rule_row.get(colm+1)));
						if(colm == 0)
							//remaining_conditions_inrule.addAll(remaining_conditions_inrule_temp);
							remaining_conditions_inrule = remaining_conditions_inrule_temp;
						else
							remaining_conditions_inrule=set_handler.intersection(remaining_conditions_inrule_temp,remaining_conditions_inrule);
					}
					if(set_handler.is_set1_subset_of_set2(remaining_conditions_inrule, fat_D)){
						//columnFound.get(i).remove(whichcol);
						//rowsFound.get(i).remove(whichcol);
					}
					else{
						rule_row.add(whichcol, rowsFound.get(i).get(colno+skiprows));
						rule_column.add(whichcol, columnFound.get(i).get(colno+skiprows));
						whichcol++;
					}
					}
					colno++;
				}while(colno <= conceptRulesetString.get(i).get(j).size()-1);

				Set<Integer> covered_cases_inrule = new LinkedHashSet <Integer>();	
				Set<Integer> covered_cases_inrule_temp = new LinkedHashSet <Integer>();
				for(int redun=0;redun < rule_row.size()-1 || rule_row.size()==1;redun++){
					if(rule_row.size() == 1){
						covered_cases_inrule=attributeValuesets.get(rule_column.get(redun)).get(rule_row.get(redun));
						break;
					}
					else
						covered_cases_inrule_temp=set_handler.intersection(attributeValuesets.get(rule_column.get(redun)).get(rule_row.get(redun)),
								attributeValuesets.get(rule_column.get(redun+1)).get(rule_row.get(redun+1)));
					if(redun == 0)
						covered_cases_inrule = covered_cases_inrule_temp;
					else
						covered_cases_inrule=set_handler.intersection(covered_cases_inrule_temp,covered_cases_inrule);
				}
				boolean includerule=true;
				Set<Integer> covered_cases_previousrules_union = new LinkedHashSet <Integer>();	
				//Set<Integer> covered_cases_previousrules_union_temp = new LinkedHashSet <Integer>();
				if(j!=0){
					for(int rulestocheck=0;rulestocheck < covered_cases_previousrules.size();rulestocheck++){
						if(rulestocheck == 0)
							covered_cases_previousrules_union = covered_cases_previousrules.get(rulestocheck);
						else
							covered_cases_previousrules_union=set_handler.union(covered_cases_previousrules_union, covered_cases_previousrules.get(rulestocheck));
					}
					if(set_handler.is_set1_subset_of_set2(covered_cases_inrule, covered_cases_previousrules_union))
						includerule=false;
				}
				covered_cases_previousrules.add(j, covered_cases_inrule);
				if(j==0 || includerule){
					for(int columnposition=0;columnposition<rule_row.size();columnposition++){
						bw.append("("+columnHeading.get(Integer.parseInt(rule_column.get(columnposition).toString())));
					
						bw.append(" "+attributeStringlist.get(rule_column.get(columnposition)).get(rule_row.get(columnposition))+")");
				//}
					}
					bw.append(" -> ("+columnHeading.get(columnHeading.size()-1));
					bw.append(" "+conceptStringlist.get(0).get(i)+")");
					bw.append("\n");
				}
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
	Set<Integer> incomplete_goal = new LinkedHashSet <Integer>();
	Set<Integer> current_intersecting_element = new LinkedHashSet <Integer>();	
	Set<Integer> current_intersection = new LinkedHashSet <Integer>();
	Set<Integer> could_not_find = new LinkedHashSet <Integer>();
	Set<Integer> alreadyFoundCases = new LinkedHashSet <Integer>();

	ScanInputData set_handler = new ScanInputData();

	float alpha =(float) 0.5;	
	int temp_coll=0;
	int temp_row=0;
	int rules_count=0;
	boolean found_rule=false;
	boolean atleast_one_selection=false;
	
	concept_block.addAll(conceptValueSets.get(0).get(a));
	fat_D.clear();
	fat_D.addAll(conceptValueSets.get(0).get(a));
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
	ArrayList<ArrayList<Set<Integer>>>possible_numeric_rule= new ArrayList<ArrayList<Set<Integer>>>();
	ArrayList<ArrayList<Set<Integer>>>possible_symbolic_rule= new ArrayList<ArrayList<Set<Integer>>>();
	Set<String>possible_numeric_rule_name= new HashSet<String>();
	ArrayList<String>possible_symbolic_rule_name= new ArrayList<String>();
	ArrayList<String>temp_string =new ArrayList<String>();	
	boolean alreadySelectedNumeric = false;
	int temp=0;
	int numtemp=0;
	int symtemp=0;
	do		
	{			
		found_rule=false;
		atleast_one_selection=false;
		
		current_intersection_rule.retainAll(current_goal);
			int current_max_no_of_intersection=0;
		for(int j=0;j<attributeValuesets.size();j++)           
		{							
					for(int k=0;k<attributeValuesets.get(j).size();k++)		
					{	
						alreadySelectedNumeric=false;
						if(checkNumeric.get(j) && possible_numeric_rule.size()!=0){
							for(int numsize=0;numsize<possible_numeric_rule_name.size();numsize++){
								//if(possible_numeric_rule.contains(attributeValuesets.get(j).get(k)) && possible_numeric_rule_name)
									alreadySelectedNumeric=true;
								}
							}
							else{
								for(int numsize=0;numsize<possible_numeric_rule.size();numsize++){
									//if(set_handler.is_set1_subset_of_set2(possible_numeric_rule.get(numsize),attributeValuesets.get(j).get(k))
										//	|| (set_handler.intersection(possible_numeric_rule.get(numsize),attributeValuesets.get(j).get(k)).size() == 0))
										//alreadySelectedNumeric=true;
								}
							}
						current_intersecting_element=set_handler.intersection(current_goal,attributeValuesets.get(j).get(k));   // calculate intersection of Goal and current a,v pair
						
						if(current_intersecting_element.size()>current_max_no_of_intersection && current_intersecting_element.size()!=0 && (!possible_rule.contains(attributeValuesets.get(j).get(k)) && !alreadySelectedNumeric) )       
								{  
									current_max_no_of_intersection=current_intersecting_element.size();
									temp_coll=j;
									temp_row=k; 
									atleast_one_selection=true;
							
								 }	
							else if(current_intersecting_element.size()==current_max_no_of_intersection && current_intersecting_element.size()!=0 && (!possible_rule.contains(attributeValuesets.get(j).get(k)) && !alreadySelectedNumeric))
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
		if(set_handler.is_set1_subset_of_set2(current_intersection, fat_D))
				{
			current_intersection_rule.retainAll(current_intersection);
				rule_temp_row.add(temp, temp_row);
				rule_temp_column.add(temp,temp_coll );
				possible_rule.add(temp, attributeValuesets.get(temp_coll).get(temp_row));
				temp_string.add(temp++,attributeStringlist.get(temp_coll).get(temp_row));
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
			if(checkNumeric.get(temp_coll)){
				possible_numeric_rule_name.add(columnHeading.get(temp_coll));
				//Getting index of columnname and adding to the coresponding rule set
				int indx=0;
				for(String ind : possible_numeric_rule_name){
					if(columnHeading.get(temp_coll) == ind){
						possible_numeric_rule.get(indx).add(attributeValuesets.get(temp_coll).get(temp_row));
						break;
					}
					indx++;
				}
				numtemp++;
			}
			/*else{
				possible_symbolic_rule_name.add(symtemp, columnHeading.get(temp_coll));
				possible_symbolic_rule.get(symtemp).add( attributeValuesets.get(temp_coll).get(temp_row));
				symtemp++;
			}*/
		}
		else
		{

			float currentgoalsize=(float)set_handler.intersection(concept_block,current_intersection).size();
			float currentintersectionsize=(float)current_intersection.size();
			if ((float)(currentgoalsize/currentintersectionsize) >= alpha)
	 		{
				//Modifying fat D value if conditional probability is greater than or equal to alpha
				fat_D=set_handler.union(fat_D, current_intersection);
				found_rule=true;

	 		}
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
			incomplete_goal=set_handler.subtract_setA_from_setB(alreadyFoundCases,concept_block);
			current_goal=set_handler.subtract_setA_from_setB(could_not_find,incomplete_goal);
		}	
	else
		{
			could_not_find=set_handler.union(could_not_find, current_goal);
			incomplete_goal=set_handler.subtract_setA_from_setB(could_not_find,concept_block);
			current_goal=set_handler.subtract_setA_from_setB(alreadyFoundCases,incomplete_goal);
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












