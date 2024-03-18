package com.project.barberShop.services.Impl;

import com.project.barberShop.dto.AppointmentDto;
import com.project.barberShop.dto.DetailedAppointmentDto;
import com.project.barberShop.exceptions.ConflictException;
import com.project.barberShop.exceptions.ResourceNotFoundException;
import com.project.barberShop.exceptions.ValidationException;
import com.project.barberShop.models.Appointment;
import com.project.barberShop.models.BarberService;
import com.project.barberShop.models.EBarberService;
import com.project.barberShop.models.User;
import com.project.barberShop.repositories.AppointmentRepository;
import com.project.barberShop.repositories.BarberServiceRepository;
import com.project.barberShop.requestresponse.AvailableSlotsRequest;
import com.project.barberShop.services.AppointmentService;
import com.project.barberShop.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private BarberServiceRepository barberServiceRepository;
    @Autowired
    private UserService userService;

    @Override
    public Appointment createAppointment(AppointmentDto appointmentDto) {
        User user = userService.getCurrentAuthenticatedUser();
        validateAppointmentConstraints(appointmentDto);
        EBarberService eBarberService = EBarberService.valueOf(appointmentDto.getService());

        BarberService barberService = barberServiceRepository.findByServiceName(eBarberService)
                .orElseThrow(() -> new RuntimeException("Service type not found"));

        Set<BarberService> services = new HashSet<>();
        services.add(barberService);

        Appointment appointment = Appointment.builder()
                .date(appointmentDto.getDate())
                .time(appointmentDto.getTime())
                .service(services)
                .user(user)
                .build();

        return appointmentRepository.save(appointment);
    }

    @Override
    public List<LocalTime> getAvailableSlotsForDateAndService(AvailableSlotsRequest availableSlotsRequest) {
        List<LocalTime> allSlots = generateTimeSlotsForDate(
                availableSlotsRequest.getDate(), availableSlotsRequest.geteBarberService());

        if (availableSlotsRequest.geteBarberService() == EBarberService.HAIR_AND_BEARD) {
            List<LocalTime> filteredSlots = new ArrayList<>();

            for (int i = 0; i < allSlots.size() - 1; i++) {
                LocalTime firstSlot = allSlots.get(i);
                LocalTime secondSlot = allSlots.get(i + 1);

                if (secondSlot.equals(firstSlot.plusMinutes(30))
                        && isTimeAvailableForAppointment(availableSlotsRequest.getDate(),
                        firstSlot, EBarberService.HAIR) && isTimeAvailableForAppointment(availableSlotsRequest.getDate(),
                        secondSlot, EBarberService.BEARD)) {
                    filteredSlots.add(firstSlot);
                }
            }
            if (isTimeAvailableForAppointment(availableSlotsRequest.getDate(), LocalTime.of(18, 30),
                    EBarberService.BEARD)) {
                filteredSlots.add(LocalTime.of(18, 0));
            }
            return filteredSlots;
        } else {
            return allSlots.stream()
                    .filter(slot -> isTimeAvailableForAppointment(
                            availableSlotsRequest.getDate(), slot, availableSlotsRequest.geteBarberService()))
                    .collect(Collectors.toList());
        }
    }

    private List<LocalTime> generateTimeSlotsForDate(LocalDate date, EBarberService service) {
        List<LocalTime> slots = new ArrayList<>();
        LocalTime startTime = LocalTime.of(9, 0);

        int serviceDuration = getServiceDuration(service);
        LocalTime endTime = service == EBarberService.HAIR_AND_BEARD ? LocalTime.of(18, 0)
                : LocalTime.of(19, 0).minusMinutes(serviceDuration);

        while (!startTime.isAfter(endTime)) {
            slots.add(startTime);
            startTime = startTime.plusMinutes(30);
        }

        if (date.equals(LocalDate.now())) {
            LocalTime now = LocalTime.now();
            LocalDateTime curr = LocalDateTime.of(date, now);
            slots = slots.stream()
                    .filter(slot -> slot.atDate(date).isAfter(curr))
                    .collect(Collectors.toList());
        }

        return slots;
    }

    private boolean isTimeAvailableForAppointment(LocalDate date, LocalTime time, EBarberService service) {
        List<Appointment> appointments = appointmentRepository.findByDate(date);

        for (Appointment appointment : appointments) {
            if (doesAppointmentOverlap(appointment, time, service)) {
                return false;
            }
        }

        return true;
    }

    private boolean isOverlappingAppointment(AppointmentDto appointmentDto) {
        EBarberService eBarberService = EBarberService.valueOf(appointmentDto.getService());
        BarberService barberService = barberServiceRepository.findByServiceName(eBarberService)
                .orElseThrow(() -> new RuntimeException("Service type not found"));
        List<Appointment> appointments = appointmentRepository.findByDateAndService(
                appointmentDto.getDate(), barberService);

        if (eBarberService == EBarberService.HAIR_AND_BEARD) {
            return appointments.stream().anyMatch(appointment -> doesAppointmentOverlap(appointment, appointmentDto));
        }

        return appointments.stream().anyMatch(
                appointment -> doesAppointmentOverlap(appointment, appointmentDto.getTime(), eBarberService));

    }

    @Override
    public List<AppointmentDto> getAppointmentsForUser(User user) {
        List<Appointment> appointments = appointmentRepository.findByUser(user);
        if (appointments.isEmpty()) {
            log.debug("This user has no appointments yet");
            return null;
        }
        return appointments.stream().sorted(Comparator.comparing(Appointment::getDate).reversed()
                        .thenComparing(Comparator.comparing(Appointment::getTime)
                                .reversed())).map(this::convertToBasicDto)
                .collect(Collectors.toList());
    }

    private AppointmentDto convertToBasicDto(Appointment appointment) {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(appointment.getId());
        appointmentDto.setDate(appointment.getDate());
        appointmentDto.setTime(appointment.getTime());
        appointment.getService().stream().findFirst().ifPresent(
                firstService -> appointmentDto.setService(firstService.getServiceName().name()));
        return appointmentDto;
    }

    @Override
    public List<DetailedAppointmentDto> getAllAppointmentsForDate(LocalDate date) {
        List<Appointment> appointments = appointmentRepository.findByDate(date);
        if (appointments == null || appointments.isEmpty()) {
            log.debug("No appointments for selected date");
            return null;
        }
        appointments.sort(Comparator.comparing(Appointment::getDate).reversed().thenComparing(Appointment::getTime));
        return appointments.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private DetailedAppointmentDto convertToDto(Appointment appointment) {
        DetailedAppointmentDto appointmentDto = new DetailedAppointmentDto();
        appointmentDto.setUserId(appointment.getUser().getId());
        appointmentDto.setId(appointment.getId());
        appointmentDto.setDate(appointment.getDate());
        appointmentDto.setTime(appointment.getTime());
        appointment.getService().stream().findFirst().ifPresent(
                firstService -> appointmentDto.setService(firstService.getServiceName().name()));
        appointmentDto.setFirstName(appointment.getUser().getFirstName());
        appointmentDto.setLastName(appointment.getUser().getLastName());
        appointmentDto.setPhoneNumber(appointment.getUser().getPhoneNumber());
        return appointmentDto;
    }

    private void validateAppointmentConstraints(AppointmentDto appointmentDto) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        if (hasAppointmentOnDate(currentUser, appointmentDto.getDate())) {
            throw new ConflictException("Only one appointment per day.");
        }
        if (isOverlappingAppointment(appointmentDto)) {
            throw new ConflictException("An overlapping appointment has already been made");
        }
        if (appointmentDto.getDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new ValidationException("Sunday is closed");
        }
        if (appointmentDto.getDate().isAfter(LocalDate.now().plusMonths(1))) {
            throw new ValidationException("Appointments cannot be made more than one month in advance");
        }
    }

    private boolean hasAppointmentOnDate(User user, LocalDate date) {
        List<Appointment> appointmentsOnDate =
                appointmentRepository.findByUserAndDate(user, date);
        return !appointmentsOnDate.isEmpty();
    }

    @Override
    public void cancelAppointmentByUser(Long appointmentId) {
        User currentUser = userService.getCurrentAuthenticatedUser();
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        if (!appointment.getUser().equals(currentUser)) {
            throw new ValidationException("You can only cancel your own appointments");
        }

        LocalDateTime appointmentDateTime = LocalDateTime.of(appointment.getDate(), appointment.getTime());
        LocalDateTime now = LocalDateTime.now();

        if (appointmentDateTime.isBefore(now)) {
            throw new ConflictException("Past appointment can't be canceled.");
        }

        if (appointmentDateTime.isBefore(now.plusHours(2))) {
            throw new ConflictException("You can't change a appointment if there is less than 2 hours.");
        }
        log.debug("Appointment canceled successfully");
        appointmentRepository.delete(appointment);
    }

    @Override
    public void cancelAppointmentByAdmin(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        LocalDateTime appointmentDateTime = LocalDateTime.of(appointment.getDate(), appointment.getTime());
        LocalDateTime now = LocalDateTime.now();
        if (appointmentDateTime.isBefore(now)) {
            throw new ConflictException("Past appointment can't be canceled.");
        }
        appointmentRepository.delete(appointment);
    }

    private boolean doesAppointmentOverlap(Appointment appointment, AppointmentDto newAppointment) {
        LocalTime appointmentStartTime = appointment.getTime();
        LocalTime appointmentEndTime = appointmentStartTime.plusMinutes(getServiceDuration(appointment.getService().iterator().next().getServiceName()));
        LocalTime newAppointmentStartTime = newAppointment.getTime();
        EBarberService eBarberService =
                EBarberService.valueOf(newAppointment.getService());
        LocalTime newAppointmentEndTime =
                newAppointmentStartTime.plusMinutes(getServiceDuration(eBarberService));
        return newAppointmentStartTime.isBefore(appointmentEndTime)
                && newAppointmentEndTime.isAfter(appointmentStartTime);
    }

    private boolean doesAppointmentOverlap(Appointment existingAppointment, LocalTime slot, EBarberService service) {
        LocalTime appointmentStartTime = existingAppointment.getTime();
        LocalTime appointmentEndTime = appointmentStartTime.plusMinutes(
                getServiceDuration(existingAppointment.getService().iterator().next().getServiceName()));
        LocalTime slotEndTime = slot.plusMinutes(getServiceDuration(service));
        return slot.isBefore(appointmentEndTime) && slotEndTime.isAfter(appointmentStartTime);
    }

    private int getServiceDuration(EBarberService service) {
        return switch (service) {
            case HAIR, BEARD -> 30;
            case HAIR_AND_BEARD -> 60;
        };
    }
}
