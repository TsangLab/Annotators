/*
 *  FeatureMapper.java
 *
 * The MIT License (MIT)
 * Copyright (c) 2014 Concordia University, Tsang Lab, Marie-Jean Meurs
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *  mj meurs, 04/2011
 *  from t kappler, 6/10/2006
 */

// package ipd.creole.featuremapper;
package featuremapper;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.net.URL;

import gate.*;
import gate.creole.*;

/** 
 * This class is the implementation of the resource FEATUREMAPPER.
 */
public class FeatureMapper  extends AbstractLanguageAnalyser
implements ProcessingResource, ANNIEConstants
{
	
	private AnnotationSet docAllAnnots;
	private Hashtable<String, String> foundCreatedFeatureTable = new Hashtable<String, String>(); 

	/* run-time parameters that can be modified from GATE */
	private String targetedAnnotation;
	private String mappedFeature;
	private String createdFeature;
	private URL mappingFile;
	
	/**
	 * Set the new targetedAnnotation value.
	 * @param newtargetedAnnotation the new targetedAnnotation value
	 */
	public void setTargetedAnnotation(String newtargetedAnnotation) {
		this.targetedAnnotation = newtargetedAnnotation;
	}
	/**
	 * Set the new mappedFeature value.
	 * @param newmappedFeature the new mappedFeature value
	 */
	public void setMappedFeature(String newmappedFeature) {
		this.mappedFeature = newmappedFeature;
	}
	/**
	 * Set the new createdFeature value.
	 * @param newcreatedFeature the new createdFeature value
	 */
	public void setCreatedFeature(String newcreatedFeature) {
		this.createdFeature = newcreatedFeature;
	}
	/**
	 * Set the new mappingFile value.
	 * @param newmappingFile the new mappingFile value
	 */
	public void setMappingFile(URL newmappingFile) {
		this.mappingFile = newmappingFile;
	}

	/**
	 * Get the targetedAnnotation value.
	 * @return a <code>String</code> of current targetedAnnotation value.
	 */    
	public String getTargetedAnnotation() {
		return targetedAnnotation;
	}
	/**
	 * Get the mappedFeature value.
	 * @return a <code>String</code> of current mappedFeature.
	 */    
	public String getMappedFeature() {
		return mappedFeature;
	}
	/**
	 * Get the createdFeature value.
	 * @return a <code>String</code> of current createdFeature.
	 */    
	public String getCreatedFeature() {
		return createdFeature;
	}	
	/**
	 * Get the mappingFile value.
	 * @return a <code>String</code> of current mappingFile value.
	 */    
	public URL getMappingFile() {
		return mappingFile;
	}

	public FeatureMapper() {
	}

	/**
	 * Searches in the mapping file of string A to string B returns the string B
	 * @return a <code>String</code> value
	 */
	private String searchMatch(String theStringToFind, URL fileToMine) throws IOException {

		String theOriginalMatchingString ="";
		theStringToFind = theStringToFind.toLowerCase();
		Reader fileReader = new FileReader(new File(fileToMine.getFile()));
		BufferedReader reader = new BufferedReader(fileReader);
		String line = reader.readLine();
		while (line != null) {

			if ((line.indexOf("\t") != -1) && !line.isEmpty()){

				StringTokenizer st = new StringTokenizer(line, "\t");
				String theCurrentString = st.nextToken();
				theCurrentString = theCurrentString.toLowerCase();
				if (theCurrentString.equals(theStringToFind)){
					if(theOriginalMatchingString != ""){
						String theOriginalBside = st.nextToken();
						String theBside = theOriginalBside.replace("[", "SqBrL").replace("]", "SqBrR");
						String theMatchingString = theOriginalMatchingString.replace("[", "SqBrL").replace("]", "SqBrR");
						Pattern patternBside1 = Pattern.compile(theBside);
						Pattern patternBside2 = Pattern.compile(".."+theBside);
						Pattern patternBside3 = Pattern.compile(theBside+"..");
						Pattern patternBside4 = Pattern.compile(".."+theBside+"..");
						Matcher matcherBside1 = patternBside1.matcher(theMatchingString);
						Matcher matcherBside2 = patternBside2.matcher(theMatchingString);
						Matcher matcherBside3 = patternBside3.matcher(theMatchingString);
						Matcher matcherBside4 = patternBside4.matcher(theMatchingString);
						if((!matcherBside1.matches()) && (!matcherBside2.find()) && (!matcherBside3.find()) && (!matcherBside4.find())){
							theOriginalMatchingString = theBside + " / or / " + theOriginalMatchingString;
						}
					}
					else{
						theOriginalMatchingString = st.nextToken();                   		 
					}
				}
			}
			line = reader.readLine();	      
		}

		return theOriginalMatchingString; 
	}     


	/**
	 * Inserts the contents of a plain text file with tab-separated key-value pairs into a
	 * {@link Map}.  If you don't already have a ready {@link Reader}, use the 
	 * {@link FeatureMapper#readFromFile(String, Map, String) readFromFile} wrapper.
	 *
	 * @param r an instantiated Reader for the input file
	 * @param destMap an instantiated Map for the file's content
	 * @param types the Java types of key and value in the file: "IntStr" or "StrInt"
	 * @see Reader
	 * @see Map
	 */
	public void load(Reader r, Map destMap, String types) throws IOException {
		BufferedReader reader = new BufferedReader(r);
		for (String line = reader.readLine(); line != null; line = reader.readLine() ) {
			StringTokenizer st = new StringTokenizer(line, "\t");
			if ( types.equals("IntStr") ) {
				destMap.put(Integer.valueOf(st.nextToken()), st.nextToken().toLowerCase());
			} else if ( types.equals("StrStr") ) {
				destMap.put(st.nextToken().toLowerCase().trim(), st.nextToken());
			} else { //StrInt
				destMap.put(st.nextToken().toLowerCase(), Integer.valueOf(st.nextToken()));
			}
		}
		reader.close();
	}


	/**
	 * Wrapper for {@link FeatureMapper#load(Reader, Map, String) load} instantiating
	 * the required Reader from a given file name.
	 * 
	 * @param filename the input file as String
	 * @param destMap an instantiated Map for the file's content
	 * @param types the Java types of key and value in the file: "IntStr" or "StrInt"
	 */
	public void readFromFile(String filename, Map destMap, String types) throws IOException {
		load(new FileReader(new File(filename)), destMap, types);
	}

	public void execute() throws ExecutionException {
		
		foundCreatedFeatureTable.clear();

		/* Get all the targeted annotations. */
		this.docAllAnnots = document.getAnnotations();
		AnnotationSet allTargetedAnnotationsAS = this.docAllAnnots.get(targetedAnnotation);
		if ( allTargetedAnnotationsAS == null ) {
			System.out.println("FeatureMapper: no " + createdFeature + " found in this document.");
			return;
		}

		Annotation currentTargetedAnnot;
		FeatureMap targetedAnnotFeats;
		String currentMappedFeat, currentCreatedFeat;
		for ( Iterator<Annotation> allTargetedAnnotsIt = allTargetedAnnotationsAS.iterator(); allTargetedAnnotsIt.hasNext(); ) {
			currentTargetedAnnot = allTargetedAnnotsIt.next();
			targetedAnnotFeats = currentTargetedAnnot.getFeatures();
			currentMappedFeat = (String)targetedAnnotFeats.get(mappedFeature);
			if (currentMappedFeat != null){
				currentMappedFeat = currentMappedFeat.replace("\n", "").replace("  ", " ").trim().toLowerCase();

				currentCreatedFeat = foundCreatedFeatureTable.get(currentMappedFeat);
				if (currentCreatedFeat!= null && currentCreatedFeat != "" ){
					targetedAnnotFeats.put(createdFeature, currentCreatedFeat);
				} else{

					try{
						currentCreatedFeat = searchMatch(currentMappedFeat, mappingFile);
						if (currentCreatedFeat != null && currentCreatedFeat != ""){
							targetedAnnotFeats.put(createdFeature, currentCreatedFeat);
							foundCreatedFeatureTable.put(currentMappedFeat, currentCreatedFeat);
						}
					} catch(Exception e) {
						System.out.println("Error in FeatureMapper (id): IOException " + e);
					}
				}
			}
		}
	}
} // FeatureMapper
