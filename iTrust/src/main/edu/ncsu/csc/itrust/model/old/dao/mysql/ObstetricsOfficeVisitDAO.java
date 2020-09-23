package edu.ncsu.csc.itrust.model.old.dao.mysql;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricsInitBean;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricsOfficeVisitBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ObstetricsOfficeVisitLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Used for managing obstetrics office visit records related to a patient. For other
 * information related to all aspects of patient care, see the other DAOs.
 * 
 * DAO stands for Database Access Object. All DAOs are intended to be
 * reflections of the database, that is, one DAO per table in the database (most
 * of the time). For more complex sets of queries, extra DAOs are added. DAOs
 * can assume that all data has been validated and is correct.
 * 
 * DAOs should never have setters or any other parameter to the constructor than
 * a factory. All DAOs should be accessed by DAOFactory (@see
 * {@link DAOFactory}) and every DAO should have a factory - for obtaining JDBC
 * connections and/or accessing other DAOs.
 */
public class ObstetricsOfficeVisitDAO {
	private DAOFactory factory;
	private ObstetricsOfficeVisitLoader obstetricsOfficeVisitLoader;
	
	/**
	 * The typical constructor.
	 * 
	 * @param factory
	 *            The {@link DAOFactory} associated with this DAO, which is used
	 *            for obtaining SQL connections, etc.
	 */
	public ObstetricsOfficeVisitDAO(DAOFactory factory)
	{
		this.factory = factory;
		this.obstetricsOfficeVisitLoader = new ObstetricsOfficeVisitLoader();
	}
	
	/**
	 * Adds an obstetrics office visit record to the database
	 * 
	 * @param oov ObstetricsOfficeVisitBean containing information about the visit
	 * @return long representing visit ID
	 * @throws DBException
	 */
	public long addObstetricsOfficeVisit(ObstetricsOfficeVisitBean oov) throws DBException {
		try (Connection conn = factory.getConnection())
		{
			PreparedStatement ps = conn.prepareStatement("INSERT INTO obstetricsOfficeVisit " +
			"(obstetricsInitRecordID, locationID, patientMID, hcpMID, apptID, visitDate, " +
			"weight, bloodPressure, fetalHeartRate, lowLyingPlacentaObserved, numberOfBabies) " +
			"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			ps = obstetricsOfficeVisitLoader.loadParameters(ps, oov);
			
			ps.executeUpdate();
			return DBUtil.getLastInsert(conn);
		}
		catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	/**
	 * Edits an existing obstetrics office visit record
	 * 
	 * @param oov ObstetricsOfficeVisitBean containing edits to be made
	 * @throws DBException
	 */
	public void editObstetricsOfficeVisit(ObstetricsOfficeVisitBean oov) throws DBException {
		try (Connection conn = factory.getConnection()) {
			PreparedStatement ps = conn.prepareStatement("UPDATE obstetricsOfficeVisit SET " +
			"obstetricsInitRecordID=?, locationID=?, patientMID=?, hcpMID=?, apptID=?, visitDate=?, " +
			"weight=?, bloodPressure=?, fetalHeartRate=?, lowLyingPlacentaObserved=?, numberOfBabies=? " +
			"WHERE visitID=?");
			
			ps = obstetricsOfficeVisitLoader.loadParameters(ps, oov);
			ps.setLong(12, oov.getVisitID());
			ps.executeUpdate();
		}
		catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	/**
	 * Returns all obstetrics office visit records associated with a given obstetrics
	 * initialization record ID
	 * 
	 * @param obstetricsInitID ID of obstetrics initialization record
	 * @return List of ObstetricOfficeVisitBeans associated with given obstetricsInitID
	 * @throws DBException
	 */
	public List<ObstetricsOfficeVisitBean> getObstetricsOfficeVisitByInitRecord(long obstetricsInitID) throws DBException {
		List<ObstetricsOfficeVisitBean> list;
		
		try (Connection conn = factory.getConnection()) {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM obstetricsOfficeVisit WHERE obstetricsInitRecordID=?");
			ps.setLong(1, obstetricsInitID);
			ResultSet rs = ps.executeQuery();
			list = obstetricsOfficeVisitLoader.loadList(rs);
			
			return list;
		}
		catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public List<ObstetricsOfficeVisitBean> sortByVisitDate(List<ObstetricsOfficeVisitBean> list) {

		Collections.sort(list, new Comparator<ObstetricsOfficeVisitBean>() {
			@Override
			public int compare(ObstetricsOfficeVisitBean o1, ObstetricsOfficeVisitBean o2) {
				Date first = o1.getVisitDate();
				Date second = o2.getVisitDate();
				if (first == null && second == null) {
					return 0;
				} else if (first == null) {
					return 1;
				} else if (second == null){
					return -1;
				}
				return second.compareTo(first);
			}
		});

		return list;
	}

	/**
	 * Returns obstetrics office visit record by its visit ID
	 * 
	 * @param visitID ID of obstetrics office visit
	 * @return ObstetricOfficeVisitBean containing information about the requested office visit
	 * @throws DBException
	 */
	public ObstetricsOfficeVisitBean getObstetricsOfficeVisitByID(long visitID) throws DBException {
		ObstetricsOfficeVisitBean oov;
		
		try (Connection conn = factory.getConnection()) {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM obstetricsOfficeVisit WHERE visitID=?");
			ps.setLong(1, visitID);
			ResultSet rs = ps.executeQuery();
			oov = rs.next() ? obstetricsOfficeVisitLoader.loadSingle(rs) : null;
			return oov;
		}
		catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	/**
	 * Returns all obstetrics office visit records associated with a given patient MID
	 * 
	 * @param patient MID
	 * @return List of ObstetricOfficeVisitBeans associated with given patient MID
	 * @throws DBException
	 */
	public List<ObstetricsOfficeVisitBean> getObstetricsOfficeVisitByMID(long patientMID) throws DBException
	{
		List<ObstetricsOfficeVisitBean> list;
		
		try (Connection conn = factory.getConnection())
		{
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM obstetricsOfficeVisit WHERE patientMID = ?");
			ps.setLong(1, patientMID);
			ResultSet rs = ps.executeQuery();
			list = obstetricsOfficeVisitLoader.loadList(rs);
			
			return list;
		}
		catch (SQLException e)
		{
			throw new DBException(e);
		}
	}
	
	/**
	 * Deletes an obstetrics office visit that matches the visitID
	 * This will remove from each of the obstetric init records
	 *
	 * @param visitID the ID of the visit to be deleted
	 * @return true if deletion was successful
	 * @throws DBException
	 */
	public boolean deleteObstetricsOfficeVisit(long visitID) throws DBException {
		try (
			Connection conn = factory.getConnection();
			PreparedStatement ps = conn.prepareStatement("DELETE FROM obstetricsOfficeVisit WHERE visitID = ?")
		) {
			ps.setLong(1, visitID);
			
			boolean success = ps.executeUpdate() == 1;
			return success;
		}
		catch (SQLException e) {
			throw new DBException(e);
		}
	}
}
