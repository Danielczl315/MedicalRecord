package edu.ncsu.csc.itrust.model.old.beans.loaders;

import edu.ncsu.csc.itrust.model.old.beans.PriorPregnancyBean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A loader for PriorPregnancyBean.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class PriorPregnancyLoader implements BeanLoader<PriorPregnancyBean>
{

	/**
	 * loadList
	 * @param rs rs
	 * @throws SQLException
	 */
	@Override
	public List<PriorPregnancyBean> loadList(ResultSet rs) throws SQLException {
		List<PriorPregnancyBean> list = new ArrayList<PriorPregnancyBean>();
		
		while (rs.next()) {
			list.add(loadSingle(rs));
		}

		return list;
	}

	private void loadCommon(ResultSet rs, PriorPregnancyBean pp) throws SQLException {
		pp.setPriorPregnancyID(rs.getLong("priorPregnancyID"));
		pp.setObstetricRecordID(rs.getLong("obstetricRecordID"));
		pp.setYearOfConception(rs.getInt("yearOfConception"));
		pp.setDaysPregnant(rs.getInt("daysPregnant"));
		pp.setHoursInLabor(rs.getFloat("hoursInLabor"));
		pp.setWeightGain(rs.getFloat("weightGain"));
		pp.setDeliveryTypeStr(rs.getString("deliveryType"));
		pp.setMultiplicity(rs.getInt("multiplicity"));
	}
	
	/**
	 * loadSingle
	 * @param rs rs
	 * @return pp
	 * @throws SQLException
	 */
	@Override
	public PriorPregnancyBean loadSingle(ResultSet rs) throws SQLException {
		PriorPregnancyBean pp = new PriorPregnancyBean();
		loadCommon(rs, pp);
		return pp;
	}

	/**
	 * loadParameters
	 * @throws SQLException
	 */
	@Override
	public PreparedStatement loadParameters(PreparedStatement ps, PriorPregnancyBean pp) throws SQLException {
		int i = 1;

		ps.setLong(i++, pp.getObstetricRecordID());
		ps.setInt(i++, pp.getYearOfConception());
		ps.setInt(i++, pp.getDaysPregnant());
		ps.setFloat(i++, pp.getHoursInLabor());
		ps.setFloat(i++, pp.getWeightGain());
		ps.setString(i++, pp.getDeliveryType().getName());
		ps.setInt(i++, pp.getMultiplicity());

		return ps;
	}
}
