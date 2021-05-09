package resource;

import exception.AuthorizationException;
import jpaUtil.JpaUtil;
import model.Carb;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import repository.CarbRepository;
import representation.CarbRepresentation;
import security.Shield;

import javax.persistence.EntityManager;

public class CarbResource extends ServerResource {
    private long id;

    protected void doInit() {
        id = Long.parseLong(getAttribute("id"));
    }


    @Get("json")
    public CarbRepresentation getCarb() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_CHIEF_DOCTOR);
        EntityManager em = JpaUtil.getEntityManager();
        CarbRepository carbRepository = new CarbRepository(em);
        Carb carb = carbRepository.read(id);
        CarbRepresentation carbRepresentation = new CarbRepresentation(carb);
        em.close();
        return carbRepresentation;
    }
}
