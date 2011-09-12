package edu.columbia.cs.cg.document.loaders.impl.ace2005;
import java.util.*;


public class ACERelation {

	String m_strID;
	boolean m_bExplicit;

	String m_strType;
	String m_strSType;

	ACEEntity m_arg1;
	ACEEntity m_arg2;

	LinkedList<ACERMention> m_lstMentions;

	public ACERelation()
	{
		m_lstMentions = new LinkedList();
		m_bExplicit = true;
	}

	public void addMention(ACERMention rmention)
	{
		m_lstMentions.add(rmention);
		rmention.m_relation = this;
	}

	public boolean isExplicit()
	{
		return m_bExplicit;
	}

	public String getId(){
		return m_strID;
	}

	public String getType(){
		return m_strType;
	}

	public String getSubType(){
		return m_strSType;
	}
	
	public List<ACERMention> getMentions(){
		return m_lstMentions;
	}

}
