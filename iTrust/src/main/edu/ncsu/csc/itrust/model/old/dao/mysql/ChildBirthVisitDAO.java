package edu.ncsu.csc.itrust.model.old.dao.mysql;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ChildBirthVisitBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ChildBirthVisitLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Used for managing child birth visit record information related to a patient. For other
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
public class ChildBirthVisitDAO
{
	private DAOFactory factory;
	private ChildBirthVisitLoader childBirthVisitLoader;

	/**
	 * The typical constructor.
	 * 
	 * @param factory
	 *            The {@link DAOFactory} associated with this DAO, which is used
	 *            for obtaining SQL connections, etc.
	 */
	public ChildBirthVisitDAO(DAOFactory factory)
	{
		this.factory = factory;
		this.childBirthVisitLoader = new ChildBirthVisitLoader();
	}
	
	/**
	 * Returns next visit ID in the database
	 * 
	 * @return The visit ID as a long.
	 * @throws DBException
	 */
	public long getNextVisitID() throws DBException
	{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT `AUTO_INCREMENT` FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'itrust' AND TABLE_NAME = 'childBirthVisit'"))
		{
			ResultSet rs = ps.executeQuery();
			long nextVisitID = rs.next() ? rs.getLong("AUTO_INCREMENT") : null;
			rs.close();

			return nextVisitID;
		}
		catch (SQLException e)
		{
			throw new DBException(e);
		}
	}

	/**
	 * Adds an empty child birth visit record to the table
	 * 
	 * @return The visit ID as a long.
	 * @throws DBException
	 */
	public long addEmptyChildBirthVisit() throws DBException
	{
		try (Connection conn = factory.getConnection();
			 PreparedStatement ps = conn.prepareStatement("INSERT INTO childBirthVisit(visitID) VALUES(NULL)"))
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
	 * Adds a child birth visit record to the table given a ChildBirthVisitBean
	 * 
	 * @param  cb the data to add to the table
	 * @throws DBException
	 */
	public void addChildBirthVisit(ChildBirthVisitBean cb) throws DBException
	{
		cb.setVisitID(addEmptyChildBirthVisit());
		editChildBirthVisit(cb);
	}
	
	/**
	 * Returns the patient's child birth visit record
	 * information for a given visit ID
	 * 
	 * @param visitID ID of visit to retrieve
	 * @return ChildBirthVisitBean.
	 * @throws DBException
	 */
	public ChildBirthVisitBean getChildBirthVisitByVisitID(long visitID) throws DBException
	{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM childBirthVisit WHERE visitID = ?"))
		{
			ps.setLong(1, visitID);
			ResultSet rs = ps.executeQuery();
			ChildBirthVisitBean childBirthVisit = rs.next() ? childBirthVisitLoader.loadSingle(rs) : null;
			rs.close();

			return childBirthVisit;
		}
		catch (SQLException e)
		{
			throw new DBException(e);
		}
	}

	/**
	 * Returns the patient's child birth visit record
	 * information for a given obstetrics initialization record ID
	 * 
	 * @param obstetricsInitRecordID
	 * @return A list of ChildBirthVisitBean.
	 * @throws DBException
	 */
	public List<ChildBirthVisitBean> getChildBirthVisitByInitID(long obstetricsInitRecordID) throws DBException
	{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM childBirthVisit WHERE obstetricsInitRecordID = ?"))
		{
			ps.setLong(1, obstetricsInitRecordID);
			ResultSet rs = ps.executeQuery();
			List<ChildBirthVisitBean> loadList = childBirthVisitLoader.loadList(rs);
			rs.close();

			return loadList;
		}
		catch (SQLException e)
		{
			throw new DBException(e);
		}
	}

	/**
	 * Updates a patient's child birth visit record
	 * information for a given ChildBirthVisitBean
	 * 
	 * @param cb ChildBirthVisitBean containing new information
	 * @throws DBException
	 */
	public void editChildBirthVisit(ChildBirthVisitBean cb) throws DBException
	{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = childBirthVisitLoader
						.loadParameters(conn.prepareStatement("UPDATE childBirthVisit SET obstetricsInitRecordID = ?, "
								+ "apptID = ?, preferredDeliveryMethod = ?, hasDelivered = ?, pitocinDosage = ?, "
								+ "pethidineDosage = ?, nitrousOxideDosage = ?, epiduralAnaesthesiaDosage = ?, magnesiumSulfateDosage = ?,"
								+ "rhImmuneGlobulinDosage = ? WHERE visitID = ?"), cb))
		{
			ps.setLong(11, cb.getVisitID());
			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			throw new DBException(e);
		}
	}
	
	/**
	 * Returns whether or not the child birth visit record of the patient exists
	 * give the visit ID of the patient
	 * 
	 * @param visitID The visit ID of the patient.
	 * @return A boolean indicating whether the child birth visit record exists.
	 * @throws DBException
	 */
	public boolean checkChildBirthVisitExistsByVisitID(long visitID) throws DBException
	{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM childBirthVisit WHERE visitID = ?"))
		{
			ps.setLong(1, visitID);
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
	 * Returns whether or not the child birth visit record of the patient exists
	 * give the obstetrics initialization record ID of the patient
	 * 
	 * @param obstetricsInitRecordID The obstetrics initialization record ID of the patient.
	 * @return A boolean indicating whether the child birth visit record exists.
	 * @throws DBException
	 */
	public boolean checkChildBirthVisitExistsByInitID(long obstetricsInitRecordID) throws DBException
	{
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM childBirthVisit WHERE obstetricsInitRecordID = ?"))
		{
			ps.setLong(1, obstetricsInitRecordID);
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
}
