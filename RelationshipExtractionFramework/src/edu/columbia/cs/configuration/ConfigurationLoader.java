package edu.columbia.cs.configuration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.sf.saxon.dom.DOMNodeList;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.columbia.cs.cg.relations.Relationship;
import edu.columbia.cs.cg.relations.RelationshipType;
import edu.columbia.cs.cg.relations.constraints.relations.RelationshipConstraint;
import edu.columbia.cs.cg.relations.constraints.roles.RoleConstraint;
import edu.columbia.cs.cg.sentence.SentenceSplitter;

public class ConfigurationLoader {

	private class ComplexStructureLoader <T>{

		private String structureName;
		private String simpleTag;
		private String simpleName;
		private String compositeTag;
		private String compositeName;
		private String parameterNameTag;
		private T[] array;
		

		private ComplexStructureLoader(String structureName, String simpleTag, String simpleName, String compositeTag, String compositeName, String parameterNameTag, T[] array){

			this.array = array;
			this.structureName = structureName;
			this.simpleTag = simpleTag;
			this.simpleName = simpleName;
			this.compositeTag = compositeTag;
			this.compositeName = compositeName;
			this.parameterNameTag = parameterNameTag;

		}

		private T getComplexStructure(Node node) {

			if (node == null)
				return null;
			
			return generateComplexStructure(node);

		}

		private T generateComplexStructure(Node node) {

			if (isComposite(node)){

				String compositeClassName = node.getAttributes().getNamedItem(compositeName).getNodeValue(); 

				try {
					
					XPath xpath = XPathFactory.newInstance().newXPath();
					
					XPathExpression expression = xpath .compile("./"+structureName+"/*");
					
					DOMNodeList components = (DOMNodeList)expression.evaluate(node, XPathConstants.NODESET);
				
					//iterate over RoleConstraints

					List<T> str = new ArrayList<T>();

					for (int i = 0; i < components.getLength(); i++) {
						
						str.add(generateComplexStructure(components.item(i)));
					
					}
					
					T[] structureArray = str.toArray(array);
					
					Class<?> params = structureArray.getClass();
					
					Constructor<?> constr = Class.forName(compositeClassName).getConstructor(array.getClass());

					return (T)constr.newInstance((Object)structureArray);
					
				} catch (XPathExpressionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
								
			} else {

				DOMNodeList parametersList = createParameterList(node.getChildNodes(),parameterNameTag);
				
				String[][] parameters = loadParameters(parametersList);
				
				return (T)loadObject(node.getAttributes().getNamedItem(simpleName).getNodeValue(), parameters);

			}
			
			return null;
		}

		private DOMNodeList createParameterList(NodeList childNodes,
				String element) {
			
			List<Node> extent = new ArrayList<Node>();
			
			for (int i = 0; i < childNodes.getLength(); i++) {
				
				if (element.equals(childNodes.item(i).getNodeName())){
					
					extent.add(childNodes.item(i));
					
				}
				
			}
			
			DOMNodeList dnl = new DOMNodeList(extent);
		
			return dnl;
		}

		private boolean isComposite(Node node) {
			
			return compositeTag.equals(node.getNodeName());
			
		}


	}

	
	private static final String STRING_CLASS = "java.lang.String";
	private static final String RC_SIMPLE_NAME = "RoleConstraintName";
	private static final String RC_STRUCTURE_NAME = "RoleConstraint";
	private static final String RC_COMPLEX_NAME = "CombinationType";
	private static final String RC_SIMPLE_TAG = "SimpleRoleConstraint";
	private static final String RC_COMPLEX_TAG = "CompositeRoleConstraint";
	private static final String RC_PARAMETER_TAG = "Parameter";
	private static final String RELC_STRUCTURE_NAME = "RelationshipConstraint";
	private static final String RELC_SIMPLE_TAG = "SimpleRelationshipConstraint";
	private static final String RELC_COMPLEX_TAG = "CompositeRelationshipConstraint";
	private static final String RELC_COMPLEX_NAME = "CombinationType";
	private static final String RELC_SIMPLE_NAME = "RelationshipConstraintName";
	private static final String RELC_PARAMETER_TAG = "Parameter";
	
	private String xmlFile;
	private SentenceSplitter sentenceSplitter;
	private RelationshipType relationshipType;

	public ConfigurationLoader(String xmlFile){
		this.xmlFile = xmlFile;
		load();
	}

