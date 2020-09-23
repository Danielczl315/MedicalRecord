package edu.ncsu.csc.itrust.model.old.beans.loaders;

import edu.ncsu.csc.itrust.model.old.beans.ObstetricsInitBean;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A loader for ObstetricsInitBean.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class ObstetricsInitLoader implements BeanLoader<ObstetricsInitBean>
{
	/**
	 * loadList
	 * @param rs rs
	 * @throws SQLException
	 */
	@Override
	public List<ObstetricsInitBean> loadList(ResultSet rs) throws SQLException
	{
		List<ObstetricsInitBean> list = new ArrayList<ObstetricsInitBean>();
		
		while (rs.next())
		{
			list.add(loadSingle(rs));
		}

		return list;
	}

	private void loadCommon(ResultSet rs, ObstetricsInitBean oi) throws SQLException
	{
		oi.setRecordID(rs.getLong("recordID"));
		oi.setPatientMID(rs.getLong("patientMID"));
		oi.setInitDate(rs.getTimestamp("initDate"));
		oi.setLastMenstrualPeriod(rs.getTimestamp("lastMenstrualPeriod"));

	}
	
	/**
	 * loadSingle
	 * @param rs rs
	 * @return oi
	 * @throws SQLException
	 */
	@Override
	public ObstetricsInitBean loadSingle(ResultSet rs) throws SQLException
	{
		ObstetricsInitBean oi = new ObstetricsInitBean();
		loadCommon(rs, oi);
		return oi;
	}

	/**
	 * loadParameters
	 * @throws SQLException
	 */
	@Override
	public PreparedStatement loadParameters(PreparedStatement ps, ObstetricsInitBean oi) throws SQLException
	{
		int i = 1;
		Date date;

		ps.setLong(i++, oi.getPatientMID());

		date = new java.sql.Date(oi.getInitDate().getTime());
		ps.setDate(i++, date);

		date = new java.sql.Date(oi.getLastMenstrualPeriod().getTime());
		ps.setDate(i++, date);

		return ps;
	}
}
