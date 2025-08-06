package br.com.crm.beauty.web.repositories;

import br.com.crm.beauty.web.models.WorkingDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.DayOfWeek;
import java.util.List;

public interface WorkingDayRepository extends JpaRepository<WorkingDay, Long> {



    @Query("SELECT DISTINCT wd.dayOfWeek FROM WorkingDay wd WHERE wd.employee.id = ?1")
    List<DayOfWeek> findDistinctDaysByEmployeeId(Long employeeId);

}
