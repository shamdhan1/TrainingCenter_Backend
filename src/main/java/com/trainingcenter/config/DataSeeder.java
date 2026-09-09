package com.trainingcenter.config;

import com.trainingcenter.entity.*;
import com.trainingcenter.enums.*;
import com.trainingcenter.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Component
@Order(2)
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserAccountRepository userAccountRepository;
    private final CenterRepository centerRepository;
    private final CourseRepository courseRepository;
    private final TrainerRepository trainerRepository;
    private final BatchRepository batchRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final FeeAccountRepository feeAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Checking if database seeding is required...");

        Role adminRole = roleRepository.findByRoleName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));
        Role trainerRole = roleRepository.findByRoleName(RoleName.ROLE_TRAINER)
                .orElseThrow(() -> new RuntimeException("ROLE_TRAINER not found"));
        Role studentRole = roleRepository.findByRoleName(RoleName.ROLE_STUDENT)
                .orElseThrow(() -> new RuntimeException("ROLE_STUDENT not found"));

        String adminPasswordHash = passwordEncoder.encode("adminpassword");
        String trainerPasswordHash = passwordEncoder.encode("trainerpassword");
        String studentPasswordHash = passwordEncoder.encode("studentpassword");

        // 1. Seed Admins from credentials.html
        seedAdmins(adminRole, adminPasswordHash);

        // 2. Seed 8 Centers
        List<Center> centers = seedCenters();

        // 3. Seed 12 Common Courses across all 8 centers
        List<Course> allCourses = seedCourses(centers);

        // 4. Seed 25 Trainers
        List<Trainer> trainers = seedTrainers(centers, trainerRole, trainerPasswordHash);

        // 5. Seed Batches
        List<Batch> batches = seedBatches(centers, allCourses, trainers);

        // 6. Seed 250 Students with Enrollments & FeeAccounts
        seedStudents(centers, allCourses, trainers, batches, studentRole, studentPasswordHash);

        log.info("Database seeding completed successfully! Total centers: {}, courses: {}, trainers: {}, students: {}",
                centerRepository.count(), courseRepository.count(), trainerRepository.count(), studentRepository.count());
    }

    private void seedAdmins(Role adminRole, String passwordHash) {
        String[][] adminData = {
                {"rahul_admin", "rahul.sharma@trainingcenter.com"},
                {"priya_admin", "priya.deshmukh@trainingcenter.com"},
                {"amit_admin", "amit.patel@trainingcenter.com"},
                {"sneha_admin", "snehal.kulkarni@trainingcenter.com"}
        };

        for (String[] a : adminData) {
            if (!userAccountRepository.existsByUsername(a[0])) {
                UserAccount user = UserAccount.builder()
                        .username(a[0])
                        .passwordHash(passwordHash)
                        .email(a[1])
                        .status(UserStatus.ACTIVE)
                        .roles(new HashSet<>(Collections.singletonList(adminRole)))
                        .build();
                userAccountRepository.save(user);
            }
        }
    }

    private List<Center> seedCenters() {
        if (centerRepository.count() >= 8) {
            return centerRepository.findAll();
        }

        String[][] centerDefs = {
                {"CEN-CSN", "Chhatrapati Sambhajinagar Campus", "Jalna Road, CIDCO, Chhatrapati Sambhajinagar", "9822011111", "csn@trainingcenter.com"},
                {"CEN-PUN", "Pune IT Excellence Center", "FC Road, Deccan Gymkhana, Pune", "9822022222", "pune@trainingcenter.com"},
                {"CEN-AHM", "Ahmednagar Skill Academy", "Savedi Road, Ahmednagar", "9822033333", "nagar@trainingcenter.com"},
                {"CEN-MUM", "Mumbai Tech Hub", "Andheri East, Near Metro Station, Mumbai", "9822044444", "mumbai@trainingcenter.com"},
                {"CEN-NSK", "Nashik Training Institute", "College Road, Nashik", "9822055555", "nashik@trainingcenter.com"},
                {"CEN-NGP", "Nagpur Regional Training Center", "Dharampeth, Nagpur", "9822066666", "nagpur@trainingcenter.com"},
                {"CEN-KOP", "Kolhapur Learning Academy", "Tarabai Park, Kolhapur", "9822077777", "kolhapur@trainingcenter.com"},
                {"CEN-SOL", "Solapur Digital Center", "Hotgi Road, Solapur", "9822088888", "solapur@trainingcenter.com"}
        };

        List<Center> centers = new ArrayList<>();
        for (String[] def : centerDefs) {
            Center center = centerRepository.findAll().stream()
                    .filter(c -> c.getCenterCode().equalsIgnoreCase(def[0]))
                    .findFirst()
                    .orElseGet(() -> centerRepository.save(Center.builder()
                            .centerCode(def[0])
                            .name(def[1])
                            .address(def[2])
                            .phone(def[3])
                            .email(def[4])
                            .status(CenterStatus.ACTIVE)
                            .build()));
            centers.add(center);
        }
        return centers;
    }

    private List<Course> seedCourses(List<Center> centers) {
        if (courseRepository.count() >= centers.size() * 12) {
            return courseRepository.findAll();
        }

        // 12 Common Courses
        Object[][] courseDefs = {
                {"C", "C Programming Fundamentals", "Core C syntax, pointers, structures, file I/O", "1.5 Months", new BigDecimal("8000.00")},
                {"CPP", "C++ Object Oriented Programming", "Classes, inheritance, polymorphism, STL, templates", "2 Months", new BigDecimal("10000.00")},
                {"JAVA", "Core Java Development", "OOPs, Collections, Multi-threading, Streams, JDBC", "3 Months", new BigDecimal("18000.00")},
                {"ADJAVA", "Advanced Java & Frameworks", "Servlets, JSP, Hibernate ORM, Spring Framework", "3 Months", new BigDecimal("22000.00")},
                {"PYTHON", "Python Programming & Django", "Data types, OOP, Django Web Framework, REST APIs", "3 Months", new BigDecimal("20000.00")},
                {"FULLSTACK", "Full Stack Web Development", "HTML, CSS, JS, React/Angular, Node, Express, MongoDB/SQL", "6 Months", new BigDecimal("45000.00")},
                {"ANGULAR", "Angular Frontend Development", "TypeScript, Components, Directives, Services, RxJS, NgRx", "2.5 Months", new BigDecimal("18000.00")},
                {"REACT", "React.js Modern Frontend", "JSX, Hooks, Redux Toolkit, React Router, TailwindCSS", "2.5 Months", new BigDecimal("18000.00")},
                {"SPRINGBOOT", "Spring Boot & Microservices", "Spring Data JPA, Spring Security, JWT, Cloud, Kafka, Docker", "3 Months", new BigDecimal("25000.00")},
                {"DATASCIENCE", "Data Science & Machine Learning", "NumPy, Pandas, Matplotlib, Scikit-learn, ML Models, NLP", "6 Months", new BigDecimal("40000.00")},
                {"DEVOPS", "DevOps & CI/CD Pipelines", "Git, Docker, Kubernetes, Jenkins, Terraform, Ansible", "3 Months", new BigDecimal("30000.00")},
                {"CLOUD", "Cloud Computing (AWS / Azure)", "EC2, S3, RDS, Lambda, IAM, VPC, Cloud Architecture", "3 Months", new BigDecimal("28000.00")}
        };

        List<Course> courses = new ArrayList<>();
        for (Center center : centers) {
            String cCode = center.getCenterCode().replace("CEN-", "");
            for (Object[] def : courseDefs) {
                String subCode = (String) def[0];
                String courseCode = cCode + "-" + subCode;

                Course course = courseRepository.findAll().stream()
                        .filter(c -> c.getCourseCode().equalsIgnoreCase(courseCode))
                        .findFirst()
                        .orElseGet(() -> courseRepository.save(Course.builder()
                                .center(center)
                                .courseCode(courseCode)
                                .courseName((String) def[1])
                                .description((String) def[2])
                                .duration((String) def[3])
                                .totalFee((BigDecimal) def[4])
                                .status(CourseStatus.ACTIVE)
                                .build()));
                courses.add(course);
            }
        }
        return courses;
    }

    private List<Trainer> seedTrainers(List<Center> centers, Role trainerRole, String passwordHash) {
        if (trainerRepository.count() >= 25) {
            return trainerRepository.findAll();
        }

        Center csn = centers.stream().filter(c -> c.getCenterCode().contains("CSN")).findFirst().orElse(centers.get(0));
        Center pun = centers.stream().filter(c -> c.getCenterCode().contains("PUN")).findFirst().orElse(centers.get(1));
        Center ahm = centers.stream().filter(c -> c.getCenterCode().contains("AHM")).findFirst().orElse(centers.get(2));
        Center mum = centers.stream().filter(c -> c.getCenterCode().contains("MUM")).findFirst().orElse(centers.get(3));
        Center nsk = centers.stream().filter(c -> c.getCenterCode().contains("NSK")).findFirst().orElse(centers.get(4));
        Center ngp = centers.stream().filter(c -> c.getCenterCode().contains("NGP")).findFirst().orElse(centers.get(5));
        Center kop = centers.stream().filter(c -> c.getCenterCode().contains("KOP")).findFirst().orElse(centers.get(6));
        Center sol = centers.stream().filter(c -> c.getCenterCode().contains("SOL")).findFirst().orElse(centers.get(7));

        Object[][] trainerDefs = {
                // 3 from credentials.html
                {"rajesh_patil_pun", "EMP-TR-001", "Rajesh Patil", "rajesh.patil@trainingcenter.com", "9823100001", "Java & Spring Boot", "M.Tech in CSE", 8, "Senior Java Lead", pun},
                {"sunita_deshpande_pun", "EMP-TR-002", "Sunita Deshpande", "sunita.deshpande@trainingcenter.com", "9823100002", "Angular & Frontend", "MCA", 6, "Senior UI Architect", pun},
                {"vikram_singh_pun", "EMP-TR-003", "Vikram Singh", "vikram.singh@trainingcenter.com", "9823100003", "Python & Data Science", "Ph.D in AI/ML", 9, "Principal AI Mentor", pun},

                // Pune additional
                {"amit_kulkarni_pun", "EMP-TR-004", "Amit Kulkarni", "amit.kulkarni@trainingcenter.com", "9823100004", "Full Stack Development", "B.Tech IT", 5, "Senior Full Stack Trainer", pun},

                // Sambhajinagar
                {"sachin_shinde_csn", "EMP-TR-005", "Sachin Shinde", "sachin.shinde@trainingcenter.com", "9823100005", "Java & Spring Boot", "M.E. Computer", 7, "Technical Lead", csn},
                {"anjali_joshi_csn", "EMP-TR-006", "Anjali Joshi", "anjali.joshi@trainingcenter.com", "9823100006", "Python & Machine Learning", "M.Tech AI", 5, "Data Science Trainer", csn},
                {"pradeep_more_csn", "EMP-TR-007", "Pradeep More", "pradeep.more@trainingcenter.com", "9823100007", "C & C++ Programming", "MCA", 8, "Systems Trainer", csn},

                // Ahmednagar
                {"ganesh_pawar_ahm", "EMP-TR-008", "Ganesh Pawar", "ganesh.pawar@trainingcenter.com", "9823100008", "Core & Adv Java", "B.E. Computer", 6, "Senior Java Trainer", ahm},
                {"smita_chavan_ahm", "EMP-TR-009", "Smita Chavan", "smita.chavan@trainingcenter.com", "9823100009", "Angular & Web Development", "M.Sc Comp Sci", 4, "Web Tech Lead", ahm},
                {"nitin_kale_ahm", "EMP-TR-010", "Nitin Kale", "nitin.kale@trainingcenter.com", "9823100010", "Python & Django", "MCA", 5, "Python Specialist", ahm},

                // Mumbai
                {"rohit_sharma_mum", "EMP-TR-011", "Rohit Sharma", "rohit.sharma@trainingcenter.com", "9823100011", "Cloud Computing & AWS", "B.Tech IT", 9, "Principal Cloud Architect", mum},
                {"deepika_raut_mum", "EMP-TR-012", "Deepika Raut", "deepika.raut@trainingcenter.com", "9823100012", "React & Frontend", "MCA", 6, "Senior Frontend Lead", mum},
                {"sanjay_mehta_mum", "EMP-TR-013", "Sanjay Mehta", "sanjay.mehta@trainingcenter.com", "9823100013", "DevOps & Kubernetes", "M.Tech", 8, "Lead DevOps Consultant", mum},

                // Nashik
                {"kavita_gaikwad_nsk", "EMP-TR-014", "Kavita Gaikwad", "kavita.gaikwad@trainingcenter.com", "9823100014", "Spring Boot Microservices", "M.E. IT", 6, "Java Specialist", nsk},
                {"manoj_jadhav_nsk", "EMP-TR-015", "Manoj Jadhav", "manoj.jadhav@trainingcenter.com", "9823100015", "Data Science & AI", "M.Tech Data Sci", 7, "AI Mentor", nsk},
                {"swati_bhosale_nsk", "EMP-TR-016", "Swati Bhosale", "swati.bhosale@trainingcenter.com", "9823100016", "Full Stack Development", "B.E. Computer", 5, "Full Stack Lead", nsk},

                // Nagpur
                {"ajay_deshmukh_ngp", "EMP-TR-017", "Ajay Deshmukh", "ajay.deshmukh@trainingcenter.com", "9823100017", "Core Java & Spring", "M.Tech", 7, "Senior Technical Lead", ngp},
                {"neha_tamboli_ngp", "EMP-TR-018", "Neha Tamboli", "neha.tamboli@trainingcenter.com", "9823100018", "Python & Web Apps", "MCA", 5, "Python Mentor", ngp},
                {"vinod_kamble_ngp", "EMP-TR-019", "Vinod Kamble", "vinod.kamble@trainingcenter.com", "9823100019", "C, C++ & Data Structures", "M.Sc CS", 10, "Foundations Lead", ngp},

                // Kolhapur
                {"mahesh_salunkhe_kop", "EMP-TR-020", "Mahesh Salunkhe", "mahesh.salunkhe@trainingcenter.com", "9823100020", "Full Stack & Angular", "B.E. Computer", 6, "Senior Full Stack Trainer", kop},
                {"rupali_patil_kop", "EMP-TR-021", "Rupali Patil", "rupali.patil@trainingcenter.com", "9823100021", "Java & Spring Boot", "MCA", 5, "Java Lead", kop},
                {"santosh_kadam_kop", "EMP-TR-022", "Santosh Kadam", "santosh.kadam@trainingcenter.com", "9823100022", "DevOps & Cloud", "M.Tech", 7, "Cloud Specialist", kop},

                // Solapur
                {"varun_kulkarni_sol", "EMP-TR-023", "Varun Kulkarni", "varun.kulkarni@trainingcenter.com", "9823100023", "Core & Adv Java", "B.Tech CSE", 6, "Senior Technical Mentor", sol},
                {"shilpa_mane_sol", "EMP-TR-024", "Shilpa Mane", "shilpa.mane@trainingcenter.com", "9823100024", "Python & Machine Learning", "M.Sc Comp Sci", 5, "Data Science Trainer", sol},
                {"anand_shinde_sol", "EMP-TR-025", "Anand Shinde", "anand.shinde@trainingcenter.com", "9823100025", "React & Frontend", "MCA", 4, "Frontend Specialist", sol}
        };

        List<Trainer> trainers = new ArrayList<>();
        for (Object[] def : trainerDefs) {
            String username = (String) def[0];
            String empCode = (String) def[1];
            String name = (String) def[2];
            String email = (String) def[3];
            String mobile = (String) def[4];
            String spec = (String) def[5];
            String qual = (String) def[6];
            Integer exp = (Integer) def[7];
            String desig = (String) def[8];
            Center center = (Center) def[9];

            UserAccount user = userAccountRepository.findByUsername(username)
                    .orElseGet(() -> userAccountRepository.save(UserAccount.builder()
                            .username(username)
                            .passwordHash(passwordHash)
                            .email(email)
                            .mobile(mobile)
                            .center(center)
                            .status(UserStatus.ACTIVE)
                            .roles(new HashSet<>(Collections.singletonList(trainerRole)))
                            .build()));

            Trainer trainer = trainerRepository.findByEmployeeCode(empCode)
                    .orElseGet(() -> trainerRepository.save(Trainer.builder()
                            .user(user)
                            .center(center)
                            .employeeCode(empCode)
                            .name(name)
                            .email(email)
                            .mobile(mobile)
                            .specialization(spec)
                            .qualification(qual)
                            .experienceYears(exp)
                            .joiningDate(LocalDate.now().minusMonths(6 + (exp * 3L)))
                            .designation(desig)
                            .contractType("FULL_TIME")
                            .salary(new BigDecimal("75000.00"))
                            .status(UserStatus.ACTIVE)
                            .city(center.getName().split(" ")[0])
                            .state("Maharashtra")
                            .build()));
            trainers.add(trainer);
        }
        return trainers;
    }

    private List<Batch> seedBatches(List<Center> centers, List<Course> courses, List<Trainer> trainers) {
        if (batchRepository.count() >= 24) {
            return batchRepository.findAll();
        }

        List<Batch> batches = new ArrayList<>();
        int count = 1;
        for (Center center : centers) {
            List<Course> centerCourses = courses.stream()
                    .filter(c -> c.getCenter().getCenterId().equals(center.getCenterId()))
                    .toList();
            List<Trainer> centerTrainers = trainers.stream()
                    .filter(t -> t.getCenter().getCenterId().equals(center.getCenterId()))
                    .toList();

            if (centerCourses.isEmpty() || centerTrainers.isEmpty()) continue;

            for (int i = 0; i < Math.min(3, centerCourses.size()); i++) {
                Course course = centerCourses.get(i);
                Trainer trainer = centerTrainers.get(i % centerTrainers.size());
                String bCode = "BAT-" + course.getCourseCode() + "-" + String.format("%02d", count++);

                final int batchIndex = i;
                Batch batch = batchRepository.findAll().stream()
                        .filter(b -> b.getBatchCode().equalsIgnoreCase(bCode))
                        .findFirst()
                        .orElseGet(() -> batchRepository.save(Batch.builder()
                                .center(center)
                                .course(course)
                                .trainer(trainer)
                                .batchCode(bCode)
                                .batchName(course.getCourseName() + " Regular Batch " + (batchIndex + 1))
                                .startDate(LocalDate.now().minusWeeks(4 + batchIndex))
                                .endDate(LocalDate.now().plusWeeks(8 + batchIndex))
                                .maxStudents(30)
                                .status(CourseStatus.ACTIVE)
                                .build()));
                batches.add(batch);
            }
        }
        return batches;
    }

    private void seedStudents(List<Center> centers, List<Course> courses, List<Trainer> trainers,
                              List<Batch> batches, Role studentRole, String passwordHash) {
        if (studentRepository.count() >= 250) {
            return;
        }

        // 4 students from credentials.html
        String[][] credStudents = {
                {"arjun_kulkarni_pun_1", "Arjun", "Kulkarni", "arjun.kulkarni1@example.com", "9890000001"},
                {"aditya_patil_pun_2", "Aditya", "Patil", "aditya.patil2@example.com", "9890000002"},
                {"vihaan_deshmukh_pun_3", "Vihaan", "Deshmukh", "vihaan.deshmukh3@example.com", "9890000003"},
                {"krishna_shinde_pun_4", "Krishna", "Shinde", "krishna.shinde4@example.com", "9890000004"}
        };

        String[] firstNames = {
                "Aarav", "Vivaan", "Aditya", "Vihaan", "Arjun", "Sai", "Reyansh", "Ayaan", "Krishna", "Ishaan",
                "Shaurya", "Atharva", "Rohan", "Om", "Devansh", "Parth", "Samarth", "Aniket", "Siddharth", "Pranav",
                "Tejas", "Sanket", "Swapnil", "Akshay", "Nikhil", "Vikas", "Gaurav", "Mandar", "Shubham", "Tushar",
                "Ananya", "Diya", "Saanvi", "Ira", "Aadhya", "Pari", "Avani", "Riya", "Myra", "Prisha",
                "Tanvi", "Shruti", "Sneha", "Pooja", "Sakshi", "Neha", "Sayali", "Vaishnavi", "Pallavi", "Rutika"
        };

        String[] lastNames = {
                "Patil", "Deshmukh", "Kulkarni", "Joshi", "Shinde", "Pawar", "Chavan", "More", "Gaikwad", "Jadhav",
                "Kale", "Bhosale", "Salunkhe", "Kadam", "Mane", "Wagh", "Thorat", "Sawant", "Gore", "Suryavanshi",
                "Tamboli", "Kamble", "Shelar", "Nikam", "Ghatge", "Gholap", "Sonawane", "Borade", "Mahajan", "Kharat"
        };

        String[] qualifications = {"B.E. Computer", "B.Tech IT", "BCA", "MCA", "B.Sc Comp Sci", "Diploma Engg", "M.Tech"};

        Random rand = new Random(42);
        long existingStudents = studentRepository.count();
        int targetStudents = 250;

        List<Student> studentsToSave = new ArrayList<>();
        List<Enrollment> enrollmentsToSave = new ArrayList<>();
        List<FeeAccount> feeAccountsToSave = new ArrayList<>();

        for (int i = 1; i <= targetStudents; i++) {
            String regNo = String.format("REG-2026-%04d", i);
            if (studentRepository.existsByRegistrationNo(regNo)) {
                continue;
            }

            String username;
            String fName;
            String lName;
            String email;
            String mobile;

            if (i <= 4) {
                username = credStudents[i - 1][0];
                fName = credStudents[i - 1][1];
                lName = credStudents[i - 1][2];
                email = credStudents[i - 1][3];
                mobile = credStudents[i - 1][4];
            } else {
                fName = firstNames[rand.nextInt(firstNames.length)];
                lName = lastNames[rand.nextInt(lastNames.length)];
                username = (fName + "_" + lName + "_" + i).toLowerCase();
                email = (fName + "." + lName + i + "@example.com").toLowerCase();
                mobile = "98" + String.format("%08d", rand.nextInt(100000000));
            }

            Center center = centers.get(rand.nextInt(centers.size()));
            String qual = qualifications[rand.nextInt(qualifications.length)];

            UserAccount user = userAccountRepository.findByUsername(username)
                    .orElseGet(() -> userAccountRepository.save(UserAccount.builder()
                            .username(username)
                            .passwordHash(passwordHash)
                            .email(email)
                            .mobile(mobile)
                            .center(center)
                            .status(UserStatus.ACTIVE)
                            .roles(new HashSet<>(Collections.singletonList(studentRole)))
                            .build()));

            Student student = Student.builder()
                    .user(user)
                    .center(center)
                    .registrationNo(regNo)
                    .firstName(fName)
                    .lastName(lName)
                    .email(email)
                    .mobile(mobile)
                    .qualification(qual)
                    .dateOfBirth(LocalDate.of(2000 + rand.nextInt(4), 1 + rand.nextInt(12), 1 + rand.nextInt(28)))
                    .gender(rand.nextBoolean() ? "Male" : "Female")
                    .address("Flat " + (101 + rand.nextInt(800)) + ", Landmark Residency")
                    .city(center.getName().split(" ")[0])
                    .state("Maharashtra")
                    .pincode("41" + String.format("%04d", 1000 + rand.nextInt(9000)))
                    .registrationDate(LocalDate.now().minusDays(10 + rand.nextInt(120)))
                    .status(UserStatus.ACTIVE)
                    .build();

            studentsToSave.add(student);
        }

        List<Student> savedStudents = studentRepository.saveAll(studentsToSave);

        // Assign enrollments & fee accounts
        for (Student student : savedStudents) {
            Center center = student.getCenter();
            List<Course> centerCourses = courses.stream()
                    .filter(c -> c.getCenter().getCenterId().equals(center.getCenterId()))
                    .toList();
            List<Trainer> centerTrainers = trainers.stream()
                    .filter(t -> t.getCenter().getCenterId().equals(center.getCenterId()))
                    .toList();

            if (centerCourses.isEmpty()) continue;

            Course course = centerCourses.get(rand.nextInt(centerCourses.size()));
            Trainer trainer = centerTrainers.isEmpty() ? null : centerTrainers.get(rand.nextInt(centerTrainers.size()));

            Enrollment enrollment = Enrollment.builder()
                    .student(student)
                    .course(course)
                    .trainer(trainer)
                    .center(center)
                    .registrationDate(student.getRegistrationDate())
                    .startDate(student.getRegistrationDate().plusDays(2))
                    .expectedEndDate(student.getRegistrationDate().plusMonths(3))
                    .status(rand.nextInt(10) > 2 ? EnrollmentStatus.ACTIVE : EnrollmentStatus.COMPLETED)
                    .remarks("Enrolled through campus counselor")
                    .build();

            enrollmentsToSave.add(enrollment);
        }

        List<Enrollment> savedEnrollments = enrollmentRepository.saveAll(enrollmentsToSave);

        for (Enrollment enrollment : savedEnrollments) {
            BigDecimal totalFee = enrollment.getCourse().getTotalFee();
            int paymentType = rand.nextInt(3); // 0 = Full paid, 1 = Partial, 2 = Pending
            BigDecimal discount = BigDecimal.valueOf(rand.nextInt(3) * 1000);
            BigDecimal netFee = totalFee.subtract(discount);
            BigDecimal paidAmount;

            if (paymentType == 0) {
                paidAmount = netFee;
            } else if (paymentType == 1) {
                paidAmount = netFee.divide(BigDecimal.valueOf(2), java.math.RoundingMode.HALF_UP);
            } else {
                paidAmount = BigDecimal.ZERO;
            }

            FeeAccount feeAccount = FeeAccount.builder()
                    .enrollment(enrollment)
                    .totalFee(totalFee)
                    .discount(discount)
                    .netFee(netFee)
                    .paidAmount(paidAmount)
                    .remainingAmount(netFee.subtract(paidAmount))
                    .dueDate(LocalDate.now().plusDays(15))
                    .paymentStatus(paymentType == 0 ? PaymentStatus.PAID : (paymentType == 1 ? PaymentStatus.PARTIAL : PaymentStatus.PENDING))
                    .build();

            feeAccountsToSave.add(feeAccount);
        }

        feeAccountRepository.saveAll(feeAccountsToSave);
        log.info("Saved {} students, {} enrollments, and {} fee accounts.", savedStudents.size(), savedEnrollments.size(), feeAccountsToSave.size());
    }
}
