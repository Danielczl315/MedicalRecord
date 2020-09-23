package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricsInitBean;
import edu.ncsu.csc.itrust.model.old.beans.PriorPregnancyBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.PriorPregnancyLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

/**
 * Used for managing prior pregnancy information related to a patient. For other
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
public class PriorPregnancyDAO
{
	private DAOFactory factory;
	private PriorPregnancyLoader priorPregnancyLoader;

	/**
	 * The typical constructor.
	 * 
	 * @param factory
	 *            The {@link DAOFactory} associated with this DAO, which is used
	 *            for obtaining SQL connections, etc.
	 */
	public PriorPregnancyDAO(DAOFactory factory)
	{
		this.factory = factory;
		this.priorPregnancyLoader = new PriorPregnancyLoader();

	}

	/**
	 * Adds an empty prior pregnancy record to the table
	 * 
	 * @return The prior pregnancy ID of the record as a long.
	 * @throws DBException
	 */
	public long addEmptyPriorPregnancy() throws DBException
	{
		try (Connection conn = factory.getConnection();
			 PreparedStatement ps = conn.prepareStatement("INSERT INTO priorPregnancies(priorPregnancyID) VALUES(NULL)"))
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
	 * Adds an prior pregnancy record to the table given a PriorPregnancyBean
	 * 
	 * @param  pp	a PriorPregnancy bean that contains the information to be added
	 * @throws DBException
	 */
	public void addPriorPregnancy(PriorPregnancyBean pp) throws DBException
	{
		pp.setPriorPregnancyID(addEmptyPriorPregnancy());

		editPriorPregnancy(pp);
	}

	/**
	 * Returns the patient's prior pregnancy
	 * information for a given patient MID
	 * 
	 * @param patientMID
	 * @return A list of PriorPregnancyBean.
	 * @throws DBException
	 */
	public List<PriorPregnancyBean> getPriorPregnancyByObstetricRecordID(long obstetricRecordID) throws DBException
	{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM priorPregnancies WHERE obstetricRecordID = ?"))
		{
			ps.setLong(1, obstetricRecordID);
			ResultSet rs = ps.executeQuery();
			List<PriorPregnancyBean> loadList = priorPregnancyLoader.loadList(rs);
			rs.close();

			return loadList;
		}
		catch (SQLException e)
		{
			throw new DBException(e);
		}
	}
	
	/**
	 * Returns the patient's prior pregnancies before or
	 * on the given obstetrics init. date
	 *
	 * @param records	list of all obstetric init records for the current patient
	 * @param currentDate	the current date
	 * @return A list of PriorPregnancyBean from all Obstetric Init Records before the current date
	 * @throws DBException
	 */
	public List<PriorPregnancyBean> getPriorPregnancyByObstetricRecordIDList(List<ObstetricsInitBean> records,
																			 Date currentDate) throws DBException {
		List<PriorPregnancyBean> allPriorPregnancies = new ArrayList<>();
		for (ObstetricsInitBean record: records) {
			Date recordDate = record.getLastMenstrualPeriod();
			if (recordDate.before(currentDate) || recordDate.equals(currentDate)) {
				List<PriorPregnancyBean> priorPregnancies = getPriorPregnancyByObstetricRecordID(record.getRecordID());
				allPriorPregnancies.addAll(priorPregnancies);
			}
		}

		return allPriorPregnancies;
	}
	
	/**
	 * Returns the patient's prior pregnancy
	 * information for a given prior pregnancy ID
	 * 
	 * @param priorPregnancyID
	 * @return PriorPregnancyBean.
	 * @throws DBException
	 */
	public PriorPregnancyBean getPriorPregnancyByID(long priorPregnancyID) throws DBException
	{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM priorPregnancies WHERE priorPregnancyID = ?"))
		{
			ps.setLong(1, priorPregnancyID);
			ResultSet rs = ps.executeQuery();
			PriorPregnancyBean priorPregnancy = rs.next() ? priorPregnancyLoader.loadSingle(rs) : null;
			rs.close();

			return priorPregnancy;
		}
		catch (SQLException e)
		{
			throw new DBException(e);
		}
	}

	/**
	 * Updates a patient's prior pregnancy
	 * information for a given PriorPregnancyBean
	 * 
	 * @param pp PriorPregnancyBean containing new information
	 * @throws DBException
	 */
	public void editPriorPregnancy(PriorPregnancyBean pp) throws DBException
	{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = priorPregnancyLoader
						.loadParameters(conn.prepareStatement("UPDATE priorPregnancies SET obstetricRecordID = ?, "
								+ "yearOfConception = ?, daysPregnant = ?, hoursInLabor = ?, "
								+ "weightGain = ?, deliveryType = ?, multiplicity = ? WHERE priorPregnancyID = ?"), pp))
		{
			ps.setLong(8, pp.getPriorPregnancyID());
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			throw new DBException(e);
		}
	}
	
	/**
	 * Returns whether or not the prior pregnancy record of the patient exists
	 * give the MID of the patient
	 * 
	 * @param obstetricRecordID The MID of the patient.
	 * @return A boolean indicating whether the prior pregnancy record exists.
	 * @throws DBException
	 */
	public boolean checkPriorPregnancyExistsByObstetricRecordID(long obstetricRecordID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM priorPregnancies WHERE obstetricRecordID = ?"))
		{
			ps.setLong(1, obstetricRecordID);
			ResultSet rs = ps.executeQuery();
			boolean exists = rs.next();
			rs.close();

			return exists;
		}
		catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns whether or not the prior pregnancy record of the patient exists
	 * give the prior pregnancy ID of the patient
	 * 
	 * @param priorPregnancyID The prior pregnancy ID of the patient.
	 * @return A boolean indicating whether the prior pregnancy record exists.
	 * @throws DBException
	 */
	public boolean checkPriorPregnancyExistsByID(long priorPregnancyID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM priorPregnancies WHERE priorPregnancyID = ?"))
		{
			ps.setLong(1, priorPregnancyID);
			ResultSet rs = ps.executeQuery();
			boolean exists = rs.next();
			rs.close();

			return exists;
		}
		catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Deletes a prior pregnancy record that matches the recordID
	 * This will remove from each of the obstetric init records
	 *
	 * @param recordID the ID of the record to be deleted
	 * @return true if deletion was successful
	 * @throws DBException
	 */
	public boolean deletePriorPregnancy(long recordID) throws DBException {
		try(
				Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("DELETE FROM priorPregnancies WHERE priorPregnancyID = ?")) {
			ps.setLong(1, recordID);

			boolean success = ps.executeUpdate() == 1;
			return success;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
}
