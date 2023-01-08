package edu.dhbw_ka.webeng.biletado_backend.rest;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class Status {
    List<String> authors = Lists.newArrayList("Johannes Vater", "Fabian Droll");
    String apiVersion = "0.0.0";
}
