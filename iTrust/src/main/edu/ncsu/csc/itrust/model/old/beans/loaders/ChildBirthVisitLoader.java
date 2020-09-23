package edu.ncsu.csc.itrust.model.old.beans.loaders;

import edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A loader for ChildBirthVisitBean.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class ChildBirthVisitLoader implements BeanLoader<ChildBirthVisitBean>
{
	/**
	 * loadList
	 * @param rs rs
	 * @throws SQLException
	 */
	@Override
	public List<ChildBirthVisitBean> loadList(ResultSet rs) throws SQLException
	{
		List<ChildBirthVisitBean> list = new ArrayList<ChildBirthVisitBean>();
		
		while (rs.next())
		{
			list.add(loadSingle(rs));
		}

		return list;
	}

	private void loadCommon(ResultSet rs, ChildBirthVisitBean cb) throws SQLException
	{
		cb.setVisitID(rs.getLong("visitID"));
		cb.setObstetricsInitRecordID(rs.getLong("obstetricsInitRecordID"));
		cb.setApptID(rs.getLong("apptID"));
		cb.setPreferredDeliveryMethodStr(rs.getString("preferredDeliveryMethod"));
		cb.setHasDeliveredStr(rs.getString("hasDelivered"));
		cb.setPitocinDosage(rs.getLong("pitocinDosage"));
		cb.setNitrousOxideDosage(rs.getLong("nitrousOxideDosage"));
		cb.setPethidineDosage(rs.getLong("pethidineDosage"));
		cb.setEpiduralAnaesthesiaDosage(rs.getLong("epiduralAnaesthesiaDosage"));
		cb.setMagnesiumSulfateDosage(rs.getLong("magnesiumSulfateDosage"));
		cb.setRhImmuneGlobulinDosage(rs.getLong("rhImmuneGlobulinDosage"));
	}
	
	/**
	 * loadSingle
	 * @param rs rs
	 * @return cb
	 * @throws SQLException
	 */
	@Override
	public ChildBirthVisitBean loadSingle(ResultSet rs) throws SQLException
	{
		ChildBirthVisitBean cb = new ChildBirthVisitBean();
		loadCommon(rs, cb);
		return cb;
	}

	/**
	 * loadParameters
	 * @throws SQLException
	 */
	@Override
	public PreparedStatement loadParameters(PreparedStatement ps, ChildBirthVisitBean cb) throws SQLException
	{
		int i = 1;
		ps.setLong(i++, cb.getObstetricsInitRecordID());
		ps.setLong(i++, cb.getApptID());
		ps.setString(i++, cb.getPreferredDeliveryMethod().getName());
		ps.setString(i++, cb.getHasDelivered().getName());
		ps.setLong(i++, cb.getPitocinDosage());
		ps.setLong(i++, cb.getPethidineDosage());
		ps.setLong(i++, cb.getNitrousOxideDosage());
		ps.setLong(i++, cb.getEpiduralAnaesthesiaDosage());
		ps.setLong(i++, cb.getMagnesiumSulfateDosage());
		ps.setLong(i++, cb.getRhImmuneGlobulinDosage());

		return ps;
	}
}
