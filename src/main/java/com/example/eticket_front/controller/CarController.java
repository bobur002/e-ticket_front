package com.example.eticket_front.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import uz.pdp.eticket_model.dto.receive.CarReceiveDTO;
import uz.pdp.eticket_model.dto.responce.ApiResponse;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("admin/car")
@RequiredArgsConstructor
public class CarController {
    private final RestTemplate restTemplate;
    String BASE_URL ="http://localhost:3399/admin/car/";

    @GetMapping("/add")
    public String addCar(){
        return "car/addCar";
    }
    @PostMapping("/add")
    public String addCar(
            Model model,
            @ModelAttribute CarReceiveDTO carReceiveDTO
    ){
        restTemplate.postForObject(BASE_URL + "/add",carReceiveDTO, ApiResponse.class);

        ApiResponse apiResponse = restTemplate.getForObject(BASE_URL + "/list", ApiResponse.class);
        List<CarReceiveDTO> cars = new ArrayList<>((List<CarReceiveDTO>) apiResponse.getData());
        model.addAttribute("cars",cars);
        return "car/carHome";
    }
    @GetMapping("/list")
    public String getCarList(Model model){
        ApiResponse apiResponse = restTemplate.getForObject(BASE_URL + "/list", ApiResponse.class);
        List<CarReceiveDTO> cars = new ArrayList<>((List<CarReceiveDTO>) apiResponse.getData());
        model.addAttribute("cars",cars);
        return "car/carHome";
    }
    @GetMapping("/edit")
    public String editCarList(
            Model model,
            @RequestParam("id") String id,
            @RequestParam("carType") String carType,
            @RequestParam("price") Double price,
            @RequestParam("seatCount") Integer seatCount
            ){
        CarReceiveDTO car = new CarReceiveDTO(id,carType,price,seatCount);
        model.addAttribute("car",car);

        return "car/carEdit";
    }
    @PostMapping("/edit/{id}")
    public String editCar(
            Model model,
            @ModelAttribute CarReceiveDTO carReceiveDTO,
            @PathVariable("id") String id
    ){
        carReceiveDTO.setId(id);
        ApiResponse apiResponse = restTemplate.postForObject(BASE_URL + "edit", carReceiveDTO, ApiResponse.class);
        apiResponse = restTemplate.getForObject(BASE_URL + "/list", ApiResponse.class);
        List<CarReceiveDTO> cars = new ArrayList<>((List<CarReceiveDTO>) apiResponse.getData());
        model.addAttribute("cars",cars);
        return "car/carHome";
    }

    @GetMapping("/delete")
    public String deleteCar(
            Model model,
            @RequestParam("id") String id
    ){
        ApiResponse apiResponse = restTemplate.getForObject(BASE_URL + "delete/"+id, ApiResponse.class);
        apiResponse = restTemplate.getForObject(BASE_URL + "/list", ApiResponse.class);
        List<CarReceiveDTO> cars = new ArrayList<>((List<CarReceiveDTO>) apiResponse.getData());
        model.addAttribute("cars",cars);
        return "car/carHome";
    }
}
