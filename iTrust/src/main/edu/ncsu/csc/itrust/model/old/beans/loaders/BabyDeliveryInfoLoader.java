package edu.ncsu.csc.itrust.model.old.beans.loaders;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.old.beans.BabyDeliveryInfoBean;

/**
 * A loader for BabyDeliveryInfoBean.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class BabyDeliveryInfoLoader implements BeanLoader<BabyDeliveryInfoBean>
{
	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

	/**
	 * loadList
	 * @param rs rs
	 * @throws SQLException
	 */
	@Override
	public List<BabyDeliveryInfoBean> loadList(ResultSet rs) throws SQLException
	{
		List<BabyDeliveryInfoBean> list = new ArrayList<BabyDeliveryInfoBean>();
		
		while (rs.next())
		{
			list.add(loadSingle(rs));
		}

		return list;
	}

	private void loadCommon(ResultSet rs, BabyDeliveryInfoBean bd) throws SQLException
	{
		bd.setRecordID(rs.getLong("recordID"));
		bd.setChildBirthVisitID(rs.getLong("childBirthVisitID"));
		bd.setPatientMID(rs.getLong("patientMID"));
		bd.setGenderStr(rs.getString("gender"));
		Date dateOfBirth = rs.getDate("dateOfBirth");

		if (dateOfBirth != null)
		{
			bd.setDateOfBirthStr(DATE_FORMAT.format(dateOfBirth));
		}
		
		bd.setDeliveryMethodStr(rs.getString("deliveryMethod"));
		bd.setIsEstimatedStr(rs.getString("isEstimated"));
		bd.setPatientMID(rs.getLong("patientMID"));
	}
	
	/**
	 * loadSingle
	 * @param rs rs
	 * @return bd
	 * @throws SQLException
	 */
	@Override
	public BabyDeliveryInfoBean loadSingle(ResultSet rs) throws SQLException
	{
		BabyDeliveryInfoBean bd = new BabyDeliveryInfoBean();
		loadCommon(rs, bd);
		return bd;
	}

	/**
	 * loadParameters
	 * @throws SQLException
	 */
	@Override
	public PreparedStatement loadParameters(PreparedStatement ps, BabyDeliveryInfoBean bd) throws SQLException
	{
		int i = 1;
		Date date = null;
		ps.setLong(i++, bd.getPatientMID());
		ps.setLong(i++, bd.getChildBirthVisitID());

		ps.setString(i++, bd.getGender().getName());

		date = new java.sql.Date(bd.getDateOfBirth().getTime());


		ps.setDate(i++, date);
		ps.setString(i++, bd.getDeliveryMethod().getName());
		ps.setString(i++, bd.getIsEstimated().getName());
		ps.setLong(i++, bd.getPatientMID());

		return ps;
	}
}
