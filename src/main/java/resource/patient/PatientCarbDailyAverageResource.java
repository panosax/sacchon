package resource.patient;

import exception.AuthorizationException;
import jpaUtil.JpaUtil;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import repository.PatientRepository;
import resource.ResourceUtils;
import security.Shield;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

public class PatientCarbDailyAverageResource extends ServerResource {
    private long patientId;


    @Get
    public List<Double> getAverageCarb() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_PATIENT);
        String start = getQueryValue("start");
        String end = getQueryValue("end");
        Date dateStart = ResourceUtils.stringToDate(start, -1);
        Date dateEnd = ResourceUtils.stringToDate(end, 1);

        EntityManager em = JpaUtil.getEntityManager();
        patientId = Long.parseLong(this.getRequest().getClientInfo().getUser().getIdentifier());

        PatientRepository patientRepository = new PatientRepository(em);
        List<Double> carbList = patientRepository.getCarbAverageList(patientId, dateStart, dateEnd);

        em.close();
        return carbList;
    }
}
