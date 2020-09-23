package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.beans.*;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.*;

import java.util.ArrayList;
import java.util.List;

public class AddBabyInfoAction {
    private PersonnelDAO personnelDAO;
    private ChildBirthVisitDAO cbvDAO;
    private BabyDeliveryInfoDAO bdiDAO;
    private PatientDAO patientDAO;
    private ObstetricsInitDAO obstetricsInitDAO;

    private long loggedInMID;
    /**
     * AddBabyInfoAction is the constructor for this action class. It simply initializes the
     * instance variables.
     * @param factory The facory used to get the patientDAO.
     * @param loggedInMID The MID of the logged in HCP.
     * @param motherMID The MID of mother
     * @throws ITrustException When the user is not with a specialization of “OB/GYN”
     */
    public AddBabyInfoAction(DAOFactory factory, long loggedInMID) throws ITrustException {
        this.personnelDAO = factory.getPersonnelDAO();
        checkEligibility(personnelDAO, loggedInMID);
        this.cbvDAO = factory.getChildBirthVisitDAO();
        this.bdiDAO = factory.getBabyDeliveryInfoDAO();
        this.patientDAO = factory.getPatientDAO();
        this.obstetricsInitDAO = factory.getObstetricsInitDAO();

        this.loggedInMID = loggedInMID;
    }

    /**
     * Check if the HCP is with a specialization of “OB/GYN” or not
     *
     * @param personnelDAO
     * @param loggedInMID
     * @throws ITrustException
     */
    private void checkEligibility(PersonnelDAO personnelDAO, long loggedInMID) throws ITrustException{
        PersonnelBean personnel = personnelDAO.getPersonnel(loggedInMID);
        String specialty = personnel.getSpecialty();
        if(!specialty.equals("OB/GYN")){
            throw new ITrustException("User is not with a specialization of “OB/GYN”");
        }
    }

    public List<PatientBean> getBabiesBymotherMID(long motherMID) throws DBException{
        ObstetricsInitBean o = obstetricsInitDAO.getObstetricsInitByMostRecent(motherMID);
        if (o == null) {
            return new ArrayList<>();
        }

        long obstetricsInitID = o.getRecordID();
        List<ChildBirthVisitBean> CBVlist = cbvDAO.getChildBirthVisitByInitID(obstetricsInitID);
        List<Long> visitedIDlist = new ArrayList<>();
        for (int i = 0; i < CBVlist.size(); i ++)
        {
            ChildBirthVisitBean currentRecord = CBVlist.get(i);
            long visitedID = currentRecord.getVisitID();
            visitedIDlist.add(visitedID);
        }

        List<BabyDeliveryInfoBean> babyDeliveryInfoBeanList = new ArrayList<>();
        for (int i = 0; i <visitedIDlist.size(); i ++)
        {
            long visitedID = visitedIDlist.get(i);
            List<BabyDeliveryInfoBean> blist = bdiDAO.getBabyDeliveryInfoByVisitID(visitedID);
            for (int j = 0; j < blist.size(); j++){
                BabyDeliveryInfoBean bdiBean = blist.get(j);
                babyDeliveryInfoBeanList.add(bdiBean);
            }
        }
        List<PatientBean> babyList = new ArrayList<>();
        for (int i = 0; i <babyDeliveryInfoBeanList.size(); i ++) {
            BabyDeliveryInfoBean babyDeliveryInfo = babyDeliveryInfoBeanList.get(i);
            long babyMID = babyDeliveryInfo.getPatientMID();
            babyList.add(patientDAO.getPatient(babyMID));
        }
        return babyList;
    }

    public void updateBabyInfo(long babyMID, long motherMID, String fstName, String lstName, String fatherMID) throws DBException{
        PatientBean baby = patientDAO.getPatient(babyMID);
        PatientBean mother = patientDAO.getPatient(motherMID);
        baby.setFirstName(fstName);
        baby.setLastName(lstName);
        baby.setFatherMID(fatherMID);
        baby.setStreetAddress1(mother.getStreetAddress1());
        baby.setStreetAddress2(mother.getStreetAddress2());
        baby.setCity(mother.getCity());
        baby.setState(mother.getState());
        baby.setZip(mother.getZip());
        patientDAO.editPatient(baby,this.loggedInMID);

    }
}