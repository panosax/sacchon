package resource.doctor;

import exception.AuthorizationException;
import jpaUtil.JpaUtil;
import model.Patient;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import repository.DoctorRepository;
import representation.PatientRepresentation;
import resource.ResourceUtils;
import security.Shield;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class DoctorNeedConsultationPatientListResource extends ServerResource {
    private long doctorId;


    @Get("json")
    public List<PatientRepresentation> getPatientList() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_DOCTOR);
        doctorId = Long.parseLong(this.getRequest().getClientInfo().getUser().getIdentifier()); // extract id user

        EntityManager em = JpaUtil.getEntityManager();


        DoctorRepository doctorRepository = new DoctorRepository(em);
        List<Patient> patientList = doctorRepository.getNeedConsultationPatientList(this.doctorId);

        List<PatientRepresentation> patientRepresentationList = new ArrayList<>();

        for (Patient patient : patientList) {
            patientRepresentationList.add(new PatientRepresentation(patient));
        }

        em.close();

        return patientRepresentationList;
    }
}