package resource.doctor;

import exception.AuthorizationException;
import jpaUtil.JpaUtil;
import model.Patient;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import repository.DoctorRepository;
import repository.PatientRepository;
import representation.PatientRepresentation;
import resource.ResourceUtils;
import security.Shield;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class DoctorPatientListResource extends ServerResource {
    private long doctorId;



    @Get("json")
    public List<PatientRepresentation> getPatientList() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_DOCTOR);
        doctorId = Long.parseLong(this.getRequest().getClientInfo().getUser().getIdentifier()); // extract id user
        EntityManager em = JpaUtil.getEntityManager();
        DoctorRepository doctorRepository = new DoctorRepository(em);
        List<Patient> patientList = doctorRepository.getPatientList(this.doctorId);
        List<PatientRepresentation> patientRepresentationList = new ArrayList<>();

        for (Patient patient : patientList) {
            patientRepresentationList.add(new PatientRepresentation(patient));
        }

        em.close();

        return patientRepresentationList;
    }

    @Post("json")
    public PatientRepresentation add(PatientRepresentation patientRepresentationIn) throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_DOCTOR);
        if (patientRepresentationIn == null) return null;

        EntityManager em = JpaUtil.getEntityManager();
        patientRepresentationIn.setDoctorId(this.doctorId);
        Patient patient = patientRepresentationIn.createPatient();

        PatientRepository patientRepository = new PatientRepository(em);
        patientRepository.save(patient);
        PatientRepresentation p = new PatientRepresentation(patient);
        return p;
    }
}
