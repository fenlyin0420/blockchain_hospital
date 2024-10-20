import com.example.entity.Doctor;

public class Main {
    public static void main(String[] args) {
        DoctorService doctorService = new DoctorService();

        Doctor doctor = doctorService.selectById(1);
        System.out.println(doctor);
    }
}
