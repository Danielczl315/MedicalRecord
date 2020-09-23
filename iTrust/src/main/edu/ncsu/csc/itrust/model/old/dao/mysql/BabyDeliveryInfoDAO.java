package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.BabyDeliveryInfoBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.BabyDeliveryInfoLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

/**
 * Used for managing baby delivery info record information related to a patient. For other
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
public class BabyDeliveryInfoDAO
{
	private DAOFactory factory;
	private BabyDeliveryInfoLoader babyDeliveryInfoLoader;

	/**
	 * The typical constructor.
	 * 
	 * @param factory
	 *            The {@link DAOFactory} associated with this DAO, which is used
	 *            for obtaining SQL connections, etc.
	 */
	public BabyDeliveryInfoDAO(DAOFactory factory)
	{
		this.factory = factory;
		this.babyDeliveryInfoLoader = new BabyDeliveryInfoLoader();
	}

	/**
	 * Adds an empty baby delivery info record to the table
	 * 
	 * @return The the record ID as a long.
	 * @throws DBException
	 */
	public long addEmptyBabyDeliveryInfo() throws DBException
	{
		try (Connection conn = factory.getConnection();
			 PreparedStatement ps = conn.prepareStatement("INSERT INTO babyDeliveryInfo(recordID) VALUES(NULL)"))
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
	 * Adds a baby delivery info record to the table given a BabyDeliveryInfoBean
	 * 
	 * @param  BabyDeliveryInfoBean. 
	 * @throws DBException
	 */
	public void addBabyDeliveryInfo(BabyDeliveryInfoBean bd) throws DBException
	{
		bd.setRecordID(addEmptyBabyDeliveryInfo());
		editBabyDeliveryInfo(bd);
	}
	
	/**
	 * Returns the patient's baby delivery info record
	 * information for a given record ID
	 * 
	 * @param recordID
	 * @return BabyDeliveryInfoBean.
	 * @throws DBException
	 */
	public BabyDeliveryInfoBean getBabyDeliveryInfoByRecordID(long recordID) throws DBException
	{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM babyDeliveryInfo WHERE recordID = ?"))
		{
			ps.setLong(1, recordID);
			ResultSet rs = ps.executeQuery();
			BabyDeliveryInfoBean babyDeliveryInfo = rs.next() ? babyDeliveryInfoLoader.loadSingle(rs) : null;
			rs.close();

			return babyDeliveryInfo;
		}
		catch (SQLException e)
		{
			throw new DBException(e);
		}
	}

	/**
	 * Returns the patient's baby delivery info record
	 * information for a given child birth visit ID
	 * 
	 * @param childBirthVisitID
	 * @return A list of BabyDeliveryInfoBean.
	 * @throws DBException
	 */
	public List<BabyDeliveryInfoBean> getBabyDeliveryInfoByVisitID(long childBirthVisitID) throws DBException
	{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM babyDeliveryInfo WHERE childBirthVisitID = ?"))
		{
			ps.setLong(1, childBirthVisitID);
			ResultSet rs = ps.executeQuery();
			List<BabyDeliveryInfoBean> loadList = babyDeliveryInfoLoader.loadList(rs);
			rs.close();

			return loadList;
		}
		catch (SQLException e)
		{
			throw new DBException(e);
		}
	}

	/**
	 * Updates a patient's baby delivery info record
	 * information for a given BabyDeliveryInfoBean
	 * 
	 * @param bd BabyDeliveryInfoBean containing new information
	 * @throws DBException
	 */
	public void editBabyDeliveryInfo(BabyDeliveryInfoBean bd) throws DBException
	{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = babyDeliveryInfoLoader
						.loadParameters(conn.prepareStatement("UPDATE babyDeliveryInfo SET patientMID = ?, childBirthVisitID = ?, "
								+ "gender = ?, dateOfBirth = ?, deliveryMethod = ?, isEstimated = ? WHERE recordID = ?"), bd))
		{
			ps.setLong(7, bd.getRecordID());
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			throw new DBException(e);
		}
	}
	
	/**
	 * Returns whether or not the baby delivery info record of the patient exists
	 * give the record ID of the patient
	 * 
	 * @param recordID The record ID of the patient.
	 * @return A boolean indicating whether the baby delivery info record exists.
	 * @throws DBException
	 */
	public boolean checkBabyDeliveryInfoExistsByRecordID(long recordID) throws DBException
	{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM babyDeliveryInfo WHERE recordID = ?"))
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
	 * Returns whether or not the baby delivery info record of the patient exists
	 * give the child birth visit ID of the patient
	 * 
	 * @param childBirthVisitID The child birth visit ID of the patient.
	 * @return A boolean indicating whether the baby delivery info record exists.
	 * @throws DBException
	 */
	public boolean checkBabyDeliveryInfExistsByVisitID(long childBirthVisitID) throws DBException
	{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM babyDeliveryInfo WHERE childBirthVisitID = ?"))
		{
			ps.setLong(1, childBirthVisitID);
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
	 * Deletes a baby delivery record that matches the recordID
	 *
	 * @param recordID the ID of the record to be deleted
	 * @return true if deletion was successful
	 * @throws DBException
	 */
	public boolean deleteBayDeliveryInfo(long recordID) throws DBException {
		try(
				Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("DELETE FROM babyDeliveryInfo WHERE recordID = ?")) {
			ps.setLong(1, recordID);

			return ps.executeUpdate() == 1;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
}
