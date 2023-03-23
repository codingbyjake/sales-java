package com.maxtrain.bootcamp.sales.employee;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
	Optional<Employee> findByEmail(String email);
	Optional<Employee> findByEmailAndPassword(String email, String password);
	//Optional<Employee> employeeLogin(String email, String password);
}	
/*
public interface EmployeeRepository extends CrudRepository<Employee, String, String> {
		Optional<Employee> login(String email, String password);
}
*/

