package resource;

import exception.AuthorizationException;
import jpaUtil.JpaUtil;
import model.ChiefDoctor;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import repository.ChiefDoctorRepository;
import representation.ChiefDoctorRepresentation;
import security.Shield;

import javax.persistence.EntityManager;

public class ChiefDoctorResource extends ServerResource {
    private long id;

//    protected void doInit() {
//        id = Long.parseLong(getAttribute("id"));
//    }


    @Get("json")
    public ChiefDoctorRepresentation getChiefDoctor() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_CHIEF_DOCTOR);
        id = Long.parseLong(this.getRequest().getClientInfo().getUser().getIdentifier());
        EntityManager em = JpaUtil.getEntityManager();
        ChiefDoctorRepository chiefDoctorRepository = new ChiefDoctorRepository(em);
        ChiefDoctor chiefDoctor = chiefDoctorRepository.read(id);
        ChiefDoctorRepresentation chiefDoctorRepresentation = new ChiefDoctorRepresentation(chiefDoctor);
        em.close();
        return chiefDoctorRepresentation;
    }
}