	private boolean load(){

		try {

	        InputSource is = new InputSource(new File(xmlFile).toURL().toString());

			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

			DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();

			Document xmlDoc = documentBuilder.parse(is);

			XPath xpath = XPathFactory.newInstance().newXPath(); 

			createSentenceSplitter(xpath,xmlDoc);
			
			createRelationshipType(xpath,xmlDoc);
			
			return true;	

		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}

	private void createRelationshipType(XPath xpath, Document xmlDoc) {
		
		XPathExpression expression;
		try {
			expression = xpath.compile("//Relationship/@Type");
			
			Object result = expression.evaluate(xmlDoc, XPathConstants.NODE);
			
			Attr node = (Attr) result;
			
			String type = node.getValue();
			
			expression = xpath.compile("//Relationship/Role/@Role");
			
			result = expression.evaluate(xmlDoc, XPathConstants.NODESET);
			
			DOMNodeList roleNames = (DOMNodeList) result;
			
			String[] roles = loadRoles(roleNames);

			relationshipType = new RelationshipType(type, roles);

			for (int i = 0; i < roles.length; i++) {

				expression = xpath.compile("//Relationship/Role[@Role='"+roles[i]+"']/RoleConstraint/*");
				
				Node roleConstraint = (Node)expression.evaluate(xmlDoc, XPathConstants.NODE);
				
				RoleConstraint constraint = new ComplexStructureLoader<RoleConstraint>(RC_STRUCTURE_NAME,RC_SIMPLE_TAG,RC_SIMPLE_NAME,RC_COMPLEX_TAG,RC_COMPLEX_NAME,RC_PARAMETER_TAG,new RoleConstraint[0]).getComplexStructure(roleConstraint);

				if (constraint != null)

					relationshipType.setConstraints(constraint, roles[i]);

			}

			expression = xpath.compile("//Relationship/RelationshipConstraint/*");
			
			Node relationshipConstraint = (Node)expression.evaluate(xmlDoc, XPathConstants.NODE);
			
			RelationshipConstraint rc = new ComplexStructureLoader<RelationshipConstraint>(RELC_STRUCTURE_NAME,RELC_SIMPLE_TAG,RELC_SIMPLE_NAME,RELC_COMPLEX_TAG,RELC_COMPLEX_NAME,RELC_PARAMETER_TAG,new RelationshipConstraint[0]).getComplexStructure(relationshipConstraint);

			if (rc != null)
				relationshipType.setConstraints(rc);

			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	private void createSentenceSplitter(XPath xpath, Document xmlDoc) {
		
		XPathExpression expression;
		try {
			expression = xpath.compile("//SentenceSplitter/@path");
			
			Object result = expression.evaluate(xmlDoc, XPathConstants.NODE);
			
			Attr node = (Attr) result; // works

			String className = node.getValue();
			
			XPathExpression expressionParamters = xpath.compile("//SentenceSplitter/Parameter");
			
			Object resultParameters = expressionParamters.evaluate(xmlDoc,XPathConstants.NODESET);
			
			DOMNodeList nodeParameters = (DOMNodeList)resultParameters;
			
			loadSentenceSplitter(className,loadParameters(nodeParameters));
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	private String[][] loadParameters(DOMNodeList nodeParameters) {
		
		String[][] parameters = new String[nodeParameters.getLength()][2];
		
		for (int i = 0; i < nodeParameters.getLength(); i++) {
			
//			System.out.println(nodeParameters.item(i).getAttributes().getNamedItem("Name").getNodeValue());

			parameters[i][0] = nodeParameters.item(i).getAttributes().getNamedItem("Type").getNodeValue();
			parameters[i][1] = nodeParameters.item(i).getAttributes().getNamedItem("Value").getNodeValue();

		}

		return parameters;
	}

	private String[] loadRoles(DOMNodeList roleNames) {
		
		String[] roles = new String[roleNames.getLength()];
		
		for (int i = 0; i < roles.length; i++) {
			
			roles[i] = roleNames.item(i).getNodeValue();
			
		}
		
		return roles;
		
	}

	private void loadSentenceSplitter(String className, String[][] parameters) {

		sentenceSplitter = (SentenceSplitter)loadObject(className,parameters);

	}

	private Object loadObject(String className, String[][] parameters) {

		try {

			Class<?> stringClass = Class.forName(STRING_CLASS);

			Class<?> c = Class.forName(className);

			if (parameters.length == 0){
				
				Constructor<?> constr = c.getConstructor(null);
				
				return constr.newInstance(null);
				
			}
			
			Class<?>[] parameterTypes = new Class<?>[parameters.length];

			Object[] parameterValues = new Object[parameters.length];

			for (int i = 0; i < parameters.length; i++) {

				parameterTypes[i] = Class.forName(parameters[i][0]);

				parameterValues[i] = parameterTypes[i].getConstructor(stringClass).newInstance(parameters[i][1]);

			}

			Constructor<?> constr = c.getConstructor(parameterTypes);

			return constr.newInstance(parameterValues);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return null;

	}

	public SentenceSplitter getSentenceSplitter(){
		return sentenceSplitter;
	}

	public static void main(String[] args) {

		ConfigurationLoader c = new ConfigurationLoader("Configuration.xml");

		SentenceSplitter s = c.getSentenceSplitter();

		System.out.println(s.toString());
		
		RelationshipType relationshipType = c.getRelationshipType();
		
		System.out.println(relationshipType.toString());

	}

	private RelationshipType getRelationshipType() {
		return relationshipType;
	}

}
