package edu.columbia.cs.cg.document.loaders.impl.ace2005;
import java.util.*;

public class ACEEntity {

	String m_strID;
	String m_strType;
	String m_strSubType;
	boolean m_bGeneric;

	LinkedList<ACEEMention> m_lstMentions;

	// entity attributes
	HashMap<String,String> m_mapAtt;

	public ACEEntity()
	{
		m_lstMentions = new LinkedList<ACEEMention>();
		m_mapAtt = new HashMap<String,String>();
	}

	public void addMention(ACEEMention emention)
	{
		emention.m_entity = this;
		m_lstMentions.add(emention);
	}

	public String getAttribute(String strKey)
	{
		return (String) m_mapAtt.get(strKey);
	}

	public void setAttribute(String strKey, String strVal)
	{
		m_mapAtt.put(strKey, strVal);
	}

	public String getId(){
		return m_strID;
	}

	public String getType(){
		return m_strType;
	}

	public String getSubtype(){
		return m_strSubType;
	}
	
	public List<ACEEMention> getMentions(){
		return m_lstMentions;
	}
}
