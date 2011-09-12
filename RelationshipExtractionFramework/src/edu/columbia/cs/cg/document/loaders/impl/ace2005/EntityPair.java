package edu.columbia.cs.cg.document.loaders.impl.ace2005;

public class EntityPair {

  static public class Labels {
    public static final int NONE = 0;
    public static final int SOC_SYM = 1;
  }

  String m_strID;

  // index of left entity
  int m_nLeft;
  // index of right entity 
  int m_nRight;
  
  int m_nTrueLabel;
  int m_nInfLabel;

  // confidence for inferred label
  double m_conf;


  public EntityPair(String strID, int nLeft, int nRight)
  {
    m_strID = m_strID;

    m_nLeft = nLeft;
    m_nRight = nRight;

    m_nTrueLabel = m_nInfLabel = -1;
    m_conf = 0.0;
  }


}
