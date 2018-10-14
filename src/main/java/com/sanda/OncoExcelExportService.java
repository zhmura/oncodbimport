package com.sanda;

import com.monitorjbl.xlsx.StreamingReader;
import com.sanda.pojo.Diagnostics;
import com.sanda.pojo.GeneticPredictors;
import com.sanda.pojo.Patient;
import com.sanda.pojo.Treatment;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Zhmura on 3/22/2018.
 */

public class OncoExcelExportService {
    public static void main(String[] args) throws FileNotFoundException {
        InputStream is = new FileInputStream(new File("d:/oncology/PulmGen_01-12-17_mod2.xlsx"));
        Workbook workbook = StreamingReader.builder()
                .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
                .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
                .open(is);        // InputStream or File for XLSX file (required)

        List<Patient> patients = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0);
        System.out.println(sheet.getSheetName());
        Row header = null;
        int i = 1;
        for (Row r : sheet) {
            if (i > 400) {
                break;
            }
            if (i == 1) {
                header = r;
            }
            Patient patient = new Patient();
            Treatment treatment = new Treatment();
            Diagnostics diagnostics = new Diagnostics();
            GeneticPredictors geneticPredictors = new GeneticPredictors();

            if (i > 1) {
                try {
                    patient.setFullName(checkStringValue(r.getCell(1).getStringCellValue(), header.getCell(1).getStringCellValue(), true, i));
                    patient.setCardNumber(checkStringValue(r.getCell(2).getStringCellValue(), header.getCell(2).getStringCellValue(), true, i));

                    treatment.setSurgeryDate(r.getCell(3).getDateCellValue());
                    treatment.setSurgeryApplied(checkBooleanValue(r.getCell(4).getStringCellValue(), i, header.getCell(4).getStringCellValue()));
                    treatment.setSurgeryCode(checkIntegerValue(r.getCell(5).getStringCellValue(), header.getCell(5).getStringCellValue(), treatment.isSurgeryApplied(), i));

                    getPatientGender(i, r, patient);
                    patient.setBirthDate(r.getCell(8).getDateCellValue());
                    patient.setEmployed(checkBooleanValue(r.getCell(9).getStringCellValue(), i, header.getCell(9).getStringCellValue()));
                    patient.setProfessionCode(checkIntegerValue(r.getCell(10).getStringCellValue(), header.getCell(10).getStringCellValue(), false, i));
                    diagnostics.setComplains(checkBooleanValue(r.getCell(12).getStringCellValue(), i, header.getCell(12).getStringCellValue()));
                    diagnostics.setOncoAnamesys(checkBooleanValue(r.getCell(13).getStringCellValue(), i, header.getCell(13).getStringCellValue()));
                    diagnostics.setStage(checkStringValue(r.getCell(14).getStringCellValue(), header.getCell(14).getStringCellValue(), true, i));
                    diagnostics.setTnm(checkStringValue(r.getCell(16).getStringCellValue(), header.getCell(16).getStringCellValue(), true, i));

                    diagnostics.setT(checkStringValue(r.getCell(21).getStringCellValue(), header.getCell(21).getStringCellValue(), true, i));
                    diagnostics.setN(checkStringValue(r.getCell(22).getStringCellValue(), header.getCell(22).getStringCellValue(), true, i));
                    diagnostics.setM(checkStringValue(r.getCell(23).getStringCellValue(), header.getCell(23).getStringCellValue(), true, i));

                    diagnostics.setBronchialCarcinoma(checkBooleanValue(r.getCell(17).getStringCellValue(), i, header.getCell(17).getStringCellValue()));
                    diagnostics.setTumourSize(getTumorSize(checkLongValue(r.getCell(18).getStringCellValue(), header.getCell(18).getStringCellValue(), true, i)));
                    diagnostics.setLungCarcinoma(checkBooleanValue(r.getCell(19).getStringCellValue(), i, header.getCell(19).getStringCellValue()));
                    diagnostics.setGrade(checkIntegerValue(r.getCell(24).getStringCellValue(), header.getCell(24).getStringCellValue(), false, i));
                    diagnostics.setBreathingOrgansDiseases(checkIntegerArray(r.getCell(25).getStringCellValue(), header.getCell(25).getStringCellValue(), false, i));
                    diagnostics.setVascularDiseases(checkIntegerArray(r.getCell(26).getStringCellValue(), header.getCell(26).getStringCellValue(), false, i));
                    diagnostics.setAtherosclerosis(checkBooleanValue(r.getCell(27).getStringCellValue(), i, header.getCell(27).getStringCellValue()));
                    diagnostics.setAg(checkBooleanValue(r.getCell(28).getStringCellValue(), i, header.getCell(28).getStringCellValue()));
                    diagnostics.setHeartDiseases(checkIntegerArray(r.getCell(29).getStringCellValue(), header.getCell(29).getStringCellValue(), false, i));
                    diagnostics.setMiocardDiseases(checkIntegerArray(r.getCell(30).getStringCellValue(), header.getCell(30).getStringCellValue(), false, i));
                    diagnostics.setBloodDiseases(checkBooleanValue(r.getCell(31).getStringCellValue(), i, header.getCell(31).getStringCellValue()));
                    diagnostics.setDigestiveSystemDiseases(checkIntegerArray(r.getCell(32).getStringCellValue(), header.getCell(32).getStringCellValue(), false, i));
                    diagnostics.setUlcerDisease(checkBooleanValue(r.getCell(33).getStringCellValue(), i, header.getCell(33).getStringCellValue()));
                    diagnostics.setDiabetes(checkBooleanValue(r.getCell(34).getStringCellValue(), i, header.getCell(34).getStringCellValue()));
                    diagnostics.setEndocrineSystemDiseases(checkBooleanValue(r.getCell(35).getStringCellValue(), i, header.getCell(35).getStringCellValue()));
                    diagnostics.setStrokeConsequences(checkIntegerArray(r.getCell(36).getStringCellValue(), header.getCell(36).getStringCellValue(), false, i));
                    diagnostics.setNervousSystemDiseases(checkBooleanValue(r.getCell(37).getStringCellValue(), i, header.getCell(37).getStringCellValue()));
                    diagnostics.setGenitourinarySystemDiseases(checkIntegerArray(r.getCell(38).getStringCellValue(), header.getCell(38).getStringCellValue(), false, i));
                    diagnostics.setRheumaticDiseases(checkBooleanValue(r.getCell(39).getStringCellValue(), i, header.getCell(39).getStringCellValue()));
                    diagnostics.setMusculoskeletalSystemDiseases(checkBooleanValue(r.getCell(40).getStringCellValue(), i, header.getCell(40).getStringCellValue()));
                    diagnostics.setTuberculosis(checkBooleanValue(r.getCell(41).getStringCellValue(), i, header.getCell(41).getStringCellValue()));
                    diagnostics.setHistologyDiagnosis(checkIntegerValue(r.getCell(46).getStringCellValue(), header.getCell(46).getStringCellValue(), false, i));
                    diagnostics.setHystologyCode(checkStringValue(r.getCell(47).getStringCellValue(), header.getCell(47).getStringCellValue(), false, i));
                    diagnostics.setSmoking(checkBooleanValue(r.getCell(48).getStringCellValue(), i, header.getCell(48).getStringCellValue()));

                    Integer[] emptyArray = new Integer[0];
                    diagnostics.getDiseases().addAll(Arrays.asList(diagnostics.getBreathingOrgansDiseases() != null ? diagnostics.getBreathingOrgansDiseases() : emptyArray));
                    diagnostics.getDiseases().addAll(Arrays.asList(diagnostics.getVascularDiseases()!= null ? diagnostics.getVascularDiseases() : emptyArray));
                    diagnostics.getDiseases().addAll(Arrays.asList(diagnostics.getHeartDiseases()!= null ? diagnostics.getHeartDiseases() : emptyArray));
                    diagnostics.getDiseases().addAll(Arrays.asList(diagnostics.getMiocardDiseases()!= null ? diagnostics.getMiocardDiseases() : emptyArray));
                    diagnostics.getDiseases().addAll(Arrays.asList(diagnostics.getStrokeConsequences()!= null ? diagnostics.getStrokeConsequences() : emptyArray));
                    diagnostics.getDiseases().addAll(Arrays.asList(diagnostics.getGenitourinarySystemDiseases()!= null ? diagnostics.getGenitourinarySystemDiseases() : emptyArray));
                    diagnostics.getDiseases().addAll(Arrays.asList(diagnostics.getDigestiveSystemDiseases()!= null ? diagnostics.getDigestiveSystemDiseases() : emptyArray));
                    if (diagnostics.getBloodDiseases()) {
                        diagnostics.getDiseases().addAll(Collections.singletonList(120));
                    }
                    if (diagnostics.isUlcerDisease()) {
                        diagnostics.getDiseases().addAll(Collections.singletonList(105));
                    }
                    if (diagnostics.isEndocrineSystemDiseases()) {
                        diagnostics.getDiseases().addAll(Collections.singletonList(115));
                    }
                    if (diagnostics.isNervousSystemDiseases()) {
                        diagnostics.getDiseases().addAll(Collections.singletonList(116));
                    }
                    if (diagnostics.isRheumaticDiseases()) {
                        diagnostics.getDiseases().addAll(Collections.singletonList(118));
                    }
                    if (diagnostics.isMusculoskeletalSystemDiseases()) {
                        diagnostics.getDiseases().addAll(Collections.singletonList(109));
                    }


                    treatment.setRadiationTherapyApplied(checkBooleanValue(r.getCell(51).getStringCellValue(), i, header.getCell(51).getStringCellValue()));
                    treatment.setChemotherapyApplied(checkBooleanValue(r.getCell(53).getStringCellValue(), i, header.getCell(53).getStringCellValue()));
                    treatment.setFirstLineCourse(checkIntegerValue(r.getCell(54).getStringCellValue(), header.getCell(54).getStringCellValue(), false, i));
                    treatment.setSecondLineCourse(checkIntegerValue(r.getCell(90).getStringCellValue(), header.getCell(90).getStringCellValue(), false, i));
                    treatment.setThirdLineCourse(checkIntegerValue(r.getCell(119).getStringCellValue(), header.getCell(119).getStringCellValue(), false, i));

                    patient.setAlive(checkBooleanValue(r.getCell(50).getStringCellValue(), i, header.getCell(50).getStringCellValue()));
                    patient.setContactDate(checkDateValue(r.getCell(140).getDateCellValue(), header.getCell(140).getStringCellValue(), true, i));
                    patient.setAddress(checkStringValue(r.getCell(139).getStringCellValue(), header.getCell(139).getStringCellValue(), false, i));
                    patient.setPhone(checkStringValue(r.getCell(141).getStringCellValue(), header.getCell(141).getStringCellValue(), false, i));

                    geneticPredictors.setVegf_634(checkStringValueGenetic(r.getCell(144), header.getCell(144).getStringCellValue(), false, i));
                    geneticPredictors.setVegf_2578(checkStringValueGenetic(r.getCell(145), header.getCell(145).getStringCellValue(), false, i));
                    geneticPredictors.setVegf_936(checkStringValueGenetic(r.getCell(146), header.getCell(146).getStringCellValue(), false, i));
                    geneticPredictors.setEgf(checkStringValueGenetic(r.getCell(147), header.getCell(147).getStringCellValue(), false, i));

                    geneticPredictors.setGstt(checkStringValueGenetic(r.getCell(148), header.getCell(148).getStringCellValue(), false, i));
                    geneticPredictors.setGstm(checkStringValueGenetic(r.getCell(149), header.getCell(149).getStringCellValue(), false, i));
                    geneticPredictors.setGstp(checkStringValueGenetic(r.getCell(150), header.getCell(150).getStringCellValue(), false, i));
                    geneticPredictors.setNatkpn(checkStringValueGenetic(r.getCell(151), header.getCell(151).getStringCellValue(), false, i));
                    geneticPredictors.setNattag(checkStringValueGenetic(r.getCell(152), header.getCell(152).getStringCellValue(), false, i));
                    geneticPredictors.setNatbam(checkStringValueGenetic(r.getCell(153), header.getCell(153).getStringCellValue(), false, i));
                    geneticPredictors.setAcetylation_type(checkIntegerValue(r.getCell(154) != null ?
                            r.getCell(154).getStringCellValue() : null, header.getCell(147).getStringCellValue(), false, i));
                    geneticPredictors.setCyp_1a2(checkStringValueGenetic(r.getCell(155), header.getCell(155).getStringCellValue(), false, i));
                    geneticPredictors.setCyp_2d6(checkStringValueGenetic(r.getCell(156), header.getCell(156).getStringCellValue(), false, i));
                    geneticPredictors.setMdr(checkStringValueGenetic(r.getCell(157), header.getCell(157).getStringCellValue(), false, i));
                    geneticPredictors.setEgfr_18_tumor(checkStringValueGenetic(r.getCell(160), header.getCell(160).getStringCellValue(), false, i));
                    geneticPredictors.setEgfr_18_norm(checkStringValueGenetic(r.getCell(161), header.getCell(161).getStringCellValue(), false, i));
                    geneticPredictors.setEgfr_19_tumor(checkStringValueGenetic(r.getCell(162), header.getCell(162).getStringCellValue(), false, i));
                    geneticPredictors.setEgfr_19_norm(checkStringValueGenetic(r.getCell(163), header.getCell(163).getStringCellValue(), false, i));
                    geneticPredictors.setEgfr_20_tumor(checkStringValueGenetic(r.getCell(164), header.getCell(164).getStringCellValue(), false, i));
                    geneticPredictors.setEgfr_20_norm(checkStringValueGenetic(r.getCell(165), header.getCell(165).getStringCellValue(), false, i));
                    geneticPredictors.setEgfr_21_tumor(checkStringValueGenetic(r.getCell(166), header.getCell(166).getStringCellValue(), false, i));
                    geneticPredictors.setEgfr_21_norm(checkStringValueGenetic(r.getCell(167), header.getCell(167).getStringCellValue(), false, i));
                    geneticPredictors.setEgfr_21_blood(checkStringValueGenetic(r.getCell(168), header.getCell(168).getStringCellValue(), false, i));


                    geneticPredictors.setTgf_509(checkStringValueGenetic(r.getCell(169), header.getCell(169).getStringCellValue(), false, i));
                    geneticPredictors.setTgf_25_codon(checkStringValueGenetic(r.getCell(170), header.getCell(170).getStringCellValue(), false, i));
                    geneticPredictors.setTgfr_206(checkStringValueGenetic(r.getCell(171), header.getCell(171).getStringCellValue(), false, i));
                    geneticPredictors.setKdr_1719(checkStringValueGenetic(r.getCell(172), header.getCell(172).getStringCellValue(), false, i));
                    geneticPredictors.setKdr_906(checkStringValueGenetic(r.getCell(173), header.getCell(173).getStringCellValue(), false, i));
                    geneticPredictors.setSult1(checkStringValueGenetic(r.getCell(174), header.getCell(174).getStringCellValue(), false, i));
                    geneticPredictors.setMmp9_2660(checkStringValueGenetic(r.getCell(175), header.getCell(175).getStringCellValue(), false, i));
                    geneticPredictors.setMmp9_1562(checkStringValueGenetic(r.getCell(176), header.getCell(176).getStringCellValue(), false, i));
                    geneticPredictors.setMmp2_735(checkStringValueGenetic(r.getCell(177), header.getCell(177).getStringCellValue(), false, i));
                    geneticPredictors.setMmp2_1575(checkStringValueGenetic(r.getCell(178), header.getCell(178).getStringCellValue(), false, i));
                    geneticPredictors.setKras_2ex_tumor(checkStringValueGenetic(r.getCell(179), header.getCell(179).getStringCellValue(), false, i));
                    geneticPredictors.setKras_2ex_norm(checkStringValueGenetic(r.getCell(180), header.getCell(180).getStringCellValue(), false, i));
                    geneticPredictors.setPik3ca_9ex(checkStringValueGenetic(r.getCell(181), header.getCell(181).getStringCellValue(), false, i));
                    geneticPredictors.setPik3ca_20ex(checkStringValueGenetic(r.getCell(182), header.getCell(182).getStringCellValue(), false, i));
                    geneticPredictors.setPten(checkStringValueGenetic(r.getCell(183), header.getCell(183).getStringCellValue(), false, i));
                    geneticPredictors.setDnmt_149(checkStringValueGenetic(r.getCell(184), header.getCell(184).getStringCellValue(), false, i));
                    geneticPredictors.setDnmt_579(checkStringValueGenetic(r.getCell(185), header.getCell(185).getStringCellValue(), false, i));

                    patient.setSurvivalMonth(getMonthsDifference(patient.getContactDate(), treatment.getSurgeryDate()));
                    patient.setGeneticPredictors(geneticPredictors);
                    patient.setDiagnostics(diagnostics);
                    patient.setTreatment(treatment);

                    patients.add(patient);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            i++;
        }
        dbImport(patients);
    }

    public static void dbImport(List<Patient> patients) {


        System.out.println("-------- PostgreSQL "
                + "JDBC Connection Testing ------------");

        try {

            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {

            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
            return;

        }

        System.out.println("PostgreSQL JDBC Driver Registered!");

        Connection connection = null;

        try {

            connection = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/onco_cure_assist", "postgres",
                    "postgres");
            // Create Oracle DatabaseMetaData object
            DatabaseMetaData meta = connection.getMetaData();

            // gets driver info:
            System.out.println("JDBC driver version is " + meta.getDriverVersion());

        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;

        }

        System.out.println("You made it, take control your database now!");

        for (Patient patient : patients) {
            //"INSERT INTO public.patient (full_name, gender, birth_date, contact_date, alive, survival_month, employed, profession_code, address, phone, patient_card_number) VALUES ('full name', 0, '2018-05-27', '2018-05-27', true, 28, true, 1, 'address', '+37529', '123456');";

            String patientImport =
                    "INSERT INTO patient (full_name, gender, birth_date, contact_date, alive, survival_month, employed, profession_code, address, phone, patient_card_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";
            String diagnosticsImport = "INSERT INTO diagnostics(" +
                    "patient_id, TNM, primary_tumour, regional_lymph_nodes, distant_metastasis, tumour_size, side, bronchial_carcinoma, lung_carcinoma, peribroncial_carcinoma, interlobar, subcarinal, lower_paratracheal, upper_paratracheal, same_lung_metastasis, symptoms, VLC, TVC, TIFF_number, volume_force_exp, heart_rate, heart_block, extrasistols, somatic, BAC, onco_anamesys, stage, complains, grade, histology_diagnosis, smoking, histology_code, t, n, m, copd, tuberculomas, chd, lcd, inflammatory_digestive_processes, hepatitis, cirrhosis, pancreatitis, musculoskeletal_diseases, inflammatory_kidneys_bladder_diseases, prostate_benign_diseases, veins_diseases, blood_vessels_diseases, rhythm_disturbances, thyroid_gland_benign_diseases, nervous_diseases, strokes, rheumatic_diseases, anemia) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            String treatmentImport = "INSERT INTO treatment(" +
                    "surgery_applied, chemotherapy_applied, radiation_therapy_applied, surgery_code, surgeon_name, patient_id, surgery_date, first_line_course, second_line_course, third_line_course) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            String geneticImport = "INSERT INTO genetic_predictors(" +
                    "patient_id, vegf_634, vegf_2578, vegf_936, egf, gstt, gstm, gstp, natkpn, nattag, natbam, acetylation_type, cyp_1a2, cyp_2d6, mdr, egfr_18_tumor, egfr_18_norm, egfr_19_tumor, egfr_19_norm, egfr_20_tumor, egfr_20_norm, egfr_21_tumor, egfr_21_norm, egfr_21_blood, tgf_509, tgf_25_codon, tgfr_206, kdr_1719, kdr_906, sult1, mmp9_2660, mmp9_1562, mmp2_735, \"ММp2_1575\", kras_2ex_tumor, kras_2ex_norm, pik3ca_9ex, pik3ca_20ex, pten, dnmt_149, dnmt_579) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            Long id = null;
            try {
                connection.setAutoCommit(false); //transaction block start
                Long patientId = savePatient(connection, patient, patientImport);
                Diagnostics diagnostics = patient.getDiagnostics();
                saveDiagnostics(connection, patientId, diagnostics, diagnosticsImport);
                Treatment treatment = patient.getTreatment();
                saveTreatment(connection, patientId, treatment, treatmentImport);
                GeneticPredictors geneticPredictors = patient.getGeneticPredictors();
                saveGenetics(connection, patientId, geneticPredictors, geneticImport);
                connection.commit();
            } catch (Exception se) {
                System.err.println(se.getMessage());
            }
        }
    }

    private static void saveDiagnostics(Connection connection, Long id, Diagnostics diagnostics, String diagnosticsImport) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(diagnosticsImport);

        pstmt.setLong(1, id);
        pstmt.setString(2, diagnostics.getTnm());
        pstmt.setNull(3, Types.INTEGER);
        pstmt.setNull(4, Types.INTEGER);
        pstmt.setNull(5, Types.INTEGER);

        if (diagnostics.getTumourSize() != null) {
            pstmt.setLong(6, diagnostics.getTumourSize());
        } else {
            pstmt.setNull(6, Types.BIGINT);
        }        //side
        pstmt.setNull(7, Types.INTEGER);

        pstmt.setBoolean(8, diagnostics.isBronchialCarcinoma());
        pstmt.setBoolean(9, diagnostics.isLungCarcinoma());
        pstmt.setBoolean(10, diagnostics.isPeribroncialCarcinoma());
        pstmt.setBoolean(11, diagnostics.isInterlobar());
        pstmt.setBoolean(12, diagnostics.isSubcarinal());
        pstmt.setBoolean(13, diagnostics.isLowerParatracheal());
        pstmt.setBoolean(14, diagnostics.isUpperParatracheal());
        //same_lung_metastasis, symptoms
        pstmt.setNull(15, Types.BOOLEAN);
        pstmt.setNull(16, Types.BOOLEAN);

        pstmt.setNull(17, Types.DOUBLE);
        pstmt.setNull(18, Types.DOUBLE);
        pstmt.setNull(19, Types.DOUBLE);
        pstmt.setNull(20, Types.DOUBLE);
        pstmt.setNull(21, Types.INTEGER);
        pstmt.setNull(22, Types.BOOLEAN);
        pstmt.setNull(23, Types.BOOLEAN);
        pstmt.setNull(24, Types.BOOLEAN);
        pstmt.setNull(25, Types.BOOLEAN);
        pstmt.setBoolean(26, diagnostics.isOncoAnamesys());
        pstmt.setString(27, diagnostics.getStage());
        pstmt.setBoolean(28, diagnostics.isComplains());
        if (diagnostics.getGrade() != null) {
            pstmt.setInt(29, diagnostics.getGrade());
        } else {
            pstmt.setNull(29, Types.INTEGER);
        }
        if (diagnostics.getHistologyDiagnosis() != null) {
            pstmt.setInt(30, diagnostics.getHistologyDiagnosis());
        } else {
            pstmt.setNull(30, Types.INTEGER);
        }
        pstmt.setBoolean(31, diagnostics.isSmoking());
        pstmt.setString(32, diagnostics.getHystologyCode());
        pstmt.setString(33, diagnostics.getT());
        pstmt.setString(34, diagnostics.getM());
        pstmt.setString(35, diagnostics.getN());

        pstmt.setBoolean(36, diagnostics.getDiseases().contains(101));
        pstmt.setBoolean(37, diagnostics.getDiseases().contains(102));
        pstmt.setBoolean(38, diagnostics.getDiseases().contains(103));
        pstmt.setBoolean(39, diagnostics.getDiseases().contains(104));
        pstmt.setBoolean(40, diagnostics.getDiseases().contains(105));
        pstmt.setBoolean(41, diagnostics.getDiseases().contains(106));
        pstmt.setBoolean(42, diagnostics.getDiseases().contains(107));
        pstmt.setBoolean(43, diagnostics.getDiseases().contains(108));
        pstmt.setBoolean(44, diagnostics.getDiseases().contains(109));
        pstmt.setBoolean(45, diagnostics.getDiseases().contains(110));
        pstmt.setBoolean(46, diagnostics.getDiseases().contains(111));
        pstmt.setBoolean(47, diagnostics.getDiseases().contains(112));
        pstmt.setBoolean(48, diagnostics.getDiseases().contains(113));
        pstmt.setBoolean(49, diagnostics.getDiseases().contains(114));
        pstmt.setBoolean(50, diagnostics.getDiseases().contains(115));
        pstmt.setBoolean(51, diagnostics.getDiseases().contains(116));
        pstmt.setBoolean(52, diagnostics.getDiseases().contains(117));
        pstmt.setBoolean(53, diagnostics.getDiseases().contains(118));
        pstmt.setBoolean(54, diagnostics.getDiseases().contains(120));
        try {
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Diagnostics save issue " + e.getMessage());
        }
    }

    private static void saveTreatment(Connection connection, Long id, Treatment treatment, String treatmentImport) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(treatmentImport);
        pstmt.setBoolean(1, treatment.isSurgeryApplied());
        pstmt.setBoolean(2, treatment.isChemotherapyApplied());
        pstmt.setBoolean(3, treatment.isRadiationTherapyApplied());
        if ( treatment.getSurgeryCode() != null) {
            pstmt.setInt(4, treatment.getSurgeryCode());
        } else {
            pstmt.setNull(4, Types.INTEGER);
        }
        pstmt.setNull(5, Types.VARCHAR);
        pstmt.setLong(6, id);
        if (treatment.getSurgeryDate() != null) {
            pstmt.setDate(7, java.sql.Date.valueOf(treatment.getSurgeryDate().toInstant()
                    .atZone(ZoneId.of("UTC")).toLocalDate()));
        } else {
            pstmt.setDate(7, null);
        }
        if ( treatment.getFirstLineCourse() != null) {
            pstmt.setInt(8, treatment.getFirstLineCourse());
        } else {
            pstmt.setNull(8, Types.INTEGER);
        }
        if ( treatment.getSecondLineCourse() != null) {
            pstmt.setInt(9, treatment.getSecondLineCourse());
        } else {
            pstmt.setNull(9, Types.INTEGER);
        }
        if ( treatment.getThirdLineCourse() != null) {
            pstmt.setInt(10, treatment.getThirdLineCourse());
        } else {
            pstmt.setNull(10, Types.INTEGER);
        }

        try {
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Treatment save issue " + e.getMessage());
        }
    }

    private static void saveGenetics(Connection connection, Long id, GeneticPredictors geneticPredictors, String geneticImport) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(geneticImport);

        pstmt.setLong(1, id);
        pstmt.setString(2, geneticPredictors.getVegf_634());
        pstmt.setString(3, geneticPredictors.getVegf_2578());
        pstmt.setString(4, geneticPredictors.getVegf_936());
        pstmt.setString(5, geneticPredictors.getEgf());
        pstmt.setString(6, geneticPredictors.getGstt());
        pstmt.setString(7, geneticPredictors.getGstm());
        pstmt.setString(8, geneticPredictors.getGstp());
        pstmt.setString(9, geneticPredictors.getNatkpn());
        pstmt.setString(10, geneticPredictors.getNattag());
        pstmt.setString(11, geneticPredictors.getNatbam());
        if (geneticPredictors.getAcetylation_type() != null) {
            pstmt.setInt(12, geneticPredictors.getAcetylation_type());
        } else {
            pstmt.setNull(12, Types.INTEGER);
        }

        pstmt.setString(13, geneticPredictors.getCyp_1a2());
        pstmt.setString(14, geneticPredictors.getCyp_2d6());
        pstmt.setString(15, geneticPredictors.getMdr());
        pstmt.setString(16, geneticPredictors.getEgfr_18_tumor());
        pstmt.setString(17, geneticPredictors.getEgfr_18_norm());
        pstmt.setString(18, geneticPredictors.getEgfr_19_tumor());
        pstmt.setString(19, geneticPredictors.getEgfr_19_norm());
        pstmt.setString(20, geneticPredictors.getEgfr_20_tumor());
        pstmt.setString(21, geneticPredictors.getEgfr_20_norm());
        pstmt.setString(22, geneticPredictors.getEgfr_21_tumor());
        pstmt.setString(23, geneticPredictors.getEgfr_21_norm());
        pstmt.setString(24, geneticPredictors.getEgfr_21_blood());
        pstmt.setString(25, geneticPredictors.getTgf_509());
        pstmt.setString(26, geneticPredictors.getTgf_25_codon());
        pstmt.setString(27, geneticPredictors.getTgfr_206());
        pstmt.setString(28, geneticPredictors.getKdr_1719());
        pstmt.setString(29, geneticPredictors.getKdr_906());
        pstmt.setString(30, geneticPredictors.getSult1());
        pstmt.setString(31, geneticPredictors.getMmp9_2660());
        pstmt.setString(32, geneticPredictors.getMmp9_1562());
        pstmt.setString(33, geneticPredictors.getMmp2_735());
        pstmt.setString(34, geneticPredictors.getMmp2_1575());
        pstmt.setString(35, geneticPredictors.getKras_2ex_tumor());
        pstmt.setString(36, geneticPredictors.getKras_2ex_norm());
        pstmt.setString(37, geneticPredictors.getPik3ca_9ex());
        pstmt.setString(38, geneticPredictors.getPik3ca_20ex());
        pstmt.setString(39, geneticPredictors.getPten());
        pstmt.setString(40, geneticPredictors.getDnmt_149());
        pstmt.setString(41, geneticPredictors.getDnmt_579());


        try {
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Genetics save issue " + e.getMessage());
        }
    }


