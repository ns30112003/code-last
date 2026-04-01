
package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final EmployeeRepository employeeRepository;
    private final PetRepository petRepository;

    public ScheduleService(ScheduleRepository scheduleRepository,
                           EmployeeRepository employeeRepository,
                           PetRepository petRepository) {
        this.scheduleRepository = scheduleRepository;
        this.employeeRepository = employeeRepository;
        this.petRepository = petRepository;
    }

    public Schedule createSchedule(Schedule schedule, List<Long> employeeIds, List<Long> petIds) {
        List<Employee> employees = employeeIds.stream()
                .map(id -> employeeRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Employee not found: " + id)))
                .collect(Collectors.toList());
        List<Pet> pets = petIds.stream()
                .map(id -> petRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Pet not found: " + id)))
                .collect(Collectors.toList());
        schedule.setEmployees(employees);
        schedule.setPets(pets);
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> getScheduleForPet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found: " + petId));
        return scheduleRepository.findByPetsContaining(pet);
    }

    public List<Schedule> getScheduleForEmployee(Long employeeId) {
        Employee emp = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + employeeId));
        return scheduleRepository.findByEmployeesContaining(emp);
    }

    public List<Schedule> getScheduleForCustomer(Long customerId) {
        List<Pet> customerPets = petRepository.findByOwnerId(customerId);
        return customerPets.stream()
                .flatMap(pet -> scheduleRepository.findByPetsContaining(pet).stream())
                .distinct()
                .collect(Collectors.toList());
    }
}
