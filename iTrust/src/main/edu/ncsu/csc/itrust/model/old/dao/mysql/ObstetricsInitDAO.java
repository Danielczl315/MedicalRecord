package edu.ncsu.csc.itrust.model.old.dao.mysql;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricsInitBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ObstetricsInitLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Date;

/**
 * Used for managing obstetrics initialization record related to a patient. For other
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
public class ObstetricsInitDAO
{
	private DAOFactory factory;
	private ObstetricsInitLoader obstetricsInitLoader;

	/**
	 * The typical constructor.
	 * 
	 * @param factory
	 *            The {@link DAOFactory} associated with this DAO, which is used
	 *            for obtaining SQL connections, etc.
	 */
	public ObstetricsInitDAO(DAOFactory factory)
	{
		this.factory = factory;
		this.obstetricsInitLoader = new ObstetricsInitLoader();
	}

	/**
	 * Adds an empty obstetrics initialize record to the table
	 * 
	 * @return The record ID as a long.
	 * @throws DBException
	 */
	public long addEmptyObstetricsInit() throws DBException
	{
		try (Connection conn = factory.getConnection();
			 PreparedStatement ps = conn.prepareStatement("INSERT INTO obstetricsInitRecords(recordID) VALUES(NULL)"))
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
	 * Adds an obstetrics initialize record to the table given a ObstetricsInitBean
	 * 
	 * @param  oi
	 * @throws DBException
	 * @throws ParseException 
	 */
	public long addObstetricsInit(ObstetricsInitBean oi) throws DBException
	{
		oi.setRecordID(addEmptyObstetricsInit());
		editObstetricsInit(oi);
		return oi.getRecordID();
	}

	/**
	 * Returns the patient's obstetrics initialization record
	 * information for a given patient MID
	 * 
	 * @param patientMID
	 * @return A list of ObstetricsInitBean.
	 * @throws DBException
	 */
	public List<ObstetricsInitBean> getObstetricsInitByMID(long patientMID) throws DBException
	{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM obstetricsInitRecords WHERE patientMID = ?"))
		{
			ps.setLong(1, patientMID);
			ResultSet rs = ps.executeQuery();
			List<ObstetricsInitBean> loadList = obstetricsInitLoader.loadList(rs);
			rs.close();

			return loadList;
		}
		catch (SQLException e)
		{
			throw new DBException(e);
		}
	}
	
	/**
	 * Returns the patient's obstetrics initialization record
	 * information for a given record ID
	 * 
	 * @param recordID
	 * @return ObstetricsInitBean.
	 * @throws DBException
	 */
	public ObstetricsInitBean getObstetricsInitByID(long recordID) throws DBException
	{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM obstetricsInitRecords WHERE recordID = ?"))
		{
			ps.setLong(1, recordID);
			ResultSet rs = ps.executeQuery();
			ObstetricsInitBean obstetricsInit = rs.next() ? obstetricsInitLoader.loadSingle(rs) : null;
			rs.close();

			return obstetricsInit;
		}
		catch (SQLException e)
		{
			throw new DBException(e);
		}
	}
	
	/**
	 * Returns the patient's most recent obstetrics initialization
	 * record information for a given patient MID
	 * 
	 * @param patientMID
	 * @return ObstetricsInitBean.
	 * @throws DBException
	 */
	public ObstetricsInitBean getObstetricsInitByMostRecent(long patientMID) throws DBException
	{
		List<ObstetricsInitBean> obstetricsInitList = getObstetricsInitByMID(patientMID);
		if(obstetricsInitList.size() == 0) {
			return null;
		}
		ObstetricsInitBean mostRecentRecord = obstetricsInitList.get(0);
		Date mostRecentDate = mostRecentRecord.getInitDate();
		
		for (int i = 1; i < obstetricsInitList.size(); i ++)
		{
			ObstetricsInitBean currentRecord = obstetricsInitList.get(i);
			Date currentDate = currentRecord.getInitDate();
			
			if (currentDate.after(mostRecentDate))
			{
				mostRecentRecord = currentRecord;
				mostRecentDate = currentDate;
			}
		}
		
		return mostRecentRecord;
	}

	/**
	 * Updates a patient's obstetrics initialization record
	 * information for a given ObstetricsInitBean
	 * 
	 * @param oi ObstetricsInitBean containing new information
	 * @throws DBException
	 */
	public void editObstetricsInit(ObstetricsInitBean oi) throws DBException
	{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = obstetricsInitLoader
						.loadParameters(conn.prepareStatement("UPDATE obstetricsInitRecords SET patientMID = ?, "
								+ "initDate = ?, lastMenstrualPeriod = ? WHERE recordID = ?"), oi))
		{
			ps.setLong(4, oi.getRecordID());
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			throw new DBException(e);
		}
	}
	
	/**
	 * Returns whether or not the obstetrics initialization record of the patient exists
	 * give the MID of the patient
	 * 
	 * @param patientMID The MID of the patient.
	 * @return A boolean indicating whether the obstetrics initialization record exists.
	 * @throws DBException
	 */
	public boolean checkObstetricsInitExistsByMID(long patientMID) throws DBException
	{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM obstetricsInitRecords WHERE patientMID = ?"))
		{
			ps.setLong(1, patientMID);
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
	 * Returns whether or not the obstetrics initialization record of the patient exists
	 * give the record ID of the patient
	 * 
	 * @param recordID The record ID of the patient.
	 * @return A boolean indicating whether the obstetrics initialization record exists.
	 * @throws DBException
	 */
	public boolean checkObstetricsInitExistsByID(long recordID) throws DBException
	{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM obstetricsInitRecords WHERE recordID = ?"))
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
	 * Deletes an obstetric initialization record that matches the recordID
	 * This does NOT remove any prior pregnancies.
	 *
	 * @param recordID the ID of the record to be deleted
	 * @return true if deletion was successful
	 * @throws DBException
	 */
	public boolean deleteObstetricInit(long recordID) throws DBException {
		try(
			Connection conn = factory.getConnection();
			PreparedStatement ps = conn.prepareStatement("DELETE FROM obstetricsInitRecords WHERE recordID = ?")) {
			ps.setLong(1, recordID);

			boolean success = ps.executeUpdate() == 1;
			return success;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
}