    private static Long savePatient(Connection connection, Patient patient, String patientImport) throws SQLException {
        Long id = null;
        PreparedStatement pstmt = connection.prepareStatement(patientImport);
        pstmt.setString(1, patient.getFullName());
        pstmt.setInt(2, patient.getGender().equals("male") ? 1 : 0);
        if (patient.getBirthDate() != null) {
            pstmt.setDate(3, java.sql.Date.valueOf(patient.getBirthDate().toInstant()
                    .atZone(ZoneId.of("UTC")).toLocalDate()));
        } else {
            pstmt.setDate(3, null);
        }
        if (patient.getContactDate() != null) {
            pstmt.setDate(4, java.sql.Date.valueOf(patient.getContactDate().toInstant()
                    .atZone(ZoneId.of("UTC")).toLocalDate()));
        } else {
            pstmt.setDate(4, null);
        }
        pstmt.setBoolean(5, patient.isAlive());
        if (patient.getSurvivalMonth() != null) {
            pstmt.setDouble(6, patient.getSurvivalMonth());
        } else {
            pstmt.setNull(6, Types.FLOAT);
        }
        pstmt.setBoolean(7, patient.isEmployed());
        if (patient.getProfessionCode() != null) {
            pstmt.setInt(8, patient.getProfessionCode());
        } else {
            pstmt.setNull(8, Types.INTEGER);
        }
        pstmt.setString(9, patient.getAddress());
        pstmt.setString(10, patient.getPhone());
        pstmt.setString(11, patient.getCardNumber());
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            id = rs.getLong(1);
            patient.setId(id);
        }
        try {
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Patient save issue " + e.getMessage());
        }
        return id;
    }

    public static final Long getMonthsDifference(java.util.Date date1, java.util.Date date2) {
        if (date1 != null && date2 != null) {
            YearMonth m1 = YearMonth.from(date1.toInstant().atZone(ZoneId.of("UTC")));
            YearMonth m2 = YearMonth.from(date2.toInstant().atZone(ZoneId.of("UTC")));

            return m2.until(m1, ChronoUnit.MONTHS) + 1;
        } else {
            return null;
        }
    }

    private static Long getTumorSize(Long param) {
        if (param == null) {
            return null;
        }
        if (param >= 60) {
            return 2L;
        }
        if (param < 60 && param >= 30) {
            return 1L;
        }
        if (param < 30 && param >= 0) {
            return 0L;
        }
        return null;
    }

    public static Integer[] checkIntegerArray(String s, String fieldName, boolean isMandatory, int row) {
        //System.out.println("checking array value " + s + " for column: " + fieldName);
        if (s != null && !s.trim().isEmpty()) {
            return Arrays.asList(s.trim().split(",")).stream().map(String::trim).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new);
        } else if (isMandatory) {
            throw new IllegalArgumentException("Integer array is mandatory for " + fieldName + " but empty for row: " + row);
        }
        return null;
    }

    public static Integer checkIntegerValue(String s, String fieldName, boolean isMandatory, int row) {
        //System.out.println("checking int value " + s + " for column: " + fieldName);
        if (s != null && !s.trim().isEmpty()) {
            return Integer.parseInt(s.trim());
        } else if (isMandatory) {
            throw new IllegalArgumentException("Integer is mandatory for " + fieldName + " but empty for row: " + row);
        }
        return null;
    }

    public static void getPatientGender(int i, Row r, Patient patient) {
        String s = checkStringValue(r.getCell(7).getStringCellValue(), "gender", true, i);
        switch (s.toLowerCase()) {
            case "мужской":
                patient.setGender("male");
                break;
            case "женский":
                patient.setGender("female");
                break;
            default:
                System.out.println("Gender parsing failed with row: " + i);
                patient.setGender("male");

        }
    }

    public static String checkStringValueGenetic(Cell c, String fieldName, boolean isMandatory, long row) {
        if (c == null) return null;
        //System.out.println("checking string value " + c.getStringCellValue() + " for column: " + fieldName);
        if (c.getStringCellValue() != null && !c.getStringCellValue().trim().isEmpty()) {
            if (c.getStringCellValue().equalsIgnoreCase("нет")) {
                return "no";
            } else {
                return c.getStringCellValue().trim();
            }
        } else if (isMandatory) {
            throw new IllegalArgumentException("String is mandatory for " + fieldName + " but empty for row: " + row);
        }
        return null;
    }

    public static String checkStringValueGenetic(String s, String fieldName, boolean isMandatory, long row) {
        //System.out.println("checking string value " + s + " for column: " + fieldName);
        if (s != null && !s.trim().isEmpty()) {
            if (s.equalsIgnoreCase("нет")) {
                return "no";
            } else {
                return s.trim();
            }
        } else if (isMandatory) {
            throw new IllegalArgumentException("String is mandatory for " + fieldName + " but empty for row: " + row);
        }
        return null;
    }

    public static String checkStringValue(String s, String fieldName, boolean isMandatory, long row) {
        //System.out.println("checking string value " + s + " for column: " + fieldName);
        if (s != null && !s.trim().isEmpty()) {
            return s.trim();
        } else if (isMandatory) {
            throw new IllegalArgumentException("String is mandatory for " + fieldName + " but empty for row: " + row);
        }
        return null;
    }

    public static Long checkLongValue(String s, String fieldName, boolean isMandatory, long row) {
        //System.out.println("checking long value " + s + " for column: " + fieldName);
        if (s != null && !s.trim().isEmpty()) {
            return Long.parseLong(s.trim());
        } else if (isMandatory) {
            throw new IllegalArgumentException("Long is mandatory for " + fieldName + " but empty for row: " + row);
        }
        return null;
    }

    public static Boolean checkBooleanValue(String s, long row, String fieldName) {
        //System.out.println("checking bool value: " + s + " for column: " + fieldName);
        return !(s == null || s.trim().isEmpty() || s.trim().equals("0"));
    }

    public static java.util.Date checkDateValue(String s, String fieldName, boolean isMandatory, String format, long row) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (s != null && !s.trim().isEmpty()) {
            return sdf.parse(s.trim());
        } else if (isMandatory) {
            throw new IllegalArgumentException("Long is mandatory for " + fieldName + "but empty for row: " + row);
        }
        return null;
    }

    public static java.util.Date checkDateValue(java.util.Date d, String fieldName, boolean isMandatory, long row) throws ParseException {
        if (d != null) {
            return d;
        } else if (isMandatory) {
            throw new IllegalArgumentException("Date is mandatory for " + fieldName + "but empty for row: " + row);
        }
        return null;
    }

