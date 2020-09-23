package edu.ncsu.csc.itrust.model.old.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;

/**
 * A loader for UltrasoundRecordBean.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class UltrasoundRecordLoader implements BeanLoader<UltrasoundRecordBean>
{
	/**
	 * loadList
	 * @param rs rs
	 * @throws SQLException
	 */
	@Override
	public List<UltrasoundRecordBean> loadList(ResultSet rs) throws SQLException
	{
		List<UltrasoundRecordBean> list = new ArrayList<UltrasoundRecordBean>();
		
		while (rs.next())
		{
			list.add(loadSingle(rs));
		}

		return list;
	}

	private void loadCommon(ResultSet rs, UltrasoundRecordBean ur) throws SQLException
	{
		ur.setRecordID(rs.getLong("recordID"));
		ur.setObstetricsOfficeVisitID(rs.getLong("obstetricsOfficeVisitID"));
		ur.setCrownRumpLength(rs.getFloat("crownRumpLength"));
		ur.setBiparietalDiameter(rs.getFloat("biparietalDiameter"));
		ur.setHeadCircumference(rs.getFloat("headCircumference"));
		ur.setFemurLength(rs.getFloat("femurLength"));
		ur.setOccipitofrontalDiameter(rs.getFloat("occipitofrontalDiameter"));
		ur.setAbdominalCircumference(rs.getFloat("abdominalCircumference"));
		ur.setHumerusLength(rs.getFloat("humerusLength"));
		ur.setEstimatedFetalWeight(rs.getFloat("estimatedFetalWeight"));
		ur.setFileURL(rs.getString("fileURL"));
	}
	
	/**
	 * loadSingle
	 * @param rs rs
	 * @return ur
	 * @throws SQLException
	 */
	@Override
	public UltrasoundRecordBean loadSingle(ResultSet rs) throws SQLException
	{
		UltrasoundRecordBean ur = new UltrasoundRecordBean();
		loadCommon(rs, ur);
		return ur;
	}

	/**
	 * loadParameters
	 * @throws SQLException
	 */
	@Override
	public PreparedStatement loadParameters(PreparedStatement ps, UltrasoundRecordBean ur) throws SQLException
	{
		int i = 1;
		ps.setLong(i++, ur.getObstetricsOfficeVisitID());
		ps.setFloat(i++, ur.getCrownRumpLength());
		ps.setFloat(i++, ur.getBiparietalDiameter());
		ps.setFloat(i++, ur.getHeadCircumference());
		ps.setFloat(i++, ur.getFemurLength());
		ps.setFloat(i++, ur.getOccipitofrontalDiameter());
		ps.setFloat(i++, ur.getAbdominalCircumference());
		ps.setFloat(i++, ur.getHumerusLength());
		ps.setFloat(i++, ur.getEstimatedFetalWeight());
		ps.setString(i++, ur.getFileURL());

		return ps;
	}
}
