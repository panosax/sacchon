package router;

import org.restlet.Application;
import org.restlet.routing.Router;
import resource.*;
import resource.chiefDoctor.*;
import resource.doctor.*;
import resource.patient.*;

public class CustomRouter {


    private Application application;

    public CustomRouter(Application application) {
        this.application = application;
    }


    public Router publicResources() {
        Router router = new Router();
        router.attach("/ping", PingServerResource.class);
        router.attach("/login", LogInResource.class);

        router.attach("/register", registerResource.class);
//        router.attach("/chiefDoctor", ChiefDoctorListResource.class);
//        router.attach("/chiefDoctor/{id}", ChiefDoctorResource.class);


        return router;
    }


    public Router protectedResources() {
        Router router = new Router();

        //Patient
        router.attach("/patientSettings", PatientSettingsResource.class);
        router.attach("/patient/carb/{carbId}", PatientCarbResource.class);
        router.attach("/patient/carb/", PatientCarbListResource.class);
        router.attach("/patient/glucose/{glucoseId}", PatientGlucoseResource.class);
        router.attach("/patient/glucose/", PatientGlucoseListResource.class);
        router.attach("/patient/consultation/{consultationId}", PatientConsultationResource.class);
        router.attach("/patient/consultation/", PatientConsultationListResource.class);
        router.attach("/patientCarbDailyAverage/", PatientCarbDailyAverageResource.class); //get
        router.attach("/patientCarbAverage/", PatientCarbAverageResource.class);
        router.attach("/patientGlucoseAverage/", PatientGlucoseAverageResource.class); //get


        //Doctor
        router.attach("/doctor/patient/", DoctorPatientListResource.class);
        router.attach("/doctor/patient/{patientId}", DoctorPatientResource.class);
        router.attach("/doctor/{patientId}/carb/", DoctorPatientCarbListResource.class);
        router.attach("/doctor/{patientId}/glucose/", DoctorPatientGlucoseListResource.class);
        router.attach("/doctorPatient/{patientId}/consultation/", DoctorPatientConsultationListResource.class);
        router.attach("/doctorPatient/{patientId}/consultation/{consultationId}", DoctorPatientConsultationResource.class);

        router.attach("/doctor/unconsultedPatients/", DoctorUnconsultedPatientListResource.class);//
        router.attach("/doctor/unconsultedPatient/{unconsultedPatientId}", DoctorUnconsultedPatientResource.class);//
        router.attach("/doctor/needConsultationPatients/", DoctorNeedConsultationPatientListResource.class);//
        router.attach("/doctor/needConsultationPatient/{needConsultationPatientId}", DoctorNeedConsultationPatientResource.class);//
        router.attach("/doctor/consultation/", DoctorConsultationListResource.class);// put
        router.attach("/doctor/consultation/{consultationId}", DoctorConsultationResource.class);


        //ChiefDoctor
        router.attach("/patient", PatientListResource.class);
        router.attach("/patient/{id}", PatientResource.class);
        router.attach("/doctor", DoctorListResource.class);
        router.attach("/doctor/{id}", DoctorResource.class);

        router.attach("/carb", CarbListResource.class);
        router.attach("/carb/{id}", CarbResource.class);
        router.attach("/glucose", GlucoseListResource.class);
        router.attach("/glucose/{id}", GlucoseResource.class);
        router.attach("/consultation", ConsultationListResource.class);
        router.attach("/consultation/{id}", ConsultationResource.class);

        router.attach("/reportPatientCarb/{patientId}", ReportPatientCarbListResource.class); //get
        router.attach("/reportPatientGlucose/{patientId}", ReportPatientGlucoseListResource.class); //get
        router.attach("/reportDoctorConsultation/{doctorId}", ReportDoctorConsultationListResource.class);
        router.attach("/reportUnconsultedPatient/", ReportUnconsultedPatientListResource.class);//get more than a month
        router.attach("/reportUnconsultedPatientDiff/", ReportUnconsultedPatientDiffListResource.class);
        router.attach("/patientInactive", PatientInactiveListResource.class);
        router.attach("/doctorInactive", DoctorInactiveListResource.class);
        router.attach("/chiefDoctor", ChiefDoctorListResource.class);
        router.attach("/chiefDoctor", ChiefDoctorResource.class);

        return router;
    }


}