//18	Воробьёв Е.А.	12792/03	01/12/06	1	0	пневмонэктомия	мужской	17/11/52	1	3	охранник	0	0	1		T2N0M0		40	40	2	2	0	0	2	ХОБЛ								язвенная болезнь									1				плоскоклеточный	17629/03	1	1	1	0		1	2													доксо	90			натулан	2100											натулан	2100	доксорубицин	90																																																					Минск, Гамарника 21-56	8/4/2007	261-74-18			G/G	A/A	C/T	G/A	1	0	G/A	C/C	G/A	G/G	fast	C/A	G/A	C/T	8.1	51										C/T			T/T	C/C		A/A		C/C	G/A



 /*
    0 - ШифрГен
    1 - Фамилия
    2 - № а/к
    3 - Дата операции
    4 - Операция (да; нет)
    5 - Операция (шифр.)
    6 - Операция
    7 - Пол
    8 - Дата рождения
    9 - Работающие - 1;не работающие - 0
    10 - Профессия (шифр)
    11 - Профессия
    12 - Жалобы (шифр)
    13 - Онкоанамнез
    14 - Стадия
    15 - Стадия(уточнена до a и b)
    16 - TNM
    17 - Рак Бронха (bronchial carcinoma)
    18 - РазмерыОпухоли (мм)
    19 - Опухоль центральная/периферическая (lung carcinoma)
    20 - T уточнена до a и b
    21 - T
    22 - N
    23 - M
    24 - Степень Дифференцировки (grade)
    25 - Заболевания Органов Дыхания
    26 - Заболевания сосудов
    27 - Атеросклероз
    28 - АГ
    29 - ИБС и другие состояния сердца
    30 - Дистрофия миокарда
    31 - Болезни крови
    32 - Заболевания Органов Пищеварения
    33 - Язвенная болезнь
    34 - Диабет
    35 - Заболевания Эндокринной системы другие
    36 - Инсульт и последствия
    37 - Болезни нервной системы
    38 - Заболевания мочеполовой системы
    39 - Ревматические болезни
    40 - Болезни опорно-двиг. Аппарата
    41 - Гист. Туберкулез
    42 - Гист. РЛ (шифр)
    43 - Гист. Доброкач. Опухоли и Процессы
    44 - Карциноид легкого
    45 - Гист. Другой не РЛ
    46 - Гистологический диагноз
    47 - № Гистологии
    48 - Статус курения
    49 - Течение Заболевания (1 = прогрессирование, 0 = здоров)
    50 - Исход Заболевания
    51 - Лучевая терапия
    52 - СОД (Гр)
    53 - Химио-терапия
    54 - Количество Курсов Первой Линии
    55 - Платина
    56 - Доза Платины
    57 - Карбоплатин
    58 - Доза Карбоплатина
    59 - Навельбин
    60 - Доза Навельбина
    61 - Этопозид
    62 - Доза Этопозида
    63 - Паклитаксел
    64 - Доза Паклитаксела
    65 - Винкристин
    66 - Доза Винкристина
    67 - Доксорубицин
    68 - Доза Доксорубицина
    69 - Циклофосфан
    70 - Доза Циклофосфана
    71 - Натулан
    72 - Доза Натулана
    73 - 5-фторурацил
    74 - Доза 5-фторурацила
    75 - Преднизолон
    76 - Доза Преднизолона
    77 - Золедрон
    78 - Доза Золедрона
    79 - Алимта
    80 - Доза Алимты
    81 - Гемцитабин
    82 - Доза Гемцитабина
    83 - Препарат 1 Первой Линии
    84 - Доза 1 Первой Линии
    85 - Препарат 2 Первой Линии
    86 - Доза 2 Первой Линии
    87 - Препарат 3 Первой Линии
    88 - Доза 3 Первой Линии
    89 - Линия Вторая
    90 - Количество Курсов Второй Линии
    91 - Платина 2
    92 - Доза Платины 2
    93 - Карбоплатин 2
    94 - Доза Карбоплатина 2
    95 - Навельбин 2	Доза
    96 - Навельбина 2
    97 - Этопозид 2
    98 - Доза Этопозида 2
    99 - Паклитаксел 2
    100 - Доза Паклитаксела 2
    101 - Винкристин 2
    102 - Доза Винкристина 2
    103 - Доксорубицин 2
    104 - Доза Доксорубицина 2
    105 - Ифосфамид 2
    106 - Доза Ифосфамида 2
    107 - Циклофосфан 2
    108 - Доза Циклофосфана 2
    109 - Гемцитабин 2
    110 - Доза Гемцитабина 2
    111 - Золедрон 2
    112 - Доза Золедрона 2
    113 - Алимта 2
    114 - Доза Алимты 2
    115 - Препарат 1_2
    116 - Препарат 2_2
    117 - Препарат 3_2
    118 - Линия Третья
    119 - Количество Курсов Третьей Линии
    120 - Навельбин 3
    121 - Доза Навельбина 3
    122 - Циклофосфан 3
    123 - Доза Циклофосфана 3
    124 - Винкристин 3
    125 - Доза Винкристина 3
    126 - Доксорубицин 3
    127 - Доза Доксорубицина 3
    128 - Гемцитабин 3
    129 - Доза Гемцитабина 3
    130 - Ломустин 3
    131 - Доза Ломустина 3
    132 - Месна 3
    133 - Доза Месны 3
    134 - Препарат 1_3
    135 - препарат 2_3
    136 - ПМО злокачественные
    137 - ПМО доброкачественные
    138 - ПервичноМножественныеОпухоли
    139 - Адрес
    140 - КонтактДата
    141 - ТелефонКонтакта
    142 - Рост (см)
    143 - Вес (кг)
    144 - VEGF 634
    145 - VEGF 2578
    146 - VEGF 936
    147 - EGF
    148 - GSTT
    149 - GSTM
    150 - GSTP
    151 - NATkpn
    152 - NATtag
    153 - NATbam
    154 - Тип ацетилирования
    155 - CYP_1A2
    156 - CYP_2D6
    157 - MDR
    158 - Продолжительность наблюдения (лет)
    159 - Возраст
    160 - EGFR_18_tumor
    161 - EGFR_18_norm
    162 - EGFR_19_tumor
    163 - EGFR_19_norm
    164 - EGFR_20_tumor
    165 - EGFR_20_norm
    166 - EGFR_21_tumor
    167 - EGFR_21_norm
    168 - EGFR_21_blood
    169 - TGF_509
    170 - TGF_25 codon
    171 - TGFR_206
    172 - KDR_1719
    173 - KDR_906
    174 - SULT1
    175 - MMP9_2660
    176 - MMP9_1562
    177 - MMP2_735
    178 - ММP2_1575
    179 - KRAS_2ex_tumor
    180 - KRAS_2ex_norm
    181 - PIK3CA_9ex
    182 - PIK3CA_20ex
    183 - PTEN
    184 - DNMT_149
    185 - DNMT_579
*/
}


