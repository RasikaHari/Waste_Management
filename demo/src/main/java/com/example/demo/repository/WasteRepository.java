package com.example.demo.repository;

import com.example.demo.model.Waste;
import com.example.demo.model.WasteStatus;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WasteRepository extends JpaRepository<Waste, Long> {

    List<Waste> findByUser(User user);

    List<Waste> findByStatus(WasteStatus status);
}