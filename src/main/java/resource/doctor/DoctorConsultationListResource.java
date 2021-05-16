package resource.doctor;

import exception.AuthorizationException;
import jpaUtil.JpaUtil;
import model.Consultation;
import model.Doctor;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import repository.ConsultationRepository;
import repository.DoctorRepository;
import representation.ConsultationRepresentation;
import resource.ResourceUtils;
import security.Shield;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DoctorConsultationListResource extends ServerResource {
    private long doctorId;


    @Get("json")
    public List<ConsultationRepresentation> getConsultationList() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_DOCTOR);
        doctorId = Long.parseLong(this.getRequest().getClientInfo().getUser().getIdentifier()); // extract id user

        EntityManager em = JpaUtil.getEntityManager();
        DoctorRepository doctorRepository = new DoctorRepository(em);
        List<Consultation> consultationList = doctorRepository.getConsultationList(this.doctorId);
        List<ConsultationRepresentation> consultationRepresentationList = new ArrayList<>();

        for (Consultation consultation : consultationList) {
            consultationRepresentationList.add(new ConsultationRepresentation(consultation));
        }

        em.close();

        return consultationRepresentationList;
    }

    @Post("json")
    public ConsultationRepresentation add(ConsultationRepresentation consultationRepresentationIn) throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_DOCTOR);
        if (consultationRepresentationIn == null) return null;
        doctorId = Long.parseLong(this.getRequest().getClientInfo().getUser().getIdentifier()); // extract id user

        consultationRepresentationIn.setDoctorId(this.doctorId);
        Consultation consultation = consultationRepresentationIn.createConsultation();
        EntityManager em = JpaUtil.getEntityManager();
        ConsultationRepository consultationRepository = new ConsultationRepository(em);
        consultationRepository.save(consultation);
        ConsultationRepresentation c = new ConsultationRepresentation(consultation);

        DoctorRepository doctorRepository = new DoctorRepository(em);
        Doctor doctor = doctorRepository.read(doctorId);
        em.detach(doctor);
        doctor.setRecentConsultation(new Date());
        doctorRepository.update(doctor);

        return c;
    }

}
