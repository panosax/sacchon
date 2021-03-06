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
import java.util.List;

public class DoctorPatientResource extends ServerResource {
    private long doctorId;
    private long patientId;

    protected void doInit() {
        patientId = Long.parseLong(getAttribute("patientId"));
    }

    @Get("json")
    public PatientRepresentation getPatient() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_DOCTOR);
        doctorId = Long.parseLong(this.getRequest().getClientInfo().getUser().getIdentifier());
        EntityManager em = JpaUtil.getEntityManager();
        DoctorRepository doctorRepository = new DoctorRepository(em);
        List<Patient> patientList = doctorRepository.getPatientList(this.doctorId);
        Patient patient = new Patient();
        for (Patient p : patientList) {
            if (p.getId() == patientId) {
                patient = p;
            }
        }

        PatientRepresentation patientRepresentation = new PatientRepresentation(patient);
        em.close();

        return patientRepresentation;
    }


}
