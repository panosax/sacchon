package resource;

import dto.Credential;
import jpaUtil.JpaUtil;
import model.ChiefDoctor;
import model.Doctor;
import model.Patient;
import model.User;
import org.restlet.Request;
import org.restlet.Response;

import org.restlet.data.Header;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;
import repository.ChiefDoctorRepository;
import repository.DoctorRepository;
import repository.PatientRepository;
import security.Shield;
import util.CryptoUtils;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import static util.JWT.jwtCreate;
public class LogInResource extends ServerResource {



    @Post("json")
    public List<Integer> logIn(Credential credential) {

        Series<Header> responseHeaders = (Series<Header>) getResponse().getAttributes().get("org.restlet.http.headers");
        if (responseHeaders == null) {
            responseHeaders = new Series(Header.class);
            getResponse().getAttributes().put("org.restlet.http.headers", responseHeaders);
        }

        String username = credential.getUsername();
        String password = credential.getPassword();
        List<Integer> result = new ArrayList<>(3);


        Patient patient = isPatient(username, password, responseHeaders);
        if (patient != null) {
            result.add(1);
            result.add((int) patient.getId());
            result.add(patient.isConsultationChanged() ? 1 : 0);
            resetFlag(patient);
        }

        Doctor doctor = isDoctor(username, password, responseHeaders);
        if (doctor != null) {
            result.add(2);
            result.add((int) doctor.getId());
        }

        ChiefDoctor chiefDoctor = isChiefDoctor(username, password, responseHeaders);
        if (chiefDoctor != null) {
            result.add(3);
            result.add((int) chiefDoctor.getId());
        }
        return result;
    }

    public Patient isPatient(String username, String password, Series<Header> responseHeaders) {
        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        Patient patient = patientRepository.getByUsername(username);
        if (patient != null) {
            if (CryptoUtils.checkPassword(password, patient.getPassword())) {
                String token = jwtCreate(patient.getName(),"Patient",Shield.ROLE_PATIENT, patient.getId());
                responseHeaders.add(new Header("Token", token));
                return patient;
            }
        }
        return null;
    }

    public Doctor isDoctor(String username, String password, Series<Header> responseHeaders) {
        EntityManager em = JpaUtil.getEntityManager();
        DoctorRepository doctorRepository = new DoctorRepository(em);
        Doctor doctor = doctorRepository.getByUsername(username);
        if (doctor != null) {
            if (CryptoUtils.checkPassword(password, doctor.getPassword())) {
                String token = jwtCreate(doctor.getName(),"Doctor",Shield.ROLE_DOCTOR, doctor.getId());
                responseHeaders.add(new Header("Token", token));
                return doctor;
            }
        }
        return null;
    }

    public ChiefDoctor isChiefDoctor(String username, String password, Series<Header> responseHeaders) {
        EntityManager em = JpaUtil.getEntityManager();
        ChiefDoctorRepository chiefDoctorRepository = new ChiefDoctorRepository(em);
        ChiefDoctor chiefDoctor = chiefDoctorRepository.getByUsername(username);
        if (chiefDoctor != null) {
            if (CryptoUtils.checkPassword(password, chiefDoctor.getPassword())) {
                String token = jwtCreate(chiefDoctor.getName(),"ChiefDoctor",Shield.ROLE_CHIEF_DOCTOR, chiefDoctor.getId());
                responseHeaders.add(new Header("Token", token));
                return chiefDoctor;
            }
        }
        return null;
    }

    public void resetFlag(Patient patient) {
        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        patient.setConsultationChanged(false);
        patientRepository.update(patient);
    }


}
