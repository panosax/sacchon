package resource.patient;

import exception.AuthorizationException;
import jpaUtil.JpaUtil;
import model.Consultation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import repository.PatientRepository;
import representation.ConsultationRepresentation;
import resource.ResourceUtils;
import security.Shield;

import javax.persistence.EntityManager;
import java.util.List;

public class PatientConsultationResource extends ServerResource {
    private long patientId;
    private long consultationId;

    protected void doInit() {
        consultationId = Long.parseLong(getAttribute("consultationId"));
    }


    @Get("json")
    public ConsultationRepresentation getConsultation() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_PATIENT);
        EntityManager em = JpaUtil.getEntityManager();
        patientId = Long.parseLong(this.getRequest().getClientInfo().getUser().getIdentifier());

        PatientRepository patientRepository = new PatientRepository(em);
        List<Consultation> consultationList = patientRepository.getConsultationList(patientId);
        Consultation consultation = new Consultation();
        for (Consultation c : consultationList) {
            if (c.getId() == consultationId) {
                consultation = c;
            }
        }
        ConsultationRepresentation consultationRepresentation = new ConsultationRepresentation(consultation);
        em.close();
        return consultationRepresentation;
    }

}
