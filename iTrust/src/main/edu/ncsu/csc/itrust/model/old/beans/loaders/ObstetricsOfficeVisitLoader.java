package edu.ncsu.csc.itrust.model.old.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.old.beans.ObstetricsOfficeVisitBean;

/**
 * A loader for ObstetricsOfficeVisitBean.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class ObstetricsOfficeVisitLoader implements BeanLoader<ObstetricsOfficeVisitBean> {

	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
	
	/**
	 * loadList
	 * @param rs ResultSet from SQL query
	 * @return list containing ObstetricsOfficeVisitBeans
	 * @throws SQLException
	 */
	@Override
	public List<ObstetricsOfficeVisitBean> loadList(ResultSet rs) throws SQLException {
		List<ObstetricsOfficeVisitBean> list = new ArrayList<ObstetricsOfficeVisitBean>();
		
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		
		return list;
	}

	/**
	 * loadSingle
	 * @param rs ResultSet from SQL query
	 * @return an ObstetricsOfficeVisitBean containing loaded data
	 * @throws SQLException
	 */
	@Override
	public ObstetricsOfficeVisitBean loadSingle(ResultSet rs) throws SQLException {
		ObstetricsOfficeVisitBean oov = new ObstetricsOfficeVisitBean();
		loadCommon(rs, oov);
		return oov;
	}
	
	/**
	 * loadCommon
	 * @param rs ResultSet from SQL query
	 * @throws SQLException
	 */
	public void loadCommon(ResultSet rs, ObstetricsOfficeVisitBean oov) throws SQLException {
		oov.setVisitID(rs.getLong("visitID"));
		oov.setObstetricsInitRecordID(rs.getLong("obstetricsInitRecordID"));
		oov.setLocationID(rs.getLong("locationID"));
		oov.setPatientMID(rs.getLong("patientMID"));
		oov.setHcpMID(rs.getLong("hcpMID"));
		oov.setApptID(rs.getLong("apptID"));
		
		Date visitDate = rs.getDate("visitDate");
		if (visitDate != null) {
			oov.setVisitDateStr(DATE_FORMAT.format(visitDate));
		}
		
		oov.setWeight(rs.getFloat("weight"));
		oov.setBloodPressure(rs.getInt("bloodPressure"));
		oov.setFetalHeartRate(rs.getInt("fetalHeartRate"));
		oov.setLowLyingPlacentaObservedStr(rs.getString("lowLyingPlacentaObserved"));
		oov.setNumberOfBabies(rs.getInt("numberOfBabies"));
	}

	/**
	 * loadList
	 * @param rs ResultSet from SQL query
	 * @return list containing ObstetricsOfficeVisitBeans
	 * @throws SQLException
	 */
	@Override
	public PreparedStatement loadParameters(PreparedStatement ps, ObstetricsOfficeVisitBean oov) throws SQLException {
		int i = 1;
		Date date = null;
		
		ps.setLong(i++, oov.getObstetricsInitRecordID());
		ps.setLong(i++, oov.getLocationID());
		ps.setLong(i++, oov.getPatientMID());
		ps.setLong(i++, oov.getHcpMID());
		ps.setLong(i++, oov.getApptID());
		
		try {
			date = new java.sql.Date(DATE_FORMAT.parse(oov.getVisitDateStr()).getTime());
		}
		catch (ParseException e) {
			if ("".equals(oov.getVisitDateStr()))
				date = null;
		}
		catch (NullPointerException e) {
			if ("".equals(oov.getVisitDateStr()))
				date = null;
		}
		
		ps.setDate(i++, date);
		ps.setFloat(i++, oov.getWeight());
		ps.setInt(i++, oov.getBloodPressure());
		ps.setInt(i++, oov.getFetalHeartRate());
		ps.setString(i++, oov.getLowLyingPlacentaObserved().getName());
		ps.setInt(i++, oov.getNumberOfBabies());
		return ps;
	}

}
