package com.project.login.controllers.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.sql.Time;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Date;
@Data
@Entity
@Table(name = "medicine_details", uniqueConstraints = {@UniqueConstraint(columnNames = "PrescriptionID")})
public class MedicineDetails {
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private long prescriptionID;
            @Column(name = "user_name")
            private String userName;

            @Column(name = "medicine_name")
            private String medicineName;

            @Column(name = "stock")
            private Integer stock;

            @Column(name = "expiry_date")
            private LocalDate expiryDate;

            @Column(name = "med_start_date")
            private LocalDate medStartDate;

            @Column(name = "med_end_date")
            private LocalDate medEndDate;

            @Column(name = "reminder_time")
            private String reminderTime;
            @Column(name="active_status")
            private int activeStatus;

            @ElementCollection
            @CollectionTable(name = "medicine_details_frequency", joinColumns = @JoinColumn(name = "medication_id"))
            @Column(name = "frequency")
            private List<String> frequency;

            @ElementCollection
            @CollectionTable(name = "medicine_details_time_of_day", joinColumns = @JoinColumn(name = "medication_id"))
            @Column(name = "time_of_day")
            private List<String> timeOfDay;

            @Column(name = "morning")
            private String morning;
            @Column(name = "afternoon")
            private String afternoon;
            @Column(name = "evening")
            private String evening;
            @Column(name = "night")
            private String night;
}
