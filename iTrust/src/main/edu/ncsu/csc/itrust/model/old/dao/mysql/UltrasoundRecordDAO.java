package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.UltrasoundRecordLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

/**
 * Used for managing ultrasound record information related to a patient. For other
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
public class UltrasoundRecordDAO
{
	private DAOFactory factory;
	private UltrasoundRecordLoader ultrasoundRecordLoader;

	/**
	 * The typical constructor.
	 * 
	 * @param factory
	 *            The {@link DAOFactory} associated with this DAO, which is used
	 *            for obtaining SQL connections, etc.
	 */
	public UltrasoundRecordDAO(DAOFactory factory)
	{
		this.factory = factory;
		this.ultrasoundRecordLoader = new UltrasoundRecordLoader();
	}

	/**
	 * Adds an empty ultrasound record to the table
	 * 
	 * @return The the record ID as a long.
	 * @throws DBException
	 */
	public long addEmptyUltrasoundRecord() throws DBException
	{
		try (Connection conn = factory.getConnection();
			 PreparedStatement ps = conn.prepareStatement("INSERT INTO ultrasoundRecords(recordID) VALUES(NULL)"))
		{
			ps.executeUpdate();
			return DBUtil.getLastInsert(conn);
		}
		catch (SQLException e)
		{
			throw new DBException(e);
		}
	}
	
	/**
	 * Adds an ultrasound record to the table given a UltrasoundRecordBean
	 * 
	 * @param  UltrasoundRecordBean. 
	 * @throws DBException
	 */
	public void addUltrasoundRecord(UltrasoundRecordBean ur) throws DBException
	{
		ur.setRecordID(addEmptyUltrasoundRecord());
		editUltrasoundRecord(ur);
	}
	
	/**
	 * Returns the patient's ultrasound record
	 * information for a given record ID
	 * 
	 * @param recordID
	 * @return UltrasoundRecordBean.
	 * @throws DBException
	 */
	public UltrasoundRecordBean getUltrasoundRecordByRecordID(long recordID) throws DBException
	{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM ultrasoundRecords WHERE recordID = ?"))
		{
			ps.setLong(1, recordID);
			ResultSet rs = ps.executeQuery();
			UltrasoundRecordBean ultrasoundRecord = rs.next() ? ultrasoundRecordLoader.loadSingle(rs) : null;
			rs.close();

			return ultrasoundRecord;
		}
		catch (SQLException e)
		{
			throw new DBException(e);
		}
	}

	/**
	 * Returns the patient's ultrasound record
	 * information for a given obstetrics office visit ID
	 * 
	 * @param obstetricsOfficeVisitID
	 * @return A list of UltrasoundRecordBean.
	 * @throws DBException
	 */
	public List<UltrasoundRecordBean> getUltrasoundRecordByVisitID(long obstetricsOfficeVisitID) throws DBException
	{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM ultrasoundRecords WHERE obstetricsOfficeVisitID = ?"))
		{
			ps.setLong(1, obstetricsOfficeVisitID);
			ResultSet rs = ps.executeQuery();
			List<UltrasoundRecordBean> loadList = ultrasoundRecordLoader.loadList(rs);
			rs.close();

			return loadList;
		}
		catch (SQLException e)
		{
			throw new DBException(e);
		}
	}

	/**
	 * Updates a patient's ultrasound record
	 * information for a given UltrasoundRecordBean
	 * 
	 * @param ur UltrasoundRecordBean containing new information
	 * @throws DBException
	 */
	public void editUltrasoundRecord(UltrasoundRecordBean ur) throws DBException
	{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = ultrasoundRecordLoader
						.loadParameters(conn.prepareStatement("UPDATE ultrasoundRecords SET obstetricsOfficeVisitID = ?, "
								+ "crownRumpLength = ?, biparietalDiameter = ?, headCircumference = ?, femurLength = ?, "
								+ "occipitofrontalDiameter = ?, abdominalCircumference = ?, humerusLength = ?,"
								+ "estimatedFetalWeight = ?, fileURL = ? WHERE recordID = ?"), ur))
		{
			ps.setLong(11, ur.getRecordID());
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			throw new DBException(e);
		}
	}
	
	/**
	 * Returns whether or not the ultrasound record of the patient exists
	 * give the record ID of the patient
	 * 
	 * @param recordID The record ID of the patient.
	 * @return A boolean indicating whether the ultrasound record exists.
	 * @throws DBException
	 */
	public boolean checkUltrasoundRecordExistsByRecordID(long recordID) throws DBException
	{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM ultrasoundRecords WHERE recordID = ?"))
		{
			ps.setLong(1, recordID);
			ResultSet rs = ps.executeQuery();
			boolean exists = rs.next();
			rs.close();

			return exists;
		}
		catch (SQLException e)
		{
			throw new DBException(e);
		}
	}
	
	/**
	 * Returns whether or not the ultrasound record of the patient exists
	 * give the obstetrics office visit ID of the patient
	 * 
	 * @param obstetricsOfficeVisitID The obstetrics office visit ID of the patient.
	 * @return A boolean indicating whether the ultrasound record exists.
	 * @throws DBException
	 */
	public boolean checkUltrasoundRecordExistsByVisitID(long obstetricsOfficeVisitID) throws DBException
	{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM ultrasoundRecords WHERE obstetricsOfficeVisitID = ?"))
		{
			ps.setLong(1, obstetricsOfficeVisitID);
			ResultSet rs = ps.executeQuery();
			boolean exists = rs.next();
			rs.close();

			return exists;
		}
		catch (SQLException e)
		{
			throw new DBException(e);
		}
	}
	
	/**
	 * Deletes an ultrasound record that matches the recordID
	 * This will remove from each of the obstetric office visit records
	 *
	 * @param recordID the ID of the record to be deleted
	 * @return true if deletion was successful
	 * @throws DBException
	 */
	public boolean deleteUltrasoundRecord(long recordID) throws DBException {
		try (
			Connection conn = factory.getConnection();
			PreparedStatement ps = conn.prepareStatement("DELETE FROM ultrasoundRecords WHERE recordID = ?")
		) {
			ps.setLong(1, recordID);
			
			boolean success = ps.executeUpdate() == 1;
			return success;
		}
		catch (SQLException e) {
			throw new DBException(e);
		}
	}
}
