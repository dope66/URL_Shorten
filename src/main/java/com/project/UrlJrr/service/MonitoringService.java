package com.project.UrlJrr.service;

import com.project.UrlJrr.dto.MonitoringDto;
import com.project.UrlJrr.entity.Monitoring;
import com.project.UrlJrr.repository.MonitoringRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MonitoringService {
    private final MonitoringRepository monitoringRepository;

    public Monitoring register(MonitoringDto monitoringDto) {
        return monitoringRepository.save(monitoringDto.toEntity());
    }

    public List<Monitoring> findAll() {
        return monitoringRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }



    public List<Monitoring> findAllToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // 자정으로 시간 설정
        cal.set(Calendar.MINUTE, 0); // 시간당 분 설정
        cal.set(Calendar.SECOND, 0); // 분당 초 설정
        cal.set(Calendar.MILLISECOND, 0); // 초당 밀리초 설정

        Date startOfDay = cal.getTime(); // 오늘의 시작

        cal.add(Calendar.DAY_OF_MONTH, 1); // 다음 날의 시작을 위해 1일 추가
        Date nextDay = cal.getTime(); // 다음 날의 시작
        return monitoringRepository.findAllToday(startOfDay, nextDay);
    }
}
