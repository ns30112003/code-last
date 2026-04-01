//
package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final PetRepository petRepository;

    public UserService(CustomerRepository customerRepository,
                       EmployeeRepository employeeRepository,
                       PetRepository petRepository) {
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.petRepository = petRepository;
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerByPetId(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found: " + petId));
        return pet.getOwner();
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + employeeId));
    }

    public void setAvailability(Set<DayOfWeek> daysAvailable, Long employeeId) {
        Employee employee = getEmployee(employeeId);
        employee.setDaysAvailable(daysAvailable);
    }

    public List<Employee> findEmployeesForService(Set<EmployeeSkill> skills, DayOfWeek day) {
        return employeeRepository.findByDaysAvailableContaining(day)
                .stream()
                .filter(e -> e.getSkills().containsAll(skills))
                .collect(Collectors.toList());
    }
}
